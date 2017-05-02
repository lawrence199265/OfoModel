package com.zhuangbudong.ofo.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.zhuangbudong.ofo.R;

/**
 * Created by xxx on 17/3/28.
 */

public class ChooseDialogFragment extends DialogFragment implements View.OnClickListener {
    private ListView lsSingle;
    public static final String ARGUMENTS_DATA = "data";
    public static final String ARGUMENTS_CHECKED_ID = "checked";
    private String[] data;
    private int checkedId = 0;
    private Button btnCommit;
    private OnDialogPickedListener onDialogPickedListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_commit:
                onDialogPickedListener.onPicked(lsSingle.getCheckedItemPosition());
                dismiss();
                break;
        }

    }

    public void setOnDialogPickedListener(OnDialogPickedListener onDialogPickedListener) {
        this.onDialogPickedListener = onDialogPickedListener;
    }


    public interface OnDialogPickedListener {
        void onPicked(int checkedId);
    }


    public static ChooseDialogFragment newInstance(String[] data, int checkedId) {

        Bundle args = new Bundle();

        ChooseDialogFragment fragment = new ChooseDialogFragment();
        args.putStringArray(ARGUMENTS_DATA, data);
        args.putInt(ARGUMENTS_CHECKED_ID, checkedId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        View view = inflater.inflate(R.layout.dialog_single_choose, container, false);
        lsSingle = (ListView) view.findViewById(R.id.ls_sing_choose);
        btnCommit = (Button) view.findViewById(R.id.dialog_btn_commit);
        btnCommit.setOnClickListener(this);
        lsSingle.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lsSingle.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, data));
        lsSingle.setItemChecked(checkedId, true);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);//设置无标题、透明背景、无边框等等属性
        setCancelable(true);//设置点击除了对话框以外的部分就关闭
        data = getArguments().getStringArray(ARGUMENTS_DATA);
        checkedId = getArguments().getInt(ARGUMENTS_CHECKED_ID);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setWindowAnimations(R.style.style_item);

    }
}

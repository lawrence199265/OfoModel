package com.zhuangbudong.ofo.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.event.BusAction;
import com.zhuangbudong.ofo.utils.PriceFormatUtil;

/**
 * Created by xxx on 17/3/28.
 */

public class InputDialogFragment extends DialogFragment implements View.OnClickListener {
    private EditText paramEditText;
    private static final String TYPE_DIALOG = "type";
    public static final int TYPE_DEPOSIT = 0;
    public static final int TYPE_PROVIDE = 1;
    public static final int TYPE_DEMAND = 2;

    private static final String INIT_PARAM = "initParam";
    private int type;
    private TextView tvSend;
    private String initParam;

    public static InputDialogFragment newInstance(int type, String initParam) {

        Bundle args = new Bundle();
        args.putInt(TYPE_DIALOG, type);
        args.putString(INIT_PARAM, initParam);
        InputDialogFragment fragment = new InputDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        View view = inflater.inflate(R.layout.fragment_input_dialog, container, false);
        paramEditText = (EditText) view.findViewById(R.id.dialog_edit_text);

        if (type == TYPE_DEPOSIT) {
            paramEditText.setKeyListener(DigitsKeyListener.getInstance("01234567890."));
        }

        tvSend = (TextView) view.findViewById(R.id.dialog_send);
        tvSend.setOnClickListener(this);
        paramEditText.requestFocus();
        paramEditText.setText(initParam);
        paramEditText.setSelection(initParam.length());

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);//设置无标题、透明背景、无边框等等属性
        setCancelable(true);//设置点击除了对话框以外的部分就关闭
        initData();

    }

    private void initData() {
        type = getArguments().getInt(TYPE_DIALOG);
        initParam = getArguments().getString(INIT_PARAM);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        super.onDismiss(dialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setWindowAnimations(R.style.style_item);
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_send:
                switch (type) {
                    case TYPE_DEPOSIT:
                        RxBus.get().post(BusAction.TAG_INPUT_DISPOSE, PriceFormatUtil.reduceDouble(Float.parseFloat(paramEditText.getText().toString())));
                        break;
                    case TYPE_PROVIDE:
                        RxBus.get().post(BusAction.TAG_INPUT_PROVIDE, paramEditText.getText().toString());
                        break;
                    case TYPE_DEMAND:
                        RxBus.get().post(BusAction.TAG_INPUT_DEMAND, paramEditText.getText().toString());
                        break;
                }
                dismiss();
                break;
        }
    }
}

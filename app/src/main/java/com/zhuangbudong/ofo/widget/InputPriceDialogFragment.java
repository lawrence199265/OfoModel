package com.zhuangbudong.ofo.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.event.InputPriceEvent;
import com.zhuangbudong.ofo.utils.PriceFormatUtil;

import java.text.DecimalFormat;

/**
 * Created by xxx on 17/3/28.
 */

public class InputPriceDialogFragment extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText paramEditText;
    private TextView tvDialogSend;
    private RadioGroup rgLabel;
    private int checkId = 0;
    private View view;
    private InputPriceEvent inputPriceEvent = new InputPriceEvent();
    private static final String ARGUMENT_INPUT = "argument_input";
    private static final String ARGUMENT_CHECK_INDEX = "argument_checkId";


    public static InputPriceDialogFragment newInstance(String input, int checkIndex) {

        Bundle args = new Bundle();
        args.putString(ARGUMENT_INPUT, input);
        args.putInt(ARGUMENT_CHECK_INDEX, checkIndex);

        InputPriceDialogFragment fragment = new InputPriceDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        view = inflater.inflate(R.layout.fragment_input_price_dialog, container, false);
        paramEditText = (EditText) view.findViewById(R.id.dialog_edit_text);
        tvDialogSend = (TextView) view.findViewById(R.id.dialog_send);
        rgLabel = (RadioGroup) view.findViewById(R.id.rg_check_label);
        tvDialogSend.setOnClickListener(this);
        paramEditText.requestFocus();
        rgLabel.setOnCheckedChangeListener(this);
        String input = getArguments().getString(ARGUMENT_INPUT);
        paramEditText.setText(input);
        paramEditText.setSelection(input.length());
        checkId = getArguments().getInt(ARGUMENT_CHECK_INDEX);
        rgLabel.check(rgLabel.getChildAt(checkId).getId());
        paramEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && Float.parseFloat(s.toString().trim()) < 5) {
                    paramEditText.setText(String.valueOf(5));
                    paramEditText.setSelection(1);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);//设置无标题、透明背景、无边框等等属性
        setCancelable(true);//设置点击除了对话框以外的部分就关闭
        RxBus.get().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
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
                inputPriceEvent.input(PriceFormatUtil.reduceDouble(Double.parseDouble(paramEditText.getText().toString().trim())), checkId);
                dismiss();
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        checkId = group.indexOfChild(view.findViewById(checkedId));
    }
}

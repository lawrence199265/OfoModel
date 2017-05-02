package com.zhuangbudong.ofo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

/**
 * Created by xxx on 17/3/27.
 */

public class CalculateTextLengthUtil {
    private TextView updateText;
    private int sum;
    private EditText inputText;

    public void updateTextCount(TextView updateText, int sum, EditText inputText) {

        this.updateText = updateText;
        this.sum = sum;
        this.inputText = inputText;
        this.inputText.addTextChangedListener(textLengthRecord);
    }

    private TextWatcher textLengthRecord = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                updateTextLength();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private void updateTextLength() throws UnsupportedEncodingException {
        String gbkText = inputText.getText().toString();
        int remainCount = sum - gbkText.getBytes("GBK").length;
        updateText.setText(remainCount + "/" + sum);
        if (remainCount < 0) {
            int index = inputText.getText().toString().length();
            inputText.getEditableText().delete(index - 1, index);
        }
    }
}



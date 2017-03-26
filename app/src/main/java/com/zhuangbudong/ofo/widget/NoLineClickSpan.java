package com.zhuangbudong.ofo.widget;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NoLineClickSpan extends ClickableSpan {
    String text;

    public NoLineClickSpan(String text) {
        super();
        this.text = text;
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false); //去掉下划线
    }


}
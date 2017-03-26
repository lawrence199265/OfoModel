package com.zhuangbudong.ofo.widget;

import android.support.annotation.IdRes;

public class TabEntity {
    @IdRes
    private int iconResId;
    private String title;

    public TabEntity(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "title:" + title + ",id:" + iconResId;
    }
}
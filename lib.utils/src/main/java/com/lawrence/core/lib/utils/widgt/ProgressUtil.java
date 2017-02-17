package com.lawrence.core.lib.utils.widgt;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by wangxu on 17/2/14.
 */
public class ProgressUtil extends ProgressDialog {

//    private static Context context;

    private static ProgressUtil ourInstance;

    public static ProgressUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ProgressUtil(context);
        }
        return ourInstance;
    }

    private ProgressUtil(Context context) {
        super(context);
    }


    public void show(String title, String msg, boolean cancel) {
        setTitle(title);
        setMessage(msg);
        setCancelable(cancel);
    }

    public void show(String title, String msg, boolean cancel, OnCancelListener cancelListener) {
        setTitle(title);
        setMessage(msg);
        setCancelable(cancel);
        setOnCancelListener(cancelListener);
    }

    public void dissmiss() {
        dismiss();
    }
}

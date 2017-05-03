package com.zhuangbudong.ofo.utils;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class DialogUtil {

    //android.support.v7.app.

    public static Context context;


    private static DialogInterface loadingDialog;//缓存加载中的dialog,便于以后可以不需要对象就让它消失


    public static void init(Context context) {
        DialogUtil.context = context;
    }

    public static void setLoadingObj(DialogInterface loading) {
        dismiss(loadingDialog);
        loadingDialog = loading;
    }


    /**
     * 一键让loading消失.
     */
    public static void dismissLoading() {
        if (loadingDialog != null) {
            dismiss(loadingDialog);
            loadingDialog = null;
        }
    }


    public static void dismiss(DialogInterface... dialogs) {
        if (dialogs != null && dialogs.length > 0) {
            for (DialogInterface dialog : dialogs) {
                if (dialog instanceof Dialog) {
                    Dialog dialog1 = (Dialog) dialog;
                    if (dialog1.isShowing()) {
                        dialog1.dismiss();
                    }
                } else if (dialog instanceof AppCompatDialog) {
                    AppCompatDialog dialog2 = (AppCompatDialog) dialog;
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                }
            }

        }
    }

    public static Dialog buildLoading(CharSequence title, String msg) {
        return DialogAssigner.getInstance().assignLoading(context, msg, title, false, false);
    }


    private static class DialogAssigner {

        private static DialogAssigner instance;

        private DialogAssigner() {

        }

        public static DialogAssigner getInstance() {
            if (instance == null) {
                instance = new DialogAssigner();
            }
            return instance;
        }


        public Dialog assignLoading(Context context, CharSequence msg, CharSequence title, boolean cancelable, boolean outsideTouchable) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(outsideTouchable);
            progressDialog.setCancelable(cancelable);
            setLoadingObj(progressDialog);
            return progressDialog;
        }

    }
}

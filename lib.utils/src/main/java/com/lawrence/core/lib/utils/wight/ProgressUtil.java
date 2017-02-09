package com.lawrence.core.lib.utils.wight;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by wangxu on 16/11/11.
 */

public class ProgressUtil extends ProgressDialog {

    private Context context;

    public ProgressUtil(Context context) {
        super(context);
        this.context = context;
    }


}

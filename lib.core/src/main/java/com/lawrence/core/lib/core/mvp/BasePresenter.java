package com.lawrence.core.lib.core.mvp;

import android.content.Context;
import android.util.Log;

/**
 * Created by wangxu on 17/1/11.
 */

public abstract class BasePresenter<T extends IBaseView> {

    public static final String TAG = "BasePresenter";
    public T iView;
    public Context context;


    public BasePresenter(T iView, Context context) {
        this.iView = iView;
        this.context = context;
    }

    void init() {
        iView.initView();
        Log.d(TAG, "init: finished");
    }
}

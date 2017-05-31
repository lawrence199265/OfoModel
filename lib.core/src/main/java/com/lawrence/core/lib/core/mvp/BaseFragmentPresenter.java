package com.lawrence.core.lib.core.mvp;

import android.content.Context;
import android.view.View;

/**
 * Created by xxx on 17/5/12.
 */

public class BaseFragmentPresenter<T extends IBaseFragmentView>  {
    protected T iView;
    protected Context context;


    public BaseFragmentPresenter(T iView, Context context) {
        this.iView = iView;
        this.context = context;
    }

    void init(View view) {
        iView.initView(view);
    }
}

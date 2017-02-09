package com.lawrence.core.lib.core.mvp;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by wangxu on 17/1/11.
 */

public abstract class BaseActivity<T extends BasePresenter> extends Activity implements IBaseView {

    public static final String TAG = "BaseActivity";

    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initPresenter();
        init();
    }

    private void init() {
        if (presenter == null) {
            throw new NullPointerException("初始化 presenter 失败");
        }
        presenter.init();
    }

    protected abstract void initPresenter();

    protected abstract int getLayoutId();
}

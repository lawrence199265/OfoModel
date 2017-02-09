package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.view.Window;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.ILoadingActivity;
import com.zhuangbudong.ofo.presenter.LoadingPresenter;

/**
 * Created by wangxu on 17/2/8.
 */

public class LoadingActivity extends BaseActivity<LoadingPresenter> implements ILoadingActivity {

    @Override
    protected void initPresenter() {
        presenter = new LoadingPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return R.layout.activity_loading;
    }

    @Override
    public void initView() {
        presenter.copyResource();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSnack(String msg) {

    }

    @Override
    public void success() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}

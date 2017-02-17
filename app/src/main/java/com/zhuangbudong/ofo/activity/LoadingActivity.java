package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.ILoadingActivity;
import com.zhuangbudong.ofo.presenter.LoadingPresenter;
import com.zhuangbudong.ofo.widget.indicator.CircleIndicator;

/**
 * Created by wangxu on 17/2/8.
 */

public class LoadingActivity extends BaseActivity<LoadingPresenter> implements ILoadingActivity {

    private CircleIndicator indicators;
    private ViewPager vpLoading;

    @Override
    protected void initPresenter() {
        presenter = new LoadingPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    public void initView() {
        presenter.copyResource();
        indicators = (CircleIndicator) findViewById(R.id.indicators);
        vpLoading = (ViewPager) findViewById(R.id.vp_loading);
        indicators.setViewPager(vpLoading);
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
        this.finish();
    }
}

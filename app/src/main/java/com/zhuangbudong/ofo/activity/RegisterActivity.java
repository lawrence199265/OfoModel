package com.zhuangbudong.ofo.activity;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.IRegisterActivity;
import com.zhuangbudong.ofo.presenter.RegisterPresenter;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterActivity {


    @Override
    protected void initPresenter() {
        presenter = new RegisterPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSnack(String msg) {

    }
}

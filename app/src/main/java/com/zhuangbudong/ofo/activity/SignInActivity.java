package com.zhuangbudong.ofo.activity;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.ISignInActivity;
import com.zhuangbudong.ofo.presenter.SignInPresenter;

public class SignInActivity extends BaseActivity<SignInPresenter> implements ISignInActivity {


    @Override
    protected void initPresenter() {
        presenter = new SignInPresenter(this,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin;
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

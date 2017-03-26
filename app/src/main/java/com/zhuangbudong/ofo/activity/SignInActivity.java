package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.ISignInActivity;
import com.zhuangbudong.ofo.presenter.SignInPresenter;
import com.zhuangbudong.ofo.widget.XEditText;

public class SignInActivity extends BaseActivity<SignInPresenter> implements ISignInActivity, View.OnClickListener {

    public static final String EXTRA_SIGN_OK = "signOK";
    private XEditText pwdEdit;
    private EditText userEdit;
    private Button btnSign, btnRegister;

    @Override
    protected void initPresenter() {
        presenter = new SignInPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    public void initView() {
        pwdEdit = (XEditText) findViewById(R.id.et_password);
        userEdit = (EditText) findViewById(R.id.et_user_name);
        btnSign = (Button) findViewById(R.id.btn_sign);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnSign.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSnack(String msg) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SIGN_OK, true);
                setResult(RESULT_OK, intent);
                finish();

                break;
            case R.id.btn_register:
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }
}

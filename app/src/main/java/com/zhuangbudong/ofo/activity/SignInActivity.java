package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.ISignInActivity;
import com.zhuangbudong.ofo.event.LoginEvent;
import com.zhuangbudong.ofo.presenter.SignInPresenter;
import com.zhuangbudong.ofo.utils.DialogUtil;
import com.zhuangbudong.ofo.utils.PrefsUtils;
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
        userEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pwdEdit.setText(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        DialogUtil.init(this);

    }

    @Override
    public void showToast(String msg) {
        Log.d(TAG, "showToast: " + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnack(String msg) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign:
                presenter.signIn(userEdit.getText().toString(), pwdEdit.getText().toString());
//                startIntent();
                break;
            case R.id.btn_register:
                startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {
        DialogUtil.buildLoading("提示", "登录中").show();
    }

    @Override
    public void dismissLoading() {
        DialogUtil.dismissLoading();
    }

    @Override
    public void startIntent() {
        PrefsUtils.savePrefBoolean(this, EXTRA_SIGN_OK, true);
        RxBus.get().post(new LoginEvent(LoginEvent.LOGIN));
        finish();
    }
}

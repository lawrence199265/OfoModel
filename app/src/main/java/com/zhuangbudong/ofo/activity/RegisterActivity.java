package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.IRegisterActivity;
import com.zhuangbudong.ofo.presenter.RegisterPresenter;
import com.zhuangbudong.ofo.widget.NoLineClickSpan;


import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterActivity, View.OnClickListener {
    private RelativeLayout rlContainer, rlMatch;
    private Toolbar tlBar;
    private TextView tvJumpToLogin;

    private EditText etPwd, etPwdAgain, etUserName;

    private Button btnRegister;


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
        rlContainer = (RelativeLayout) findViewById(R.id.register_rl_container);
        rlMatch = (RelativeLayout) findViewById(R.id.rl_match_layout);
        tlBar = (Toolbar) findViewById(R.id.register_tl_bar);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle(R.string.label_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvJumpToLogin = (TextView) findViewById(R.id.register_jump_login);
        etPwd = (EditText) findViewById(R.id.et_password);
        etPwdAgain = (EditText) findViewById(R.id.et_password_again);
        etUserName = (EditText) findViewById(R.id.register_user_name);

        handleJumpText();

        handlePassword();


    }

    private void handlePassword() {
        Observable<CharSequence> pwdObservable = RxTextView.textChanges(etPwd);
        Observable<CharSequence> pwdAgainObservable = RxTextView.textChanges(etPwdAgain);
        Observable.combineLatest(pwdObservable, pwdAgainObservable, new BiFunction<CharSequence, CharSequence, Boolean>() {

            @Override
            public Boolean apply(@NonNull CharSequence pwd, @NonNull CharSequence newPwd) throws Exception {
                if (newPwd.toString().trim().length() == 0) {
                    return true;
                }
                return pwd.toString().trim().equals(newPwd.toString().trim());
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    etPwdAgain.setError("两次密码不一致");
                } else {
                    etPwdAgain.setError(null);
                }
            }
        });
    }

    private void handleJumpText() {
        String handleText = tvJumpToLogin.getText().toString();
        SpannableString spannableString = new SpannableString(handleText);
        spannableString.setSpan(new NoLineClickSpan(handleText) {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                finish();

            }
        }, 6, handleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.main_color)), 6, handleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvJumpToLogin.setText(spannableString);
        tvJumpToLogin.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnack(String msg) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                presenter.registerUser(etUserName.getText().toString(), etPwd.getText().toString());
                showToast("注册成功");
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
        }
    }
}

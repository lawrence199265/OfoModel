package com.zhuangbudong.ofo.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.IRegisterActivity;
import com.zhuangbudong.ofo.presenter.RegisterPresenter;
import com.zhuangbudong.ofo.widget.NoLineClickSpan;
import com.zhuangbudong.ofo.widget.SwipeCaptchaView;
import com.zhuangbudong.ofo.widget.SwipeCaptchaView.OnCaptchaMatchCallback;

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements IRegisterActivity, SeekBar.OnSeekBarChangeListener, OnCaptchaMatchCallback {
    private SwipeCaptchaView captchaView;
    private SeekBar seekBar;
    private RelativeLayout rlContainer, rlMatch;
    private Toolbar tlBar;
    private TextView tvJumpToLogin;

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
        captchaView = (SwipeCaptchaView) findViewById(R.id.register_captcha_view);
        seekBar = (SeekBar) findViewById(R.id.register_drag_bar);
        rlContainer = (RelativeLayout) findViewById(R.id.register_rl_container);
        rlMatch = (RelativeLayout) findViewById(R.id.rl_match_layout);
        tlBar = (Toolbar) findViewById(R.id.register_tl_bar);
        captchaView.setOnCaptchaMatchCallback(this);
        seekBar.setOnSeekBarChangeListener(this);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle(R.string.label_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvJumpToLogin = (TextView) findViewById(R.id.register_jump_login);

        handleJumpText();


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

    }

    @Override
    public void showSnack(String msg) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        captchaView.setCurrentSwipeValue(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBar.setMax(captchaView.getMaxSwipeValue());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        captchaView.matchCaptcha();
    }

    @Override
    public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
        Toast.makeText(RegisterActivity.this, "恭喜你啊,验证成功", Toast.LENGTH_SHORT).show();
        seekBar.setEnabled(false);
        rlMatch.setVisibility(View.GONE);
        rlContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
        Toast.makeText(RegisterActivity.this, "你有80%的可能是机器人，现在走还来得及", Toast.LENGTH_SHORT).show();
        swipeCaptchaView.resetCaptcha();
        seekBar.setProgress(0);
    }
}

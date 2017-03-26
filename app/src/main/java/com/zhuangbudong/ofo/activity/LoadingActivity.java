package com.zhuangbudong.ofo.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.ILoadingActivity;
import com.zhuangbudong.ofo.adpter.LoadingAdapter;
import com.zhuangbudong.ofo.presenter.LoadingPresenter;
import com.zhuangbudong.ofo.utils.Constants;
import com.zhuangbudong.ofo.utils.PrefsUtils;
import com.zhuangbudong.ofo.widget.OnPageClickListener;
import com.zhuangbudong.ofo.widget.indicator.CircleIndicator;

/**
 * Created by wangxu on 17/2/8.
 */

public class LoadingActivity extends BaseActivity<LoadingPresenter> implements ILoadingActivity, OnPageClickListener {

    private CircleIndicator indicators;
    private ViewPager vpLoading;
    private int[] imageList = new int[]{R.drawable.splash_1, R.drawable.splash_2, R.drawable.splash_3, R.drawable.splash_4, R.drawable.splash_5};
    private boolean isScrollEnd;
    private LoadingAdapter loadingAdapter;
    private ImageView ivStart;
    private RelativeLayout rlContent;
    private CountDownTimer countDownTimer;
    private TextView tvCountDown;


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
        LayoutTransition transition = new LayoutTransition();
        rlContent = (RelativeLayout) findViewById(R.id.content);
        rlContent.setLayoutTransition(transition);


        tvCountDown = (TextView) findViewById(R.id.loading_tv_count);
        ivStart = (ImageView) findViewById(R.id.loading_iv_start);
        indicators = (CircleIndicator) findViewById(R.id.indicators);


        vpLoading = (ViewPager) findViewById(R.id.vp_loading);
        loadingAdapter = new LoadingAdapter(this, imageList);
        vpLoading.setAdapter(loadingAdapter);
        loadingAdapter.setOnPageClickListener(this);
        indicators.setViewPager(vpLoading);
        handleViewpager();


        startCountDown();

    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText((int) (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {
                if (PrefsUtils.loadPrefBoolean(LoadingActivity.this, Constants.INDICATOR_SHOW, false)) {
                    startActivity();
                } else {
                    PrefsUtils.savePrefBoolean(LoadingActivity.this, Constants.INDICATOR_SHOW, true);
                    ivStart.setVisibility(View.GONE);
                    vpLoading.setVisibility(View.VISIBLE);
                    indicators.setVisibility(View.VISIBLE);
                }
            }
        };
        countDownTimer.start();
    }

    private void handleViewpager() {
        vpLoading.addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == imageList.length - 1) {
                    indicators.setVisibility(View.GONE);
                    isScrollEnd = true;
                } else if (isScrollEnd && position == imageList.length - 2) {
                    indicators.setVisibility(View.VISIBLE);
                    isScrollEnd = false;
                }
            }
        });


    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSnack(String msg) {

    }

    @Override
    public void success() {

    }


    @Override
    public void onItemClick(int position) {
        if (position == imageList.length - 1) {
            startActivity();
        }
    }

    public void startActivity() {
        startActivity(new Intent(this, Main2Activity.class));
        finish();
    }
}

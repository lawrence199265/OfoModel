package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.lawrence.core.lib.core.mvp.BasePresenter;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.IMainActivity;
import com.zhuangbudong.ofo.event.LoginEvent;
import com.zhuangbudong.ofo.fragment.NewsFragment;
import com.zhuangbudong.ofo.fragment.PersonFragment;
import com.zhuangbudong.ofo.presenter.MainPresenter;
import com.zhuangbudong.ofo.utils.PopupMenuUtil;
import com.zhuangbudong.ofo.utils.PrefsUtils;
import com.zhuangbudong.ofo.widget.TabEntity;
import com.zhuangbudong.ofo.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.zhuangbudong.ofo.activity.SignInActivity.EXTRA_SIGN_OK;
import static com.zhuangbudong.ofo.utils.PopupMenuUtil.TYPE_RENT;
import static com.zhuangbudong.ofo.utils.PopupMenuUtil.TYPE_SWAP;

public class Main2Activity extends BaseActivity<MainPresenter> implements IMainActivity, TabLayout.OnTabClickListener, PopupMenuUtil.OnItemClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */


    private int[] iconList = new int[]{R.drawable.ic_home, R.drawable.ic_myself};
    private String[] titles = new String[]{"首页", "我的"};
    private TabLayout tab;
    private List<TabEntity> entities;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] fragments = new Fragment[2];
    private ImageView ivAdd;
    private boolean isNotLogin;
    private int currentPosition = 0;
    private static final String TAG = "Main2Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RxBus.get().register(this);
        initData();
        initView();
        showFirstFragment();

    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    public void initView() {
        ivAdd = (ImageView) findViewById(R.id.add_iv_icon);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNotLogin) {
                    startActivity(new Intent(Main2Activity.this, SignInActivity.class));
                    return;
                }
                PopupMenuUtil.getInstance().show(Main2Activity.this, ivAdd);
                PopupMenuUtil.getInstance().setOnItemClickListener(Main2Activity.this);
            }
        });
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnack(String msg) {

    }

    private void showFirstFragment() {
        fragments[0] = NewsFragment.newInstance();
        fragments[1] = PersonFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fl_container, fragments[0]);
        fragmentTransaction.add(R.id.main_fl_container, fragments[1]);
        fragmentTransaction.hide(fragments[1]);
        fragmentTransaction.commit();


    }

    private void showHideFragment(Fragment showFragment, Fragment hideFragment) {
        if (showFragment == hideFragment) {
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction().show(showFragment);
        if (hideFragment == null) {
            List<Fragment> fragmentList = fragmentManager.getFragments();
            if (fragmentList != null) {
                for (Fragment fragment : fragmentList) {
                    if (fragment != null || fragment != showFragment) {
                        ft.hide(fragment);
                    }
                }
            }
        } else {
            ft.hide(hideFragment);
        }
        ft.commit();
    }

    private void initData() {
        isNotLogin = PrefsUtils.loadPrefBoolean(this, EXTRA_SIGN_OK, false);
        tab = (TabLayout) findViewById(R.id.main_tab);
        entities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            entities.add(new TabEntity(iconList[i], titles[i]));
        }
        tab.setTabData(entities);
        tab.setOnTabClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rent, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (PopupMenuUtil.getInstance().isShowing()) {
            PopupMenuUtil.getInstance().rlClickAction();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View tab, int position) {
        changeFragment(position);
    }

    private void changeFragment(int position) {
        if (position == 1 && !isNotLogin) {
            this.tab.setCurrentPosition(0);
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            if (currentPosition != position) {
                tab.setCurrentPosition(position);
                showHideFragment(fragments[position], fragments[currentPosition]);
                currentPosition = position;
            }
        }
    }


    @Override
    public void onClick(int position) {

        switch (position) {
            case TYPE_RENT:
                startActivity(new Intent(this, RentActivity.class));
                PopupMenuUtil.getInstance().close();
                break;
            case TYPE_SWAP:
                startActivity(new Intent(this, SwapActivity.class));
                PopupMenuUtil.getInstance().close();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Subscribe
    public void changeLoginState(LoginEvent event) {
        isNotLogin = (event.getType() == LoginEvent.LOGIN);
        //返回首页
        changeFragment(0);
    }
}

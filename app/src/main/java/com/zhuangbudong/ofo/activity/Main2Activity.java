package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.fragment.NewsFragment;
import com.zhuangbudong.ofo.fragment.PersonFragment;
import com.zhuangbudong.ofo.utils.PopupMenuUtil;
import com.zhuangbudong.ofo.widget.TabEntity;
import com.zhuangbudong.ofo.widget.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.zhuangbudong.ofo.activity.SignInActivity.EXTRA_SIGN_OK;
import static com.zhuangbudong.ofo.utils.PopupMenuUtil.TYPE_RENT;
import static com.zhuangbudong.ofo.utils.PopupMenuUtil.TYPE_SWAP;

public class Main2Activity extends AppCompatActivity implements TabLayout.OnTabClickListener, PopupMenuUtil.OnItemClickListener {
    private static final int REQUEST_CODE = 0;

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
    private FrameLayout flContainer;
    private NewsFragment newsFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment[] fragments = new Fragment[2];
    private ImageView ivAdd;
    private boolean isNotLogin;
    private int currentPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initView();
        showFirstFragment();

    }

    private void initView() {
        ivAdd = (ImageView) findViewById(R.id.add_iv_icon);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuUtil.getInstance().show(Main2Activity.this, ivAdd);
                PopupMenuUtil.getInstance().setOnItemClickListener(Main2Activity.this);
            }
        });
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
        if (position == 1 && !isNotLogin) {
            this.tab.setCurrentPosition(0);
            startActivityForResult(new Intent(this, SignInActivity.class), REQUEST_CODE);
        } else {
            if (currentPosition != position) {
                showHideFragment(fragments[position], fragments[currentPosition]);
                currentPosition = position;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            isNotLogin = data.getBooleanExtra(EXTRA_SIGN_OK, false);
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
}

package com.zhuangbudong.ofo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.IPersonActivity;
import com.zhuangbudong.ofo.model.User;
import com.zhuangbudong.ofo.presenter.PersonInfoPresenter;
import com.zhuangbudong.ofo.utils.AddressPickTask;
import com.zhuangbudong.ofo.widget.LinearLayoutListItemView;


import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class PersonInfoActivity extends BaseActivity<PersonInfoPresenter> implements IPersonActivity {
    private Toolbar tlBar;
    private LinearLayoutListItemView itemAddress;
    private EditText etEmail, etTel, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindData();

    }

    private void bindData() {
        presenter.getUser();
    }

    @Override
    protected void initPresenter() {
        presenter = new PersonInfoPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_info;
    }


    public void initView() {
        tlBar = (Toolbar) findViewById(R.id.personal_info_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle("个人信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemAddress = (LinearLayoutListItemView) findViewById(R.id.personal_info_address);

        etTel = (EditText) findViewById(R.id.personal_info_tel);
        etEmail = (EditText) findViewById(R.id.personal_info_email);
        etAddress = (EditText) findViewById(R.id.personal_et_detail_address);

        initAddressPicker();


    }


    private void initAddressPicker() {
        itemAddress.setOnLinearLayoutListItemClickListener(new LinearLayoutListItemView.OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
                AddressPickTask task = new AddressPickTask(PersonInfoActivity.this);
                task.setHideProvince(false);
                task.setHideCounty(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            itemAddress.setRightText(province.getAreaName() + city.getAreaName());
                        } else {
                            itemAddress.setRightText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                        }
                    }
                });
                task.execute("北京市", "北京市", "东城区");
            }
        });

    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSnack(String msg) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                //保存信息
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUser(User user) {
        etEmail.setText(user.getEmail());
        etTel.setText(user.getPhone());
        String address = user.getAddress();
        if (address == null || address.isEmpty()) {
            return;
        }
        itemAddress.setRightText(address.substring(0, address.indexOf("区") + 1));
        etAddress.setText(address.substring(address.indexOf("区") + 1, address.length()));
    }
}


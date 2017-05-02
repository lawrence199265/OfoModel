package com.zhuangbudong.ofo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.utils.AddressPickTask;
import com.zhuangbudong.ofo.widget.ChooseDialogFragment;
import com.zhuangbudong.ofo.widget.DatePicker;
import com.zhuangbudong.ofo.widget.LinearLayoutListItemView;


import java.util.Calendar;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DateTimePicker;

public class PersonInfoActivity extends AppCompatActivity {
    private Toolbar tlBar;
    private LinearLayoutListItemView itemSex, itemBirthday, itemAddress;
    private String[] data;
    private int checkedId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initData();
        initView();
        showSexPicker();
        showDatePicker();
        showAddressPicker();


    }

    private void initData() {
        data = new String[]{getString(R.string.label_man), getString(R.string.label_woman)};
    }

    private void initView() {
        tlBar = (Toolbar) findViewById(R.id.personal_info_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle("个人信息");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemSex = (LinearLayoutListItemView) findViewById(R.id.personal_info_sex);
        itemBirthday = (LinearLayoutListItemView) findViewById(R.id.personal_info_birthday);
        itemAddress = (LinearLayoutListItemView) findViewById(R.id.personal_info_address);

    }

    private void showSexPicker() {
        itemSex.setOnLinearLayoutListItemClickListener(new LinearLayoutListItemView.OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
                ChooseDialogFragment sexDialog = ChooseDialogFragment.newInstance(data, checkedId);
                sexDialog.show(getSupportFragmentManager(), "ChooseDialogFragment");
                sexDialog.setOnDialogPickedListener(new ChooseDialogFragment.OnDialogPickedListener() {
                    @Override
                    public void onPicked(int checkedId) {
                        itemSex.setRightText(data[checkedId]);
                        PersonInfoActivity.this.checkedId = checkedId;
                    }
                });
            }
        });

    }

    private void showDatePicker() {
        itemBirthday.setOnLinearLayoutListItemClickListener(new LinearLayoutListItemView.OnLinearLayoutListItemClickListener() {
            @Override
            public void onLinearLayoutListItemClick(Object object) {
                final DatePicker datePicker = new DatePicker(PersonInfoActivity.this, DateTimePicker.YEAR_MONTH_DAY, DatePicker.NONE);
                datePicker.setYearMonthDayWeight(0.8f, 0.5f, 0.5f);
                datePicker.setDateRangeStart(1970, 1, 1);
                datePicker.setLabelTextSize(18);
                datePicker.setLineVisible(false);
                Calendar date = Calendar.getInstance();
                datePicker.setDateRangeEnd(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE));
                datePicker.setTextSize(16);
                datePicker.setCycleDisable(false);
                datePicker.setTextColor(getResources().getColor(R.color.main_color));
                datePicker.show();


                datePicker.setOnDateTimePickListener(new DatePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        itemBirthday.setRightText(year + "-" + month + "-" + day);

                    }
                });
            }
        });

    }


    private void showAddressPicker() {
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

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
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
}


package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.lawrence.core.lib.core.mvp.BaseActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.nanchen.compresshelper.CompressHelper;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.inter.IRentActivity;
import com.zhuangbudong.ofo.adpter.DynamicPickerAdapter;
import com.zhuangbudong.ofo.application.OfoApplication;
import com.zhuangbudong.ofo.event.BusAction;
import com.zhuangbudong.ofo.event.InputPriceEvent;
import com.zhuangbudong.ofo.model.Issue;
import com.zhuangbudong.ofo.presenter.RentPresenter;
import com.zhuangbudong.ofo.utils.BitmapUtil;
import com.zhuangbudong.ofo.utils.CalculateTextLengthUtil;
import com.zhuangbudong.ofo.widget.FullyGridLayoutManager;
import com.zhuangbudong.ofo.widget.InputDialogFragment;
import com.zhuangbudong.ofo.widget.InputPriceDialogFragment;

import java.util.ArrayList;

public class RentActivity extends BaseActivity<RentPresenter> implements IRentActivity, DynamicPickerAdapter.onAddImageListener, DynamicPickerAdapter.OnItemClickListener, View.OnClickListener {
    private RecyclerView rlImgPicker;
    private CompressHelper mCompressor;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ArrayList<ImageItem> selImageList = new ArrayList<>();
    private int maxImageCount = 3;
    private DynamicPickerAdapter adapter;
    private EditText etBody, etTitle;
    private TextView tvBodyLength;
    private CalculateTextLengthUtil calculateTextLengthUtil;
    private RelativeLayout rlProvide, rlPrice, rlDeposit;
    private InputDialogFragment inputDialogFragment;
    private InputPriceDialogFragment inputPriceDialogFragment;
    private Toolbar tlBar;
    private TextView tvDeposit, tvPrice, tvProvide;
    private static final String TAG = "RentActivity";
    private String[] priceFormat;
    private int checkedId = 0;
    private String price = "5";
    private String dispose = "2";
    private String custom = "";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rent);
        fragmentManager = getSupportFragmentManager();
        initCompressHelper();
        initData();
        initView();

    }

    @Override
    protected void initPresenter() {
        presenter = new RentPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rent;
    }

    private void initData() {
        priceFormat = getResources().getStringArray(R.array.priceFormat);
    }

    private void initCompressHelper() {
        mCompressor = new CompressHelper.Builder(this)
                .setMaxHeight(640)
                .setMaxWidth(480)
                .setQuality(80)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build();
    }

    public void initView() {
        tlBar = (Toolbar) findViewById(R.id.rent_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.label_rent);
        calculateTextLengthUtil = new CalculateTextLengthUtil();
        etBody = (EditText) findViewById(R.id.rent_et_description);
        tvBodyLength = (TextView) findViewById(R.id.rent_tv_text_length);

        etTitle = (EditText) findViewById(R.id.rent_et_title);

        //添加选中的图片
        rlImgPicker = (RecyclerView) findViewById(R.id.rl_iv_picker);
        selImageList = new ArrayList<>();
        rlImgPicker.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        adapter = new DynamicPickerAdapter(RentActivity.this);
        adapter.setOnAddImageListener(this);
        adapter.setOnItemClickListener(this);
        rlImgPicker.setHasFixedSize(true);
        rlImgPicker.setAdapter(adapter);
        rlProvide = (RelativeLayout) findViewById(R.id.rent_rl_provide);
        rlProvide.setOnClickListener(this);


        //更新录入的字数
        calculateTextLengthUtil.updateTextCount(tvBodyLength, 99, etBody);


        //价格 租金item
        rlPrice = (RelativeLayout) findViewById(R.id.rent_rl_price);
        rlPrice.setOnClickListener(this);
        rlDeposit = (RelativeLayout) findViewById(R.id.rent_rl_deposit);
        rlDeposit.setOnClickListener(this);


        //格式化价格
        tvDeposit = (TextView) findViewById(R.id.rent_tv_deposit);
        tvPrice = (TextView) findViewById(R.id.rent_tv_price);
//        tvPrice.setText(formHtml(String.format(priceFormat[checkedId], price)));
//        tvDeposit.setText(formHtml(String.format(getString(R.string.depositFormat), dispose)));

        tvProvide = (TextView) findViewById(R.id.rent_tv_provide);


    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSnack(String msg) {

    }


    @Override
    public void onAdd(int type, int position) {
        switch (type) {
            case 0:
                ImagePicker.getInstance().setSelectLimit(maxImageCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            case 1:
                selImageList.remove(position);
                adapter.setList(selImageList);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                selImageList.addAll(images);
                adapter.setList(selImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                selImageList.clear();
                selImageList.addAll(images);
                adapter.setList(selImageList);
            }
        }
    }


    @Override
    public void onItemClick(int position, View v) {
        Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImage());
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rent, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rent_rl_provide:
                if (inputDialogFragment != null) {
                    fragmentManager.beginTransaction().remove(inputDialogFragment).commit();
                }
                inputDialogFragment = InputDialogFragment.newInstance(InputDialogFragment.TYPE_PROVIDE, custom);
                inputDialogFragment.show(getSupportFragmentManager(), "InputProvideFragment");
                break;
            case R.id.rent_rl_price:

                if (inputPriceDialogFragment != null) {
                    fragmentManager.beginTransaction().remove(inputPriceDialogFragment).commit();
                }
                inputPriceDialogFragment = InputPriceDialogFragment.newInstance(price, checkedId);
                inputPriceDialogFragment.show(fragmentManager, "InputPriceDialogFragment");


                break;
            case R.id.rent_rl_deposit:
                if (inputDialogFragment != null) {
                    fragmentManager.beginTransaction().remove(inputDialogFragment).commit();
                }
                inputDialogFragment = InputDialogFragment.newInstance(InputDialogFragment.TYPE_DEPOSIT, dispose);
                inputDialogFragment.show(getSupportFragmentManager(), "InputDepositFragment");
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_submit:
                ArrayList<byte[]> images = new ArrayList<>();
                if (TextUtils.isEmpty(etTitle.getText().toString())) {
                    showToast("标题不能为空");
                    break;
                }
                if (TextUtils.isEmpty(etBody.getText().toString())) {
                    showToast("描述不能为空");
                    break;
                }


                for (int i = 0; i < selImageList.size(); i++) {
                    Bitmap src = BitmapFactory.decodeFile(selImageList.get(i).path);
                    images.add(BitmapUtil.bitmapToBase64(src));
                }

                Issue issue = new Issue();
                issue.setUserId(OfoApplication.getInstance().userId);
                issue.setTitle(etTitle.getText().toString());
                issue.setDetail(etBody.getText().toString());
//                issue.setImage(images);
                presenter.submitRentInfo(issue);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Subscribe(tags = @Tag(BusAction.TAG_INPUT_PRICE))
    public void setPriceText(InputPriceEvent inputEvent) {
        checkedId = inputEvent.getCheckIndex();
        price = inputEvent.getInput();
        tvPrice.setText(formHtml(String.format(priceFormat[checkedId], inputEvent.getInput())));
    }

    @Subscribe(tags = @Tag(BusAction.TAG_INPUT_DISPOSE))
    public void setDepositText(String disposeText) {
        dispose = disposeText;
        tvDeposit.setText(formHtml(String.format(getString(R.string.depositFormat), disposeText)));
    }

    @Subscribe(tags = @Tag(BusAction.TAG_INPUT_PROVIDE))
    public void setProvideText(String provideText) {
        custom = provideText;
        tvProvide.setText(provideText);
    }

    public static Spanned formHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

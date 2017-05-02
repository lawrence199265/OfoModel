package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.nanchen.compresshelper.CompressHelper;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.adpter.DynamicPickerAdapter;
import com.zhuangbudong.ofo.utils.CalculateTextLengthUtil;
import com.zhuangbudong.ofo.widget.FullyGridLayoutManager;
import com.zhuangbudong.ofo.widget.InputDialogFragment;
import com.zhuangbudong.ofo.widget.InputPriceDialogFragment;

import java.util.ArrayList;

public class RentActivity extends AppCompatActivity implements DynamicPickerAdapter.onAddImageListener, DynamicPickerAdapter.OnItemClickListener, View.OnClickListener {
    private RecyclerView rlImgPicker;
    private CompressHelper mCompressor;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ArrayList<ImageItem> selImageList = new ArrayList<>();
    private int maxImageCount = 3;
    private DynamicPickerAdapter adapter;
    private EditText etBody;
    private TextView tvBodyLength;
    private CalculateTextLengthUtil calculateTextLengthUtil;
    private RelativeLayout rlProvide, rlPrice, rlDeposit;
    private InputDialogFragment inputDialogFragment;
    private InputPriceDialogFragment inputPriceDialogFragment;
    private Toolbar tlBar;
    private TextView tvDeposit, tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        initCompressHelper();
        initView();

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

    private void initView() {
        tlBar = (Toolbar) findViewById(R.id.rent_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.label_rent);
        calculateTextLengthUtil = new CalculateTextLengthUtil();
        etBody = (EditText) findViewById(R.id.rent_et_description);
        tvBodyLength = (TextView) findViewById(R.id.rent_tv_text_length);
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
        calculateTextLengthUtil.updateTextCount(tvBodyLength, 99, etBody);
        rlPrice = (RelativeLayout) findViewById(R.id.rent_rl_price);
        rlPrice.setOnClickListener(this);
        rlDeposit = (RelativeLayout) findViewById(R.id.rent_rl_deposit);
        rlDeposit.setOnClickListener(this);

        tvDeposit = (TextView) findViewById(R.id.rent_tv_deposit);
        tvPrice = (TextView) findViewById(R.id.rent_tv_price);
        handleData(tvPrice);
        handleData(tvDeposit);

    }

    private void handleData(TextView tvText) {
        String text = tvText.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        int endIndex = text.indexOf("元");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvText.setText(spannableString);
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
                inputDialogFragment = InputDialogFragment.newInstance(InputDialogFragment.TYPE_CUSTOM);
                inputDialogFragment.show(getSupportFragmentManager(), "InputDialogFragment");
                break;
            case R.id.rent_rl_price:
                inputPriceDialogFragment = InputPriceDialogFragment.newInstance();
                inputPriceDialogFragment.show(getSupportFragmentManager(), "InputPriceDialogFragment");
                break;
            case R.id.rent_rl_deposit:
                inputDialogFragment = InputDialogFragment.newInstance(InputDialogFragment.TYPE_DEPOSIT);
                inputDialogFragment.show(getSupportFragmentManager(), "InputDialogFragment");
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}

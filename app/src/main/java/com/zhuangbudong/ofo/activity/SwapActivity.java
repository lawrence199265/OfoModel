
package com.zhuangbudong.ofo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.zhuangbudong.ofo.widget.FullyGridLayoutManager;
import com.zhuangbudong.ofo.widget.InputDialogFragment;

import java.util.ArrayList;

public class SwapActivity extends AppCompatActivity implements DynamicPickerAdapter.onAddImageListener, DynamicPickerAdapter.OnItemClickListener, View.OnClickListener {

    private CompressHelper mCompressor;
    private Toolbar tlBar;
    private EditText etBody;
    private TextView tvBodyLength;
    private RelativeLayout rlProvide, rlDemand;
    private TextView tvProvide, tvDemand;
    private RecyclerView rlImgPicker;
    private ArrayList<ImageItem> selImageList;
    private DynamicPickerAdapter adapter;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private int maxImageCount = 3;
    private InputDialogFragment inputDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);
        initCompressHelper();
        initView();
    }

    private void initView() {
        tlBar = (Toolbar) findViewById(R.id.swap_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle(R.string.label_title_swap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlImgPicker = (RecyclerView) findViewById(R.id.rl_iv_picker);
        selImageList = new ArrayList<>();
        rlImgPicker.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        adapter = new DynamicPickerAdapter(this);
        adapter.setOnAddImageListener(this);
        adapter.setOnItemClickListener(this);
        rlImgPicker.setHasFixedSize(true);
        rlImgPicker.setAdapter(adapter);

        rlProvide= (RelativeLayout) findViewById(R.id.swap_rl_provide);
        rlDemand= (RelativeLayout) findViewById(R.id.swap_rl_demand);
        rlProvide.setOnClickListener(this);
        rlDemand.setOnClickListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rent, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void onItemClick(int position, View v) {
        Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImage());
        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.swap_rl_provide:
                inputDialogFragment = InputDialogFragment.newInstance(InputDialogFragment.TYPE_CUSTOM);
                inputDialogFragment.show(getSupportFragmentManager(), "InputDialogFragment");
                break;
            case R.id.swap_rl_demand:
                inputDialogFragment = InputDialogFragment.newInstance(InputDialogFragment.TYPE_CUSTOM);
                inputDialogFragment.show(getSupportFragmentManager(), "InputDialogFragment");
                break;
        }
    }
}

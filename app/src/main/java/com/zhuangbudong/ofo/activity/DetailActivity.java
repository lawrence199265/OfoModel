package com.zhuangbudong.ofo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.utils.Constants;
import com.zhuangbudong.ofo.utils.DensityUtils;
import com.zhuangbudong.ofo.utils.GlideImageLoader;
import com.zhuangbudong.ofo.widget.CircleImageView;
import com.zhuangbudong.ofo.widget.ParticipantView;

public class DetailActivity extends AppCompatActivity {

    private TextView omitTv;
    private ParticipantView participantView;
    private CircleImageView addIv;
    private Toolbar tlBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initParticipantArea();
    }

    private void initView() {
        participantView = (ParticipantView) findViewById(R.id.detail_participant);
        this.addIv = this.createCircleImageView();
        this.addIv.setImageResource(R.drawable.ic_camnter);

        tlBar = (Toolbar) findViewById(R.id.detail_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle("详情页");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private CircleImageView createCircleImageView() {
        CircleImageView iv = new CircleImageView(this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(this, 36), DensityUtils.dip2px(this, 36)));
        return iv;
    }

    protected void setOmitView(int count) {
        View omitView = LayoutInflater.from(this).inflate(R.layout.view_omit_style, null);
        this.omitTv = (TextView) omitView.findViewById(R.id.topic_omit_tv);
        this.omitTv.setText(this.getString(this.getOmitVieStringFormatId(), count));
        this.participantView.setOmitView(omitView);
    }

    protected int getOmitVieStringFormatId() {
        return R.string.view_omit_style_content;
    }


    private void initParticipantArea() {
        this.setOmitView(Constants.STYLE_AVATARS.length);
//        Toast.makeText(this, Constants.STYLE_AVATARS.length + "个", Toast.LENGTH_SHORT).show();
        for (int idRes : Constants.STYLE_AVATARS) {
            CircleImageView iv = this.createCircleImageView();
            GlideImageLoader.displayNative(iv, idRes);
            this.participantView.addView(iv);
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
}

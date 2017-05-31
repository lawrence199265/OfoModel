package com.zhuangbudong.ofo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.model.Issue;
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

    private Button callUser;
    private Issue issue;


    private TextView title;
    private TextView detail;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        issue = intent.getParcelableExtra("data");
        initView();
        initParticipantArea();
    }

    private void initView() {
        participantView = (ParticipantView) findViewById(R.id.detail_participant);
        title = (TextView) findViewById(R.id.detail_tv_title);
        detail = (TextView) findViewById(R.id.detail_tv_description);
        userName= (TextView) findViewById(R.id.detail_tv_username);

        title.setText(issue.getTitle());
        detail.setText(issue.getDetail());
        userName.setText(issue.getUserName());

        this.addIv = this.createCircleImageView();
        this.addIv.setImageResource(R.drawable.ic_camnter);

        tlBar = (Toolbar) findViewById(R.id.detail_tl_bar);
        setSupportActionBar(tlBar);
        getSupportActionBar().setTitle("详情页");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        callUser = (Button) findViewById(R.id.call_user);
        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("联系他")
                        .setMessage("".equals(issue.getPhone()) || issue.getPhone() == null ? "未找到联系人电话" : issue.getPhone())
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            }
        });
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

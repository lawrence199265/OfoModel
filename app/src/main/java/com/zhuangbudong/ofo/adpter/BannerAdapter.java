package com.zhuangbudong.ofo.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.widget.OnPageClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx on 17/3/1.
 */

public class BannerAdapter extends PagerAdapter {
    private int[] imageList;
    private OnPageClickListener onPageClickListener;
    private static final String TAG = "BannerAdapter";

    public BannerAdapter(int[] imageList) {
        this.imageList = imageList;
    }


    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_loading, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.item_layout_iv);
        iv.setImageResource(imageList[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }
}

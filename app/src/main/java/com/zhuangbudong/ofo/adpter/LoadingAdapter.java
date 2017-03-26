package com.zhuangbudong.ofo.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.widget.OnPageClickListener;

/**
 * Created by xunzongxia on 17/2/24.
 */

public class LoadingAdapter extends PagerAdapter {


    private Context context;
    private int[] imageList;
    private LayoutInflater inflater;
    private OnPageClickListener OnItemClickListener;

    public LoadingAdapter(Context context, int[] imageList) {
        this.context = context;
        this.imageList = imageList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View root = inflater.inflate(R.layout.item_loading, container, false);
        ImageView pageIv = (ImageView) root.findViewById(R.id.item_layout_iv);
        pageIv.setImageResource(imageList[position]);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemClickListener.onItemClick(position);
            }
        });
        container.addView(root);
        return root;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnPageClickListener(OnPageClickListener onItemClickListener) {
        OnItemClickListener = onItemClickListener;
    }


}

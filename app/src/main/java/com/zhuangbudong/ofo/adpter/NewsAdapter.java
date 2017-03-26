package com.zhuangbudong.ofo.adpter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.ImagePagerActivity;
import com.zhuangbudong.ofo.model.ImageModel;
import com.zhuangbudong.ofo.model.Item;
import com.zhuangbudong.ofo.widget.MultiImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xunzongxia on 17/3/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<Item> datas;
    private Context context;

    private ArrayList<String> transitionNames;

    public void setTransitionNames(ArrayList<String> transitionNames) {
        this.transitionNames = transitionNames;
    }


    public NewsAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<>();

    }

    public void setDatas(List<Item> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rl_news, null);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final NewsAdapter.ViewHolder holder, int position) {
        final List<ImageModel> photos = datas.get(position).getPhotos();
        holder.multiImageView.setList(photos);
        final ArrayList<String> photoUrl = new ArrayList<String>();
        for (ImageModel photo : photos) {
            photoUrl.add(photo.url);
        }


        holder.multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                ActivityOptionsCompat optionsCompat = null;
                if (Build.VERSION.SDK_INT >= 16) {
                    optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, holder.multiImageView.getWidth() / 2, holder.multiImageView.getHeight() / 2, 0, 0);
                }
                ImagePagerActivity.startImagePagerActivity(context, photoUrl, position, imageSize, transitionNames, optionsCompat.toBundle());
            }

        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private MultiImageView multiImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            multiImageView = (MultiImageView) itemView.findViewById(R.id.main_iv_list);

        }
    }


}

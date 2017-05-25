package com.zhuangbudong.ofo.adpter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.activity.ImagePagerActivity;
import com.zhuangbudong.ofo.model.Issue;
import com.zhuangbudong.ofo.widget.BannerViewPager;
import com.zhuangbudong.ofo.widget.MultiImageView;

import android.support.v7.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xxx on 17/3/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Issue> datas;
    private Context context;
    private int[] imageList;

    private ArrayList<String> transitionNames;

    private final static int TYPE_HEADER = 0, TYPE_CONTENT = 1;

    private onRecyclerItemListener onRecyclerItemListener;


    public void setTransitionNames(ArrayList<String> transitionNames) {
        this.transitionNames = transitionNames;
    }


    public NewsAdapter(Context context, int[] imageList) {
        this.context = context;
        this.imageList = imageList;
        this.datas = new ArrayList<>();

    }

    public void setDatas(List<Issue> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTENT) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rl_news, parent, false);
            return new ViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_header, parent, false);
            return new HeaderViewHolder(headerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);


        switch (itemViewType) {
            case TYPE_HEADER:

                break;
            case TYPE_CONTENT:
                final List<byte[]> photos = datas.get(position).getImage();

                if (photos.size() != 0) {
                    ((NewsAdapter.ViewHolder) holder).multiImageView.setList(photos);
                    final ArrayList<byte[]> photoUrl = new ArrayList<byte[]>();
                    for (byte[] photo : photos) {
                        photoUrl.add(photo);
                    }


                    ((NewsAdapter.ViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                            ActivityOptionsCompat optionsCompat = null;
                            if (Build.VERSION.SDK_INT >= 16) {
                                optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, ((NewsAdapter.ViewHolder) holder).multiImageView.getWidth() / 2, ((NewsAdapter.ViewHolder) holder).multiImageView.getHeight() / 2, 0, 0);
                            }
                            ImagePagerActivity.startImagePagerActivity(context, photoUrl, position, imageSize, transitionNames, optionsCompat.toBundle());
                        }

                    });
                }
                break;

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerItemListener != null) {
                    onRecyclerItemListener.onItemClick(holder.getLayoutPosition());
                }
            }
        });


    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }

    public void setOnRecyclerItemListener(NewsAdapter.onRecyclerItemListener onRecyclerItemListener) {
        this.onRecyclerItemListener = onRecyclerItemListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private MultiImageView multiImageView;


        public ViewHolder(View itemView) {
            super(itemView);
            multiImageView = (MultiImageView) itemView.findViewById(R.id.main_iv_list);

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private BannerViewPager loopViewPager;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            loopViewPager = (BannerViewPager) itemView.findViewById(R.id.news_banner_vp);
            loopViewPager.setFocusable(true);
            loopViewPager.setFocusableInTouchMode(true);
            loopViewPager.requestFocus();
            loopViewPager.setAdapter(new BannerAdapter(imageList));
        }
    }

    public interface onRecyclerItemListener {
        void onItemClick(int position);
    }


}

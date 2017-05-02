package com.zhuangbudong.ofo.adpter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.zhuangbudong.ofo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxx on 17/3/27.
 */

public class DynamicPickerAdapter extends RecyclerView.Adapter<DynamicPickerAdapter.ViewHolder> {
    private final int TYPE_ADD_IMAGE = 1;
    private final int TYPE_SHOW_IMAGE = 2;
    private Context context;
    private onAddImageListener onAddImageListener;
    private LayoutInflater inflater;

    public List<ImageItem> getImage() {
        return list;
    }

    private List<ImageItem> list = new ArrayList<>();
    private int selectMaxSize = 3;

    public DynamicPickerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    public void setOnAddImageListener(DynamicPickerAdapter.onAddImageListener onAddImageListener) {
        this.onAddImageListener = onAddImageListener;
    }

    public interface onAddImageListener {
        void onAdd(int type, int position);
    }

    public void setList(List<ImageItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_ADD_IMAGE;
        } else {
            return TYPE_SHOW_IMAGE;
        }
    }


    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.gv_filter_image, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemClickListener.onItemClick(viewHolder.getAdapterPosition(), v);
                        }
                    });
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ADD_IMAGE) {
            holder.img.setImageDrawable(context.getDrawable(R.drawable.ic_add));
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddImageListener != null) {
                        onAddImageListener.onAdd(0, holder.getAdapterPosition());
                    }

                }
            });
            holder.llDelete.setVisibility(View.INVISIBLE);
        } else {
            holder.llDelete.setVisibility(View.VISIBLE);
            holder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddImageListener != null) {
                        onAddImageListener.onAdd(1, holder.getAdapterPosition());
                    }
                }
            });
            ImageItem item = list.get(position);
            ImagePicker.getInstance().getImageLoader().displayImage((Activity) context, item.path, holder.img, 0, 0);

        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMaxSize) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private LinearLayout llDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.fiv);
            llDelete = (LinearLayout) itemView.findViewById(R.id.ll_del);
        }


    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}

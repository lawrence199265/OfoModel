package com.zhuangbudong.ofo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.model.ImageModel;
import com.zhuangbudong.ofo.utils.DensityUtils;

import java.util.List;

/**
 * Created by xunzongxia on 17/3/14.
 */

public class MultiImageView extends LinearLayout {
    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<ImageModel> imagesList;

    /**
     * 长度 单位为Pixel
     **/
    //private int pxOneMaxWandH;  // 单张图最大允许宽高
    private int pxMoreWandH = 0;// 多张图的宽高
    private int pxImagePadding = DensityUtils.dip2px(getContext(), 5);// 图片间的间距
    private int pxImageStartEnd = DensityUtils.dip2px(getContext(), 10);

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数
    private static final String TAG = "MultiImageView";

    //private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private MultiImageView.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(MultiImageView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<ImageModel> lists) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;

        if (MAX_WIDTH > 0) {
            pxMoreWandH = (MAX_WIDTH - pxImageStartEnd * MAX_PER_ROW_COUNT) / 3; //解决右侧图片和内容对不齐问题
            initImageLayoutParams();
        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initImageLayoutParams() {

        moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
        moreParaColumnFirst.setMargins(pxImageStartEnd, 0, 0, 0);
        morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
        morePara.setMargins(pxImagePadding, 0, 0, 0);


    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(HORIZONTAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }
        int allCount = imagesList.size();
        for (int columnCursor = 0; columnCursor < allCount; columnCursor++) {
            int position = columnCursor;
            addView(createImageView(position));
        }

    }


    private ImageView createImageView(int position) {
        ImageModel photoInfo = imagesList.get(position);
        ImageView imageView = new ColorFilterImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);


        imageView.setId(photoInfo.url.hashCode());
        imageView.setOnClickListener(new MultiImageView.ImageOnClickListener(position));
        imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
        Glide.with(getContext()).load(photoInfo.url).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.empty_netexception)
                .error(R.drawable.a)
                .into(imageView);

        return imageView;
    }

    private class ImageOnClickListener implements OnClickListener {

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}

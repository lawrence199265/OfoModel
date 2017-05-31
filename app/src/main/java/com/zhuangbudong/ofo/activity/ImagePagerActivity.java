package com.zhuangbudong.ofo.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.zhuangbudong.ofo.R;
import com.zhuangbudong.ofo.utils.BitmapUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ImagePagerActivity extends AppCompatActivity {

    private static final String INTENT_IMGURLS = "imageUrl";
    private static final String INTENT_POSITION = "position";
    private static final String INTENT_IMAGESIZE = "imageSize";
    public static final String PARAMS_TRANSITIONNAMES = "transition_names";
    private ViewPager vpImage;
    private int startPos;
    private ArrayList<byte[]> imgUrls;
    private ImageSize imageSize;

    private List<String> transitionNames = new ArrayList<>();
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);


        vpImage = (ViewPager) findViewById(R.id.image_pager);
        getIntentData();
        supportPostponeEnterTransition();
        imageAdapter = new ImageAdapter(this);

        imageAdapter.setDatas(imgUrls);
        imageAdapter.setImageSize(imageSize);
        vpImage.setAdapter(imageAdapter);
        vpImage.setCurrentItem(startPos);


    }

    private void getIntentData() {
        startPos = getIntent().getIntExtra(INTENT_POSITION, 0);
//        imgUrls = getIntent().gete(INTENT_IMGURLS);
        imageSize = (ImageSize) getIntent().getSerializableExtra(INTENT_IMAGESIZE);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void startImagePagerActivity(Context context, List<byte[]> imgUrls, int position, ImageSize imageSize, ArrayList<String> transitionNames, Bundle bundle) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(INTENT_IMGURLS, new ArrayList<byte[]>(imgUrls));
        intent.putExtra(INTENT_POSITION, position);
        intent.putExtra(INTENT_IMAGESIZE, imageSize);
        intent.putExtra(PARAMS_TRANSITIONNAMES, transitionNames);
        if (bundle == null) {
            context.startActivity(intent);
        } else {
            context.startActivity(intent, bundle);
        }
    }

    private class ImageAdapter extends PagerAdapter {

        private List<byte[]> datas = new ArrayList<byte[]>();
        private LayoutInflater inflater;
        private Context context;
        private ImageSize imageSize;
        private ImageView smallImageView = null;

        public void setDatas(List<byte[]> datas) {
            if (datas != null)
                this.datas = datas;
        }

        public void setImageSize(ImageSize imageSize) {
            this.imageSize = imageSize;
        }

        public ImageAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (datas == null) return 0;
            return datas.size();
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = inflater.inflate(R.layout.item_pager_image, container, false);
            if (view != null) {
                final ImageView imageView = (ImageView) view.findViewById(R.id.photo_image);


                if (imageSize != null) {
                    //预览imageView
                    smallImageView = new ImageView(context);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageSize.getWidth(), imageSize.getHeight());
                    layoutParams.gravity = Gravity.CENTER;
                    smallImageView.setLayoutParams(layoutParams);
                    smallImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ((FrameLayout) view).addView(smallImageView);
                }

                //loading
                final ProgressBar loading = new ProgressBar(context);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

//                final String imgurl = datas.get(position);

                Glide.with(context)
                        .load(BitmapUtil.base64ToBitmap("123"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存多个尺寸
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.drawable.empty_netexception)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);

                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                            }
                        });

                container.addView(view, 0);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class ImageSize implements Serializable {

        private int width;
        private int height;

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }


    public static class ImageLocation implements Serializable {
        private int left;
        private int top;

        public ImageLocation(int left, int top) {
            this.left = left;
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }
    }


    @Override
    public void finishAfterTransition() {
        super.finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }
}

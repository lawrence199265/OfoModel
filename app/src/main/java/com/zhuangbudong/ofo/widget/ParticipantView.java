package com.zhuangbudong.ofo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.zhuangbudong.ofo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.VISIBLE;

/**
 * Created by xxx on 17/5/2.
 */

public class ParticipantView extends ViewGroup {

    private List<View> likeViews = new ArrayList<>();

    private static final boolean DEFAULT_OMIT_CENTER = true;

    /**************
     * Default dp *
     **************/
    private static final int DEFAULT_LIKE_SPACING = 5;
    private static final int DEFAULT_OMIT_SPACING = 8;

    private EasyViewProxy easyProxy;

    /**************
     * Default px *
     **************/
    private int likeSpacing;
    private int omitSpacing;

    private View omitView;
    private int omitViewWidth;
    private int omitViewHeight;
    private boolean omitCenter;

    private int maxViewWidth = 0;

    private static final int NO_FULL = -106;
//    private int fullLikeCount = NO_FULL;

    //    private boolean isFull = false;
    private int lineCount = 0;
    private boolean addOmitView = true;

    private DisplayMetrics mMetrics;

    private static final int LAYOUT_DIRECTION_LEFT = 2601;
    private static final int LAYOUT_DIRECTION_RIGHT = 2602;

    private int childCountForLine = 0;


    private static final String TAG = "ParticipantView";


    public ParticipantView(Context context) {
        super(context);
        this.init(context, null);
    }


    public ParticipantView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }


    public ParticipantView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParticipantView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.easyProxy = new EasyViewProxy();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParticipantView);
        this.likeSpacing = a.getDimensionPixelOffset(
                R.styleable.ParticipantView_headSpacing, this.dp2px(DEFAULT_LIKE_SPACING));
        this.omitSpacing = a.getDimensionPixelOffset(
                R.styleable.ParticipantView_omitSpacing, this.dp2px(DEFAULT_OMIT_SPACING));
        this.omitCenter = a.getBoolean(R.styleable.ParticipantView_omitCenter,
                DEFAULT_OMIT_CENTER);

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        this.maxViewWidth = Math.max(this.maxViewWidth, viewWidth);

        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();


        int resultWidth = 0;
        int resultHeight = 0;

        if (this.omitView != null && this.omitView.getVisibility() == VISIBLE) {
            this.measureChild(this.omitView, widthMeasureSpec, heightMeasureSpec);
            this.omitViewWidth = this.omitView.getMeasuredWidth();
            this.omitViewHeight = this.omitView.getMeasuredHeight();
            this.omitViewWidth += this.omitSpacing;
        }

        final int childCount = this.getChildCount();
        if (this.isHasLikes()) {
            lineCount = 1;
            int space = viewWidth - paddingLeft - paddingRight;
            for (int i = 0; i < childCount; i++) {
                if (i == childCount - 1) {
                    break;
                }
                View like = this.getChildAt(i);
                if (like.getVisibility() == VISIBLE) {
                    this.measureChild(like, widthMeasureSpec, heightMeasureSpec);
                } else {
                    continue;
                }
                int likeWidth = like.getMeasuredWidth();
                int likeHeight = like.getMeasuredHeight();


                if (space >= likeWidth) {
                    space -= likeWidth;
                    if (i == childCount - 2) {
                        if (space < omitViewWidth + omitSpacing) {
                            if (lineCount == 1) {
                                childCountForLine = i + 1;
                            }
                            lineCount++;
                            break;
                        }
                    }

                } else {
                    if (lineCount == 1) {
                        childCountForLine = i;
                    }
                    lineCount++;
                    space = viewWidth - likeWidth;
                }
                space -= likeSpacing;
            }

            if (lineCount == 1) {
                childCountForLine = childCount - 1;
            }

            resultWidth += getChildAt(0).getMeasuredWidth() * childCountForLine + likeSpacing * (childCountForLine - 1);
            resultHeight += getChildAt(0).getMeasuredHeight() * lineCount + likeSpacing * (lineCount - 1);


        }

        resultWidth += paddingLeft + paddingRight;
        resultHeight += paddingTop + paddingBottom;
        this.setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? viewWidth : resultWidth,
                (heightMode == MeasureSpec.EXACTLY) ? viewHeight : resultHeight);
    }


    /**
     * {@inheritDoc}
     *
     * @param changed changed
     * @param l       l
     * @param t       t
     * @param r       r
     * @param b       b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        System.out.println(this.getWidth() + "");
        this.likeViews.clear();

        final int paddingTop = this.getPaddingTop();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();

        final int viewWidth = this.getWidth();
        final int viewHeight = this.getHeight();

        int childCount = this.getChildCount();
        if (this.isHasLikes()) {
            for (int i = 0; i < childCount; i++) {
                if (childCount > 1 && i == childCount - 1) break;
                View like = this.getChildAt(i);
                this.likeViews.add(like);
            }
        }

        int start = paddingLeft;


        if (lineCount > 0) {

            final View icon = this.likeViews.get(0);
            final int likeWidth = icon.getMeasuredWidth();
            final int likeHeight = icon.getMeasuredHeight();

            int columnCount = childCountForLine;

            int itemIndex = 0;
            for (int i = 0; i < lineCount; i++) {
                int likeLeft;
                int likeRight = 0;
                int likeBottom;
                int top = (likeHeight + likeSpacing) * i + paddingTop;
                start = paddingLeft;
                if (lineCount >= 2) {


                    int temp = likeViews.size() - 1 - itemIndex;


                    if (temp < childCountForLine) {
                        if (i == 0) {
                            columnCount = likeViews.size();
                        } else {
                            if (i == lineCount - 2) {
                                columnCount = temp;
                            } else if (i == lineCount - 1) {
                                columnCount = 0;
                            }
                        }
                    }


                } else {
                    columnCount = childCountForLine;
                }
                for (int j = 0; j < columnCount; j++) {
                    itemIndex = i * childCountForLine + j;
                    View like = this.likeViews.get(itemIndex);

                    likeLeft = start;
                    likeRight = likeLeft + likeWidth;
                    likeBottom = top + likeHeight;
                    like.layout(likeLeft, top, likeRight, likeBottom);
                    start += likeWidth;
                    start += this.likeSpacing;

                }

            }

            if (this.existOmitView()) {

                final int omitViewWidth = this.omitView.getMeasuredWidth();
                final int omitViewHeight = this.omitView.getMeasuredHeight();

                start += this.omitSpacing;
                final int omitLeft = start;
                int omitTop = paddingTop;
                if (this.omitCenter) {
                    omitTop += (likeWidth + likeSpacing) * (lineCount - 1);
                }
                final int omitRight = omitLeft + omitViewWidth;
                final int omitBottom = omitTop + omitViewHeight;
                this.omitView.layout(omitLeft, omitTop, omitRight, omitBottom);


            }
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(this.getContext(), attrs);
    }


    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


    public boolean isHasLikes() {
        return this.getChildCount() > 1;
    }


    public boolean existOmitView() {
        return this.omitView != null && this.omitView.getVisibility() == VISIBLE;
    }


    public void setOmitView(View v) {
        if (this.omitView != null) this.removeView(this.omitView);
        this.addView(v, this.getChildCount());
        this.omitView = v;
        this.addOmitView = true;
        this.invalidate();
    }


    public View getOmitView() {
        return this.omitView;
    }


    public List<View> getLikeViews() {
        return likeViews;
    }


    public LinkedList<View> getViewCache() {
        return this.easyProxy.getCacheViews();
    }


    @Override
    public final void addView(View child) {
        if (this.omitView == null) {
            throw new RuntimeException("You must addView(...) after setOmitView(View v)");
        }
        try {
            this.easyProxy.addViewProxy(child, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addView(View child, int index) {
        if (!this.addOmitView && this.omitView == null) {
            throw new RuntimeException("You must addView(...) after setOmitView(View v)");
        }
        try {
            this.easyProxy.addViewProxy(child, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeView(View view) {
        try {
            this.easyProxy.removeViewProxy(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void removeViewAt(int index) {
        try {
            this.easyProxy.removeViewAtProxy(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Dp to px
     *
     * @param dp dp
     * @return px
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.mMetrics);
    }


    private class EasyViewProxy {

        private static final int DEFAULT_MAX_CACHE_VIEW_COUNT = 17;

        private LinkedList<View> mCacheViews;
        private int maxSize;


        public EasyViewProxy() {
            this(DEFAULT_MAX_CACHE_VIEW_COUNT);
        }


        public EasyViewProxy(int maxSize) {
            this.mCacheViews = new LinkedList<>();
            this.maxSize = maxSize;
        }


        private void addViewCache(View view) {
            this.mCacheViews.add(0, view);
            if (this.mCacheViews.size() > this.maxSize) {
                this.mCacheViews.remove(this.mCacheViews.size() - 2);
            }
        }


        private void removeViewCache(View view) {
            if (this.mCacheViews.contains(view)) this.mCacheViews.remove(view);
        }


        private void removeViewAtCache(int index) {
            if (index > this.mCacheViews.size() - 1) return;
            if (this.mCacheViews.get(index) != null) this.mCacheViews.remove(index);
        }


        public void addViewProxy(View child, int index) {
            if (ParticipantView.this.getChildCount() > 0 &&
                    index > ParticipantView.this.getChildCount() - 1) {
                index = ParticipantView.this.childCountForLine - 1;
            }
            if (ParticipantView.this.childCountForLine != 0) {
                ParticipantView.super.addView(child, index);
                //ParticipantView.super.removeViewAt(ParticipantView.this.fullLikeCount);
                // Add cache
//                this.addViewCache(child, ParticipantView.this.fullLikeCount - 1);
                this.addViewCache(child);
            } else {
                ParticipantView.super.addView(child, 0);
                // Add cache
                this.addViewCache(child);
            }
        }


        public void removeViewProxy(View view) {
            if (view == null) return;
            if (view.getParent() == null) return;
            if (ParticipantView.this.existOmitView() &&
                    view.hashCode() == ParticipantView.this.omitView.hashCode()) {
                return;
            }

            if (ParticipantView.this.childCountForLine != 0) {
                ParticipantView.super.removeView(view);

                // Remove cache
                this.removeViewCache(view);

                // Refresh cache
                View cache = this.getViewCache(ParticipantView.this.childCountForLine - 1);
                if (cache != null &&
                        ParticipantView.this.existOmitView() &&
                        cache.hashCode() != ParticipantView.this.omitView.hashCode()) {
                    if (cache.getParent() != null) {
                        ((ViewGroup) cache.getParent()).removeView(cache);
                    }
                    ParticipantView.super.addView(cache, ParticipantView.this.childCountForLine - 1);
                }
            } else {
                ParticipantView.super.removeView(view);

                // Remove cache
                this.removeViewCache(view);
            }
        }


        public void removeViewAtProxy(int index) {
            View o = ParticipantView.this.getChildAt(index);

            // Overstep
            if (index > ParticipantView.this.getChildCount() - 1) {
                index = ParticipantView.this.childCountForLine - 1;
            }

            if (ParticipantView.this.omitView != null) {
                if (o == null) o = ParticipantView.super.getChildAt(index);

                if (ParticipantView.this.existOmitView() &&
                        o.hashCode() == ParticipantView.this.omitView.hashCode()) {
                    return;
                }

                if (ParticipantView.this.childCountForLine != 0) {
                    ParticipantView.super.removeViewAt(index);
                    // Remove cache
                    this.removeViewAtCache(index);
                    // Refresh cache
                    View cache = this.getViewCache(ParticipantView.this.childCountForLine - 1);
                    if (cache != null &&
                            ParticipantView.this.existOmitView() &&
                            cache.hashCode() != ParticipantView.this.omitView.hashCode()) {
                        if (cache.getParent() != null) {
                            ((ViewGroup) cache.getParent()).removeView(cache);
                        }
                        ParticipantView.super.addView(cache, ParticipantView.this.childCountForLine - 1);
                    }
                } else {
                    ParticipantView.super.removeViewAt(index);
                    // Remove cache
                    this.removeViewCache(o);
                }
            } else {
                ParticipantView.super.removeViewAt(index);
                // Remove cache
                this.removeViewAtCache(index);
                if (ParticipantView.this.childCountForLine != 0) {
                    View cache = this.getViewCache(index);
                    // Refresh cache
                    if (cache != null) {
                        ParticipantView.super.addView(cache, ParticipantView.this.childCountForLine - 1);
                    }
                }
            }
        }


        @SuppressWarnings("unchecked")
        public <V extends View> V getViewCache(int position) {
            if (this.mCacheViews.size() - 1 > position && this.mCacheViews.get(position) != null) {
                return (V) this.mCacheViews.get(position);
            }
            return null;
        }


        public LinkedList<View> getCacheViews() {
            return mCacheViews;
        }
    }
}

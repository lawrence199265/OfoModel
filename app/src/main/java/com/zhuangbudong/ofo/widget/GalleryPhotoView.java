package com.zhuangbudong.ofo.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by xxx on 17/3/20.
 */

public class GalleryPhotoView extends PhotoView {
    private static final int ANIMA_DURATION = 350;

    private BitmapTransform bitmapTransform;

    private boolean isPlayingEnterAnima = false;
    private boolean isPlayingExitAnima = false;
    private OnEnterAnimaEndListener enterAnimaEndListener;
    private OnExitAnimaEndListener exitAnimaEndListener;
    private OnAllFinishListener allFinishListener;

    private Point globalOffset;
    private float[] scaleRatios;
    private RectF clipBounds;

    public GalleryPhotoView(Context context) {
        super(context);
    }

    public GalleryPhotoView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public GalleryPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        bitmapTransform = new BitmapTransform();
        globalOffset = new Point();
    }

    @Override
    public void draw(Canvas canvas) {
        if (clipBounds != null) {
            canvas.clipRect(clipBounds);
            clipBounds = null;
        }
        super.draw(canvas);
    }

    public void playEnterAnimation(final Rect from, OnEnterAnimaEndListener onEnterAnimaEndListener) {
        this.enterAnimaEndListener = onEnterAnimaEndListener;
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                playEnterAnimationInternal(from);
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

    }

    public void playExitAnimation(Rect to, @Nullable View alphaView, @Nullable OnExitAnimaEndListener l) {
        this.exitAnimaEndListener = l;
        playExitAnimation(to, alphaView);

    }

    private void playExitAnimation(Rect to, View alphaView) {
        if (isPlayingExitAnima) {
            if (exitAnimaEndListener != null) {
                exitAnimaEndListener.onExitAnimaEnd();
            }
            return;

        }
        float currentScale = getScale();
        if (currentScale > 1.0f)
            setScale(currentScale);
        Rect from = getViewBounds();
        Rect drawableBounds = getDrawableBounds(getDrawable());
        Rect target = new Rect(to);

        from.offset(-globalOffset.x, -globalOffset.y);
        target.offset(-globalOffset.x, -globalOffset.y);
        if (drawableBounds == null) {
            if (exitAnimaEndListener != null) {
                enterAnimaEndListener.onEnterAnimaEnd();
            }
            return;
        }
        bitmapTransform.animaTranslate(from.centerX(), target.centerX(), from.centerY(), target.centerY());
        float scale = calculateScaleByCenterCrop(from, drawableBounds, target);

        bitmapTransform.animateScale(getScale(), scale, from.centerX(), from.centerY());

        if (alphaView != null) {
            bitmapTransform.animaAlpha(alphaView, 1.0f, 0);
        }

        if (target.width() < from.width() || target.height() < from.height()) {
            bitmapTransform.animaClip(from, target);
        }


        bitmapTransform.start(new OnAllFinishListener() {
            @Override
            public void onAllFinish() {
                if (exitAnimaEndListener != null) {
                    exitAnimaEndListener.onExitAnimaEnd();
                }
            }
        });

    }

    private float calculateScaleByCenterCrop(Rect from, Rect drawableBounds, Rect target) {
        int viewWidth = from.width();
        int viewHeight = from.height();
        int imageHeight = drawableBounds.height();
        int imageWidth = drawableBounds.width();

        float result;


        /**
         * 假设原始图片高h，宽w ，Imageview的高y，宽x
         * 判断高宽比例，如果目标高宽比例大于原图，则原图高度不变，宽度为(w1 = (h * x) / y)拉伸
         * 画布宽高(w1,h),在原图的((w - w1) / 2, 0)位置进行切割
         */
        int t = (viewHeight / viewWidth) - (imageHeight / imageWidth);

        if (t > 0) {
            int w1 = (imageHeight * viewWidth) / viewHeight;
            result = Math.min(target.width() * 1.0f / w1 * 1.0f, target.height() * 1.0f / imageHeight * 1.0f);

        } else {
            int h1 = (viewHeight * imageWidth) / viewWidth;
            result = Math.max(target.width() * 1.0f / imageWidth * 1.0f, target.height() * 1.0f / h1 * 1.0f);
        }

        return result;
    }

    private Rect getDrawableBounds(Drawable drawable) {
        if (drawable == null) return null;
        Rect result = new Rect();
        Rect tDrawableRect = drawable.getBounds();
        Matrix drawableMatrix = getImageMatrix();

        float[] values = new float[9];
        drawableMatrix.getValues(values);

        //setValue translate/scale
        result.left = (int) values[Matrix.MTRANS_X];
        result.top = (int) values[Matrix.MTRANS_Y];
        result.right = (int) (result.left + tDrawableRect.width() * values[Matrix.MSCALE_X]);
        result.bottom = (int) (result.top + tDrawableRect.height() * values[Matrix.MSCALE_Y]);

        return result;
    }

    private Rect getViewBounds() {
        Rect result = new Rect();
        getGlobalVisibleRect(result, globalOffset);
        return result;
    }

    private void playEnterAnimationInternal(Rect from) {
        if (isPlayingEnterAnima || from == null) {
            return;
        }
        final Rect tFrom = new Rect(from);
        final Rect to = new Rect();
        //获取该view在屏幕坐标系的偏移量->rect globalOffset就是该view离屏幕坐标系原点的距离
        getGlobalVisibleRect(to, globalOffset);

        scaleRatios = calculateRatios(tFrom, to);

        setPivotX(from.centerX() / to.width());
        setPivotY(from.centerY() / to.height());


        final AnimatorSet enterSet = new AnimatorSet();
        enterSet.play(ObjectAnimator.ofFloat(this, View.X, tFrom.left, to.left))
                .with(ObjectAnimator.ofFloat(this, View.Y, tFrom.top, to.top))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_X, scaleRatios[0], 1.0f))
                .with(ObjectAnimator.ofFloat(this, View.SCALE_Y, scaleRatios[1], 1.0f));
        enterSet.setDuration(ANIMA_DURATION);
        enterSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isPlayingEnterAnima = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isPlayingEnterAnima = false;
                if (enterAnimaEndListener != null) {
                    enterAnimaEndListener.onEnterAnimaEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isPlayingEnterAnima = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                isPlayingEnterAnima = true;
            }
        });
        enterSet.start();


    }

    private float[] calculateRatios(Rect tFrom, Rect to) {
        float[] result = new float[2];
        float widthRatio = tFrom.width() * 1.0f / to.width() * 1.0f;
        float heightRatio = tFrom.height() * 1.0f / to.height() * 1.0f;
        result[0] = widthRatio;
        result[1] = heightRatio;
        return result;
    }

    public interface OnEnterAnimaEndListener {
        void onEnterAnimaEnd();
    }

    public interface OnExitAnimaEndListener {
        void onExitAnimaEnd();
    }

    interface OnAllFinishListener {
        void onAllFinish();
    }

    private class BitmapTransform implements Runnable {
        static final float PRECISION = 10000f;
        View targetView;
        volatile boolean isRunning;

        Scroller translateScroller;
        Scroller scaleScroller;
        Scroller alphaScroller;
        Scroller clipScroller;

        Interpolator defaultInterpolator = new DecelerateInterpolator();


        int scaleCenterX;
        int scaleCenterY;


        float scaleX;
        float scaleY;

        float alpha;

        int dx;
        int dy;


        int preTranslateX;
        int preTranslateY;


        RectF mClipRectf;
        RectF clipTo;
        RectF clipFrom;
        Matrix tempMatrix;
        OnAllFinishListener onAllFinishListener;


        public BitmapTransform() {
            isRunning = false;
            translateScroller = new Scroller(getContext(), defaultInterpolator);
            scaleScroller = new Scroller(getContext(), defaultInterpolator);
            alphaScroller = new Scroller(getContext(), defaultInterpolator);
            clipScroller = new Scroller(getContext(), defaultInterpolator);
            mClipRectf = new RectF();
            tempMatrix = new Matrix();

        }

        void animateScale(float fromX, float fromY, float toX, float toY, int centerX, int centerY) {
            this.scaleCenterX = centerX;
            this.scaleCenterY = centerY;
            scaleScroller.startScroll((int) (fromX * PRECISION), (int) (fromY * PRECISION), (int) ((toX - fromX) * PRECISION), (int) ((toY - fromY) * PRECISION), ANIMA_DURATION);
        }

        void animateScale(float from, float to, int centerX, int centerY) {
            animateScale(from, from, to, to, centerX, centerY);
        }

        void animaTranslate(int fromX, int toX, int fromY, int toY) {
            preTranslateX = 0;
            preTranslateY = 0;
            translateScroller.startScroll(0, 0, toX - fromX, toY - fromY, ANIMA_DURATION);
        }

        void animaAlpha(View target, float fromAlpha, float toAlpha) {
            this.targetView = target;
            alphaScroller.startScroll((int) (fromAlpha * PRECISION), 0, (int) ((toAlpha - fromAlpha) * PRECISION), 0, ANIMA_DURATION);
        }

        void animaClip(Rect clipFrom, Rect clipTo) {
            this.clipFrom = new RectF(clipFrom);
            this.clipTo = new RectF(clipTo);

            if (!clipFrom.isEmpty() && !clipTo.isEmpty()) {
                //算出裁剪比率
                float dx = Math.min(1.0f, clipTo.width() * 1.0f / clipFrom.width() * 1.0f);
                float dy = Math.min(1.0f, clipTo.height() * 1.0f / clipFrom.height() * 1.0f);
                //因为scroller是对起始值和终点值之间的数值影响，所以减去1，如果为0，意味着不裁剪，因为目标值比开始值大，而画布无法再扩大了，所以忽略
                dx = dx - 1;
                dy = dy - 1;
                //从1开始,乘以1w保证精度
                clipScroller.startScroll((int) (0 * PRECISION), (int) (0 * PRECISION), (int) (dx * PRECISION), (int) (dy * PRECISION), ANIMA_DURATION);
            }
        }


        @Override
        public void run() {
            boolean isAllFinish = true;
            if (scaleScroller.computeScrollOffset()) {
                scaleX = scaleScroller.getCurrX() / PRECISION;
                scaleY = scaleScroller.getCurrY() / PRECISION;
                isAllFinish = false;
            }
            if (translateScroller.computeScrollOffset()) {
                int curX = translateScroller.getCurrX();
                int curY = translateScroller.getCurrY();
                //当前和上一次的移动距离
                dx += curX - preTranslateX;
                dy += curY - preTranslateY;
                preTranslateX = curX;
                preTranslateY = curY;

                isAllFinish = false;

            }


            if (alphaScroller.computeScrollOffset()) {
                alpha = (float) alphaScroller.getCurrX() / PRECISION;
                isAllFinish = false;
            }

            if (clipScroller.computeScrollOffset()) {
                float curX = Math.abs((float) clipScroller.getCurrX() / PRECISION);
                float curY = Math.abs((float) clipScroller.getCurrY() / PRECISION);

                float dx = clipFrom.width() * curX;
                float dy = clipFrom.height() * curY;

                float ratioLeftAndRight = Math.abs(clipFrom.left - clipTo.left) / Math.abs(clipFrom.right - clipTo.right);
                float ratioTopAndBottom = Math.abs(clipFrom.top - clipTo.top) / Math.abs(clipFrom.bottom - clipTo.bottom);

                //+1---->要保留现在的位置?
                float dClipRight = dx / (ratioLeftAndRight + 1);
                float dClipLeft = dx - dClipRight;

                float dClipTop = dy / (ratioTopAndBottom + 1);
                float dClipBottom = dy - dClipTop;

                mClipRectf.left = clipFrom.left + dClipLeft;
                mClipRectf.right = clipFrom.right - dClipRight;
                mClipRectf.top = clipFrom.top + dClipTop;
                mClipRectf.bottom = clipFrom.bottom - dClipBottom;


                if (!mClipRectf.isEmpty()) {
                    clipBounds = mClipRectf;
                }
                isAllFinish = false;
                if (!isAllFinish) {
                    setMatrixValue();
                    postExecuteSelf();
                } else {
                    isRunning = false;
                    reset();
                    if (onAllFinishListener != null) {
                        onAllFinishListener.onAllFinish();
                    }
                }


            }

        }

        private void reset() {
            scaleX = 0;
            scaleY = 0;
            scaleCenterY = 0;
            scaleCenterX = 0;
            alpha = 0;
            preTranslateX = 0;
            preTranslateY = 0;
            dx = 0;
            dy = 0;

        }

        void start(OnAllFinishListener onAllFinishListener) {
            if (isRunning) {
                stop(false);
            }
            this.onAllFinishListener = onAllFinishListener;
            isRunning = true;
            postExecuteSelf();
        }

        private void stop(boolean reset) {
            scaleScroller.abortAnimation();
            translateScroller.abortAnimation();
            alphaScroller.abortAnimation();
            clipScroller.abortAnimation();
            onAllFinishListener = null;
            isRunning = false;
            if (reset) {
                reset();
            }
        }

        private void postExecuteSelf() {
            if (isRunning) {
                post(this);
            }
        }

        private void setMatrixValue() {
            if (mAttacher == null) {
                return;
            }
            resetSuppMatrix();
            postMatrixScale(scaleX, scaleY, scaleCenterX, scaleCenterY);
            postMatrixTranslate(dx, dy);
            if (targetView != null) {
                targetView.setAlpha(alpha);
                applyMatrix();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        bitmapTransform.stop(true);
        super.onDetachedFromWindow();
    }
}

package com.zhuangbudong.ofo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zhuangbudong.ofo.R;

/**
 * Created by xunzongxia on 17/2/24.
 */

public class AutoIndicator extends LinearLayout {
    private Context context;
    private Paint mPaint;
    private View moveView;
    private int mCurrentPosition;
    private float mPositionOffset;
    private int defaultColor = Color.GRAY;
    private int selectedColor = R.color.main_color;

    private int mItemCount = DEFAULT_ITEM_COUNT;
    private int mPadding = DEFAULT_PADDING;
    private int mRadius = DEFAULT_RADIUS;
    private int mDistanceBtwItem = mRadius * 2 + mPadding;

    private static final int DEFAULT_ITEM_COUNT = 3;
    private static final int DEFAULT_PADDING = 30;
    public static final int DEFAULT_RADIUS = 10;

    public AutoIndicator(Context context) {
        this(context, null);
    }

    public AutoIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setWillNotDraw(false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(defaultColor);

        moveView = new MoveView(context);
        addView(moveView);
    }


    public void setItemCount(int mItemCount) {
        this.mItemCount = mItemCount;
        requestLayout();
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        this.mDistanceBtwItem = mRadius * 2 + mPadding;
        requestLayout();
    }

    public void setPadding(int padding) {
        this.mPadding = padding;
        this.mDistanceBtwItem = mRadius * 2 + mPadding;
        requestLayout();
    }

    public void setPositionAndOffset(int position, float offset) {
        this.mCurrentPosition = position;
        this.mPositionOffset = offset;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mPadding + (mRadius * 2 + mPadding) * mItemCount, 2 * mRadius + 2 * mPadding);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        moveView.layout((int) (mPadding + mDistanceBtwItem * (mCurrentPosition + mPositionOffset)), mPadding, (int) (mDistanceBtwItem * (1 + mCurrentPosition + mPositionOffset)), mPadding + mRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mItemCount; i++) {
            canvas.drawCircle(mRadius + mPadding + mRadius * i * 2 + mPadding * i, mRadius + mPadding, mRadius, mPaint);
        }
    }

    private class MoveView extends View {

        private Paint mPaint;

        public MoveView(Context context) {
            this(context, null);
        }

        public MoveView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initMoveView();
        }

        private void initMoveView() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(ContextCompat.getColor(context, selectedColor));
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(mRadius * 2, mRadius * 2);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        }
    }
}

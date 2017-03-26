package com.zhuangbudong.ofo.widget.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zhuangbudong.ofo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxu on 17/2/16.
 */

public class CircleIndicator extends View {
    private ViewPager viewPager;
    private List<CircleShape> indicators;
    private CircleShape indicator;

    private int curItemPosition;
    private float curItemPositionOffset;
    private float indicatorRadius;
    private float indicatorMargin;
    private int indicatorBackground;
    private int indicatorSelectedBackground;
    private Gravity indicatorLayoutGravity;
    private Mode indicatorMode;
    private static final String TAG = "CircleIndicator";

    public enum Mode {
        INSIDE,
        OUTSIDE,
        SOLO
    }

    public enum Gravity {
        LEFT,
        RIGHT,
        CENTER
    }

    private final int DEFAULT_INDICATOR_RADIUS = 10;
    private final int DEFAULT_INDICATOR_MARGIN = 10;
    private final int DEFAULT_INDICATOR_BACKGROUND = Color.BLUE;
    private final int DEFAULT_INDICATOR_SELECTED_BACKGROUND = Color.RED;
    private final int DEFAULT_INDICATOR_LAYOUT_GRAVITY = Gravity.CENTER.ordinal();
    private final int DEFAULT_INDICATOR_MODE = Mode.SOLO.ordinal();


    public CircleIndicator(Context context) {
        this(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        indicators = new ArrayList<>();
        resolveAttrs(context, attrs);
    }



    private void resolveAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        indicatorRadius = typedArray.getDimension(R.styleable.CircleIndicator_radius, DEFAULT_INDICATOR_RADIUS);
        indicatorMargin = typedArray.getDimension(R.styleable.CircleIndicator_margin, DEFAULT_INDICATOR_MARGIN);
        indicatorBackground = typedArray.getColor(R.styleable.CircleIndicator_normalBackground, DEFAULT_INDICATOR_BACKGROUND);
        indicatorSelectedBackground = typedArray.getColor(R.styleable.CircleIndicator_selectedBackground, DEFAULT_INDICATOR_SELECTED_BACKGROUND);
        int gravity = typedArray.getInt(R.styleable.CircleIndicator_layoutGravity, DEFAULT_INDICATOR_LAYOUT_GRAVITY);
        indicatorLayoutGravity = Gravity.values()[gravity];
        int mode = typedArray.getInt(R.styleable.CircleIndicator_mode, DEFAULT_INDICATOR_MODE);
        indicatorMode = Mode.values()[mode];
        typedArray.recycle();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        createIndicatorItems();
        createMovingIndicator();
        setUpViewpagerListener();

    }

    private void setUpViewpagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (indicatorMode != Mode.SOLO) {
                    moveToSelectedPosition(position, positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (indicatorMode != Mode.SOLO) {
                    moveToSelectedPosition(position, 0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);



            }
        });

    }

    private void moveToSelectedPosition(int position, float positionOffset) {
        curItemPosition = position;
        curItemPositionOffset = positionOffset;
        requestLayout();
        invalidate();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int width = getWidth();
        final int height = getHeight();
        layoutIndicator(width, height);
        layoutMovingItem(curItemPosition, curItemPositionOffset);
    }

    private void layoutMovingItem(int curItemPosition, float curItemPositionOffset) {
        if (indicator == null) {
            throw new IllegalStateException("forget to create movingItem?");
        }

        if (indicators.size() == 0) {
            return;
        }
        CircleShape item = indicators.get(curItemPosition);
        indicator.resizeShape(item.getWidth(), item.getHeight());
        float x = item.getX() + (indicatorMargin + indicatorRadius * 2) * curItemPositionOffset;
        indicator.setX(x);
        indicator.setY(item.getY());
    }

    private void layoutIndicator(int width, int height) {
        if (indicators == null) {
            throw new IllegalArgumentException("indicators not null");
        }
        final float yCoordinator = height * 0.5f;
        final float startPosition = startDrawPosition(width);
        for (int i = 0; i < indicators.size(); i++) {
            CircleShape item = indicators.get(i);
            item.resizeShape(2 * indicatorRadius, 2 * indicatorRadius);
            item.setY(yCoordinator - indicatorRadius);
            float x = startPosition + (indicatorMargin + indicatorRadius * 2) * i;
            item.setX(x);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int src = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        for (CircleShape shape : indicators) {
            drawItem(canvas, shape);
        }
        if (indicator != null) {
            drawItem(canvas, indicator);
        }
        canvas.restoreToCount(src);
    }

    public void setIndicatorRadius(float indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
    }

    public void setIndicatorMargin(float indicatorMargin) {
        this.indicatorMargin = indicatorMargin;
    }

    public void setIndicatorBackground(int indicatorBackground) {
        this.indicatorBackground = indicatorBackground;
    }

    public void setIndicatorSelectedBackground(int indicatorSelectedBackground) {
        this.indicatorSelectedBackground = indicatorSelectedBackground;
    }

    public void setIndicatorLayoutGravity(Gravity indicatorLayoutGravity) {
        this.indicatorLayoutGravity = indicatorLayoutGravity;
    }

    public void setIndicatorMode(Mode indicatorMode) {
        this.indicatorMode = indicatorMode;
    }

    private void drawItem(Canvas canvas, CircleShape shape) {
        canvas.save();
        canvas.translate(shape.getX(), shape.getY());
        shape.getShape().draw(canvas);
        canvas.restore();
    }

    private float startDrawPosition(int width) {
        if (indicatorLayoutGravity == Gravity.LEFT) {
            return 0;
        }
        float indicatorWidth = indicators.size() * (2 * indicatorRadius + indicatorMargin) - indicatorMargin;
        if (width < indicatorWidth) {
            return 0;
        }
        if (indicatorLayoutGravity == Gravity.CENTER) {
            return (width - indicatorWidth) / 2;
        }
        return width - indicatorWidth;
    }

    private void createMovingIndicator() {
        OvalShape moveShape = new OvalShape();
        ShapeDrawable moveDrawable = new ShapeDrawable(moveShape);
        indicator = new CircleShape(moveDrawable);
        Paint paint = moveDrawable.getPaint();
        paint.setColor(indicatorSelectedBackground);
        paint.setAntiAlias(true);
        switch (indicatorMode) {
            case INSIDE:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                break;
            case OUTSIDE:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                break;
            case SOLO:
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                break;
        }
        indicator.setPaint(paint);

    }

    private void createIndicatorItems() {
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            OvalShape circle = new OvalShape();
            ShapeDrawable drawable = new ShapeDrawable(circle);
            CircleShape shape = new CircleShape(drawable);
            Paint paint = drawable.getPaint();
            paint.setColor(indicatorBackground);
            paint.setAntiAlias(true);
            shape.setPaint(paint);
            indicators.add(shape);
        }
    }
}

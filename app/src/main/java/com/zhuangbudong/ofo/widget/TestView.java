package com.zhuangbudong.ofo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xunzongxia on 17/3/21.
 */

public class TestView extends View {
    private Path star;
    private Paint starPaint;

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint.setStyle(Paint.Style.STROKE);
        starPaint.setColor(Color.BLACK);
        star(200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(star, starPaint);
    }

    private void star(float length) {
        star = new Path();
        float dis1 = (float) ((length / 2) / Math.tan((54f / 180) * Math.PI));
        float dis2 = (float) (length * Math.sin((72f / 180) * Math.PI));
        float dis3 = (float) (length * Math.cos((72f / 180) * Math.PI));
        star.moveTo(length / 2, 0);
        star.lineTo(length / 2 - dis3, dis2);

        star.lineTo(length, dis1);
        star.lineTo(0, dis1);
        star.lineTo(length / 2 + dis3, dis2);
        star.lineTo(length / 2, 0);
    }
}

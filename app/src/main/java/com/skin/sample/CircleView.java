package com.skin.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.skin.mylibrary.SkinViewSupport;
import com.skin.mylibrary.utils.SkinResources;

/**
 * Copyright (c), 2018-2019
 *
 * @author: lixin
 * Date: 2019-04-29
 * Description:
 */
public class CircleView extends View implements SkinViewSupport {

    private Paint mPaint;
    private int circleColorResId;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        a.getResourceId(R.styleable.CircleView_circle_color, 0);
        circleColorResId = a.getResourceId(R.styleable.CircleView_circle_color, Color.RED);
        a.recycle();

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(circleColorResId));
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 200);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSpecSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }

    public void setCircleColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    public void applySkin() {
        if (circleColorResId != 0) {
            int color = SkinResources.getInstance().getColor(circleColorResId);
            setCircleColor(color);
        }
    }
}

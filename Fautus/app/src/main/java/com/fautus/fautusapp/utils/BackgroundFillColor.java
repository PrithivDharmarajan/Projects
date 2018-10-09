package com.fautus.fautusapp.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.fautus.fautusapp.R;


public class BackgroundFillColor extends View {
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    private PointF center = new PointF();
    private RectF circleRect = new RectF();
    private Path segment = new Path();
    private Paint strokePaint = new Paint();
    private Paint fillPaint = new Paint();

    private int radius;

    private int fillColor;
    private int strokeColor;
    private float strokeWidth;
    private int value;

    public BackgroundFillColor(Context context) {
        this(context, null);
    }

    public BackgroundFillColor(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BackgroundFillColor,
                0, 0);

        try {
            fillColor = a.getColor(R.styleable.BackgroundFillColor_fillColor, ContextCompat.getColor(context, R.color.sky_blue));
            strokeColor = a.getColor(R.styleable.BackgroundFillColor_strokeColor,ContextCompat.getColor(context, R.color.white));
            strokeWidth = a.getFloat(R.styleable.BackgroundFillColor_strokeWidth, 1f);
            value = a.getInteger(R.styleable.BackgroundFillColor_value, 0);
            adjustValue(value);
        } finally {
            a.recycle();
        }

        fillPaint.setColor(fillColor);
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setStyle(Paint.Style.STROKE);
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        fillPaint.setColor(fillColor);
        invalidate();
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        strokePaint.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        strokePaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setValue(int value) {
        adjustValue(value);
        setPaths();

        invalidate();
    }

    public int getValue() {
        return value;
    }

    private void adjustValue(int value) {
        this.value = Math.min(MAX_VALUE, Math.max(MIN_VALUE, value));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        center.x = getWidth() / 2;
        center.y = getHeight() / 2;
        radius = Math.min(getWidth(), getHeight()) - (int) strokeWidth;

        circleRect.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

        setPaths();
    }

    private void setPaths() {
        float y = center.y + radius - (2 * radius * value / 100 - 1);
        float x = center.x - (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(y - center.y, 2));

        float angle = (float) Math.toDegrees(Math.atan((center.y - y) / (x - center.x)));
        float startAngle = 180 - angle;
        float sweepAngle = 2 * angle - 180;

        segment.rewind();
        segment.addArc(circleRect, startAngle, sweepAngle);
        segment.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(segment, fillPaint);
        canvas.drawCircle(center.x, center.y, radius, strokePaint);
    }
}

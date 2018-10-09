package com.smaat.spark.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.smaat.spark.R;

public class RoundEdgeFullImageView extends android.support.v7.widget.AppCompatImageView {


    public RoundEdgeFullImageView(Context context) {
        super(context);
    }

    public RoundEdgeFullImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundEdgeFullImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = getContext().getResources().getDimension(R.dimen.size25);
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
//        float scaleRatio = getResources().getDisplayMetrics().density;
//        float dimenPix = getResources().getDimension(R.dimen.size150);
//        float dimenOrginal = dimenPix/scaleRatio;
//        RectF rect = new RectF(0, 0, this.getWidth(), getResources().getDimension(R.dimen.size150));
//        RectF rectz = new RectF()
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);

    }

}
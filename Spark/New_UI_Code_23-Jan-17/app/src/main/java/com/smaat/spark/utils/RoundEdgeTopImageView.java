package com.smaat.spark.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.smaat.spark.R;

public class RoundEdgeTopImageView extends android.support.v7.widget.AppCompatImageView {


    public RoundEdgeTopImageView(Context context) {
        super(context);
    }

    public RoundEdgeTopImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundEdgeTopImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //float radius = 36.0f;  
//        Path clipPath = new Path();
//        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
////        RectF rectz = new RectF()
//        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
//        canvas.clipPath(clipPath);
//        super.onDraw(canvas);

        float radius = getContext().getResources().getDimension(R.dimen.size25);
        Path path = getPath(new RectF(0, 0, this.getWidth(), this.getHeight()),
                radius, true, true, false, false);

//        float scaleRatio = getResources().getDisplayMetrics().density;
//        float dimenPix = getResources().getDimension(R.dimen.size150);
//        float dimenOrginal = dimenPix/scaleRatio;
//        Path path = getPath(new RectF(0, 0, this.getWidth(), getContext().getResources().getDimension(R.dimen.size150)),
//                radius, true, true, false, false);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    private Path getPath(RectF bounds, float radius, boolean topLeft,
                        boolean topRight, boolean bottomRight, boolean bottomLeft) {

        final float twoRadius = radius * 2;
        final Path path = new Path();
        final RectF cornerRect = new RectF();
        cornerRect.set(0, 0, twoRadius, twoRadius);

        if (topLeft) {
            path.arcTo(cornerRect, 180f, 90f);
        }
        else {
            path.moveTo(0f, 0f);
        }

        if (topRight) {
            cornerRect.offsetTo(bounds.width() - twoRadius, 0f);
            path.arcTo(cornerRect, 270f, 90f);
        }
        else {
            path.lineTo(bounds.width(), 0f);
        }

        if (bottomRight) {
            cornerRect.offsetTo(bounds.width() - twoRadius, bounds.height() - twoRadius);
            path.arcTo(cornerRect, 0f, 90f);
        }
        else {
            path.lineTo(bounds.width(), bounds.height());
        }

        if (bottomLeft) {
            cornerRect.offsetTo(0f, bounds.height() - twoRadius);
            path.arcTo(cornerRect, 90f, 90f);
        }
        else {
            path.lineTo(0f, bounds.height());
        }

        path.close();
        return path;
    }

}
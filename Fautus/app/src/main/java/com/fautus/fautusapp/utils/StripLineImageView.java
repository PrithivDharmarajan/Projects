package com.fautus.fautusapp.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.fautus.fautusapp.R;

/**
 * Created by sys on 02-May-17.
 */

public class StripLineImageView extends android.support.v7.widget.AppCompatImageView {

    public StripLineImageView(Context context) {
        super(context);

        invalidate();
        // TODO Auto-generated constructor stub
    }

    public StripLineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        invalidate();
    }

    public StripLineImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.sky_blue));
        paint.setStrokeWidth(5);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.full_place_holder_img);
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//        canvas.drawBitmap(bitmap,getWidth(),getHeight(),paint);

        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);



    }
}
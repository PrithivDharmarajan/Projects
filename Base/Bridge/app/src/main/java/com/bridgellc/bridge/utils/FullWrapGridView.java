package com.bridgellc.bridge.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class FullWrapGridView extends GridView {
    public FullWrapGridView(Context context) {
        super(context);
    }

    public FullWrapGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullWrapGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE / 3, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
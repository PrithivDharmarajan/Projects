package com.smaat.jolt.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	private boolean isPagingEnabled = false;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (isPagingEnabled) {
			return super.onInterceptTouchEvent(event);
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isPagingEnabled) {
			return super.onTouchEvent(event);
		}
		return false;
	}

	public void setPagingEnabled(boolean pagingEnabled) {
		isPagingEnabled = pagingEnabled;
	}
}

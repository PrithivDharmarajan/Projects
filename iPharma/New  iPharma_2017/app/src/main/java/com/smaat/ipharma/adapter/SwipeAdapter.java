package com.smaat.ipharma.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.utils.AppConstants;


import java.util.ArrayList;

import static com.smaat.ipharma.utils.AppConstants.SELECTED_POSITION;

/**
 * Created by sys on 11/11/2016.
 */

public class SwipeAdapter extends PagerAdapter {
    ArrayList<HistoryOrderEntity> Swipearraylist;
    private LayoutInflater inflater;
    Context context;

    public SwipeAdapter(Activity ctx, ArrayList<HistoryOrderEntity> mSwipearray) {
        this.Swipearraylist = mSwipearray;
        inflater = ctx.getLayoutInflater();
        context = ctx;
    }


    @Override
    public int getCount() {
        return Swipearraylist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o==view;
    }

    @Override
    public Object instantiateItem(final View container, int position) {
        final ViewGroup nullParent = null;
        final View pagerview = inflater.inflate(
                R.layout.adapter_swipe_layout, nullParent,false);


        ImageView swipe_img = (ImageView) pagerview.findViewById(R.id.img_swipe);
        //Log.e("getPrescriptionURL","getPrescriptionURL"+AppConstants.BASE_URL2 + "/" +Swipearraylist.get(position).getPrescriptionURL());
        Glide.with(context).load(AppConstants.BASE_URL2 + "/" + Swipearraylist.get(position).getPrescriptionURL())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(swipe_img);

        ((ViewPager) container).addView(pagerview, 0);
        return pagerview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

}

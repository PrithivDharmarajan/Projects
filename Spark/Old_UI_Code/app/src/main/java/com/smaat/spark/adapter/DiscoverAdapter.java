package com.smaat.spark.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.spark.R;
import com.smaat.spark.entity.outputEntity.DiscoverEntity;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.ui.VideoScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.Holder> {

    private Context mContext;
    private ArrayList<DiscoverEntity> mDiscoverTempList;
    private int ViewTotWidthInt;

    public DiscoverAdapter(Context context, ArrayList<DiscoverEntity> discoverTempEntities) {
        mContext = context;
        mDiscoverTempList = discoverTempEntities;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((HomeScreen) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        ViewTotWidthInt = displaymetrics.widthPixels;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_discover_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {

        DiscoverEntity discoverRes = mDiscoverTempList.get(position);

        RelativeLayout.LayoutParams layParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        int viewPosInt = findViewPos(position);
        if (viewPosInt == 3) {
            layParams = new RelativeLayout.LayoutParams(ViewTotWidthInt / 2, RelativeLayout.LayoutParams.WRAP_CONTENT); // Width , height
        } else if (viewPosInt == 2) {
            layParams = new RelativeLayout.LayoutParams(ViewTotWidthInt / 3, RelativeLayout.LayoutParams.WRAP_CONTENT); // Width , height
        }

        layParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layParams.setMargins(10, 0, 5, 10);


        holder.mVideoNameTxt.setLayoutParams(layParams);
        holder.mVideoNameTxt.setText(discoverRes.getDicoveryName());


        if (discoverRes.getThumbnail_1().isEmpty()) {
            holder.mVideoImg.setImageResource(R.drawable.app_icon);
        } else {
            try {
                Glide.with(mContext)
                        .load(discoverRes.getThumbnail_1())
                        .into(holder.mVideoImg);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, e.toString());
                holder.mVideoImg.setImageResource(R.drawable.app_icon);
            }
        }

        holder.mVideoImgLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.DISCOVER_VIDEO_LINK = mDiscoverTempList.get(holder.getAdapterPosition()).getVideo();
                AppConstants.DISCOVER_VIDEO_NAME = mDiscoverTempList.get(holder.getAdapterPosition()).getDicoveryName();
                AppConstants.VIDEO_SCREEN = AppConstants.FAILURE_CODE;

                if (AppConstants.DISCOVER_VIDEO_LINK.contains(mContext.getString(R.string.youtube_link)) || AppConstants.DISCOVER_VIDEO_LINK.contains(mContext.getString(R.string.youtube
                )) || AppConstants.DISCOVER_VIDEO_LINK.contains(mContext.getString(R.string.ph_youtube_format))) {
                    AppConstants.YOUTUBE_VIDEO_ID = GlobalMethods.getYoutubeVideoID(AppConstants.DISCOVER_VIDEO_LINK);
                } else {
                    AppConstants.YOUTUBE_VIDEO_ID = AppConstants.FAILURE_CODE;
                }
                ((BaseActivity) mContext).nextScreen(VideoScreen.class, false);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mDiscoverTempList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_img_lay)
        RelativeLayout mVideoImgLay;

        @BindView(R.id.video_img)
        ImageView mVideoImg;

        @BindView(R.id.video_txt)
        TextView mVideoNameTxt;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private int findViewPos(int position) {
        int mod = position % 6;

        if (position == 0)
            return 3;
        else if (position < 5)
            return 1;
        else if (position == 5)
            return 2;
        else if (mod == 0)
            return 3;
        else if (mod < 5)
            return 1;
        else
            return 2;
    }
}

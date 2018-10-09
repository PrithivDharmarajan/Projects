package com.calix.calixgigaspireapp.adapter.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;


public class SpeedTestAdapter extends RecyclerView.Adapter<SpeedTestAdapter.Holder> {

    private Context mContext;
    private ArrayList<RouterMapEntity> mRouterMapResArrayList;

    public SpeedTestAdapter(ArrayList<RouterMapEntity> dataEntryRes, Context context) {
        mContext = context;
        mRouterMapResArrayList = dataEntryRes;
    }

    @NonNull
    @Override
    public SpeedTestAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_speed_test_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpeedTestAdapter.Holder holder, int position) {

        RouterMapEntity routerMapRes = mRouterMapResArrayList.get(position);

        boolean animationBool = routerMapRes.getSpeed().isAnimationBool();

        if(position==0){
            holder.mRouterTopConnectImg.setImageResource(R.drawable.ic_line_speed);
        }

        holder.mArrowGifImg.setVisibility(animationBool ? View.VISIBLE : View.GONE);
        holder.mArrowImgLay.setVisibility(animationBool ? View.GONE : View.VISIBLE);

        holder.mDeviceNameTxt.setText(routerMapRes.getName());

        holder.mDownloadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(AppConstants.IS_SPEED_STARTED ? routerMapRes.getSpeed().getDownload() : "0.0")+" "+mContext.getString(R.string.mbps));
        holder.mUploadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(AppConstants.IS_SPEED_STARTED ? routerMapRes.getSpeed().getUpload() : "0.0")+" "+mContext.getString(R.string.mbps));
         holder.mPingTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(AppConstants.IS_SPEED_STARTED ? routerMapRes.getSpeed().getPing() : "0")+" "+mContext.getString(R.string.ms));
    }

    @Override
    public int getItemCount() {
        return mRouterMapResArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.arrow_gif_img)
        GifImageView mArrowGifImg;

        @BindView(R.id.arrow_img_lay)
        RelativeLayout mArrowImgLay;

        @BindView(R.id.router_top_connect_img)
        ImageView mRouterTopConnectImg;


        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.download_speed_txt)
        TextView mDownloadSpeedTxt;

        @BindView(R.id.upload_speed_txt)
        TextView mUploadSpeedTxt;

        @BindView(R.id.ping_txt)
        TextView mPingTxt;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

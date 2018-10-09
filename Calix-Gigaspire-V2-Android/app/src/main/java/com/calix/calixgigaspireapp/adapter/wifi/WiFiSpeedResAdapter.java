package com.calix.calixgigaspireapp.adapter.wifi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WiFiSpeedResAdapter extends RecyclerView.Adapter<WiFiSpeedResAdapter.WiFiSpeedResHolder> {

    private Context mContext;
    private ArrayList<RouterMapEntity> mSpeedTestListResponse;

    public WiFiSpeedResAdapter(ArrayList<RouterMapEntity> speedTestResultRes, Context context) {
        mSpeedTestListResponse = speedTestResultRes;
        mContext = context;
    }

    @NonNull
    @Override
    public WiFiSpeedResAdapter.WiFiSpeedResHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WiFiSpeedResHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_wifi_speed_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WiFiSpeedResAdapter.WiFiSpeedResHolder holder, int position) {
        RouterMapEntity speedTestResultList = mSpeedTestListResponse.get(position);
        holder.mRouterTxt.setText(speedTestResultList.getName());
        holder.mDownloadMbpsTxt.setText(String.valueOf(speedTestResultList.getSpeed().getDownload()));
        holder.mUploadMbpsTxt.setText(String.valueOf(speedTestResultList.getSpeed().getUpload()));
        holder.mPingTxt.setText(String.valueOf(speedTestResultList.getSpeed().getPing()));
    }


    @Override
    public int getItemCount() {
        return mSpeedTestListResponse.size();
    }

    public class WiFiSpeedResHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.router_txt)
        TextView mRouterTxt;

        @BindView(R.id.download_txt)
        TextView mDownloadMbpsTxt;

        @BindView(R.id.upload_txt)
        TextView mUploadMbpsTxt;

        @BindView(R.id.ping_txt)
        TextView mPingTxt;

        private WiFiSpeedResHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

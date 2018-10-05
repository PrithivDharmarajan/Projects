package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.output.model.RouterMapEntity;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RouterMapAdapter extends RecyclerView.Adapter<RouterMapAdapter.Holder> {

    private Context mContext;
    private ArrayList<RouterMapEntity> mRouterMapResArrayList;

    public RouterMapAdapter(ArrayList<RouterMapEntity> dataEntryRes, Context context) {
        mContext = context;
        mRouterMapResArrayList = dataEntryRes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_router_map_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        RouterMapEntity routerMapRes = mRouterMapResArrayList.get(position);

        holder.mDeviceNameTxt.setText(routerMapRes.getName());

        holder.mDownloadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(routerMapRes.getSpeed().getDownload()));
        holder.mUploadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(routerMapRes.getSpeed().getUpload()));
        holder.mDownloadScaleTxt.setText(mContext.getString(R.string.mbps));
        holder.mUploadScaleTxt.setText(mContext.getString(R.string.mbps));

    }

    @Override
    public int getItemCount() {
        return mRouterMapResArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.download_speed_txt)
        TextView mDownloadSpeedTxt;

        @BindView(R.id.download_scale_txt)
        TextView mDownloadScaleTxt;

        @BindView(R.id.upload_speed_txt)
        TextView mUploadSpeedTxt;

        @BindView(R.id.upload_scale_txt)
        TextView mUploadScaleTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

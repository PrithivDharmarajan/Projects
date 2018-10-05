package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.utils.ImageUtil;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NetworkUsageAdapter extends RecyclerView.Adapter<NetworkUsageAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;
    private boolean mIsDownloadBool;

    public NetworkUsageAdapter(ArrayList<DeviceEntity> deviceListResponse, boolean isDownloadBool, Context context) {
        mDeviceListResponse = deviceListResponse;
        mIsDownloadBool = isDownloadBool;
        mContext = context;
    }


    @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_network_usage, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ControlHolder holder, int position) {

        DeviceEntity deviceListResponse = mDeviceListResponse.get(position);
        boolean isDeviceConnectedBool = deviceListResponse.isConnected2network();

        holder.mDeviceNameTxt.setText(deviceListResponse.getName());

        String deviceTypeStr = TextUtil.getInstance().deviceSubTypeNameStr(mContext, deviceListResponse.getType(), deviceListResponse.getSubType());
        holder.mDeviceSubTypeTxt.setText(deviceTypeStr);
        holder.mDeviceSubTypeTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);

        holder.mConnectedDeviceNameTxt.setText(deviceListResponse.getRouter().getName());
        holder.mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(Integer.valueOf(deviceListResponse.getType())));

        holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceListResponse.getIfType()));
        holder.mConnectionStatusTxt.setText(mContext.getString(isDeviceConnectedBool ? R.string.connected_to : R.string.disconnected_from));

        holder.mNetworkUsageTxt.setText(NumberUtil.getInstance().usageScaleValStr(mIsDownloadBool ? deviceListResponse.getNetworkUsage().getDownload() : deviceListResponse.getNetworkUsage().getUpload()) + " " + TextUtil.getInstance().deviceScaleNameStr(mContext,mIsDownloadBool ? deviceListResponse.getNetworkUsage().getDownload() : deviceListResponse.getNetworkUsage().getUpload()));
        holder.mDownloadUploadTxt.setText(mContext.getString(mIsDownloadBool ? R.string.download : R.string.upload));
        holder.mDownloadUploadImg.setImageResource(mIsDownloadBool ? R.drawable.download : R.drawable.upload);
        holder.mNetworkSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(mIsDownloadBool ? deviceListResponse.getSpeed().getDownload() : deviceListResponse.getSpeed().getUpload()));
        holder.mNetworkSpeedScaleTxt.setText(mContext.getString(R.string.mbps));

    }

    @Override
    public int getItemCount() {
        return mDeviceListResponse.size();
    }

    class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_img)
        ImageView mDeviceImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.device_sub_type_txt)
        TextView mDeviceSubTypeTxt;

        @BindView(R.id.connection_status_img)
        ImageView mConnectionStatusImg;

        @BindView(R.id.connection_status_txt)
        TextView mConnectionStatusTxt;

        @BindView(R.id.connected_device_name_txt)
        TextView mConnectedDeviceNameTxt;

        @BindView(R.id.connected_device_img)
        ImageView mConnectedDeviceImg;

        @BindView(R.id.network_usage_txt)
        TextView mNetworkUsageTxt;

        @BindView(R.id.download_upload_txt)
        TextView mDownloadUploadTxt;

        @BindView(R.id.download_upload_img)
        ImageView mDownloadUploadImg;

        @BindView(R.id.network_speed_txt)
        TextView mNetworkSpeedTxt;

        @BindView(R.id.network_speed_scale_txt)
        TextView mNetworkSpeedScaleTxt;


        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

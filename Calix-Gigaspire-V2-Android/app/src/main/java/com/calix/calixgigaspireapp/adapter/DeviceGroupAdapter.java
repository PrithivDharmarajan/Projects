package com.calix.calixgigaspireapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.utils.ImageUtil;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceGroupAdapter extends RecyclerView.Adapter<DeviceGroupAdapter.DeviceHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;
    private boolean mIsDownloadBool;

    public DeviceGroupAdapter(ArrayList<DeviceEntity> deviceListResponse, boolean isDownloadBool, Context context) {
        mDeviceListResponse = deviceListResponse;
        mIsDownloadBool = isDownloadBool;
        mContext = context;
    }

    @NonNull
    @Override
    public DeviceGroupAdapter.DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_device_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceGroupAdapter.DeviceHolder holder, int position) {
        DeviceEntity deviceListResponse = mDeviceListResponse.get(position);

        boolean isDeviceConnectedBool = deviceListResponse.isConnected2network();
        holder.mDeviceTypeTxt.setText(deviceListResponse.getName());
        holder.mDeviceSubTypeTxt.setText(deviceListResponse.getSubType() == 1 ? mContext.getString(R.string.android) : mContext.getString(R.string.ios));
        holder.mConnectedDeviceNameTxt.setText(deviceListResponse.getRouter().getName());
        holder.mDeviceChildImg.setImageResource(ImageUtil.getInstance().deviceImg(deviceListResponse.getType()));

        String deviceTypeStr = TextUtil.getInstance().deviceSubTypeNameStr(mContext, deviceListResponse.getType(), deviceListResponse.getSubType());
        holder.mDeviceSubTypeTxt.setText(deviceTypeStr);
        holder.mDeviceSubTypeTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);



        holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceListResponse.getIfType()));
//         holder.mConnectionStatusTxt.setText(String.format(mContext.getString(isDeviceConnectedBool ?  R.string.connected_to : R.string.disconnected_from), "\\s"));
        holder.mConnectionStatusTxt.setText("");


        holder.mNetworkUsageTxt.setText(NumberUtil.getInstance().usageScaleValStr(mIsDownloadBool ? deviceListResponse.getNetworkUsage().getDownload() : deviceListResponse.getNetworkUsage().getUpload()) + " " + TextUtil.getInstance().deviceScaleNameStr(mContext,mIsDownloadBool ? deviceListResponse.getNetworkUsage().getDownload() : deviceListResponse.getNetworkUsage().getUpload()));
        holder.mDownloadUploadTxt.setText(mContext.getString(mIsDownloadBool ? R.string.download : R.string.upload));
        holder.mDownloadUploadImg.setImageResource(mIsDownloadBool ? R.drawable.download_icon : R.drawable.upload_icon);
         holder.mNetworkSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(mIsDownloadBool ? deviceListResponse.getSpeed().getDownload() : deviceListResponse.getSpeed().getUpload())+" " + mContext.getString(R.string.dbm));
        holder.mScaleValueTxt.setText(mContext.getString(R.string.mbps));
    }

    @Override
    public int getItemCount() {
        return mDeviceListResponse.size();
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_child_img)
        ImageView mDeviceChildImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceTypeTxt;

        @BindView(R.id.device_sub_type_txt)
        TextView mDeviceSubTypeTxt;


        @BindView(R.id.connection_status_img)
        ImageView mConnectionStatusImg;

        @BindView(R.id.download_upload_img)
        ImageView mDownloadUploadImg;

        @BindView(R.id.connection_child_status_txt)
        TextView mConnectionStatusTxt;

        @BindView(R.id.connected_device_count_txt)
        TextView mConnectedDeviceNameTxt;

        @BindView(R.id.network_usage_percentage_txt)
        TextView mNetworkUsageTxt;

        @BindView(R.id.network_speed_txt)
        TextView mNetworkSpeedTxt;

        @BindView(R.id.download_upload_txt)
        TextView mDownloadUploadTxt;

        @BindView(R.id.scale_value_txt)
        TextView mScaleValueTxt;

        private DeviceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.ui.device.DeviceDetails;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.ImageUtil;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AbdulRahim(SmaatApps) on 1/10/2018.
 */

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public DeviceListAdapter(ArrayList<DeviceEntity> deviceListResponse, Context context) {
        mDeviceListResponse = deviceListResponse;
        mContext = context;
    }


    @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_device_list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ControlHolder holder, int position) {

        DeviceEntity deviceListResponse = mDeviceListResponse.get(position);
        boolean isDeviceConnectedBool = deviceListResponse.isConnected2network();

        holder.mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(deviceListResponse.getType()));
        holder.mDeviceNameTxt.setText(deviceListResponse.getName());

        String deviceTypeStr = TextUtil.getInstance().deviceSubTypeNameStr(mContext, deviceListResponse.getType(), deviceListResponse.getSubType());
        holder.mDeviceSubTypeTxt.setText(deviceTypeStr);
        holder.mDeviceSubTypeTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);

        holder.mConnectedDeviceNameTxt.setText(deviceListResponse.getRouter().getName());

        holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceListResponse.getIfType()));
        holder.mConnectionStatusTxt.setText(mContext.getString(isDeviceConnectedBool ? R.string.connected_to : R.string.disconnected_from));

        holder.mSignalStrengthView.setVisibility(deviceListResponse.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);
        holder.mSignalStrengthLay.setVisibility(deviceListResponse.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);

        holder.mSignalStrengthTxt.setText(deviceListResponse.getSignalStrength() + " " + mContext.getString(R.string.dbm));


        holder.mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(!isDeviceConnectedBool, deviceListResponse.getIfType()));
        holder.mConnectDisconnectTxt.setText(String.format(mContext.getString(isDeviceConnectedBool ? R.string.disconnect_device : R.string.connect_device), deviceListResponse.getRouter().getName()));
        holder.mConnectDisconnectSwitchCompat.setChecked(isDeviceConnectedBool);

        holder.mUploadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(mContext, deviceListResponse.getNetworkUsage().getUpload()));
        holder.mDownloadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(mContext, deviceListResponse.getNetworkUsage().getDownload()));
        holder.mUploadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceListResponse.getNetworkUsage().getUpload()));
        holder.mDownloadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceListResponse.getNetworkUsage().getDownload()));


        holder.mVisibleInvisibleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDetailsShowingBool = holder.mDeviceDetailsLay.getVisibility() == View.VISIBLE;
                holder.mDeviceDetailsLay.setVisibility(isDetailsShowingBool ? View.GONE : View.VISIBLE);
                holder.mVisibleInvisibleImg.setImageResource(isDetailsShowingBool ? R.drawable.eye_visible : R.drawable.eye_invisible);

            }
        });

        holder.mConnectDisconnectSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isChecked, mDeviceListResponse.get(holder.getAdapterPosition()).getIfType()));

                holder.mConnectionStatusTxt.setText(mContext.getString(isChecked ? R.string.connected_to : R.string.disconnected_from));

                holder.mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(!isChecked, mDeviceListResponse.get(holder.getAdapterPosition()).getIfType()));
                holder.mConnectDisconnectTxt.setText(String.format(mContext.getString(isChecked ? R.string.disconnect_device : R.string.connect_device), mDeviceListResponse.get(holder.getAdapterPosition()).getRouter().getName()));

                if (isChecked)
                    APIRequestHandler.getInstance().deviceConnectAPICall(mDeviceListResponse.get(holder.getAdapterPosition()).getDeviceId(), ((BaseActivity) mContext));
                else
                    APIRequestHandler.getInstance().deviceDisconnectAPICall(mDeviceListResponse.get(holder.getAdapterPosition()).getDeviceId(), ((BaseActivity) mContext));


                mDeviceListResponse.get(holder.getAdapterPosition()).setConnected2network(isChecked);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.DEVICE_DETAILS_ENTITY = mDeviceListResponse.get(holder.getAdapterPosition());
                ((BaseActivity) mContext).nextScreen(DeviceDetails.class);

            }
        });
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

        @BindView(R.id.signal_strength_view)
        View mSignalStrengthView;

        @BindView(R.id.signal_strength_lay)
        RelativeLayout mSignalStrengthLay;

        @BindView(R.id.signal_strength_txt)
        TextView mSignalStrengthTxt;

        @BindView(R.id.connect_disconnect_img)
        ImageView mConnectDisconnectImg;

        @BindView(R.id.connect_disconnect_txt)
        TextView mConnectDisconnectTxt;

        @BindView(R.id.connect_disconnect_switch_compat)
        SwitchCompat mConnectDisconnectSwitchCompat;

        @BindView(R.id.download_speed_txt)
        TextView mDownloadSpeedTxt;

        @BindView(R.id.download_scale_txt)
        TextView mDownloadScaleTxt;

        @BindView(R.id.upload_speed_txt)
        TextView mUploadSpeedTxt;

        @BindView(R.id.upload_scale_txt)
        TextView mUploadScaleTxt;

        @BindView(R.id.device_details_lay)
        LinearLayout mDeviceDetailsLay;

        @BindView(R.id.visible_invisible_img)
        ImageView mVisibleInvisibleImg;


        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}



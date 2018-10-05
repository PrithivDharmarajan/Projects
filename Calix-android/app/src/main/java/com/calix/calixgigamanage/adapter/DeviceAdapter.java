package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.ui.device.DeviceDetails;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.ImageUtil;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public DeviceAdapter(ArrayList<DeviceEntity> deviceListResponse, Context context) {
        mDeviceListResponse = deviceListResponse;
        mContext = context;
    }


    @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_device, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ControlHolder holder, int position) {

        DeviceEntity deviceListResponse = mDeviceListResponse.get(position);
        boolean isDeviceConnectedBool = deviceListResponse.isConnected2network();

        holder.mDeviceNameTxt.setText(deviceListResponse.getName());

        String deviceTypeStr = TextUtil.getInstance().deviceSubTypeNameStr(mContext, deviceListResponse.getType(), deviceListResponse.getSubType());
        holder.mDeviceSubTypeTxt.setText(deviceTypeStr);
        holder.mDeviceSubTypeTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);

        holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceListResponse.getIfType()));
        holder.mConnectionStatusTxt.setText(mContext.getString(isDeviceConnectedBool ? R.string.connected_to : R.string.disconnected_from));


        holder.mConnectedDeviceNameTxt.setText(deviceListResponse.getRouter().getName());

        holder.mUploadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(mContext, deviceListResponse.getNetworkUsage().getUpload()));
        holder.mDownloadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(mContext, deviceListResponse.getNetworkUsage().getDownload()));
        holder.mUploadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceListResponse.getNetworkUsage().getUpload()));
        holder.mDownloadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceListResponse.getNetworkUsage().getDownload()));

        holder.mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(deviceListResponse.getType()));

        holder.mVisibleInvisibleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDetailsShowingBool = holder.mDeviceDetailsLay.getVisibility() == View.VISIBLE;
                holder.mDeviceDetailsLay.setVisibility(isDetailsShowingBool ? View.GONE : View.VISIBLE);
                holder.mVisibleInvisibleImg.setImageResource(isDetailsShowingBool ? R.drawable.eye_visible : R.drawable.eye_invisible);
            }
        });

        holder.mDeviceNameEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogManager.getInstance().showEdtDeviceNamePopup(mContext, holder.mDeviceNameTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick(String edtNameStr) {
                        APIRequestHandler.getInstance().deviceRenameAPICalll(mDeviceListResponse.get(holder.getAdapterPosition()).getDeviceId(), edtNameStr, "12345", ((BaseActivity) mContext));
                        holder.mDeviceNameTxt.setText(edtNameStr);
                        mDeviceListResponse.get(holder.getAdapterPosition()).setName(edtNameStr);

                    }
                });
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

        @BindView(R.id.device_edit_img)
        ImageView mDeviceNameEditImg;

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



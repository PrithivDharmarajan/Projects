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
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.ImageUtil;
import com.calix.calixgigamanage.utils.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ParentalControlAdapter extends RecyclerView.Adapter<ParentalControlAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;
    private boolean mPauseAllBool = false;

    public ParentalControlAdapter(ArrayList<DeviceEntity> deviceListResponse, boolean pauseAllBool, Context context) {
        mDeviceListResponse = deviceListResponse;
        mPauseAllBool = pauseAllBool;
        mContext = context;
    }


    @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_control, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final ControlHolder holder, int position) {

        DeviceEntity deviceListResponse = mDeviceListResponse.get(position);
        boolean isDeviceConnectedBool = deviceListResponse.isConnected2network();

        holder.mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(Integer.valueOf(deviceListResponse.getType())));

        holder.mDeviceNameTxt.setText(deviceListResponse.getName());

        String deviceTypeStr = TextUtil.getInstance().deviceSubTypeNameStr(mContext, deviceListResponse.getType(), deviceListResponse.getSubType());
        holder.mDeviceSubTypeTxt.setText(deviceTypeStr);
        holder.mDeviceSubTypeTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);

        holder.mConnectedDeviceNameTxt.setText(deviceListResponse.getRouter().getName());

        holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceListResponse.getIfType()));
        holder.mConnectionStatusTxt.setText(mContext.getString(isDeviceConnectedBool ? R.string.connected_to : R.string.disconnected_from));

        holder.mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(!isDeviceConnectedBool, deviceListResponse.getIfType()));
        holder.mConnectDisconnectTxt.setText(String.format(mContext.getString(isDeviceConnectedBool ? R.string.disconnect_device : R.string.connect_device), deviceListResponse.getRouter().getName()));

        holder.mConnectDisconnectStatusImg.setImageResource(isDeviceConnectedBool ? R.drawable.play_img : R.drawable.pause_img);

        /*onClick*/
        holder.mConnectDisconnectStatusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean isCheckedBool = !mDeviceListResponse.get(holder.getAdapterPosition()).isConnected2network();

                mPauseAllBool = false;
                if (isCheckedBool) {
                    holder.mConnectDisconnectStatusImg.setImageResource(R.drawable.play_img);

                    holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(true, mDeviceListResponse.get(holder.getAdapterPosition()).getIfType()));
                    holder.mConnectionStatusTxt.setText(mContext.getString(R.string.connected_to));

                    holder.mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(false, mDeviceListResponse.get(holder.getAdapterPosition()).getIfType()));
                    holder.mConnectDisconnectTxt.setText(String.format(mContext.getString(R.string.disconnect_device), mDeviceListResponse.get(holder.getAdapterPosition()).getRouter().getName()));

                    APIRequestHandler.getInstance().deviceConnectAPICall(mDeviceListResponse.get(holder.getAdapterPosition()).getDeviceId(), ((BaseActivity) mContext));
                } else {
                    holder.mConnectDisconnectStatusImg.setImageResource(R.drawable.pause_img);

                    holder.mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(false, mDeviceListResponse.get(holder.getAdapterPosition()).getIfType()));
                    holder.mConnectionStatusTxt.setText(mContext.getString(R.string.disconnected_from));

                    holder.mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(true, mDeviceListResponse.get(holder.getAdapterPosition()).getIfType()));
                    holder.mConnectDisconnectTxt.setText(String.format(mContext.getString(R.string.connect_device), mDeviceListResponse.get(holder.getAdapterPosition()).getRouter().getName()));

                    APIRequestHandler.getInstance().deviceDisconnectAPICall(mDeviceListResponse.get(holder.getAdapterPosition()).getDeviceId(), ((BaseActivity) mContext));


                }
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

        @BindView(R.id.connect_disconnect_img)
        ImageView mConnectDisconnectImg;

        @BindView(R.id.connect_disconnect_txt)
        TextView mConnectDisconnectTxt;

        @BindView(R.id.connect_disconnect_status_img)
        ImageView mConnectDisconnectStatusImg;


        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

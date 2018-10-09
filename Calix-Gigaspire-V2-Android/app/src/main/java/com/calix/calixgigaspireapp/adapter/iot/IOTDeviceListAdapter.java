package com.calix.calixgigaspireapp.adapter.iot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.IOTRemoveDeviceEntity;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.WebSocket;


public class IOTDeviceListAdapter extends RecyclerView.Adapter<IOTDeviceListAdapter.Holder> {
    private Context mContext;
    private WebSocket mWebSocket;
    private boolean mIsAllDeviceBool;
    private ArrayList<IOTRemoveDeviceEntity> mIOTDeviceListResponse;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public IOTDeviceListAdapter(ArrayList<DeviceEntity> iotDeviceListResponse, Context context) {
        mDeviceListResponse = iotDeviceListResponse;
        mContext = context;
    }

    public IOTDeviceListAdapter(ArrayList<IOTRemoveDeviceEntity> iotDeviceListResponse, boolean isAllDeviceBool, WebSocket webSocket, Context context) {
        mIOTDeviceListResponse = iotDeviceListResponse;
        mIsAllDeviceBool = isAllDeviceBool;
        mWebSocket = webSocket;
        mContext = context;
    }


    @NonNull
    @Override
    public IOTDeviceListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_iot_device_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final IOTDeviceListAdapter.Holder holder, int position) {

        if (mIOTDeviceListResponse != null) {
            final IOTRemoveDeviceEntity deviceListResponse = mIOTDeviceListResponse.get(position);
            final boolean deviceStateBool = Boolean.valueOf(deviceListResponse.getDeviceONOFFState());

            holder.mDeviceNameTxt.setText(deviceListResponse.getName());
            holder.mDeviceStatusTxt.setText(deviceListResponse.getFriendlyDeviceType());

            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    holder.mDeviceImg.setImageResource(deviceStateBool ? R.drawable.default_iot_device_blue : R.drawable.default_iot_device_gray);
                    holder.mDeviceStatusImg.setImageResource(deviceStateBool ? R.drawable.hexagonal_white_with_blue : R.drawable.hexagonal_gray);
                    holder.mRemoveImg.setVisibility(mIsAllDeviceBool || deviceListResponse.getFriendlyDeviceType().equalsIgnoreCase("AlmondSiren") || deviceListResponse.getFriendlyDeviceType().equalsIgnoreCase("AlmondBlink") ? View.GONE : View.VISIBLE);
                    holder.mDeviceStatusCardView.setVisibility(deviceListResponse.getFriendlyDeviceType().equalsIgnoreCase("ContactSwitch") || !mIsAllDeviceBool ? View.GONE : View.VISIBLE);

                    holder.mOnTxt.setBackgroundColor(ContextCompat.getColor(mContext, deviceStateBool ? R.color.blue : R.color.grey));
                    holder.mOffTxt.setBackgroundColor(ContextCompat.getColor(mContext, deviceStateBool ? R.color.grey : R.color.gray));
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIOTDeviceListResponse != null) {
                        ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mIsAllDeviceBool) {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("CommandType", "UpdateDeviceIndex");
                                        jsonObject.put("MobileInternalIndex", "898");
                                        jsonObject.put("ID", mIOTDeviceListResponse.get(holder.getAdapterPosition()).getID());
                                        jsonObject.put("Index", changeDeviceStatus(mIOTDeviceListResponse.get(holder.getAdapterPosition()).getFriendlyDeviceType()));
                                        jsonObject.put("Value", !(Boolean.valueOf(mIOTDeviceListResponse.get(holder.getAdapterPosition()).getDeviceONOFFState())));
                                        mWebSocket.send(String.valueOf(jsonObject));

                                    } else if (!mIOTDeviceListResponse.get(holder.getAdapterPosition()).getFriendlyDeviceType().equalsIgnoreCase("AlmondSiren") &&
                                            !mIOTDeviceListResponse.get(holder.getAdapterPosition()).getFriendlyDeviceType().equalsIgnoreCase("AlmondBlink")) {

                                        DialogManager.getInstance().showOptionPopup(mContext, String.format(mContext.getString(R.string.remove_iot_msg), mIOTDeviceListResponse.get(holder.getAdapterPosition()).getName()), mContext.getString(R.string.yes), mContext.getString(R.string.no), new InterfaceTwoBtnCallback() {
                                            @Override
                                            public void onPositiveClick() {
                                                try {
                                                    JSONObject jsonObject = new JSONObject();
                                                    jsonObject.put("CommandType", "DeleteDevice");
                                                    jsonObject.put("MobileInternalIndex", "898");
                                                    jsonObject.put("DeviceId", mIOTDeviceListResponse.get(holder.getAdapterPosition()).getID());
                                                    mWebSocket.send(String.valueOf(jsonObject));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onNegativeClick() {

                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });

        } else {
            final DeviceEntity deviceListResponse = mDeviceListResponse.get(position);

            holder.mDeviceImg.setImageResource(R.drawable.default_iot_device_blue);
            holder.mDeviceNameTxt.setText(deviceListResponse.getName());
            holder.mDeviceStatusTxt.setText(deviceListResponse.getLocation());
            holder.mRemoveImg.setVisibility(View.GONE);
            holder.mDeviceStatusCardView.setVisibility(View.GONE);
        }


    }

    private String changeDeviceStatus(String deviceTypeInt) {
        switch (deviceTypeInt) {
            case "AlmondSiren":
                return "2";
            default:
                return "1";
        }
    }

    @Override
    public int getItemCount() {
        return mIOTDeviceListResponse != null ? mIOTDeviceListResponse.size() : mDeviceListResponse.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_status_img)
        ImageView mDeviceStatusImg;

        @BindView(R.id.device_img)
        ImageView mDeviceImg;

        @BindView(R.id.remove_img)
        ImageView mRemoveImg;

        @BindView(R.id.device_status_card_view)
        CardView mDeviceStatusCardView;

        @BindView(R.id.on_txt)
        TextView mOnTxt;

        @BindView(R.id.off_txt)
        TextView mOffTxt;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.device_status_txt)
        TextView mDeviceStatusTxt;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

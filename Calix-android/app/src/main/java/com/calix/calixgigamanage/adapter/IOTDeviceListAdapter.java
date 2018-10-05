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
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.IOTRemoveDeviceEntity;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.WebSocket;


public class IOTDeviceListAdapter extends RecyclerView.Adapter<IOTDeviceListAdapter.Holder> {
    private Context mContext;
    private WebSocket mWebSocket;
    private ArrayList<IOTRemoveDeviceEntity> mIOTDeviceListResponse;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public IOTDeviceListAdapter(ArrayList<DeviceEntity> iotDeviceListResponse, Context context) {
        mDeviceListResponse = iotDeviceListResponse;
        mContext = context;
    }

    public IOTDeviceListAdapter(ArrayList<IOTRemoveDeviceEntity> iotDeviceListResponse, WebSocket webSocket, Context context) {
        mIOTDeviceListResponse = iotDeviceListResponse;
        mWebSocket = webSocket;
        mContext = context;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_iot_device_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        holder.mDeviceImg.setImageResource(R.mipmap.ic_launcher);
        if (mIOTDeviceListResponse != null) {
            final IOTRemoveDeviceEntity deviceListResponse = mIOTDeviceListResponse.get(position);

            holder.mDeviceNameTxt.setText(deviceListResponse.getName());
            holder.mDeviceStatusTxt.setText(deviceListResponse.getFriendlyDeviceType());

            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    holder.mConnectDisconnectSwitchCompat.setVisibility(deviceListResponse.getFriendlyDeviceType().equalsIgnoreCase("ContactSwitch") ? View.GONE : View.VISIBLE);
                    holder.mConnectDisconnectSwitchCompat.setChecked(Boolean.valueOf(deviceListResponse.getDeviceONOFFState()));
                }
            });
            holder.mConnectDisconnectSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                    ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("CommandType", "UpdateDeviceIndex");
                                jsonObject.put("MobileInternalIndex", "898");
                                jsonObject.put("ID", mIOTDeviceListResponse.get(holder.getAdapterPosition()).getID());
                                jsonObject.put("Index", changeDeviceStatus(mIOTDeviceListResponse.get(holder.getAdapterPosition()).getFriendlyDeviceType()));
                                jsonObject.put("Value", isChecked);
                                mWebSocket.send(String.valueOf(jsonObject));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        } else {
            final DeviceEntity deviceListResponse = mDeviceListResponse.get(position);
            holder.mDeviceNameTxt.setText(deviceListResponse.getName());
            holder.mDeviceStatusTxt.setText(deviceListResponse.getLocation());
            holder.mConnectDisconnectSwitchCompat.setVisibility(View.GONE);
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

        @BindView(R.id.device_img)
        ImageView mDeviceImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.device_status_txt)
        TextView mDeviceStatusTxt;

        @BindView(R.id.connect_disconnect_switch_compat)
        SwitchCompat mConnectDisconnectSwitchCompat;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

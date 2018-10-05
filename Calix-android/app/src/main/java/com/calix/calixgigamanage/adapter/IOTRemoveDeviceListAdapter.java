package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.output.model.IOTRemoveDeviceEntity;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceTwoBtnCallback;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.WebSocket;


public class IOTRemoveDeviceListAdapter extends RecyclerView.Adapter<IOTRemoveDeviceListAdapter.Holder> {
    private WebSocket mWebSocket;
    private Context mContext;
    private ArrayList<IOTRemoveDeviceEntity> mIOTDeviceListResponse;

    public IOTRemoveDeviceListAdapter(ArrayList<IOTRemoveDeviceEntity> iotDeviceListResponse, Context context, WebSocket webSocket) {
        mIOTDeviceListResponse = iotDeviceListResponse;
        mWebSocket = webSocket;
        mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_iot_remove_device_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        IOTRemoveDeviceEntity deviceListResponse = mIOTDeviceListResponse.get(position);

        holder.mDeviceImg.setImageResource(R.mipmap.ic_launcher);
        holder.mDeviceNameTxt.setText(deviceListResponse.getName());
        holder.mDeviceStatusTxt.setText(deviceListResponse.getFriendlyDeviceType());

        holder.mRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });
    }


    @Override
    public int getItemCount() {
        return mIOTDeviceListResponse.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_img)
        ImageView mDeviceImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.device_status_txt)
        TextView mDeviceStatusTxt;

        @BindView(R.id.remove_btn)
        Button mRemoveBtn;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.calix.calixgigaspireapp.adapter.iot;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IOTTemperatureAdapter extends RecyclerView.Adapter<IOTTemperatureAdapter.Holder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public IOTTemperatureAdapter(ArrayList<DeviceEntity> deviceListResponse, Context context) {
        mDeviceListResponse = deviceListResponse;
        mContext = context;
    }

    @NonNull
    @Override
    public IOTTemperatureAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IOTTemperatureAdapter.Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_temp_iot_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IOTTemperatureAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

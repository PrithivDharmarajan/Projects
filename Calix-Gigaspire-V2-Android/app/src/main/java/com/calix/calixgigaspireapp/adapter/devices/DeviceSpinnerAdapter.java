package com.calix.calixgigaspireapp.adapter.devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceFilterEntity;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DeviceSpinnerAdapter extends RecyclerView.Adapter<DeviceSpinnerAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceFilterEntity> mDeviceFiltersArrList;
    private CardView mFilterCardView;
    private InterfaceEdtBtnCallback mInterfaceEdtBtnCallback;

    public DeviceSpinnerAdapter(ArrayList<DeviceFilterEntity> filters, InterfaceEdtBtnCallback interfaceEdtBtnCallback, CardView filterCardView,  Context context) {
        mContext = context;
        mFilterCardView = filterCardView;
        mDeviceFiltersArrList = filters;
        mInterfaceEdtBtnCallback = interfaceEdtBtnCallback;
    }


    @NonNull
    @Override
    public DeviceSpinnerAdapter.ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceSpinnerAdapter.ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_devices_filter_spinner_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceSpinnerAdapter.ControlHolder holder, final int position) {
        holder.mFilterNameTxt.setText(mDeviceFiltersArrList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDeviceFiltersArrList.get(holder.getAdapterPosition()).getName().equals(mContext.getString(R.string.all))) {
                    APIRequestHandler.getInstance().deviceListAPICall("", ((BaseActivity) mContext));
                } else {
                    APIRequestHandler.getInstance().deviceListByFilterAPICall(mDeviceFiltersArrList.get(holder.getAdapterPosition()).getId(), ((BaseActivity) mContext));
                }

                mInterfaceEdtBtnCallback.onPositiveClick(mDeviceFiltersArrList.get(holder.getAdapterPosition()).getName());
                mFilterCardView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDeviceFiltersArrList.size();
    }

    class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.filter_name_txt)
        TextView mFilterNameTxt;

        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


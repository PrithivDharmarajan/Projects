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
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.ui.parentalcontrol.ParentControlInsights;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentalControlAdapter extends RecyclerView.Adapter<ParentalControlAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public ParentalControlAdapter(ArrayList<DeviceEntity> deviceListResponse, Context context) {
        mDeviceListResponse = deviceListResponse;
        mContext = context;
    }

   /* @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_control, parent, false));
    }*/

    @NonNull
    @Override
    public ParentalControlAdapter.ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_control, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParentalControlAdapter.ControlHolder holder, int position) {
        //DeviceEntity deviceListResponse = mDeviceListResponse.get(position);

        /*   Item onclick*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).nextScreen(ParentControlInsights.class);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_img)
        ImageView mUserImg;

        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        @BindView(R.id.device_count_txt)
        TextView mDeviceCountTxt;

        @BindView(R.id.device_hours_txt)
        TextView mDeviceHoursTxt;

        @BindView(R.id.setting_img)
        ImageView mSettingImg;

//        @BindView(R.id.pause_img)
//        TextView mPauseImg;


        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

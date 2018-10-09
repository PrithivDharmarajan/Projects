package com.calix.calixgigaspireapp.adapter.parentcontrol;

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

public class PCDashBoardAdapter extends RecyclerView.Adapter<PCDashBoardAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceEntity> mDeviceListResponse;

    public PCDashBoardAdapter(ArrayList<DeviceEntity> deviceListResponse, Context context) {
        mDeviceListResponse = deviceListResponse;
        mContext = context;
    }

    @NonNull
    @Override
    public PCDashBoardAdapter.ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_control, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PCDashBoardAdapter.ControlHolder holder, int position) {
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
        return 1;
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

        @BindView(R.id.pc_setting_img)
        ImageView mSettingImg;

        @BindView(R.id.pc_pause_img)
        ImageView mPauseImg;


        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

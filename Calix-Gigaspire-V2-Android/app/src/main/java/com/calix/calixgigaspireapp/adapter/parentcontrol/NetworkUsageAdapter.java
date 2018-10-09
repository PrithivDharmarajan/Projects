package com.calix.calixgigaspireapp.adapter.parentcontrol;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.devices.DeviceAdapter;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.DeviceListResponse;
import com.calix.calixgigaspireapp.ui.devices.DevicesList;
import com.calix.calixgigaspireapp.utils.ImageUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NetworkUsageAdapter extends RecyclerView.Adapter<NetworkUsageAdapter.DownloadHolder> {

    private Context mContext;
    private ArrayList<DeviceListResponse> mDeviceListResponse;
    private boolean mIsDownloadBool;

    public NetworkUsageAdapter(ArrayList<DeviceListResponse> deviceListResponse, boolean isDownloadBool, Context context) {
        mDeviceListResponse = deviceListResponse;
        mIsDownloadBool = isDownloadBool;
        mContext = context;
    }


    @NonNull
    @Override
    public NetworkUsageAdapter.DownloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_device_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final NetworkUsageAdapter.DownloadHolder holder, int position) {

        DeviceListResponse deviceListArr = mDeviceListResponse.get(position);




        holder.mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(deviceListArr.getType()));

        holder.mDeviceNameTxt.setText(deviceList(Integer.parseInt(String.valueOf(deviceListArr.getType()))));

        holder.mDevicesListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mDevicesListRecyclerView.setNestedScrollingEnabled(false);
        holder.mDevicesListRecyclerView.setAdapter(new DeviceGroupAdapter(deviceListArr.getDevices(),mIsDownloadBool, mContext));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDetailsShowingBool = holder.mDevicesListRecyclerView.getVisibility() == View.VISIBLE;
                holder.mDevicesListRecyclerView.setVisibility(isDetailsShowingBool ? View.GONE : View.VISIBLE);
                holder.mSeparatorView.setVisibility(isDetailsShowingBool ? View.GONE : View.VISIBLE);
                holder.mVisibleInvisibleImg.setImageResource(isDetailsShowingBool ? R.drawable.down_arrow : R.drawable.ic_up_arrow);
            }
        });
    }


    /*find the IOT device List*/
    private int deviceList(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                return R.string.phone;
            case 2:
                return R.string.computer;
            case 3:
                return R.string.console;
            case 4:
                return R.string.storage;
            case 5:
                return R.string.printer;
            case 6:
                return R.string.television;
            case 7:
                return R.string.iot_device;
            case 8:
                return R.string.camera;
            default:
                return R.string.unknown;
        }
    }
    @Override
    public int getItemCount() {
        return mDeviceListResponse.size();
    }


    class DownloadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_type_img)
        ImageView mDeviceImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.visible_invisible_img)
        ImageView mVisibleInvisibleImg;

        @BindView(R.id.devices_recycler_view)
        RecyclerView mDevicesListRecyclerView;

        @BindView(R.id.separator_view)
        View mSeparatorView;

        private DownloadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

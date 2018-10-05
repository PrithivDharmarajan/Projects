package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigamanage.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DownloadUploadAdapter extends RecyclerView.Adapter<DownloadUploadAdapter.ControlHolder> {

    private Context mContext;

    public DownloadUploadAdapter(Context context) {
        mContext = context;
    }


    @Override
    public ControlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_download_upload, parent, false));
    }

    @Override
    public void onBindViewHolder(ControlHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.adapter_control_device_img)
        ImageView mDeviceImg;

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.number_txt)
        TextView mNumberTxt;

        @BindView(R.id.connect_via_img)
        ImageView mConnectViaImg;

        @BindView(R.id.network_usage_txt)
        TextView mNetWorkUsageTxt;

        @BindView(R.id.download_bytes_txt)
        TextView mDownloadBytesTxt;





        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

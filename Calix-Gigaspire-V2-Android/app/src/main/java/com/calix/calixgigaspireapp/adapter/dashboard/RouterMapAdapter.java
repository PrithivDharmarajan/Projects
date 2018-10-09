package com.calix.calixgigaspireapp.adapter.dashboard;

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
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RouterMapAdapter extends RecyclerView.Adapter<RouterMapAdapter.Holder> {

    private Context mContext;
    private ArrayList<RouterMapEntity> mRouterMapResArrayList;


    public RouterMapAdapter(ArrayList<RouterMapEntity> dataEntryRes, Context context) {
        mContext = context;
        mRouterMapResArrayList = dataEntryRes;
    }

    @NonNull
    @Override
    public RouterMapAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_router_map_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RouterMapAdapter.Holder holder, int position) {

        RouterMapEntity routerMapRes = mRouterMapResArrayList.get(position);

        if (position == mRouterMapResArrayList.size() - 1) {
            holder.mRouterBottomConnectImg.setVisibility(View.GONE);
        }

        holder.mDeviceNameTxt.setText(routerMapRes.getName());

        holder.mDownloadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(routerMapRes.getSpeed().getDownload()));
        holder.mUploadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(routerMapRes.getSpeed().getUpload()));

        holder.mEditDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().showEdtDeviceNamePopup(mContext,
                        mContext.getString(R.string.router_edit_pheader),
                        mContext.getString(R.string.router_edit_sheader),
                        mContext.getString(R.string.router_edit_hint),
                        holder.mDeviceNameTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
                            @Override
                            public void onNegativeClick() {

                            }

                            @Override
                            public void onPositiveClick(String edtStr) {
                                mRouterMapResArrayList.get(holder.getAdapterPosition()).setName(edtStr);
                                holder.mDeviceNameTxt.setText(edtStr);
                                AppConstants.ROUTER_DETAILS_ENTITY= mRouterMapResArrayList.get(holder.getAdapterPosition());
                                APIRequestHandler.getInstance().routerNameUpdateAPICall(mRouterMapResArrayList.get(holder.getAdapterPosition()).getRouterId(), mRouterMapResArrayList.get(holder.getAdapterPosition()).getName(), (BaseActivity) mContext);
                            }
                        });
            }
        });

    }


    @Override
    public int getItemCount() {
        return mRouterMapResArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_name_txt)
        TextView mDeviceNameTxt;

        @BindView(R.id.download_speed_txt)
        TextView mDownloadSpeedTxt;

        @BindView(R.id.upload_speed_txt)
        TextView mUploadSpeedTxt;

        @BindView(R.id.edit_device_name)
        ImageView mEditDeviceName;

        @BindView(R.id.router_top_connect_img)
        ImageView mRouterTopConnectImg;

        @BindView(R.id.router_bottom_connect_img)
        ImageView mRouterBottomConnectImg;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}

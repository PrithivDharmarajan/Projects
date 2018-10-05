package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.DeviceFilterEntity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DeviceSpinnerAdapter extends RecyclerView.Adapter<DeviceSpinnerAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<DeviceFilterEntity> mDeviceFiltersArrList;
    private CardView mFilterCardView;
    private ImageView mArrowImg;
    private InterfaceEdtBtnCallback mInterfaceEdtBtnCallback;

    public DeviceSpinnerAdapter(ArrayList<DeviceFilterEntity> filters, InterfaceEdtBtnCallback interfaceEdtBtnCallback, CardView filterCardView, ImageView arrowImg, Context context) {
        mContext = context;
        mFilterCardView = filterCardView;
        mArrowImg = arrowImg;
        mDeviceFiltersArrList = filters;
        mInterfaceEdtBtnCallback = interfaceEdtBtnCallback;
    }


    @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_devices_filter_spinner_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ControlHolder holder, final int position) {
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
                mArrowImg.setRotation(0);

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


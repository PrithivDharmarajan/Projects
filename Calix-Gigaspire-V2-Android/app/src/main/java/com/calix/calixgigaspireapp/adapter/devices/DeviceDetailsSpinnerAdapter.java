package com.calix.calixgigaspireapp.adapter.devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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


public class DeviceDetailsSpinnerAdapter extends RecyclerView.Adapter<DeviceDetailsSpinnerAdapter.ControlHolder> {

    private Context mContext;
    private String mFilterSelectedStr;
    private ArrayList<String> mDeviceFiltersArrList;
    private CardView mFilterCardView;
    private ImageView mArrowImg;
    private InterfaceEdtBtnCallback mInterfaceEdtBtnCallback;

    public DeviceDetailsSpinnerAdapter(String filterSelectedStr,ArrayList<String> filters, InterfaceEdtBtnCallback interfaceEdtBtnCallback, CardView filterCardView, Context context) {
        mContext = context;
        mFilterCardView = filterCardView;
        mDeviceFiltersArrList = filters;
        mInterfaceEdtBtnCallback = interfaceEdtBtnCallback;
        mFilterSelectedStr=filterSelectedStr;
    }


    @NonNull
    @Override
    public DeviceDetailsSpinnerAdapter.ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeviceDetailsSpinnerAdapter.ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_graph_filter_spinner_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceDetailsSpinnerAdapter.ControlHolder holder, final int position) {
        switch (mDeviceFiltersArrList.get(position)) {
            case "min":

                holder.itemView.setBackgroundColor(mFilterSelectedStr.equalsIgnoreCase("min") ? ContextCompat.getColor(mContext, R.color.read_gray):ContextCompat.getColor(mContext, R.color.white) );

                holder.mFilterNameTxt.setText(mContext.getString(R.string.f_min));
                holder.mImgFilter.setBackground(mContext.getResources().getDrawable(R.drawable.ic_guest_time));
                break;
            case "hour":
                holder.itemView.setBackgroundColor(mFilterSelectedStr.equalsIgnoreCase("hour") ? ContextCompat.getColor(mContext, R.color.read_gray):ContextCompat.getColor(mContext, R.color.white) );

                holder.mFilterNameTxt.setText(mContext.getString(R.string.f_hour));
                holder.mImgFilter.setBackground(mContext.getResources().getDrawable(R.drawable.ic_time_duration));
                break;
            case "day":
                holder.itemView.setBackgroundColor(mFilterSelectedStr.equalsIgnoreCase("day") ? ContextCompat.getColor(mContext, R.color.read_gray):ContextCompat.getColor(mContext, R.color.white) );

                holder.mFilterNameTxt.setText(mContext.getString(R.string.f_day));
                holder.mImgFilter.setBackground(mContext.getResources().getDrawable(R.drawable.ic_calendar));
                break;
            case "week":
                holder.itemView.setBackgroundColor(mFilterSelectedStr.equalsIgnoreCase("week") ? ContextCompat.getColor(mContext, R.color.read_gray):ContextCompat.getColor(mContext, R.color.white) );

                holder.mFilterNameTxt.setText(mContext.getString(R.string.f_week));
                holder.mImgFilter.setBackground(mContext.getResources().getDrawable(R.drawable.ic_calendar));
                break;
            case "month":
                holder.itemView.setBackgroundColor(mFilterSelectedStr.equalsIgnoreCase("month") ? ContextCompat.getColor(mContext, R.color.read_gray):ContextCompat.getColor(mContext, R.color.white) );

                holder.mFilterNameTxt.setText(mContext.getString(R.string.f_month));
                holder.mImgFilter.setBackground(mContext.getResources().getDrawable(R.drawable.ic_calendar));
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterfaceEdtBtnCallback.onPositiveClick(mDeviceFiltersArrList.get(holder.getAdapterPosition()));
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

        @BindView(R.id.filter_img)
        ImageView mImgFilter;

        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


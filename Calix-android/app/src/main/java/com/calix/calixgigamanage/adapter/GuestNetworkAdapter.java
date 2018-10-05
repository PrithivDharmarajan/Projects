package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.DurationEntity;
import com.calix.calixgigamanage.output.model.GuestWifiEntity;
import com.calix.calixgigamanage.ui.guest.SetupGuestNetwork;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DateUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GuestNetworkAdapter extends RecyclerView.Adapter<GuestNetworkAdapter.ControlHolder> {

    private Context mContext;
    private ArrayList<GuestWifiEntity> mGuestWifiRes;

    public GuestNetworkAdapter(ArrayList<GuestWifiEntity> guestWifiRes, Context context) {
        mContext = context;
        mGuestWifiRes = guestWifiRes;

    }


    @NonNull
    @Override
    public ControlHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_guest_network, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ControlHolder holder, int position) {

        GuestWifiEntity guestWifiEntityRes = mGuestWifiRes.get(position);

        holder.mEventNameTxt.setText(guestWifiEntityRes.getEventName().isEmpty()?guestWifiEntityRes.getGuestWifiName():guestWifiEntityRes.getEventName());
         if (!guestWifiEntityRes.isIndefinite()) {
            holder.mEventTimeTxt.setText(String.format(mContext.getString(R.string.activated_time),
                    (DateUtil.getCustomDateAndTimeFormat(mGuestWifiRes.get(position).getDuration().getStartTime(), AppConstants.CUSTOM_DATE_TIME_FORMAT) + " " + mContext.getString(R.string.to) + " " + DateUtil.getCustomDateAndTimeFormat(mGuestWifiRes.get(position).getDuration().getEndTime(), AppConstants.CUSTOM_DATE_TIME_FORMAT))));
        }else {
            holder.mEventTimeTxt.setText(mContext.getText(R.string.indefinite_network));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGuestWifiRes.get(holder.getAdapterPosition()).isIndefinite()) {
                    DurationEntity durationEntity = new DurationEntity();
                    durationEntity.setStartTime(System.currentTimeMillis());
                    durationEntity.setEndTime(System.currentTimeMillis());
                    mGuestWifiRes.get(holder.getAdapterPosition()).setDuration(durationEntity);
                }

                AppConstants.GUEST_WIFI_DETAILS = mGuestWifiRes.get(holder.getAdapterPosition());
                ((BaseActivity) mContext).nextScreen(SetupGuestNetwork.class);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mGuestWifiRes.size();
    }

    class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_name_txt)
        TextView mEventNameTxt;

        @BindView(R.id.event_time_txt)
        TextView mEventTimeTxt;

        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


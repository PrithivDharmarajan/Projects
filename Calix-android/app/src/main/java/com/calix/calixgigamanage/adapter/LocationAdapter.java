package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.LocationEntry;
import com.calix.calixgigamanage.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sibaprasad on 2/9/2018.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Holder> {

    private Context mContext;
    private ArrayList<LocationEntry> mLocationEntryRes;

    public LocationAdapter(ArrayList<LocationEntry> locationEntryRes, Context context) {
        mContext = context;
        mLocationEntryRes = locationEntryRes;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_location, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        LocationEntry locationEntryRes = mLocationEntryRes.get(position);
        holder.mLocationTxt.setText(locationEntryRes.getLocationName());

        /*This is for click listener*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.IOT_DEVICE_DETAILS.setLocationName(mLocationEntryRes.get(holder.getAdapterPosition()).getLocationName());
                AppConstants.IOT_DEVICE_DETAILS.setLocationId(mLocationEntryRes.get(holder.getAdapterPosition()).getLocationId());
                ((BaseActivity)mContext).backScreen();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocationEntryRes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

      /*  @BindView(R.id.alert_lay)
        RelativeLayout mBackgroundColor;*/


        @BindView(R.id.loc_txt)
        TextView mLocationTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

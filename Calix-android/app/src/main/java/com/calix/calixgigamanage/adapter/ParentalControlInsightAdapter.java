package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.calix.calixgigamanage.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ParentalControlInsightAdapter extends RecyclerView.Adapter<ParentalControlInsightAdapter.ControlHolder> {

    private Context mContext;

    public ParentalControlInsightAdapter(Context context) {
        mContext = context;

    }


    @Override
    public ControlHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ControlHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pc_insight, parent, false));
    }

    @Override
    public void onBindViewHolder(final ControlHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mSightAppDetailsLay.setVisibility(View.VISIBLE);
            }
        });

        holder.mInsightEyeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mSightAppDetailsLay.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class ControlHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.insight_app_details_lay)
        RelativeLayout mSightAppDetailsLay;

        @BindView(R.id.insight_eye_icon_img)
        ImageView mInsightEyeImg;



        private ControlHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


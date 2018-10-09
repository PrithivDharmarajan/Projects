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
import com.calix.calixgigaspireapp.output.model.InsightsEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopUpListAdapter extends RecyclerView.Adapter<PopUpListAdapter.InsightsListHolder> {

    private Context mContext;
    private ArrayList<InsightsEntity> mPopupListResponse;

    public PopUpListAdapter(ArrayList<InsightsEntity> popupListResponse, Context context) {
        mPopupListResponse = popupListResponse;
        mContext = context;
    }

    @NonNull
    @Override
    public PopUpListAdapter.InsightsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InsightsListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_insights_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopUpListAdapter.InsightsListHolder holder, int position) {
        //InsightsEntity popupListResponse = mPopupListResponse.get(position);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class InsightsListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.insights_details_txt)
        TextView socialNtwkName;

        @BindView(R.id.insights_details_hour_txt)
        TextView socialNtwkHours;

        private InsightsListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

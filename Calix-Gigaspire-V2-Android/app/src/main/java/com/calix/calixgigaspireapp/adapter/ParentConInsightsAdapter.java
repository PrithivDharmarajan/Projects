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
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.InsightsEntity;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentConInsightsAdapter extends RecyclerView.Adapter<ParentConInsightsAdapter.InsightsHolder> {

    private Context mContext;
    private ArrayList<InsightsEntity> mInsidesListResponse;

    public ParentConInsightsAdapter(ArrayList<InsightsEntity> insidesListResponse, Context context) {
        mInsidesListResponse = insidesListResponse;
        mContext = context;
    }

    @NonNull
    @Override
    public ParentConInsightsAdapter.InsightsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InsightsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_insights_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ParentConInsightsAdapter.InsightsHolder holder, final int position) {
        //InsightsEntity insidesListResponse = mInsidesListResponse.get(position);

        /*   Item onclick*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().showInsideDetailsPopup(mContext, mContext.getString(R.string.videos), mContext.getString(R.string.hours), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class InsightsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_insights_img)
        ImageView userImage;

        @BindView(R.id.insights_name_txt)
        TextView userName;

        @BindView(R.id.hours_txt)
        TextView countHours;

        private InsightsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

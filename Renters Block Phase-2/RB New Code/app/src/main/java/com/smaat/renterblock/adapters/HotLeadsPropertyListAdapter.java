package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.HotLeadsEntity;
import com.smaat.renterblock.entity.LeadsEntity;
import com.smaat.renterblock.fragments.HotLeadsViewerFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceAPICommonCallback;
import com.smaat.renterblock.utils.InterfaceBtnCallback;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HotLeadsPropertyListAdapter extends RecyclerView.Adapter<HotLeadsPropertyListAdapter.Holder> {
    private ArrayList<HotLeadsEntity> mHotLeadsList;
    private Context mContext;

    public HotLeadsPropertyListAdapter(Context context, ArrayList<HotLeadsEntity> hotListArrayList) {
        mHotLeadsList = hotListArrayList;
        mContext = context;


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hotleads, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final HotLeadsEntity entity = mHotLeadsList.get(holder.getAdapterPosition());
        try {


            if (entity.getPropertyImage().isEmpty()) {
                holder.mPropertyImg.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(entity.getPropertyImage()).into(holder.mPropertyImg);
                } catch (Exception e) {
                    holder.mPropertyImg.setImageResource(R.drawable.default_prop_icon);
                }
            }
            holder.mCostTxt.setText(mContext.getString(R.string.dollor) + ""
                    + entity.getPrice_range() + mContext.getString(R.string.mo));
            holder.mViewCountTxt.setText(String.format(mContext.getString(R.string.view_format), entity.getOverallcount()));
            holder.mHotLeadsCountTxt.setText(String.format(mContext.getString(R.string.hot_leads_format), entity.getLeads_count()));
            holder.mDetailTxt.setText(entity.getBeds() + " " + mContext.getString(R.string.bed) + " "
                    + entity.getBaths() + " " + mContext.getString(R.string.bath));
            holder.mLocationTxt.setText(entity.getAddress());
            holder.mRatingBar.setRating(Float.valueOf(entity.getProperty_rating()));
            holder.mReviewCountTxt.setText("(" + entity.getReview_count() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<LeadsEntity> leadsList = new ArrayList<>();
                if (entity.getLeadslist().getActive() != null) {
                    leadsList.addAll(entity.getLeadslist().getActive());
                }
                if (entity.getLeadslist().getPassive() != null) {
                    leadsList.addAll(entity.getLeadslist().getPassive());
                }
                AppConstants.LEADS_VIEW_LIST = leadsList;
                ((HomeScreen) mContext).addFragment(new HotLeadsViewerFragment());
               // callHotLeadsUpdateAPI(entity.getProperty_id());


            }
        });
    }

    @Override
    public int getItemCount() {
        return mHotLeadsList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.forward_img)
        ImageView mForwardImg;

        @BindView(R.id.property_img)
        ImageView mPropertyImg;

        @BindView(R.id.view_count_txt)
        TextView mViewCountTxt;

        @BindView(R.id.hot_leads_count_txt)
        TextView mHotLeadsCountTxt;

        @BindView(R.id.property_location_txt)
        TextView mLocationTxt;

        @BindView(R.id.property_cost_txt)
        TextView mCostTxt;

        @BindView(R.id.property_details_txt)
        TextView mDetailTxt;

        @BindView(R.id.property_reviews_count_txt)
        TextView mReviewCountTxt;

        @BindView(R.id.review_rating_bar)
        RatingBar mRatingBar;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void callHotLeadsUpdateAPI(String propertyId) {
        APIRequestHandler.getInstance().hotLeadsUpdateAPICall(mContext, propertyId, new InterfaceAPICommonCallback() {
            @Override
            public void onRequestSuccess(Object obj) {
                if (obj instanceof CommonResponse) {
                    CommonResponse commonResponse = (CommonResponse) obj;
                    if (commonResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                        ((HomeScreen) mContext).addFragment(new HotLeadsViewerFragment());
                    } else {
                        DialogManager.getInstance().showAlertPopup(mContext, commonResponse.getMsg(), mCallback);

                    }
                }
            }

            @Override
            public void onRequestFailure(Throwable t) {
                if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                        || t.getMessage() == null) {
                    DialogManager.getInstance().showAlertPopup(mContext, mContext.getString(R.string.no_internet), mCallback);
                } else if (t.getCause() instanceof java.net.SocketTimeoutException) {
                    DialogManager.getInstance().showAlertPopup(mContext, mContext.getString(R.string.connect_time_out), mCallback);
                } else if (t.getMessage() != null && !t.getMessage().isEmpty()) {
                    DialogManager.getInstance().showAlertPopup(mContext, t.getMessage(), mCallback);
                }
            }
        });

    }

    private InterfaceBtnCallback mCallback = new InterfaceBtnCallback() {
        @Override
        public void onPositiveClick() {

        }
    };

}

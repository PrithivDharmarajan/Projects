package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyReviewCommentEntity;
import com.smaat.renterblock.utils.DateUtil;
import com.smaat.renterblock.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 29-Aug-17.
 */

public class MyReviewDetailsAdapter extends RecyclerView.Adapter<MyReviewDetailsAdapter.Holder> {

    private Context mContext;
    private ArrayList<PropertyReviewCommentEntity> mReviewEntitiesResponse;

    public MyReviewDetailsAdapter(Context context, ArrayList<PropertyReviewCommentEntity> reviewEntitiesRes) {
        mContext = context;
        mReviewEntitiesResponse = reviewEntitiesRes;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_review_deatils_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        PropertyReviewCommentEntity reviewEntityRes = mReviewEntitiesResponse.get(position);

        holder.mMinutesTxt.setText(DateUtil.getTimeDifference(reviewEntityRes.getReview_date_time()));
        holder.mMainCommentTxt.setText(reviewEntityRes.getReview_comments());
        holder.mRatingBar.setRating(NumberUtil.getRatingVal(reviewEntityRes.getReview_rating()));

    }

    @Override
    public int getItemCount() {
        return mReviewEntitiesResponse.size();

    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.minutes_txt)
        TextView mMinutesTxt;

        @BindView(R.id.rating_bar)
        RatingBar mRatingBar;

        @BindView(R.id.main_comment_txt)
        TextView mMainCommentTxt;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

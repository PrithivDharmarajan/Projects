package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyReviewCommentEntity;
import com.smaat.renterblock.entity.ReviewEntity;
import com.smaat.renterblock.entity.ReviewPropertyEntity;
import com.smaat.renterblock.fragments.ReviewsDetailsFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 29-Aug-17.
 */

public class MyReviewListAdapter extends RecyclerView.Adapter<MyReviewListAdapter.Holder> {

    private Context mContext;
    private ReviewPropertyEntity mReviewEntitiesResponse;

    public MyReviewListAdapter(Context context, ReviewPropertyEntity reviewEntitiesRes) {
        mContext = context;
        mReviewEntitiesResponse = reviewEntitiesRes;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_review_list_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        ReviewEntity reviewEntityRes = mReviewEntitiesResponse.getReview().get(position);

        if (reviewEntityRes.getPro_img().isEmpty()) {
            holder.mBuilderImg.setImageResource(R.drawable.default_prop_icon);

        } else {
            try {
                Glide.with(mContext)
                        .load(reviewEntityRes.getPro_img())
                        .into(holder.mBuilderImg);
            } catch (Exception ex) {
                holder.mBuilderImg.setImageResource(R.drawable.default_prop_icon);
                Log.d(AppConstants.TAG, ex.getMessage());
            }
        }
        holder.mPropertyAddressTxt.setText(reviewEntityRes.getAddress());
        holder.mCommentsTxt.setText(reviewEntityRes.getProperty_review_comment().size() > 0 ?
                reviewEntityRes.getProperty_review_comment().get(reviewEntityRes.getProperty_review_comment().size() - 1).getReview_comments() :
                reviewEntityRes.getComments());
        holder.mReviewRatingBar.setRating(NumberUtil.getRatingVal(reviewEntityRes.getProperty_review_comment().size() > 0 ?
                reviewEntityRes.getProperty_review_comment().get(reviewEntityRes.getProperty_review_comment().size() - 1).getReview_rating() :
                reviewEntityRes.getRating()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.REVIEW_DETAILS_RES = new ReviewPropertyEntity();
                ArrayList<ReviewEntity> reviewRes = new ArrayList<>();

                reviewRes.add(mReviewEntitiesResponse.getReview().get(holder.getAdapterPosition()));
                if (reviewRes.get(0).getProperty_review_comment().size() ==0) {
                    PropertyReviewCommentEntity propertyReviewCommentEntity=new PropertyReviewCommentEntity();
                    propertyReviewCommentEntity.setProperty_id(reviewRes.get(0).getProperty_id());
                    propertyReviewCommentEntity.setProperty_review_comment_id(reviewRes.get(0).getProperty_review_id());
                    propertyReviewCommentEntity.setReview_date_time(reviewRes.get(0).getDate_time());
                    propertyReviewCommentEntity.setReview_user_id(reviewRes.get(0).getReview_user_id());
                    propertyReviewCommentEntity.setReview_comments(reviewRes.get(0).getComments());
                    propertyReviewCommentEntity.setReview_rating(reviewRes.get(0).getRating());

                    ArrayList<PropertyReviewCommentEntity> propertyReviewCmd=new ArrayList<>();
                    propertyReviewCmd.add(propertyReviewCommentEntity);
                    reviewRes.get(0).setProperty_review_comment(propertyReviewCmd);

                }
                AppConstants.REVIEW_DETAILS_RES.setReview(reviewRes);
                AppConstants.REVIEW_DETAILS_RES.setUser_details(mReviewEntitiesResponse.getUser_details());
                ((HomeScreen) mContext).addFragment(new ReviewsDetailsFragment());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReviewEntitiesResponse.getReview().size();

    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.builder_img)
        ImageView mBuilderImg;

        @BindView(R.id.property_address_txt)
        TextView mPropertyAddressTxt;

        @BindView(R.id.review_rating_bar)
        RatingBar mReviewRatingBar;

        @BindView(R.id.comments_txt)
        TextView mCommentsTxt;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

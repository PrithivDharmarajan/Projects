package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.fragments.PropertyDetailsFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DateUtil;
import com.smaat.renterblock.utils.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sys on 10/6/2017.
 */

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.Holder> {

    private Context mContext;
    private ArrayList<ProfileMyFeedsEntity> mProfileFeedsList = new ArrayList<>();

    public FeedsAdapter(Context context, ArrayList<ProfileMyFeedsEntity> mList) {
        mProfileFeedsList = mList;
        mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_feeds_row_list, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final ProfileMyFeedsEntity profileMyFeedsEntity = mProfileFeedsList.get(position);
        holder.mUserNameTxt.setText(profileMyFeedsEntity.getUser_name());
        holder.mFriendCountTxt.setText(profileMyFeedsEntity.getFriends_count());
        holder.mReviewCountTxt.setText(profileMyFeedsEntity.getReviews_count());
        holder.mPhotoCountTxt.setText(profileMyFeedsEntity.getPhotos_Count());

        if (!profileMyFeedsEntity.getDatetime().isEmpty())
            holder.mTimeStatusTxt.setText(mContext.getString(R.string.last_updated) + "  " + DateUtil.timeDiff(profileMyFeedsEntity.getDatetime()));


        if (profileMyFeedsEntity.getUser_profile_image().isEmpty()) {
            holder.mProfileImg.setImageResource(R.drawable.default_profile_icon);
        } else {
            try {
                Glide.with(mContext)
                        .load(profileMyFeedsEntity.getUser_profile_image())
                        .asBitmap()
                        .error(R.drawable.default_profile_icon)
                        .into(holder.mProfileImg);
            } catch (Exception ex) {
                holder.mProfileImg.setImageResource(R.drawable.default_profile_icon);
            }
        }

        if (profileMyFeedsEntity.getReview_user_id() != null) {
            holder.mPropertyNameTxt.setText("Wrote a Review for "
                    + profileMyFeedsEntity.getAddress());
            if (profileMyFeedsEntity.getRating() != null
                    && !profileMyFeedsEntity.getRating()
                    .equalsIgnoreCase("")) {
                holder.mUserReviewRatingBar.setRating(Float.valueOf(profileMyFeedsEntity
                        .getRating()));
            } else {
                holder.mUserReviewRatingBar.setRating(0);
            }

            holder.mUserReviewTxt.setText(profileMyFeedsEntity.getComments());

            holder.mUserReviewRatingBar.setVisibility(View.VISIBLE);
            holder.mUserReviewTxt.setVisibility(View.VISIBLE);
            holder.mProfilePhotoLay.setVisibility(View.GONE);
            holder.mProfileVideoLay.setVisibility(View.GONE);

        } else if (profileMyFeedsEntity.getPost_id() != null) {
            holder.mPropertyNameTxt.setText("Wrote a Comment for "
                    + profileMyFeedsEntity.getAddress());
            holder.mUserReviewTxt.setText(profileMyFeedsEntity.getComments());

            holder.mUserReviewRatingBar.setVisibility(View.GONE);
            holder.mUserReviewTxt.setVisibility(View.VISIBLE);
            holder.mProfilePhotoLay.setVisibility(View.GONE);
            holder.mProfileVideoLay.setVisibility(View.GONE);
        } else {
            if (profileMyFeedsEntity.getFile_type().equals("image")) {
                holder.mPropertyNameTxt.setText("Added a Photo for "
                        + profileMyFeedsEntity.getAddress());
                holder.mUserReviewRatingBar.setVisibility(View.GONE);
                holder.mUserReviewTxt.setVisibility(View.GONE);
                holder.mProfilePhotoLay.setVisibility(View.VISIBLE);
                holder.mProfileVideoLay.setVisibility(View.GONE);

                if (profileMyFeedsEntity.getFile().isEmpty()) {
                    holder.mListImg.setImageResource(R.drawable.default_prop_icon);
                } else {
                    try {

                        Glide.with(mContext)
                                .load(profileMyFeedsEntity.getFile())
                                .asBitmap().override(350, 250).error(R.drawable.default_prop_icon)
                                .error(R.drawable.default_prop_icon)
                                .into(holder.mListImg);
                    } catch (Exception ex) {
                        holder.mListImg.setImageResource(R.drawable.default_prop_icon);
                    }
                }


            } else if (profileMyFeedsEntity.getFile_type().equals("video")) {
                holder.mPropertyNameTxt.setText("Added a Video for "
                        + profileMyFeedsEntity.getAddress());
                holder.mUserReviewRatingBar.setVisibility(View.GONE);
                holder.mUserReviewTxt.setVisibility(View.GONE);
                holder.mProfilePhotoLay.setVisibility(View.GONE);
                holder.mProfileVideoLay.setVisibility(View.VISIBLE);

            }
        }

        holder.mProfileVideoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().showImageVideoDialog(mContext, profileMyFeedsEntity.getFile_type(),
                        profileMyFeedsEntity.getFile(), profileMyFeedsEntity.getDescription(),
                        profileMyFeedsEntity.getDatetime(), profileMyFeedsEntity.getFriends_count(),
                        profileMyFeedsEntity.getReviews_count()
                        , profileMyFeedsEntity.getPhotos_Count(), profileMyFeedsEntity.getUser_name(), profileMyFeedsEntity.getUser_profile_image());
            }
        });
        holder.mProfilePhotoLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().showImageVideoDialog(mContext, profileMyFeedsEntity.getFile_type(),
                        profileMyFeedsEntity.getFile(), profileMyFeedsEntity.getDescription(),
                        profileMyFeedsEntity.getDatetime(), profileMyFeedsEntity.getFriends_count(),
                        profileMyFeedsEntity.getReviews_count()
                        , profileMyFeedsEntity.getPhotos_Count(), profileMyFeedsEntity.getUser_name(), profileMyFeedsEntity.getUser_profile_image());
            }
        });
        holder.mPropLocLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (profileMyFeedsEntity.getPost_id() != null) {
                AppConstants.DETAIL_PROPERTY_ID = profileMyFeedsEntity.getProperty_id();
                ((HomeScreen) mContext).addFragment(new PropertyDetailsFragment());

                // } else {


            }
        });


    }

    @Override
    public int getItemCount() {
        return mProfileFeedsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_img)
        ImageView mProfileImg;

        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        @BindView(R.id.send_request_btn)
        Button mSendReqBtn;

        @BindView(R.id.time_status_txt)
        TextView mTimeStatusTxt;

        @BindView(R.id.property_name_txt)
        TextView mPropertyNameTxt;

        @BindView(R.id.user_review_rating_bar)
        RatingBar mUserReviewRatingBar;


        @BindView(R.id.user_review_txt)
        TextView mUserReviewTxt;

        @BindView(R.id.profile_photo_view_lay)
        RelativeLayout mProfilePhotoLay;

        @BindView(R.id.profile_video_lay)
        RelativeLayout mProfileVideoLay;

        @BindView(R.id.friends_count)
        TextView mFriendCountTxt;
        @BindView(R.id.reviews_count)
        TextView mReviewCountTxt;
        @BindView(R.id.photos_count)
        TextView mPhotoCountTxt;

        @BindView(R.id.property_location_lay)
        RelativeLayout mPropLocLay;

        @BindView(R.id.list_image_view)
        ImageView mListImg;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

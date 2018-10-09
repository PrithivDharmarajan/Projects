package com.smaat.renterblock.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FindAgentDetailReviewResultEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgentMoreReviewAdatper extends RecyclerView.Adapter<AgentMoreReviewAdatper.Holder> {

    private Context mContext;
    private ArrayList<FindAgentDetailReviewResultEntity> mMoreReviewList = new ArrayList<>();

    public AgentMoreReviewAdatper(Context context, ArrayList<FindAgentDetailReviewResultEntity> mList) {

        mMoreReviewList = mList;
        mContext = context;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_more_review, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final FindAgentDetailReviewResultEntity findAgentFilterEntity = mMoreReviewList.get(position);

        if (mMoreReviewList.size() != 0) {
            if (findAgentFilterEntity.getUser_profileImage().isEmpty()) {
                holder.mReviewUserImg.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(findAgentFilterEntity.getUser_profileImage()).placeholder(R.drawable.profile_pic).into(holder.mReviewUserImg);
                } catch (Exception e) {
                    holder.mReviewUserImg.setImageResource(R.drawable.profile_pic);
                }
            }
            holder.mReivewUserName.setText(findAgentFilterEntity.getName());
            holder.mReviewFriendCountTxt.setText(findAgentFilterEntity.getFriends_count());
            holder.mReviewReviewsCountTxt.setText(findAgentFilterEntity.getReviews_count());
            holder.mReviewPhotoCountTxt.setText(findAgentFilterEntity.getPhotos_count());
            holder.mReviewRatingBar.setRating(NumberUtil.getRatingVal(findAgentFilterEntity.getUser_avg_rating()));
            holder.mReviewCommentsFullTxt.setText(findAgentFilterEntity.getComments());
            holder.mReviewCommentsTxt.setText(findAgentFilterEntity.getComments());
            if (holder.mReviewCommentsFullTxt.getText().toString().length() > 70) {
                holder.mReviewReadMoreTxt.setVisibility(View.VISIBLE);
            } else {
                holder.mReviewReadMoreTxt.setVisibility(View.GONE);
            }

            holder.mReviewReadMoreTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.mReviewCommentsFullTxt.setVisibility(View.VISIBLE);
                    holder.mReviewCommentsTxt.setVisibility(View.GONE);
                    holder.mReviewReadMoreTxt.setVisibility(View.GONE);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mMoreReviewList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.adap_review_user_image)
        ImageView mReviewUserImg;
        @BindView(R.id.adap_review_user_name_txt)
        TextView mReivewUserName;
        @BindView(R.id.adap_review_friends_count_txt)
        TextView mReviewFriendCountTxt;
        @BindView(R.id.adap_review_photos_count_txt)
        TextView mReviewPhotoCountTxt;
        @BindView(R.id.adap_review_reviews_count_txt)
        TextView mReviewReviewsCountTxt;
        @BindView(R.id.adap_review_user_rating_bar)
        RatingBar mReviewRatingBar;
        @BindView(R.id.adap_review_comments_full_txt)
        TextView mReviewCommentsFullTxt;
        @BindView(R.id.adap_review_comments_txt)
        TextView mReviewCommentsTxt;
        @BindView(R.id.adap_read_more_txt)
        TextView mReviewReadMoreTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public class PropertyImageAdapter extends PagerAdapter {

        Context mContext;
        ArrayList<PropertyPictures> mPropertyImgList;
        LayoutInflater mLayoutInflater;
        ImageView mPagerPropertyImage;

        private PropertyImageAdapter(Context context, ArrayList<PropertyPictures> mPropertyImageList) {
            mContext = context;
            mPropertyImgList = mPropertyImageList;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mPropertyImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = mLayoutInflater.inflate(R.layout.adapter_image_pager, container, false);
            mPagerPropertyImage = view.findViewById(R.id.property_image);

            try {
                Glide.with(mContext)
                        .load(mPropertyImgList.get(position).getFile()).into(mPagerPropertyImage);
            } catch (Exception e) {
                mPagerPropertyImage.setImageResource(R.drawable.default_prop_icon);
            }


            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}

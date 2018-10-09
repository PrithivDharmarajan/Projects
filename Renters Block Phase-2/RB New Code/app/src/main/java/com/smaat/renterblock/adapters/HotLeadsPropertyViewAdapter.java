package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.LeadsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;
import com.smaat.renterblock.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HotLeadsPropertyViewAdapter extends RecyclerView.Adapter<HotLeadsPropertyViewAdapter.Holder> {

    private BaseFragment mContext;
    private ArrayList<LeadsEntity> mLeadsList;

    public HotLeadsPropertyViewAdapter(BaseFragment context, ArrayList<LeadsEntity> leadsEntityList) {
        mContext = context;
        mLeadsList = leadsEntityList;


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_leads, parent, false);
        return new HotLeadsPropertyViewAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        LeadsEntity leadsEntity = mLeadsList.get(holder.getAdapterPosition());
        try {
            holder.mUserNameTxt.setText(leadsEntity.getUser_name());
            if (leadsEntity.getUser_profileImage().isEmpty()) {
                holder.mProfileImg.setImageResource(R.drawable.profile_pic);
            } else {
                try {
                    Glide.with(mContext)
                            .load(leadsEntity.getUser_profileImage()).into(holder.mProfileImg);
                } catch (Exception e) {
                    holder.mProfileImg.setImageResource(R.drawable.profile_pic);
                }
            }
            if (Integer.parseInt(leadsEntity.getCount()) >= 3) {
                holder.mFlameImg.setVisibility(View.VISIBLE);
                holder.mBinocularImg.setVisibility(View.VISIBLE);
            }
            holder.mFavouriteImg.setVisibility(leadsEntity.getIsfavourite().equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
            holder.mPhotosCountTxt.setText(leadsEntity.getPhotos_count());
            holder.mFriendsCountTxt.setText(leadsEntity.getFriends_count());
            holder.mReviewsCountTxt.setText(leadsEntity.getReviews_count());
            holder.mTimeStatusTxt.setText(TimeUtil.getTimeDifference(leadsEntity.getDatetime()));
//            holder.mChatImg.setVisibility(View.GONE);

            if (leadsEntity.getUserId().equalsIgnoreCase(PreferenceUtil.getUserID(mContext.getActivity()))) {
                holder.mChatImg.setVisibility(View.GONE);
            } else {
                holder.mChatImg.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
        holder.mChatImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    APIRequestHandler.getInstance().getChatID(mLeadsList.get(holder.getAdapterPosition()).getUserId(),mLeadsList.get(holder.getAdapterPosition()).getUser_name(), mContext);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mLeadsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        @BindView(R.id.user_profile_img)
        ImageView mProfileImg;

        @BindView(R.id.online_status_img)
        ImageView mOnlineStatusImg;

        @BindView(R.id.requested_info_txt)
        TextView mRequestedInfoTxt;

        @BindView(R.id.fav_img)
        ImageView mFavouriteImg;

        @BindView(R.id.binocular_img)
        ImageView mBinocularImg;

        @BindView(R.id.flame_img)
        ImageView mFlameImg;

        @BindView(R.id.photos_count_txt)
        TextView mPhotosCountTxt;

        @BindView(R.id.friends_count_txt)
        TextView mFriendsCountTxt;

        @BindView(R.id.reviews_count_txt)
        TextView mReviewsCountTxt;

        @BindView(R.id.time_status_txt)
        TextView mTimeStatusTxt;

        @BindView(R.id.chat_img)
        ImageView mChatImg;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

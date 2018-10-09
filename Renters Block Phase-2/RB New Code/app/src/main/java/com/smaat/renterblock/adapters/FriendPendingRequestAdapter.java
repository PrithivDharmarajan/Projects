package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PendingDetailsEntity;
import com.smaat.renterblock.fragments.FriendPendingRequestFragment;
import com.smaat.renterblock.services.APIRequestHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 10/13/2017.
 */

public class FriendPendingRequestAdapter extends RecyclerView.Adapter<FriendPendingRequestAdapter.Holder> {

    Context mContext;
    ArrayList<PendingDetailsEntity> mFriendsPendingList;
    FriendPendingRequestFragment mFriendPendingRequestFragment;

    public FriendPendingRequestAdapter(Context activity, ArrayList<PendingDetailsEntity> mFriendsListArray, FriendPendingRequestFragment mFragment) {
        mContext = activity;
        mFriendsPendingList = mFriendsListArray;
        mFriendPendingRequestFragment = mFragment;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_friends_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        holder.mCallIconLay.setVisibility(View.GONE);
        holder.mRequestSentLay.setVisibility(View.GONE);
        holder.mAcceptRejectLay.setVisibility(View.VISIBLE);

        holder.mUserNameTxt.setText(mFriendsPendingList.get(position).getFriends_details().get(0).getFirst_name());
        holder.mFriendsCountTxt.setText(mFriendsPendingList.get(position).getFriends_details().get(0).getFriends());
        holder.mReviewsCountTxt.setText(mFriendsPendingList.get(position).getFriends_details().get(0).getReview());
        holder.mPhotosCountTxt.setText(mFriendsPendingList.get(position).getFriends_details().get(0).getPhotos());

        if (mFriendsPendingList.get(position).getFriends_details().get(0).getUser_pic().isEmpty()) {
            holder.mUserLogo.setImageResource(R.drawable.default_prop_icon);
        } else {
            try {
                Glide.with(mContext)
                        .load(mFriendsPendingList.get(position).getFriends_details().get(0).getUser_pic()).placeholder(R.drawable.profile_pic).into(holder.mUserLogo);
            } catch (Exception e) {
                holder.mUserLogo.setImageResource(R.drawable.profile_pic);
            }
        }

        holder.mAcceptTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().acceptedFriendRequest(mFriendsPendingList.get(holder.getAdapterPosition()).getFriends_details().get(0).getUser_friend_id(), mFriendPendingRequestFragment);
            }
        });

        holder.mRejectTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().rejectedFriendRequest(mFriendsPendingList.get(holder.getAdapterPosition()).getFriends_details().get(0).getUser_friend_id(), mFriendPendingRequestFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendsPendingList.size();

    }


    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_logo)
        CircleImageView mUserLogo;

        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        @BindView(R.id.friends_count_txt)
        TextView mFriendsCountTxt;

        @BindView(R.id.reviews_count_txt)
        TextView mReviewsCountTxt;

        @BindView(R.id.photos_count_txt)
        TextView mPhotosCountTxt;

        @BindView(R.id.call_icon_lay)
        LinearLayout mCallIconLay;

        @BindView(R.id.pending_txt)
        TextView mPendingTxt;

        @BindView(R.id.call_img)
        ImageView mCallImg;

        @BindView(R.id.request_sent_lay)
        LinearLayout mRequestSentLay;

        @BindView(R.id.accept_reject_lay)
        LinearLayout mAcceptRejectLay;

        @BindView(R.id.accept_txt)
        TextView mAcceptTxt;

        @BindView(R.id.reject_txt)
        TextView mRejectTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

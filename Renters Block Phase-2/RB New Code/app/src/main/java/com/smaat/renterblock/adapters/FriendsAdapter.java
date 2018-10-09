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
import com.smaat.renterblock.entity.AcceptFriendEntity;
import com.smaat.renterblock.fragments.FriendsFragment;
import com.smaat.renterblock.fragments.ProfileFragment;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.Holder> {

    private Context mContext;
    private ArrayList<AcceptFriendEntity> mFriendsList;
    private BaseFragment mAPICallBackFrag;

    public FriendsAdapter(Context activity, ArrayList<AcceptFriendEntity> mFriendsListArray, BaseFragment baseFragment) {
        mContext = activity;
        mFriendsList = mFriendsListArray;
        mAPICallBackFrag = baseFragment;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_friends_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        holder.mRequestSentLay.setVisibility(View.GONE);
        if (mFriendsList.get(position).getFriends_details().size() > 0) {
            if (mFriendsList.get(position).getFriends_details().get(0).getIspending().isEmpty() || mFriendsList.get(position).getFriends_details().get(0).getIspending() == null) {
                holder.mPendingTxt.setVisibility(View.GONE);
                holder.mCallImg.setVisibility(View.VISIBLE);
            } else {
                holder.mPendingTxt.setVisibility(View.VISIBLE);
                holder.mCallImg.setVisibility(View.GONE);
            }

            holder.mUserNameTxt.setText(mFriendsList.get(position).getFriends_details().get(0).getUser_name());
            holder.mFriendsCountTxt.setText(mFriendsList.get(position).getFriends_details().get(0).getFriends());
            holder.mReviewsCountTxt.setText(mFriendsList.get(position).getFriends_details().get(0).getReview());
            holder.mPhotosCountTxt.setText(mFriendsList.get(position).getFriends_details().get(0).getPhotos());

            holder.mCallImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogManager.getInstance().callSelection(mContext);
                }
            });

            holder.mOnlineStatusTxt.setVisibility(mFriendsList.get(position).getFriends_details().get(0).getStatus()
                    .equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);


            if (mFriendsList.get(position).getFriends_details().get(0).getUser_pic().isEmpty()) {
                holder.mUserLogo.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(mFriendsList.get(position).getFriends_details().get(0).getUser_pic()).placeholder(R.drawable.profile_pic).into(holder.mUserLogo);
                } catch (Exception e) {
                    holder.mUserLogo.setImageResource(R.drawable.profile_pic);
                }
            }

            holder.mUserLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new FriendsFragment();
                    AppConstants.PROFILE_ID = mFriendsList.get(holder.getAdapterPosition()).getFriends_details().get(0).getUser_friend_id();
                    ((HomeScreen) mContext).addFragment(new ProfileFragment());

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!mFriendsList.get(holder.getAdapterPosition()).getFriends_details().get(0).getIspending().equalsIgnoreCase("Pending")) {

                        APIRequestHandler.getInstance().getChatID(mFriendsList.get(holder.getAdapterPosition()).getFriends_details().get(0).getUser_friend_id(), mFriendsList.get(holder.getAdapterPosition()).getFriends_details().get(0).getUser_name(), mAPICallBackFrag);
                    }


                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mFriendsList.size();
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

        @BindView(R.id.online_status)
        TextView mOnlineStatusTxt;

        @BindView(R.id.request_sent_lay)
        LinearLayout mRequestSentLay;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

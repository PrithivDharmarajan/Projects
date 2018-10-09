package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FriendsSearchResponseArray;
import com.smaat.renterblock.fragments.AddFriendsFragment;
import com.smaat.renterblock.services.APIRequestHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class FriendsSearchAdapter extends RecyclerView.Adapter<FriendsSearchAdapter.Holder> {

    Context mContext;
    ArrayList<FriendsSearchResponseArray> mFriendsList;
    private AddFriendsFragment mAddFriendsFragment;

    public FriendsSearchAdapter(Context activity, ArrayList<FriendsSearchResponseArray> mFriendsListArray,AddFriendsFragment mFragment) {
        mContext = activity;
        mFriendsList = mFriendsListArray;
        mAddFriendsFragment = mFragment;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_friends_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

       holder.mRequestSentLay.setVisibility(View.VISIBLE);
        holder.mCallIconLay.setVisibility(View.GONE);
        holder.mUserNameTxt.setText(mFriendsList.get(position).getFirst_name());
        holder.mFriendsCountTxt.setText(mFriendsList.get(position).getFriends());
        holder.mReviewsCountTxt.setText(mFriendsList.get(position).getReview());
        holder.mPhotosCountTxt.setText(mFriendsList.get(position).getPhotos());

        if (mFriendsList.get(position).getUser_pic().isEmpty()) {
           holder.mUserLogo.setImageResource(R.drawable.default_prop_icon);
        } else {
            try {
                Glide.with(mContext)
                        .load(mFriendsList.get(position).getUser_pic()).placeholder(R.drawable.profile_pic).into(holder.mUserLogo);
            } catch (Exception e) {
                holder.mUserLogo.setImageResource(R.drawable.profile_pic);
            }
        }

        holder.mRequestSentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().sendFriendRequest(mAddFriendsFragment, mFriendsList.get(holder.getAdapterPosition()).getUser_id());
            }
        });
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

        @BindView(R.id.request_sent_lay)
        LinearLayout mRequestSentLay;

        @BindView(R.id.main_lay)
        RelativeLayout mParrentLay;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

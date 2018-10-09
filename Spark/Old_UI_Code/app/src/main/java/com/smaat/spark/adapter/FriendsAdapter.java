package com.smaat.spark.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.fragment.UserDetailsFragment;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.Holder> {


    private ArrayList<UserDetailsEntity> mFriendsListRes;
    private Context mContext;

    public FriendsAdapter(Context context, ArrayList<UserDetailsEntity> friendsList) {
        mContext = context;
        mFriendsListRes = friendsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_friends_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        UserDetailsEntity friendsRes = mFriendsListRes.get(position);

        holder.mFriendsTxt.setText(friendsRes.getUsername());

        if (friendsRes.getMain_picture().isEmpty()) {
            holder.mFriendsImg.setImageResource(R.drawable.default_user_img);
        } else {
            try {
                Glide.with(mContext)
                        .load(friendsRes.getMain_picture())
                        .into(holder.mFriendsImg);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, e.toString());
                holder.mFriendsImg.setImageResource(R.drawable.default_user_img);
            }
        }
        holder.mFriendsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppConstants.OTHER_USER_ID = mFriendsListRes.get(holder.getAdapterPosition()).getUser_id();
                AppConstants.OTHER_USER_DETAILS = new Gson().toJson(mFriendsListRes.get(holder.getAdapterPosition()), UserDetailsEntity.class);
                ((HomeScreen) mContext).addFragment(new UserDetailsFragment());
            }
        });

        holder.mFriendsImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogManager.getInstance().showOriginalImgPopup(mContext, mFriendsListRes.get(holder.getAdapterPosition()).getMain_picture());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendsListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.friend_profile_img)
        ImageView mFriendsImg;

        @BindView(R.id.friend_name_txt)
        TextView mFriendsTxt;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}

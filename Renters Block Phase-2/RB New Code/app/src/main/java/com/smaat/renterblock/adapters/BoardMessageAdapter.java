package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.BoardMessageEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BoardMessageAdapter extends RecyclerView.Adapter<BoardMessageAdapter.Holder> {
    private BaseFragment mContext;
    private ArrayList<BoardMessageEntity>mMessageList=new ArrayList<>();

    public BoardMessageAdapter(BaseFragment context, ArrayList<BoardMessageEntity>boardMessageList){
        mContext=context;
        mMessageList=boardMessageList;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_board_chat, parent, false);
       return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final BoardMessageEntity msgEntity=mMessageList.get(position);
        holder.mFriendCountTxt.setText(msgEntity.getFriends_count());
        holder.mReviewCountTxt.setText(msgEntity.getReviews_count());
        holder.mPhotoCountTxt.setText(msgEntity.getPhotos_count());
        holder.mNameTxt.setText(msgEntity.getUser_name());
        holder.mTimeTxt.setText(TimeUtil.getTimeDifference(msgEntity.getDate_time()));
        holder.mMessageTxt.setText(msgEntity.getMessage());
        try {
            Glide.with(mContext)
                    .load(msgEntity.getUser_profileImage())
                    .into(holder.mProfileImg);
        } catch (Exception ex) {
            holder.mProfileImg.setImageResource(R.drawable.profile_pic);
            Log.e(AppConstants.TAG, ex.getMessage());
        }

        holder.mChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().getChatID(msgEntity.getUser_id(),msgEntity.getUser_name(),mContext);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.friends_count_txt)
        TextView mFriendCountTxt;
        @BindView(R.id.review_count_txt)
        TextView mReviewCountTxt;
        @BindView(R.id.photos_count_txt)
        TextView mPhotoCountTxt;
        @BindView(R.id.name_txt)
        TextView mNameTxt;
        @BindView(R.id.time_txt)
        TextView mTimeTxt;
        @BindView(R.id.chat_txt)
        TextView mMessageTxt;
        @BindView(R.id.profile_image)
        ImageView mProfileImg;
        @BindView(R.id.chat_btn)
        Button mChatBtn;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

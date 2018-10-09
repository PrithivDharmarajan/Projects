package com.smaat.spark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.fragment.ChatFriendFragment;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {


    private ArrayList<UserDetailsEntity> mNotificationListRes;
    private Context mContext;
    private BaseFragment mFrag;

    public NotificationAdapter(Context context, BaseFragment frag, ArrayList<UserDetailsEntity> friendsList) {
        mContext = context;
        mFrag = frag;
        mNotificationListRes = friendsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_notification_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        UserDetailsEntity notificationListRes = mNotificationListRes.get(position);

        holder.mFriendsTxt.setText(notificationListRes.getUsername());
        holder.mAddressTxt.setText(notificationListRes.getMessage().trim());

//        if (notificationListRes.getType().equalsIgnoreCase(mContext.getString(R.string.acc_friend_req)) || notificationListRes.getType().equalsIgnoreCase(mContext.getString(R.string.send_msg))) {
//            holder.mChatBtn.setVisibility(View.VISIBLE);
//            holder.mAcceptBtn.setVisibility(View.GONE);
//            holder.mRejectBtn.setVisibility(View.GONE);
//
//        } else {
//            holder.mAcceptBtn.setVisibility(View.VISIBLE);
//            holder.mRejectBtn.setVisibility(View.VISIBLE);
//            holder.mChatBtn.setVisibility(View.GONE);
//        }
        if (notificationListRes.getType().trim().equalsIgnoreCase(mContext.getString(R.string.friend_req))) {

            holder.mAcceptLay.setVisibility(View.VISIBLE);
            holder.mDeclineLay.setVisibility(View.VISIBLE);
            holder.mChatLay.setVisibility(View.GONE);

        } else if (notificationListRes.getType().trim().equalsIgnoreCase(mContext.getString(R.string.message)) ||
                notificationListRes.getType().trim().equalsIgnoreCase(mContext.getString(R.string.friend_req_acc))) {

            holder.mAcceptLay.setVisibility(View.GONE);
            holder.mDeclineLay.setVisibility(View.GONE);
            holder.mChatLay.setVisibility(View.VISIBLE);
        }

        if (notificationListRes.getMain_picture().isEmpty()) {
            holder.mFriendsImg.setImageResource(R.drawable.default_user_img);
        } else {
            try {
                Glide.with(mContext)
                        .load(notificationListRes.getMain_picture())
                        .into(holder.mFriendsImg);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, e.toString());
                holder.mFriendsImg.setImageResource(R.drawable.default_user_img);
            }
        }

        holder.mAcceptLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ACCEPT_REQUEST, AppConstants.PARAMS_ACCEPT_REQUEST, GlobalMethods.getUserID(mContext), mNotificationListRes.get(holder.getAdapterPosition()).getSender_id(), AppConstants.SUCCESS_CODE);
                APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, mFrag);
            }
        });
        holder.mDeclineLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ACCEPT_REQUEST, AppConstants.PARAMS_ACCEPT_REQUEST, GlobalMethods.getUserID(mContext), mNotificationListRes.get(holder.getAdapterPosition()).getSender_id(), mContext.getString(R.string.two));
                APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, mFrag);
            }
        });
        holder.mChatLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConstants.CHAT_FRIEND_ID = mNotificationListRes.get(holder.getAdapterPosition()).getSender_id();
                AppConstants.CHAT_FRIEND_NAME = mNotificationListRes.get(holder.getAdapterPosition()).getUsername();
                ((HomeScreen) mContext).resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);
                ((HomeScreen) mContext).setFooterImg(2);
                ((HomeScreen) mContext).resetFragmentStack(AppConstants.BOTTOM_BUTTON_POS);

                AppConstants.OTHER_USER_DETAILS = new Gson().toJson(mNotificationListRes.get(holder.getAdapterPosition()), UserDetailsEntity.class);

                AppConstants.CHAT_FRIEND_FROM_CONNECT=AppConstants.FAILURE_CODE;
                ((HomeScreen) mContext).replaceFragment(new ChatFriendFragment(), 1);
            }
        });
        holder.mFriendsImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogManager.getInstance().showOriginalImgPopup(mContext, mNotificationListRes.get(holder.getAdapterPosition()).getMain_picture());
                return true;
            }
        });

//        holder.mAcceptChatLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if(holder.mAcceptChatTxt.getText().equals(mContext.getString(R.string.accept))){
//                   ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ACCEPT_REQUEST, AppConstants.PARAMS_ACCEPT_REQUEST, GlobalMethods.getUserID(mContext), mNotificationListRes.get(holder.getAdapterPosition()).getSender_id(), AppConstants.SUCCESS_CODE);
//                   APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, mFrag);
//
//               }else{
//                   AppConstants.CHAT_SUB = AppConstants.CHAT_SUB_FRIEND;
//                   AppConstants.CHAT_FRIEND_ID = mNotificationListRes.get(holder.getAdapterPosition()).getSender_id();
//                   AppConstants.CHAT_FRIEND_NAME = mNotificationListRes.get(holder.getAdapterPosition()).getUsername();
//                   ((HomeScreen) mContext).replaceFragment(new ChatFriendFragment(), 1);
//               }
//            }
//        });
//
//        holder.mDeclineLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ACCEPT_REQUEST, AppConstants.PARAMS_ACCEPT_REQUEST, GlobalMethods.getUserID(mContext), mNotificationListRes.get(holder.getAdapterPosition()).getSender_id(), mContext.getString(R.string.two));
//                APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, mFrag);
//            }
//        });
//        holder.mFriendsImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AppConstants.OTHER_USER_ID = mNotificationListRes.get(holder.getAdapterPosition()).getUser_id();
//                ((HomeScreen) mContext).addFragment(new UserDetailsFragment());
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mNotificationListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.friend_name_txt)
        TextView mFriendsTxt;

        @BindView(R.id.address_txt)
        TextView mAddressTxt;

        @BindView(R.id.friend_profile_img)
        ImageView mFriendsImg;

        @BindView(R.id.chat_lay)
        LinearLayout mChatLay;

        @BindView(R.id.accept_lay)
        LinearLayout mAcceptLay;

        @BindView(R.id.decline_lay)
        LinearLayout mDeclineLay;


        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

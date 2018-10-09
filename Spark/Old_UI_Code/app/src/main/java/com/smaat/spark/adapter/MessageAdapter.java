package com.smaat.spark.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.spark.R;
import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ChatReceiveEntity> mChatMsgListRes;


    public MessageAdapter(Context context, ArrayList<ChatReceiveEntity> chatListRes) {
        this.mContext = context;
        this.mChatMsgListRes = chatListRes;
    }

    @Override
    public int getCount() {
        return mChatMsgListRes.size();
    }

    @Override
    public Object getItem(int pos) {
        return mChatMsgListRes.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adap_message_view, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        ChatReceiveEntity msgListRes = mChatMsgListRes.get(position);

        holder.mUserStatusImg.setVisibility(msgListRes.getFriend_online_status().equals(AppConstants.SUCCESS_CODE)?View.VISIBLE:View.GONE);

        if (msgListRes.getFriendimage().isEmpty()) {
            holder.mFriendProfileImg.setImageResource(R.drawable.default_user_img);
        } else {
            try {
                Glide.with(mContext)
                        .load(msgListRes.getFriendimage())
                        .into(holder.mFriendProfileImg);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, e.toString());
                holder.mFriendProfileImg.setImageResource(R.drawable.default_user_img);
            }
        }

        holder.mFriendNameTxt.setText(msgListRes.getFriendname());
        holder.mDateTxt.setText(msgListRes.getMessage().trim());
        String date_time = GlobalMethods.convertLocalUTCtime(mContext,
                msgListRes.getDatetime());
        //noinspection deprecation
        Date date2 = new Date(date_time);
        holder.mDateTxt.setText(GlobalMethods.getTimeDifference(mContext, new Date(), date2));
        holder.mMsgTxt.setText(GlobalMethods.getMsgDecoder(msgListRes.getMessage().trim()));

        holder.mFriendProfileImg.setTag(position);
        holder.mFriendProfileImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int pos = (int) view.getTag();
                DialogManager.getInstance().showOriginalImgPopup(mContext, mChatMsgListRes.get(pos).getFriendimage());
                return true;
            }
        });
        return convertView;
    }


    class Holder {

        @BindView(R.id.user_status_img)
        ImageView mUserStatusImg;

        @BindView(R.id.friend_profile_img)
        ImageView mFriendProfileImg;

        @BindView(R.id.friend_name_txt)
        TextView mFriendNameTxt;

        @BindView(R.id.date_txt)
        TextView mDateTxt;

        @BindView(R.id.msg_txt)
        TextView mMsgTxt;

        Holder(View itemView) {

//            mUserStatusImg = (ImageView) itemView.findViewById(R.id.user_status_img);
//            mFriendProfileImg = (ImageView) itemView.findViewById(R.id.friend_profile_img);
//            mFriendNameTxt = (TextView) itemView.findViewById(R.id.friend_name_txt);
//            mDateTxt = (TextView) itemView.findViewById(R.id.date_txt);
//            mMsgTxt = (TextView) itemView.findViewById(R.id.msg_txt);


            ButterKnife.bind(this, itemView);
        }
    }
}

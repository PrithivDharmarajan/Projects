package com.fautus.fautusapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ChatEntity;
import com.fautus.fautusapp.utils.AppConstants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private Context mContext;
    private ArrayList<ChatEntity> mChatListRes;


    public ChatAdapter(Context context, ArrayList<ChatEntity> chatReceiveEntityRes) {
        mContext = context;
        mChatListRes = chatReceiveEntityRes;

    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_chat_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        boolean isAmSenderBool = mChatListRes.get(position).getSender().equalsIgnoreCase(AppConstants.SUCCESS_CODE);

        holder.mLeftLay.setVisibility(isAmSenderBool ? View.GONE : View.VISIBLE);
        holder.mRightLay.setVisibility(isAmSenderBool ? View.VISIBLE : View.GONE);
        holder.mTypingStatusLay.setVisibility(position == mChatListRes.size() - 1 && mChatListRes.get(position).isUserTyping() ? View.VISIBLE : View.GONE);
        holder.mLeftMsgTxt.setText(mChatListRes.get(position).getMsg());
        holder.mRightMsgTxt.setText(mChatListRes.get(position).getMsg());

        /*Typing animation*/

        if (position == mChatListRes.size() - 1 && holder.mTypingStatusLay.getVisibility() == View.VISIBLE) {
            holder.mTypingIndicatorView.smoothToHide();
            holder.mTypingIndicatorView.smoothToShow();
        } else if (position == mChatListRes.size() - 1) {
            holder.mTypingIndicatorView.smoothToHide();
        }
    }

    @Override
    public int getItemCount() {
        return mChatListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.left_lay)
        RelativeLayout mLeftLay;

        @BindView(R.id.right_lay)
        RelativeLayout mRightLay;

        @BindView(R.id.typing_status_lay)
        RelativeLayout mTypingStatusLay;

        @BindView(R.id.indicator_view)
        AVLoadingIndicatorView mTypingIndicatorView;

        @BindView(R.id.left_msg_txt)
        TextView mLeftMsgTxt;

        @BindView(R.id.right_msg_txt)
        TextView mRightMsgTxt;


        Holder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }

}

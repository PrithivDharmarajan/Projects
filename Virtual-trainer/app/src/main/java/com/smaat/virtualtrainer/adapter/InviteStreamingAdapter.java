package com.smaat.virtualtrainer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.entity.InviteStreamingEntityRes;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.utils.AppConstants;

import java.util.ArrayList;


public class InviteStreamingAdapter extends RecyclerView.Adapter<InviteStreamingAdapter.Holder> {

    private ArrayList<InviteStreamingEntityRes> mUserStrArrList;
    private Context mContext;

    public InviteStreamingAdapter(Context context, ArrayList<InviteStreamingEntityRes> userList) {
        this.mContext = context;
        this.mUserStrArrList = userList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.adapter_invite_stream, parent, false);
        return new Holder(rootview);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        holder.mMsgTxt.setText(mUserStrArrList.get(position).getStream_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIRequestHandler.getInstance().joinStreamingAPICall(mUserStrArrList.get(holder.getAdapterPosition())
                        .getStream_id(), mUserStrArrList.get(holder.getAdapterPosition()).getStream().get(0).getStatus(), (BaseActivity) mContext);

            }
        });

        holder.mStreamJoinBtn.setVisibility((mUserStrArrList.get(position).getStream().get(0).getStatus().
                equalsIgnoreCase(AppConstants.FAILURE_CODE) ? View.VISIBLE : View.INVISIBLE));
        holder.mStreamLiveBtn.setVisibility((mUserStrArrList.get(position).getStream().get(0).getStatus().
                equalsIgnoreCase(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.INVISIBLE));
    }

    @Override
    public int getItemCount() {
        return mUserStrArrList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        RelativeLayout mBtnLay;
        TextView mMsgTxt;
        Button mStreamJoinBtn, mStreamLiveBtn;

        Holder(View itemView) {
            super(itemView);
            mBtnLay = (RelativeLayout) itemView.findViewById(R.id.btn_lay);
            mMsgTxt = (TextView) itemView.findViewById(R.id.msg_txt);
            mStreamJoinBtn = (Button) itemView.findViewById(R.id.stream_join_btn);
            mStreamLiveBtn = (Button) itemView.findViewById(R.id.stream_live_btn);
        }
    }
}

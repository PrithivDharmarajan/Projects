package com.calix.calixgigamanage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.AlertEntity;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DateUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.Holder> {

    private Context mContext;
    private ArrayList<AlertEntity> mDataEntryRes;

    public AlertAdapter(ArrayList<AlertEntity> dataEntryRes, Context context) {
        mContext = context;
        mDataEntryRes = dataEntryRes;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adap_alert, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        AlertEntity dataEntryRes = mDataEntryRes.get(position);
        // holder.mIssueTxt.setBackground(mContext.getResources().getDrawable(dataEntryRes.getRead()? R.drawable.box_textview_green:R.drawable.box_textview_shift));

        holder.mIssueTxt.setText(dataEntryRes.getText());
        holder.mDateTxt.setText(DateUtil.getCustomDateAndTimeFormat(dataEntryRes.getCreated(), AppConstants.CUSTOM_DATE_TIME_FORMAT));
        holder.itemView.setBackgroundColor(dataEntryRes.getRead() ? ContextCompat.getColor(mContext, R.color.white) : ContextCompat.getColor(mContext, R.color.read_gray));
//        holder.mIssueStatusImg.setImageResource(dataEntryRes.getRead() ? R.drawable.fixed : R.drawable.issue);
        holder.mFixItDoneBtn.setVisibility(dataEntryRes.getRead() ? View.GONE : View.VISIBLE);

        holder.mFixItDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstants.ALERT_NOTIFY_ID = mDataEntryRes.get(holder.getAdapterPosition()).getNotifId();
                APIRequestHandler.getInstance().notificationReadAPICall(AppConstants.ALERT_NOTIFY_ID, (BaseActivity) mContext);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataEntryRes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {


        @BindView(R.id.issue_status_img)
        ImageView mIssueStatusImg;

        @BindView(R.id.issue_txt)
        TextView mIssueTxt;

        @BindView(R.id.date_txt)
        TextView mDateTxt;

        @BindView(R.id.fix_it_done_btn)
        Button mFixItDoneBtn;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

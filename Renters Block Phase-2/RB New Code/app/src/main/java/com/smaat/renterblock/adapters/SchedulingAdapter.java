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

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ScheduleListArray;
import com.smaat.renterblock.fragments.CreateScheduleFragment;
import com.smaat.renterblock.fragments.SchedulingFragment;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SchedulingAdapter extends RecyclerView.Adapter<SchedulingAdapter.ItemViewHolder> {

    private SchedulingFragment mContext;
    private ArrayList<ScheduleListArray> mScheduleListArray;
    private boolean mEditValue;

    public SchedulingAdapter(SchedulingFragment context, ArrayList<ScheduleListArray> mScheduleArray, boolean mBoolValue) {
        AppConstants.DELETE_SCHEDULE_ID = new ArrayList<>();
        mContext = context;
        this.mScheduleListArray = mScheduleArray;
        this.mEditValue = mBoolValue;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scheduling, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        if (mEditValue) {
            holder.mAlertsCheckLay.setVisibility(View.VISIBLE);
        } else {
            holder.mAlertsCheckLay.setVisibility(View.GONE);
            mScheduleListArray.get(position).setSelected(false);
        }

        holder.mDateTxt.setText(mScheduleListArray.get(position).getDate().replace("-", "/"));

        final String time = mScheduleListArray.get(position).getTime();

        if (mScheduleListArray.get(position).getIs_friends_schedule().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            holder.mAcceptTxt.setVisibility(View.VISIBLE);
            holder.mRejectTxt.setVisibility(View.VISIBLE);
        } else if (mScheduleListArray.get(position).getIs_my_schedule().equalsIgnoreCase(AppConstants.SUCCESS_CODE) && mScheduleListArray.get(position).getIs_Accepted_schedule().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            holder.mAcceptTxt.setVisibility(View.GONE);
            holder.mRejectTxt.setVisibility(View.GONE);
        } else if (mScheduleListArray.get(position).getIs_Accepted_schedule().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            holder.mAcceptTxt.setVisibility(View.GONE);
            holder.mRejectTxt.setVisibility(View.GONE);
        }


//        holder.mAcceptTxt.setVisibility(mScheduleListArray.size() >0 && mScheduleListArray.get(position).getIs_my_schedule().equalsIgnoreCase(AppConstants.SUCCESS_CODE)
//                && mScheduleListArray.get(position).getIs_Accepted_schedule().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? View.GONE : View.VISIBLE);
//
//       holder.mRejectTxt.setVisibility(mScheduleListArray.size() >0 && mScheduleListArray.get(position).getIs_my_schedule().equalsIgnoreCase(AppConstants.SUCCESS_CODE)
        //             && mScheduleListArray.get(position).getIs_Accepted_schedule().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? View.GONE : View.VISIBLE);

        holder.mCheckImg.setImageResource(mEditValue && mScheduleListArray.get(position).isSelected() ? R.drawable.tick_on : R.drawable.tick_off);

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final Date dateObj = sdf.parse(time);

            holder.mTimeTxt.setText(new SimpleDateFormat("hh:mm a").format(dateObj));
        } catch (final ParseException e) {
            e.printStackTrace();
        }



//        holder.mTimeTxt.setText();
        holder.mFriendsGrpNameTxt.setText(mScheduleListArray.get(position).getMeeting_subject());
        holder.mFriendsGrpAdminNameTxt.setText(mScheduleListArray.get(position).getVenue());

        holder.mRejectTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().acceptRejectMeeting(mScheduleListArray.get(position).getSchedule_id(), AppConstants.FAILURE_CODE, mContext);
            }
        });

        holder.mAcceptTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIRequestHandler.getInstance().acceptRejectMeeting(mScheduleListArray.get(position).getSchedule_id(), AppConstants.SUCCESS_CODE, mContext);

            }
        });

        holder.mAdapterScheduleLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditValue && !mScheduleListArray.get(holder.getAdapterPosition()).isSelected()) {
                    holder.mCheckImg.setImageResource(R.drawable.tick_on);
                    mScheduleListArray.get(holder.getAdapterPosition()).setSelected(true);
                    AppConstants.DELETE_SCHEDULE_ID.add(mScheduleListArray.get(holder.getAdapterPosition()).getSchedule_id());
                } else if (mEditValue && mScheduleListArray.get(holder.getAdapterPosition()).isSelected()) {
                    holder.mCheckImg.setImageResource(R.drawable.tick_off);
                    mScheduleListArray.get(holder.getAdapterPosition()).setSelected(false);
                    AppConstants.DELETE_SCHEDULE_ID.remove(mScheduleListArray.get(holder.getAdapterPosition()).getSchedule_id());
                } else if (!mEditValue) {
                    if(mScheduleListArray.get(holder.getAdapterPosition()).getIs_friends_schedule().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                        AppConstants.mSelectedPos = mScheduleListArray.get(holder.getAdapterPosition());
                        AppConstants.SCHEDULE_DATE = holder.mDateTxt.getText().toString();
                        AppConstants.SCHEDULE_TIME = holder.mTimeTxt.getText().toString();
                        AppConstants.EDIT = "EDIT";
                        ((HomeScreen) mContext.getActivity()).addFragment(new CreateScheduleFragment());
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mScheduleListArray.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date_txt)
        TextView mDateTxt;

        @BindView(R.id.time_txt)
        TextView mTimeTxt;

        @BindView(R.id.friends_group_name)
        TextView mFriendsGrpNameTxt;

        @BindView(R.id.friends_group_admin_name)
        TextView mFriendsGrpAdminNameTxt;

        @BindView(R.id.friend_check_status)
        TextView mFriendCheckStatusTxt;

        @BindView(R.id.accept_txt)
        TextView mAcceptTxt;

        @BindView(R.id.reject_txt)
        TextView mRejectTxt;

        @BindView(R.id.alerts_check_lay)
        LinearLayout mAlertsCheckLay;

        @BindView(R.id.adapter_schedule_lay)
        RelativeLayout mAdapterScheduleLay;

        @BindView(R.id.check_img)
        ImageView mCheckImg;

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }




    }
}

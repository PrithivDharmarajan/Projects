package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.FriendsRecentListArray;
import com.smaat.renterblock.fragments.ChatFragment;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;
import com.smaat.renterblock.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class RecentFriendsListAdapter extends RecyclerView.Adapter<RecentFriendsListAdapter.Holder> {

    private Context mContext;
    private ArrayList<FriendsRecentListArray> mRecentList;
    private BaseFragment mAPICallBackFrag;
    private String mUserIDStr;

    public RecentFriendsListAdapter(Context context, ArrayList<FriendsRecentListArray> mRecentListArray, BaseFragment baseFragment) {
        mContext = context;
        mRecentList = mRecentListArray;
        mAPICallBackFrag = baseFragment;
        mUserIDStr = PreferenceUtil.getUserID(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recent_friends_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (mRecentList.get(position).getUserdetails().size() > 0) {
            int userPos = (mRecentList.get(position).getUserdetails().size() == 2 && !mRecentList.get(position).getUserdetails().get(1).getUser_id().equals(mUserIDStr)) ? 0 : 1;

            if (mRecentList.get(position).getUser_id().equalsIgnoreCase(PreferenceUtil.getUserID(mContext))){
                holder.mMessageTxt.setText("You : "+mRecentList.get(position).getMessage());
            }
            else {
                holder.mMessageTxt.setText(mRecentList.get(position).getUser_name()+" : "+ mRecentList.get(position).getMessage());
            }


            holder.mDateTimeTxt.setText(TimeUtil.getTimeDifference(mRecentList.get(position).getSend_time()));

            holder.mNameTxt.setText(mRecentList.get(position).getUserdetails().get(userPos).getUser_name());
            if (mRecentList.get(position).getUserdetails().get(userPos).getUserimage().isEmpty()) {
                holder.mUserLogo.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(mContext)
                            .load(mRecentList.get(position).getUserdetails().get(userPos).getUserimage()).placeholder(R.drawable.profile_pic).into(holder.mUserLogo);
                } catch (Exception e) {
                    holder.mUserLogo.setImageResource(R.drawable.profile_pic);
                }
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();

                    AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(mRecentList.get(holder.getAdapterPosition()).getGroup_id());
                    AppConstants.CHAT_INPUT_ENTITY.setGroup_name(mRecentList.get(holder.getAdapterPosition()).getGroupname());
                    ((HomeScreen) mContext).addFragment(new ChatFragment());
                    //int userPos = (mRecentList.get(holder.getAdapterPosition()).getUserdetails().size() == 2 && mRecentList.get(holder.getAdapterPosition()).getUserdetails().get(2).getUser_id().equals(mUserIDStr)) ? 1 : 0;

                    //APIRequestHandler.getInstance().getChatID(mRecentList.get(holder.getAdapterPosition()).getUserdetails().get(userPos).getUser_id(), mRecentList.get(holder.getAdapterPosition()).getGroupname(), mAPICallBackFrag);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecentList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_txt)
        TextView mNameTxt;

        @BindView(R.id.message_txt)
        TextView mMessageTxt;

        @BindView(R.id.date_time_txt)
        TextView mDateTimeTxt;

        @BindView(R.id.user_logo)
        CircleImageView mUserLogo;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

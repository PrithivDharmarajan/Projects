package com.smaat.spark.adapter;

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
import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.entity.inputEntity.ChatSendReceiveInputEntity;
import com.smaat.spark.entity.inputEntity.LoginRegResetInputEntity;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;
import com.smaat.spark.fragment.UserDetailsFragment;
import com.smaat.spark.main.BaseFragment;
import com.smaat.spark.services.APIRequestHandler;
import com.smaat.spark.ui.HomeScreen;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;
import com.smaat.spark.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.Holder> {


    private ArrayList<UserDetailsEntity> mInviteListRes;
    private Context mContext;
    private BaseFragment mFrag;

    public InviteFriendsAdapter(Context context, BaseFragment frag, ArrayList<UserDetailsEntity> friendsList) {
        mContext = context;
        mFrag = frag;
        mInviteListRes = friendsList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adap_invite_friends_view, parent, false);
        return new Holder(rootView);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        UserDetailsEntity inviteListRes = mInviteListRes.get(position);

        holder.mFriendsTxt.setText(inviteListRes.getUsername());
        holder.mAddressTxt.setText(inviteListRes.getHide_location().equals(AppConstants.SUCCESS_CODE)?"":inviteListRes.getAddress().trim());

        if (inviteListRes.getMain_picture().isEmpty()) {
            holder.mFriendsImg.setImageResource(R.drawable.default_user_img);
        } else {
            try {
                Glide.with(mContext)
                        .load(inviteListRes.getMain_picture())
                        .into(holder.mFriendsImg);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, e.toString());
                holder.mFriendsImg.setImageResource(R.drawable.default_user_img);
            }
        }
        holder.mAddBtn.setText(inviteListRes.getInvite_btn_name());
        holder.mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.mAddBtn.getText().toString().equals(mContext.getString(R.string.add))) {
                    ChatSendReceiveInputEntity friendListInputEntityRes = new ChatSendReceiveInputEntity(AppConstants.API_ADD_FRIEND, AppConstants.PARAMS_ADD_FRIEND, GlobalMethods.getUserID(mContext), mInviteListRes.get(holder.getAdapterPosition()).getUser_id());
                    APIRequestHandler.getInstance().callAddFriendAPI(friendListInputEntityRes, mFrag);
                } else {
                    LoginRegResetInputEntity friendListInputEntityRes = new LoginRegResetInputEntity(AppConstants.API_INVITE_FRIEND, AppConstants.PARAMS_INVITE_FRIEND, GlobalMethods.getUserID(mContext), mInviteListRes.get(holder.getAdapterPosition()).getEmail_id());
                    APIRequestHandler.getInstance().callAddContactsFriendAPI(friendListInputEntityRes, mFrag);
                }
            }
        });

        holder.mFriendsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConstants.OTHER_USER_DETAILS = new Gson().toJson(mInviteListRes.get(holder.getAdapterPosition()), UserDetailsEntity.class);

                AppConstants.CHAT_USER_BACK=AppConstants.FAILURE_CODE;
                ((HomeScreen) mContext).replaceFragment(new UserDetailsFragment(),1);
            }
        });
        holder.mFriendsImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogManager.getInstance().showOriginalImgPopup(mContext, mInviteListRes.get(holder.getAdapterPosition()).getMain_picture());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInviteListRes.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.friend_name_txt)
        TextView mFriendsTxt;

        @BindView(R.id.address_txt)
        TextView mAddressTxt;

        @BindView(R.id.friend_profile_img)
        ImageView mFriendsImg;

        @BindView(R.id.add_btn)
        Button mAddBtn;

        Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}

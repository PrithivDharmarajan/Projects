package com.smaat.renterblock.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AcceptFriendEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.Holder> {

    Context mContext;
    ArrayList<AcceptFriendEntity> mFriendsList;
    private boolean isbool = true;

    public FriendsListAdapter(Context activity, ArrayList<AcceptFriendEntity> mFriendsListArray) {
        mContext = activity;
        mFriendsList = mFriendsListArray;

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_existed_friends_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        holder.mFriendsName.setText(mFriendsList.get(position).getFriends_details().get(0).getFirst_name());

        holder.mParentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isbool) {
                    isbool = false;
                    holder.mFriendsSelectionImg.setImageResource(R.drawable.tick_off);
                }else {
                    isbool = true;
                    holder.mFriendsSelectionImg.setImageResource(R.drawable.tick_on);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.friends_name)
        TextView mFriendsName;

        @BindView(R.id.friends_selection_img)
        ImageView mFriendsSelectionImg;

        @BindView(R.id.prop_type_lay)
        RelativeLayout mParentLay;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

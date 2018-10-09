package com.smaat.renterblock.sectionrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AcceptFriendEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


public class HeaderRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    SectionedRecyclerViewAdapter mSectionAdapter;

    ArrayList<AcceptFriendEntity> list = new ArrayList<>();

    public HeaderRecyclerViewSection(String title, ArrayList<AcceptFriendEntity> list, SectionedRecyclerViewAdapter mSectionAdapter) {
        super(R.layout.header_layout, R.layout.item_layout);
        this.title = title;
        this.list = list;
        this.mSectionAdapter = mSectionAdapter;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder iHolder = (ItemViewHolder) holder;
        iHolder.itemContent.setText(list.get(position).getFriends_details().get(0).getUser_name());
        if (list.get(position).getFriends_details().get(0).isSelect()) {
            iHolder.itemImgTick.setImageResource(R.drawable.tick_on);
        } else {
            iHolder.itemImgTick.setImageResource(R.drawable.tick_off);
        }
        iHolder.mInviteFriendLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(position).getFriends_details().get(0).isSelect()) {
                    list.get(position).getFriends_details().get(0).setSelect(false);
                    iHolder.itemImgTick.setImageResource(R.drawable.tick_off);

                } else {
                    list.get(position).getFriends_details().get(0).setSelect(true);
                    iHolder.itemImgTick.setImageResource(R.drawable.tick_on);
                }

                if (AppConstants.INVITE_FRIENDS_HASH_MAP.containsKey(list.get(position).getFriends_details().get(0).getUser_friend_id())) {
                    AppConstants.INVITE_FRIENDS_HASH_MAP.remove(list.get(position).getFriends_details().get(0).getUser_friend_id());
                } else {
                    AppConstants.INVITE_FRIENDS_HASH_MAP.put(list.get(position).getFriends_details().get(0).getUser_friend_id(),
                            list.get(position).getFriends_details());


                }


                mSectionAdapter.notifyDataSetChanged();


            }

        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.headerTitle.setText(title);
    }
}
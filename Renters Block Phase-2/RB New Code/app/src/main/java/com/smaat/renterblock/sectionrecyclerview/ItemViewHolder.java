package com.smaat.renterblock.sectionrecyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;

public class ItemViewHolder extends RecyclerView.ViewHolder{
    public TextView itemContent;
    public ImageView itemImgTick;
    public LinearLayout mInviteFriendLay;
    public ItemViewHolder(View itemView) {
        super(itemView);
        itemContent = (TextView)itemView.findViewById(R.id.item_content);
        itemImgTick = (ImageView)itemView.findViewById(R.id.tick_img);
        mInviteFriendLay =(LinearLayout)itemView.findViewById(R.id.friend_invite_lay);

    }
}
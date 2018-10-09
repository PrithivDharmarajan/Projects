package com.smaat.virtualtrainer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.entity.UserDetailsEntityRes;

import java.util.ArrayList;

import static com.smaat.virtualtrainer.ui.AddJoinessScreen.sIdUDEArrList;


public class AddJoinessAdapter extends RecyclerView.Adapter<AddJoinessAdapter.Holder> {

    private ArrayList<UserDetailsEntityRes> mInviteList;
    private Context mContext;


    public AddJoinessAdapter(Context context, ArrayList<UserDetailsEntityRes> mList) {
        this.mInviteList = mList;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootview = LayoutInflater.from(mContext).inflate(R.layout.adapter_add_joiness, parent, false);
//        ButterKnife.bind(mContext, rootview);
        return new Holder(rootview);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        holder.mUserMailIdTxt.setText(mInviteList.get(position).getEmail_id());
        holder.mEmailAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInviteList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                sIdUDEArrList.clear();
                sIdUDEArrList.addAll(mInviteList);
                notifyDataSetChanged();
            }
        });
    }


     class Holder extends RecyclerView.ViewHolder {
//        @BindView(R.id.user_mail_id_txt)
        TextView mUserMailIdTxt;

//        @BindView(R.id.email_add_img)
        ImageView mEmailAddImg;

         Holder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
            mUserMailIdTxt = (TextView) itemView.findViewById(R.id.user_mail_id_txt);
            mEmailAddImg = (ImageView) itemView.findViewById(R.id.email_add_img);
        }
    }

    @Override
    public int getItemCount() {
        return mInviteList.size();
    }
}

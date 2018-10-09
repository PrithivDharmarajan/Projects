package com.smaat.renterblock.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.NotificationEntity;
import com.smaat.renterblock.fragments.NotificationFragment;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.services.APIRequestHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {

    private Context mContext;
    private ArrayList<NotificationEntity> mNotificationResultList = new ArrayList<>();
    private NotificationFragment notificationFragment;


    public NotificationAdapter(Context context, ArrayList<NotificationEntity> mNotificationList, NotificationFragment notificationFragment) {
        mNotificationResultList = mNotificationList;
        mContext = context;
        this.notificationFragment = notificationFragment;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adap_notification_screen, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder mHolder, int position) {
        final NotificationEntity mNotificationEntity = mNotificationResultList.get(position);

        mHolder.mNotificationTypeTxt.setText(mNotificationEntity.type_of_notification);
        mHolder.mNotificationiMessageTxt.setText(mNotificationEntity.message);


        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationFragment.notifyDeleteApi(mNotificationEntity);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotificationResultList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_type)
        TextView mNotificationTypeTxt;

        @BindView(R.id.notification_message)
        TextView mNotificationiMessageTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}


package com.smaat.renterblock.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.NotificationEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.NotificationActivity;
import com.smaat.renterblock.util.TypefaceSingleton;

public class NotificationAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<NotificationEntity> mNotificationEntityList;

	public NotificationAdapter(Context context,
			ArrayList<NotificationEntity> notificationEntity) {
		mContext = context;
		mNotificationEntityList = notificationEntity;
	}

	class Holder {
		private TextView mNotificationType, mNotificationMessage;
		private RelativeLayout mNotificationlay;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		holder = new Holder();
		if (mNotificationEntityList.size() != 0) {

			LayoutInflater mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mLayoutInflater.inflate(
					R.layout.adapter_notification, null);
			ViewGroup root = (ViewGroup) convertView
					.findViewById(R.id.parent_layout);
			Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
					mContext);
			Typeface mTypefaceBold = TypefaceSingleton.getInstance()
					.getHelveticaBold(mContext);
			((BaseActivity) mContext).setFont(root, mTypefaceBold);

			holder.mNotificationType = (TextView) convertView
					.findViewById(R.id.notification_type);
			holder.mNotificationMessage = (TextView) convertView
					.findViewById(R.id.notification_message);
			holder.mNotificationlay = (RelativeLayout) convertView
					.findViewById(R.id.main_lay);
			holder.mNotificationlay.setTag(position);
			holder.mNotificationlay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					String notification_id = mNotificationEntityList.get(pos).getNotification_id();
					NotificationActivity.callDeleteNotification(notification_id, pos);
				}
			});
			

			NotificationEntity mNotificationEntity = mNotificationEntityList
					.get(position);
			if(mNotificationEntity.getType_of_notification().equalsIgnoreCase("hotleadchat")) {
				holder.mNotificationType.setText("hot lead chat");
			} else {
				holder.mNotificationType.setText(mNotificationEntity
						.getType_of_notification());				
			}
			holder.mNotificationMessage.setText(mNotificationEntity
					.getMessage());
			holder.mNotificationMessage.setTypeface(mTypeface);
		}

		return convertView;
	}

	@Override
	public int getCount() {

		return mNotificationEntityList.size();
	}

	@Override
	public Object getItem(int pos) {

		return mNotificationEntityList.get(pos);
	}

	@Override
	public long getItemId(int pos) {

		return pos;
	}

}

package com.smaat.renterblock.friends.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.AcceptFriendEntity;
import com.smaat.renterblock.friends.entity.FriendDetailsEntity;
import com.smaat.renterblock.friends.ui.FriendsMainScreen;
import com.smaat.renterblock.scheduling.ScheduleMeetingActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FriendsInviteAdapter extends BaseAdapter implements
		DialogMangerCallback {

	private Context mContext;
	private Holder holder = null;
	private ArrayList<Button> mNameItemBtn = new ArrayList<Button>();
	ArrayList<AcceptFriendEntity> mAcceptedFriendsList;
	ArrayList<String> muser_name_arr = new ArrayList<String>();
	ArrayList<String> muser_id_arr = new ArrayList<String>();
	Typeface helvetica_normal, helvetica_bold, helvetica_light;

	public FriendsInviteAdapter(Context context,
			ArrayList<AcceptFriendEntity> acceptedFriendsList,
			ArrayList<String> mUserName, ArrayList<String> mUserId) {
		mContext = context;
		mAcceptedFriendsList = acceptedFriendsList;
		muser_name_arr = mUserName;
		muser_id_arr = mUserId;

		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(
				context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	class Holder {

		private TextView mName;
		private RelativeLayout mNameLay;
		private Button mCheckName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = new Holder();

		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(R.layout.adapter_prop_type_list,
				null);
		if (mAcceptedFriendsList.size() != 0) {

			final ArrayList<FriendDetailsEntity> mFriendDetailsEntity = mAcceptedFriendsList
					.get(position).getFriends_details();

			holder.mName = (TextView) convertView
					.findViewById(R.id.prop_type_text);
			holder.mName.setTypeface(helvetica_normal);

			holder.mCheckName = (Button) convertView
					.findViewById(R.id.prop_type_btn);
			holder.mNameLay = (RelativeLayout) convertView
					.findViewById(R.id.prop_type_lay);

			mNameItemBtn.add(holder.mCheckName);
			holder.mNameLay.setTag(position);

			holder.mNameLay.setOnClickListener(new OnClickListener() {
				private boolean isFirst = true;

				@Override
				public void onClick(View v) {
					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					if (isFirst) {
						if (mAcceptedFriendsList.get(pos).getFriends_details()
								.get(0).getEnhanced_profile().equals("1")) {
							mNameItemBtn.get(pos).setBackgroundResource(
									R.drawable.tick_on);

							ScheduleMeetingActivity.mUserID
									.add(mFriendDetailsEntity.get(0)
											.getUser_friend_id());
							ScheduleMeetingActivity.mUserName
									.add(mFriendDetailsEntity.get(0)
											.getUser_name());

							if (FriendsMainScreen.mUserID != null
									&& FriendsMainScreen.mUserNames != null) {
								FriendsMainScreen.mUserID
										.add(mFriendDetailsEntity.get(0)
												.getUser_friend_id());
								FriendsMainScreen.mUserNames
										.add(mFriendDetailsEntity.get(0)
												.getUser_name());
							}
							isFirst = false;
						} else {
							if (AppConstants.from_chat_dialog
									.equalsIgnoreCase("true")) {
								if (mAcceptedFriendsList.get(pos)
										.getFriends_details().get(0)
										.getEnhanced_profile().equals("1")) {
									mNameItemBtn.get(pos)
											.setBackgroundResource(
													R.drawable.tick_on);

									ScheduleMeetingActivity.mUserID
											.add(mFriendDetailsEntity.get(0)
													.getUser_friend_id());
									ScheduleMeetingActivity.mUserName
											.add(mFriendDetailsEntity.get(0)
													.getUser_name());

									if (FriendsMainScreen.mUserID != null
											&& FriendsMainScreen.mUserNames != null) {
										FriendsMainScreen.mUserID
												.add(mFriendDetailsEntity
														.get(0)
														.getUser_friend_id());
										FriendsMainScreen.mUserNames
												.add(mFriendDetailsEntity
														.get(0).getUser_name());
									}
									isFirst = false;
								}
							} else {
								DialogManager
										.showCustomAlertDialog(mContext,
												FriendsInviteAdapter.this,
												"You cannot make a Video/Voice call with this user.");
							}
						}
					} else {
						mNameItemBtn.get(pos).setBackgroundResource(
								R.drawable.tick_off);
						ScheduleMeetingActivity.mUserID
								.remove(mFriendDetailsEntity.get(0)
										.getUser_friend_id());
						ScheduleMeetingActivity.mUserName
								.remove(mFriendDetailsEntity.get(0)
										.getUser_name());

						if (FriendsMainScreen.mUserID != null
								&& FriendsMainScreen.mUserNames != null) {
							FriendsMainScreen.mUserID
									.remove(mFriendDetailsEntity.get(0)
											.getUser_friend_id());
							FriendsMainScreen.mUserNames
									.remove(mFriendDetailsEntity.get(0)
											.getUser_name());
						}
						isFirst = true;
					}
				}
			});
			if (mFriendDetailsEntity.size() != 0) {
				holder.mName
						.setText(mFriendDetailsEntity.get(0).getUser_name());
			}
		}
		return convertView;
	}

	@Override
	public int getCount() {

		return mAcceptedFriendsList.size();
	}

	@Override
	public Object getItem(int position) {

		return mAcceptedFriendsList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub

	}

}

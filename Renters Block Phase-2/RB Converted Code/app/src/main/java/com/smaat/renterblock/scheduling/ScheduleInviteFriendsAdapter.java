package com.smaat.renterblock.scheduling;

import java.util.ArrayList;

import android.content.Context;
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
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;

public class ScheduleInviteFriendsAdapter extends BaseAdapter implements
		DialogMangerCallback {

	private Context mContext;
	private Holder holder = null;
	// private ArrayList<Button> mNameItemBtn = new ArrayList<Button>();
	ArrayList<AcceptFriendEntity> mAcceptedFriendsList;
	ArrayList<String> muser_name_arr = new ArrayList<String>();
	ArrayList<String> muser_id_arr = new ArrayList<String>();
	
	public ScheduleInviteFriendsAdapter(Context context,
			ArrayList<AcceptFriendEntity> acceptedFriendsList,
			ArrayList<String> mUserName, ArrayList<String> mUserId) {
		mContext = context;
		mAcceptedFriendsList = acceptedFriendsList;
		muser_name_arr = mUserName;
		muser_id_arr = mUserId;
	}

	class Holder {

		private TextView mName;
		private RelativeLayout mNameLay;
		private Button mCheckName;
	}

	public class TagClass {
		View view;
		boolean isCheck;
		int position;
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

			holder.mCheckName = (Button) convertView
					.findViewById(R.id.prop_type_btn);
			holder.mNameLay = (RelativeLayout) convertView
					.findViewById(R.id.prop_type_lay);

			TagClass tag = new TagClass();
			tag.view = holder.mCheckName;
			tag.isCheck = false;
			tag.position = position;

			holder.mNameLay.setTag(tag);

			holder.mNameLay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TagClass tag = (TagClass) v.getTag();
					int pos = tag.position;
					if (!tag.isCheck) {
						tag.view.setBackgroundResource(R.drawable.tick_on);
						if (mAcceptedFriendsList.get(pos).getFriends_details()
								.get(0).getEnhanced_profile() != null
								&& mAcceptedFriendsList.get(pos)
										.getFriends_details().get(0)
										.getEnhanced_profile().equals("1")) {
							ScheduleMeetingActivity.mUserID
									.add(mFriendDetailsEntity.get(0)
											.getUser_friend_id());
							ScheduleMeetingActivity.mUserName
									.add(mFriendDetailsEntity.get(0)
											.getUser_name());

							for (int i = 0; i < ScheduleMeetingActivity.more_sch_local_arr
									.size(); i++) {

								if (ScheduleMeetingActivity.more_sch_local_arr
										.get(i)
										.getFriends_details()
										.get(0)
										.getUser_name()
										.equalsIgnoreCase(
												mAcceptedFriendsList.get(pos)
														.getFriends_details()
														.get(0).getUser_name())) {
									ScheduleMeetingActivity.more_sch_local_arr
											.get(i).getFriends_details().get(0)
											.setIsRemove("1");
									break;
								}
							}

							// if ((FriendsMainScreen.mUserID != null &&
							// FriendsMainScreen.mUserID
							// .size() != 0)
							// && (FriendsMainScreen.mUserNames != null &&
							// FriendsMainScreen.mUserNames
							// .size() != 0)) {
							// FriendsMainScreen.mUserID
							// .add(mFriendDetailsEntity.get(0)
							// .getUser_friend_id());
							// FriendsMainScreen.mUserNames
							// .add(mFriendDetailsEntity.get(0)
							// .getUser_name());
							// }
						} else {
							DialogManager
									.showCustomAlertDialog(mContext,
											ScheduleInviteFriendsAdapter.this,
											"You cannot create a meeting with this user.");
						}
						tag.isCheck = true;
					} else {
						tag.view.setBackgroundResource(R.drawable.tick_off);
						ScheduleMeetingActivity.mUserID
								.remove(mFriendDetailsEntity.get(0)
										.getUser_friend_id());
						ScheduleMeetingActivity.mUserName
								.remove(mFriendDetailsEntity.get(0)
										.getUser_name());

						ScheduleMeetingActivity.more_sch_local_arr.get(pos)
								.getFriends_details().get(0).setIsRemove("0");

						// if ((FriendsMainScreen.mUserID != null &&
						// FriendsMainScreen.mUserID
						// .size() != 0)
						// && (FriendsMainScreen.mUserNames != null &&
						// FriendsMainScreen.mUserNames
						// .size() != 0)) {
						// FriendsMainScreen.mUserID
						// .remove(mFriendDetailsEntity.get(0)
						// .getUser_friend_id());
						// FriendsMainScreen.mUserNames
						// .remove(mFriendDetailsEntity.get(0)
						// .getUser_name());
						// }
						tag.isCheck = false;
					}

				}
			});
			holder.mName.setText(mFriendDetailsEntity.get(0).getUser_name());
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

	}

	@Override
	public void onOkclick() {

	}

}

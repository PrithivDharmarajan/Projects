package com.smaat.renterblock.scheduling;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.AcceptFriendEntity;
import com.smaat.renterblock.friends.entity.FriendsDetailsEntity;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ProfileScreen.Utility;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class ScheduleInviteFriendsActivity extends BaseActivity implements
		OnClickListener {

	private TextView mHeaderText;
	private Context mContext;
	private ListView mInviteFriendsList, mUserInScheduleList;
	private ScheduleInviteFriendsAdapter mInviteFriendsAdapter;
	private ScheduleUserInScheduleAdapter mScheduleUserinAdapter;
	private ArrayList<FriendsDetailsEntity> mFriendsResultList;
	private ArrayList<AcceptFriendEntity> mAcceptedFriendsList;

	private ArrayList<String> mUserName = new ArrayList<String>();
	private ArrayList<String> mUserId = new ArrayList<String>();

	ArrayList<String> user_names_i = new ArrayList<String>();

	private TextView invite_text;

	private ArrayList<String> more_sch_local_str = new ArrayList<String>();
	private ArrayList<String> sch_local_str = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_schedule_invite_friends);

		ViewGroup mRootView = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(mRootView, mTypefaceBold);
		mContext = ScheduleInviteFriendsActivity.this;
		UserID = GlobalMethods.getUserID(this);
		setupUI(mRootView);
		initComponents();
		getFriendsList();
	}

	private void initComponents() {

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mUserName = extras.getStringArrayList("user_name_arr");
			mUserId = extras.getStringArrayList("user_id_arr");
		}

		invite_text = (TextView) findViewById(R.id.invite_text);

		mHeaderText = (TextView) findViewById(R.id.header_txt);
		mHeaderText.setText(getString(R.string.invite_friends));
		mInviteFriendsList = (ListView) findViewById(R.id.schedule_friends_list);
		mUserInScheduleList = (ListView) findViewById(R.id.user_in_schedule_friends_list);
	}

	private void setFriendsAdapter() {

		mInviteFriendsAdapter = new ScheduleInviteFriendsAdapter(this,
				ScheduleMeetingActivity.more_sch_local_arr, mUserName, mUserId);
		mInviteFriendsList.setAdapter(mInviteFriendsAdapter);
		mInviteFriendsAdapter.notifyDataSetChanged();

		Utility.setListViewHeightBasedOnChildren(mInviteFriendsList);

		if (ScheduleMeetingActivity.from_bundle) {
			invite_text.setText("Update");
			if (ScheduleMeetingActivity.sch_local_arr.size() != 0
					&& ScheduleMeetingActivity.more_sch_local_arr.size() != 0) {

				for (int i = 0; i < ScheduleMeetingActivity.sch_local_arr
						.size(); i++) {
					for (int j = 0; j < ScheduleMeetingActivity.more_sch_local_arr
							.size(); j++) {
						if (ScheduleMeetingActivity.more_sch_local_arr
								.get(j)
								.getFriends_details()
								.get(0)
								.getUser_name()
								.equalsIgnoreCase(
										ScheduleMeetingActivity.sch_local_arr
												.get(i).getFriends_details()
												.get(0).getUser_name())) {
							ScheduleMeetingActivity.sch_local_ent = new AcceptFriendEntity();
							ScheduleMeetingActivity.sch_local_ent = ScheduleMeetingActivity.more_sch_local_arr
									.get(j);
							ScheduleMeetingActivity.more_sch_local_arr
									.remove(ScheduleMeetingActivity.sch_local_ent);
						}
					}
				}
			}
		} else {
			invite_text.setText("Invite");
		}
		more_sch_local_str.clear();
		for (int i = 0; i < ScheduleMeetingActivity.more_sch_local_arr.size(); i++) {
			if (ScheduleMeetingActivity.more_sch_local_arr.get(i)
					.getFriends_details().get(0).getIsRemove() != null
					&& ScheduleMeetingActivity.more_sch_local_arr.get(i)
							.getFriends_details().get(0).getIsRemove()
							.equalsIgnoreCase("1")) {
				more_sch_local_str.add(i + "");
			}
		}

		for (int idx = more_sch_local_str.size() - 1; idx >= 0; idx--) {
			try {
				ScheduleMeetingActivity.sch_local_ent = new AcceptFriendEntity();
				ScheduleMeetingActivity.sch_local_ent = ScheduleMeetingActivity.more_sch_local_arr
						.get(idx);
				ScheduleMeetingActivity.sch_local_arr
						.add(ScheduleMeetingActivity.sch_local_ent);
				ScheduleMeetingActivity.more_sch_local_arr
						.remove(ScheduleMeetingActivity.sch_local_ent);
				// mProperty.remove(Integer.parseInt(more_sch_local_str.get(idx)));
			} catch (Exception e) {
				break;
			}
		}
		sch_local_str.clear();
		for (int j = 0; j < ScheduleMeetingActivity.sch_local_arr.size(); j++) {
			if (ScheduleMeetingActivity.sch_local_arr.get(j)
					.getFriends_details().get(0).getIsRemove() != null
					&& ScheduleMeetingActivity.sch_local_arr.get(j)
							.getFriends_details().get(0).getIsRemove()
							.equalsIgnoreCase("0")) {
				sch_local_str.add(j + "");

			}
		}

		for (int idxs = sch_local_str.size() - 1; idxs >= 0; idxs--) {
			try {
				ScheduleMeetingActivity.more_sch_local_ent = new AcceptFriendEntity();
				ScheduleMeetingActivity.more_sch_local_ent = ScheduleMeetingActivity.sch_local_arr
						.get(idxs);
				ScheduleMeetingActivity.more_sch_local_arr
						.add(ScheduleMeetingActivity.more_sch_local_ent);
				ScheduleMeetingActivity.sch_local_arr
						.remove(ScheduleMeetingActivity.more_sch_local_ent);
				// mProperty.remove(Integer.parseInt(sch_local_str.get(idxs)));
			} catch (Exception e) {
				break;
			}
		}

		mInviteFriendsAdapter.notifyDataSetChanged();
		Utility.setListViewHeightBasedOnChildren(mInviteFriendsList);

		mScheduleUserinAdapter = new ScheduleUserInScheduleAdapter(this,
				ScheduleMeetingActivity.sch_local_arr, mUserName, mUserId);
		mUserInScheduleList.setAdapter(mScheduleUserinAdapter);
		mScheduleUserinAdapter.notifyDataSetChanged();

		Utility.setListViewHeightBasedOnChildren(mUserInScheduleList);
	}

	// ScheduleMeetingActivity.mUserID
	// .add(mFriendDetailsEntity.get(0)
	// .getUser_friend_id());
	// ScheduleMeetingActivity.mUserName
	// .add(mFriendDetailsEntity.get(0)
	// .getUser_name());

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;
		case R.id.invite_lay:
			ScheduleMeetingActivity.mUserID.clear();
			ScheduleMeetingActivity.mUserName.clear();
			if (ScheduleMeetingActivity.sch_local_arr.size() == 0) {

				for (int i = 0; i < ScheduleMeetingActivity.more_sch_local_arr
						.size(); i++) {
					if (ScheduleMeetingActivity.more_sch_local_arr.get(i)
							.getFriends_details().get(0).getIsRemove() != null
							&& ScheduleMeetingActivity.more_sch_local_arr
									.get(i).getFriends_details().get(0)
									.getIsRemove().equalsIgnoreCase("1")) {
						user_names_i
								.add(ScheduleMeetingActivity.more_sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_name());
						ScheduleMeetingActivity.mUserID
								.add(ScheduleMeetingActivity.more_sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_friend_id());
						ScheduleMeetingActivity.mUserName
								.add(ScheduleMeetingActivity.more_sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_name());
					}
				}
				String use_nm = user_names_i.toString().replace("[", "")
						.replace("]", "");
				if (user_names_i.size() != 0) {
					ScheduleMeetingActivity.mAddFriendsText.setText(use_nm);
					AppConstants.isAPI = true;
					finish();
					overridePendingTransition(R.anim.slide_out_right,
							R.anim.slide_in_left);
				} else {
					ScheduleMeetingActivity.mAddFriendsText
							.setText("Add Friends");
					DialogManager.showCustomAlertDialog(this, this,
							getString(R.string.please_select_friends));
				}

			} else {
				for (int i = 0; i < ScheduleMeetingActivity.sch_local_arr
						.size(); i++) {
					if (ScheduleMeetingActivity.sch_local_arr.get(i)
							.getFriends_details().get(0).getIsRemove() != null
							&& ScheduleMeetingActivity.sch_local_arr.get(i)
									.getFriends_details().get(0).getIsRemove()
									.equalsIgnoreCase("1")) {
						user_names_i.add(ScheduleMeetingActivity.sch_local_arr
								.get(i).getFriends_details().get(0)
								.getUser_name());
						ScheduleMeetingActivity.mUserID
								.add(ScheduleMeetingActivity.sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_friend_id());
						ScheduleMeetingActivity.mUserName
								.add(ScheduleMeetingActivity.sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_name());
					}
				}
				for (int i = 0; i < ScheduleMeetingActivity.more_sch_local_arr
						.size(); i++) {
					if (ScheduleMeetingActivity.more_sch_local_arr.get(i)
							.getFriends_details().get(0).getIsRemove() != null
							&& ScheduleMeetingActivity.more_sch_local_arr
									.get(i).getFriends_details().get(0)
									.getIsRemove().equalsIgnoreCase("1")) {
						user_names_i
								.add(ScheduleMeetingActivity.more_sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_name());
						ScheduleMeetingActivity.mUserID
								.add(ScheduleMeetingActivity.more_sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_friend_id());
						ScheduleMeetingActivity.mUserName
								.add(ScheduleMeetingActivity.more_sch_local_arr
										.get(i).getFriends_details().get(0)
										.getUser_name());
					}
				}
				String use_nm = user_names_i.toString().replace("[", "")
						.replace("]", "");
				if (user_names_i.size() != 0) {
					ScheduleMeetingActivity.mAddFriendsText.setText(use_nm);
				} else {
					ScheduleMeetingActivity.mAddFriendsText
							.setText("Add Friends");
				}
				AppConstants.isAPI = true;
				finish();
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			}
			break;

		}
	}

	private void getFriendsList() {

		String Url = AppConstants.BASE_URL + "friend";
		GsonTransformer t = new GsonTransformer();

		HashMap<String, Object> params = new HashMap<String, Object>();
		// UserID = "3";
		params.put("user_id", UserID);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {
								super.callback(url, json, status);

								if (json != null) {
									FriendsResponse mResponse = new Gson()
											.fromJson(json.toString(),
													FriendsResponse.class);
									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mFriendsResultList = mResponse
												.getResult();
										mAcceptedFriendsList = mFriendsResultList
												.get(0).getAccept_friend();
										if (!SchedulingActivity.isAdded) {
											if (!SchedulingActivity.not_from_add) {
												ScheduleMeetingActivity.sch_local_arr
														.clear();
											}
											ScheduleMeetingActivity.more_sch_local_arr
													.clear();
											ScheduleMeetingActivity.more_sch_local_arr = mAcceptedFriendsList;
											SchedulingActivity.isAdded = true;
										}
										setFriendsAdapter();
									}

								} else {
									statusErrorCode(status);
								}
							}

						});
	}

}

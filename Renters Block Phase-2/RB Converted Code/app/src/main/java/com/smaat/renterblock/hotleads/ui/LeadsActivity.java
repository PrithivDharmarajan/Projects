package com.smaat.renterblock.hotleads.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.hotleads.adapter.LeadsViewAdapter;
import com.smaat.renterblock.hotleads.entity.HotLeadsModelEntity;
import com.smaat.renterblock.hotleads.entity.HotLeadsPropertyEntity;
import com.smaat.renterblock.hotleads.entity.LeadsActiveEntity;
import com.smaat.renterblock.hotleads.entity.LeadsListEntity;
import com.smaat.renterblock.hotleads.entity.LeadsPassiveEntity;
import com.smaat.renterblock.model.PendingRequestResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;

public class LeadsActivity extends BaseActivity implements DialogMangerCallback {

	private TextView header_txt;
	private LeadsListEntity leads_list;
	private ListView leads_listview;
	private LeadsViewAdapter leads_adapter;
	static Context mContext;
	private Bundle mBundle;
	private String mCallFrom = null, mPropertyId = null;
	private ArrayList<HotLeadsPropertyEntity> mHotLeadsPropertyEntityList;

	private LeadsActiveEntity leads_active;
	private LeadsPassiveEntity leads_passive;
	private ArrayList<LeadsActiveEntity> leads_active_aray;

	// private ArrayList<LeadsPassiveEntity> leads_passive_array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leads_main_screen);

		UserID = (String) GlobalMethods.getValueFromPreference(
				LeadsActivity.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);
		initializeViews();
		mContext = LeadsActivity.this;
		updateHotLeadsService();
	}

	private void updateHotLeadsService() {
		String Url = AppConstants.BASE_URL + "getmyhotleads/update";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("property_id", HotLeadsMainScreen.prop_id);

		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						super.callback(url, json, status);

						if (json != null) {

						} else {
							DialogManager.showCustomAlertDialog(
									LeadsActivity.this, LeadsActivity.this,
									getString(R.string.server_unreachable));
						}
					}

				});
	}

	private void initializeViews() {
		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setText(getString(R.string.hot_leads));
		header_txt.setTypeface(helvetica_bold);
		leads_listview = (ListView) findViewById(R.id.leads_list);

		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			leads_list = new LeadsListEntity();
			leads_list = (LeadsListEntity) mBundle.getSerializable("leadsList");
			mCallFrom = mBundle.getString("CallFrom");
			mPropertyId = mBundle.getString("PropertyId");
		}
		if (leads_list != null && leads_list.getPassive() != null) {

			setLeadsAdapter(leads_list);
		}
		if (mPropertyId != null) {
			callHotLeadsApi();
		}

	}

	private void setLeadsAdapter(LeadsListEntity leads_list) {

		leads_active_aray = new ArrayList<LeadsActiveEntity>();
		// leads_passive_array = new ArrayList<LeadsPassiveEntity>();

		for (int active = 0; active < leads_list.getActive().size(); active++) {
			leads_active = new LeadsActiveEntity();
			leads_active.setDatetime(leads_list.getActive().get(active)
					.getDatetime());
			leads_active.setFriends_count(leads_list.getActive().get(active)
					.getFriends_count());
			leads_active.setIs_friend(leads_list.getActive().get(active)
					.getIs_friend());
			leads_active.setPhotos_count(leads_list.getActive().get(active)
					.getPhotos_count());
			leads_active.setReviews_count(leads_list.getActive().get(active)
					.getReviews_count());
			leads_active.setUser_avg_rating(leads_list.getActive().get(active)
					.getUser_avg_rating());
			leads_active.setUser_profileImage(leads_list.getActive()
					.get(active).getUser_profileImage());
			leads_active.setUserId(leads_list.getActive().get(active)
					.getUserId());
			leads_active.setUser_name(leads_list.getActive().get(active)
					.getUser_name());
			leads_active.setRb_user(leads_list.getActive().get(active)
					.getRb_user());
			leads_active
					.setCount(leads_list.getActive().get(active).getCount());
			leads_active.setIsfavourite(leads_list.getActive().get(active)
					.getIsfavourite());
			leads_active.setIsActive("1");
			leads_active_aray.add(leads_active);

		}

		for (int passive = 0; passive < leads_list.getPassive().size(); passive++) {
			leads_active = new LeadsActiveEntity();
			leads_active.setDatetime(leads_list.getPassive().get(passive)
					.getDatetime());
			leads_active.setFriends_count(leads_list.getPassive().get(passive)
					.getFriends_count());
			leads_active.setIs_friend(leads_list.getPassive().get(passive)
					.getIs_friend());
			leads_active.setPhotos_count(leads_list.getPassive().get(passive)
					.getPhotos_count());
			leads_active.setReviews_count(leads_list.getPassive().get(passive)
					.getReviews_count());
			leads_active.setUser_avg_rating(leads_list.getPassive()
					.get(passive).getUser_avg_rating());
			leads_active.setUser_profileImage(leads_list.getPassive()
					.get(passive).getUser_profileImage());
			leads_active.setUserId(leads_list.getPassive().get(passive)
					.getUserId());
			leads_active.setUser_name(leads_list.getPassive().get(passive)
					.getUser_name());
			leads_active.setRb_user(leads_list.getPassive().get(passive)
					.getRb_user());
			leads_active.setIsfavourite(leads_list.getPassive().get(passive)
					.getIsfavourite());
			leads_active.setCount(leads_list.getPassive().get(passive)
					.getCount());
			leads_active.setIsActive("0");
			leads_active_aray.add(leads_active);
		}

		leads_adapter = new LeadsViewAdapter(LeadsActivity.this,
				R.layout.leads_activity_page, leads_active_aray);
		leads_listview.setAdapter(leads_adapter);
	}

	private void callHotLeadsApi() {
		String Url = AppConstants.BASE_URL + "getmyhotleads";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserID);
		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						if (json != null) {
							HotLeadsModelEntity mResponse = new Gson()
									.fromJson(json.toString(),
											HotLeadsModelEntity.class);
							if (mResponse.getError_code().equalsIgnoreCase(
									AppConstants.SUCCESS_CODE)) {
								mHotLeadsPropertyEntityList = mResponse
										.getResult();

								for (int i = 0; i < mHotLeadsPropertyEntityList
										.size(); i++) {
									if (mPropertyId
											.equalsIgnoreCase(mHotLeadsPropertyEntityList
													.get(i).getProperty_id())) {
										leads_list = new LeadsListEntity();
										leads_list = mHotLeadsPropertyEntityList
												.get(i).getLeadslist();
										setLeadsAdapter(leads_list);
									}
								}
							}
						} else {
							statusErrorCode(status);
						}
					}
				});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
				Intent inte = new Intent(LeadsActivity.this,
						HotLeadsMainScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				finish();
				AppConstants.push_notification_call = "false";
			}
			// if (mCallFrom != null && mCallFrom.equalsIgnoreCase("GCM")) {
			// launchActivity(MapFragmentActivity.class);
			// overridePendingTransition(R.anim.slide_out_right,
			// R.anim.slide_in_left);
			// }
			else {
				launchActivity(HotLeadsMainScreen.class);
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
			Intent inte = new Intent(LeadsActivity.this,
					HotLeadsMainScreen.class);
			startActivity(inte);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
			AppConstants.push_notification_call = "false";
		} else {
			launchActivity(HotLeadsMainScreen.class);
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
		}
	}

	public static void sendFriendRequest(String friend_user_id) {
		((LeadsActivity) mContext).callSendFriendRequestService(friend_user_id);
	}

	private void callSendFriendRequestService(String friend_user_id) {
		String url = AppConstants.BASE_URL + "friend/sendfrindrequest";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("friend_user_id", friend_user_id);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(mContext))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									PendingRequestResponse obj = new Gson().fromJson(
											json.toString(),
											PendingRequestResponse.class);
									onSuccessRequest(obj);
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	private void onSuccessRequest(PendingRequestResponse mResponse) {
		DialogManager.showCustomAlertDialog(LeadsActivity.this,
				LeadsActivity.this, mResponse.getMsg());
	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub
		super.onOkclick();
		leads_adapter.notifyDataSetChanged();
	}

}

package com.smaat.renterblock.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.NotificationAdapter;
import com.smaat.renterblock.alerts.AlertsActivity;
import com.smaat.renterblock.entity.NotificationEntity;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.friends.ui.FriendsPendingRequest;
import com.smaat.renterblock.hotleads.ui.LeadsActivity;
import com.smaat.renterblock.model.NotificationResponse;
import com.smaat.renterblock.scheduling.ScheduleMeetingActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class NotificationActivity extends BaseActivity implements OnClickListener {
	/**
	 * Notofication Declaration
	 */
	private ListView mNotificationList;
	private ArrayList<NotificationEntity> mNotificationEntity;
	private NotificationAdapter mNotificationAdapter;
	private Intent intent = null;
	static Context mContext;
	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private Button slide_menu_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		mContext = NotificationActivity.this;

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initComponents();
		setGoogleAnalytics(NotificationActivity.this);
		getNotification();

	}

	private void initComponents() {
		/**
		 * Notification Initialization
		 */
		mNotificationList = (ListView) findViewById(R.id.notification_list);
		/**
		 * Slide Menu Intialization
		 */
		UserID = GlobalMethods.getUserID(this);

		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);

		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER) || selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.notification_icon);
			AppConstants.view_id = R.id.notification_icon;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.notification_icon);
			AppConstants.view_id = R.id.notification_icon;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.notification_icon);
			AppConstants.view_id = R.id.notification_icon;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.notification_icon);
			AppConstants.view_id = R.id.notification_icon;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);
		slide_menu_btn = (Button) findViewById(R.id.menu_btn);

		if (AppConstants.from_profile_notification.equalsIgnoreCase("true")) {
			slide_menu_btn.setBackgroundResource(R.drawable.back_arrow_white);
		} else {
			slide_menu_btn.setBackgroundResource(R.drawable.slide_button);
		}

		slide_holder.setAllowInterceptTouch(false);

		// mNotificationList.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int pos,
		// long arg3) {
		//
		// }
		//
		// });

	}

	private void getNotification() {

		String Url = AppConstants.BASE_URL + "notification";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						if (json != null) {
							NotificationResponse mResponse = new Gson().fromJson(json.toString(),
									NotificationResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mNotificationEntity = mResponse.getResult();
								mNotificationAdapter = new NotificationAdapter(NotificationActivity.this,
										mNotificationEntity);
								mNotificationList.setAdapter(mNotificationAdapter);
							}
						} else {
							statusErrorCode(status);
						}
					}

				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_icon:
			if (AppConstants.from_profile_notification.equalsIgnoreCase("true")) {
				Intent inte = new Intent(NotificationActivity.this, ProfileScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				AppConstants.from_profile_notification = "false";
			} else {
				slide_holder.toggle();
			}
			break;
		}
	}

	public void onUserClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuUserNameClick(v);
		}

	}

	public void onbuyMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuBuyClick(v);
			slide_holder.toggle();
		}
	}

	public void onSellerMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuSellerclick(v);
			slide_holder.toggle();
		}
	}

	public void onAgentMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuAgentClick(v);
			slide_holder.toggle();
		}
	}

	public static void callDeleteNotification(String notification_id, int pos) {
		// TODO Auto-generated method stub
		((NotificationActivity) mContext).deleteNotification(notification_id);
		((NotificationActivity) mContext).moveNotification(pos);
	}

	private void moveNotification(int pos) {
		if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("alertproperty")
				|| mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("savedproperty")) {

			intent = new Intent(NotificationActivity.this, PropertyDetailsActivity.class);
			intent.putExtra("PropertyId", mNotificationEntity.get(pos).getType_id());
			intent.putExtra("PropType", "");
			intent.putExtra("CallFrom", "Notification");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("schedule")) {
			intent = new Intent(NotificationActivity.this, ScheduleMeetingActivity.class);
			intent.putExtra("ScheduleID", mNotificationEntity.get(pos).getType_id());
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("friend")) {
			intent = new Intent(NotificationActivity.this, FriendsPendingRequest.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("request")) {
			intent = new Intent(NotificationActivity.this, LeadsActivity.class);
			intent.putExtra("PropertyId", mNotificationEntity.get(pos).getType_id());
			intent.putExtra("CallFrom", "Notification");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("Alert")) {
			intent = new Intent(NotificationActivity.this, AlertsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("chat")
				|| mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("hotleadchat")) {
			intent = new Intent(NotificationActivity.this, FriendChatScreen.class);
			intent.putExtra("groupId", mNotificationEntity.get(pos).getType_id());
			intent.putExtra("from_call", "");
			intent.putExtra("type", "group");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

		} else if (mNotificationEntity.get(pos).getType_of_notification().equalsIgnoreCase("request to agent")) {
			intent = new Intent(NotificationActivity.this, ProfileScreen.class);
			intent.putExtra("user_id", mNotificationEntity.get(pos).getType_id());
			intent.putExtra("from_call", "");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}
	}

	private void deleteNotification(String notification_id) {
		String Url = AppConstants.BASE_URL + "notification/delete";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("notification_id", notification_id);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						if (json != null) {
							System.out.println(json);
						} else {
						}
					}

				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getNotification();
	}
}

package com.smaat.renterblock.scheduling;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.NotificationEntityScheduling;
import com.smaat.renterblock.localnotification.MyReceiver;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SchedulingActivity extends BaseActivity implements OnClickListener {

	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;

	/**
	 * Scheduling Declaration
	 */
	private ListView mScheduleList;
	private SchedulingAdapter mAdapter;
	private ArrayList<ScheduleCommonEntity> mScheduleCommonEntityList;
	private ScheduleCommonEntity mScheduleCommonEntity;
	static boolean isAdded = false;
	static boolean not_from_add = true;

	private ArrayList<NotificationEntityScheduling> notification_date_time;
	private NotificationEntityScheduling nt_schedule;

	static Context mContext;
	private RelativeLayout mScheduling_delete_view;
	private ScheduleResponse mResponse;
	private boolean isShown = false;
	private Button mEditBtn;

	// private ArrayList<String> mScheduleIds = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheduling);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelvetica(
				this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		mContext = this;
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		TextView header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(helvetica_bold);
		initComponents();
		setGoogleAnalytics(SchedulingActivity.this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			getSchedulingList();
		}
	}

	public void getSchedulingList() {

		String Url = AppConstants.BASE_URL + "getschedule";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
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
									mResponse = new Gson().fromJson(
											json.toString(),
											ScheduleResponse.class);

									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {

										setSchedulingAdapter(mResponse, "0");

									}
								} else {
									statusErrorCode(status);
								}
							}

						});

	}

	private void setSchedulingAdapter(ScheduleResponse mResponse, String chec) {
		mScheduleCommonEntityList = new ArrayList<ScheduleCommonEntity>();
		notification_date_time = new ArrayList<NotificationEntityScheduling>();
		for (int i = 0; i < mResponse.getResult().getFriend_schedule().size(); i++) {
			mScheduleCommonEntity = new ScheduleCommonEntity();
			nt_schedule = new NotificationEntityScheduling();
			mScheduleCommonEntity.setMeeting_id(mResponse.getResult()
					.getFriend_schedule().get(i).getMeeting_id());
			mScheduleCommonEntity.setScheduler_user_id(mResponse.getResult()
					.getFriend_schedule().get(i).getScheduler_user_id());
			mScheduleCommonEntity.setSchedule_id(mResponse.getResult()
					.getFriend_schedule().get(i).getSchedule_id());
			mScheduleCommonEntity.setUser_id(mResponse.getResult()
					.getFriend_schedule().get(i).getUser_id());
			mScheduleCommonEntity.setStatus(mResponse.getResult()
					.getFriend_schedule().get(i).getStatus());
			mScheduleCommonEntity.setFirst_name(mResponse.getResult()
					.getFriend_schedule().get(i).getUser_name());
			mScheduleCommonEntity.setUser_pic(mResponse.getResult()
					.getFriend_schedule().get(i).getUser_pic());
			mScheduleCommonEntity.setMeeting_subject(mResponse.getResult()
					.getFriend_schedule().get(i).getMeeting_subject());
			mScheduleCommonEntity.setDescription(mResponse.getResult()
					.getFriend_schedule().get(i).getDescription());
			mScheduleCommonEntity.setFriends(mResponse.getResult()
					.getFriend_schedule().get(i).getFriends());
			mScheduleCommonEntity.setDate(mResponse.getResult()
					.getFriend_schedule().get(i).getDate());
			mScheduleCommonEntity.setTime(mResponse.getResult()
					.getFriend_schedule().get(i).getTime());
			mScheduleCommonEntity.setVenue(mResponse.getResult()
					.getFriend_schedule().get(i).getVenue());
			mScheduleCommonEntity.setIsonline(mResponse.getResult()
					.getFriend_schedule().get(i).getIsonline());
			mScheduleCommonEntity.setIs_friends_schedule("1");
			mScheduleCommonEntity.setIs_my_schedule("0");
			mScheduleCommonEntity.setIs_Accepted_schedule("0");
			mScheduleCommonEntityList.add(mScheduleCommonEntity);

			nt_schedule.setDatetime(mResponse.getResult().getFriend_schedule()
					.get(i).getDate()
					+ " "
					+ mResponse.getResult().getFriend_schedule().get(i)
							.getTime());
			nt_schedule.setSch_id(mResponse.getResult().getFriend_schedule()
					.get(i).getSchedule_id());
			nt_schedule.setMeeting_subject(mResponse.getResult()
					.getFriend_schedule().get(i).getMeeting_subject());
			notification_date_time.add(nt_schedule);
		}
		for (int i = 0; i < mResponse.getResult().getMyschedule().size(); i++) {
			mScheduleCommonEntity = new ScheduleCommonEntity();
			nt_schedule = new NotificationEntityScheduling();
			mScheduleCommonEntity.setMeeting_id(mResponse.getResult()
					.getMyschedule().get(i).getMeeting_id());
			mScheduleCommonEntity.setScheduler_user_id(mResponse.getResult()
					.getMyschedule().get(i).getScheduler_user_id());
			mScheduleCommonEntity.setSchedule_id(mResponse.getResult()
					.getMyschedule().get(i).getSchedule_id());
			mScheduleCommonEntity.setUser_id(mResponse.getResult()
					.getMyschedule().get(i).getUser_id());
			mScheduleCommonEntity.setStatus(mResponse.getResult()
					.getMyschedule().get(i).getStatus());
			mScheduleCommonEntity.setFirst_name(mResponse.getResult()
					.getMyschedule().get(i).getUser_name());
			mScheduleCommonEntity.setUser_pic(mResponse.getResult()
					.getMyschedule().get(i).getUser_pic());
			mScheduleCommonEntity.setMeeting_subject(mResponse.getResult()
					.getMyschedule().get(i).getMeeting_subject());
			mScheduleCommonEntity.setDescription(mResponse.getResult()
					.getMyschedule().get(i).getDescription());
			mScheduleCommonEntity.setFriends(mResponse.getResult()
					.getMyschedule().get(i).getFriends());
			mScheduleCommonEntity.setDate(mResponse.getResult().getMyschedule()
					.get(i).getDate());
			mScheduleCommonEntity.setTime(mResponse.getResult().getMyschedule()
					.get(i).getTime());
			mScheduleCommonEntity.setVenue(mResponse.getResult()
					.getMyschedule().get(i).getVenue());
			mScheduleCommonEntity.setIsonline(mResponse.getResult()
					.getMyschedule().get(i).getIsonline());
			mScheduleCommonEntity.setIs_friends_schedule("0");
			mScheduleCommonEntity.setIs_my_schedule("1");
			mScheduleCommonEntity.setIs_Accepted_schedule("0");
			for (int j = 0; j < mScheduleCommonEntity.getFriends().size(); j++) {
				if (mScheduleCommonEntity.getFriends().get(j).getStatus()
						.equalsIgnoreCase("1")) {
					mScheduleCommonEntity.setIs_Accepted_schedule("1");
				} else {
					mScheduleCommonEntity.setIs_Accepted_schedule("0");
				}
			}
			mScheduleCommonEntityList.add(mScheduleCommonEntity);

			nt_schedule.setDatetime(mResponse.getResult().getMyschedule()
					.get(i).getDate()
					+ " "
					+ mResponse.getResult().getMyschedule().get(i).getTime());
			nt_schedule.setSch_id(mResponse.getResult().getMyschedule().get(i)
					.getSchedule_id());
			nt_schedule.setMeeting_subject(mResponse.getResult()
					.getMyschedule().get(i).getMeeting_subject());
			notification_date_time.add(nt_schedule);

		}
		for (int i = 0; i < mResponse.getResult().getAccepted_schedule().size(); i++) {
			mScheduleCommonEntity = new ScheduleCommonEntity();
			nt_schedule = new NotificationEntityScheduling();
			mScheduleCommonEntity.setMeeting_id(mResponse.getResult()
					.getAccepted_schedule().get(i).getMeeting_id());
			mScheduleCommonEntity.setScheduler_user_id(mResponse.getResult()
					.getAccepted_schedule().get(i).getScheduler_user_id());
			mScheduleCommonEntity.setSchedule_id(mResponse.getResult()
					.getAccepted_schedule().get(i).getSchedule_id());
			mScheduleCommonEntity.setUser_id(mResponse.getResult()
					.getAccepted_schedule().get(i).getUser_id());
			mScheduleCommonEntity.setStatus(mResponse.getResult()
					.getAccepted_schedule().get(i).getStatus());
			mScheduleCommonEntity.setFirst_name(mResponse.getResult()
					.getAccepted_schedule().get(i).getUser_name());
			mScheduleCommonEntity.setUser_pic(mResponse.getResult()
					.getAccepted_schedule().get(i).getUser_pic());
			mScheduleCommonEntity.setMeeting_subject(mResponse.getResult()
					.getAccepted_schedule().get(i).getMeeting_subject());
			mScheduleCommonEntity.setDescription(mResponse.getResult()
					.getAccepted_schedule().get(i).getDescription());
			mScheduleCommonEntity.setFriends(mResponse.getResult()
					.getAccepted_schedule().get(i).getFriends());
			mScheduleCommonEntity.setDate(mResponse.getResult()
					.getAccepted_schedule().get(i).getDate());
			mScheduleCommonEntity.setTime(mResponse.getResult()
					.getAccepted_schedule().get(i).getTime());
			mScheduleCommonEntity.setIsonline(mResponse.getResult()
					.getAccepted_schedule().get(i).getIsonline());
			mScheduleCommonEntity.setVenue(mResponse.getResult()
					.getAccepted_schedule().get(i).getVenue());
			mScheduleCommonEntity.setIs_friends_schedule("0");
			mScheduleCommonEntity.setIs_my_schedule("0");
			mScheduleCommonEntity.setIs_Accepted_schedule("1");
			mScheduleCommonEntityList.add(mScheduleCommonEntity);

			nt_schedule.setDatetime(mResponse.getResult()
					.getAccepted_schedule().get(i).getDate()
					+ " "
					+ mResponse.getResult().getAccepted_schedule().get(i)
							.getTime());
			nt_schedule.setSch_id(mResponse.getResult().getAccepted_schedule()
					.get(i).getSchedule_id());
			nt_schedule.setMeeting_subject(mResponse.getResult()
					.getAccepted_schedule().get(i).getMeeting_subject());
			notification_date_time.add(nt_schedule);
		}

		System.out.println(notification_date_time.size() + "");

		mAdapter = new SchedulingAdapter(this, mScheduleCommonEntityList);
		mScheduleList.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		if (chec.equalsIgnoreCase("0")) {
			setNotification(notification_date_time);
		}

	}

	private void setNotification(
			ArrayList<NotificationEntityScheduling> notification_date_time) {
		for (int i = 0; i < notification_date_time.size(); i++) {
			long eventTime = GlobalMethods
					.getLocTimeInMillis(notification_date_time.get(i)
							.getDatetime());

			Intent inte = new Intent(SchedulingActivity.this, MyReceiver.class);
			inte.putExtra("schedule_text", "Your "
					+ notification_date_time.get(i).getMeeting_subject()
					+ " meeting start on "
					+ notification_date_time.get(i).getDatetime());
			PendingIntent pi = PendingIntent
					.getBroadcast(mContext,
							Integer.parseInt(notification_date_time.get(i)
									.getSch_id()), inte,
							PendingIntent.FLAG_ONE_SHOT);

			// Cancel the previous alarm.
			AlarmManager alarmManager = (AlarmManager) SchedulingActivity.this
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pi);

			inte = new Intent(SchedulingActivity.this, MyReceiver.class);
			inte.putExtra("schedule_text", "Your "
					+ notification_date_time.get(i).getMeeting_subject()
					+ " meeting start on "
					+ notification_date_time.get(i).getDatetime());
			pi = PendingIntent
					.getBroadcast(SchedulingActivity.this,
							Integer.parseInt(notification_date_time.get(i)
									.getSch_id()), inte,
							PendingIntent.FLAG_ONE_SHOT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, eventTime, pi);

		}
	}

	private void initComponents() {
		/**
		 * Scheduling View's Intialization
		 */
		mScheduleList = (ListView) findViewById(R.id.scheduling_list);

		/**
		 * Slide Menu Intialization
		 */
		UserID = GlobalMethods.getUserID(this);
		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mEditBtn = (Button) findViewById(R.id.edit_btn);
		mScheduling_delete_view = (RelativeLayout) findViewById(R.id.scheduling_delete_view);
		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);
		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_scheduling);
			AppConstants.view_id = R.id.buy_scheduling;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_scheduling);
			AppConstants.view_id = R.id.sell_scheduling;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_scheduling);
			AppConstants.view_id = R.id.agent_scheduling;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_scheduling);
			AppConstants.view_id = R.id.buy_scheduling;
		}
		if (UserName.equalsIgnoreCase("") || UserName.equalsIgnoreCase("0")) {
			mUserName.setText("Login");
		} else {
			mUserName.setText(UserName);
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

	}

	public static void scheduleListClick(int pos) {
		((SchedulingActivity) mContext).schonClick(pos);
	}

	private void schonClick(int pos) {
		SchedulingActivity.not_from_add = true;
		Intent intent = new Intent(SchedulingActivity.this,
				ScheduleMeetingActivity.class);
		intent.putExtra("ScheduleCommonEntity",
				mScheduleCommonEntityList.get(pos));
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	private void callDeleteSchedule() {
		String Url = AppConstants.BASE_URL + "getschedule/delete";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		String sc_id = SchedulingAdapter.mSchedule_Ids.toString()
				.replace("[", "").replace("]", "");

		params.put("user_id", UserID);
		params.put("schedule_id", sc_id);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {
								super.callback(url, json, status);
								if (json != null) {
									getSchedulingList();
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
			slide_holder.toggle();
			break;
		case R.id.delete_icon:
			callDeleteSchedule();
			break;
		case R.id.edit_button:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mResponse.getResult().getAccepted_schedule().size() == 0
						&& mResponse.getResult().getFriend_schedule().size() == 0
						&& mResponse.getResult().getMyschedule().size() == 0) {
					DialogManager.showCustomAlertDialog(this, this,
							"No Alerts are available!");
				} else {
					if (!isShown) {
						AppConstants.IS_EDIT = "true";
						mScheduling_delete_view.setVisibility(View.VISIBLE);
						SchedulingAdapter.mSchedule_Ids.clear();
						mEditBtn.setBackgroundResource(R.drawable.close_icon);
						setSchedulingAdapter(mResponse, "1");
						isShown = true;
					} else {
						AppConstants.IS_EDIT = "false";
						mScheduling_delete_view.setVisibility(View.GONE);
						SchedulingAdapter.mSchedule_Ids.clear();
						mEditBtn.setBackgroundResource(R.drawable.edit_button);
						setSchedulingAdapter(mResponse, "1");
						isShown = false;
					}
				}
			}
			break;
		case R.id.add_icon:
			// isAdded = true;
			not_from_add = false;
			SchedulingActivity.isAdded = false;
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				Intent intent = new Intent(this, ScheduleMeetingActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			break;
		}

	}

	public void onUserClick(View v) {
		onMenuUserNameClick(v);
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

	@Override
	protected void onResume() {
		if (AppConstants.isAPI) {
			AppConstants.isAPI = false;
			getSchedulingList();
		} else {

		}
		super.onResume();
	}
}

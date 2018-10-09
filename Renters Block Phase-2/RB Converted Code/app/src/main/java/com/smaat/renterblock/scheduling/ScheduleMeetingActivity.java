package com.smaat.renterblock.scheduling;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.friends.entity.AcceptFriendEntity;
import com.smaat.renterblock.friends.entity.FriendDetailsEntity;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.PropertyDetailResponse;
import com.smaat.renterblock.savedsearch.LocalSavedSearchEntity;
import com.smaat.renterblock.sqlite.LocalSavedSearch;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GPSTracker;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.PlaceRespone;
import com.smaat.renterblock.util.Places;
import com.smaat.renterblock.util.ServieRequestHandler;
import com.smaat.renterblock.util.TypefaceSingleton;
import com.smaat.renterblock.util.WebserviceCallbackInterface;

public class ScheduleMeetingActivity extends BaseActivity
		implements OnClickListener, WebserviceCallbackInterface, DialogMangerCallback {

	private static EditText mMeetingEdit, mDescriotionEdit, mVenueEdit;
	private TextView mDateTxt, mTimeTxt;
	private Button mOnline, mOffline;
	private RelativeLayout mParentLay;
	static final int DATE_DIALOG_ID = 999;
	private int year;
	private int month;
	private int day;
	private int mHour;
	private int mMinute;
	private int mSec;
	static final int TIME_DIALOG_ID = 0;
	static TextView mHeaderText, mAddFriendsText;
	public static ArrayList<String> mUserID = new ArrayList<String>();
	public static ArrayList<String> mUserName = new ArrayList<String>();
	private boolean fromEdit = false;
	private String strDescription = "", strMeetingSubject = "", strDate = "", strTime = "", strVenue = "",
			strFriendsUserId = "", strStatus = "1", mAlert = "", strScheduleId = "";
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-M-d");
	private SimpleDateFormat mNewDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static String ids = null;
	private Bundle mBundle;
	private ScheduleCommonEntity mCommonEntity;
	private String mScheduleID = null, mScheduleID1 = null, mCallFrom = null;
	private ScheduleCommonEntity mCommonEntity1;
	private int mMonth;
	private int mYear;
	private int mDate;
	Date currentLocalTime;
	private ArrayList<ScheduleCommonEntity> mScheduleCommonEntityList;
	private ScheduleCommonEntity mScheduleCommonEntity;
	private int checkYear;
	private int checkMonth;
	private int checkDay;

	static boolean from_bundle = false;
	/**
	 * Google places Api
	 */
	private static Context mContext;
	static FrameLayout mSavedSearchFrameView;
	private ListView mSavedSearchList;
	private ArrayList<LocalSavedSearchEntity> mLocalSavedSearch = new ArrayList<LocalSavedSearchEntity>();
	private ScheduleSavedSearchAdapter mSavedSearchAdapter;
	private LocalSavedSearchEntity mLocalSavedSearchEntity;
	private ArrayList<String> mArea;
	static double mLatitude, mLongitude;
	private String area;
	private String latitu;
	private String Longi;
	private LinearLayout mAcceptBtnLay;
	private Button mSave;
	private RelativeLayout mAddFriendsLay, mDateLay, mTimeLay;
	private Object mSchedulerID;
	private Object mIsOnline;

	String inputPattern = "yyyy-MM-dd";
	String outputPattern = "MM-dd-yyyy";

	String rev_outputPattern = "yyyy-MM-dd";
	String rev_inputPattern = "MM-dd-yyyy";
	String rev_time_output = "HH:mm:ss";
	String rev_time_input = "hh:mm a";

	String time_input = "hh:mm:ss";
	String time_output = "hh:mm a";

	ArrayList<String> user_names_sch = new ArrayList<String>();

	// Scheduling local values
	static ArrayList<AcceptFriendEntity> sch_local_arr;
	static AcceptFriendEntity sch_local_ent;

	static ArrayList<AcceptFriendEntity> more_sch_local_arr;
	static AcceptFriendEntity more_sch_local_ent;

	private FriendDetailsEntity fr_det_ent;
	private ArrayList<FriendDetailsEntity> fr_det_arr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_schedule_meeting);
		ViewGroup mRootView = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		setFont(mRootView, mTypefaceBold);
		setupUI(mRootView);

		from_bundle = false;
		UserID = GlobalMethods.getUserID(this);

		mContext = ScheduleMeetingActivity.this;
		sch_local_arr = new ArrayList<AcceptFriendEntity>();
		more_sch_local_arr = new ArrayList<AcceptFriendEntity>();
		fr_det_arr = new ArrayList<FriendDetailsEntity>();

		sch_local_arr.clear();
		fr_det_arr.clear();

		mArea = new ArrayList<String>();
		getCurentDate();
		getCurentTime();
		initComponents();

	}

	private void initComponents() {

		mHeaderText = (TextView) findViewById(R.id.header_txt);
		mHeaderText.setText(getString(R.string.schedule_meet_header));

		mAddFriendsText = (TextView) findViewById(R.id.add_friends_txt);
		mSave = (Button) findViewById(R.id.save_btn);
		mMeetingEdit = (EditText) findViewById(R.id.meeting_edit);
		mDescriotionEdit = (EditText) findViewById(R.id.description_edit);
		mVenueEdit = (EditText) findViewById(R.id.add_venue_edit);
		mAcceptBtnLay = (LinearLayout) findViewById(R.id.accept_btn_lay);
		mAddFriendsLay = (RelativeLayout) findViewById(R.id.add_friends_lay);
		mDateLay = (RelativeLayout) findViewById(R.id.date_lay);
		mTimeLay = (RelativeLayout) findViewById(R.id.time_lay);
		mDescriotionEdit.setMovementMethod(new ScrollingMovementMethod());
		mDateTxt = (TextView) findViewById(R.id.date_text);
		mTimeTxt = (TextView) findViewById(R.id.time_text);
		mOnline = (Button) findViewById(R.id.online_btn);
		mOffline = (Button) findViewById(R.id.offline_btn);

		mParentLay = (RelativeLayout) findViewById(R.id.parent_layout);

		mSavedSearchFrameView = (FrameLayout) findViewById(R.id.save_search_frame_view);
		mSavedSearchList = (ListView) findViewById(R.id.saved_search_list);

		mParentLay.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mSavedSearchFrameView.setVisibility(View.GONE);
				return false;
			}
		});
		// mVenueEdit.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (fromEdit) {
		// mSavedSearchFrameView.setVisibility(View.GONE);
		// } else {
		// mSavedSearchFrameView.setVisibility(View.VISIBLE);
		// }
		// if (mVenueEdit.getText().toString().equals("")) {
		// addedDatasTosaveSearch();
		// } else {
		// callPlaces(mVenueEdit.getText().toString());
		// }
		// return false;
		// }
		// });
		// mVenueEdit.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// if (mVenueEdit.getText().toString().equals("")) {
		// addedDatasTosaveSearch();
		// } else {
		// callPlaces(s.toString());
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// }
		// });
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			from_bundle = true;
			mCommonEntity = (ScheduleCommonEntity) mBundle.get("ScheduleCommonEntity");
			if (mCommonEntity != null) {
				ScheduleMeetingActivity.sch_local_arr.clear();

				for (int i = 0; i < mCommonEntity.getFriends().size(); i++) {
					fr_det_ent = new FriendDetailsEntity();

					fr_det_ent.setUser_friend_id(mCommonEntity.getFriends().get(i).getUser_id());
					fr_det_ent.setUser_name(mCommonEntity.getFriends().get(i).getUser_name());
					fr_det_ent.setIsRemove("1");
					fr_det_arr.add(fr_det_ent);

					// ScheduleMeetingActivity.sch_local_arr
					// .add(ScheduleMeetingActivity.sch_local_ent);
				}

				for (int i = 0; i < fr_det_arr.size(); i++) {
					sch_local_ent = new AcceptFriendEntity();
					AcceptFriendEntity local_schedule_ent = new AcceptFriendEntity();
					ArrayList<FriendDetailsEntity> friends_deta_arr = new ArrayList<FriendDetailsEntity>();
					friends_deta_arr.add(fr_det_arr.get(i));
					local_schedule_ent.setFriends_details(friends_deta_arr);
					ScheduleMeetingActivity.sch_local_arr.add(local_schedule_ent);
				}
				// sch_local_ent.setFriends_details(fr_det_arr);

				String[] days_format = mCommonEntity.getDate().split("-");
				year = Integer.parseInt(days_format[0]);
				month = Integer.parseInt(days_format[1]);
				day = Integer.parseInt(days_format[2]);

			}

			mScheduleID = mBundle.getString("ScheduleID");
			mCallFrom = mBundle.getString("CallFrom");
			if (mCallFrom == null) {
				mCallFrom = "";
			}
			if (mScheduleID != null) {
				getSchedulingList();
			} else {
				setDefaultValues(mCommonEntity);
			}
		}
	}

	private void setDefaultValues(ScheduleCommonEntity mCommonEntity) {

		String string = mCommonEntity.getDate();
		String[] parts = string.split("-");
		checkYear = Integer.parseInt(parts[0]);
		checkMonth = Integer.parseInt(parts[1]);
		checkDay = Integer.parseInt(parts[2]);

		mSchedulerID = mCommonEntity.getSchedule_id();
		mMeetingEdit.setText(mCommonEntity.getMeeting_subject());
		mDescriotionEdit.setText(mCommonEntity.getDescription());
		// mDateTxt.setText(mCommonEntity.getDate());
		SimpleDateFormat inputFormat = new SimpleDateFormat(outputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		SimpleDateFormat outputFormats = new SimpleDateFormat(inputPattern);

		Date date = null;
		String str = null;

		try {
			Date da = outputFormats.parse(mCommonEntity.getDate());
			str = outputFormat.format(da);
			// date = inputFormat.parse(da);

			mDateTxt.setText(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat timeinputFormat = new SimpleDateFormat(time_input);
		SimpleDateFormat timeoutputFormat = new SimpleDateFormat(time_output);

		Date date1 = null;
		String str1 = null;

		try {
			date1 = timeinputFormat.parse(mCommonEntity.getTime());
			str1 = timeoutputFormat.format(date1);
			mTimeTxt.setText(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String sd = GlobalMethods.dateFormats(mContext, mCommonEntity.getDate() + " " + mCommonEntity.getTime());
		String date_time[] = sd.split(" ");
		String dat = date_time[0];
		String tim = date_time[1] + " " + date_time[2];

		// mDateTxt.setText(dat);
		mTimeTxt.setText(tim);

		// mTimeTxt.setText(mCommonEntity.getTime());
		mVenueEdit.setText(mCommonEntity.getVenue());
		strScheduleId = mCommonEntity.getSchedule_id();
		mUserName.clear();
		if (mCommonEntity.getFriends().size() != 0) {
			for (int i = 0; i < mCommonEntity.getFriends().size(); i++) {
				mUserName.add(mCommonEntity.getFriends().get(i).getUser_name());
			}
			String mString = mUserName.toString();
			String mString2 = mString.replace("]", "");
			String mString3 = mString2.replace("[", "");
			mAddFriendsText.setText(mString3);
		}
		mUserID.clear();
		if (mCommonEntity.getFriends().size() != 0) {
			for (int i = 0; i < mCommonEntity.getFriends().size(); i++) {
				mUserID.add(mCommonEntity.getFriends().get(i).getScheduler_user_id());
			}
		}
		if (mCommonEntity.getStatus().equals("0")) {
			strStatus = "0";
			mOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_normal, 0);
			mOffline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_over, 0);
		} else {
			strStatus = "1";
			mOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_over, 0);
			mOffline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_normal, 0);
		}

		if (mCommonEntity.getIs_friends_schedule().equalsIgnoreCase("1")) {

			mMeetingEdit.setFocusable(false);
			mMeetingEdit.setFocusableInTouchMode(false);
			mDescriotionEdit.setFocusable(false);
			mDescriotionEdit.setFocusableInTouchMode(false);
			mDateLay.setClickable(false);
			mTimeLay.setClickable(false);
			mVenueEdit.setFocusable(false);
			mVenueEdit.setFocusableInTouchMode(false);
			mVenueEdit.setClickable(false);
			mOnline.setClickable(false);
			mOffline.setClickable(false);
			String dat_tiem = dat + " " + tim;
			if (validateTime(dat_tiem)) {
				mSave.setText("Save");
			} else {
				mSave.setText("Chat");
			}
			mSave.setClickable(false);
			mAddFriendsLay.setClickable(true);
			mAcceptBtnLay.setVisibility(View.VISIBLE);

		} else if (mCommonEntity.getIs_my_schedule().equalsIgnoreCase("1")) {

			mMeetingEdit.setFocusable(true);
			mMeetingEdit.setFocusableInTouchMode(true);
			mDescriotionEdit.setFocusable(true);
			mDescriotionEdit.setFocusableInTouchMode(true);
			mDateLay.setClickable(true);
			mTimeLay.setClickable(true);
			mVenueEdit.setFocusable(true);
			mVenueEdit.setFocusableInTouchMode(true);
			mVenueEdit.setClickable(true);
			mOnline.setClickable(true);
			mOffline.setClickable(true);
			mAddFriendsLay.setClickable(true);
			String dat_tiem = dat + " " + tim;
			if (validateTime(dat_tiem)) {
				mSave.setText("Save");
			} else {
				mSave.setText("Chat");
			}
			mSave.setClickable(true);
			mAcceptBtnLay.setVisibility(View.GONE);

		} else if (mCommonEntity.getIs_Accepted_schedule().equalsIgnoreCase("1")) {
			fromEdit = true;
			mMeetingEdit.setFocusable(false);
			mMeetingEdit.setFocusableInTouchMode(false);
			mDescriotionEdit.setFocusable(false);
			mDescriotionEdit.setFocusableInTouchMode(false);
			mDateLay.setClickable(false);
			mTimeLay.setClickable(false);
			mVenueEdit.setFocusable(false);
			mVenueEdit.setFocusableInTouchMode(false);
			mVenueEdit.setClickable(false);
			mOnline.setClickable(false);
			mOffline.setClickable(false);
			String dat_tiem = dat + " " + tim;
			if (validateTime(dat_tiem)) {
				mSave.setText("Save");
			} else {
				mSave.setText("Chat");
			}
			mSave.setClickable(true);
			mAddFriendsLay.setClickable(true);
			mAcceptBtnLay.setVisibility(View.GONE);

		}

	}

	private boolean validateTime(String dat_tiem) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			Calendar start_time = Calendar.getInstance();
			Date d1 = start_time.getTime();
			String tim = sdf.format(d1);
			String current_time = dat_tiem;

			Date currentdate = sdf.parse(tim);
			Date Given_date = sdf.parse(current_time);

			if (currentdate.before(Given_date)) {
				return true;
			}

		} catch (Exception e) {
		}

		return false;
	}

	private void setApiValues(ScheduleCommonEntity mCommonEntity) {
		mSchedulerID = mCommonEntity.getSchedule_id();
		mMeetingEdit.setText(mCommonEntity.getMeeting_subject());
		mDescriotionEdit.setText(mCommonEntity.getDescription());
		// mDateTxt.setText(mCommonEntity.getDate());
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(mCommonEntity.getDate());
			str = outputFormat.format(date);
			mDateTxt.setText(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat timeinputFormat = new SimpleDateFormat(time_input);
		SimpleDateFormat timeoutputFormat = new SimpleDateFormat(time_output);

		Date date1 = null;
		String str1 = null;

		try {
			date1 = timeinputFormat.parse(mCommonEntity.getTime());
			str1 = timeoutputFormat.format(date1);
			mTimeTxt.setText(str1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// mTimeTxt.setText(mCommonEntity.getTime());
		mVenueEdit.setText(mCommonEntity.getVenue());
		strScheduleId = mCommonEntity.getSchedule_id();
		mUserName.clear();
		if (mCommonEntity.getFriends().size() != 0) {
			for (int i = 0; i < mCommonEntity.getFriends().size(); i++) {
				mUserName.add(mCommonEntity.getFriends().get(i).getUser_name());
			}
			String mString = mUserName.toString();
			String mString2 = mString.replace("]", "");
			String mString3 = mString2.replace("[", "");
			mAddFriendsText.setText(mString3);
		}
		mUserID.clear();
		if (mCommonEntity.getFriends().size() != 0) {
			for (int i = 0; i < mCommonEntity.getFriends().size(); i++) {
				mUserID.add(mCommonEntity.getFriends().get(i).getUser_id());
			}
		}
		if (mCommonEntity.getStatus().equals("0")) {
			strStatus = "0";
			mOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_normal, 0);
			mOffline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_over, 0);
		} else {
			strStatus = "1";
			mOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_over, 0);
			mOffline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_normal, 0);
		}

		if (mCommonEntity.getIs_friends_schedule().equalsIgnoreCase("1")) {

			mMeetingEdit.setFocusable(false);
			mMeetingEdit.setFocusableInTouchMode(false);
			mDescriotionEdit.setFocusable(false);
			mDescriotionEdit.setFocusableInTouchMode(false);
			mDateLay.setClickable(false);
			mTimeLay.setClickable(false);
			mVenueEdit.setFocusable(false);
			mVenueEdit.setFocusableInTouchMode(false);
			mVenueEdit.setClickable(false);
			mOnline.setClickable(false);
			mOffline.setClickable(false);
			mSave.setText("Save");
			mSave.setClickable(false);
			mAddFriendsLay.setClickable(true);
			mAcceptBtnLay.setVisibility(View.VISIBLE);

		} else if (mCommonEntity.getIs_my_schedule().equalsIgnoreCase("1")) {

			mMeetingEdit.setFocusable(true);
			mMeetingEdit.setFocusableInTouchMode(true);
			mDescriotionEdit.setFocusable(true);
			mDescriotionEdit.setFocusableInTouchMode(true);
			mDateLay.setClickable(true);
			mTimeLay.setClickable(true);
			mVenueEdit.setFocusable(true);
			mVenueEdit.setFocusableInTouchMode(true);
			mVenueEdit.setClickable(true);
			mOnline.setClickable(true);
			mOffline.setClickable(true);
			mAddFriendsLay.setClickable(true);
			mSave.setText("Save");
			mSave.setClickable(true);
			mAcceptBtnLay.setVisibility(View.GONE);

		} else if (mCommonEntity.getIs_Accepted_schedule().equalsIgnoreCase("1")) {
			fromEdit = true;
			mMeetingEdit.setFocusable(false);
			mMeetingEdit.setFocusableInTouchMode(false);
			mDescriotionEdit.setFocusable(false);
			mDescriotionEdit.setFocusableInTouchMode(false);
			mDateLay.setClickable(false);
			mTimeLay.setClickable(false);
			mVenueEdit.setFocusable(false);
			mVenueEdit.setFocusableInTouchMode(false);
			mVenueEdit.setClickable(false);
			mOnline.setClickable(false);
			mOffline.setClickable(false);
			mSave.setText("Chat");
			mSave.setClickable(true);
			mAddFriendsLay.setClickable(true);
			mAcceptBtnLay.setVisibility(View.GONE);

		}

	}

	private void callAcceptorReject() {

		String Url = AppConstants.BASE_URL + "acceptmeeting";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		// UserID = "3";
		params.put("user_id", UserID);
		params.put("schedule_id", mSchedulerID);
		params.put("isonline", mIsOnline);

		((BaseActivity) mContext).aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(Url,
				params, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);

						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mAlert = "CallApi";
								DialogManager.showCustomAlertDialog(ScheduleMeetingActivity.this,
										ScheduleMeetingActivity.this, mResponse.getMsg());
							}
						} else {
							((BaseActivity) mContext).statusErrorCode(status);
						}
					}

				});

	}

	private void addedDatasTosaveSearch() {
		AppConstants.isAdded = false;
		if (UserID.equalsIgnoreCase("0") || UserID.equalsIgnoreCase("")) {

			mLocalSavedSearch.clear();
			mLocalSavedSearchEntity = new LocalSavedSearchEntity();
			mLocalSavedSearchEntity.setLocation("");
			mLocalSavedSearchEntity.setProperty_type("");
			mLocalSavedSearchEntity.setUser_id("");
			mLocalSavedSearch.add(mLocalSavedSearchEntity);

			AppConstants.from_api = false;

			mSavedSearchAdapter = new ScheduleSavedSearchAdapter(ScheduleMeetingActivity.this,
					R.layout.save_search_custom_adapter_place_list, mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();

		} else {

			mLocalSavedSearch = LocalSavedSearch.saveSearch(ScheduleMeetingActivity.this);
			AppConstants.from_api = false;

			mSavedSearchAdapter = new ScheduleSavedSearchAdapter(ScheduleMeetingActivity.this,
					R.layout.save_search_custom_adapter_place_list, mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();
		}
	}

	private void callPlaces(String place) {

		ServieRequestHandler.getInstance().getResultPlaces(getURL(AppConstants.PLACES_API_BASE, place), aq(),
				ScheduleMeetingActivity.this, null, null, this);
	}

	public static String getURL(String apiKey, String place) {
		String key = "key=" + apiKey;
		String input = "";
		try {
			input = "input=" + URLEncoder.encode(place, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String types = "types=geocode";
		String sensor = "sensor=false";
		String parameters = input + "&" + types + "&" + sensor + "&" + key;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;
		return url;
	}

	public static void callPlacesApi(String location) {
		mVenueEdit.setText(location);
		((ScheduleMeetingActivity) mContext).new getLatLong().execute(location);
		mSavedSearchFrameView.setVisibility(View.GONE);

	}

	public static void getPropertyDetails(String lat, String longi, String location_name) {
		mVenueEdit.setText(location_name);
		mLatitude = Double.valueOf(lat);
		mLongitude = Double.valueOf(lat);
		mSavedSearchFrameView.setVisibility(View.GONE);
	}

	public static void setCurrentLocationDetails() {
		((ScheduleMeetingActivity) mContext).showCurrentLocation();

	}

	private void showCurrentLocation() {
		GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation() == false) {
			tracker.showSettingsAlert();
		} else {
			double latitude = tracker.getLatitude();
			double longitude = tracker.getLongitude();
			callGoogleApiService(latitude, longitude);
		}
	}

	private void callGoogleApiService(double latitude, double longitude) {

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude
				+ "&sensor=true";
		aq().ajax(url, JSONObject.class, this, "addresslocation");
	}

	public void addresslocation(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				GoogleApiEntity obj = new Gson().fromJson(json.toString(), GoogleApiEntity.class);
				onRequest(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onRequest(GoogleApiEntity response) {
		try {
			if (response != null) {
				mVenueEdit.setText("");
				mVenueEdit.setText(response.getResults().get(0).getFormatted_address().toString());
				mSavedSearchFrameView.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRequestSuccess(PlaceRespone obj) {
		if (mArea != null && mArea.size() > 0) {
			mArea.clear();
		}
		if (obj != null) {
			String[] from = new String[] { "description" };
			int[] to = new int[] { R.id.location_txt };
			List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
			mLocalSavedSearch.clear();
			for (Places placeDetail : obj.predictions) {
				HashMap<String, String> place = new HashMap<String, String>();

				place.put("description", placeDetail.getDescription());
				place.put("place_id", placeDetail.getId());
				place.put("reference", placeDetail.getReference());
				placesList.add(place);
				mLocalSavedSearchEntity = new LocalSavedSearchEntity();
				mLocalSavedSearchEntity.setLocation(placeDetail.getDescription());
				mLocalSavedSearch.add(mLocalSavedSearchEntity);
				mArea.add(placeDetail.getDescription());
			}
			AppConstants.from_api = true;
			mSavedSearchAdapter = new ScheduleSavedSearchAdapter(ScheduleMeetingActivity.this,
					R.layout.save_search_custom_adapter_place_list, mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();
		}
		super.onRequestSuccess(obj);

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if (mArea != null) {
				area = mArea.get(position);
				mVenueEdit.setText(area);
			}
		}
	};

	@Override
	public void onResponseError(String errorCode) {

	}

	@Override
	public void onRequestSuccess(PropertyDetailResponse obj) {

	}

	class getLatLong extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {

			String address = params[0];
			return GlobalMethods.getLatlong(address, ScheduleMeetingActivity.this);
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			if (result != null) {
				onLocationFound(result.get(1));
			}
			super.onPostExecute(result);
		}

	}

	private void getCurentDate() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDate = c.get(Calendar.DAY_OF_MONTH);
	}

	private void getCurentTime() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
		currentLocalTime = cal.getTime();
	}

	public void onLocationFound(String latLong) {
		if (latLong != null) {
			String lati = latLong;
			String s[] = lati.split(",");
			for (int i = 0; i < s.length; i++) {
				latitu = s[0];
				Longi = s[1];

				mLatitude = Double.parseDouble(latitu);
				mLongitude = Double.parseDouble(Longi);
			}

		}
	}

	@SuppressWarnings("deprecation")
	private void showDatePicker() {
		showDialog(DATE_DIALOG_ID);
	}

	@SuppressWarnings("deprecation")
	private void showTimePicker() {
		showDialog(TIME_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:

			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog_NoActionBar, datePickerListener, year,
					month, day);
		case TIME_DIALOG_ID:

			final Calendar cal = Calendar.getInstance();
			mHour = cal.get(Calendar.HOUR_OF_DAY);
			mMinute = cal.get(Calendar.MINUTE);
			mSec = cal.get(Calendar.SECOND);
			return new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog_NoActionBar, mTimeSetListener, mHour,
					mMinute, false);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			mTimeTxt.setText(new StringBuilder().append(mHour).append(":").append(mMinute).append(":").append(mSec));

			String time = mTimeTxt.getText().toString();

			SimpleDateFormat timeinputFormat = new SimpleDateFormat(time_input);
			SimpleDateFormat timeoutputFormat = new SimpleDateFormat(time_output);

			Date date1 = null;
			String str1 = null;

			try {
				date1 = timeinputFormat.parse(time);
				str1 = timeoutputFormat.format(date1);
				mTimeTxt.setText(str1);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	};
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			String len = String.valueOf(day);
			if (len.length() == 1) {
				len = "0" + len;
			}
			String month_len = String.valueOf(month + 1);
			if (month_len.length() == 1) {
				month_len = "0" + month_len;
			}

			mDateTxt.setText(new StringBuilder().append(month_len).append("-").append(len).append("-").append(year));
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.accept_txt:
			mIsOnline = "1";
			callAcceptorReject();
			break;
		case R.id.reject_txt:
			mIsOnline = "0";
			callAcceptorReject();
			break;
		case R.id.back_icon:
			SchedulingActivity.isAdded = false;
			if (mCallFrom != null && mCallFrom.equalsIgnoreCase("GCM")) {
				launchActivity(MapFragmentActivity.class);
				overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			} else {
				finish();
				overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			}

			break;
		case R.id.save_btn:

			if (mSave.getText().toString().equals("Save")) {
				validateAllFields();
			} else {
				String mString = mUserID.toString().replace("]", "").replace("[", "");
				String userNames = mUserName.toString().replace("]", "").replace("[", "");
				if (checkYear < mYear) {
					DialogManager.showCustomAlertDialog(mContext, ScheduleMeetingActivity.this,
							"you can't chat now because schedule date is not today");
				} else if (checkMonth < mMonth) {
					DialogManager.showCustomAlertDialog(mContext, ScheduleMeetingActivity.this,
							"you can't chat now because schedule date is not today");
				} else if (checkMonth == mMonth) {
					if (checkDay < mDate) {
						DialogManager.showCustomAlertDialog(mContext, ScheduleMeetingActivity.this,
								"you can't chat now because schedule date is not today");
					}

				} else {
					createGroup(mString, userNames);
				}

			}
			break;
		case R.id.online_btn:
			strStatus = "1";
			mOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_over, 0);
			mOffline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_normal, 0);
			break;
		case R.id.offline_btn:
			strStatus = "0";
			mOffline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_over, 0);
			mOnline.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.listing_radio_normal, 0);
			break;
		case R.id.date_lay:
			showDatePicker();
			break;
		case R.id.time_lay:
			showTimePicker();
			break;
		case R.id.add_friends_lay:
			if (mBundle != null) {
				// if (mUserName.size() != 0) {
				Intent intent = new Intent(this, ScheduleInviteFriendsActivity.class);
				ScheduleMeetingActivity.sch_local_arr.size();
				System.out.println(ScheduleMeetingActivity.sch_local_arr.size() + "");
				if (mUserName != null && mUserName.size() != 0) {
					intent.putExtra("user_name_arr", mUserName);
					intent.putExtra("user_id_arr", mUserID);
				}
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				// }

			} else {
				mUserID.clear();
				mUserName.clear();
				Intent intent = new Intent(this, ScheduleInviteFriendsActivity.class);
				if (mUserName != null && mUserName.size() != 0) {
					intent.putExtra("user_name_arr", mUserName);
					intent.putExtra("user_id_arr", mUserID);
				}
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
		case R.id.add_venue_edit:
			if (fromEdit) {
				mSavedSearchFrameView.setVisibility(View.GONE);
			} else {
				// mSavedSearchFrameView.setVisibility(View.VISIBLE);
				if (mVenueEdit.getText().toString().equals("")) {
					addedDatasTosaveSearch();
				} else {
					callPlaces(mVenueEdit.getText().toString());
				}
			}

			break;
		}
	}

	private void createGroup(String ids, String userNames) {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", ids);
		params.put("name", userNames);
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							System.out.println(json);
						} else {
							statusErrorCode(status);
						}
						ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
						onSuccessRequest(obj);

					}
				});

	}

	protected void onSuccessRequest(ChatSendResponse obj) {
		if (obj.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

			if (mUserID.size() == 1) {
				ids = mUserID.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",", "");
			} else if (mUserID.size() > 1) {
				ids = mUserID.toString().replace("[", "").replace("]", "").replace(" ", "");
			}
			Intent intent = new Intent(ScheduleMeetingActivity.this, FriendChatScreen.class);
			intent.putExtra("ids", ids);
			intent.putExtra("names", obj.username);
			intent.putExtra("groupId", mCommonEntity.getSchedule_id());
			intent.putExtra("type", "schedule");
			startActivity(intent);
			mUserID.clear();
			mUserName.clear();
		}
	}

	private void showFriendsListPopup(ArrayList<String> mFriendsName) {
		mDialog = new Dialog(ScheduleMeetingActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setCancelable(false);
		mDialog.setContentView(R.layout.activity_schedule_invite_friends);

		TextView mHeaderTxt = (TextView) mDialog.findViewById(R.id.header_txt);
		mHeaderTxt.setText("Invited Friends");
		RelativeLayout mInviteLay = (RelativeLayout) mDialog.findViewById(R.id.invite_lay);
		mInviteLay.setVisibility(View.GONE);
		LinearLayout mBackIcon = (LinearLayout) mDialog.findViewById(R.id.back_icon);
		ListView mFriendsList = (ListView) mDialog.findViewById(R.id.schedule_friends_list);
		mBackIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		ArrayAdapter<String> mFriendsAdapter = new ArrayAdapter<String>(this, R.layout.adapter_friends_invited_list,
				R.id.prop_type_text, mFriendsName);
		mFriendsList.setAdapter(mFriendsAdapter);
		mFriendsList.setSelection(0);
		mDialog.show();
	}

	private void validateAllFields() {
		strMeetingSubject = mMeetingEdit.getText().toString().trim();
		strDescription = mDescriotionEdit.getText().toString().trim();
		strDate = mDateTxt.getText().toString().trim();
		strTime = mTimeTxt.getText().toString().trim();
		strVenue = mVenueEdit.getText().toString().trim();

		Date mDate1 = new Date();
		try {
			mDate1 = mDateFormat.parse(strDate);
			strDate = mNewDateFormat.format(mDate1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String mID = mUserID.toString();
		String mID1 = mID.replace("[", "");
		strFriendsUserId = mID1.replace("]", "");

		if (strMeetingSubject.equals("") || strMeetingSubject.length() == 0) {
			DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_meeting));
		} else if (strDescription.equals("") || strDescription.length() == 0) {
			DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_description));
		} else if (strDate.equals("Date") || strDate.length() == 0) {
			DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_select_date));
		} else if (year < mYear || month < mMonth || day < mDate) {
			DialogManager.showCustomAlertDialog(this, this, "The selected date is already existed select another date");
			mDateTxt.setText("Date");
		} else if (strTime.equals("Time") || strTime.length() == 0) {
			DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_select_time));
		} else {
			if (mBundle != null) {
				callUpdateSchedule();
			} else {
				callAddNewSchedule();
			}

		}

	}

	private void callAddNewSchedule() {

		SimpleDateFormat inputFormat = new SimpleDateFormat(rev_inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(rev_outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(mDateTxt.getText().toString());
			str = outputFormat.format(date);
			// mDateTxt.setText(str);
			strDate = str;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat timeinputFormat = new SimpleDateFormat(rev_time_input);
		SimpleDateFormat timeoutputFormat = new SimpleDateFormat(rev_time_output);

		Date date1 = null;
		String str1 = null;

		try {
			date1 = timeinputFormat.parse(mTimeTxt.getText().toString());
			str1 = timeoutputFormat.format(date1);
			// mTimeTxt.setText(str1);
			strTime = str1;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String sd = GlobalMethods.dateFormateNewFormat(mContext, strDate + " " + strTime);
		String date_time[] = sd.split(" ");
		String dat = date_time[0];
		String tim = date_time[1];

		String Url = AppConstants.BASE_URL + "schedulemeeting";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("meeting_subject", strMeetingSubject);
		params.put("description", strDescription);
		params.put("date", dat);
		params.put("time", tim);
		params.put("venue", strVenue);
		params.put("invite_user_ids", strFriendsUserId);
		params.put("status", strStatus);
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);
						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mAlert = "CallApi";
								DialogManager.showCustomAlertDialog(ScheduleMeetingActivity.this,
										ScheduleMeetingActivity.this, mResponse.getMsg());
							}
						} else {
							statusErrorCode(status);
						}

					}

				});
	}

	private void callUpdateSchedule() {

		SimpleDateFormat inputFormat = new SimpleDateFormat(rev_inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(rev_outputPattern);

		Date date = null;
		String str = null;

		try {
			date = inputFormat.parse(mDateTxt.getText().toString());
			str = outputFormat.format(date);
			// mDateTxt.setText(str);
			strDate = str;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat timeinputFormat = new SimpleDateFormat(rev_time_input);
		SimpleDateFormat timeoutputFormat = new SimpleDateFormat(rev_time_output);

		Date date1 = null;
		String str1 = null;

		try {
			date1 = timeinputFormat.parse(mTimeTxt.getText().toString());
			str1 = timeoutputFormat.format(date1);
			// mTimeTxt.setText(str1);
			strTime = str1;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String sd = GlobalMethods.dateFormateNewFormat(mContext, strDate + " " + strTime);
		String date_time[] = sd.split(" ");
		String dat = date_time[0];
		String tim = date_time[1];

		ArrayList<String> ids_friend = new ArrayList<String>();
		for (int i = 0; i < fr_det_arr.size(); i++) {
			ids_friend.add(fr_det_arr.get(i).getUser_friend_id());
		}

		String friends_ids = TextUtils.join(",", ids_friend);

		String Url = AppConstants.BASE_URL + "updatemeeting";
		GsonTransformer t = new GsonTransformer();
		// UserID = "3";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("meeting_subject", strMeetingSubject);
		params.put("schedule_id", strScheduleId);
		params.put("description", strDescription);
		params.put("date", dat);
		params.put("time", tim);
		params.put("venue", strVenue);
		params.put("invite_user_ids", friends_ids);
		params.put("status", strStatus);
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);
						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mAlert = "CallApi";
								DialogManager.showCustomAlertDialog(ScheduleMeetingActivity.this,
										ScheduleMeetingActivity.this, mResponse.getMsg());
							}
						} else {
							statusErrorCode(status);
						}

					}

				});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void getSchedulingList() {

		String Url = AppConstants.BASE_URL + "getschedule";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						super.callback(url, json, status);
						if (json != null) {
							ScheduleResponse mResponse = new Gson().fromJson(json.toString(), ScheduleResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

								setSchedulingAdapter(mResponse);

							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

	private void setSchedulingAdapter(ScheduleResponse mResponse) {
		mScheduleCommonEntityList = new ArrayList<ScheduleCommonEntity>();
		for (int i = 0; i < mResponse.getResult().getFriend_schedule().size(); i++) {
			mScheduleCommonEntity = new ScheduleCommonEntity();
			mScheduleCommonEntity.setMeeting_id(mResponse.getResult().getFriend_schedule().get(i).getMeeting_id());
			mScheduleCommonEntity
					.setScheduler_user_id(mResponse.getResult().getFriend_schedule().get(i).getScheduler_user_id());
			mScheduleCommonEntity.setSchedule_id(mResponse.getResult().getFriend_schedule().get(i).getSchedule_id());
			mScheduleCommonEntity.setUser_id(mResponse.getResult().getFriend_schedule().get(i).getUser_id());
			mScheduleCommonEntity.setStatus(mResponse.getResult().getFriend_schedule().get(i).getStatus());
			mScheduleCommonEntity.setFirst_name(mResponse.getResult().getFriend_schedule().get(i).getFirst_name());
			mScheduleCommonEntity.setUser_name(mResponse.getResult().getFriend_schedule().get(i).getUser_name());
			mScheduleCommonEntity.setUser_pic(mResponse.getResult().getFriend_schedule().get(i).getUser_pic());
			mScheduleCommonEntity
					.setMeeting_subject(mResponse.getResult().getFriend_schedule().get(i).getMeeting_subject());
			mScheduleCommonEntity.setDescription(mResponse.getResult().getFriend_schedule().get(i).getDescription());
			mScheduleCommonEntity.setFriends(mResponse.getResult().getFriend_schedule().get(i).getFriends());
			mScheduleCommonEntity.setDate(mResponse.getResult().getFriend_schedule().get(i).getDate());
			mScheduleCommonEntity.setTime(mResponse.getResult().getFriend_schedule().get(i).getTime());
			mScheduleCommonEntity.setIs_friends_schedule("1");
			mScheduleCommonEntity.setIs_my_schedule("0");
			mScheduleCommonEntity.setIs_Accepted_schedule("0");
			mScheduleCommonEntityList.add(mScheduleCommonEntity);

		}
		for (int i = 0; i < mResponse.getResult().getMyschedule().size(); i++) {
			mScheduleCommonEntity = new ScheduleCommonEntity();
			mScheduleCommonEntity.setMeeting_id(mResponse.getResult().getMyschedule().get(i).getMeeting_id());
			mScheduleCommonEntity
					.setScheduler_user_id(mResponse.getResult().getMyschedule().get(i).getScheduler_user_id());
			mScheduleCommonEntity.setSchedule_id(mResponse.getResult().getMyschedule().get(i).getSchedule_id());
			mScheduleCommonEntity.setUser_id(mResponse.getResult().getMyschedule().get(i).getUser_id());
			mScheduleCommonEntity.setStatus(mResponse.getResult().getMyschedule().get(i).getStatus());
			mScheduleCommonEntity.setFirst_name(mResponse.getResult().getMyschedule().get(i).getFirst_name());

			mScheduleCommonEntity.setUser_name(mResponse.getResult().getMyschedule().get(i).getUser_name());

			mScheduleCommonEntity.setUser_pic(mResponse.getResult().getMyschedule().get(i).getUser_pic());
			mScheduleCommonEntity.setMeeting_subject(mResponse.getResult().getMyschedule().get(i).getMeeting_subject());
			mScheduleCommonEntity.setDescription(mResponse.getResult().getMyschedule().get(i).getDescription());
			mScheduleCommonEntity.setFriends(mResponse.getResult().getMyschedule().get(i).getFriends());
			mScheduleCommonEntity.setDate(mResponse.getResult().getMyschedule().get(i).getDate());
			mScheduleCommonEntity.setTime(mResponse.getResult().getMyschedule().get(i).getTime());
			mScheduleCommonEntity.setVenue(mResponse.getResult().getMyschedule().get(i).getVenue());
			mScheduleCommonEntity.setIsonline(mResponse.getResult().getMyschedule().get(i).getIsonline());
			mScheduleCommonEntity.setIs_friends_schedule("0");
			mScheduleCommonEntity.setIs_my_schedule("1");
			mScheduleCommonEntity.setIs_Accepted_schedule("0");
			mScheduleCommonEntityList.add(mScheduleCommonEntity);

		}
		for (int i = 0; i < mResponse.getResult().getAccepted_schedule().size(); i++) {
			mScheduleCommonEntity = new ScheduleCommonEntity();
			mScheduleCommonEntity.setMeeting_id(mResponse.getResult().getAccepted_schedule().get(i).getMeeting_id());
			mScheduleCommonEntity
					.setScheduler_user_id(mResponse.getResult().getAccepted_schedule().get(i).getScheduler_user_id());
			mScheduleCommonEntity.setSchedule_id(mResponse.getResult().getAccepted_schedule().get(i).getSchedule_id());
			mScheduleCommonEntity.setUser_id(mResponse.getResult().getAccepted_schedule().get(i).getUser_id());
			mScheduleCommonEntity.setStatus(mResponse.getResult().getAccepted_schedule().get(i).getStatus());
			mScheduleCommonEntity.setFirst_name(mResponse.getResult().getAccepted_schedule().get(i).getFirst_name());

			mScheduleCommonEntity.setUser_name(mResponse.getResult().getAccepted_schedule().get(i).getUser_name());

			mScheduleCommonEntity.setUser_pic(mResponse.getResult().getAccepted_schedule().get(i).getUser_pic());
			mScheduleCommonEntity
					.setMeeting_subject(mResponse.getResult().getAccepted_schedule().get(i).getMeeting_subject());
			mScheduleCommonEntity.setDescription(mResponse.getResult().getAccepted_schedule().get(i).getDescription());
			mScheduleCommonEntity.setFriends(mResponse.getResult().getAccepted_schedule().get(i).getFriends());
			mScheduleCommonEntity.setDate(mResponse.getResult().getAccepted_schedule().get(i).getDate());
			mScheduleCommonEntity.setTime(mResponse.getResult().getAccepted_schedule().get(i).getTime());
			mScheduleCommonEntity.setIs_friends_schedule("0");
			mScheduleCommonEntity.setIs_my_schedule("0");
			mScheduleCommonEntity.setIs_Accepted_schedule("1");
			mScheduleCommonEntityList.add(mScheduleCommonEntity);
		}

		Collections.sort(mScheduleCommonEntityList, ScheduleCommonEntity.SCHEDULE_DATE_SORT);
		Collections.sort(mScheduleCommonEntityList, ScheduleCommonEntity.SCHEDULE_TIME_SORT);
		for (int i = 0; i < mScheduleCommonEntityList.size(); i++) {
			if (mScheduleID.equalsIgnoreCase(mScheduleCommonEntityList.get(i).getSchedule_id())) {
				mScheduleID1 = mScheduleCommonEntityList.get(i).getSchedule_id();
				mCommonEntity1 = mScheduleCommonEntityList.get(i);
			}
		}
		if (mScheduleID.equalsIgnoreCase(mScheduleID1)) {
			setApiValues(mCommonEntity1);
		} else {
			finish();
		}

	}

	@Override
	public void onOkclick() {
		if (mAlert.equalsIgnoreCase("CallApi")) {
			AppConstants.isAPI = true;
			finish();
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
		} else {
			/**
			 * Close Dialog
			 */
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SchedulingActivity.isAdded = false;
	}

	// public String parseDateToddMMyyyy(String time) {
	// String inputPattern = "yyyy-MM-dd HH:mm:ss";
	// String outputPattern = "dd-MMM-yyyy h:mm a";
	// SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
	// SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
	//
	// Date date = null;
	// String str = null;
	//
	// try {
	// date = inputFormat.parse(time);
	// str = outputFormat.format(date);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return str;
	// }
}

package com.smaat.renterblock.findagent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.ui.SplashScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GPSTracker;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import eu.erikw.PullToRefreshListView;

public class FindAgentActivity extends BaseActivity implements OnClickListener,DialogMangerCallback,PullToRefreshListView.OnRefreshListener {

	/**
	 * Slide Menu Declaration
	 */
	private TextView mUserName;
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType, UserName;

	/**
	 * FindAgent View's Declaration
	 */

	private ListView mAgentList;
	public PullToRefreshListView mPullToRefreshListView;
	private FindAgentAdapter mAgentAdapter;
	private TextView mPlaceText, mResultText;
	Typeface mTypeface;
	private ArrayList<AgentFilterResultEntity> mResult;
	private ArrayList<AgentFilterResultEntity> mResultNew;
	private int mStartIndex = 0;
	private int mLimit = 10;
	private TextView header_txt;
	private int review_count = 0;

	static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_agent);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypeface);
		setupUI(root);
		mContext = FindAgentActivity.this;
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(mTypefaceBold);
		initComponents();
		setGoogleAnalytics(this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			if (SplashScreen.mAgentFilterLocalEntity.getLocation() != null
					&& SplashScreen.mAgentFilterLocalEntity.getLocation()
							.equalsIgnoreCase("")) {
				showCurrentLocation();
			} else {
				getFilterAgent();
			}
		}

	}

	private void showCurrentLocation() {
		GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation() == false) {
			tracker.showSettingsAlert();
		} else {
			double latitude = tracker.getLatitude();
			double longitude = tracker.getLongitude();
			SplashScreen.mAgentFilterLocalEntity.setLatitude(String
					.valueOf(latitude));
			SplashScreen.mAgentFilterLocalEntity.setLongitude(String
					.valueOf(longitude));

			callGoogleApiService(latitude, longitude);
		}
	}

	private void callGoogleApiService(double latitude, double longitude) {

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ latitude + "," + longitude + "&sensor=true";
		aq().progress(DialogManager.getProgressDialog(this)).ajax(url,
				JSONObject.class, this, "addresslocation");
	}

	public void addresslocation(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				GoogleApiEntity obj = new Gson().fromJson(json.toString(),
						GoogleApiEntity.class);
				onRequest(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onRequest(GoogleApiEntity response) {
		try {
			if (response != null) {
				SplashScreen.mAgentFilterLocalEntity.setLocation(response
						.getResults().get(0).getFormatted_address().toString());
				getFilterAgent();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {

		/**
		 * FindAgent View's Intialization
		 */

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.agent_list);
		//mPullToRefreshListView.setMode(Mode.PULL_UP_TO_REFRESH);

		mPullToRefreshListView.setOnRefreshListener(this);
		//mAgentList = mPullToRefreshListView.getRefreshableView();

		mPlaceText = (TextView) findViewById(R.id.place_txt);
		mResultText = (TextView) findViewById(R.id.result_count_txt);
		mResultText.setTypeface(mTypeface);
		/**
		 * Slide Menu Intialization
		 */
		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);
		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_find_agent);
			AppConstants.view_id = R.id.buy_find_agent;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_find_agent);
			AppConstants.view_id = R.id.sell_find_agent;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_find_agent);
			AppConstants.view_id = R.id.agent_find_agent;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_find_agent);
			AppConstants.view_id = R.id.buy_find_agent;
		}
		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

//		mAgentList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
//					long arg3) {
//
//				Intent intent = new Intent(FindAgentActivity.this,
//						FindAgentDetailsActivity.class);
//				intent.putExtra("mUserID", mResult.get(pos - 1).getUser_id());
//				startActivity(intent);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}
//		});
	}

	private void setAdapter() {

		mAgentAdapter = new FindAgentAdapter(this, mResult);
//		mAgentList.setAdapter(mAgentAdapter);
//        mAgentList.setSelection(mStartIndex);
        mPullToRefreshListView.setAdapter(mAgentAdapter);
        mPullToRefreshListView.setSelection(mStartIndex);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_icon:
			slide_holder.toggle();
			break;

		case R.id.filter_icon:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				Intent intent = new Intent(this, AgentFilterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			break;
		}
	}

	private void getFilterAgent() {

		String Url = AppConstants.BASE_URL + "agentfilter";

		GsonTransformer transformer = new GsonTransformer();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("name", SplashScreen.mAgentFilterLocalEntity.getName());
		params.put("location",
				SplashScreen.mAgentFilterLocalEntity.getLocation());
		params.put("type", SplashScreen.mAgentFilterLocalEntity.getType());
		params.put("price_range_min",
				SplashScreen.mAgentFilterLocalEntity.getPrice_range_min());
		params.put("price_range_max",
				SplashScreen.mAgentFilterLocalEntity.getPrice_range_max());
		params.put("property_experties",
				SplashScreen.mAgentFilterLocalEntity.getProperty_experties());
		params.put("limit", String.valueOf(mLimit));
		params.put("start", String.valueOf(mStartIndex));
		params.put("latitude",
				SplashScreen.mAgentFilterLocalEntity.getLatitude());
		params.put("longitude",
				SplashScreen.mAgentFilterLocalEntity.getLongitude());
		aq().transformer(transformer)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									FindAgentFilterResonpse mResponse = new Gson().fromJson(
											json.toString(),
											FindAgentFilterResonpse.class);
									onSuccessAgentResponse(mResponse);
								} else {
									statusErrorCode(status);
								}
							}

						});

	}

	protected void onSuccessAgentResponse(FindAgentFilterResonpse mResponse) {

		if (mResponse.getError_code().equalsIgnoreCase(
				AppConstants.SUCCESS_CODE)) {
			mPlaceText.setText(mResponse.getLocation());
			int res_cou = mResponse.getResult().size();
			review_count = review_count + res_cou;
			mResultText.setText(mResponse.getResult().size() + " Results");

			mPullToRefreshListView.onRefreshComplete();

			if (mStartIndex == 0) {
				mResult = mResponse.getResult();

			} else {
				mResultNew = mResponse.getResult();
				for (AgentFilterResultEntity mAgentFilterResultEntity : mResultNew) {
					mResult.add(mAgentFilterResultEntity);
				}
			}
			setAdapter();

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
			mStartIndex = 0;
			getFilterAgent();
		}

		super.onResume();
	}

	/*@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

		if (mResult != null) {
			mStartIndex += mResult.size();
		} else {
			mStartIndex = 0;
		}
		getFilterAgent();

	}*/

	@Override
	public void onOkclick() {
		/**
		 * Close Dialog
		 */
	}

	public static void showMessageMailer(String email_id) {
		((FindAgentActivity) mContext).showMailerDialog(email_id);
	}

	private void showMailerDialog(String email_id) {
		String body = "<HTML><Body>Hi, I saw your available listings "
				+ "on Renter's Block and would like to learn more about "
				+ "a specific listing. Please <a href=\"goo.gl/DGDC1v\">LOGIN!</a> "
				+ "and feel free to message me through the app or website. Looking forward to connecting with you!</BODY></HTML>";
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email_id });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Hot Lead from Renter's Block");
		emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
		emailIntent.setType("text/plain");
		startActivity(emailIntent);

	}

	@Override
	public void onRefresh() {
		if (mResult != null) {
			mStartIndex += mResult.size();
		} else {
			mStartIndex = 0;
		}
		getFilterAgent();
	}
}

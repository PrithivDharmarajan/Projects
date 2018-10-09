package com.smaat.renterblock.alerts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.PropertyDetailResponse;
import com.smaat.renterblock.savedsearch.LocalSavedSearchEntity;
import com.smaat.renterblock.sqlite.LocalSavedSearch;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.FilterActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.ui.SplashScreen;
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

public class AlertsActivity extends BaseActivity implements OnClickListener,
		DialogMangerCallback, WebserviceCallbackInterface {

	/**
	 * Alerts Declaration
	 */
	private LinearLayout mAlertsLayout;
	private RelativeLayout mEditDeleteView;
	private Button mEditBtn;
	private ArrayList<AlertsEntity> mAlertsEntityList;
	private AlertsEntity mAlertsEntity;
	private ArrayList<Button> mCheckBtnList = new ArrayList<Button>();
	private ArrayList<String> mAlertIDS = new ArrayList<String>();
	private String mAlert = "Alert", mAlertID = "";
	private boolean isShown = false;
	private String mSplitedAmountMin, mSplitedAmountMax, mType;

	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private TextView header_txt;
	private Dialog mDialog;
	private String latitu;
	private String Longi;
	private double mLatitude = 0;
	private double mLongitude = 0;
	private ListView mSavedSearchList;
	static EditText mLocation;
	static Context mContext;
	static FrameLayout mSavedSearchFrameView;

	private ArrayList<LocalSavedSearchEntity> mLocalSavedSearch = new ArrayList<LocalSavedSearchEntity>();
	private AlertsSavedSearchAdapter mSavedSearchAdapter;
	private LocalSavedSearchEntity mLocalSavedSearchEntity;
	private ArrayList<String> mArea = new ArrayList<String>();
	private String area;
	private String alert_loc_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alerts);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelvetica(
				this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		mContext = AlertsActivity.this;
		initComponents();
		setGoogleAnalytics(this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			getAlerts();
		}

	}

	private void initComponents() {

		/**
		 * Alerts Initialization
		 */
		mAlertsLayout = (LinearLayout) findViewById(R.id.alerts_layout);
		mEditDeleteView = (RelativeLayout) findViewById(R.id.edit_delete_view);
		mEditBtn = (Button) findViewById(R.id.edit_btn);
		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(helvetica_bold);
		/**
		 * Slide Menu Initialization
		 */
		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

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
			setBuyBackground(R.id.buy_alerts);
			AppConstants.view_id = R.id.buy_alerts;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_alerts);
			AppConstants.view_id = R.id.sell_alerts;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_alerts);
			AppConstants.view_id = R.id.agent_alerts;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_alerts);
			AppConstants.view_id = R.id.buy_alerts;
		}
		slide_holder.setAllowInterceptTouch(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_icon:
			slide_holder.toggle();
			break;
		case R.id.edit_button:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mAlertsEntityList.size() == 0) {
					DialogManager.showCustomAlertDialog(this, this,
							"No Alerts are available!");
				} else {
					if (!isShown) {
						AppConstants.IS_EDIT = "true";
						mEditDeleteView.setVisibility(View.VISIBLE);
						mAlertIDS.clear();
						mEditBtn.setBackgroundResource(R.drawable.close_icon);
						setAlertsAdapter(mAlertsEntityList);
						isShown = true;
					} else {
						AppConstants.IS_EDIT = "false";
						mEditDeleteView.setVisibility(View.GONE);
						mAlertIDS.clear();
						mEditBtn.setBackgroundResource(R.drawable.edit_button);
						setAlertsAdapter(mAlertsEntityList);
						isShown = false;
					}
				}
			}
			break;
		case R.id.add_icon:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				showLocationDialog("");
			}
			break;
		case R.id.edit_icon:
			if (mAlertID.equals("") || mAlertID == null
					|| mAlertIDS.size() == 0) {
				DialogManager.showCustomAlertDialog(this, this,
						"Select any alert from list");
			} else if (mAlertIDS.size() > 1) {
				DialogManager.showCustomAlertDialog(this, this,
						"Select only one alert from list");
			} else {
				setLocalFilterDb();
				try {
					mLatitude = Double.valueOf(mAlertsEntity.getLatitude());
					mLongitude = Double.valueOf(mAlertsEntity.getLongitude());
				} catch (Exception e) {
					// TODO: handle exception
					getcurrLocation();
				}
				showLocationDialog(mAlertsEntity.getLocation());
			}
			break;
		case R.id.delete_icon:
			if (mAlertID.equals("") || mAlertID == null
					|| mAlertIDS.size() == 0) {
				DialogManager.showCustomAlertDialog(this, this,
						"Select any alert from list");
			} else {
				deleteAlerts();
			}
			break;
		}

	}

	private void getcurrLocation() {
		GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation() == false) {
			tracker.showSettingsAlert();
		} else {
			mLatitude = tracker.getLatitude();
			mLongitude = tracker.getLongitude();
		}
	}

	private void showLocationDialog(final String location) {
		mDialog = new Dialog(AlertsActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.save_search_dialog);
		mDialog.setCancelable(false);
		final RelativeLayout parent_layout = (RelativeLayout) mDialog
				.findViewById(R.id.parent_layout);
		Button cancel = (Button) mDialog.findViewById(R.id.cancel);
		Button save = (Button) mDialog.findViewById(R.id.save);
		TextView text1 = (TextView) mDialog.findViewById(R.id.text1);
		TextView text2 = (TextView) mDialog.findViewById(R.id.text2);
		mSavedSearchFrameView = (FrameLayout) mDialog
				.findViewById(R.id.save_search_frame_view);
		mSavedSearchList = (ListView) mDialog
				.findViewById(R.id.saved_search_list);

		text2.setVisibility(View.INVISIBLE);
		text1.setText("Please enter your alert location");

		mLocation = (EditText) mDialog.findViewById(R.id.enter_search_name);
		mLocation.setHint("location");

		mLocation.setText(location);

		mLocation.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				mSavedSearchFrameView.setVisibility(View.GONE);
				hideSoftKeyboard(AlertsActivity.this);
				return false;
			}
		});

		parent_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(parent_layout.getWindowToken(), 0);
				mSavedSearchFrameView.setVisibility(View.GONE);
			}
		});

		mLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSavedSearchFrameView.setVisibility(View.VISIBLE);
				hideSoftKeyboard(AlertsActivity.this);
			}
		});

		mLocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (mLocation.getText().toString().equals("")) {
					addedDatasTosaveSearch();
				} else {
					callPlaces(s.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLatitude == 0 || mLongitude == 0) {
					DialogManager.showCustomAlertDialog(mContext,
							AlertsActivity.this,
							"Please enter the valid location");
				} else {
					mDialog.dismiss();
					if (location.equalsIgnoreCase("")) {
						Intent intent = new Intent(AlertsActivity.this,
								FilterActivity.class);
						intent.putExtra("FROM", "Alerts");
						intent.putExtra("AlertID", "");
						intent.putExtra("Latitude", String.valueOf(mLatitude));
						intent.putExtra("Longitude", String.valueOf(mLongitude));
						intent.putExtra("Location", mLocation.getText()
								.toString());
						intent.putExtra("alert_name", "");
						startActivity(intent);
					} else {
						mAlertIDS.clear();
						Intent intent1 = new Intent(AlertsActivity.this,
								FilterActivity.class);
						intent1.putExtra("FROM", "Alerts");
						intent1.putExtra("AlertID", mAlertID);
						intent1.putExtra("Latitude",
								SplashScreen.mLocaleRentFilterObjectEntity
										.getLatitude());
						intent1.putExtra("Longitude",
								SplashScreen.mLocaleRentFilterObjectEntity
										.getLongitude());
						intent1.putExtra("Location", mLocation.getText()
								.toString());
						intent1.putExtra("alert_name",
								mAlertsEntity.getAlert_name());
						startActivity(intent1);
					}
				}

			}
		});

		mDialog.show();
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

			mSavedSearchAdapter = new AlertsSavedSearchAdapter(
					AlertsActivity.this,
					R.layout.save_search_custom_adapter_place_list,
					mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();

		} else {

			mLocalSavedSearch = LocalSavedSearch
					.saveSearch(AlertsActivity.this);
			AppConstants.from_api = false;

			mSavedSearchAdapter = new AlertsSavedSearchAdapter(
					AlertsActivity.this,
					R.layout.save_search_custom_adapter_place_list,
					mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();
		}
	}

	private void callPlaces(String place) {

		ServieRequestHandler.getInstance().getResultPlaces(
				getURL(AppConstants.PLACES_API_BASE, place), aq(),
				AlertsActivity.this, null, null, this);
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
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
				+ output + "?" + parameters;
		return url;
	}

	public static void callPlacesApi(String location) {
		mLocation.setText(location);
		((AlertsActivity) mContext).new getLatLong().execute(location);
		mSavedSearchFrameView.setVisibility(View.GONE);

	}

	public static void setCurrentLocationDetails() {
		((AlertsActivity) mContext).showCurrentLocation();

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

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ latitude + "," + longitude + "&sensor=true";
		aq().ajax(url, JSONObject.class, this, "addresslocation");
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
				mLocation.setText("");
				mLocation.setText(response.getResults().get(0)
						.getFormatted_address().toString());
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
			int[] to = new int[] { R.id.enter_search_name };
			List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
			mLocalSavedSearch.clear();
			for (Places placeDetail : obj.predictions) {
				HashMap<String, String> place = new HashMap<String, String>();

				place.put("description", placeDetail.getDescription());
				place.put("place_id", placeDetail.getId());
				place.put("reference", placeDetail.getReference());
				placesList.add(place);
				mLocalSavedSearchEntity = new LocalSavedSearchEntity();
				mLocalSavedSearchEntity.setLocation(placeDetail
						.getDescription());
				mLocalSavedSearch.add(mLocalSavedSearchEntity);
				mArea.add(placeDetail.getDescription());
			}
			AppConstants.from_api = true;
			mSavedSearchAdapter = new AlertsSavedSearchAdapter(
					AlertsActivity.this,
					R.layout.save_search_custom_adapter_place_list,
					mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();
		}
		super.onRequestSuccess(obj);

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			if (mArea != null) {
				area = mArea.get(position);
				mLocation.setText(area);
			}
		}
	};

	@Override
	public void onResponseError(String errorCode) {

	}

	class getLatLong extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {

			String address = params[0];
			return GlobalMethods.getLatlong(address, AlertsActivity.this);
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			if (result != null) {
				onLocationFound(result.get(1));
			}
			super.onPostExecute(result);
		}

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

	private void setLocalFilterDb() {

		if (mAlertsEntity.getType().equalsIgnoreCase("Rent")) {
			AppConstants.type_of_property_filter = "rent";

			SplashScreen.mLocaleRentFilterObjectEntity
					.setPrice_range_min(mAlertsEntity.getPrice_range_min());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setPrice_range_max(mAlertsEntity.getPrice_range_max());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setProperty_type(mAlertsEntity.getProperty_type());
			SplashScreen.mLocaleRentFilterObjectEntity.setBeds(mAlertsEntity
					.getBeds());
			SplashScreen.mLocaleRentFilterObjectEntity.setBaths(mAlertsEntity
					.getBaths());
			SplashScreen.mLocaleRentFilterObjectEntity.setNo_fee(mAlertsEntity
					.getNo_fee());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setSquare_footage_min(mAlertsEntity
							.getSquare_footage_min());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setSquare_footage_max(mAlertsEntity
							.getSquare_footage_max());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setYear_build_min(mAlertsEntity.getYear_build_min());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setYear_build_max(mAlertsEntity.getYear_build_max());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setLot_size(mAlertsEntity.getLot_size());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setDays_on_RB(mAlertsEntity.getDays_on_RB());
			SplashScreen.mLocaleRentFilterObjectEntity.setResale(mAlertsEntity
					.getResale());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setNew_construction(mAlertsEntity.getNew_construction());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setFore_closure(mAlertsEntity.getFore_closure());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setOpen_house(mAlertsEntity.getOpen_house());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setReduced_prices(mAlertsEntity.getReduced_prices());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setKeywords(mAlertsEntity.getKeywords());
			SplashScreen.mLocaleRentFilterObjectEntity.setMLS(mAlertsEntity
					.getMLS());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setSold_within(mAlertsEntity.getSold_within());

			SplashScreen.mLocaleRentFilterObjectEntity
					.setLocation(mAlertsEntity.getLocation());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setLatitude(mAlertsEntity.getLatitude());
			SplashScreen.mLocaleRentFilterObjectEntity
					.setLongitude(mAlertsEntity.getLongitude());

		} else if (mAlertsEntity.getType().equalsIgnoreCase("Sold")) {

			AppConstants.type_of_property_filter = "sold";

			SplashScreen.mLocaleSoldFilterObjectEntity
					.setPrice_range_min(mAlertsEntity.getPrice_range_min());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setPrice_range_max(mAlertsEntity.getPrice_range_max());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setProperty_type(mAlertsEntity.getProperty_type());
			SplashScreen.mLocaleSoldFilterObjectEntity.setBeds(mAlertsEntity
					.getBeds());
			SplashScreen.mLocaleSoldFilterObjectEntity.setBaths(mAlertsEntity
					.getBaths());
			SplashScreen.mLocaleSoldFilterObjectEntity.setNo_fee(mAlertsEntity
					.getNo_fee());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setSquare_footage_min(mAlertsEntity
							.getSquare_footage_min());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setSquare_footage_max(mAlertsEntity
							.getSquare_footage_max());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setYear_build_min(mAlertsEntity.getYear_build_min());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setYear_build_max(mAlertsEntity.getYear_build_max());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setLot_size(mAlertsEntity.getLot_size());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setDays_on_RB(mAlertsEntity.getDays_on_RB());
			SplashScreen.mLocaleSoldFilterObjectEntity.setResale(mAlertsEntity
					.getResale());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setNew_construction(mAlertsEntity.getNew_construction());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setFore_closure(mAlertsEntity.getFore_closure());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setOpen_house(mAlertsEntity.getOpen_house());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setReduced_prices(mAlertsEntity.getReduced_prices());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setKeywords(mAlertsEntity.getKeywords());
			SplashScreen.mLocaleSoldFilterObjectEntity.setMLS(mAlertsEntity
					.getMLS());
			SplashScreen.mLocaleSoldFilterObjectEntity
					.setSold_within(mAlertsEntity.getSold_within());

			// SplashScreen.mLocaleSoldFilterObjectEntity
			// .setLocation(mAlertsEntity.getLocation());
			// SplashScreen.mLocaleSoldFilterObjectEntity
			// .setLatitude(mAlertsEntity.getLatitude());
			// SplashScreen.mLocaleSoldFilterObjectEntity
			// .setLongitude(mAlertsEntity.getLongitude());

		} else if (mAlertsEntity.getType().equalsIgnoreCase("Sale")) {

			AppConstants.type_of_property_filter = "sale";

			SplashScreen.mLocaleSellFilterObjectEntity
					.setPrice_range_min(mAlertsEntity.getPrice_range_min());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setPrice_range_max(mAlertsEntity.getPrice_range_max());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setProperty_type(mAlertsEntity.getProperty_type());
			SplashScreen.mLocaleSellFilterObjectEntity.setBeds(mAlertsEntity
					.getBeds());
			SplashScreen.mLocaleSellFilterObjectEntity.setBaths(mAlertsEntity
					.getBaths());
			SplashScreen.mLocaleSellFilterObjectEntity.setNo_fee(mAlertsEntity
					.getNo_fee());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setSquare_footage_min(mAlertsEntity
							.getSquare_footage_min());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setSquare_footage_max(mAlertsEntity
							.getSquare_footage_max());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setYear_build_min(mAlertsEntity.getYear_build_min());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setYear_build_max(mAlertsEntity.getYear_build_max());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setLot_size(mAlertsEntity.getLot_size());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setDays_on_RB(mAlertsEntity.getDays_on_RB());
			SplashScreen.mLocaleSellFilterObjectEntity.setResale(mAlertsEntity
					.getResale());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setNew_construction(mAlertsEntity.getNew_construction());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setFore_closure(mAlertsEntity.getFore_closure());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setOpen_house(mAlertsEntity.getOpen_house());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setReduced_prices(mAlertsEntity.getReduced_prices());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setKeywords(mAlertsEntity.getKeywords());
			SplashScreen.mLocaleSellFilterObjectEntity.setMLS(mAlertsEntity
					.getMLS());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setSold_within(mAlertsEntity.getSold_within());

			SplashScreen.mLocaleSellFilterObjectEntity
					.setLocation(mAlertsEntity.getLocation());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setLatitude(mAlertsEntity.getLatitude());
			SplashScreen.mLocaleSellFilterObjectEntity
					.setLongitude(mAlertsEntity.getLongitude());
		}

	}

	private void getAlerts() {

		String Url = AppConstants.BASE_URL + "alert/get";
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

								if (json != null) {
									AlertsResponse mResponse = new Gson()
											.fromJson(json.toString(),
													AlertsResponse.class);
									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mAlertsEntityList = mResponse
												.getResult();
										AppConstants.IS_EDIT = "false";
										mEditDeleteView
												.setVisibility(View.GONE);
										mEditBtn.setBackgroundResource(R.drawable.edit_button);
										isShown = false;

										setAlertsAdapter(mAlertsEntityList);
									}

								} else {
									statusErrorCode(status);
								}
							}

						});
	}

	private void setAlertsAdapter(
			final ArrayList<AlertsEntity> mAlertsEntityList) {

		mAlertsLayout.removeAllViews();

		if (mAlertsEntityList != null && mAlertsEntityList.size() > 0) {
			for (int i = 0; i < mAlertsEntityList.size(); i++) {

				LayoutInflater inflater = (LayoutInflater) this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View convertView = inflater.inflate(
						R.layout.adapter_alerts, null);
				Typeface mTypefaceBold = TypefaceSingleton.getInstance()
						.getHelveticaBold(this);
				Typeface mTypeface = TypefaceSingleton.getInstance()
						.getHelvetica(this);

				final Button mCheckBtn = (Button) convertView
						.findViewById(R.id.check_btn);

				TextView count_text = (TextView) convertView
						.findViewById(R.id.alerts_count_txt);
				count_text.setTypeface(mTypefaceBold);

				count_text.setText(mAlertsEntityList.get(i).getCount());

				RelativeLayout mMainLay = (RelativeLayout) convertView
						.findViewById(R.id.main_lay);
				TextView mAlertName = (TextView) convertView
						.findViewById(R.id.name_txt);
				mAlertName.setTypeface(mTypefaceBold);
				TextView mObjectText = (TextView) convertView
						.findViewById(R.id.other_info_txt);
				mObjectText.setTypeface(mTypeface);
				// mObjectText.setTypeface(mTypeface);
				LinearLayout mCheckLay = (LinearLayout) convertView
						.findViewById(R.id.alerts_check_lay);

				if (AppConstants.IS_EDIT.equals("true")) {
					mCheckLay.setVisibility(View.VISIBLE);
					mMainLay.setClickable(true);
				} else {
					mCheckLay.setVisibility(View.GONE);
					mMainLay.setClickable(true);
				}

				mAlertName.setText(mAlertsEntityList.get(i).getAlert_name().toLowerCase());

				String beds = "";
				String baths = "";
				if (mAlertsEntityList.get(i).getBeds() != null
						&& !mAlertsEntityList.get(i).getBeds().equals("")) {
					beds = mAlertsEntityList.get(i).getBeds();
				} else {
					beds = "Any";
				}
				if (mAlertsEntityList.get(i).getBaths() != null
						&& !mAlertsEntityList.get(i).getBaths().equals("")) {
					baths = mAlertsEntityList.get(i).getBaths();
				} else {
					baths = "Any";
				}
				if (mAlertsEntityList.get(i).getType().equalsIgnoreCase("rent")) {
					mType = "Rent";
					if (mAlertsEntityList.get(i).getPrice_range_min()
							.equalsIgnoreCase("")
							|| mAlertsEntityList.get(i).getPrice_range_min() == null) {
						mSplitedAmountMin = "$100";
					} else {
						mSplitedAmountMin = "$"
								+ mAlertsEntityList.get(i).getPrice_range_min();
					}
					if (mAlertsEntityList.get(i).getPrice_range_max()
							.equalsIgnoreCase("")
							|| mAlertsEntityList.get(i).getPrice_range_max() == null) {
						mSplitedAmountMax = "$10,000";
					} else {
						mSplitedAmountMax = "$"
								+ mAlertsEntityList.get(i).getPrice_range_max();
					}
				} else {
					mType = "Sale";
					if (mAlertsEntityList.get(i).getPrice_range_min()
							.equalsIgnoreCase("")
							|| mAlertsEntityList.get(i).getPrice_range_min() == null) {
						mSplitedAmountMin = "$10,000";
					} else {
						mSplitedAmountMin = "$"
								+ mAlertsEntityList.get(i).getPrice_range_min();
					}
					if (mAlertsEntityList.get(i).getPrice_range_max()
							.equalsIgnoreCase("")
							|| mAlertsEntityList.get(i).getPrice_range_max() == null) {
						mSplitedAmountMax = "$20M";
					} else {
						mSplitedAmountMax = "$"
								+ mAlertsEntityList.get(i).getPrice_range_max();
					}
				}
				String keywords = "";
				if (mAlertsEntityList.get(i).getKeywords() != null
						&& !mAlertsEntityList.get(i).getKeywords()
								.equalsIgnoreCase("")) {
					keywords = mAlertsEntityList.get(i).getKeywords()
							.toString().replace("[", "").replace("]", "");
				}
				// mObjectText.setTypeface(mTypeface);
				mObjectText.setText(mType + ", " + mSplitedAmountMin + " - "
						+ mSplitedAmountMax + "\n" + beds + "+ Beds | " + baths
						+ "+ Baths" + "\n" + keywords);

				mCheckBtnList.add(mCheckBtn);
				mMainLay.setTag(i);
				mMainLay.setOnClickListener(new OnClickListener() {

					private boolean isFirst = true;

					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						if (AppConstants.IS_EDIT.equals("true")) {
							if (isFirst) {
								isFirst = false;
								mCheckBtn
										.setBackgroundResource(R.drawable.tick_on);
								mAlertID = mAlertsEntityList.get(pos)
										.getAlert_id();
								mAlertsEntity = mAlertsEntityList.get(pos);
								mAlertIDS.add(mAlertID);
							} else {
								isFirst = true;
								mCheckBtn
										.setBackgroundResource(R.drawable.tick_off);
								mAlertID = mAlertsEntityList.get(pos)
										.getAlert_id();
								// mAlertsEntity = mAlertsEntityList.get(pos);
								mAlertIDS.remove(mAlertID);
							}
						} else {
							AppConstants.saved_search_Latitude = mAlertsEntityList
									.get(pos).getLatitude().toString();
							AppConstants.saved_search_Longitude = mAlertsEntityList
									.get(pos).getLongitude().toString();
							AppConstants.from_alert = true;
							AppConstants.from_profile_list = "true";
							Intent inte = new Intent(AlertsActivity.this,
									MapFragmentActivity.class);
							startActivity(inte);
						}
					}
				});
				mAlertsLayout.addView(convertView);

			}
		}
	}

	private void deleteAlerts() {

		if (mAlertIDS.size() != 0 && mAlertIDS.size() > 1) {
			mAlertID = mAlertIDS.toString().replace("[", "").replace("]", "");
		}

		String Url = AppConstants.BASE_URL + "alert/delete";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("alert_id", mAlertID);
		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									CommonResponse mResponse = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);

									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mAlertIDS.clear();
										mAlert = "CallApi";
										DialogManager.showCustomAlertDialog(
												AlertsActivity.this,
												AlertsActivity.this,
												mResponse.getMsg());
									}
								} else {

								}
							}

						});

	}

	@Override
	public void onOkclick() {
		if (mAlert.equalsIgnoreCase("CallApi")) {
			getAlerts();
			mAlert = "";
		} else {
			/**
			 * Close Dialog
			 */
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
	public void onRequestSuccess(PropertyDetailResponse obj) {
		// TODO Auto-generated method stub

	}
}

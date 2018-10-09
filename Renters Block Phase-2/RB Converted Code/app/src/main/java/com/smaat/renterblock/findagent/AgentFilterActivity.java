package com.smaat.renterblock.findagent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.model.PropertyDetailResponse;
import com.smaat.renterblock.savedsearch.LocalSavedSearchEntity;
import com.smaat.renterblock.sqlite.LocalSavedSearch;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.PropertyTypeActivity;
import com.smaat.renterblock.ui.SplashScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GPSTracker;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.PlaceRespone;
import com.smaat.renterblock.util.Places;
import com.smaat.renterblock.util.ServieRequestHandler;
import com.smaat.renterblock.util.TypefaceSingleton;
import com.smaat.renterblock.util.WebserviceCallbackInterface;

public class AgentFilterActivity extends BaseActivity implements
		OnClickListener, OnItemSelectedListener, WebserviceCallbackInterface,
		DialogMangerCallback {

	private Typeface mTypeface;
	private TableRow mPriceRangeRow;
	private ArrayList<String> mPriceMinList;
	private ArrayList<String> mPriceMaxList;
	private ArrayList<String> mTempListPrice;
	private Spinner mPriceMinSpin, mPriceMaxSpin;
	private TextView mPropertyTypeTxt, mPriceRangeTxt;
	private ArrayAdapter<String> mPriceMinAdapter;
	private ArrayAdapter<String> mPriceMaxAdapter;
	private String mPriceMin = "", mPriceMax = "", mSplitPriceMin = "",
			mSplitPriceMax = "";
	static EditText mLocationEdit, mKeywordsEdit;
	static FrameLayout mSavedSearchFrameView;
	private ListView mSavedSearchList;
	private ArrayList<LocalSavedSearchEntity> mLocalSavedSearch = new ArrayList<LocalSavedSearchEntity>();
	private AgentSavedSearchAdapter mSavedSearchAdapter;
	private LocalSavedSearchEntity mLocalSavedSearchEntity;
	private RelativeLayout mParentLay;
	private ArrayList<String> mArea;
	static Context mContext;
	static double mLatitude, mLongitude;
	private String area;
	private String mLocation = "", mKeywords = "", mUserType = "",
			mPropType = "", mLat = "", mLong = "";
	private TextView mUserTypeTxt;
	private String mCallApi = "Alert";
	private String latitu;
	private String Longi;
	private ArrayList<String> price_range = new ArrayList<String>();
	private ArrayList<String> prices_range = new ArrayList<String>();
	private LinearLayout mUserTypeLay;
	private ToggleButton mTogAgent, mTogBroker, mTogSeller;
	private boolean boolAgent = false, boolBroker = false, boolSeller = false;
	private String strAgent = "", strBroker = "", strSeller = "",
			strUserType = "";
	public static PropertyTypeActivity mPropertyTypeActivity = new PropertyTypeActivity();
	private TextView header_txt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_agent_filter);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		mContext = AgentFilterActivity.this;
		mArea = new ArrayList<String>();
		mTempListPrice = new ArrayList<String>();
		mPriceMinList = new ArrayList<String>();
		mPriceMaxList = new ArrayList<String>();

		initComponents();
		setDefaultValues();
	}

	private void setDefaultValues() {
		mLocationEdit.setText(SplashScreen.mAgentFilterLocalEntity
				.getLocation());
		mKeywordsEdit.setText(SplashScreen.mAgentFilterLocalEntity.getName());
		mUserTypeTxt.setText(SplashScreen.mAgentFilterLocalEntity.getType());
		mPropertyTypeTxt.setText(SplashScreen.mAgentFilterLocalEntity
				.getProperty_experties());
		if (SplashScreen.mAgentFilterLocalEntity.getPrice_range_min()
				.equalsIgnoreCase("")) {
			mPriceRangeTxt.setText("Any");
		} else {
			mPriceRangeTxt
					.setText(SplashScreen.mAgentFilterLocalEntity
							.getPrice_range_min()
							+ "-"
							+ SplashScreen.mAgentFilterLocalEntity
									.getPrice_range_max());
		}

		mLat = SplashScreen.mAgentFilterLocalEntity.getLatitude();
		mLong = SplashScreen.mAgentFilterLocalEntity.getLongitude();
	}

	private void initComponents() {
		
		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(helvetica_bold);
		
		mUserTypeLay = (LinearLayout) findViewById(R.id.user_type_lay);
		mParentLay = (RelativeLayout) findViewById(R.id.parent_layout);
		mLocationEdit = (EditText) findViewById(R.id.location_edit);
		mLocationEdit.setTypeface(helvetica_normal);
		mKeywordsEdit = (EditText) findViewById(R.id.keywords_edit);
		mKeywordsEdit.setTypeface(helvetica_normal);

		mUserTypeTxt = (TextView) findViewById(R.id.user_type_txt);
		mUserTypeTxt.setTypeface(helvetica_normal);
		
		mTogAgent = (ToggleButton) findViewById(R.id.tog_agent);
		mTogBroker = (ToggleButton) findViewById(R.id.tog_broker);
		mTogSeller = (ToggleButton) findViewById(R.id.tog_seller);

		mPriceRangeRow = (TableRow) findViewById(R.id.price_range_spin_row);
		mPropertyTypeTxt = (TextView) findViewById(R.id.property_type_txt);
		mPriceRangeTxt = (TextView) findViewById(R.id.price_range_txt);

		mPriceMinSpin = (Spinner) findViewById(R.id.price_range_min_spin);
		mPriceMaxSpin = (Spinner) findViewById(R.id.price_range_max_spin);

		mPriceMinSpin.setOnItemSelectedListener(this);
		mPriceMaxSpin.setOnItemSelectedListener(this);

		// mPropertyTypeTxt.setText(AppConstants.SELECTED_PROPERTY_TYPE);

		mPropertyTypeTxt.setText(SplashScreen.mAgentFilterLocalEntity
				.getProperty_experties());
		setSpinnerListValues();

		mSavedSearchFrameView = (FrameLayout) findViewById(R.id.save_search_frame_view);
		mSavedSearchList = (ListView) findViewById(R.id.saved_search_list);

		mPropertyTypeTxt.setTypeface(mTypeface);
		mPriceRangeTxt.setTypeface(mTypeface);

		mParentLay.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mSavedSearchFrameView.setVisibility(View.GONE);
				mUserTypeLay.setVisibility(View.GONE);
				return false;
			}
		});
		mLocationEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (mLocationEdit.getText().toString().equals("")) {
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
	}

	private void callPlaces(String place) {

		ServieRequestHandler.getInstance().getResultPlaces(
				getURL(AppConstants.PLACES_API_BASE, place), aq(),
				AgentFilterActivity.this, null, null, this);
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
		mLocationEdit.setText(location);
		((AgentFilterActivity) mContext).new getLatLong().execute(location);
		mSavedSearchFrameView.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.agent_lay:

			if (!boolAgent) {
				mTogAgent.setBackgroundResource(R.drawable.tick_on);
				strAgent = "Agents";

				boolAgent = true;
			} else {
				mTogAgent.setBackgroundResource(R.drawable.tick_off);
				strAgent = "";
				boolAgent = false;
			}
			setUserTypeText();
			break;
		case R.id.broker_lay:
			if (!boolBroker) {
				mTogBroker.setBackgroundResource(R.drawable.tick_on);
				strBroker = "Brokers";
				boolBroker = true;
			} else {
				mTogBroker.setBackgroundResource(R.drawable.tick_off);
				strBroker = "";
				boolBroker = false;
			}
			setUserTypeText();
			break;
		case R.id.seller_lay:
			if (!boolSeller) {
				mTogSeller.setBackgroundResource(R.drawable.tick_on);
				strSeller = "Sellers";
				boolSeller = true;
			} else {
				mTogSeller.setBackgroundResource(R.drawable.tick_off);
				strSeller = "";
				boolSeller = false;
			}
			setUserTypeText();
			break;
		case R.id.user_type_txt:
			if (mUserTypeLay.getVisibility() == View.VISIBLE) {
				mUserTypeLay.setVisibility(View.GONE);
			} else {
				mUserTypeLay.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.location_edit:
			mSavedSearchFrameView.setVisibility(View.VISIBLE);
			mUserTypeLay.setVisibility(View.GONE);
			addedDatasTosaveSearch();
			break;

		case R.id.back_icon:
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;
		case R.id.price_range_lay:
			if (mPriceRangeRow.getVisibility() == View.GONE) {
				mPriceRangeRow.setVisibility(View.VISIBLE);
			} else {
				mPriceRangeRow.setVisibility(View.GONE);
			}
			break;
		case R.id.reset_btn:
			mLocationEdit.setText("");
			mKeywordsEdit.setText("");
			mUserTypeTxt.setText("");
			mPropertyTypeTxt.setText("Select");
			mPriceRangeTxt.setText("Any");
			break;
		case R.id.apply_btn:
			validateFields();
			break;
		case R.id.property_type_lay:

			Intent intent = new Intent(AgentFilterActivity.this,
					PropertyTypeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;

		}

	}

	private void setUserTypeText() {

		if (!strAgent.equals("") && strBroker.equals("")
				&& strSeller.equals("")) {
			strUserType = strAgent;
		} else if (strAgent.equals("") && !strBroker.equals("")
				&& strSeller.equals("")) {
			strUserType = strBroker;
		} else if (strAgent.equals("") && strBroker.equals("")
				&& !strSeller.equals("")) {
			strUserType = strSeller;
		} else if (!strAgent.equals("") && !strBroker.equals("")
				&& strSeller.equals("")) {
			strUserType = strAgent + "," + strBroker;
		} else if (strAgent.equals("") && !strBroker.equals("")
				&& !strSeller.equals("")) {
			strUserType = strBroker + "," + strSeller;
		} else if (!strAgent.equals("") && strBroker.equals("")
				&& !strSeller.equals("")) {
			strUserType = strAgent + "," + strSeller;
		} else if (!strAgent.equals("") && !strBroker.equals("")
				&& !strSeller.equals("")) {
			strUserType = strAgent + "," + strBroker + "," + strSeller;
		} else if (strAgent.equals("") && strBroker.equals("")
				&& strSeller.equals("")) {
			strUserType = strAgent + strBroker + strSeller;
		}

		mUserTypeTxt.setText(strUserType);
	}

	private void validateFields() {
		mLocation = mLocationEdit.getText().toString().trim();
		mKeywords = mKeywordsEdit.getText().toString().trim();
		mUserType = mUserTypeTxt.getText().toString().trim();
		mPropType = mPropertyTypeTxt.getText().toString().trim();
		// mUserType = "agent";
		if (mUserType.equalsIgnoreCase("Agents")) {
			mUserType = "agent";
		} else if (mUserType.equalsIgnoreCase("Sellers")) {
			mUserType = "seller";
		} else if (mUserType.equalsIgnoreCase("Brokers")) {
			mUserType = "broker";
		} else {
			mUserType = "";
		}

		mLat = String.valueOf(mLatitude);
		mLong = String.valueOf(mLongitude);
		if (mLocation == null) {
			mLocation = "";
		}
		if (mKeywords == null) {
			mKeywords = "";
		}
		if (mUserType == null) {
			mUserType = "";
		}
		if (mPropType == null || mPropType.equalsIgnoreCase("Select")) {
			mPropType = "";
		}
		if (mLat == null) {
			mLat = "";
		}
		if (mLong == null) {
			mLong = "";
		}
		SplashScreen.mAgentFilterLocalEntity.setName(mKeywords);
		SplashScreen.mAgentFilterLocalEntity.setLocation(mLocation);
		SplashScreen.mAgentFilterLocalEntity.setType(mUserType);
		SplashScreen.mAgentFilterLocalEntity.setPrice_range_min(mPriceMin);
		SplashScreen.mAgentFilterLocalEntity.setPrice_range_max(mPriceMax);
		SplashScreen.mAgentFilterLocalEntity.setProperty_experties(mPropType);
		SplashScreen.mAgentFilterLocalEntity.setLatitude(mLat);
		SplashScreen.mAgentFilterLocalEntity.setLongitude(mLong);
		// if (mLocation.equals("") || mLocation.length() == 0) {
		// DialogManager.showCustomAlertDialog(this, this,
		// getString(R.string.please_enter_location));
		// } else {
		AppConstants.isAPI = true;
		finish();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
		// }

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

			mSavedSearchAdapter = new AgentSavedSearchAdapter(
					AgentFilterActivity.this,
					R.layout.save_search_custom_adapter_place_list,
					mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();

		} else {

			mLocalSavedSearch = LocalSavedSearch
					.saveSearch(AgentFilterActivity.this);
			AppConstants.from_api = false;

			mSavedSearchAdapter = new AgentSavedSearchAdapter(
					AgentFilterActivity.this,
					R.layout.save_search_custom_adapter_place_list,
					mLocalSavedSearch);
			mSavedSearchList.setAdapter(mSavedSearchAdapter);
			mSavedSearchAdapter.notifyDataSetChanged();
		}
	}

	private void setSpinnerListValues() {
		// for (int i = 0; i < 41; i++) {
		// if (i == 0) {
		// mPriceMinList.add("No Min");
		// mPriceMaxList.add("No Max");
		// } else {
		// mPriceMinList.add("$" + String.valueOf(1000 * i));
		// mPriceMaxList.add("$" + String.valueOf(1000 * i));
		// }
		// }
		addSellPriceRangeList();
		mPriceMinList = price_range;
		mPriceMaxList = prices_range;
		setSpinnerAdapterValues();
	}

	private void setSpinnerAdapterValues() {

		mPriceMinAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mPriceMinList);
		mPriceMinSpin.setAdapter(mPriceMinAdapter);

		mPriceMaxAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item_lay, R.id.spinner_item, mPriceMaxList);
		mPriceMaxSpin.setAdapter(mPriceMaxAdapter);
	}

	private void addSellPriceRangeList() {
		price_range.clear();
		prices_range.clear();
		price_range.add("No Min");
		price_range.add("$10,000");
		price_range.add("$20,000");
		price_range.add("$30,000");
		price_range.add("$50,000");
		price_range.add("$100,000");
		price_range.add("$130,000");
		price_range.add("$150,000");
		price_range.add("$200,000");
		price_range.add("$250,000");
		price_range.add("$300,000");
		price_range.add("$350,000");
		price_range.add("$400,000");
		price_range.add("$450,000");
		price_range.add("$500,000");
		price_range.add("$550,000");
		price_range.add("$600,000");
		price_range.add("$650,000");
		price_range.add("$700,000");
		price_range.add("$750,000");
		price_range.add("$800,000");
		price_range.add("$850,000");
		price_range.add("$900,000");
		price_range.add("$950,000");
		price_range.add("$1M");
		price_range.add("$1.1M");
		price_range.add("$1.2M");
		price_range.add("$1.25M");
		price_range.add("$1.4M");
		price_range.add("$1.5M");
		price_range.add("$1.6M");
		price_range.add("$1.7M");
		price_range.add("$1.75M");
		price_range.add("$1.8M");
		price_range.add("$1.9M");
		price_range.add("$2M");
		price_range.add("$2.25M");
		price_range.add("$2.5M");
		price_range.add("$2.75M");
		price_range.add("$3M");
		price_range.add("$3.5M");
		price_range.add("$4M");
		price_range.add("$5M");
		price_range.add("$10M");
		price_range.add("$20M");

		prices_range.add("No Max");
		prices_range.add("$10,000");
		prices_range.add("$20,000");
		prices_range.add("$30,000");
		prices_range.add("$50,000");
		prices_range.add("$100,000");
		prices_range.add("$130,000");
		prices_range.add("$150,000");
		prices_range.add("$200,000");
		prices_range.add("$250,000");
		prices_range.add("$300,000");
		prices_range.add("$350,000");
		prices_range.add("$400,000");
		prices_range.add("$450,000");
		prices_range.add("$500,000");
		prices_range.add("$550,000");
		prices_range.add("$600,000");
		prices_range.add("$650,000");
		prices_range.add("$700,000");
		prices_range.add("$750,000");
		prices_range.add("$800,000");
		prices_range.add("$850,000");
		prices_range.add("$900,000");
		prices_range.add("$950,000");
		prices_range.add("$1M");
		prices_range.add("$1.1M");
		prices_range.add("$1.2M");
		prices_range.add("$1.25M");
		prices_range.add("$1.4M");
		prices_range.add("$1.5M");
		prices_range.add("$1.6M");
		prices_range.add("$1.7M");
		prices_range.add("$1.75M");
		prices_range.add("$1.8M");
		prices_range.add("$1.9M");
		prices_range.add("$2M");
		prices_range.add("$2.25M");
		prices_range.add("$2.5M");
		prices_range.add("$2.75M");
		prices_range.add("$3M");
		prices_range.add("$3.5M");
		prices_range.add("$4M");
		prices_range.add("$5M");
		prices_range.add("$10M");
		prices_range.add("$20M");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int pos,
			long arg3) {
		if (parent.getId() == R.id.price_range_min_spin) {

			mTempListPrice.clear();
			mTempListPrice.addAll(mPriceMaxList);
			mPriceMin = (String) parent.getItemAtPosition(pos);
			mPriceRangeTxt.setText(mPriceMin + " - No Max");
			if (mPriceMin.equals("No Min")) {
				if (mPriceMax.equals("No Max")) {
					mPriceRangeTxt.setText("Any");
				} else {
					mPriceRangeTxt.setText("No Min" + " - " + mPriceMax);
				}
			}
			mSplitPriceMin = mPriceMin.replace("$", "");
			mSplitPriceMax = "";

			if (mPriceMin.equalsIgnoreCase(mTempListPrice.get(pos))) {
				for (int j = 1; j < pos; j++) {
					mTempListPrice.remove(1);
				}
				mPriceMaxAdapter = new ArrayAdapter<String>(this,
						R.layout.spinner_item_lay, R.id.spinner_item,
						mTempListPrice);
				mPriceMaxSpin.setAdapter(mPriceMaxAdapter);
				mPriceMaxAdapter.notifyDataSetChanged();

			}
		} else if (parent.getId() == R.id.price_range_max_spin) {

			mPriceMax = (String) parent.getItemAtPosition(pos);
			mPriceRangeTxt.setText(mPriceMin + " - " + mPriceMax);
			if (mPriceMax.equals("No Max")) {
				if (mPriceMin.equals("No Min")) {
					mPriceRangeTxt.setText("Any");
				} else {
					mPriceRangeTxt.setText(mPriceMin + " - " + "No Max");
				}
				mPriceRangeTxt.setText("Any");
			}
			mSplitPriceMax = mPriceMax.replace("$", "");

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	public static void setCurrentLocationDetails() {
		((AgentFilterActivity) mContext).showCurrentLocation();

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
				mLocationEdit.setText("");
				mLocationEdit.setText(response.getResults().get(0)
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
				mLocalSavedSearchEntity.setLocation(placeDetail
						.getDescription());
				mLocalSavedSearch.add(mLocalSavedSearchEntity);
				mArea.add(placeDetail.getDescription());
			}
			AppConstants.from_api = true;
			mSavedSearchAdapter = new AgentSavedSearchAdapter(
					AgentFilterActivity.this,
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
				mLocationEdit.setText(area);
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
			return GlobalMethods.getLatlong(address, AgentFilterActivity.this);
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

	@Override
	public void onOkclick() {

		if (mCallApi.equalsIgnoreCase("CallApi")) {
			AppConstants.isAPI = true;
			finish();
		} else {
			/**
			 * Close Dialog
			 */
		}
	}

	@Override
	protected void onResume() {
		if (AppConstants.isAPI) {
			AppConstants.isAPI = false;
			if (SplashScreen.mAgentFilterLocalEntity.getProperty_experties()
					.equalsIgnoreCase("")) {
				mPropertyTypeTxt.setText("All Types");
			} else {
				mPropertyTypeTxt.setText(SplashScreen.mAgentFilterLocalEntity
						.getProperty_experties());
			}

		}
		super.onResume();
	}
}

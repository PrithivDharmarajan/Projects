package com.smaat.renterblock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smaat.renterblock.adapter.CustomPagerAdapter;
import com.smaat.renterblock.adapter.HomeScreenListAdapter;
import com.smaat.renterblock.adapter.MapSavedSearchAdapter;
import com.smaat.renterblock.entity.GoogleApiEntity;
import com.smaat.renterblock.entity.MapLocationEntity;
import com.smaat.renterblock.entity.MapPropertDetailsEntity;
import com.smaat.renterblock.entity.MarkerID;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyImageEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.findagent.FindAgentDetailsActivity;
import com.smaat.renterblock.friends.ui.FriendsMainScreen;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.MapFragmentResponse;
import com.smaat.renterblock.model.PropertyDetailResponse;
import com.smaat.renterblock.model.PropertyResponse;
import com.smaat.renterblock.model.Result;
import com.smaat.renterblock.myfavourite.MyFavouriteActivity;
import com.smaat.renterblock.savedsearch.LocalSavedSearchEntity;
import com.smaat.renterblock.sqlite.DatabaseManager;
import com.smaat.renterblock.sqlite.LocalSavedSearch;
import com.smaat.renterblock.sqlite.RentersBlockDatabase;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.FilterActivity;
import com.smaat.renterblock.ui.LoginActivity;
import com.smaat.renterblock.ui.MyReviewActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.ui.SplashScreen;
import com.smaat.renterblock.ui.WriteReviewActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GPSTracker;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.PlaceRespone;
import com.smaat.renterblock.util.Places;
import com.smaat.renterblock.util.ScrollDisabledListView;
import com.smaat.renterblock.util.TypefaceSingleton;
import com.smaat.renterblock.util.WebserviceCallbackInterface;
import com.smaat.renterblock.webservice.ServiceRequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapFragmentActivity extends BaseActivity implements LocationListener, OnMapClickListener,
		OnMarkerClickListener, WebserviceCallbackInterface, DialogMangerCallback, OnMapLoadedCallback, OnMapReadyCallback,AdapterView.OnItemClickListener {

	private GoogleMap mGoogleMap;
	private Button mDrawButton;
	public Bitmap bitmap[];
	private boolean mIsMapMoveable = false;
	private List<LatLng> mLatLngList;
	private FrameLayout mGoogleMapLayout;
	private Polygon mDrawShape;
	private Projection mProjection;
	private DrawingView mDrawingViews;
	private Paint mPaint;
	private ArrayList<LatLng> mValidate1;
	private Dialog d1, d2, d3, save_search_di;
	private static ViewGroup rootView;
	private DirectionalViewPager pager;
	private ImageView mFilter;
	private Button mSlide;
	private LinearLayout mSlideIcon;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ArrayList<HashMap<Integer, ArrayList<String>>> imageUrls = new ArrayList<HashMap<Integer, ArrayList<String>>>();
	// public Bitmap bitmap[];
	private Marker marker1, marker2;
	private boolean isClicked = true;
	private RelativeLayout mFrame;
	private LinearLayout mBottomLay;
	private boolean isArcclicked = false;
	private MarkerID mPrevMarkerId;
	private Marker mPrevMarker;
	private Button arc_image_view;
	private Handler mHandler, mlistHandler;
	private RelativeLayout ad_view;
	private int mInterval = 25000;
	private TextView mSearchText, mListText;
	static AutoCompleteTextView mSearchEdit;
	private RelativeLayout mSearchLay;
	private ArrayList<String> mArea;
	private LatLng mLatlng;
	private String area = "";
	// private GeoPoint p1 = null;
	private MarkerOptions marker;
	public boolean isClicked1 = true, islistClick = true;
	private DisplayMetrics dm;
	private ScrollView mPropertyList;
	private String addresss = "";
	private EditText mLocation;
	private TableLayout view_Container, List_view_container;
	private ArrayList<MapLocationEntity> map_loc;
	private MapLocationEntity mapentity;
	private ArrayList<PropertyImageEntity> property_image;
	private PropertyImageEntity property_ent;
	private boolean donzoom = false;
	private ProgressDialog progress;
	private boolean isFirst = false;
	private boolean isList = false;
	private boolean isEnable = false;
	private int count = 0;
	private String Lat_check, Long_check;
	// private HashMap<String, TextView> loc_marker;
	private HashMap<String, ImageView> loc_img_view;
	private View marker_view, selected_marker;
	private View marker_view_shown;
	private TextView numTxt;
	private Button prop_type_icon;
	private SlideHolder slide_holder;
	private Button logout, favourite;
	private boolean isSwipe = false;
	private MarkerID mMarkerid;
	private ImageView mMarkerImg;
	private ArrayList<MarkerID> mMarker = new ArrayList<MarkerID>();
	private boolean isVertical = true, isHorizontal = true;
	private static final int MIN_DISTANCE = 100;
	private float downX, downY, upX, upY;
	private static final String logTag = "ActivitySwipeDetector";
	// private String UserID;
	private String strjson, filtertype;
	private ArrayList<PropertyEntity> mProperty = new ArrayList<PropertyEntity>();
	private PropertyEntity propertyEntity;
	private ArrayList<PropertyPictures> mPropertyPics;
	private int position;
	private boolean isfavourite = true;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private String propertyID, UserName;
	private SharedPreferences pref;
	private Editor editor;
	private TextView mUserName;
	// private Button mBuySettings, mSellSettings;
	private double lat, lon;
	private TextView save_Search;
	private Handler handler1;
	private boolean ishown = false;
	private int deviceHeight;
	private int pagerHeight;
	private int minHeight;
	private int maxHeight;
	private Bundle mBundle;
	public static String isReview = "", mSlideClick = "";
	private LocationInfo curr_loc;
	private RelativeLayout mEditLay;
	private View mEditView;
	private TextView mCancelText;
	private ProgressBar mLocProgress;
	private double saved_latitude, saved_longitude;
	private String property_id;
	private int curr_position;
	private String lati;
	private String longti;
	private String Pproperty;

	private double pLat = 0.0, pLong = 0.0;
	private Dialog save_search_dialog;
	private String saved_search_input_text;

	private int currentHashMapIndex = 0, currentImageIndex = 0;
	private ArrayList<MapPropertDetailsEntity> map_arr_ent;
	MapPropertDetailsEntity map_ent;

	private ScrollDisabledListView propertyImageListView;
	private ArrayList<Marker> markersList = new ArrayList<Marker>();
	private ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
	private HomeScreenListAdapter homeScreenListAdapter;
	private int currentScrollXList = 0, currentScrollYList = 0, oldScrollXList = 0, oldScrollYList = 0,
			selectedItem = 0;
	ArrayList<LocalSavedSearchEntity> local_save_search = new ArrayList<LocalSavedSearchEntity>();
	private MapSavedSearchAdapter saveSearchAdapter;
	private LocalSavedSearchEntity local_search_ent;
	private String prop_type_marker;
	private FrameLayout saved_search_frame_view;
	private ProgressBar mMap_progress_bar;
	private ListView saved_Search_list;
	static Context mContext;
	CameraPosition cameraposition;
	boolean isMapMove = true;
	boolean isMoved = false;

	private String type_of_property = "Rent";
	ArrayList<String> position_map;
	private boolean don_call_api = false;
	PolygonOptions drawShapeOptions;
	int mCount = 0;
	private Button layers_txt;

	private LinearLayout mPropListLay;
	private ArrayList<String> mPropertyTypeList;
	private ArrayList<Button> mPropItemBtn = new ArrayList<Button>();
	private MapFragmentResponse obj;
	private int currentPage;
	private String DeviceID, Device;
	private String mNEW_SAVED_SEARCH_MATCHES = "", mSEARCH_RESULT_COUNT = "", mUPDATE_RESULT_AS_MAP_MOVES = "";

	private Dialog payment_alert_dialog;
	static final int DATE_DIALOG_ID = 999;

	private String card_month, card_year;
	private WebView webView;
	private ProgressDialog progres;

	private TextView agent_description_txt, agent_user_name_txt, agent_phone_number;
	private ImageView agent_profile_img;

	private float map_distance;
	private String mAgent_user_id;
	private Animation mFromRight, mFromLeft;
	// private static Animation mFromTop;
	// private Animation mFromBottom;

	GPSTracker tracker;

	private boolean MAP_MOVE_FLAG = false;

	private Handler map_move_Handler = new Handler();
	private Handler ads_show_handler = new Handler();
	private ProgressDialog mProgressDialog;


	private ArrayList<String> places;
	private AQuery mAQuery;
	private List<HashMap<String, String>> placesList;

	int viewPagerPosition, listViewPosition;
	String[] positions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_map_screen_check);

		rootView = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelveticaLight(this);
		setFont(rootView, mTypeface);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		mContext = MapFragmentActivity.this;
		mValidate1 = new ArrayList<LatLng>();
		// mMap_screen_progress.setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map, fragment).commit();
		}
		fragment.getMapAsync(this);


		initComponents();
		setGoogleAnalytics(this);
		slideUserNameComponents();
		buySlidemenuComponents();
		sellSlidemenuComponents();
		agentSlidemenuComponents();


		mPropertyTypeList = new ArrayList<String>();
		addSortingTypesList();

		getDeviceHeight();
		if (curr_loc == null) {
			curr_loc = new LocationInfo(this);
		}

		progres = DialogManager.getProgressDialog(this);
		progres.dismiss();

		curr_loc.refresh(this);
		pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		editor = pref.edit();
		handler1 = new Handler();

		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);

		if (selectedType.equalsIgnoreCase(AppConstants.BUYER) || selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			if (AppConstants.buy_home_type.equals("rent")) {
				setBuyBackground(R.id.buy_homes_for_rent);
				AppConstants.view_id = R.id.buy_homes_for_rent;
				type_of_property = "rent";
			} else if (AppConstants.buy_home_type.equals("sale")) {
				setBuyBackground(R.id.buy_homes_for_sale);
				AppConstants.view_id = R.id.buy_homes_for_sale;
				type_of_property = "sale";
			} else if (AppConstants.buy_home_type.equals("exclusive")) {
				setBuyBackground(R.id.buy_exclusives);
				AppConstants.view_id = R.id.buy_exclusives;
				type_of_property = "exclusive";
			} else if (AppConstants.buy_home_type.equals("openhouse")) {
				setBuyBackground(R.id.buy_open_house);
				AppConstants.view_id = R.id.buy_open_house;
				type_of_property = "openhouse";
			} else {
				setBuyBackground(R.id.buy_homes_for_rent);
				AppConstants.view_id = R.id.buy_homes_for_rent;
				type_of_property = "rent";
			}
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			if (AppConstants.sell_home_type.equals("rent")) {
				setSellBackground(R.id.sell_homes_for_rent);
				AppConstants.view_id = R.id.sell_homes_for_rent;
				type_of_property = "rent";
			} else if (AppConstants.sell_home_type.equals("sale")) {
				setSellBackground(R.id.sell_homes_for_sale);
				AppConstants.view_id = R.id.sell_homes_for_sale;
				type_of_property = "sale";
			} else if (AppConstants.sell_home_type.equals("openhouse")) {
				setSellBackground(R.id.sell_open_house);
				AppConstants.view_id = R.id.sell_open_house;
				type_of_property = "openhouse";
			} else if (AppConstants.sell_home_type.equals("exclusive")) {
				setSellBackground(R.id.sell_exclusives);
				AppConstants.view_id = R.id.sell_exclusives;
				type_of_property = "exclusive";
			} else {
				setSellBackground(R.id.sell_homes_for_rent);
				AppConstants.view_id = R.id.sell_homes_for_rent;
				type_of_property = "rent";
			}
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			if (AppConstants.agent_home_type.equals("rent")) {
				setAgentBackground(R.id.agent_homes_for_rent);
				AppConstants.view_id = R.id.agent_homes_for_rent;
				type_of_property = "rent";
			} else if (AppConstants.agent_home_type.equals("sale")) {
				setAgentBackground(R.id.agent_homes_for_sale);
				AppConstants.view_id = R.id.agent_homes_for_sale;
				type_of_property = "sale";
			} else if (AppConstants.agent_home_type.equals("openhouse")) {
				setAgentBackground(R.id.agent_open_house);
				AppConstants.view_id = R.id.agent_open_house;
				type_of_property = "openhouse";
			} else if (AppConstants.agent_home_type.equals("exclusive")) {
				setAgentBackground(R.id.agent_exclusives);
				AppConstants.view_id = R.id.agent_exclusives;
				type_of_property = "exclusive";
			} else {
				setAgentBackground(R.id.agent_homes_for_rent);
				AppConstants.view_id = R.id.agent_homes_for_rent;
				type_of_property = "rent";
			}
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			if (AppConstants.buy_home_type.equals("rent")) {
				setBuyBackground(R.id.buy_homes_for_rent);
				AppConstants.view_id = R.id.buy_homes_for_rent;
				type_of_property = "rent";
			} else if (AppConstants.buy_home_type.equals("sale")) {
				setBuyBackground(R.id.buy_homes_for_sale);
				AppConstants.view_id = R.id.buy_homes_for_sale;
				type_of_property = "sale";
			} else if (AppConstants.buy_home_type.equals("exclusive")) {
				setBuyBackground(R.id.buy_exclusives);
				AppConstants.view_id = R.id.buy_exclusives;
				type_of_property = "exclusive";
			} else if (AppConstants.buy_home_type.equals("openhouse")) {
				setBuyBackground(R.id.buy_open_house);
				AppConstants.view_id = R.id.buy_open_house;
				type_of_property = "openhouse";
			} else {
				setBuyBackground(R.id.buy_homes_for_rent);
				AppConstants.view_id = R.id.buy_homes_for_rent;
				type_of_property = "rent";
			}
		}

		if (mProperty != null) {
			for (int i = 0; i < mProperty.size(); i++) {
				if (mProperty.get(i).getProperty_pics() != null) {
					HashMap<Integer, ArrayList<String>> imageHash = new HashMap<Integer, ArrayList<String>>();
					ArrayList<String> singlePropImages = new ArrayList<String>();
					for (PropertyPictures propertyPictures : mProperty.get(i).getProperty_pics()) {
						singlePropImages.add(propertyPictures.getFile());
					}
					imageHash.put(0, singlePropImages);
					imageUrls.add(imageHash);
				}
			}
		}

		if (AppConstants.CALL_MAP.equalsIgnoreCase("false")) {
			AppConstants.CALL_MAP = "true";
			String check_shown = pref.getString("shown", "false");
			if (check_shown.equals("false")) {
				showHintDialog();
				editor.putString("shown", "true");
				editor.commit();
			}
		}

		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			isReview = mBundle.getString("review");
			if (isReview != null && isReview.equalsIgnoreCase("review")) {
				mSlide.setBackgroundResource(R.drawable.back_arrow_white);
			}
			mSlideClick = mBundle.getString("mSlideClick");
			if (mSlideClick == null) {
				mSlideClick = "";
			}
			lati = mBundle.getString("latitude");
			longti = mBundle.getString("longitude");

			if (lati != null && longti != null) {
				newlatlan();
			}

			pLat = mBundle.getDouble("PropertyLatitude");
			pLong = mBundle.getDouble("PropertyLongtitude");
			Pproperty = mBundle.getString("Propertymap");

		}
		if (AppConstants.from_map_list) {
			mEditLay.setVisibility(View.VISIBLE);
			mEditView.setVisibility(View.VISIBLE);
			mCancelText.setVisibility(View.GONE);
			mFilter.setVisibility(View.VISIBLE);
			mPropertyList.setVisibility(View.VISIBLE);
			mListText.setText("Map");
			layers_txt.setText("Sort");
			islistClick = false;
			// callPropertyApi();
			// showLayersDialog();
			getPropertyLatLong(AppConstants.type_of_property_filter);
			AppConstants.from_map_list = false;
		} else {
			mEditLay.setVisibility(View.VISIBLE);
			mEditView.setVisibility(View.VISIBLE);
			mCancelText.setVisibility(View.GONE);
			mFilter.setVisibility(View.VISIBLE);
			mPropertyList.setVisibility(View.GONE);
			mListText.setText("List");
			layers_txt.setText("Layers");
			islistClick = true;
			// showSortingDialog();
			// callPropertyApi();
			getPropertyLatLong(AppConstants.type_of_property_filter);
		}

		if (AppConstants.from_favourites_act) {
			mSlide.setBackgroundResource(R.drawable.back_arrow_white);
		}

		initViews();

		if (mSlideClick.equalsIgnoreCase("Sale")) {
			AppConstants.type_of_property_filter = "Sale";
			getPropertyLatLong("Sale");
		} else if (mSlideClick.equalsIgnoreCase("Rent")) {
			AppConstants.type_of_property_filter = "Rent";
			getPropertyLatLong("Rent");
		} else if (mSlideClick.equalsIgnoreCase("Exclusive")) {
			AppConstants.type_of_property_filter = "Exclusive";
			getPropertyLatLong("Exclusive");
		} else if (mSlideClick.equalsIgnoreCase("OpenHouse")) {
			AppConstants.type_of_property_filter = "open_house";
			getPropertyLatLong("open_house");
		}
		/* Push Notification */

		String deviceId = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_deviceReg_Id);

		boolean isRegistered = (Boolean) GlobalMethods.getValueFromPreference(this, GlobalMethods.BOOLEAN_PREFERENCE,
				AppConstants.pref_device_reg_status);

		boolean isDeveiceIdChanged = (Boolean) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.pref_device_id_changed);

		DeviceID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_deviceReg_Id);
		Device = "Android";

		if (!isRegistered || isDeveiceIdChanged) {
			if (deviceId != null && !deviceId.equals("")) {

			} else {
				//GCMRegistrar.register(this, AppConstants.SENDER_ID);
			}
		}

		mNEW_SAVED_SEARCH_MATCHES = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.NEW_SAVED_SEARCH_MATCHES);

		mSEARCH_RESULT_COUNT = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.SEARCH_RESULT_COUNT);

		mUPDATE_RESULT_AS_MAP_MOVES = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.UPDATE_RESULT_AS_MAP_MOVES);
		// getPropertyLatLong(AppConstants.type_of_property_filter);
		// if (mNEW_SAVED_SEARCH_MATCHES.equalsIgnoreCase("OFF")) {
		//
		// } else {
		//
		// }

		// showPaymentDialogAlert();
		// showExpirationDialog("hello");
	}

	private void initComponents() {
		mFromRight = AnimationUtils.loadAnimation(MapFragmentActivity.this, R.anim.anim_in_right);
		mFromLeft = AnimationUtils.loadAnimation(MapFragmentActivity.this, R.anim.anim_in_left);
		// mFromTop = AnimationUtils.loadAnimation(MapFragmentActivity.this,
		// R.anim.anim_in_top);
		// mFromBottom = AnimationUtils.loadAnimation(MapFragmentActivity.this,
		// R.anim.anim_in_bottom);
		mlistHandler = new Handler();
		tracker = new GPSTracker(this);
		if (tracker.canGetLocation() == false) {
			tracker.showSettingsAlert();
		}
		mLocProgress = (ProgressBar) findViewById(R.id.location_progress);
		mSlide = (Button) findViewById(R.id.slide);
		mSlideIcon = (LinearLayout) findViewById(R.id.slide_icon);
		if (AppConstants.from_profile_list.equals("true")) {
			mSlide.setBackgroundResource(R.drawable.back_arrow_white);
		} else {
			mSlide.setBackgroundResource(R.drawable.slide_button);
		}
		mFrame = (RelativeLayout) findViewById(R.id.mainLayout);
		ad_view = (RelativeLayout) findViewById(R.id.ad_view);

		ad_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inte = new Intent(MapFragmentActivity.this, FindAgentDetailsActivity.class);
				inte.putExtra("mUserID", mAgent_user_id);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		mEditLay = (RelativeLayout) findViewById(R.id.edit_layout);

		mBottomLay = (LinearLayout) findViewById(R.id.bottom_layout);
		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);

		agent_description_txt = (TextView) findViewById(R.id.agent_description);
		agent_description_txt.setTypeface(helvetica_bold);
		agent_profile_img = (ImageView) findViewById(R.id.profile_image_agent_view);
		agent_user_name_txt = (TextView) findViewById(R.id.agent_user_name);
		agent_phone_number = (TextView) findViewById(R.id.agent_phone_number);
		webView = (WebView) findViewById(R.id.webView1);

		arc_image_view = (Button) findViewById(R.id.arc_slide_image);

		// mSearchText = (TextView) findViewById(R.id.how);
		layers_txt = (Button) findViewById(R.id.layers);
		save_Search = (TextView) findViewById(R.id.save_Search);
		mListText = (TextView) findViewById(R.id.list);

		layers_txt.setTypeface(helvetica_light);
		save_Search.setTypeface(helvetica_light);
		mListText.setTypeface(helvetica_light);

		mUserName = (TextView) findViewById(R.id.username);

		mCancelText = (TextView) findViewById(R.id.cancel);
		mFilter = (ImageView) findViewById(R.id.filter);

		mSearchEdit = (AutoCompleteTextView) findViewById(R.id.how_edit);
		mSearchEdit.setTypeface(helvetica_bold);
		mSearchEdit.setText(AppConstants.Map_search_name);
		mSearchEdit.setOnItemClickListener(this);
		mPropertyList = (ScrollView) findViewById(R.id.property_list);

		List_view_container = (TableLayout) findViewById(R.id.view_containers);

		mEditView = (View) findViewById(R.id.edit_view);

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

		propertyImageListView = (ScrollDisabledListView) findViewById(R.id.images_list);

		saved_search_frame_view = (FrameLayout) findViewById(R.id.save_search_frame_view);
		mMap_progress_bar = (ProgressBar) findViewById(R.id.map_progress_bar);
		// mMap_progress_bar.setVisibility(View.VISIBLE);

		saved_Search_list = (ListView) findViewById(R.id.saved_search_list);

		/**
		 * ArrayList
		 */
		map_loc = new ArrayList<MapLocationEntity>();
		property_image = new ArrayList<PropertyImageEntity>();
		// mProperty = new ArrayList<PropertyEntity>();
		mPropertyPics = new ArrayList<PropertyPictures>();

		mArea = new ArrayList<String>();

		mFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MapFragmentActivity.this, FilterActivity.class);
				intent.putExtra("Filter", "Filter");
				startActivity(intent);
			}
		});

		mSlideIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppConstants.saved_search_json = "";
				if (AppConstants.from_profile_list.equals("true")) {
					finish();
					AppConstants.from_profile_list = "false";
				} else {
					if (AppConstants.from_favourites_act) {
						Intent intent = new Intent(MapFragmentActivity.this, MyFavouriteActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					} else {
						if (isReview != null && isReview.equalsIgnoreCase("review")) {
							Intent intent = new Intent(MapFragmentActivity.this, MyReviewActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						} else {
							if (slide_holder != null) {
								slide_holder.toggle();
							}
							onMapClick(null);
						}
					}
				}
			}
		});

		mSearchEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSearchEdit.setHint("");
				mSearchEdit.setText("");
				saved_search_frame_view.setVisibility(View.VISIBLE);
				// saved_search_frame_view.startAnimation(mFromBottom);
				mCancelText.setVisibility(View.VISIBLE);
				mFilter.setVisibility(View.GONE);
				addedDatasTosaveSearch();
			}
		});
		mSearchEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mSearchEdit.getText().toString().equals("")) {
					addedDatasTosaveSearch();
				} else {
					if (AppConstants.Map_search_name.equalsIgnoreCase("")) {
						callPlaces(s.toString());
					} else {
						callPlaces(AppConstants.Map_search_name);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					saved_search_frame_view.setVisibility(View.GONE);
					// saved_search_frame_view.startAnimation(mFromTop);
					mSearchEdit.setHint(getString(R.string.enter_your_city));
					if (mSearchEdit.getText().toString().trim().length() == 0
							&& mSearchEdit.getText().toString().trim().equalsIgnoreCase("")) {
						Toast.makeText(MapFragmentActivity.this, "Please select location", Toast.LENGTH_LONG).show();
					} else {
						fetchLatLongFromService latlng = new fetchLatLongFromService(
								mSearchEdit.getText().toString().trim().replaceAll("\\s+", ""));
						AppConstants.Map_search_name = mSearchEdit.getText().toString();
						latlng.execute();
					}
					// getPropertyLatLong(AppConstants.type_of_property_filter);
					hideSoftKeyboard(MapFragmentActivity.this);
					mCancelText.setVisibility(View.GONE);
					mFilter.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

	}

	private void addedDatasTosaveSearch() {
		AppConstants.isAdded = false;
		if (UserID.equalsIgnoreCase("0") || UserID.equalsIgnoreCase("")) {
			local_save_search.clear();
			local_search_ent = new LocalSavedSearchEntity();
			local_search_ent.setLocation("");
			local_search_ent.setProperty_type("");
			local_search_ent.setUser_id("");
			local_save_search.add(local_search_ent);
			AppConstants.from_api = false;
			saveSearchAdapter = new MapSavedSearchAdapter(MapFragmentActivity.this,
					R.layout.save_search_custom_adapter_place_list, local_save_search);
			saved_Search_list.setAdapter(saveSearchAdapter);
			saveSearchAdapter.notifyDataSetChanged();
		} else {
			local_save_search = LocalSavedSearch.saveSearch(MapFragmentActivity.this);
			if (local_save_search.isEmpty()) {
				local_save_search.clear();
				local_search_ent = new LocalSavedSearchEntity();
				local_search_ent.setLocation("");
				local_search_ent.setProperty_type("");
				local_search_ent.setUser_id("");
				local_save_search.add(local_search_ent);
				AppConstants.from_api = false;
				saveSearchAdapter = new MapSavedSearchAdapter(MapFragmentActivity.this,
						R.layout.save_search_custom_adapter_place_list, local_save_search);
				saved_Search_list.setAdapter(saveSearchAdapter);
				saveSearchAdapter.notifyDataSetChanged();
			} else {
				AppConstants.from_api = false;
				saveSearchAdapter = new MapSavedSearchAdapter(MapFragmentActivity.this,
						R.layout.save_search_custom_adapter_place_list, local_save_search);
				saved_Search_list.setAdapter(saveSearchAdapter);
				saveSearchAdapter.notifyDataSetChanged();
			}
		}
	}

	private void newlatlan() {

		marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
				.position(new LatLng(Double.valueOf(lati), Double.valueOf(longti)));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(Double.valueOf(lati), Double.valueOf(longti))).zoom((float) 16).build();

		mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

	}

	public static void Propertylatlan(String lat, String longi) {
		((MapFragmentActivity) mContext).showCurrentLocation(lat, longi);
		// ((MapFragmentActivity) mContext).hideSoftKeyboard(mContext);
		((MapFragmentActivity) mContext).saved_search_frame_view.setVisibility(View.GONE);
		// ((MapFragmentActivity) mContext).saved_search_frame_view
		// .startAnimation(mFromTop);

		((MapFragmentActivity) mContext).mCancelText.setVisibility(View.GONE);
		((MapFragmentActivity) mContext).mFilter.setVisibility(View.VISIBLE);

	}

	private void callPropertyLatLong(String pro_type) {

		MAP_MOVE_FLAG = true;
		if (map_arr_ent != null) {
			map_arr_ent.clear();
		}
		String url = AppConstants.BASE_URL + "propertylist";
		GsonTransformer t = new GsonTransformer();

		Gson gson = new Gson();
		if (AppConstants.type_of_property_filter.equalsIgnoreCase("rent")) {
			if (AppConstants.saved_search_json.equalsIgnoreCase("")) {
				strjson = "{\"Rent\" :" + gson.toJson(SplashScreen.mLocaleRentFilterObjectEntity) + "}";
			} else {
				strjson = "{\"Rent\" :" + AppConstants.saved_search_json + "}";
			}
		} else if (AppConstants.type_of_property_filter.equalsIgnoreCase("sale")) {
			if (AppConstants.saved_search_json.equalsIgnoreCase("")) {
				strjson = "{\"Sale\" :" + gson.toJson(SplashScreen.mLocaleSellFilterObjectEntity) + "}";
			} else {
				strjson = "{\"Sale\" :" + AppConstants.saved_search_json + "}";
			}
		} else if (AppConstants.type_of_property_filter.equalsIgnoreCase("sold")) {
			strjson = gson.toJson(SplashScreen.mLocaleSoldFilterObjectEntity);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (UserID.equals("")) {
			UserID = "0";
		}
		params.put("user_id", UserID);
		params.put("filterObject", strjson);
		if (AppConstants.agent_home_type.equals("rent") || AppConstants.agent_home_type.equals("Rent")) {
			pro_type = "Rent";
		} else if (AppConstants.agent_home_type.equals("sale") || AppConstants.agent_home_type.equals("Sale")) {
			pro_type = "Sale";
		}
		params.put("type", pro_type);
		curr_loc.refresh(this);
		if (AppConstants.saved_search_Latitude.equals("0.0")) {
			if (isMapMove) {
				params.put("latitude", saved_latitude);
				params.put("longitude", saved_longitude);
			} else {
				params.put("latitude", curr_loc.lastLat);
				params.put("longitude", curr_loc.lastLong);
			}
		} else {
			params.put("latitude", AppConstants.saved_search_Latitude);
			params.put("longitude", AppConstants.saved_search_Longitude);
		}
		params.put("distance", map_distance);
		params.put("limit", mSEARCH_RESULT_COUNT);
		params.put("start", "0");
		aq().transformer(t)
				// .progress(DialogManager.getProgressDialog(mContext))
				.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							obj = new Gson().fromJson(json.toString(), MapFragmentResponse.class);
							onMapRequestSuccess(obj);
							// if (AppConstants.from_alert) {
							// AppConstants.saved_search_Latitude = "0.0";
							// AppConstants.saved_search_Longitude = "0.0";
							// }
						} else {
							statusErrorCode(status);
						}
					}
				});
	}

	public static void getPropertyLatLong(String type) {
		((MapFragmentActivity) mContext).callPropertyLatLong(type);
	}

	private boolean isShown = false;

	private void showPurchaseAlertDialog(int validationText) {
		if (!isShown) {
			AppConstants.From_Map_Fragment = true;
			DialogManager.showDialogAlert(MapFragmentActivity.this, getString(validationText),
					getString(R.string.close), getString(R.string.enhanc), ProfileScreen.class, R.anim.slide_in_right,
					R.anim.slide_out_left, ProfileScreen.class, FriendsMainScreen.class, this);
			isShown = true;
		}
	}

	private void onMapRequestSuccess(MapFragmentResponse response) {

		GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.PURCHASE_EXPIRATION,
				response.getIs_purchased());
		if (response.getIs_purchased() != null && response.getIs_purchased().equals("1")
				&& !UserID.equalsIgnoreCase("0")) {
			if (mUser_Type.equalsIgnoreCase("renter") || mUser_Type.equalsIgnoreCase("buyer")) {
			} else {
				showPurchaseAlertDialog(R.string.validation_text);
				GlobalMethods.storeValuetoPreference(MapFragmentActivity.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.ENHANCED_PROFILE, "0");
			}

		} else if (response.getIs_purchased() != null && response.getIs_purchased().equals("2")
				&& !UserID.equalsIgnoreCase("0")) {
			if (mUser_Type.equalsIgnoreCase("renter") || mUser_Type.equalsIgnoreCase("buyer")) {
			} else {
				showPurchaseAlertDialog(R.string.validation_text_1);
				GlobalMethods.storeValuetoPreference(MapFragmentActivity.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.ENHANCED_PROFILE, "0");
			}
		}
		if (mHandler != null) {
			ad_view.setVisibility(View.GONE);
			mHandler.removeCallbacks(mRunnable);
		}
		if (response.getAgent().getTotal_records() != null) {
			if (response.getAgent().getResult().size() != 0) {
				mAgent_user_id = response.getAgent().getResult().get(0).getUser_id();
				// agent_description_txt.setText(response.getAgent().getResult()
				// .get(0).getDescription());
				agent_user_name_txt.setText(response.getAgent().getResult().get(0).getFirst_name() + " "
						+ response.getAgent().getResult().get(0).getLast_name());
				agent_phone_number.setText(response.getAgent().getResult().get(0).getBusiness_name());
				aq().id(agent_profile_img).image(response.getAgent().getResult().get(0).getUser_pic(), true, true, 0,
						R.drawable.profile_pic, null, 0, 1.0f / 1.0f);
				// ad_view.setVisibility(View.VISIBLE);
				ads_show_handler.removeCallbacks(adsShowingservice);
				ads_show_handler.postDelayed(adsShowingservice, 5000);
				mHandler = new Handler();
				mHandler.postDelayed(mRunnable, mInterval);
			}
		}

		mProperty.clear();

		map_arr_ent = response.getResult();

		int map_si = 0;
		int size = map_arr_ent.size();

		System.out.println(String.valueOf(size));

		int settingsCount = Integer.parseInt(mSEARCH_RESULT_COUNT);

		if (size < settingsCount) {
			map_si = size;
		} else {
			map_si = settingsCount;
		}

		for (int i = 0; i < map_si; i++) {

			PropertyEntity propertyEntity = new PropertyEntity();

			propertyEntity.setLatitude(response.getResult().get(i).getLatitude());
			propertyEntity.setLongitude(response.getResult().get(i).getLongitude());
			propertyEntity.setPrice_range(response.getResult().get(i).getPrice_range());
			propertyEntity.setProperty_id(response.getResult().get(i).getProperty_id());
			propertyEntity.setProperty_name(response.getResult().get(i).getProperty_name());
			propertyEntity.setProperty_pics(response.getResult().get(i).getProperty_pics());
			propertyEntity.setRb_block(response.getResult().get(i).getRb_block());
			propertyEntity.setOpen_house(response.getResult().get(i).getOpen_house());
			propertyEntity.setAddress(response.getResult().get(i).getAddress());
			propertyEntity.setBaths(response.getResult().get(i).getBaths());
			propertyEntity.setBeds(response.getResult().get(i).getBeds());
			propertyEntity.setReview_count(response.getResult().get(i).getReview_count());
			propertyEntity.setProperty_rating(response.getResult().get(i).getProperty_rating());
			propertyEntity.setIsfavourite(response.getResult().get(i).getIsfavourite());
			propertyEntity.setSquare_footage(response.getResult().get(i).getSquare_footage());
			propertyEntity.setProperty_datetime(response.getResult().get(i).getProperty_datetime());
			mProperty.add(propertyEntity);

		}
		if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Latest Updates")) {
			Collections.sort(mProperty, PropertyEntity.DATE_SORT);
		} else if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Rating")) {
			Collections.sort(mProperty, PropertyEntity.RATING_SORT);
		} else if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Price (Low to High)")) {
			Collections.sort(mProperty, PropertyEntity.PRICE_MIN_TO_MAX_SORT);
		} else if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Price (High to Low)")) {
			Collections.sort(mProperty, PropertyEntity.PRICE_MAX_TO_MIN_SORT);
		} else if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Bed Rooms")) {
			Collections.sort(mProperty, PropertyEntity.BED_SORT);
		} else if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Baths")) {
			Collections.sort(mProperty, PropertyEntity.BATH_SORT);
		} else if (AppConstants.SELECTED_SORT_TYPE.equalsIgnoreCase("Square Feet (High to Low")) {
			Collections.sort(mProperty, PropertyEntity.SQUARE_FEET_SORT);
		}
		if (mGoogleMap != null) {
			mGoogleMap.clear();
		}
		mMap_progress_bar.setVisibility(View.VISIBLE);
		placeRandomLatandLongMarkers();
		mlistHandler.postDelayed(showListadap, 500);
		// ShowListAdapter();
		mFrame.setVisibility(View.GONE);
		mBottomLay.setVisibility(View.VISIBLE);
		if (AppConstants.isDeviceFirst) {
			AppConstants.isDeviceFirst = false;
			if (!UserID.equals("0") && !UserID.equals("")) {
				sendDeviceID(UserID, DeviceID, Device);
			}
		}
	}

	public void placeRandomLatandLongMarkers() {

		mMarker.clear();
		markersList.clear();
		latLngList.clear();
		mPrevMarker = null;

		LatLng latLng = null;
		mValidate1.clear();
		mMap_progress_bar.setVisibility(View.VISIBLE);
		for (int i = 0; i < mProperty.size(); i++) {
			try {
				latLng = new LatLng(Double.valueOf(mProperty.get(i).getLatitude()),
						Double.valueOf(mProperty.get(i).getLongitude()));
				mValidate1.add(latLng);
				count++;
				String am = "";
				String amount_ex = "";
				marker_view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.map_marker_blue, null);
				numTxt = (TextView) marker_view.findViewById(R.id.num_txt);
				numTxt.setTypeface(helvetica_bold);
				prop_type_icon = (Button) marker_view.findViewById(R.id.property_type_icon);
				try {
					propertyID = mProperty.get(i).getPro_id();
					if (mProperty.get(i).getPrice_range().toString().equals("")) {
						numTxt.setText("NA");
						am = "NA" + count;
					} else {
						amount_ex = mProperty.get(i).getPrice_range();
						am = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_ex));
						if (type_of_property.equals("rent")) {
							Integer validate = Integer.parseInt(mProperty.get(i).getPrice_range());
							if (validate <= 2) {
								numTxt.setText("$" + amount_ex);
							} else {
								numTxt.setText("$" + am);
							}
						} else {
							float amount_conversion = (Integer.parseInt(mProperty.get(i).getPrice_range()) / 1000.0f);
							double round_off = Math.floor(amount_conversion * 10) / 10;
							numTxt.setText("$" + round_off + " k");
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (mProperty.get(i).getRb_block().equalsIgnoreCase("1")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.exclusives_icon);
					prop_type_marker = "exclusive";
				} else if (mProperty.get(i).getOpen_house().equalsIgnoreCase("1")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.open_houses_icon);
					prop_type_marker = "openhouse";
				} else if (mProperty.get(i).getIsfavourite().equalsIgnoreCase("1")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.favourite_disable);
					prop_type_marker = "favourite";
				} else {
					prop_type_icon.setVisibility(View.GONE);
					prop_type_marker = "";
				}
				Marker marker1 = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
						.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_view))));

				MarkerID mMarkerid = new MarkerID();
				mMarkerid.setMarkerid(marker1.getId());

				if (type_of_property.equals("rent")) {
					amount_ex = mProperty.get(i).getPrice_range();
					am = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_ex));
					Integer validate = Integer.parseInt(mProperty.get(i).getPrice_range());
					if (validate <= 2) {
						mMarkerid.setMarkername("$" + amount_ex);
					} else {

						mMarkerid.setMarkername("$" + am);
					}
				} else {
					float amount_conversion = (Integer.parseInt(mProperty.get(i).getPrice_range()) / 1000.0f);
					double round_off = Math.floor(amount_conversion * 10) / 10;
					mMarkerid.setMarkername("$" + round_off + " k");
				}
				mMarkerid.setMarkerpropType(prop_type_marker);
				mMarker.add(mMarkerid);
				markersList.add(marker1);
				latLngList.add(latLng);

				mProperty.get(i).setLatitude(mProperty.get(i).getLatitude());
				mProperty.get(i).setLongitude(mProperty.get(i).getLongitude());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mMap_progress_bar.setVisibility(View.GONE);
		mLocProgress.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mGoogleMap = googleMap;
		initilizeMap();
	}



	class callZillowPropertyImages extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			property_image.clear();
			for (int i = 0; i < map_loc.size(); i++) {
				try {
					URL urls = new URL("http://www.zillow.com/webservice/GetUpdatedPropertyDetails.htm?"
							+ "zws-id=X1-ZWz1ae6cp8nhmz_32ywy&zpid=" + map_loc.get(i).getZpid() + "");
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse(new InputSource(urls.openStream()));
					doc.getDocumentElement().normalize();
					NodeList nodeList = doc.getElementsByTagName("image");
					for (int k = 0; k < nodeList.getLength(); k++) {

						property_ent = new PropertyImageEntity();
						Node node = nodeList.item(k);

						Element fstElmnt = (Element) node;

						NodeList children = fstElmnt.getChildNodes();
						for (int j = 0; j < children.getLength(); j++) {
							Node child = children.item(j);
							Element fElement = (Element) child;
							if (j == 0) {
								property_ent.setUrl1(getImageValue("url", fElement).toString());
							} else if (j == 1) {
								property_ent.setUrl2(getImageValue("url", fElement).toString());
							} else if (j == 2) {
								property_ent.setUrl3(getImageValue("url", fElement).toString());
							} else if (j == 3) {
								property_ent.setUrl4(getImageValue("url", fElement).toString());
							} else if (j == 4) {
								property_ent.setUrl5(getImageValue("url", fElement).toString());
							}
						}
						property_image.add(property_ent);
					}
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			property_image.size();
			// imageUrls.clear();
			// img_page_adapter.notifyDataSetChanged();
			// addImages();
			System.out.println(property_image.size());
		}
	}

	private static String getValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		return node.getNodeValue();
	}

	private static String getImageValue(String tag, Element element) {
		// String nodeList = element.getNodeValue();
		String nodeList = element.getChildNodes().item(0).getNodeValue();
		// Node node = (Node) nodeList.item(0);
		return nodeList;
	}

	public void ShowListAdapter() {
		List_view_container.removeAllViews();
		// mPropertyPics.clear();
		imageUrls.clear();
		for (int i = 0; i < mProperty.size(); i++) {
			position = i;
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View convertView = inflater.inflate(R.layout.image_slider_adapter_view, null);

			pager = (DirectionalViewPager) convertView.findViewById(R.id.pager);

			// pager.setOrientation(DirectionalViewPager.HORIZONTAL);
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(MapFragmentActivity.this));
			options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
					.cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

			bitmap = new Bitmap[mProperty.size()];

			propertyID = mProperty.get(i).getPro_id();
			mPropertyPics = mProperty.get(i).getProperty_pics();

			HashMap<Integer, ArrayList<String>> imageHash = new HashMap<Integer, ArrayList<String>>();
			ArrayList<String> singlePropImages = new ArrayList<String>();
			for (PropertyPictures propertyPictures : mProperty.get(i).getProperty_pics()) {
				if (propertyPictures.getFile() != null && !propertyPictures.getFile().equalsIgnoreCase("")) {
					singlePropImages.add(propertyPictures.getFile());
				} else {
					singlePropImages.add("http://smaatapps.com/rb/default_prop_icon.png");
				}
			}
			if (singlePropImages.size() != 0) {
				imageHash.put(0, singlePropImages);
			} else {
				singlePropImages.add("http://smaatapps.com/rb/default_prop_icon.png");
				imageHash.put(0, singlePropImages);
			}
			imageUrls.add(imageHash);

			dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			propertyImageListView.getLayoutParams().height = pagerHeight;

			pager.setAdapter(new ImagePagerAdapter(imageUrls.get(i).get(0), mProperty.get(i)));
			DetailOnPageChangeListener listener = new DetailOnPageChangeListener();
			pager.setOnPageChangeListener(listener);
			dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			pager.getLayoutParams().height = pagerHeight;
			List_view_container.addView(convertView);

		}
		if (imageUrls.size() > 0) {
			homeScreenListAdapter = new HomeScreenListAdapter(MapFragmentActivity.this, imageUrls, 0, 0, mProperty);
			propertyImageListView.setAdapter(homeScreenListAdapter);

		}
	}

	public void onImageTouch(String tag, int direction) {
		isMapMove = false;
		if (!tag.equalsIgnoreCase("") && tag.contains(",")) {

			switch (direction) {
				case CustomPagerAdapter.VIEW_PAGER_NEXT:
					viewPagerPosition = 0;
					listViewPosition = 0;
					positions = tag.split(",");

					listViewPosition = Integer.parseInt(positions[0]);

					viewPagerPosition = Integer.parseInt(positions[1]);
					++viewPagerPosition;
					AppConstants.mViewPagerPosition = viewPagerPosition;
					AppConstants.mListViewPosition = listViewPosition;
					break;
				case CustomPagerAdapter.VIEW_PAGER_PREVIOUS:
					viewPagerPosition = 0;
					listViewPosition = 0;
					positions = tag.split(",");

					listViewPosition = Integer.parseInt(positions[0]);

					viewPagerPosition = Integer.parseInt(positions[1]);
					--viewPagerPosition;
					AppConstants.mViewPagerPosition = viewPagerPosition;
					AppConstants.mListViewPosition = listViewPosition;
					break;
				case CustomPagerAdapter.LIST_NEXT:
					viewPagerPosition = 0;
					listViewPosition = 0;
					positions = tag.split(",");

					listViewPosition = Integer.parseInt(positions[0]);

					viewPagerPosition = Integer.parseInt(positions[1]);
					++listViewPosition;
					viewPagerPosition = 0;
					AppConstants.mViewPagerPosition = viewPagerPosition;
					AppConstants.mListViewPosition = listViewPosition;
					break;
				case CustomPagerAdapter.LIST_PREVIOUS:
					viewPagerPosition = 0;
					listViewPosition = 0;
					positions = tag.split(",");

					listViewPosition = Integer.parseInt(positions[0]);

					viewPagerPosition = Integer.parseInt(positions[1]);
					--listViewPosition;
					viewPagerPosition = 0;
					AppConstants.mViewPagerPosition = viewPagerPosition;
					AppConstants.mListViewPosition = listViewPosition;
					break;
			}
			homeScreenListAdapter = new HomeScreenListAdapter(MapFragmentActivity.this, imageUrls, listViewPosition,
					viewPagerPosition, mProperty);
			propertyImageListView.setAdapter(homeScreenListAdapter);
			propertyImageListView.setSelection(listViewPosition);
			if (markersList.size() > listViewPosition) {
				try {
					markerClick(markersList.get(listViewPosition));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void getDeviceHeight() {

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch (metrics.densityDpi) {

			case DisplayMetrics.DENSITY_LOW:
				deviceHeight = metrics.heightPixels;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				deviceHeight = metrics.heightPixels;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				deviceHeight = metrics.heightPixels;
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				deviceHeight = metrics.heightPixels;
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				deviceHeight = metrics.heightPixels;
				break;

		}
		minHeight = deviceHeight / 4;
		maxHeight = deviceHeight - minHeight;
		pagerHeight = maxHeight / 2;
	}

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			ad_view.setVisibility(View.GONE);
			mHandler.removeCallbacks(mRunnable);
		}
	};

	private void showHintDialog() {
		d1 = new Dialog(MapFragmentActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		d1.requestWindowFeature(Window.FEATURE_NO_TITLE);

		d1.setContentView(R.layout.search_location_hint_dialog);
		d1.setCancelable(false);
		Button close = (Button) d1.findViewById(R.id.close_btn);
		Button bottom = (Button) d1.findViewById(R.id.bottom_button);
		RelativeLayout dialo_lay = (RelativeLayout) d1.findViewById(R.id.hint_dialog_lay);
		save_Search.setTextColor(Color.parseColor("#0077fd"));

		dialo_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d1.cancel();
				save_Search.setTextColor(Color.parseColor("#000000"));
				// ad_view.setVisibility(View.VISIBLE);
				// mHandler = new Handler();
				// mHandler.postDelayed(mRunnable, mInterval);
			}
		});
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d1.cancel();
				save_Search.setTextColor(Color.parseColor("#000000"));
				// ad_view.setVisibility(View.VISIBLE);
				// mHandler = new Handler();
				// mHandler.postDelayed(mRunnable, mInterval);
			}
		});
		bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d1.cancel();
				save_Search.setTextColor(Color.parseColor("#000000"));
				// ad_view.setVisibility(View.VISIBLE);
				// mHandler = new Handler();
				// mHandler.postDelayed(mRunnable, mInterval);
			}
		});

		d1.show();
	}

	private void initViews() {

		mGoogleMapLayout = (FrameLayout) findViewById(R.id.map_view_layout);

		setDrawingView();
		mDrawButton = (Button) findViewById(R.id.draw_button);

		mDrawButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isEnable) {
					don_call_api = true;
					isMapMove = false;
					mGoogleMap.clear();
					mDrawButton.setBackgroundResource(R.drawable.finger_draw_disable);
					isEnable = true;
					mIsMapMoveable = !mIsMapMoveable;
					if (mIsMapMoveable) {
						mDrawingViews = new DrawingView(MapFragmentActivity.this);
						mGoogleMapLayout.addView(mDrawingViews, 0);
						VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
						double lat = vr.latLngBounds.getCenter().latitude;
						double longitude = vr.latLngBounds.getCenter().longitude;
						saved_latitude = lat;
						saved_longitude = longitude;
						// callGoogleApiService(lat, longitude);
						// callPropertyApi();
						// getPropertyLatLong(AppConstants.type_of_property_filter);
					}
				} else {
					don_call_api = false;
					isMapMove = true;
					mGoogleMap.clear();
					mDrawButton.setBackgroundResource(R.drawable.finger_draw_enable);
					isEnable = false;
					VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
					double lat = vr.latLngBounds.getCenter().latitude;
					double longitude = vr.latLngBounds.getCenter().longitude;
					callGoogleApiService(lat, longitude);
					// callPropertyApi();
					getPropertyLatLong(AppConstants.type_of_property_filter);
				}
			}
		});

		try {
			// Loading map
			// initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}
		mGoogleMapLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isMapMove = true;
				// long timeLastTouched = System.currentTimeMillis();
				// System.out.println("time_last_touched " +
				// timeLastTouched+"");
				// map_move_Handler.removeCallbacks(chatCheckingService);
				// map_move_Handler.postDelayed(chatCheckingService, 3000);
				return false;
			}
		});

	}

	private void setDrawingView() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GRAY);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);
	}

	public void drawMap() {

		drawShapeOptions = new PolygonOptions();
		drawShapeOptions.addAll(mLatLngList);
		drawShapeOptions.strokeColor(Color.BLACK);
		drawShapeOptions.strokeWidth(5);
		drawShapeOptions.fillColor(Color.GRAY);
		mDrawShape = mGoogleMap.addPolygon(drawShapeOptions);
		mIsMapMoveable = !mIsMapMoveable;
		mGoogleMapLayout.removeView(mDrawingViews);
		boolean is_add = false;
		position_map = new ArrayList<String>();
		for (int i = 0; i < mValidate1.size(); i++) {
			is_add = false;
			if (coordinateInRegion(mLatLngList, mValidate1.get(i))) {
				is_add = true;

			}
			if (!is_add) {
				position_map.add(i + "");
			}
		}

		for (int idx = position_map.size() - 1; idx >= 0; idx--) {
			try {
				mProperty.remove(Integer.parseInt(position_map.get(idx)));
			} catch (Exception e) {
				break;
			}
		}
		placeCircleMarkers();
		ShowListAdapter();
	}

	public void placeCircleMarkers() {

		mMarker.clear();
		markersList.clear();
		latLngList.clear();
		mPrevMarker = null;
		LatLng latLng = null;

		mValidate1.clear();
		for (int i = 0; i < mProperty.size(); i++) {
			try {
				latLng = new LatLng(Double.valueOf(mProperty.get(i).getLatitude()),
						Double.valueOf(mProperty.get(i).getLongitude()));
				mValidate1.add(latLng);
				count++;
				String am = "";
				String amount = "";
				String amount_ex = "";

				marker_view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.map_marker_blue, null);
				numTxt = (TextView) marker_view.findViewById(R.id.num_txt);
				numTxt.setTypeface(helvetica_bold);
				prop_type_icon = (Button) marker_view.findViewById(R.id.property_type_icon);
				try {
					propertyID = mProperty.get(i).getPro_id();
					if (mProperty.get(i).getPrice_range().toString().equals("")) {
						numTxt.setText("NA");
						am = "NA" + count;
					} else {
						amount_ex = mProperty.get(i).getPrice_range();
						am = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_ex));
						if (type_of_property.equals("rent")) {
							Integer validate = Integer.parseInt(mProperty.get(i).getPrice_range());
							if (validate <= 2) {
								numTxt.setText("$" + amount_ex);
							} else {
								numTxt.setText("$" + am);
							}
						} else {
							float amount_conversion = (Integer.parseInt(mProperty.get(i).getPrice_range()) / 1000.0f);
							double round_off = Math.floor(amount_conversion * 10) / 10;
							numTxt.setText("$" + round_off + " k");
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (mProperty.get(i).getRb_block().equalsIgnoreCase("1")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.exclusives_icon);
					prop_type_marker = "exclusive";
				} else if (mProperty.get(i).getOpen_house().equalsIgnoreCase("1")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.open_houses_icon);
					prop_type_marker = "openhouse";
				} else if (mProperty.get(i).getIsfavourite().equalsIgnoreCase("1")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.favourite_disable);
					prop_type_marker = "favourite";
				} else {
					prop_type_icon.setVisibility(View.GONE);
					prop_type_marker = "";
				}
				Marker marker1 = mGoogleMap.addMarker(new MarkerOptions().position(latLng)
						.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_view))));

				MarkerID mMarkerid = new MarkerID();
				mMarkerid.setMarkerid(marker1.getId());
				if (type_of_property.equals("rent")) {
					amount_ex = mProperty.get(i).getPrice_range();
					am = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_ex));
					Integer validate = Integer.parseInt(mProperty.get(i).getPrice_range());
					if (validate <= 2) {
						mMarkerid.setMarkername("$" + amount_ex);
					} else {

						mMarkerid.setMarkername("$" + am);
					}
				} else {
					float amount_conversion = (Integer.parseInt(mProperty.get(i).getPrice_range()) / 1000.0f);
					double round_off = Math.floor(amount_conversion * 10) / 10;
					mMarkerid.setMarkername("$" + round_off + " k");
				}
				mMarkerid.setMarkerpropType(prop_type_marker);
				mMarker.add(mMarkerid);
				markersList.add(marker1);
				latLngList.add(latLng);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (LatLng marker : drawShapeOptions.getPoints()) {
			builder.include(marker);
		}
		LatLngBounds bounds = builder.build();
		bounds.getCenter();
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 12));

		mLocProgress.setVisibility(View.INVISIBLE);
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initilizeMap() {
		//if (mGoogleMap == null) {
		//mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
		mGoogleMap.setOnMapClickListener(this);

		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				markerClick(arg0);
				return false;
			}
		});

		if (mGoogleMap == null) {
			Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		} else {
			CameraPosition cameraPosition;
			if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
			mGoogleMap.getUiSettings().setCompassEnabled(true);
			mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
			mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
			mGoogleMap.setOnMapLoadedCallback(this);

			mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {

				@Override
				public void onCameraChange(CameraPosition arg0) {

					float zoomLevel = mGoogleMap.getCameraPosition().zoom;

					if (mUPDATE_RESULT_AS_MAP_MOVES.equalsIgnoreCase("OFF")) {

					} else {
						if (isMapMove) {
							// mLocProgress.setVisibility(View.VISIBLE);
							VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
							double lat = vr.latLngBounds.getCenter().latitude;
							double longitude = vr.latLngBounds.getCenter().longitude;

							saved_latitude = vr.latLngBounds.getCenter().latitude;
							saved_longitude = vr.latLngBounds.getCenter().longitude;

							if (!don_call_api) {
								// if (!MAP_MOVE_FLAG) {
								if (map_arr_ent != null) {
									map_arr_ent.clear();
								}
								if (mProperty != null) {
									mProperty.clear();
								}
								aq().ajaxCancel();
								if (AppConstants.saved_search_Latitude.equals("0.0")) {
									callGoogleApiService(lat, longitude);
									getScreenCoordinates(lat, longitude);
								} else {
									callGoogleApiService(Double.valueOf(AppConstants.saved_search_Latitude),
											Double.valueOf(AppConstants.saved_search_Longitude));
									getScreenCoordinates(Double.valueOf(AppConstants.saved_search_Latitude),
											Double.valueOf(AppConstants.saved_search_Longitude));
								}
								map_move_Handler.removeCallbacks(chatCheckingService);
								map_move_Handler.postDelayed(chatCheckingService, 600);
								// getPropertyLatLong(AppConstants.type_of_property_filter);
								// }
							}
						}
					}

				}
			});

			String latitud = (String) GlobalMethods.getValueFromPreference(MapFragmentActivity.this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.Latitude);
			String longitud = (String) GlobalMethods.getValueFromPreference(MapFragmentActivity.this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.Longitude);
			if (AppConstants.saved_search_Latitude.equals("0.0")) {
				// if (AppConstants.Map_longitude.equalsIgnoreCase("")) {
				// latitud = String.valueOf(AppConstants.Map_longitude);
				// longitud = String.valueOf(AppConstants.Map_longitude);
				// } else {
				latitud = String.valueOf(tracker.getLatitude());
				longitud = String.valueOf(tracker.getLongitude());
				// }
			} else {
				latitud = AppConstants.saved_search_Latitude;
				longitud = AppConstants.saved_search_Longitude;
			}
			LatLng latLng = new LatLng(Double.valueOf(latitud), Double.valueOf(longitud));
			saved_latitude = Double.valueOf(latitud);
			saved_longitude = Double.valueOf(longitud);
			// Show the current location in Google Map
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			cameraPosition = new CameraPosition.Builder().target(latLng).zoom((float) 16).build();
			mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		}
		if (AppConstants.CALL_MAP.equalsIgnoreCase("false")) {
			// callPropertyApi();
			getPropertyLatLong(AppConstants.type_of_property_filter);
		}

		//	}
	}

	private void getScreenCoordinates(double lat, double longitude) {
		Display mdisp = getWindowManager().getDefaultDisplay();
		Point mdispSize = new Point();
		mdisp.getSize(mdispSize);
		int maxX = mdispSize.x;
		int maxY = mdispSize.y;

		Point x_y_points = new Point(0, maxY / 2);

		LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(x_y_points);
		double latitudes = latLng.latitude;

		double longitudes = latLng.longitude;

		calculateTheDistance(lat, longitude, latitudes, longitudes);
	}

	private void calculateTheDistance(double point_A_lat, double point_A_long, double point_B_lat,
									  double point_B_long) {
		Location locationA = new Location("point A");
		locationA.setLatitude(point_A_lat);
		locationA.setLongitude(point_A_long);
		Location locationB = new Location("point B");
		locationB.setLatitude(point_B_lat);
		locationB.setLongitude(point_B_long);
		map_distance = locationA.distanceTo(locationB) / 1000;
		System.out.println(map_distance + "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// initilizeMap();
	}

	boolean coordinateInRegion(List<LatLng> verts, LatLng coord) {
		int i, j;
		boolean isInside = false;

		int sides = verts.size();
		for (i = 0, j = sides - 1; i < sides; j = i++) {
			// verifying if your coordinate is inside your region
			if ((((verts.get(i).longitude <= coord.longitude) && (coord.longitude < verts.get(j).longitude))
					|| ((verts.get(j).longitude <= coord.longitude) && (coord.longitude < verts.get(i).longitude)))
					&& (coord.latitude < (verts.get(j).latitude - verts.get(i).latitude)
					* (coord.longitude - verts.get(i).longitude)
					/ (verts.get(j).latitude - verts.get(i).longitude) + verts.get(i).latitude)) {
				isInside = !isInside;
			}
		}
		return isInside;
	}

	public class DrawingView extends View {

		public int width;
		public int height;
		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;
		Context context;
		private Paint circlePaint;
		private Path circlePath;

		public DrawingView(Context c) {
			super(c);
			context = c;
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
			circlePaint = new Paint();
			circlePath = new Path();
			circlePaint.setAntiAlias(true);
			circlePaint.setColor(Color.BLUE);
			circlePaint.setStyle(Paint.Style.STROKE);
			circlePaint.setStrokeJoin(Paint.Join.MITER);
			circlePaint.setStrokeWidth(4f);

		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);

			mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);

		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaint);

			canvas.drawPath(circlePath, circlePaint);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		private void touch_start(float x, float y) {
			mPath.reset();
			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;

				circlePath.reset();
				circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
			}
		}

		private void touch_up() {
			mPath.lineTo(mX, mY);
			circlePath.reset();
			// commit the path to our offscreen
			mCanvas.drawPath(mPath, mPaint);
			// kill this so we don't double draw
			LatLng mll = new LatLng(lat, lon);
			LatLngBounds AUSTRALIA = new LatLngBounds(new LatLng(lat, lon), new LatLng(lat, lon));

			// mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mll,
			// 17));
			// mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(mll));

			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			int x_co = Math.round(x);
			int y_co = Math.round(y);

			mProjection = mGoogleMap.getProjection();
			Point x_y_points = new Point(x_co, y_co);

			LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(x_y_points);
			double latitude = latLng.latitude;

			double longitude = latLng.longitude;

			Point xy = new Point(x_co, y_co);
			LatLng mlat = mGoogleMap.getProjection().fromScreenLocation(xy);
			lat = mlat.latitude;
			lon = mlat.longitude;

			if (mIsMapMoveable) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						mLatLngList = new ArrayList<LatLng>();
						mLatLngList.add(new LatLng(latitude, longitude));

						touch_start(x, y);
						invalidate();
						break;
					case MotionEvent.ACTION_MOVE:
						touch_move(x, y);
						invalidate();
						mLatLngList.add(new LatLng(latitude, longitude));
						break;
					case MotionEvent.ACTION_UP:
						touch_up();
						invalidate();

						drawMap();

						break;
				}
				return true;
			}
			return true;
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

			case R.id.arc_slide_image:
				showarcDialog();
				break;
			case R.id.search:

				if (isClicked1) {
					mFilter.setVisibility(View.GONE);
					mEditLay.setVisibility(View.GONE);
					mSearchEdit.setText(area);
					// mSearchText.setText(area);
					fetchLatLongFromService latlng = new fetchLatLongFromService(
							mSearchEdit.getText().toString().trim().replaceAll("\\s+", ""));
					latlng.execute();
					isClicked1 = false;
				} else {
					Toast.makeText(MapFragmentActivity.this, "Please select location", Toast.LENGTH_LONG).show();
					isClicked1 = true;
				}
				hideSoftKeyboard(MapFragmentActivity.this);
				break;
			case R.id.show_current_location:
				showCurrentLocation("", "");

				break;
			case R.id.list:
				if (islistClick) {
					ShowProgressDialog();
					mFilter.setVisibility(View.VISIBLE);
					mEditLay.setVisibility(View.VISIBLE);
					mEditView.setVisibility(View.VISIBLE);
					mCancelText.setVisibility(View.GONE);
					mPropertyList.setVisibility(View.VISIBLE);
					mListText.setText("Map");
					ShowListAdapter();
					layers_txt.setText("Sort");
					islistClick = false;
					AppConstants.from_map_list = true;

				} else {
					layers_txt.setText("Layers");
					mPropertyList.setVisibility(View.GONE);
					mListText.setText("List");
					mEditLay.setVisibility(View.VISIBLE);
					mEditView.setVisibility(View.VISIBLE);
					mCancelText.setVisibility(View.GONE);
					mFilter.setVisibility(View.VISIBLE);
					islistClick = true;
					AppConstants.from_map_list = false;
				}
				break;
			case R.id.save_Search:
				if (UserID.equals("") || UserID.equals("0")) {
					Intent test3 = new Intent(MapFragmentActivity.this, LoginActivity.class);
					startActivity(test3);
				} else {
					// SaveSearchDialog();
					showSaveSearchDialog();
				}

				break;
			case R.id.markers:
				break;
			case R.id.cancel:
				mCancelText.setVisibility(View.GONE);
				mFilter.setVisibility(View.VISIBLE);
				onMapClick(null);
				break;
			case R.id.layers:
				if (layers_txt.getText().toString().equalsIgnoreCase("Layers")) {
					showLayersDialog();
				} else {
					showSortingDialog();
				}
				break;
			default:
				break;
		}
	}

	private void showSortingDialog() {
		mDialog = new Dialog(MapFragmentActivity.this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.popup_property_type_new);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;

		Button mCancel, mSet;
		mSet = (Button) mDialog.findViewById(R.id.set);
		mCancel = (Button) mDialog.findViewById(R.id.cancel);
		mPropListLay = (LinearLayout) mDialog.findViewById(R.id.prop_list_lay);

		setPropListAdapter(mPropertyTypeList);

		mSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				onMapRequestSuccess(obj);
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});

		mDialog.show();
	}

	private void setPropListAdapter(final ArrayList<String> mPropertyTypeList) {

		mPropListLay.removeAllViews();

		if (mPropertyTypeList != null && mPropertyTypeList.size() > 0) {
			for (int i = 0; i < mPropertyTypeList.size(); i++) {

				final String mPropType = mPropertyTypeList.get(i);

				LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				final View convertView = inflater.inflate(R.layout.adapter_prop_type_list, null);
				TextView mPropTypeText = (TextView) convertView.findViewById(R.id.prop_type_text);
				final Button mPropTypeButton = (Button) convertView.findViewById(R.id.prop_type_btn);
				RelativeLayout mPropTypeLay = (RelativeLayout) convertView.findViewById(R.id.prop_type_lay);

				mPropItemBtn.add(mPropTypeButton);
				String type = AppConstants.SELECTED_SORT_TYPE;
				if (mPropertyTypeList.get(i).toString().equalsIgnoreCase(type)) {
					mPropTypeButton.setBackgroundResource(R.drawable.tick_on);
				}
				mPropTypeLay.setTag(i);
				mPropTypeLay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(String.valueOf(v.getTag()));
						for (Button btn : mPropItemBtn) {
							btn.setBackgroundResource(R.drawable.tick_off);
						}
						mPropTypeButton.setBackgroundResource(R.drawable.tick_on);
						AppConstants.SELECTED_SORT_TYPE = mPropertyTypeList.get(pos);
					}
				});
				mPropTypeText.setText(mPropType);

				mPropListLay.addView(convertView);

			}
		}
	}

	private void ShowProgressDialog() {
		progress = DialogManager.getProgressDialog(this);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				progress.dismiss();
			}
		}, 5000);

	}

	private void showCurrentLocation(final String lat, final String longi) {
		ishown = false;
		onMapClick(null);
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mGoogleMap.setMyLocationEnabled(true);
		mGoogleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

			@Override
			public void onMyLocationChange(Location arg0) {
				if (!ishown) {
					if (lat.equalsIgnoreCase("") || longi.equalsIgnoreCase("")) {
						cameraposition = new CameraPosition.Builder()
								.target(new LatLng(arg0.getLatitude(), arg0.getLongitude())).zoom((float) 16).build();
					} else {
						cameraposition = new CameraPosition.Builder()
								.target(new LatLng(Double.valueOf(lat), Double.valueOf(longi))).zoom((float) 16)
								.build();
					}
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraposition));
					ishown = true;
					// handler1.postDelayed(mRunnables, mInterval);
				}
			}
		});
	}

	private void CallSaveSearchService(String type) {

		String url = AppConstants.BASE_URL + "savesearch";
		GsonTransformer t = new GsonTransformer();
		Gson gson = new Gson();
		if (AppConstants.type_of_property_filter.equalsIgnoreCase("rent")) {
			SplashScreen.mLocaleRentFilterObjectEntity.setLatitude(Lat_check);
			SplashScreen.mLocaleRentFilterObjectEntity.setLongitude(Long_check);
			strjson = gson.toJson(SplashScreen.mLocaleRentFilterObjectEntity);
		} else if (AppConstants.type_of_property_filter.equalsIgnoreCase("sale")) {
			SplashScreen.mLocaleSellFilterObjectEntity.setLatitude(Lat_check);
			SplashScreen.mLocaleSellFilterObjectEntity.setLongitude(Long_check);
			strjson = gson.toJson(SplashScreen.mLocaleSellFilterObjectEntity);
		} else if (AppConstants.type_of_property_filter.equalsIgnoreCase("sold")) {
			strjson = gson.toJson(SplashScreen.mLocaleSoldFilterObjectEntity);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("filter_object", strjson);
		params.put("filter_type", type);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {

							DialogManager.showCustomAlertDialog(MapFragmentActivity.this, MapFragmentActivity.this,
									"Data Added Successfully.");
							System.out.println("saved search " + json);
							// mDialog.dismiss();
							// FriendsResponse obj = new
							// Gson().fromJson(
							// json.toString(),
							// FriendsResponse.class);
							// onSuccessRequest(obj);
							addsaveSearchindb();
						} else {
							statusErrorCode(status);
						}

					}
				});

	}

	private void addsaveSearchindb() {
		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();
		try {
			// String insertQuery = "INSERT INTO " +
			// RentersBlockDatabase.saved_search + " "
			// + "VALUES ('" + saved_search_input_text + "', '"
			// + "Sale" + "')";
			ContentValues values = new ContentValues();
			values.put("location", saved_search_input_text);
			values.put("property_type", "Sale");
			values.put("user_id", UserID);
			values.put("latitude", String.valueOf(saved_latitude));
			values.put("Longitude", String.valueOf(saved_longitude));
			db.insert(RentersBlockDatabase.saved_search, null, values);

			// db.execSQL(insertQuery);
			// System.out.println(insertQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager.getInstance().closeDatabase();
	}

	// private void onSuccessRequest(FriendsResponse response) {
	// Toast.makeText(MapFragmentActivity.this, "Success", Toast.LENGTH_SHORT)
	// .show();
	//
	// System.out.println(response);
	//
	// }

	private void showarcDialog() {
		d2 = new Dialog(MapFragmentActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		d2.requestWindowFeature(Window.FEATURE_NO_TITLE);

		d2.setContentView(R.layout.arc_menu_dialog);
		d2.setCancelable(true);

		RelativeLayout relay = (RelativeLayout) d2.findViewById(R.id.arc_lay);
		Button close_dialog = (Button) d2.findViewById(R.id.close_slide_image);
		TextView save_search = (TextView) d2.findViewById(R.id.save_Search);
		save_search.setTypeface(helvetica_light);

		TextView layers = (TextView) d2.findViewById(R.id.layers);
		layers.setTypeface(helvetica_light);

		TextView list = (TextView) d2.findViewById(R.id.list);
		list.setTypeface(helvetica_light);

		ImageView video = (ImageView) d2.findViewById(R.id.video_chat);
		ImageView review = (ImageView) d2.findViewById(R.id.reviews);
		ImageView photo_video = (ImageView) d2.findViewById(R.id.photo_video);

		video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d2.dismiss();
				AppConstants.from_profile_friends = "true";
				Intent inte = new Intent(MapFragmentActivity.this, FriendsMainScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		review.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.from_map_reviews = "true";
				Intent inte = new Intent(MapFragmentActivity.this, MyReviewActivity.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		photo_video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogManager.showCustomAlertDialog(MapFragmentActivity.this, MapFragmentActivity.this,
						"Select any property to add photos.");
			}
		});

		save_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d2.dismiss();
				arc_image_view.setBackgroundResource(R.drawable.bottom_menu_icon);
				showSaveSearchDialog();
			}
		});

		layers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d2.dismiss();
				arc_image_view.setBackgroundResource(R.drawable.bottom_menu_icon);
			}
		});

		list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d2.dismiss();
				arc_image_view.setBackgroundResource(R.drawable.bottom_menu_icon);
				if (islistClick) {
					ShowListAdapter();
					mPropertyList.setVisibility(View.VISIBLE);
					mGoogleMapLayout.setVisibility(View.GONE);
					mListText.setText("Map");
					islistClick = false;
					mFilter.setVisibility(View.GONE);
				} else {
					mPropertyList.setVisibility(View.GONE);
					mGoogleMapLayout.setVisibility(View.VISIBLE);
					mListText.setText("List");
					mFilter.setVisibility(View.VISIBLE);
					islistClick = true;
				}
			}
		});

		relay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d2.dismiss();
			}
		});

		close_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				d2.dismiss();
				arc_image_view.setBackgroundResource(R.drawable.bottom_menu_icon);
			}
		});

		d2.show();
	}

	@Override
	public void onLocationChanged(Location location) {

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		// mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onMapClick(LatLng arg0) {
		isMapMove = true;
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isAcceptingText()) {
			setupUI(rootView);
		} else {
		}
		mFrame.setVisibility(View.GONE);
		mBottomLay.setVisibility(View.VISIBLE);
		saved_search_frame_view.setVisibility(View.GONE);
		mSearchEdit.setHint(getString(R.string.enter_your_city));
		setupUI(rootView);
		isClicked = true;
	}

	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams());
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	public boolean markerClick(Marker v) {

		isMapMove = false;
		if (mPrevMarker != null) {
			View markerView1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.map_marker_light, null);
			TextView numTxt1 = (TextView) markerView1.findViewById(R.id.num_txt);
			numTxt1.setTypeface(helvetica_bold);
			Button prop_type_icon = (Button) markerView1.findViewById(R.id.property_type_icon);
			numTxt1.setText(mPrevMarkerId.getMarkername());
			if (mPrevMarkerId.getMarkerpropType().equals("exclusive")) {
				prop_type_icon.setVisibility(View.VISIBLE);
				prop_type_icon.setBackgroundResource(R.drawable.exclusives_icon);
			} else if (mPrevMarkerId.getMarkerpropType().equals("openhouse")) {
				prop_type_icon.setVisibility(View.VISIBLE);
				prop_type_icon.setBackgroundResource(R.drawable.open_houses_icon);
			} else if (mPrevMarkerId.getMarkerpropType().equals("favourite")) {
				prop_type_icon.setVisibility(View.VISIBLE);
				prop_type_icon.setBackgroundResource(R.drawable.favourite_disable);
			} else {
				prop_type_icon.setVisibility(View.GONE);
			}
			mPrevMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView1)));
		}

		View markerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.map_marker_dark, null);
		TextView numTxt = (TextView) markerView.findViewById(R.id.num_txt);
		numTxt.setTypeface(helvetica_bold);
		Button prop_type_icon = (Button) markerView.findViewById(R.id.property_type_icon);

		int i = -1;
		for (MarkerID markerId : mMarker) {
			i++;
			if (v.getId().equalsIgnoreCase(markerId.getMarkerid())) {

				mPrevMarkerId = markerId;
				mPrevMarker = v;
				numTxt.setText(markerId.getMarkername());
				if (markerId.getMarkerpropType().equals("exclusive")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.exclusives_icon);
				} else if (markerId.getMarkerpropType().equals("openhouse")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.open_houses_icon);
				} else if (markerId.getMarkerpropType().equals("favourite")) {
					prop_type_icon.setVisibility(View.VISIBLE);
					prop_type_icon.setBackgroundResource(R.drawable.favourite_disable);
				} else {
					prop_type_icon.setVisibility(View.GONE);
				}

				if (latLngList.size() > i) {
					LatLng latLng = latLngList.get(i);

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(Double.valueOf(latLng.latitude), Double.valueOf(latLng.longitude)))
							.zoom((float) 16).build();

					// mGoogleMap.animateCamera(CameraUpdateFactory
					// .newCameraPosition(cameraPosition));
				}

				if (imageUrls.size() > 0) {
					homeScreenListAdapter = new HomeScreenListAdapter(MapFragmentActivity.this, imageUrls, i, 0,
							mProperty);
					propertyImageListView.setAdapter(homeScreenListAdapter);
					propertyImageListView.setSelection(i);
					break;

				}
			}

		}

		v.setIcon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, markerView)));

		mFrame.setVisibility(View.VISIBLE);
		mBottomLay.setVisibility(View.GONE);

		return true;
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
		// ((MapFragmentActivity) mContext).callPlaces(location);
		((OnMapClickListener) mContext).onMapClick(null);
		mSearchEdit.setText(location);
		((MapFragmentActivity) mContext).setupUI(rootView);
		((MapFragmentActivity) mContext).saved_search_frame_view.setVisibility(View.GONE);
		((MapFragmentActivity) mContext).mCancelText.setVisibility(View.GONE);
		((MapFragmentActivity) mContext).mFilter.setVisibility(View.VISIBLE);
	}

//	private void callPlaces(String place) {
//
//		ServieRequestHandler.getInstance().getResultPlaces(getURL(AppConstants.PLACES_API_BASE, place), aq(),
//				MapFragmentActivity.this, null, null, this);
//	}

	private void callPlaces(CharSequence charSequence) {
		getStringValue(getURL(AppConstants.API_KEY, charSequence.toString()), mAQuery(), null, null);
	}
	private void getStringValue(String url, AQuery mAQuery, Map<String, Object> params, final Class<?> reDirectoryActivity) {
		GsonTransformer mGsonTransformer = new GsonTransformer();
		mAQuery.transformer(mGsonTransformer).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			public void callback(String url, JSONObject profile, AjaxStatus status) {
				try {
					PlaceRespone response = new Gson().fromJson(profile.toString(), PlaceRespone.class);
					if (response != null) {
						String[] from = new String[]{"description"};
						int[] to = new int[]{android.R.id.text1};
						placesList = new ArrayList<HashMap<String, String>>();
						places = new ArrayList<String>();
						for (Places placeDetials : response.predictions) {
							HashMap<String, String> place = new HashMap<String, String>();
							place.put("description", placeDetials.getDescription());
							place.put("_Id", placeDetials.getId());
							place.put("reference", placeDetials.getReference());
							placesList.add(place);
							places.add(placeDetials.getDescription());
						}
						SimpleAdapter adapter = new SimpleAdapter(MapFragmentActivity.this, placesList, android.R.layout.simple_list_item_1, from, to);
						mSearchEdit.setAdapter(adapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public static class GsonTransformer implements Transformer {

		@Override
		public <T> T transform(String s, Class<T> aClass, String s1, byte[] bytes, AjaxStatus ajaxStatus) {
			Gson gson = new Gson();
			return gson.fromJson(new String(bytes), aClass);
		}
	}

	protected AQuery mAQuery() {
		if (mAQuery == null) {
			mAQuery = new AQuery(this);
		}
		return mAQuery;
	}

	@Override
	public void onRequestSuccess(PlaceRespone obj) {
		if (mArea != null && mArea.size() > 0) {
			mArea.clear();
		}
		if (obj != null) {
			String[] from = new String[]{"description"};
			int[] to = new int[]{R.id.location_txt};
			List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
			local_save_search.clear();
			for (Places placeDetail : obj.predictions) {
				HashMap<String, String> place = new HashMap<String, String>();

				place.put("description", placeDetail.getDescription());
				place.put("place_id", placeDetail.getId());
				place.put("reference", placeDetail.getReference());
				placesList.add(place);
				local_search_ent = new LocalSavedSearchEntity();
				local_search_ent.setLocation(placeDetail.getDescription());
				local_save_search.add(local_search_ent);
				mArea.add(placeDetail.getDescription());
			}
			AppConstants.from_api = true;
			saveSearchAdapter = new MapSavedSearchAdapter(MapFragmentActivity.this,
					R.layout.save_search_custom_adapter_place_list, local_save_search);
			saved_Search_list.setAdapter(saveSearchAdapter);
			saveSearchAdapter.notifyDataSetChanged();
			// SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
			// placesList, R.layout.save_search_custm_adp, from, to);

			// mSearchEdit.setAdapter(adapter);

		}
		super.onRequestSuccess(obj);

	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			if (mArea != null) {
				area = mArea.get(position);
				mSearchEdit.setText(area);
			}
		}
	};

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
	// long arg3) {
	// if (mArea != null) {
	// area = mArea.get(position);
	// mSearchEdit.setText(area);
	// }
	//
	// }


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
							long arg3) {
		String area = placesList.get(position).get("description").toString();
		// if (mArray.length > 0) {
//		String area = mArray[0];
		mSearchEdit.setText(area);
//            if (mArray[1] != null) {
//                area = area + " " + mArray[1];
//            }
//            if (mArray[2] != null) {
//                area = area + " " + mArray[2];
//            }
		getLatLong(aq(), null, placesList.get(position).get("description").toString());
	}

	public void getLatLong(AQuery aq,
						   Map<String, Object> params, String address) {


		String add = address.replace(" ", "+");
		String url = "https://maps.googleapis.com/maps/api/geocode/json?&address="
				+ add + "&sensor=true";
		GsonTransformer t = new GsonTransformer();
		aq.transformer(t).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {
					public void callback(String url, JSONObject profile,
										 AjaxStatus status) {

						try {

							JSONArray jsonArray = GlobalMethods.getJsonArrayFromJsonObject(profile, "results");

							double lng = (jsonArray.getJSONObject(0).getJSONObject("geometry")
									.getJSONObject("location").getDouble("lng"));

							double lat = (jsonArray.getJSONObject(0).getJSONObject("geometry")
									.getJSONObject("location").getDouble("lat"));


							if (lat != 0 && lng != 0) {
								LatLng curloc = new LatLng(lat, lng);
								CameraUpdate center = CameraUpdateFactory.newLatLng(curloc);
								mGoogleMap.moveCamera(center);
								CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
								mGoogleMap.animateCamera(zoom);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
	}

	public class fetchLatLongFromService extends AsyncTask<Void, Void, StringBuilder> {
		String place;
		private int o;

		public fetchLatLongFromService(String place) {
			super();
			this.place = place;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			this.cancel(true);
		}

		@Override
		protected StringBuilder doInBackground(Void... params) {
			try {
				HttpURLConnection conn = null;
				StringBuilder jsonResults = new StringBuilder();
				String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=" + this.place
						+ "&sensor=false";

				URL url = new URL(googleMapUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				InputStreamReader in = new InputStreamReader(conn.getInputStream());
				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
				return jsonResults;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(StringBuilder result) {

			super.onPostExecute(result);
			try {
				if (result != null) {
					JSONObject jsonObj = new JSONObject(result.toString());
					JSONArray resultJsonArray = jsonObj.getJSONArray("results");
					JSONObject before_geometry_jsonObj = resultJsonArray.getJSONObject(o);
					JSONObject geometry_jsonObj = before_geometry_jsonObj.getJSONObject("geometry");
					JSONObject location_jsonObj = geometry_jsonObj.getJSONObject("location");
					String lat_helper = location_jsonObj.getString("lat");
					double lat = Double.valueOf(lat_helper);
					String lng_helper = location_jsonObj.getString("lng");
					double lng = Double.valueOf(lng_helper);

					if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					mGoogleMap.setMyLocationEnabled(true);
					// Double.valueOf("36.222711"),
					// Double.valueOf("-115.261436")))
					mLatlng = new LatLng(lat, lng);
					AppConstants.Map_longitude = lat_helper;
					AppConstants.Map_longitude = lng_helper;
					// marker2 = mGoogleMap.addMarker(new MarkerOptions()
					// .position(mLatlng).icon(
					// BitmapDescriptorFactory.defaultMarker()));

					AppConstants.saved_search_Latitude = String.valueOf(lat);
					AppConstants.saved_search_Longitude = String.valueOf(lng);

					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLatlng));
					saved_search_frame_view.setVisibility(View.GONE);
					// saved_search_frame_view.startAnimation(mFromTop);
					hideSoftKeyboard(MapFragmentActivity.this);
					mCancelText.setVisibility(View.GONE);
					mFilter.setVisibility(View.VISIBLE);
					// callGoogleApiService(lat, lng);
					// mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					// mLatlng, 11));
				}
			} catch (JSONException e) {
				e.printStackTrace();

			}
		}
	}

	private void showLayersDialog() {
		mDialog = new Dialog(MapFragmentActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.popup_layers);
		mDialog.setCancelable(false);
		Button done = (Button) mDialog.findViewById(R.id.done);
		RelativeLayout mDefaultLay, mSateliteLay, mHybridLay;
		final ToggleButton togDefault, togSatelite, togHybrid;

		mDefaultLay = (RelativeLayout) mDialog.findViewById(R.id.default_lay);
		mSateliteLay = (RelativeLayout) mDialog.findViewById(R.id.satelite_lay);
		mHybridLay = (RelativeLayout) mDialog.findViewById(R.id.hybrid_lay);

		togDefault = (ToggleButton) mDialog.findViewById(R.id.tog_default);
		togSatelite = (ToggleButton) mDialog.findViewById(R.id.tog_satelite);
		togHybrid = (ToggleButton) mDialog.findViewById(R.id.tog_hybrid);

		if (AppConstants.LAYER_TYPE.equalsIgnoreCase("Satelite")) {

			togDefault.setBackgroundResource(R.drawable.radio_disable);
			togSatelite.setBackgroundResource(R.drawable.radio_enable);
			togHybrid.setBackgroundResource(R.drawable.radio_disable);

		} else if (AppConstants.LAYER_TYPE.equalsIgnoreCase("Hybrid")) {
			togDefault.setBackgroundResource(R.drawable.radio_disable);
			togSatelite.setBackgroundResource(R.drawable.radio_disable);
			togHybrid.setBackgroundResource(R.drawable.radio_enable);

		} else if (AppConstants.LAYER_TYPE.equalsIgnoreCase("Default")) {
			togDefault.setBackgroundResource(R.drawable.radio_enable);
			togSatelite.setBackgroundResource(R.drawable.radio_disable);
			togHybrid.setBackgroundResource(R.drawable.radio_disable);
		}
		mDefaultLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.LAYER_TYPE = "Default";
				togDefault.setBackgroundResource(R.drawable.radio_enable);
				togSatelite.setBackgroundResource(R.drawable.radio_disable);
				togHybrid.setBackgroundResource(R.drawable.radio_disable);
			}
		});
		mSateliteLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.LAYER_TYPE = "Satelite";
				togDefault.setBackgroundResource(R.drawable.radio_disable);
				togSatelite.setBackgroundResource(R.drawable.radio_enable);
				togHybrid.setBackgroundResource(R.drawable.radio_disable);
			}
		});
		mHybridLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppConstants.LAYER_TYPE = "Hybrid";
				togDefault.setBackgroundResource(R.drawable.radio_disable);
				togSatelite.setBackgroundResource(R.drawable.radio_disable);
				togHybrid.setBackgroundResource(R.drawable.radio_enable);
			}
		});

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				if (AppConstants.LAYER_TYPE.equalsIgnoreCase("Satelite")) {
					mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					mCount = 1;
				} else if (AppConstants.LAYER_TYPE.equalsIgnoreCase("Hybrid")) {
					mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					mCount = 2;
				} else if (AppConstants.LAYER_TYPE.equalsIgnoreCase("Default")) {
					mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					mCount = 0;
				}
			}
		});

		mDialog.show();
	}

	private void showSaveSearchDialog() {
		d3 = new Dialog(MapFragmentActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.save_search_dialog);
		d3.setCancelable(false);
		Button cancel = (Button) d3.findViewById(R.id.cancel);
		Button save = (Button) d3.findViewById(R.id.save);
		mLocation = (EditText) d3.findViewById(R.id.enter_search_name);
		mLocation.setText(mSearchEdit.getText().toString().trim());

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d3.dismiss();

			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String type = "";
				saved_search_input_text = mLocation.getText().toString();
				if (AppConstants.type_of_property_filter.equalsIgnoreCase("rent")) {
					SplashScreen.mLocaleRentFilterObjectEntity.setFilter_name(saved_search_input_text);
					SplashScreen.mLocaleRentFilterObjectEntity.setProperty_type("rent");
					type = "Rent";
				} else if (AppConstants.type_of_property_filter.equalsIgnoreCase("sale")) {
					SplashScreen.mLocaleSellFilterObjectEntity.setFilter_name(saved_search_input_text);
					SplashScreen.mLocaleSellFilterObjectEntity.setProperty_type("sale");
					type = "Sale";
				} else if (AppConstants.type_of_property_filter.equalsIgnoreCase("sold")) {
					SplashScreen.mLocaleSoldFilterObjectEntity.setFilter_name(saved_search_input_text);
					SplashScreen.mLocaleSoldFilterObjectEntity.setProperty_type("sold");
				}
				d3.dismiss();
				CallSaveSearchService(type);
			}
		});

		d3.show();
	}

	public String getAddress(Context ctx, double latitude, double longitude) {
		StringBuilder result = new StringBuilder();
		try {
			Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);

				String locality = address.getLocality();
				String city = address.getCountryName();
				String region_code = address.getCountryCode();
				String zipcode = address.getPostalCode();
				double lat = address.getLatitude();
				double lon = address.getLongitude();

				result.append(locality + " ");
				result.append(city + " " + region_code + " ");
				result.append(zipcode);

			}
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}

		return result.toString();
	}

	private void callGoogleApiService(double latitude, double longitude) {

		Lat_check = String.valueOf(latitude);
		Long_check = String.valueOf(longitude);

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
			String formatted_address = response.getResults().get(0).getFormatted_address().toString();
			String direction = "";
			String[] separated = formatted_address.split(",");
			int size = separated.length;
			// mGoogleMap.clear();
			String city = separated[size - 3];
			String state = separated[size - 2];
			city = city.replaceAll("[0-9]", "");
			state = state.replaceAll("[0-9]", "");
			mSearchEdit.setText(city + ", " + state);
		} catch (Exception e) {
			mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// finish();
	}

	public void centerMapAt(LatLng latLng) {
		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	}

	public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = mGoogleMap.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed / duration);
				double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
				double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} else {
					if (hideMarker) {
						marker.setVisible(false);
					} else {
						marker.setVisible(true);
					}
				}
			}
		});
	}

	// public void mapDetails() {
	//
	// LatLng latLng = null;
	//
	// if (!map_arr_ent.isEmpty()) {
	// LatLng mll = new LatLng(Double.valueOf(map_arr_ent.get(0)
	// .getLatitude()), Double.valueOf(map_arr_ent.get(0)
	// .getLongitude()));
	// mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mll, 15));
	// }
	// for (int i = 0; i < map_arr_ent.size(); i++) {
	// try {
	//
	// latLng = new LatLng(Double.valueOf(map_arr_ent.get(i)
	// .getLatitude()), Double.valueOf(map_arr_ent.get(i)
	// .getLongitude()));
	// count++;
	// String am = "";
	//
	// marker_view = ((LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE))
	// .inflate(R.layout.list_item, null);
	// numTxt = (TextView) marker_view.findViewById(R.id.num_txt);
	// // ImageView img = (ImageView) marker_view
	// // .findViewById(R.id.marker);
	// try {
	// if (map_arr_ent.get(i).getPrice_range().toString()
	// .equals("")) {
	// numTxt.setText("NA");
	// am = "NA" + count;
	// } else {
	// System.out.println(NumberFormat.getIntegerInstance()
	// .format(Integer.parseInt(map_arr_ent.get(i)
	// .getPrice_range())));
	// String amount = NumberFormat.getIntegerInstance()
	// .format(Integer.parseInt(map_arr_ent.get(i)
	// .getPrice_range()));
	// am = amount.substring(0, amount.lastIndexOf(","));
	// propertyID = map_arr_ent.get(i).getPro_id();
	// numTxt.setText("$" + am + " k");
	// }
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// marker1 = mGoogleMap.addMarker(new MarkerOptions().position(
	// latLng).icon(
	// BitmapDescriptorFactory
	// .fromBitmap(createDrawableFromView(this,
	// marker_view))));
	// loc_marker.put(marker1.getId(), numTxt);
	// mMarkerid = new MarkerID();
	// mMarkerid.setMarkerid(marker1.getId());
	// mMarkerid.setMarkername("$" + am + " k");
	// mMarker.add(mMarkerid);
	//
	// mGoogleMap.setOnMarkerClickListener(this);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public void mapDetails1() {
	// LatLng latLng = null;
	// if (mProperty != null) {
	// count = 0;
	// loc_marker = new HashMap<String, TextView>();
	// loc_img_view = new HashMap<String, ImageView>();
	// mMarker = new ArrayList<MarkerID>();
	//
	// for (int i = 0; i <= 10; i++) {
	// propertyEntity = new PropertyEntity();
	// Random random = new Random();
	// double radiusInDegrees = 20 / 111000f;
	//
	// double u = random.nextDouble();
	// double v = random.nextDouble();
	// double w = radiusInDegrees * Math.sqrt(u);
	// double t = 2 * Math.PI * v;
	// double x = w * Math.cos(t);
	// double y = w * Math.sin(t);
	//
	// // Adjust the x-coordinate for the shrinking of the east-west
	// // distances
	// double new_x = x / Math.cos(saved_longitude);
	//
	// double foundLongitude = new_x + saved_latitude;
	// double foundLatitude = y + saved_longitude;
	// System.out.println("Longitude: " + foundLongitude
	// + " Latitude: " + foundLatitude);
	//
	// saved_latitude = foundLatitude;
	// saved_longitude = foundLongitude;
	// if (i < 5) {
	// propertyEntity.setPrice_range("2000");
	// } else {
	// propertyEntity.setPrice_range("3000");
	// }
	// mProperty.get(i).setLatitude(String.valueOf(saved_latitude));
	// mProperty.get(i).setLongitude(String.valueOf(saved_longitude));
	// // propertyEntity.setLatitude(String.valueOf(saved_latitude));
	// // propertyEntity.setLongitude(String.valueOf(saved_longitude));
	// // mProperty.add(propertyEntity);
	//
	// }
	// for (int i = 0; i < mProperty.size(); i++) {
	// try {
	//
	// latLng = new LatLng(Double.valueOf(mProperty.get(i)
	// .getLatitude()), Double.valueOf(mProperty.get(i)
	// .getLongitude()));
	// count++;
	// String am = "";
	//
	// marker_view = ((LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE))
	// .inflate(R.layout.list_item, null);
	// numTxt = (TextView) marker_view.findViewById(R.id.num_txt);
	// // ImageView img = (ImageView) marker_view
	// // .findViewById(R.id.marker);
	// try {
	// if (mProperty.get(i).getPrice_range().toString()
	// .equals("")) {
	// numTxt.setText("NA");
	// am = "NA" + count;
	// } else {
	// System.out.println(NumberFormat
	// .getIntegerInstance().format(
	// Integer.parseInt(mProperty.get(i)
	// .getPrice_range())));
	// String amount = NumberFormat.getIntegerInstance()
	// .format(Integer.parseInt(mProperty.get(i)
	// .getPrice_range()));
	// am = amount.substring(0, amount.lastIndexOf(","));
	// propertyID = mProperty.get(i).getPro_id();
	// numTxt.setText("$" + am + " k");
	// }
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// marker1 = mGoogleMap.addMarker(new MarkerOptions()
	// .position(latLng).icon(
	// BitmapDescriptorFactory
	// .fromBitmap(createDrawableFromView(
	// this, marker_view))));
	// loc_marker.put(marker1.getId(), numTxt);
	// mMarkerid = new MarkerID();
	// mMarkerid.setMarkerid(marker1.getId());
	// mMarkerid.setMarkername("$" + am + " k");
	// mMarker.add(mMarkerid);
	//
	// mGoogleMap.setOnMarkerClickListener(this);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// mLocProgress.setVisibility(View.INVISIBLE);
	// }
	// }

	private void callPropertyApi() {
		String url = AppConstants.BASE_URL + "property";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(MapFragmentActivity.this)).ajax(url, params,
				JSONObject.class, new AjaxCallback<JSONObject>() {
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							System.out.println(json.toString());
						} else {
							statusErrorCode(status);
						}
						PropertyResponse obj = new Gson().fromJson(json.toString(), PropertyResponse.class);
						onSuccessResponse(obj);
					}

				});
	}

	protected void onSuccessResponse(PropertyResponse obj) {
		PropertyResponse propertyResponse = (PropertyResponse) obj;
		if (propertyResponse.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			mProperty = propertyResponse.getResult();
			map_arr_ent = new ArrayList<MapPropertDetailsEntity>();

			for (int i = 0; i < mProperty.size(); i++) {

				map_ent = new MapPropertDetailsEntity();

				map_ent.setLatitude(mProperty.get(i).getLatitude());
				map_ent.setLongitude(mProperty.get(i).getLongitude());
				map_ent.setPrice_range(mProperty.get(i).getPrice_range());
				map_ent.setPro_id(mProperty.get(i).getPro_id());

				map_arr_ent.add(map_ent);
			}
			placeRandomLatandLongMarkers();
			ShowListAdapter();
			mFrame.setVisibility(View.GONE);
			mBottomLay.setVisibility(View.VISIBLE);
		} else {
			DialogManager.showMessageDialog(MapFragmentActivity.this, propertyResponse.getMsg(),
					getString(R.string.ok));
		}
	}

	@Override
	public void onResponseError(String errorCode) {

	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		super.onRequestSuccess(responseObj);
		CommonResponse commonresponse = (CommonResponse) responseObj;
		if (commonresponse.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

		}
	}

	public void callADDFavouriteService(String UserId, String propId) {
		String url = AppConstants.BASE_URL + "addtofavorite";

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", UserId);
		params.put("property_id", propId);

		ServiceRequestHandler.getInstance().CommonService(url, aq(), this, params);

	}

	@Override
	public void onRequestSuccess(PropertyDetailResponse obj) {

	}

	class DetailOnPageChangeListener extends DirectionalViewPager.SimpleOnPageChangeListener {
		private int currentPage;

		@Override
		public void onPageSelected(int position) {
			isSwipe = true;
			currentPage = position;
			if (isVertical) {
				marker_view_shown = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.map_marker_dark, null);
				LatLng latLng = new LatLng(Double.valueOf(mProperty.get(position).getLatitude()),
						Double.valueOf(mProperty.get(position).getLongitude()));
				marker1 = mGoogleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
						.fromBitmap(createDrawableFromView(MapFragmentActivity.this, marker_view_shown))));
				AppConstants.marker_id = marker1.getId();
				selected_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.selected_list_item, null);
				numTxt = (TextView) selected_marker.findViewById(R.id.num_txt);
				mMarkerImg = (ImageView) selected_marker.findViewById(R.id.marker);
				numTxt.setText(mMarker.get(position).getMarkername());

				if (mMarker != null) {
					for (int i = 0; i < mMarker.size(); i++) {

						if (marker1.getId().equalsIgnoreCase(AppConstants.marker_id)) {
							numTxt.setText(mMarker.get(i).getMarkername());
						}

					}
				}

				isVertical = false;
			}

		}

		public int getCurrentPage() {
			return currentPage;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

			pager.getParent().requestDisallowInterceptTouchEvent(true);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<String> images;
		private PropertyEntity map_loc_info;

		ImagePagerAdapter(ArrayList<String> imageUrls, PropertyEntity propertyEntity) {
			this.images = imageUrls;
			this.map_loc_info = propertyEntity;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((DirectionalViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View imageLayout = inflater.inflate(R.layout.item_image_pager, null);
			final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final TextView property_amount = (TextView) imageLayout.findViewById(R.id.property_cost);
			property_amount.setTypeface(helvetica_bold);
			final TextView property_details = (TextView) imageLayout.findViewById(R.id.property_details);
			property_details.setTypeface(helvetica_normal);
			final TextView property_address = (TextView) imageLayout.findViewById(R.id.property_location);
			property_address.setTypeface(helvetica_normal);
			final TextView mDate = (TextView) imageLayout.findViewById(R.id.time);
			mDate.setTypeface(helvetica_normal);
			final TextView mReviewsCount = (TextView) imageLayout.findViewById(R.id.property_reviews);
			mReviewsCount.setTypeface(helvetica_normal);
			final RatingBar mRating = (RatingBar) imageLayout.findViewById(R.id.review_ratingbar);
			final ImageView mFav = (ImageView) imageLayout.findViewById(R.id.favourite);
			final RelativeLayout fram = (RelativeLayout) imageLayout.findViewById(R.id.propert_img_view);

			mDate.setText("Last Updated " + GlobalMethods.timeDiff(map_loc_info.getProperty_datetime()));

			System.out.println("Current pos in list.." + String.valueOf(currentPage));
			fram.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (isReview != null && isReview.equalsIgnoreCase("review")) {
						Intent writeReview = new Intent(MapFragmentActivity.this, WriteReviewActivity.class);
						writeReview.putExtra("ReviewType", "Post");
						writeReview.putExtra("Rating", "0.0");
						writeReview.putExtra("Comments", "");
						writeReview.putExtra("PropertyId", map_loc_info.getProperty_id());
						writeReview.putExtra("PropertyReviewId", "");
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						startActivity(writeReview);
					} else {
						String prop_marker = "";
						if (map_loc_info.getRb_block().equalsIgnoreCase("1")) {
							prop_marker = "exclusive";
						} else if (map_loc_info.getOpen_house().equalsIgnoreCase("1")) {
							prop_marker = "openhouse";
						} else if (map_loc_info.getIsfavourite().equalsIgnoreCase("1")) {
							prop_marker = "favourite";
						} else {
							prop_marker = "";
						}

						Intent intent = new Intent(MapFragmentActivity.this, PropertyDetailsActivity.class);
						intent.putExtra("PropertyId", map_loc_info.getProperty_id());
						intent.putExtra("PropType", prop_marker);
						// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						// finish();
					}
				}
			});

			String am = "";
			String amount_ex = "";
			try {
				if (map_loc_info.getPrice_range().equals("")) {
					property_amount.setText("NA");
				} else {

					amount_ex = map_loc_info.getPrice_range();
					am = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_ex));
					Integer validate = Integer.parseInt(map_loc_info.getPrice_range());
					if (validate <= 2) {
						property_amount.setText("$" + amount_ex + "/ mo");
					} else {
						property_amount.setText("$" + am + "/ mo");
					}

				}
				property_details
						.setText(map_loc_info.getBeds() + " " + "bed" + " " + map_loc_info.getBaths() + " " + "bath");
				if (map_loc_info.getReview_count() != null && (map_loc_info.getReview_count().equalsIgnoreCase(""))) {
					mReviewsCount.setText("( " + "0" + " )");
				} else {
					mReviewsCount.setText("( " + map_loc_info.getReview_count() + " )");
				}
				float propertyRating = (float) 0.0;
				if (map_loc_info.getProperty_rating() != null) {
					try {
						if (map_loc_info.getProperty_rating().equalsIgnoreCase("")) {
							mRating.setRating(0);
						}
					} catch (Exception e) {

					}
				}
				try {
					propertyRating = Float.parseFloat(map_loc_info.getProperty_rating());
				} catch (Exception e) {
					propertyRating = (float) 0.0;
				}
				mRating.setRating(propertyRating);

				if (map_loc_info.getIsfavourite().equalsIgnoreCase("1")) {
					mFav.setBackgroundResource(R.drawable.red_hear_icon_list);
				} else {
					mFav.setBackgroundResource(R.drawable.favourite_enable);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
			property_address.setText(map_loc_info.getAddress().toString());
			mFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					property_id = map_loc_info.getProperty_id();
					if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
						launchActivity(LoginActivity.class);
					} else {
						if (map_loc_info.getIsfavourite().equals("0")) {
							callADDFavouriteService(UserID, property_id);
							// mProperty.get(position).setIsfavourite("1");
							mFav.setBackgroundResource(R.drawable.red_hear_icon_list);
							isfavourite = false;
							for (int i = 0; i < mProperty.size(); i++) {
								if (property_id.equals(mProperty.get(i).getProperty_id())) {
									obj.getResult().get(i).setIsfavourite("1");
								}
							}
							notifyDataSetChanged();
						} else {
							callADDFavouriteService(UserID, property_id);
							mFav.setBackgroundResource(R.drawable.favourite_enable);
							// mProperty.get(position).setIsfavourite("0");
							isfavourite = true;
							for (int i = 0; i < mProperty.size(); i++) {
								if (property_id.equals(mProperty.get(i).getProperty_id())) {
									obj.getResult().get(i).setIsfavourite("0");
								}
							}
							notifyDataSetChanged();
						}
					}

				}
			});
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			imageLoader.displayImage(images.get(position), imageView, options, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted() {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(FailReason failReason) {

					spinner.setVisibility(View.GONE);
					// imageView.setImageResource(R.drawable.ic_launcher);
				}

				@Override
				public void onLoadingComplete(final Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
					try {
						Animation anim = AnimationUtils.loadAnimation(MapFragmentActivity.this, R.anim.fade_in);
						imageView.setAnimation(anim);
						anim.start();
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								int width = dm.widthPixels;
								int height = width * loadedImage.getHeight() / loadedImage.getWidth();
								imageView.getLayoutParams().width = width;
								imageView.getLayoutParams().height = height;
								imageView.setImageBitmap(loadedImage);
								bitmap[position] = loadedImage;
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(e);
					}
				}

				@Override
				public void onLoadingCancelled() {
					// Do nothing
				}
			});

			// if
			// (imageView.canScrollHorizontally(DirectionalViewPager.VERTICAL)){
			//
			// }
			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						downX = event.getX();
						downY = event.getY();
						return true;
					}
					case MotionEvent.ACTION_UP: {
						upX = event.getX();
						upY = event.getY();

						float deltaX = downX - upX;
						float deltaY = downY - upY;

						// swipe horizontal?
						if (Math.abs(deltaX) > Math.abs(deltaY)) {
							if (Math.abs(deltaX) > MIN_DISTANCE) {
								// left or right
								if (deltaX < 0) {
									onRightToLeftSwipe(position);
									return true;
								}
								if (deltaX > 0) {
									onLeftToRightSwipe(position);
									return true;
								}
							} else {
								Log.i(logTag, "Horizontal Swipe was only " + Math.abs(deltaX) + " long, need at least "
										+ MIN_DISTANCE);
								return false;
							}
						}
						// swipe vertical?
						else {
							if (Math.abs(deltaY) > MIN_DISTANCE) {
								// top or down
								if (deltaY < 0) {
									onBottomToTopSwipe(position);
									return true;
								}
								if (deltaY > 0) {
									onTopToBottomSwipe(position);
									return true;
								}
							} else {
								Log.i(logTag, "Vertical Swipe was only " + Math.abs(deltaX) + " long, need at least "
										+ MIN_DISTANCE);
								return false;
							}
						}

						return true;
					}
					}
					return true;
				}
			});

			((DirectionalViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}
	}

	public void onRightToLeftSwipe(int position) {

		Log.i(logTag, "RightToLeftSwipe!");
		isVertical = false;
		// pager1.setOrientation(DirectionalViewPager.HORIZONTAL);
		// pager1.setAdapter(new ImagePagerAdapter1(
		// imageUrls.get(position).get(0), mProperty.get(position)));
		// if (curr_position == 0) {
		// pager1.setCurrentItem(curr_position);
		// } else {
		// pager1.setCurrentItem(curr_position - 1);
		// }
		isSwipe = true;
	}

	public void onLeftToRightSwipe(int position) {
		Log.i(logTag, "LeftToRightSwipe!");
		isVertical = false;
		// pager1.setOrientation(DirectionalViewPager.HORIZONTAL);
		// pager1.setAdapter(new ImagePagerAdapter1(
		// imageUrls.get(position).get(0), mProperty.get(position)));
		// if (curr_position == mProperty.size()) {
		// pager1.setCurrentItem(curr_position);
		// } else {
		// pager1.setCurrentItem(curr_position + 1);
		// }

		isSwipe = true;
	}

	public void onTopToBottomSwipe(int position) {
		Log.i(logTag, "onTopToBottomSwipe!");

		isVertical = true;
		// pager1.setOrientation(DirectionalViewPager.VERTICAL);
		// pager1.setAdapter(new ImagePagerAdapter1(
		// imageUrls.get(position).get(0), mProperty.get(position)));
		// if (curr_position == 0) {
		// pager1.setCurrentItem(curr_position);
		// } else {
		// pager1.setCurrentItem(curr_position + 1);
		// }

		isSwipe = true;
	}

	public void onBottomToTopSwipe(int position) {
		Log.i(logTag, "onBottomToTopSwipe!");
		isVertical = true;
		// pager1.setOrientation(DirectionalViewPager.VERTICAL);
		// pager1.setAdapter(new ImagePagerAdapter1(
		// imageUrls.get(position).get(0), mProperty.get(position)));
		// if (curr_position == mProperty.size()) {
		// pager1.setCurrentItem(curr_position);
		// } else {
		// pager1.setCurrentItem(curr_position - 1);
		// }
		isSwipe = true;
	}

	class ImagePagerAdapter1 extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<String> images;
		private PropertyEntity map_loc_info;

		ImagePagerAdapter1(ArrayList<String> imageUrls, PropertyEntity map_loc) {
			this.images = imageUrls;
			this.map_loc_info = map_loc;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((DirectionalViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View imageLayout = inflater.inflate(R.layout.item_image_pager, null);
			final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			final TextView property_amount = (TextView) imageLayout.findViewById(R.id.property_cost);
			final TextView property_details = (TextView) imageLayout.findViewById(R.id.property_details);
			final TextView property_address = (TextView) imageLayout.findViewById(R.id.property_location);
			final TextView mDate = (TextView) imageLayout.findViewById(R.id.time);
			final TextView mReviewsCount = (TextView) imageLayout.findViewById(R.id.property_reviews);
			final RatingBar mRating = (RatingBar) imageLayout.findViewById(R.id.review_ratingbar);
			final ImageView mFav = (ImageView) imageLayout.findViewById(R.id.favourite);

			final RelativeLayout fram = (RelativeLayout) imageLayout.findViewById(R.id.propert_img_view);

			try {
				if (map_loc_info.getPrice_range().equals("")) {
					property_amount.setText("NA");
				} else {
					System.out.println(
							NumberFormat.getIntegerInstance().format(Integer.parseInt(map_loc_info.getPrice_range())));
					String amount = NumberFormat.getIntegerInstance()
							.format(Integer.parseInt(map_loc_info.getPrice_range()));
					String am = amount.substring(0, amount.lastIndexOf(","));
					if (type_of_property.equals("rent")) {
						property_amount.setText("$" + map_loc_info.getPrice_range());
					} else {
						property_amount.setText("$" + am + " k");
					}
					// property_amount.setText("$" + am + " k");
				}

				property_address.setText(map_loc_info.getAddress());
				property_details
						.setText(map_loc_info.getBeds() + " " + "bd" + " " + map_loc_info.getBaths() + " " + "ba");
				mDate.setText(GlobalMethods.timeDiff(map_loc_info.getProperty_posted_user_details().get(0).getDate()));

				if ((map_loc_info.getReview_count().equalsIgnoreCase(""))) {
					mReviewsCount.setText("0" + " " + "Review");
				}
				mReviewsCount.setText(map_loc_info.getReview_count() + " " + "Reviews");

				float propertyRating = (float) 0.0;
				if (map_loc_info.getProperty_rating().equalsIgnoreCase("")) {
					mRating.setRating(0);
				}

				try {
					propertyRating = Float.parseFloat(map_loc_info.getProperty_rating());
				} catch (Exception e) {
					propertyRating = (float) 0.0;
				}
				mRating.setRating(propertyRating);
			} catch (Exception e) {
				System.out.println(e);
			}
			mFav.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
						launchActivity(LoginActivity.class);
					} else {
						if (isfavourite) {
							callADDFavouriteService(UserID, property_id);
							mFav.setBackgroundResource(R.drawable.favourites_icon);
							isfavourite = false;
						} else {
							callADDFavouriteService(UserID, property_id);
							mFav.setBackgroundResource(R.drawable.favourite_enable);
							isfavourite = true;
						}
					}

				}
			});

			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			imageLoader.displayImage(images.get(position), imageView, options, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted() {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(FailReason failReason) {

					spinner.setVisibility(View.GONE);
					// imageView.setImageResource(R.drawable.ic_launcher);
				}

				@Override
				public void onLoadingComplete(final Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
					try {
						Animation anim = AnimationUtils.loadAnimation(MapFragmentActivity.this, R.anim.fade_in);
						imageView.setAnimation(anim);
						anim.start();
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								int width = dm.widthPixels;
								int height = width * loadedImage.getHeight() / loadedImage.getWidth();
								imageView.getLayoutParams().width = width;
								imageView.getLayoutParams().height = height;
								imageView.setImageBitmap(loadedImage);
								bitmap[position] = loadedImage;
							}
						});
					} catch (Exception e) {
						System.out.println(e);
					}
				}

				@Override
				public void onLoadingCancelled() {
					// Do nothing
				}
			});

			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						downX = event.getX();
						downY = event.getY();
						return true;
					}
					case MotionEvent.ACTION_UP: {
						upX = event.getX();
						upY = event.getY();

						float deltaX = downX - upX;
						float deltaY = downY - upY;

						// swipe horizontal?
						if (Math.abs(deltaX) > Math.abs(deltaY)) {
							if (Math.abs(deltaX) > MIN_DISTANCE) {
								// left or right
								if (deltaX < 0) {
									onRightToLeftSwipe(position);
									return true;
								}
								if (deltaX > 0) {
									onLeftToRightSwipe(position);
									return true;
								}
							} else {
								Log.i(logTag, "Horizontal Swipe was only " + Math.abs(deltaX) + " long, need at least "
										+ MIN_DISTANCE);
								return false; // We don't consume the event
							}
						}
						// swipe vertical?
						else {
							if (Math.abs(deltaY) > MIN_DISTANCE) {
								// top or down
								if (deltaY < 0) {
									onBottomToTopSwipe(position);
									return true;
								}
								if (deltaY > 0) {
									onTopToBottomSwipe(position);
									return true;
								}
							} else {
								Log.i(logTag, "Vertical Swipe was only " + Math.abs(deltaX) + " long, need at least "
										+ MIN_DISTANCE);
								return false; // We don't consume the event
							}
						}

						return true;
					}
					}
					return true;
				}
			});

			((DirectionalViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		// private int CalcPostion(int last) {
		// currentPage = pager1.getCurrentItem();
		// if ((last == currentPage) && (currentPage != 1)
		// && (currentPage != 0)) {
		// currentPage = currentPage + 1;
		// // pager1.setCurrentItem(currentPage);
		// }
		// if ((last == 1) && (currentPage == 1)) {
		// last = 0;
		// currentPage = 0;
		// }
		// return last = currentPage;
		// }

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
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
		onMapClick(null);
	}

	public void onSellerMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuSellerclick(v);
			slide_holder.toggle();
		}
		onMapClick(null);
	}

	public void onAgentMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuAgentClick(v);
			slide_holder.toggle();
		}
		onMapClick(null);
	}

	private void addSortingTypesList() {
		mPropertyTypeList.clear();
		mPropertyTypeList.add("Best Match");
		mPropertyTypeList.add("Latest Updates");
		mPropertyTypeList.add("Rating");
		mPropertyTypeList.add("Price (Low to High)");
		mPropertyTypeList.add("Price (High to Low)");
		mPropertyTypeList.add("Bed Rooms");
		mPropertyTypeList.add("Baths");
		mPropertyTypeList.add("Square Feet (High to Low)");
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {
	}

	@Override
	public void onOkclick() {
	}

	public static void callEnhancedtoStandarAPI() {
		// TODO Auto-generated method stub
		((MapFragmentActivity) mContext).updateEnhancetostd();
	}

	private void updateEnhancetostd() {
		String url = AppConstants.BASE_URL + "registration/enhancedtostandard";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							Result response = new Gson().fromJson(json.toString(), Result.class);
							DialogManager.showCustomAlertDialog(MapFragmentActivity.this, MapFragmentActivity.this,
									"You have been moved to a Standard user mode. "
											+ "Please enhance your Profile from My Profile Section.");
						} else {
							statusErrorCode(status);
						}

					}
				});
	}

	private Runnable showListadap = new Runnable() {

		@Override
		public void run() {
			ShowListAdapter();
			mlistHandler.removeCallbacks(showListadap);
		}
	};

	private Runnable chatCheckingService = new Runnable() {

		@Override
		public void run() {
			getPropertyLatLong(AppConstants.type_of_property_filter);
			map_move_Handler.removeCallbacks(chatCheckingService);
		}

	};

	private Runnable adsShowingservice = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ad_view.setVisibility(View.VISIBLE);
			ads_show_handler.removeCallbacks(adsShowingservice);
		}
	};

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		mMap_progress_bar.setVisibility(View.GONE);

	}
}

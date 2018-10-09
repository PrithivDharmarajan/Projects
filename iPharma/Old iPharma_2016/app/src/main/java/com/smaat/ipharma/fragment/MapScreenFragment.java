package com.smaat.ipharma.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.adapter.SearchAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.AdsEntity;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.MarkerID;
import com.smaat.ipharma.entity.PlaceResponse;
import com.smaat.ipharma.entity.Places;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.model.MapResponse;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.ui.SplashScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;

import static com.smaat.ipharma.ui.SplashScreen.latitude;
import static com.smaat.ipharma.ui.SplashScreen.longitude;

public class MapScreenFragment extends BaseFragment implements OnClickListener,
        DialogMangerCallback, LocationListener, OnMapClickListener,
        OnMarkerClickListener, OnItemClickListener, OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    SharedPreferences sharedPreferences;
    int locationCount = 0;
    double mIntLat, mIntLong;
    boolean isFirst = false;
    OnDragListener onDrag;
    ArrayList<MapPropertyEntity> mMapProperties;
    private FrameLayout mGoogleMapLayout, map_fragment_main;
    private List<LatLng> mLatLngList = new ArrayList<LatLng>();
    private boolean mIsMapMoveable = false;
    // private Polygon mDrawShape;
    // private Projection mProjection;
    private DrawingView mDrawingViews;
    private Paint mPaint;
    private ImageView mLocation;

    private String mLatitude, mLongitude;
    private View rootview;
    public static AutoCompleteTextView mPharmacyAddress;
    static ArrayList<MapPropertyEntity> mMapEntity;

    private ArrayList<String> places;
    private List<HashMap<String, String>> placesList;
    boolean isCurrent = true;
    private boolean ishown;
    private CameraPosition cameraposition;
    public static String add_txt;
    ListView searchList;
    static MapScreenFragment mcontext;
    ArrayList<Marker> mMarkerList = new ArrayList<Marker>();
    ArrayList<MarkerID> mMarkerID = new ArrayList<MarkerID>();
    GPSTracker tracker;
    private float map_distance = (float) 10.0;
    private float currentZoom = -1;
    private boolean isCalled = false;
    int premium_count = 0, normal_count = 0, total_count = 0;
    private TextView mPreminum, mAll, mNormal;
    private String UserID;
    private SearchAdapter searchAdpter;
    private LinearLayout mAdLayout, mCurrentLocation;
    private FrameLayout searchSugestionView;
    private static final int MAP_DURATION = 2000;
    private boolean isClicked = true;
    private int ads_count = 0;
    AdsEntity ads_local_entity = new AdsEntity();

    // private ArrayList<MapPropertyEntity> mPharmacyList = new
    // ArrayList<MapPropertyEntity>();
    private Handler mHandler;
    private ImageView mShop_icon;
    private TextView mShop_name, mShop_distance;
    public static ListView mMapListView;
    private boolean from_search = false;
    Bundle bundle;
    private Animation mInBottom, mOutBottom;
    private int curr_pos_marker;
    int pos = 0;
    private int _index = 1;
    private ImageView AdImg;
    Handler handler;
    ArrayList<MapPropertyEntity> mMapproplist = new ArrayList<MapPropertyEntity>();
    MapPropertyEntity mMappropent;
    Handler mainHandler;
    Runnable myRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (rootview != null) {

            if (container != null) {
                container.removeView(rootview);
            }

        }
        try {
            rootview = inflater.inflate(R.layout.map_fragment_view, container,
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootview;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SplashScreen.getUpdatedLocation(getActivity());
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        mViewGroup.requestTransparentRegion(mViewGroup);
        Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
                getActivity());
        setFont(mViewGroup, mTypeface);
        setupUI(mViewGroup);
        hideSoftKeyboard(getActivity());
        handler = new Handler();
        AdImg = (ImageView) view.findViewById(R.id.adLayout);
        handler.postDelayed(mRunnable, 0);


        UserID = GlobalMethods.getUserID(getActivity());
        getLocation();
        isCalled = false;
        // DialogManager.showProgress(getActivity());

        initComponents(view);
        AppConstants.FRAG = AppConstants.EXIT;
        mPharmacyAddress.setText(AppConstants.Search_text);
        initializeMap();

        HomeScreen.mHeaderText.setText("Your Nearest Pharmacy");
        if (tracker != null) {
            // callAdsService(tracker.getLatitude(), tracker.getLongitude());
        }
        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                HomeScreen.slideMenu();
            }
        });
        if (!AppConstants.Search_text.equalsIgnoreCase("")) {
            from_search = true;
            VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
            // callAdsService(vr.latLngBounds.getCenter().latitude,
            // vr.latLngBounds.getCenter().longitude);
            callMapService(vr.latLngBounds.getCenter().latitude + "",
                    vr.latLngBounds.getCenter().longitude + "",
                    AppConstants.FAILURE_CODE);
            hideSoftKeyboard(getActivity());
            AppConstants.from_map_list = AppConstants.FALSE;
        }

        // HomeScreen.mHeaderText.setTypeface(HomeScreen.mHelveticaBold);
    }

    private void iniLoadMapmarker() {
        if (AppConstants.FROMINTLOADMAP.equalsIgnoreCase(AppConstants.FROMMAP)) {
            callMapService(mIntLat + "", mIntLong + "",
                    AppConstants.FAILURE_CODE);
            AppConstants.FROMINTLOADMAP = AppConstants.SUCCESS_CODE;

        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacks(mRunnable);
    }

    @SuppressWarnings("unused")
    private void callAdsService(double latitude, double longitude) {
        APIRequestHandler.getInstance().getAds(
                String.valueOf(tracker.getLatitude()),
                String.valueOf(tracker.getLongitude()), this);
    }

    Runnable mRunnable = new Runnable() {

        public void run() {
            try {
                if (!(_index >= 3)) {
                    Bitmap bmp = BitmapFactory.decodeStream(getActivity()
                            .getAssets().open(
                                    "ads/ipharmaAd-" + _index + ".png"));
                    AdImg.setImageBitmap(bmp);
                    _index++;
                    handler.postDelayed(this, 6000);
                }
                if (_index == 3) {
                    _index = 1;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch blocks
                Log.v("", e.getMessage());
            }
        }
    };

    private void showAdsViewDuration() {
        mAdLayout.setVisibility(View.VISIBLE);
        mShop_name.setText(ads_local_entity.getResult().get(ads_count)
                .getShopName());
        if (ads_local_entity.getResult().get(ads_count).getDistance()
                .contains(".")) {
            DecimalFormat distance_roundoff = new DecimalFormat("#.##");
            mShop_distance.setText(Double.valueOf(distance_roundoff
                    .format(Double.valueOf(ads_local_entity.getResult()
                            .get(ads_count).getDistance())))
                    + " " + getString(R.string.Km));
        } else {
            mShop_distance.setText(ads_local_entity.getResult().get(ads_count)
                    + " " + getString(R.string.Km));
        }

        aq().id(mShop_icon).image(
                ads_local_entity.getResult().get(ads_count).getAdImage(), true,
                true, 0, R.mipmap.ic_launcher, null, 0, 1.0f);

        if (ads_local_entity.getResult().size() - 1 == ads_count) {
            ads_count = 0;
        } else {
            ads_count++;
        }
    }

    private void initComponents(View view) {

        AppConstants.MAP_CONT_VAL = AppConstants.MAP_SCREEN;
        AppConstants.FROM_MAPFAVORITE_SCREEN = AppConstants.MAP_SCREEN;
        mInBottom = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_from_bottom);
        mOutBottom = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_to_bottom);
        // mMapListBtn = (Button) getActivity().findViewById(R.id.header_right);

        map_fragment_main = (FrameLayout) view
                .findViewById(R.id.map_fragment_main);
        HomeScreen.mHeaderRight
                .setBackgroundResource(R.drawable.map_view_normal);
        mGoogleMapLayout = (FrameLayout) view
                .findViewById(R.id.map_fragment_lay);
        mGoogleMapLayout.removeView(mDrawingViews);
        mGoogleMapLayout.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                isCalled = false;
                return false;
            }
        });

        places = new ArrayList<String>();
        mMapListView = (ListView) view.findViewById(R.id.map_list_view);
        mMapListView.setVisibility(View.VISIBLE);
        mPreminum = (TextView) view.findViewById(R.id.Preminum);
        mPreminum.setTypeface(HomeScreen.mHelveticaBold);
        mPreminum.setOnClickListener(this);
        mPreminum.setText(getString(R.string.Premium_zero));
        mNormal = (TextView) view.findViewById(R.id.Normal);
        mNormal.setTypeface(HomeScreen.mHelveticaBold);
        mNormal.setOnClickListener(this);
        mNormal.setText(getString(R.string.Normal_zero));
        mAll = (TextView) view.findViewById(R.id.All);
        mAll.setTypeface(HomeScreen.mHelveticaBold);
        mAll.setOnClickListener(this);
        mAll.setText(getString(R.string.All_zero));

        TextView oneTouch = (TextView) view.findViewById(R.id.oneTouch);
        oneTouch.setOnClickListener(this);

        // mDrawButton = (Button) getActivity().findViewById(R.id.draw_button);
        mCurrentLocation = (LinearLayout) getActivity().findViewById(
                R.id.current_location_button);
        mLocation = (ImageView) view.findViewById(R.id.location);
        mLocation.setOnClickListener(this);
        mPharmacyAddress = (AutoCompleteTextView) view
                .findViewById(R.id.phar_address_edits);
        // mPharmacyAddress.setText(AppConstants.Search_text);
        mAdLayout = (LinearLayout) view.findViewById(R.id.adLayout3);

        mShop_icon = (ImageView) view.findViewById(R.id.ad_image);
        mShop_name = (TextView) view.findViewById(R.id.ads_shopname);
        mShop_distance = (TextView) view.findViewById(R.id.shop_distance);
        searchSugestionView = (FrameLayout) getActivity().findViewById(
                R.id.searchSugestionView);
        searchList = (ListView) view.findViewById(R.id.searchList);
        mPharmacyAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPharmacyAddress.getText().clear();
                showCurrentLocation("", "");
                callMapService(mIntLat + "", mIntLong + "",
                        AppConstants.FAILURE_CODE);
                AppConstants.FROMINTLOADMAP = AppConstants.SUCCESS_CODE;
            }
        });
        mPharmacyAddress
                .setOnEditorActionListener(new OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        from_search = true;
                        isClicked = false;
                        mPharmacyAddress.dismissDropDown();
                        VisibleRegion vr = mGoogleMap.getProjection()
                                .getVisibleRegion();
                        callSearchMapService(
                                vr.latLngBounds.getCenter().latitude + "",
                                vr.latLngBounds.getCenter().longitude + "",
                                AppConstants.FAILURE_CODE);
                        callGoogleApiService(
                                vr.latLngBounds.getCenter().latitude,
                                vr.latLngBounds.getCenter().longitude);
                        // new getPositionInfo().execute(mPharmacyAddress
                        // .getText().toString().trim());
                        // mPharmacyAddress.setText("");
                        hideSoftKeyboard(getActivity());
                        return true;
                    }
                });

        mPharmacyAddress.setThreshold(1);
        mPharmacyAddress.setOnItemClickListener(this);
        mPharmacyAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String mCityValue = mPharmacyAddress.getText().toString()
                        .trim();
                callPlaces(s);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        setDrawingView();

        HomeScreen.mHeaderRightLay.setVisibility(View.VISIBLE);

        HomeScreen.mHeaderRightLay.setOnClickListener(this);
        // mDrawButton.setOnClickListener(this);
        mCurrentLocation.setOnClickListener(this);

    }

    private void callPlaces(CharSequence s) {
        getResultPlaces(getURL(AppConstants.API_KEY, s.toString()), aq(), null,
                null);
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

                            JSONArray jsonArray = GlobalMethods.getJsonArrayFromJsonObject(profile,
                                    "results");

                            double lng = (jsonArray.getJSONObject(0).getJSONObject("geometry")
                                    .getJSONObject("location").getDouble("lng"));

                            double lat = (jsonArray.getJSONObject(0).getJSONObject("geometry")
                                    .getJSONObject("location").getDouble("lat"));


                            if (lat != 0 && lng != 0) {
                                mLatitude = String.valueOf(lat);
                                mLongitude = String.valueOf(lng);

                                AppConstants.Search_text = mPharmacyAddress.getText()
                                        .toString();
                                from_search = true;
                                isClicked = false;
                                VisibleRegion vr = mGoogleMap.getProjection()
                                        .getVisibleRegion();


                                callSearchMapService(mLatitude + "", mLongitude + "",
                                        AppConstants.FAILURE_CODE);

                            }

                        } catch (Exception e) {

                        }

                    }
                });
    }

    public void getResultPlaces(String url, AQuery aq,
                                Map<String, Object> params, final Class<?> reDirectActivity) {
        GsonTransformer t = new GsonTransformer();
        aq.transformer(t).ajax(url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {
                    public void callback(String url, JSONObject profile,
                                         AjaxStatus status) {
                        try {
                            PlaceResponse response = new Gson().fromJson(
                                    profile.toString(), PlaceResponse.class);
                            if (response != null) {
                                String[] from = new String[]{"description"};
                                int[] to = new int[]{android.R.id.text1};
                                placesList = new ArrayList<HashMap<String, String>>();

                                for (Places placeDetail : response.predictions) {
                                    HashMap<String, String> place = new HashMap<String, String>();

                                    place.put("description",
                                            placeDetail.getDescription());
                                    place.put("_id", placeDetail.getId());
                                    place.put("reference",
                                            placeDetail.getReference());
                                    placesList.add(place);
                                    places.add(placeDetail.getDescription());
                                }
                                SimpleAdapter adapter = new SimpleAdapter(
                                        getActivity(), placesList,
                                        android.R.layout.simple_list_item_1,
                                        from, to);
                                mPharmacyAddress.setAdapter(adapter);
                            }

                        } catch (Exception e) {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                }
                            });

                        }

                    }
                });
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

    ArrayList<MapPropertyEntity> searchItemList = new ArrayList<MapPropertyEntity>();

    private void getMatchedResult(String searchText) {

        searchItemList.clear();

        for (MapPropertyEntity shop : mMapEntity) {
            // shop name
            if (shop.getShopName().toLowerCase(Locale.US)
                    .contains(searchText.toLowerCase(Locale.US))
                    || shop.getAddress().toLowerCase(Locale.US)
                    .contains(searchText.toLowerCase(Locale.US))
                    || shop.getCity().toLowerCase(Locale.US)
                    .contains(searchText.toLowerCase(Locale.US))
                    || shop.getPincode().toLowerCase(Locale.US)
                    .contains(searchText.toLowerCase(Locale.US))) {
                searchItemList.add(shop);
            }
        }
        searchAdpter = new SearchAdapter(getActivity(),
                R.layout.search_list_view, searchItemList);
        searchList.setAdapter(searchAdpter);
        searchAdpter.notifyDataSetChanged();

        if (searchItemList.size() > 0) {
            searchSugestionView.setVisibility(View.VISIBLE);
        } else {
            searchSugestionView.setVisibility(View.INVISIBLE);
        }
    }


    private void oncameraChange() {
        if (mGoogleMap != null) {
            mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
                public void onCameraChange(CameraPosition pos) {
                    if (!isCalled) {
                        String map_mov = AppConstants.FAILURE_CODE;
                        if (pos.zoom != currentZoom) {
                            currentZoom = pos.zoom;
                            map_mov = AppConstants.SUCCESS_CODE;
                        } else {
                            mMarkerList.clear();
                            mGoogleMap.clear();
                            from_search = false;
                            VisibleRegion vr = mGoogleMap.getProjection()
                                    .getVisibleRegion();
                            double lat = vr.latLngBounds.getCenter().latitude;
                            double longitude = vr.latLngBounds.getCenter().longitude;

                            getScreenCoordinates(lat, longitude);
                            callMapService(lat + "", longitude + "", map_mov);
                            callGoogleApiService(lat, longitude);
                            isCalled = true;
                        }
                    }
                }
            });
        }
    }

    private void callGoogleApiService(double latitude, double longitude) {

        String url = AppConstants.LATLNG_LINK + latitude + "," + longitude
                + "&sensor=true";
        aq().ajax(url, JSONObject.class, this,
                getString(R.string.addresslocation));
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
                if (response.getResults().size() > 0) {
                    String[] addresses = response.getResults().get(0)
                            .getFormatted_address().toString().split(",");
                    if (addresses.length >= 4) {
                        add_txt = response
                                .getResults()
                                .get(0)
                                .getFormatted_address()
                                .replace(
                                        (addresses[(addresses.length - 3)] + ","),
                                        "")
                                .replace(
                                        (addresses[(addresses.length - 2)] + ","),
                                        "")
                                .replace(addresses[(addresses.length - 1)], "");
                        // String city = addresses[(addresses.length - 3)];
                        HomeScreen.mHeaderText.setText(add_txt);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeMap() {


        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

        fragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


        if (mGoogleMap == null) {
            Toast.makeText(getActivity(),
                    getString(R.string.unable_to_create_maps),
                    Toast.LENGTH_SHORT).show();
        } else {

            mGoogleMap.clear();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            mGoogleMap
                    .setOnMarkerClickListener(new OnMarkerClickListener() {

                        public boolean onMarkerClick(Marker marker) {
                            if (!isCurrent) {
                                markerClick(marker);
                                isCurrent = true;
                            }
                            return false;

                        }
                    });

            showCurrentLocation("", "");
            oncameraChange();
            iniLoadMapmarker();
        }
        //	mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @SuppressLint("NewApi")
    private void getScreenCoordinates(double lat, double longitude) {
        Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        // int maxX = mdispSize.x;
        int maxY = mdispSize.y;

        Point x_y_points = new Point(0, maxY / 2);

        LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(
                x_y_points);
        double latitudes = latLng.latitude;

        double longitudes = latLng.longitude;

        calculateTheDistance(lat, longitude, latitudes, longitudes);
    }

    private void calculateTheDistance(double point_A_lat, double point_A_long,
                                      double point_B_lat, double point_B_long) {
        Location locationA = new Location(getString(R.string.point_A));
        locationA.setLatitude(point_A_lat);
        locationA.setLongitude(point_A_long);
        Location locationB = new Location(getString(R.string.point_B));
        locationB.setLatitude(point_B_lat);
        locationB.setLongitude(point_B_long);
        map_distance = locationA.distanceTo(locationB) / 1000;

    }

    boolean isDouble(String str) {
        if (str == null)
            return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    private void getLatLong(ArrayList<MapPropertyEntity> response) {

        if (AppConstants.MAP_CONT_VAL.equalsIgnoreCase("Normal")) {
            normal_count = 0;
        } else if (AppConstants.MAP_CONT_VAL.equalsIgnoreCase("Preminum")) {
            premium_count = 0;
        } else {
            premium_count = 0;
            normal_count = 0;
        }
        mMarkerID.clear();
        mGoogleMap.clear();
        ishown = false;
        if (!ishown) {

            LatLng newLatLng1 = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(newLatLng1);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.current_location));
            mGoogleMap.addMarker(markerOptions);
            mIntLat = latitude;
            mIntLong = longitude;

            ishown = true;
        }


        for (int i = 0; i < response.size(); i++) {
            if (isDouble(response.get(i).getLatitude())
                    && isDouble(response.get(i).getLongitude())) {
                if (response.get(i).getIsPremium() != null
                        && response.get(i).getIsPremium()
                        .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    premium_count = premium_count + 1;
                } else {
                    normal_count = normal_count + 1;
                }
                drawMarker(
                        new LatLng(Double
                                .valueOf(response.get(i).getLatitude()), Double
                                .valueOf(response.get(i).getLongitude())),
                        response.get(i).getAddress(), response.get(i)
                                .getShopName());
            }
        }

        if (from_search) {
            searchZoomMapLevel();
            from_search = false;
        }

        if (AppConstants.MAP_CONT_VAL.equalsIgnoreCase("All")
                || AppConstants.MAP_CONT_VAL
                .equalsIgnoreCase(AppConstants.MAP_SCREEN)) {
            mPreminum.setText(getString(R.string.Premium) + " ("
                    + premium_count + ")");

            mAll.setText(getString(R.string.All) + " (" + response.size() + ")");
            mNormal.setText(getString(R.string.Normal) + " (" + normal_count
                    + ")");
        } else if (AppConstants.MAP_CONT_VAL.equalsIgnoreCase("Normal")) {
            mNormal.setText(getString(R.string.Normal) + " (" + normal_count
                    + ")");
        } else if (AppConstants.MAP_CONT_VAL.equalsIgnoreCase("Preminum")) {
            mPreminum.setText(getString(R.string.Premium) + " ("
                    + premium_count + ")");

        }
        AppConstants.MAP_CONT_VAL = (AppConstants.MAP_SCREEN);
    }

    private void setListAdapter(ArrayList<MapPropertyEntity> mPharmacyList) {

        // premium_count = 0;
        // normal_count = 0;

        // mPharmacyList.clear();
        if (mPharmacyList != null) {
            MapListAdapter adapter = new MapListAdapter(getActivity(),
                    mPharmacyList);
            mMapListView.setAdapter(adapter);
            // mMapListView.notifyAll();

            for (int i = 0; i < mPharmacyList.size(); i++) {
                if (mPharmacyList.get(i).getIsPremium() != null
                        && mPharmacyList.get(i).getIsPremium()
                        .equalsIgnoreCase("1")) {
                    premium_count = premium_count + 1;
                } else {
                    normal_count = normal_count + 1;
                }
            }


        }
    }

    public void setInfoAdapter(final String address, final String name) {

        mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            public View getInfoWindow(Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(
                        R.layout.info_window, null);
                TextView PharName = (TextView) v
                        .findViewById(R.id.pharmacy_name);
                TextView PharAddress = (TextView) v
                        .findViewById(R.id.pharmacy_address);
                PharName.setText(name);
                PharAddress.setText(address);
                return v;
            }

            public View getInfoContents(Marker arg0) {
                // TODO Auto-generated method stub
                return null;
            }

        });

        mGoogleMap
                .setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                    @SuppressWarnings("static-access")
                    public void onInfoWindowClick(Marker arg0) {
                        mPharmacyAddress.setText("");
                        ((HomeScreen) getActivity()).mFragment = new OrderNowFragment();
                        bundle = new Bundle();
                        bundle.putSerializable("pharmcay_details",
                                mMapEntity.get(curr_pos_marker));
                        // MapPropertyEntity st=mMapEntity.get(curr_pos_marker);
                        ((HomeScreen) getActivity()).mFragment
                                .setArguments(bundle);
                        ((HomeScreen) getActivity()).replaceFragment(
                                ((HomeScreen) getActivity()).mFragment, true);
                        HomeScreen.mHeaderRightLay
                                .setVisibility(View.INVISIBLE);
                        HomeScreen.mHeaderLeft
                                .setBackgroundResource(R.drawable.back_butto);
                    }
                });

    }

    private void setDrawingView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
    }

    public void drawMap() {

        PolygonOptions drawShapeOptions = new PolygonOptions();
        drawShapeOptions.addAll(mLatLngList);
        drawShapeOptions.strokeColor(Color.BLACK);
        drawShapeOptions.strokeWidth(5);
        drawShapeOptions.fillColor(Color.GRAY);
        // mDrawShape = mGoogleMap.addPolygon(drawShapeOptions);
        mIsMapMoveable = !mIsMapMoveable;
        mGoogleMapLayout.removeView(mDrawingViews);

    }

    boolean coordinateInRegion(List<LatLng> verts, LatLng coord) {
        int i, j;
        boolean isInside = false;

        int sides = verts.size();
        for (i = 0, j = sides - 1; i < sides; j = i++) {
            // verifying if your coordinate is inside your region
            if ((((verts.get(i).longitude <= coord.longitude) && (coord.longitude < verts
                    .get(j).longitude)) || ((verts.get(j).longitude <= coord.longitude) && (coord.longitude < verts
                    .get(i).longitude)))
                    && (coord.latitude < (verts.get(j).latitude - verts.get(i).latitude)
                    * (coord.longitude - verts.get(i).longitude)
                    / (verts.get(j).latitude - verts.get(i).longitude)
                    + verts.get(i).latitude)) {
                isInside = !isInside;
            }
        }
        return isInside;
    }

    private void drawMarker(LatLng newLatLng1, String pharmacyaddress,
                            String pharmacyname) {
        isCurrent = false;
        Marker marker1 = mGoogleMap.addMarker(new MarkerOptions().position(
                newLatLng1).icon(
                BitmapDescriptorFactory
                        .fromResource(R.drawable.pharmacy_marker)));
        mMarkerList.add(marker1);
        MarkerID markerId = new MarkerID();
        markerId.setMarkerid(marker1.getId());
        markerId.setPharmacyaddress(pharmacyaddress);
        markerId.setPharmacyname(pharmacyname);
        mMarkerID.add(markerId);

        // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng1));
        // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public boolean markerClick(Marker v) {
        isCalled = true;
        pos = 0;
        curr_pos_marker = 0;
        for (MarkerID markerId : mMarkerID) {

            if (v.getId().equalsIgnoreCase(markerId.getMarkerid())) {
                setInfoAdapter(markerId.getPharmacyaddress(),
                        markerId.getPharmacyname());
                curr_pos_marker = pos;
                v.showInfoWindow();
                break;
            } else {
                v.hideInfoWindow();
            }
            pos++;
        }

        return true;
    }

    private void searchZoomMapLevel() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < mMarkerList.size(); i++) {
            builder.include(mMarkerList.get(i).getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        // mGoogleMap.moveCamera(cu);
        mGoogleMap.animateCamera(cu);
    }

    private void showCurrentLocation(final String lat, final String longi) {
        isCurrent = true;
        ishown = false;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(false);
        if (!ishown) {
            if (lat.equalsIgnoreCase("") || longi.equalsIgnoreCase("")) {
                LatLng newLatLng1 = new LatLng(latitude,
                        longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(newLatLng1);
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.current_location));
                mGoogleMap.addMarker(markerOptions);
                mIntLat = latitude;
                mIntLong = longitude;

            }
            ishown = true;
        }

        // if (!ishown) {
        // if (lat.equalsIgnoreCase("") || longi.equalsIgnoreCase("")) {
        // LatLng newLatLng1 = new LatLng(location.lastLat,
        // location.lastLong);
        // MarkerOptions markerOptions = new MarkerOptions();
        // markerOptions.position(newLatLng1);
        // markerOptions.icon(BitmapDescriptorFactory
        // .fromResource(R.drawable.current_location));
        // mGoogleMap.addMarker(markerOptions);
        // mIntLat = location.lastLat;
        // mIntLong = location.lastLong;
        //
        // }
        // ishown = true;
        // }

        // mGoogleMap
        // .setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
        //
        // public void onMyLocationChange(Location location) {
        // if (!ishown) {
        // if (lat.equalsIgnoreCase("")
        // || longi.equalsIgnoreCase("")) {
        //
        // cameraposition = new CameraPosition.Builder()
        // .target(new LatLng(location
        // .getLatitude(), location
        // .getLongitude()))
        // .zoom((float) 13).build();
        // mGoogleMap.addMarker(new MarkerOptions()
        // .position(
        // new LatLng(location
        // .getLatitude(),
        // location.getLongitude()))
        // .icon(BitmapDescriptorFactory
        // .fromResource(R.drawable.current_location)));
        // mIntLat = location.getLatitude();
        // mIntLong = location.getLongitude();
        //
        // }
        // ishown = true;
        // }
        // // foundLatitude1 = location.getLatitude();
        // // foundLongitude1 = location.getLongitude();
        //
        // }
        // });
        tracker = new GPSTracker(getActivity());
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            double latitude = tracker.getLatitude();
            double longitude = tracker.getLongitude();
            mIntLat = latitude;
            mIntLong = longitude;
            LatLng latLng = new LatLng(latitude, longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            cameraposition = new CameraPosition.Builder().target(latLng)
                    .zoom((float) 13).build();
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraposition));
            callGoogleApiService(tracker.getLatitude(), tracker.getLongitude());
        }
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

            mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            mPath.close();
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
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            int x_co = Math.round(x);
            int y_co = Math.round(y);

            // mProjection = mGoogleMap.getProjection();
            Point x_y_points = new Point(x_co, y_co);

            LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(
                    x_y_points);
            double latitude = latLng.latitude;

            double longitude = latLng.longitude;
            if (mIsMapMoveable) {
                // mLatLngList.clear();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

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


    private void getLocation() {
        tracker = new GPSTracker(getActivity());
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
        }
    }

    private void callMapService(String lat, String longitude, String map_mov) {

        APIRequestHandler.getInstance().getSearchPharmacies(UserID, lat,
                longitude, mPharmacyAddress.getText().toString(), map_distance,
                this);
        // mPharmacyAddress.setText("");

    }

    private void callSearchMapService(String lat, String longitude,
                                      String map_mov) {

        APIRequestHandler.getInstance().getSearchPharmacies(UserID, lat,
                longitude, mPharmacyAddress.getText().toString(), map_distance,
                this);
//        APIRequestHandler.getInstance().getSearchPharmacies(UserID, lat,
//                longitude, mPharmacyAddress.getText().toString(), map_distance,
//                true, this);

        // mPharmacyAddress.setText("");

    }

    public static void callApi(String mShopId, String mUserID, String rating) {
        // TODO Auto-generated method stub
        APIRequestHandler.getInstance().shopRating(mShopId, mUserID, rating,
                mcontext);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {

        //TODO: ekfdkfdfdk fd
        DialogManager.hideProgress(getActivity());
        if (responseObj instanceof AdsEntity) {

            AdsEntity mAdsResponse = (AdsEntity) responseObj;

            if (mAdsResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {

                if (mAdsResponse.getResult() != null
                        && mAdsResponse.getResult().size() != 0) {

                    mHandler = new Handler();
                    ads_local_entity = mAdsResponse;
                    mHandler.postDelayed(mRunnable, 10);
                } else {

                    mAdLayout.setVisibility(View.GONE);
                }
            } else {
                mAdLayout.setVisibility(View.GONE);
                DialogManager.showCustomAlertDialog(getActivity(),
                        MapScreenFragment.this, mAdsResponse.getMsg());
            }
        } else if (responseObj instanceof RateResponseEntity) {

            RateResponseEntity mRatingResponse = (RateResponseEntity) responseObj;

            if (mRatingResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)
                    || mRatingResponse.getStatus().equalsIgnoreCase("success")) {
                DialogManager.showCustomAlertDialog(getActivity(),
                        MapScreenFragment.this,
                        getString(R.string.rating_added));
                mMapEntity.get(MapListAdapter.mSelectPos).setAvgRating(
                        String.valueOf(mRatingResponse.getResult()));
                setListAdapter(mMapEntity);

            } else {
                DialogManager.showCustomAlertDialog(getActivity(),
                        MapScreenFragment.this, mRatingResponse.getMsg());
            }

        } else if (responseObj instanceof MapResponse) {

            MapResponse mMapResponse = (MapResponse) responseObj;

            if (mMapResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                // mMapEntity.clear();
                mMarkerList.clear();

                mMapEntity = mMapResponse.getResult();
                if (!mMapResponse.getMsg().equalsIgnoreCase(
                        AppConstants.SEARCH_SUCCESS_CODE)
                        && !isClicked) {
                    isClicked = true;
                    if (mLatitude != null && !mLatitude.equalsIgnoreCase("")) {
                        callGoogleApiService(Double.valueOf(mLatitude),
                                Double.valueOf(mLongitude));
                    } else {
                        // callGoogleApiService(Double.valueOf("0"),
                        // Double.valueOf("0"));
                    }
                } else if (mMapResponse.getMsg().equalsIgnoreCase(
                        AppConstants.SEARCH_SUCCESS_CODE)
                        && !isClicked) {

                    DialogManager.showToast(getActivity(),
                            getString(R.string.could_not_find_add));
                    isClicked = false;
                }
                try {
                    mGoogleMapLayout.setVisibility(View.VISIBLE);
                    mLocation.setVisibility(View.INVISIBLE);
                    HomeScreen.mHeaderRightLay.setVisibility(View.VISIBLE);
                    HomeScreen.mHeaderRight
                            .setBackgroundResource(R.drawable.map_view_normal);
                    mMarkerList.clear();
                    getLatLong(mMapEntity);
                    setListAdapter(mMapEntity);
                } catch (Exception e) {

                    e.printStackTrace();
                }

            } else {
                DialogManager.showCustomAlertDialog(getActivity(),
                        MapScreenFragment.this, mMapResponse.getMsg());
            }

        }
        super.onRequestSuccess(responseObj);
    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        super.onRequestFailure(errorCode);

        mPreminum.setText(getString(R.string.Premium) + " (0) ");

        mAll.setText(getString(R.string.All) + " (0)");
        mNormal.setText(getString(R.string.Normal) + " (0)");
    }

    public void getLatLongFromPlace(String place) {
        try {
            Geocoder selected_place_geocoder = new Geocoder(getActivity());
            List<Address> address;

            address = selected_place_geocoder.getFromLocationName(place, 1);

            if (address == null) {
                fetchLatLongFromService fetch_latlng_from_service_abc = new fetchLatLongFromService(
                        place.replaceAll("\\s+", ""));
                fetch_latlng_from_service_abc.execute();

            } else {
                Address location = address.get(0);
                double lat = location.getLatitude();
                double lng = location.getLongitude();

            }

        } catch (Exception e) {
            e.printStackTrace();
            fetchLatLongFromService fetch_latlng_from_service_abc = new fetchLatLongFromService(
                    place.replaceAll("\\s+", ""));
            fetch_latlng_from_service_abc.execute();

        }

    }

    // Sometimes happens that device gives location = null

    public class fetchLatLongFromService extends
            AsyncTask<Void, Void, StringBuilder> {
        String place;

        public fetchLatLongFromService(String place) {
            super();
            this.place = place;

        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
            this.cancel(true);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection conn = null;
                StringBuilder jsonResults = new StringBuilder();
                String googleMapUrl = AppConstants.GOOGLE_MAP_LINK + this.place
                        + "&sensor=false";

                URL url = new URL(googleMapUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                @SuppressWarnings("unused")
                String a = "";
                return jsonResults;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(StringBuilder result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                JSONObject jsonObj = new JSONObject(result.toString());
                JSONArray resultJsonArray = jsonObj
                        .getJSONArray(getString(R.string.results));

                // Extract the Place descriptions from the results
                // resultList = new ArrayList<String>(resultJsonArray.length());

                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject(getString(R.string.geometry));

                JSONObject location_jsonObj = geometry_jsonObj
                        .getJSONObject(getString(R.string.location));

                String lat_helper = location_jsonObj
                        .getString(getString(R.string.lat));
                double lat = Double.valueOf(lat_helper);

                String lng_helper = location_jsonObj
                        .getString(getString(R.string.lng));
                double lng = Double.valueOf(lng_helper);

                @SuppressWarnings("unused")
                LatLng point = new LatLng(lat, lng);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_right_lay:
                if (map_fragment_main.getVisibility() == View.VISIBLE) {

                    AppConstants.from_map_list = AppConstants.TRUE;
                    map_fragment_main.setVisibility(View.INVISIBLE);
                    map_fragment_main.startAnimation(mOutBottom);
                    HomeScreen.mHeaderRight
                            .setBackgroundResource(R.drawable.map_view_normal);


                } else {
                    AppConstants.from_map_list = AppConstants.FALSE;
                    map_fragment_main.setVisibility(View.VISIBLE);
                    map_fragment_main.startAnimation(mInBottom);
                    HomeScreen.mHeaderRightLay.setVisibility(View.VISIBLE);

                    HomeScreen.mHeaderRight
                            .setBackgroundResource(R.drawable.maplist_normal);


                }

                break;
            case R.id.current_location_button:
                showCurrentLocation("", "");
                callMapService(mIntLat + "", mIntLong + "",
                        AppConstants.FAILURE_CODE);
                AppConstants.FROMINTLOADMAP = AppConstants.SUCCESS_CODE;
                break;

            case R.id.Normal:
                mMapproplist.clear();
                mGoogleMap.clear();
                for (int i = 0; i < mMapEntity.size(); i++) {
                    if (mMapEntity.get(i).getIsPremium().equalsIgnoreCase("0")) {
                        setValuesinAdapter(i);
                    }
                }
                AppConstants.MAP_CONT_VAL = "Normal";
                getLatLong(mMapproplist);
                setListAdapter(mMapproplist);
                break;
            case R.id.All:
                mMapproplist.clear();
                mGoogleMap.clear();
                for (int i = 0; i < mMapEntity.size(); i++) {
                    setValuesinAdapter(i);
                }
                AppConstants.MAP_CONT_VAL = "All";
                getLatLong(mMapEntity);
                setListAdapter(mMapproplist);
                break;
            case R.id.Preminum:
                mMapproplist.clear();
                mGoogleMap.clear();
                for (int i = 0; i < mMapEntity.size(); i++) {
                    if (mMapEntity.get(i).getIsPremium().equalsIgnoreCase("1")) {
                        setValuesinAdapter(i);
                    }
                }
                AppConstants.MAP_CONT_VAL = "Preminum";
                getLatLong(mMapproplist);
                setListAdapter(mMapproplist);
                break;

            case R.id.oneTouch:
                AppConstants.from_map_list = AppConstants.TRUE;
                HomeScreen.mFragment = new OneTouchFragment();
                HomeScreen.mHeaderRightLay.setVisibility(View.VISIBLE);
                HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                HomeScreen.mHeaderRight.setBackgroundResource(R.drawable.one_touch_white);
                ((HomeScreen) getActivity()).replaceFragment(HomeScreen.mFragment, true);


                break;

        }
    }

    private void setValuesinAdapter(int i) {
        mMappropent = new MapPropertyEntity();
        mMappropent.setAddress(mMapEntity.get(i).getAddress());
        mMappropent.setArea(mMapEntity.get(i).getArea());
        mMappropent.setAvgRating(mMapEntity.get(i).getAvgRating());
        mMappropent.setBreakTime(mMapEntity.get(i).getBreakTime());
        mMappropent.setCapturedDevice(mMapEntity.get(i).getCapturedDevice());
        mMappropent.setCity(mMapEntity.get(i).getCity());

        mMappropent.setCloseTime(mMapEntity.get(i).getCloseTime());
        mMappropent.setDeliveryCharges(mMapEntity.get(i).getDeliveryCharges());
        mMappropent.setDeliveryTime(mMapEntity.get(i).getDeliveryTime());
        mMappropent.setDistance(mMapEntity.get(i).getDistance());
        mMappropent.setEmail(mMapEntity.get(i).getEmail());
        mMappropent.setHomeDelivery(mMapEntity.get(i).getHomeDelivery());
        mMappropent.setInvestigatorID(mMapEntity.get(i).getInvestigatorID());
        mMappropent.setIsActive(mMapEntity.get(i).getIsActive());
        mMappropent.setIsFav(mMapEntity.get(i).getIsFav());
        mMappropent.setIsPremium(mMapEntity.get(i).getIsPremium());
        mMappropent.setLandmark(mMapEntity.get(i).getLandmark());
        mMappropent.setLatitude(mMapEntity.get(i).getLatitude());

        mMappropent.setLongitude(mMapEntity.get(i).getLongitude());
        mMappropent.setMinimumOrderValue(mMapEntity.get(i)
                .getMinimumOrderValue());
        mMappropent.setMobile(mMapEntity.get(i).getMobile());
        mMappropent.setOpenTime(mMapEntity.get(i).getOpenTime());
        mMappropent.setOwnerName(mMapEntity.get(i).getOwnerName());
        mMappropent.setPharmacyID(mMapEntity.get(i).getPharmacyID());
        mMappropent.setPharmacyOffers(mMapEntity.get(i).getPharmacyOffers());
        mMappropent.setPhone(mMapEntity.get(i).getPhone());
        mMappropent.setPincode(mMapEntity.get(i).getPincode());
        mMappropent.setProfilePic(mMapEntity.get(i).getProfilePic());
        mMappropent.setProfileVideo(mMapEntity.get(i).getProfileVideo());
        mMappropent.setReferenceNumber(mMapEntity.get(i).getReferenceNumber());
        mMappropent.setRegisteredDateTime(mMapEntity.get(i)
                .getRegisteredDateTime());

        mMappropent.setShopIcon(mMapEntity.get(i).getShopIcon());
        mMappropent.setShopName(mMapEntity.get(i).getShopName());
        mMappropent.setShopPassword(mMapEntity.get(i).getShopPassword());
        mMappropent.setShopUserName(mMapEntity.get(i).getShopUserName());
        mMappropent.setTotalReviews(mMapEntity.get(i).getTotalReviews());
        mMappropent.setWebsite(mMapEntity.get(i).getWebsite());
        mMapproplist.add(mMappropent);
    }

    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        // TODO Auto-generated method stub

    }

    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub

    }

    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String[] mArray = placesList.get(position).get("description")
                .toString().split(",");
        if (mArray.length > 0) {
            mPharmacyAddress.setText(mArray[0]);
            hideSoftKeyboard(getActivity());

            getLatLong(aq(), null, mPharmacyAddress.getText().toString()
                    .trim());

        }

    }


    public static class GsonTransformer implements Transformer {

        public <T> T transform(String url, Class<T> type, String encoding,
                               byte[] data, AjaxStatus status) {
            Gson g = new Gson();
            return g.fromJson(new String(data), type);
        }
    }
}

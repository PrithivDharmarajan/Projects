package com.smaat.ipharma.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.MapResponse;
import com.smaat.ipharma.entity.OneTouchResponse;
import com.smaat.ipharma.entity.PlaceResponse;
import com.smaat.ipharma.entity.Places;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.smaat.ipharma.ui.HomeScreen.address_title;


/**
 * Created by admin on 1/16/2017.
 */

public class PharmacyListFragment extends BaseFragment implements AdapterView.OnItemClickListener,GoogleMap.OnMapClickListener, OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    @BindView(R.id.main_layout)
    LinearLayout m_Mainlayout;

    @BindView(R.id.pharmacy_text)
    AutoCompleteTextView m_PharmacyText;


    @BindView(R.id.pharmacy_list)
    ListView m_pharmacylist;

    @BindView(R.id.map_layout)
    RelativeLayout m_Map_layout;

    @BindView(R.id.edittext_layout)
    LinearLayout m_Edt_layout;


    @BindView(R.id.current_location)
    ImageView mCurrentlocation;

    @BindView(R.id.refresh_btn)
    ImageView mRefreshbutton;

    @BindView(R.id.clear_btn)
    ImageView mClearBtn;

    private boolean from_search = false;
    String UserID = "";
    public static double mDeflat = 0.0;
    public static double mDeflong = 0.0;
    double latitude = 0.0;
    double longitude = 0.0;
    GPSTracker gpstrack;
    private Marker m_currentMarker = null;
    private float map_distance = (float) 1000.0;

    private boolean isClicked = true;

    private ArrayList<String> places;
    private List<HashMap<String, String>> placesList;

    private Animation mInBottom, mOutBottom;

    private GoogleMap mGoogleMap;

    boolean isCurrent = true;
    private boolean ishown;

    private MapListAdapter adapter;

    ArrayList<MapPropertyEntity> mPharmacyList2;

    public static ArrayList<MapPropertyEntity> mLoadAlldata = new ArrayList<MapPropertyEntity>();

    public static  HashMap<Marker, MapPropertyEntity> m_locMarkersHashmap = new HashMap<Marker, MapPropertyEntity>();

    double mIntLat, mIntLong;

    @BindView(R.id.internetconnection_layout)
    RelativeLayout internetconnection_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.ui_pharmacy_list, container,
                false);
        HomeScreen.homeFragment = this;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);
        initializeMap();
        UserID = GlobalMethods.getUserID(getActivity());
        gpstrack = new GPSTracker(getActivity());
        latitude = gpstrack.getLatitude();
        longitude = gpstrack.getLongitude();
        if(mDeflat==0.0&&mDeflong==0.0)
        {
            mDeflat = latitude;
            mDeflong = longitude;
        }

        /*m_PharmacyText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                m_PharmacyText.getText().clear();
                m_Edt_layout.setGravity(Gravity.LEFT);
                m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                *//*showCurrentLocation("", "");
                callMapService(mIntLat + "", mIntLong + "",
                        AppConstants.FAILURE_CODE);*//*
            }
        });*/
        m_PharmacyText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                m_Edt_layout.setGravity(Gravity.LEFT);
                m_PharmacyText.setText("");
                plotMarkers(mLoadAlldata,mGoogleMap);
                InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(m_PharmacyText, InputMethodManager.SHOW_IMPLICIT);
                m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                return false;
            }
        });

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_PharmacyText.setText("");
                setListAdapter(mLoadAlldata);
                mGoogleMap.clear();
                LatLng latlng = new LatLng(gpstrack.getLatitude(), gpstrack.getLongitude());
                CameraUpdate center= CameraUpdateFactory.newLatLng(latlng);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
                mGoogleMap.moveCamera(center);
                mGoogleMap.animateCamera(zoom);
                plotMarkers(mLoadAlldata,mGoogleMap);

                if(m_Map_layout.getVisibility()==View.VISIBLE){
                    m_pharmacylist.setVisibility(View.GONE);
                    m_Map_layout.setVisibility(View.VISIBLE);
                }else if(m_pharmacylist.getVisibility()==View.VISIBLE){
                    m_pharmacylist.setVisibility(View.VISIBLE);
                    m_Map_layout.setVisibility(View.GONE);
                }

                m_PharmacyText.setText("");
                m_Edt_layout.setGravity(Gravity.CENTER_HORIZONTAL);
                m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
        });
        mCurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GPSTracker gps = new GPSTracker(getActivity());
                if (gps.canGetLocation()) {
                    LatLng curloc = new LatLng(latitude, longitude);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curloc, 16));
                    plotMarkers(mLoadAlldata,mGoogleMap);
                    setListAdapter(mLoadAlldata);
                    m_PharmacyText.setText("");
                    m_Edt_layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                } else {
                    gps.showSettingsAlert();
                }

            }
        });


        mRefreshbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gpstrack.canGetLocation())
                {
                    map_distance = (float) 1000.0;
                    APIRequestHandler.getInstance().SearchPharmacies(latitude,
                            longitude, m_PharmacyText.getText().toString(), map_distance,internetconnection_layout,m_pharmacylist,
                            PharmacyListFragment.this);
                }else{
                    gpstrack.showSettingsAlert();
                }
            }
        });
        m_PharmacyText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        if(!m_PharmacyText.getText().toString().trim().isEmpty())
                        {
                            from_search = true;
                            isClicked = false;
                            m_PharmacyText.dismissDropDown();
                            latitude = gpstrack.getLatitude();
                            longitude = gpstrack.getLongitude();
                            LoadPharmacyList(latitude,longitude);
                            hideSoftKeyboard(getActivity());
                        }else{
                            DialogManager.showMsgPopup(getActivity(),"","Please enter a text to search");
                        }

                        return true;

                    }
                });

        m_PharmacyText.setThreshold(1);
        m_PharmacyText.setOnItemClickListener(PharmacyListFragment.this);
        m_PharmacyText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if(s.length()>0)
                {
                    m_Edt_layout.setGravity(Gravity.LEFT);
                    mClearBtn.setVisibility(View.VISIBLE);
                    m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                    if(s.length()>=3)
                    {
                        callPlaces(s);
                    }
                }else{
                    m_Edt_layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    mClearBtn.setVisibility(View.GONE);
                    m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
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


        return rootview;
    }



    private void callPlaces(CharSequence s) {
        getResultPlaces(getURL(AppConstants.API_KEY, s.toString()), aq(), null,
                null);
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


    public void changeView(ImageView img_icon)
    {
        mInBottom = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_from_bottom);
        mOutBottom = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_to_bottom);


        if(m_Map_layout.getVisibility()==View.VISIBLE){
            m_pharmacylist.setVisibility(View.VISIBLE);
            m_pharmacylist.setAnimation(mInBottom);
            m_Map_layout.setVisibility(View.GONE);
            img_icon.setImageResource(R.drawable.map_view);
        }else if(m_pharmacylist.getVisibility()==View.VISIBLE){
            m_pharmacylist.setVisibility(View.GONE);
            m_Map_layout.setVisibility(View.VISIBLE);
            m_Map_layout.setAnimation(mInBottom);
            img_icon.setImageResource(R.drawable.list_view);
        }
    }
    public static String getURL(String apiKey, String place) {
        String key = "key=" + apiKey;
        String input = "";
        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String types = "types=establishment|geocode";
        String sensor = "radius=500";
        String lang = "language=en";
        String parameters = input + "&" + types + "&" + sensor + "&"+ lang + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                + output + "?" + parameters;
        Log.e("urlurlurlurl","urlurlurlurlurlurlurl"+url);
        return url;
    }


    public void getResultPlaces(String url, AQuery aq, Map<String, Object> params, final Class<?> reDirectActivity) {
        GsonTransformer t = new GsonTransformer();
        aq.transformer(t).ajax(url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {
                    public void callback(String url, JSONObject profile,
                                         AjaxStatus status) {
                        try {
                            PlaceResponse response = new Gson().fromJson(profile.toString(), PlaceResponse.class);
                            if (response != null) {
                                String[] from = new String[]{"description"};
                                int[] to = new int[]{android.R.id.text1};
                                placesList = new ArrayList<HashMap<String, String>>();
                                places = new ArrayList<String>();
                                for (Places placeDetail : response.predictions) {
                                    HashMap<String, String> place = new HashMap<String, String>();

                                    place.put("description",
                                            placeDetail.getDescription());
                                    place.put("_id", placeDetail.getId());
                                    place.put("reference",
                                            placeDetail.getReference());
                                    placesList.add(place);

                                    places.add(placeDetail.getDescription());
                                    Log.e("RESPONSE","RESPONSE"+placesList);
                                }
                                Log.e("PlaceslistPlaceslist","Placeslist"+placesList);
                                SimpleAdapter adapter = new SimpleAdapter(
                                        getActivity(), placesList,
                                        android.R.layout.simple_list_item_1,
                                        from, to);
                                m_PharmacyText.setAdapter(adapter);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                });
    }


    private void LoadPharmacyList(double latitude,double longitude) {
        if(gpstrack.canGetLocation())
        {
            callGoogleApiService(latitude, longitude);
            map_distance = (float) 50.0;
            APIRequestHandler.getInstance().SearchPharmacies(latitude,
                    longitude, m_PharmacyText.getText().toString(), map_distance,internetconnection_layout,m_pharmacylist,
                    PharmacyListFragment.this);
        }else{
            gpstrack.showSettingsAlert();
        }

    }



    private void callGoogleApiService(double latitude, double longitude) {

        String url = AppConstants.LATLNG_LINK + latitude + "," + longitude
                + "&sensor=true";
        aq().ajax(url, JSONObject.class, this,getString(R.string.addresslocation));
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
                    Log.e("getResults","getResults"+ response.getResults().get(0).getFormatted_address());
                    String[] addresses = response.getResults().get(0)
                            .getFormatted_address().toString().split(",");
                    if (addresses.length >= 4) {
                        String add_txt = response
                                .getResults()
                                .get(0)
                                .getFormatted_address()
                                .replace((addresses[(addresses.length - 3)] + ","),"").replace((addresses[(addresses.length - 2)] + ","),"").replace(addresses[(addresses.length - 1)], "");
                        add_txt = add_txt.substring(0, add_txt.length()-1);
                        String area = addresses[(addresses.length - 4)].trim();
                        String city = addresses[(addresses.length - 3)].trim();
                        String pincode = addresses[(addresses.length - 2)].replaceAll("\\D", "");
                        address_title.setText(response.getResults().get(0).getFormatted_address());
                        /*m_textAddress.setText(add_txt.trim());
                        m_textCity.setText(city.trim());
                        m_textPincode.setText(pincode.trim());
                        m_StrArea=area;*/
                        //m_textArea.setText(area);
                    }
                } else {
                    DialogManager.showMsgPopup(getActivity(),getString(R.string.app_name),
                            getString(R.string.unable_to_find_location));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestSuccess(Object responseObj) {

        if(responseObj instanceof MapResponse)
        {
            if(internetconnection_layout.getVisibility()==View.VISIBLE)
            {
                internetconnection_layout.setVisibility(View.GONE);
            }

            MapResponse mMapResponse = (MapResponse) responseObj;
            if(mMapResponse.getResult().size()>0)
            {
                mPharmacyList2 = mMapResponse.getResult();
                if(mLoadAlldata.size()==0)
                {
                    mLoadAlldata = mMapResponse.getResult();
                }
                if(m_Map_layout.getVisibility()!=View.VISIBLE)
                {
                    m_pharmacylist.setVisibility(View.VISIBLE);
                }
                setListAdapter(mMapResponse.getResult());
                LatLng latlng = new LatLng(gpstrack.getLatitude(), gpstrack.getLongitude());
                CameraUpdate center= CameraUpdateFactory.newLatLng(latlng);
                mGoogleMap.moveCamera(center);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
                 mGoogleMap.animateCamera(zoom);
                //mGoogleMap.clear();
                /*LatLng latlng = new LatLng(gpstrack.getLatitude(), gpstrack.getLongitude());
                CameraUpdate center= CameraUpdateFactory.newLatLng(latlng);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
                MarkerOptions markerOption = new MarkerOptions().position(latlng);*/
                //markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.cur_loc_icon));
                /*mGoogleMap.moveCamera(center);
                mGoogleMap.animateCamera(zoom);
                mGoogleMap.addMarker(markerOption);*/
                plotMarkers(mMapResponse.getResult(),mGoogleMap);

            }else{
                setListAdapter(mMapResponse.getResult());
                plotMarkers(mMapResponse.getResult(),mGoogleMap);
                showMsgPopup(getActivity(),getString(R.string.app_name),getString(R.string.no_shops));
            }


        }else if(responseObj instanceof WriteReviewEntity)
        {
            WriteReviewEntity mWriteReviewResponse = (WriteReviewEntity) responseObj;
            if (mWriteReviewResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {

            } else {

            }
        }else if(responseObj instanceof RateResponseEntity)
        {
            RateResponseEntity rateresponse = (RateResponseEntity) responseObj;
            DialogManager.showMsgPopup(getActivity(),"",rateresponse.getMsg());
        }else if(responseObj instanceof OneTouchResponse)
        {
            OneTouchResponse rateresponse = (OneTouchResponse) responseObj;
            if (rateresponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                GlobalMethods.UpdateShopDetails(getActivity(),rateresponse.getResult().get(0));
                DialogManager.showMsgPopup(getActivity(),"","One touch updated");
                AppConstants.enableOneTouch = false;
            }
        }

    }

    private void setListAdapter(ArrayList<MapPropertyEntity> mPharmacyList) {
        AppConstants.IS_FAVOURITE = false;
        if (mPharmacyList != null) {
            adapter = new MapListAdapter(getActivity(),AppConstants.IS_FAVOURITE,
                    mPharmacyList,this);
            m_pharmacylist.setAdapter(adapter);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    public void showMsgPopup(final Context mContext,String title,String msg) {
        mDialog = DialogManager.getDialog(mContext, R.layout.popup_msg_layout);
        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        m_btnNo.setVisibility(View.GONE);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                /*setListAdapter(mLoadAlldata);
                mGoogleMap.clear();
                LatLng latlng = new LatLng(gpstrack.getLatitude(), gpstrack.getLongitude());
                CameraUpdate center= CameraUpdateFactory.newLatLng(latlng);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
                mGoogleMap.moveCamera(center);
                mGoogleMap.animateCamera(zoom);
                plotMarkers(mLoadAlldata,mGoogleMap);
                m_pharmacylist.setVisibility(View.VISIBLE);
                m_PharmacyText.setText("");
                m_Edt_layout.setGravity(Gravity.CENTER_HORIZONTAL);
                m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));*/

            }
        });
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    setListAdapter(mLoadAlldata);
                    mGoogleMap.clear();
                    LatLng latlng = new LatLng(gpstrack.getLatitude(), gpstrack.getLongitude());
                    CameraUpdate center= CameraUpdateFactory.newLatLng(latlng);
                    CameraUpdate zoom=CameraUpdateFactory.zoomTo(16);
                    mGoogleMap.moveCamera(center);
                    mGoogleMap.animateCamera(zoom);
                    plotMarkers(mLoadAlldata,mGoogleMap);
                    m_pharmacylist.setVisibility(View.VISIBLE);
                    return true;
                }

                return false;
            }
        });
        mDialog.show();
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
            mGoogleMap.setMyLocationEnabled(true);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }



        }

        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MapPropertyEntity ppty = m_locMarkersHashmap.get(marker);
                AppConstants.PharmacyDetails = ppty;
                ((HomeScreen) getActivity()).pushFragment(new ShopdetailFragment());
            }
        });


        LoadPharmacyList(latitude,longitude);

    }

    @Override
    public void onResume() {
        super.onResume();

       /* if (!GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT).isEmpty()) {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.home), getString(R.string.one_touch_updated)+" "+
                    GlobalMethods.getStringValue(getActivity(), AppConstants.ShopName_OT));
        } else {
            ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.home),getString(R.string.one_touch_order));
        }*/
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.home),getString(R.string.one_touch_order));

        if(!m_PharmacyText.getText().toString().equalsIgnoreCase(""))
        {

            m_Edt_layout.setGravity(Gravity.LEFT);
            mClearBtn.setVisibility(View.VISIBLE);
            m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        }else{
            mClearBtn.setVisibility(View.GONE);
            m_Edt_layout.setGravity(Gravity.CENTER_HORIZONTAL);
            m_PharmacyText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }

        if(m_Map_layout.getVisibility()==View.VISIBLE){
            HomeScreen.right_side_menu_icon.setImageResource(R.drawable.list_view);
        }else if(m_pharmacylist.getVisibility()==View.VISIBLE){
            HomeScreen.right_side_menu_icon.setImageResource(R.drawable.map_view);
        }

        GPSTracker gpstrack = new GPSTracker(getActivity());
        LatLng preflatlng = new LatLng(mDeflat,mDeflong);
        LatLng curlatlng = new LatLng(gpstrack.getLatitude(),gpstrack.getLongitude());

        /*Log.e("getDistancegetDistance","getDistancegetDistance"+gpstrack.getLatitude()+""+gpstrack.getLongitude());
        Log.e("getDistancegetDistance","getDistancegetDistance"+mDeflat+""+mDeflong);
        Log.e("getDistancegetDistance","getDistancegetDistance"+getDistance(preflatlng,curlatlng));
        DialogManager.showToast(getActivity(),String.valueOf(getDistance(preflatlng,curlatlng)));*/
        if(Float.parseFloat(getDistance(preflatlng,curlatlng)!=""?getDistance(preflatlng,curlatlng):"0.0")>100)
        {
            LoadPharmacyList(gpstrack.getLatitude(),gpstrack.getLongitude());
        }else{
            if(mPharmacyList2!=null)
            {
                adapter = new MapListAdapter(getActivity(),AppConstants.IS_FAVOURITE,
                        mPharmacyList2,this);
                adapter.notifyDataSetChanged();
            }
        }



    }


    @Override
    public boolean onMarkerClick(Marker marker) {
    MapPropertyEntity clickmarker = m_locMarkersHashmap.get(marker);
        if(clickmarker!=null)
        {
            marker.showInfoWindow();
        }

        return true;
    }

    /*@Override
    public void onInfoWindowClick(Marker marker) {
        MapPropertyEntity clickmarker = m_locMarkersHashmap.get(marker);
        Log.e("clickmarker","clickmarkerclickmarker"+clickmarker);
        AppConstants.PharmacyDetails =clickmarker;
        ((HomeScreen) getActivity()).pushFragment(new ShopdetailFragment());
    }*/


    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {

        Context context;
        public MarkerInfoWindowAdapter(Context ctx)
        {

            context = ctx;

        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            View v  =  getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

            MapPropertyEntity myMarker = m_locMarkersHashmap.get(marker);
            if(myMarker!=null)
            {
                String url = "";
                if (myMarker.getShopIcon() != null && !myMarker.getShopIcon().trim().isEmpty()) {
                    url = AppConstants.ADMIN_BASE_URL + myMarker.getShopIcon();
                } else {
                    url = AppConstants.ADMIN_BASE_URL + myMarker.getProfilePic();
                }

                //ImageView shop_logo = (ImageView)v.findViewById(R.id.shopLogo);
                TextView pharmacy_name = (TextView)v.findViewById(R.id.pharmacy_name);
                TextView pharmacy_address = (TextView)v.findViewById(R.id.pharmacy_address);
                /*Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.placeholder)
                        .into(shop_logo);*/
                pharmacy_name.setText(myMarker.getShopName());
                pharmacy_address.setText(myMarker.getAddress());

            }
            return v;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            return null;
        }
    }


    private void plotMarkers(final ArrayList<MapPropertyEntity> markers, final GoogleMap mMap) {


        mGoogleMap.clear();
        if (markers.size() > 0) {
            for (int i=0;i<markers.size();i++)
            {
                try{
                    MarkerOptions markerOption = new MarkerOptions().position(new LatLng(Double.parseDouble(markers.get(i).getLatitude()!=""?markers.get(i).getLatitude():"0.0"),
                            Double.parseDouble(markers.get(i).getLongitude()!=""?markers.get(i).getLongitude():"0.0")));
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer));
                    m_currentMarker = mMap.addMarker(markerOption);
                    mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getActivity()));
                    LatLng curloc = new LatLng(Double.parseDouble(markers.get(0).getLatitude()!=""?markers.get(0).getLatitude():"0.0"),
                            Double.parseDouble(markers.get(0).getLongitude()!=""?markers.get(0).getLongitude():"0.0"));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curloc, 16));
                    m_locMarkersHashmap.put(m_currentMarker, markers.get(i));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }

    }



    public static class GsonTransformer implements Transformer {

        public <T> T transform(String url, Class<T> type, String encoding,
                               byte[] data, AjaxStatus status) {
            Gson g = new Gson();
            return g.fromJson(new String(data), type);
        }
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

        if (!ishown) {
            if (lat.equalsIgnoreCase("") || longi.equalsIgnoreCase("")) {
                LatLng newLatLng1 = new LatLng(latitude,
                        longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(newLatLng1);
                markerOptions.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.map_pointer));
                mGoogleMap.addMarker(markerOptions);
                mIntLat = latitude;
                mIntLong = longitude;

            }
            ishown = true;
        }
        GPSTracker tracker = new GPSTracker(getActivity());
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            double latitude = tracker.getLatitude();
            double longitude = tracker.getLongitude();
            mIntLat = latitude;
            mIntLong = longitude;
            LatLng latLng = new LatLng(latitude, longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            CameraPosition cameraposition = new CameraPosition.Builder().target(latLng)
                    .zoom((float) 13).build();
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraposition));
            //callGoogleApiService(tracker.getLatitude(), tracker.getLongitude());
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] mArray = placesList.get(position).get("description").toString().split(",");
        if (mArray.length > 0) {
            m_PharmacyText.setText(mArray[0]);
            hideSoftKeyboard(getActivity());
            getLatLong(aq(), null, m_PharmacyText.getText().toString()
                    .trim());

        }
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

                            JSONArray jsonArray = GlobalMethods.getJsonArrayFromJsonObject(profile,"results");

                            double lng = (jsonArray.getJSONObject(0).getJSONObject("geometry")
                                    .getJSONObject("location").getDouble("lng"));

                            double lat = (jsonArray.getJSONObject(0).getJSONObject("geometry")
                                    .getJSONObject("location").getDouble("lat"));

                            Log.e("latlat","latlat"+lat);
                            Log.e("lnglng","lnglnglng"+lng);
                            if (lat != 0 && lng != 0) {


                                AppConstants.Search_text = m_PharmacyText.getText()
                                        .toString();
                                from_search = true;
                                isClicked = false;


                                LoadPharmacyList(lat,lng);

                            }else{
                                DialogManager.showMsgPopup(getActivity(),"","Invalid Search Location");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogManager.showMsgPopup(getActivity(),"","Invalid Search Location");
                        }

                    }
                });
    }

    public String getDistance(LatLng previous,LatLng current)
    {
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(previous.latitude);
        startPoint.setLongitude(previous.longitude);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(current.latitude);
        endPoint.setLongitude(current.longitude);

        double distance=startPoint.distanceTo(endPoint);
        return String.valueOf(String.format(Locale.US,"%.1f", startPoint.distanceTo(endPoint)));
    }
}

package com.fautus.fautusapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ConChatDetailsEntity;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.ParsePhotoPolEntity;
import com.fautus.fautusapp.entity.ParsePhotographerEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.ImageUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoEdtCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.NumberUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ConfigCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.utils.ParseAPIConstants.currentLocation;
import static com.fautus.fautusapp.utils.ParseAPIConstants.location;
import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * This class implements UI and functions for Consumer home screen
 *
 * @author Smaat Apps
 * @version 1.0
 * @since 2017-05-03
 */

public class ConsHomeFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, InterfaceBtnCallback {


    @BindView(R.id.photo_gallery_count_txt)
    TextView mPhotoGalleryCountTxt;

    @BindView(R.id.consumer_lay)
    LinearLayout mConsumerLay;

    @BindView(R.id.photo_gallery_lay)
    RelativeLayout mPhotoGalleryLay;

    @BindView(R.id.photographer_lay)
    LinearLayout mPhotographerLay;

    @BindView(R.id.con_select_avid_img)
    ImageView mConSelectAvidImg;

    @BindView(R.id.con_select_skill_img)
    ImageView mConSelectSkillImg;

    @BindView(R.id.con_select_pro_img)
    ImageView mConSelectProImg;

    @BindView(R.id.con_select_avid_pointer_img)
    ImageView mConSelectAvidPointerImg;

    @BindView(R.id.con_select_skill_pointer_img)
    ImageView mConSelectSkillPointerImg;

    @BindView(R.id.con_select_pro_pointer_img)
    ImageView mConSelectProPointerImg;

    @BindView(R.id.moment_amt_txt)
    TextView mMomentAmtTxt;

    @BindView(R.id.each_photo_amt_txt)
    TextView mEachPhotoAmtTxt;


    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<ParsePhotographerEntity> mPhotographerList;
    private ArrayList<ParsePhotoPolEntity> mPhotoPolList;
    private HashMap<String, String> mMomentPhotoHashMap;
    private HashMap<String, ParseObject> mRequestedPhotographerList = new HashMap<>();
    private boolean mMomentCancelledBool = true;
    private Dialog mRequestPhotographerDialog, mCancelPhotographerDialog;

    private int mMomentCountInt = 0, mPhotoAPICountInt = 0, mSkillLevelInt = 0;
    private ParseObject mParseReqMomentPhotographerObj, mParseMomentAcceptObj;
    private Timer mMapPhotographerTimer, mMomentAcceptanceTimer, mCancelCheckMomentTimer;
    private Runnable mContactAnotherPhotographerRunnable;
    private Handler mContactAnotherPhotographerHandler = new Handler();
    private HashMap<Marker, ParsePhotoPolEntity> mPhotoPolMarkersHashMapList = new HashMap<>();
    private LocationSettingsRequest.Builder mLocSettingsReqBuilder;
    private PendingResult<LocationSettingsResult> mPendingResult;
    private final int REQUEST_CHECK_SETTINGS = 300;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_home_screen, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);
        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;

    }

    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {
        super.onFragmentResume();
        /*set header text and header images*/
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.fautus));
        ((HomeScreen) getActivity()).setDrawerAction(true);
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    /* Check for the permission to access location */
        if (permissionsAccessLocation(0)) {
            initView();
        }
    }

    private void initView() {

        /*Init Google API client for location update*/
        initGoogleAPIClient();

        SupportMapFragment fragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_frag);

        /* Map synchronization */
        fragment.getMapAsync(this);

        mConsumerLay.setVisibility(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? View.VISIBLE : View.GONE);
        mPhotoGalleryLay.setVisibility(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? View.VISIBLE : View.GONE);
        mPhotographerLay.setVisibility(PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.USER_IS_CONSUMER) ? View.GONE : View.VISIBLE);


        /*Direct to last Moment*/
        final ConChatDetailsEntity chatEntityRes = PreferenceUtil.getConsumerDetails(getActivity());
        if (chatEntityRes != null && chatEntityRes.getMomentObjIdStr() != null && chatEntityRes.getMomentAdHocCodeStr() != null && chatEntityRes.getPhotographerObjIdStr() != null) {
            Bundle notificationBundle = getActivity().getIntent().getExtras();
            String pushNotificationStatus = AppConstants.FAILURE_CODE;
            if (notificationBundle != null)
                pushNotificationStatus = notificationBundle.getString(AppConstants.PUSH_CHAT_STATUS);

            if (pushNotificationStatus != null && pushNotificationStatus.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                getChatDetails(chatEntityRes);
            } else {
                previousChatDetails(chatEntityRes.getMomentObjIdStr());
            }
        } else {
            DialogManager.getInstance().hideProgress();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsAccessLocation(2);
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    /*Set current location to map view*/
    private void setCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsAccessLocation(4);
            return;
        }
        if (mGoogleMap != null) {
      /* Enable current location */
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            ParseGeoPoint currentLoc = getCurrentLatLong();
            LatLng coordinate = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
        }
    }

    /*Init Google API clients*/
    private void initGoogleAPIClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(ConsHomeFragment.this)
                    .addOnConnectionFailedListener(ConsHomeFragment.this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /*View OnClick*/
    @OnClick({R.id.current_loc_img, R.id.photo_gallery_lay, R.id.photo_gallery_count_txt, R.id.gallery_lay, R.id.con_select_avid_img, R.id.con_select_avid_pointer_img, R.id.con_avid_txt, R.id.con_select_skill_img,
            R.id.con_select_skill_pointer_img, R.id.con_skill_txt, R.id.con_select_pro_img, R.id.con_select_pro_pointer_img, R.id.con_pro_txt,
            R.id.req_photographer_lay, R.id.chat_img, R.id.req_photographer_txt, R.id.code_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_loc_img:
        /* focusing the current location */
                setCurrentLocation();
                break;
            case R.id.photo_gallery_lay:
            case R.id.photo_gallery_count_txt:
            case R.id.gallery_lay:
                AppConstants.MOMENT_FROM_MENU = AppConstants.FAILURE_CODE;
                ((HomeScreen) getActivity()).addFragment(new MomentFragment());
                break;
            case R.id.con_select_avid_img:
            case R.id.con_select_avid_pointer_img:
                if (mSkillLevelInt != 0) {
                    setConsumerData(0);
                    getPhotographerLocMarker();
                }
                break;
            case R.id.con_avid_txt:
        /* Showing the information about avid */
                showPhotographerInfo(getResources().getString(R.string.avid_photo), getResources().getString(R.string.avid_photo_info));
                break;
            case R.id.con_select_skill_img:
            case R.id.con_select_skill_pointer_img:
                if (mSkillLevelInt != 1) {
                    setConsumerData(1);
                    getPhotographerLocMarker();
                }
                break;
            case R.id.con_skill_txt:
        /* Showing the information about Skill */
                showPhotographerInfo(getResources().getString(R.string.skill_photo), getResources().getString(R.string.skill_photo_info));
                break;
            case R.id.con_select_pro_img:
            case R.id.con_select_pro_pointer_img:
                if (mSkillLevelInt != 2) {
                    setConsumerData(2);
                    getPhotographerLocMarker();
                }
                break;
            case R.id.con_pro_txt:
        /* Showing the information about pro */
                showPhotographerInfo(getResources().getString(R.string.pro_photo), getResources().getString(R.string.pro_photo_info));
                break;
            case R.id.req_photographer_lay:
            case R.id.chat_img:
            case R.id.req_photographer_txt:
        /*Checking the internet connection */
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    sendBirdSDKInit();

                    /*contactPhotographer API call*/
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            baseFragmentAlertDismiss(mRequestPhotographerDialog);

                            mRequestPhotographerDialog = DialogManager.getInstance().showPhotographerProgressPopup(getActivity(), new InterfaceBtnCallback() {
                                @Override
                                public void onOkClick() {
                                    if (mParseReqMomentPhotographerObj != null) {
                                        mParseReqMomentPhotographerObj.put(ParseAPIConstants.enabled, false);
                                        mParseReqMomentPhotographerObj.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e != null) {
                                                    mParseReqMomentPhotographerObj.saveEventually();
                                                }
                                                mContactAnotherPhotographerHandler.removeCallbacks(mContactAnotherPhotographerRunnable);
                                                cancelContactPhotographer();
                                            }
                                        });
                                    } else {
                                        mContactAnotherPhotographerHandler.removeCallbacks(mContactAnotherPhotographerRunnable);
                                        cancelContactPhotographer();
                                    }
                                }
                            });
                            mRequestedPhotographerList = new HashMap<>();
                            mMomentCancelledBool = false;
                            /* Request for photographer */
                            requestPhotographer();
                        }
                    });
                } else {
        /*Alert message will be appreared if there is no internet connection*/
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), ConsHomeFragment.this);

                }
                break;
            case R.id.code_txt:
                DialogManager.getInstance().showPhotoSessionCodePopup(getActivity(), new InterfaceTwoEdtCallback() {
                    @Override
                    public void onEdtOneClick(String firstEdtStr, String secondEdtStr) {
                        if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    /* getting the moment using Code*/
                            getMomentsUsingCode(firstEdtStr);
                        } else {
        /*Alert message will be appreared if there is no internet connection*/
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), ConsHomeFragment.this);
                        }
                    }
                });
                break;
        }

    }

    /* Showing the information on photographer's Skill */
    private void showPhotographerInfo(String topicTxtStr, String msgTxtStr) {
        DialogManager.getInstance().showBasicInfoAlertPopup(getActivity(), topicTxtStr.toUpperCase(Locale.US), msgTxtStr, getResources().getString(R.string.close_underline), false, true, new InterfaceTwoBtnCallback() {
            @Override
            public void onYesClick() {

            }

            @Override
            public void onNoClick() {

            }
        });
    }

    /* Get current location */
    private ParseGeoPoint getCurrentLatLong() {

        ParseGeoPoint parseGeoPoint = new ParseGeoPoint();
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*Ask for permission on locatio access
            * Set flag for call back to continue this process*/
                permissionsAccessLocation(3);
            }

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);


            if (mLastLocation != null) {
                parseGeoPoint.setLatitude(mLastLocation.getLatitude());
                parseGeoPoint.setLongitude(mLastLocation.getLongitude());

            }
        }

        return parseGeoPoint;
    }

    /*current Location changed*/
    @Override
    public void onLocationChanged(Location location) {

        ParseUser user = ParseUser.getCurrentUser();
        if (user != null && location != null) {
            user.put(currentLocation, new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
            user.saveInBackground();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        cancelMapPhotographerMarkerTimer();
        cancelCheckMomentForAcceptanceTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mLocSettingsReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
            mPendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocSettingsReqBuilder.build());
            mPendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;

                    }
                }
            });
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                initGoogleAPIClient();
            } else {
                screenAPICall();
            }
        } else {
            mLocSettingsReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
            mPendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocSettingsReqBuilder.build());
            mPendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // API call.
                            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                                initGoogleAPIClient();
                            } else {
                                screenAPICall();
                            }
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                            initGoogleAPIClient();
                        } else {
                            screenAPICall();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // user does not want to update setting. Handle it in a way that it will to affect your app functionality
                        if (getActivity() != null)
                            DialogManager.getInstance().showToast(getActivity(), getString(R.string.user_not_update));
                        break;
                }
                break;
        }
    }

    private void screenAPICall() {
           /*Check for internet connection*/
        if (getActivity() != null) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                setCurrentLocation();
                getMapPhotographerMarkersFromDB();
                startLocationUpdate();
                getMomentCountFromParseDB();
                getParseConfigFromParseDB(0);
            } else {
            /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        DialogManager.getInstance().showToast(getActivity(), "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogManager.getInstance().showToast(getActivity(), connectionResult.getErrorMessage());

    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);              // milli sec
        locationRequest.setFastestInterval(1000);      // milli sec
        locationRequest.setSmallestDisplacement(25f);  // in fet
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    /* Location update */
    private void startLocationUpdate() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
/* location Request */
        locationRequest.setSmallestDisplacement(25f);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           /*Ask for permission on location access*/
            permissionsAccessLocation(1);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, ConsHomeFragment.this);

    }

    /* to stop the location updates */
    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);

    }


    /* Get map markers from Parse Database*/
    private void getPhotographerLocMarker() {
        if (getActivity() != null) {
        /*Map Pol Query*/
            ParseQuery<ParseObject> photoPolQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_POL);
            photoPolQuery.whereWithinMiles(ParseAPIConstants.Location, getCurrentLatLong(), 100);
//        photoPolQuery.setLimit(3);

        /*Map Photographer Query*/
            final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
            photographerQuery.whereEqualTo(ParseAPIConstants.advertisedSkillLevel, mSkillLevelInt);
            photographerQuery.whereEqualTo(ParseAPIConstants.isAvailable, true);
            photographerQuery.include(ParseAPIConstants.user);
            ParseQuery<ParseObject> userQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_User);
            userQuery.whereWithinMiles(ParseAPIConstants.currentLocation, getCurrentLatLong(), (mMomentPhotoHashMap != null && mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestRadius) != null && !mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestRadius).isEmpty()) ? Integer.valueOf(mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestRadius)) : 50);
            photographerQuery.whereMatchesQuery(user, userQuery);
//            photographerQuery.setLimit(3);

        /*get pol list from DB*/
            photoPolQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> polListRes, ParseException ex) {
                    if (ex == null && polListRes != null) {
                        mPhotoPolList = new ArrayList<>();
                        for (int i = 0; i < polListRes.size(); i++) {
                            try {
                                ParsePhotoPolEntity polEntityRes = new ParsePhotoPolEntity();
                                polEntityRes.setAttraction(polListRes.get(i).getString(ParseAPIConstants.Attraction) != null ? polListRes.get(i).getString(ParseAPIConstants.Attraction) : getResources().getString(R.string.app_name));
                                polEntityRes.setFoursquareURL(polListRes.get(i).getString(ParseAPIConstants.FoursquareURL) != null ? polListRes.get(i).getString(ParseAPIConstants.FoursquareURL) : "");
                                polEntityRes.setLocation(polListRes.get(i).getParseGeoPoint(ParseAPIConstants.Location) != null ? polListRes.get(i).getParseGeoPoint(ParseAPIConstants.Location) : getCurrentLatLong());
                                mPhotoPolList.add(polEntityRes);
                            } catch (Exception err) {
                                Log.e(AppConstants.TAG, err.getMessage());
                            }
                        }


                    /*get photographer list from DB*/
                        photographerQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> photographerListRes, ParseException e) {
                                if (e == null && photographerListRes != null) {
                                    mPhotographerList = new ArrayList<>();
                                    for (int i = 0; i < photographerListRes.size(); i++) {
                                        try {
                                            ParsePhotographerEntity photographerRes = new ParsePhotographerEntity();
                                            photographerRes.setUser(photographerListRes.get(i).getParseUser(user));
                                            photographerRes.setAddress1(photographerListRes.get(i).getString(ParseAPIConstants.address1));
                                            photographerRes.setFullName(photographerListRes.get(i).getString(ParseAPIConstants.fullName));
                                            photographerRes.setCurrentLocation(photographerRes.getUser().getParseGeoPoint(currentLocation));
                                            mPhotographerList.add(photographerRes);
                                        } catch (Exception ec) {
                                            Log.e(AppConstants.TAG, ec.getMessage());
                                        }

                                    }
                                    setPhotographerMarkers(mPhotoPolList, mPhotographerList, mGoogleMap);
                                }
                            }
                        });

                    }
                }
            });
        }
    }

    /*Set photographer marker*/
    private void setPhotographerMarkers(final ArrayList<ParsePhotoPolEntity> photoPolMarkersList, final ArrayList<ParsePhotographerEntity> photographerMarkersList, final GoogleMap mMap) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        mMap.clear();
                        /*add photo pol marker to map*/
                        try {
                            mPhotoPolMarkersHashMapList = new HashMap<>();
                            for (int polMarkerPosInt = 0; polMarkerPosInt < photoPolMarkersList.size(); polMarkerPosInt++) {
                                LatLng photographerLoc = new LatLng(photoPolMarkersList.get(polMarkerPosInt).getLocation().getLatitude(), photoPolMarkersList.get(polMarkerPosInt).getLocation().getLongitude());
                                MarkerOptions markerOption = new MarkerOptions().position(photographerLoc);
                                markerOption.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeIcon(getActivity(), R.drawable.fautus_opp_img, getResources().getDimensionPixelSize(R.dimen.size30), getResources().getDimensionPixelSize(R.dimen.size30))));
                                final Marker mapMarker = mMap.addMarker(markerOption);
                                mPhotoPolMarkersHashMapList.put(mapMarker, photoPolMarkersList.get(polMarkerPosInt));
                                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(getActivity()));
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        if (mPhotoPolMarkersHashMapList != null && mPhotoPolMarkersHashMapList.get(mapMarker) != null && mPhotoPolMarkersHashMapList.get(marker).getFoursquareURL() != null && !mPhotoPolMarkersHashMapList.get(marker).getFoursquareURL().isEmpty()) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPhotoPolMarkersHashMapList.get(marker).getFoursquareURL()));
                                            startActivity(intent);
                                        }
                                    }
                                });

                            }

                            for (int photographerMarPosInt = 0; photographerMarPosInt < photographerMarkersList.size(); photographerMarPosInt++) {
                                LatLng photographerLoc = new LatLng(photographerMarkersList.get(photographerMarPosInt).getCurrentLocation().getLatitude(), photographerMarkersList.get(photographerMarPosInt).getCurrentLocation().getLongitude());
                                MarkerOptions markerOption = new MarkerOptions().position(photographerLoc);
                                int markerImg = R.drawable.avid_img;
                                if (mSkillLevelInt > 0) {
                                    markerImg = mSkillLevelInt == 1 ? R.drawable.skill_img : R.drawable.pro_img;
                                }
                                markerOption.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.resizeIcon(getActivity(), markerImg, getResources().getDimensionPixelSize(R.dimen.size30), getResources().getDimensionPixelSize(R.dimen.size30))));

                                mMap.addMarker(markerOption);
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    /*Get moment count from parse DB*/
    private void getMomentCountFromParseDB() {
        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
        userQuery.whereEqualTo(ParseAPIConstants.closed, true);
        userQuery.whereContainedIn(ParseAPIConstants.authorizedUsers, Collections.singletonList(ParseUser.getCurrentUser()));
        userQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> momentObjects, ParseException e) {
                if (e == null && momentObjects != null) {
                    final int maxMomentInt = momentObjects.size();
                    mPhotoAPICountInt = 0;
                    mMomentCountInt = 0;
                    for (int i = 0; i < momentObjects.size(); i++) {
                        ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                        photoQuery.whereEqualTo(ParseAPIConstants.moment, momentObjects.get(i));
                        photoQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                mPhotoAPICountInt += 1;
                                if (e == null && object != null) {
                                    ParseFile photoFile = object.getParseFile(ParseAPIConstants.photo);
                                    if (photoFile != null) {
                                        mMomentCountInt += 1;
                                    }
                                }
                                /* Set moment count to TXT view */
                                mPhotoGalleryCountTxt.setText(mPhotoAPICountInt == maxMomentInt ? String.valueOf(mMomentCountInt) : AppConstants.FAILURE_CODE);
                            }
                        });
                    }
                } else if (getActivity() != null && e != null) {
                    DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.app_name), e.getMessage(), ConsHomeFragment.this);
                }

            }
        });
    }

    /*Get amount Configuration*/
    private void getParseConfigFromParseDB(final int skillLevelInt) {
        mMomentPhotoHashMap = new HashMap<>();
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                if (e == null) {
                    setParseConfig(config, skillLevelInt);
                }
            }
        });

    }

    private void setParseConfig(ParseConfig parseConfig, int skillLevelInt) {
        try {
            mMomentPhotoHashMap.put(ParseAPIConstants.AvidPhoto, parseConfig.getNumber(ParseAPIConstants.AvidPhoto) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.AvidMoment, parseConfig.getNumber(ParseAPIConstants.AvidMoment) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.SkillPhoto, parseConfig.getNumber(ParseAPIConstants.SkillPhoto) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.SkillMoment, parseConfig.getNumber(ParseAPIConstants.SkillMoment) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.ProPhoto, parseConfig.getNumber(ParseAPIConstants.ProPhoto) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.ProMoment, parseConfig.getNumber(ParseAPIConstants.ProMoment) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.MaxRequestRadius, parseConfig.getNumber(ParseAPIConstants.MaxRequestRadius) + "");
            mMomentPhotoHashMap.put(ParseAPIConstants.MaxRequestTime, parseConfig.getNumber(ParseAPIConstants.MaxRequestTime) + "");

        } catch (Exception ex) {
            Log.e(AppConstants.TAG, ex.getMessage());
        }
        setConsumerData(skillLevelInt);
    }

    /*Set consumer details*/
    private void setConsumerData(int skillLevelInt) {
        mSkillLevelInt = skillLevelInt;
        mConSelectAvidImg.setVisibility(skillLevelInt == 0 ? View.VISIBLE : View.INVISIBLE);
        mConSelectSkillImg.setVisibility(skillLevelInt == 1 ? View.VISIBLE : View.INVISIBLE);
        mConSelectProImg.setVisibility(skillLevelInt == 2 ? View.VISIBLE : View.INVISIBLE);

        mConSelectAvidPointerImg.setImageResource(skillLevelInt == 0 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_black_bg);
        mConSelectSkillPointerImg.setImageResource(skillLevelInt == 1 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_black_bg);
        mConSelectProPointerImg.setImageResource(skillLevelInt == 2 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_black_bg);

        String momentAmtStr, eachPhotoAmtStr;

        if (getActivity() != null) {
            momentAmtStr = skillLevelInt > 1 ? ParseAPIConstants.ProMoment : ParseAPIConstants.SkillMoment;
            eachPhotoAmtStr = skillLevelInt > 1 ? ParseAPIConstants.ProPhoto : ParseAPIConstants.SkillPhoto;

            if (skillLevelInt == 0) {
                momentAmtStr = ParseAPIConstants.AvidMoment;
                eachPhotoAmtStr = ParseAPIConstants.AvidPhoto;
            }

            if (mMomentPhotoHashMap != null && mMomentPhotoHashMap.size() > 0) {
                mMomentAmtTxt.setText(getResources().getString(R.string.dollar_sym) + "" + NumberUtil.afterTwoPointVal(mMomentPhotoHashMap.get(momentAmtStr)));
                mEachPhotoAmtTxt.setText(getResources().getString(R.string.dollar_sym) + "" + NumberUtil.afterTwoPointVal(mMomentPhotoHashMap.get(eachPhotoAmtStr)));
            } else {
                getParseConfigFromParseDB(skillLevelInt);
            }
        }

    }

    /* Ask for permission on Location access*/
    private boolean permissionsAccessLocation(final int askPermissionFromIntFlag) {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCoarseLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = isPermission(listPermissionsNeeded, new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                    if (askPermissionFromIntFlag == 0) {
                        initView();
                    } else if (askPermissionFromIntFlag == 1) {
                        startLocationUpdate();

                    } else if (askPermissionFromIntFlag == 2 || askPermissionFromIntFlag == 4) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                        if (askPermissionFromIntFlag == 4) {
                            setCurrentLocation();
                        }

                    }
                }

                public void onNoClick() {

                }
            });
        }

        return addPermission;
    }


    /*Request photographer api call*/
    private void requestPhotographer() {
        mContactAnotherPhotographerHandler.removeCallbacks(mContactAnotherPhotographerRunnable);

        final ParseGeoPoint currentLoc = getCurrentLatLong();
        mParseReqMomentPhotographerObj = new ParseObject(ParseAPIConstants.Parse_Moment);
        mParseReqMomentPhotographerObj.put(ParseAPIConstants.creator, ParseUser.getCurrentUser());
        mParseReqMomentPhotographerObj.put(ParseAPIConstants.authorizedUsers, Collections.singletonList(ParseUser.getCurrentUser()));
        mParseReqMomentPhotographerObj.put(location, currentLoc);
        mParseReqMomentPhotographerObj.put(ParseAPIConstants.skillLevelRequested, mSkillLevelInt);
        mParseReqMomentPhotographerObj.put(ParseAPIConstants.enabled, true);
        mParseReqMomentPhotographerObj.put(ParseAPIConstants.momentDate, new Date());

        try {
            ParseGeoPoint parseGeoPoint = getCurrentLatLong();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.US);
            List<Address> addresses = geocoder.getFromLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality() != null ? addresses.get(0).getLocality() : "";
            String state = addresses.get(0).getAdminArea() != null ? addresses.get(0).getAdminArea() : "";
            String country = addresses.get(0).getCountryName() != null ? addresses.get(0).getCountryName() : "";

            mParseReqMomentPhotographerObj.put(ParseAPIConstants.locationName, address);
            mParseReqMomentPhotographerObj.put(ParseAPIConstants.locationCity, city);
            mParseReqMomentPhotographerObj.put(ParseAPIConstants.locationState, state);
            mParseReqMomentPhotographerObj.put(ParseAPIConstants.locationCountry, country);
        } catch (Exception e) {
            Log.e(getActivity().getClass().getSimpleName(), e.getMessage());
        }


        mParseReqMomentPhotographerObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mParseMomentAcceptObj = mParseReqMomentPhotographerObj;
                if (e == null) {
                    /*Check for internet connection*/
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    /*Contact photographer API call*/
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                contactPhotographer(currentLoc, mParseReqMomentPhotographerObj);
                            }
                        });
                    } else {
                    /*Alert message will be appreared if there is no internet connection*/
                        cancelContactPhotographer();
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), ConsHomeFragment.this);
                    }

                } else {
                    cancelContactPhotographer();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ConsHomeFragment.this);

                }

            }
        });

    }

    /*Contact photographer API cal*/
    private void contactPhotographer(final ParseGeoPoint currentGeoPointer, final ParseObject parseMomentObj) {

        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        photographerQuery.whereEqualTo(ParseAPIConstants.isAvailable, true);
        photographerQuery.include(ParseAPIConstants.user);


        ParseQuery<ParseObject> userQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_User);
        userQuery.whereEqualTo(ParseAPIConstants.emailVerified, true);


        photographerQuery.whereMatchesQuery(ParseAPIConstants.user, userQuery);
        photographerQuery.whereWithinMiles(ParseAPIConstants.currentLocation, currentGeoPointer, (mMomentPhotoHashMap != null && mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestRadius) != null && !mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestRadius).isEmpty()) ? Integer.valueOf(mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestRadius)) : 50);
        ArrayList<Integer> skillLevelStrArr = new ArrayList<>();
        skillLevelStrArr.clear();
        skillLevelStrArr.add(mSkillLevelInt);

        photographerQuery.whereContainedIn(ParseAPIConstants.advertisedSkillLevel, skillLevelStrArr);
        photographerQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && !mMomentCancelledBool && objects != null) {
                    if (objects.size() > 0) {
                        ParseObject photographerObj = null;
                        for (int i = 0; i < objects.size(); i++) {
                            //To check if the photography is already requested to photographer*/
                            if (!mRequestedPhotographerList.containsKey(objects.get(i).getObjectId())) {
                            /* if the request is not sent already, send request to a new photographer */
                                photographerObj = objects.get(i);
                                mRequestedPhotographerList.put(objects.get(i).getObjectId(), objects.get(i));
                                break;
                            }
                        }

                        //if there no photographers available to take the request, then the request will be cancelled */
                        if (photographerObj == null) {
                            maxPhotographerRequestWaitTime();
                        } else if (!mMomentCancelledBool) {
                            sentMomentPhotographerRequest(currentGeoPointer, parseMomentObj, photographerObj);
                        } else {
                            maxPhotographerRequestWaitTime();
                        }

                    } else {
                        cancelContactPhotographer();
                        if (mParseMomentAcceptObj != null) {
                            mParseMomentAcceptObj.put(ParseAPIConstants.accepted, new Date());
                            mParseMomentAcceptObj.put(ParseAPIConstants.enabled, false);
                            mParseMomentAcceptObj.saveEventually();
                        }
                        mCancelPhotographerDialog = DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_ava), ConsHomeFragment.this);
                    }

                } else if (e != null) {
                    cancelContactPhotographer();
                    if (mParseMomentAcceptObj != null) {
                        mParseMomentAcceptObj.put(ParseAPIConstants.accepted, new Date());
                        mParseMomentAcceptObj.saveEventually();
                    }
                    mCancelPhotographerDialog = DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ConsHomeFragment.this);
                }
            }
        });
    }


    /*Max Request Wait Timer*/
    private void maxPhotographerRequestWaitTime() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelMomentTimer();
                mCancelCheckMomentTimer = new Timer();
                /*Default interval time*/
                int requestTimeOutIntervalInt = 60000;

                /*Get max request value from DB and set to interval time*/
                if (mMomentPhotoHashMap != null && mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestTime) != null && !mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestTime).isEmpty()) {
                    requestTimeOutIntervalInt = (Integer.valueOf(mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestTime)) / 60) * 1000;
                }

                mCancelCheckMomentTimer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (getActivity() != null && !mMomentCancelledBool) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mCancelPhotographerDialog = DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_ava), ConsHomeFragment.this);
                                        }
                                    });
                                }
                                if (mParseMomentAcceptObj != null) {
                                    mParseMomentAcceptObj.put(ParseAPIConstants.accepted, new Date());
                                    mParseMomentAcceptObj.saveEventually();
                                }
                                cancelContactPhotographer();
                            }
                        }, requestTimeOutIntervalInt);
            }
        });
    }

    /* Send moment request to photographer */
    private void sentMomentPhotographerRequest(final ParseGeoPoint currentGeoPointer, final ParseObject parseMomentObj, final ParseObject photographerObj) {
        if (NetworkUtil.isNetworkAvailable(getActivity())) {
            photographerObj.getParseObject(ParseAPIConstants.user).fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        HashMap<String, Object> params = new HashMap<>();
                        params.put(ParseAPIConstants.photographer, object.getObjectId());
                        params.put(ParseAPIConstants.moment, parseMomentObj.getObjectId());
                        saveMomentLog(parseMomentObj, photographerObj);
                        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_requestPhotographer, params, new FunctionCallback<Object>() {
                            public void done(Object resObject, ParseException e) {
                                DialogManager.getInstance().hideProgress();
                                if (e != null) {
                                    cancelContactPhotographer();
                                    try {
                                        JSONObject errorJsonObj = new JSONObject(e.getMessage());
                                        String messageStr = errorJsonObj.getString(getString(R.string.message));
                                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), ConsHomeFragment.this);
                                    } catch (Exception ex) {
                                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ConsHomeFragment.this);
                                        Log.e(AppConstants.TAG, ex.getMessage());
                                    }
                                } else if (mMomentCancelledBool) {
                                    cancelContactPhotographer();
                                }
                            }
                        });
                        checkMomentForAcceptance();
                        contactAnotherPhotographer(currentGeoPointer, parseMomentObj);
                    } else if (e != null) {
                        cancelContactPhotographer();
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ConsHomeFragment.this);

                    }
                }
            });

        } else {
            cancelContactPhotographer();
            /*Alert message will be appreared if there is no internet connection*/
            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), ConsHomeFragment.this);

        }

    }

    /* cancel photographer request */
    private void cancelContactPhotographer() {
        mMomentCancelledBool = true;
        baseFragmentAlertDismiss(mRequestPhotographerDialog);
        cancelCheckMomentForAcceptanceTimer();
        cancelMomentTimer();
    }

    private void cancelMomentTimer() {
        if (mCancelCheckMomentTimer != null) {
            mCancelCheckMomentTimer.cancel();
            mCancelCheckMomentTimer.purge();
        }
    }

    /* save moment log in parse DB */
    private void saveMomentLog(ParseObject momentObj, ParseObject photographerObj) {
        ParseObject mParseMomentLogObj = new ParseObject(ParseAPIConstants.Parse_MomentLog);
        mParseMomentLogObj.put(ParseAPIConstants.moment, momentObj);
        mParseMomentLogObj.put(ParseAPIConstants.photographer, photographerObj);
        mParseMomentLogObj.put(ParseAPIConstants.date, new Date());
        mParseMomentLogObj.saveEventually();
    }

    /* Contacting another photographer */
    private void contactAnotherPhotographer(final ParseGeoPoint currentGeoPointer, final ParseObject parseMomentObj) {

        int requestTimeOutIntervalInt = 60000;

                /*Get max request value from DB and set to interval time*/
        if (mMomentPhotoHashMap != null && mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestTime) != null && !mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestTime).isEmpty()) {
            requestTimeOutIntervalInt = (Integer.valueOf(mMomentPhotoHashMap.get(ParseAPIConstants.MaxRequestTime)) / 60) * 1000;
        }

        mContactAnotherPhotographerRunnable = new Runnable() {
            @Override
            public void run() {

                mContactAnotherPhotographerHandler.removeCallbacks(mContactAnotherPhotographerRunnable);
                contactPhotographer(currentGeoPointer, parseMomentObj);

            }
        };
        /*set handler for wait 15 sec */
        mContactAnotherPhotographerHandler.postDelayed(mContactAnotherPhotographerRunnable, requestTimeOutIntervalInt);
    }

    /* to check if the photographer accepted the moment or not */
    private void checkMomentForAcceptance() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelCheckMomentForAcceptanceTimer();
                mMomentAcceptanceTimer = new Timer();
                mMomentAcceptanceTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        getConnectToChat(mParseMomentAcceptObj.getObjectId());
                    }
                }, 0, 2000);
            }
        });
    }

    /* to check if the photographer accepted the moment or not */
    private void getMapPhotographerMarkersFromDB() {
        cancelMapPhotographerMarkerTimer();
        mMapPhotographerTimer = new Timer();
        mMapPhotographerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getPhotographerLocMarker();
            }
        }, 3000, 3000);

    }


    /*get previous moment details*/
    private void previousChatDetails(String momentObjIDStr) {
        DialogManager.getInstance().showProgress(getActivity());
        ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
        momentQuery.whereEqualTo(ParseAPIConstants.objectId, momentObjIDStr);

        momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject momentObject, ParseException e) {
                if (e == null && momentObject != null && momentObject.getParseObject(ParseAPIConstants.photographer) != null) {
                    momentObject.getParseObject(ParseAPIConstants.photographer).fetchInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject photographerObject, ParseException e) {
                            DialogManager.getInstance().hideProgress();
                            if (e == null && photographerObject != null) {

                                try {
                                    DialogManager.getInstance().showOptionAlertPopup(getActivity(), getString(R.string.moment) + " " + getResources().getString(R.string.colon_sym) + " " + momentObject.getString(ParseAPIConstants.adHocCode), getString(R.string.previous_moment_msg), getString(R.string.no), getString(R.string.yes), new InterfaceTwoBtnCallback() {
                                        @Override
                                        public void onYesClick() {
                                            baseFragmentAlertDismiss(mCancelPhotographerDialog);
                                          /* after the acceptance of moment, direct the screen to chat screen */
                                            AppConstants.CHAT_PHOTOGRAPHER_DETAILS = photographerObject;
                                            AppConstants.CHAT_MOMENT_DETAILS = momentObject;
                                            AppConstants.CHAT_CONSUMER_DETAILS = ParseUser.getCurrentUser();
                                            AppConstants.SHOW_CON_PHOTO_DIALOG = AppConstants.FAILURE_CODE;


                                            ConChatDetailsEntity chatDetails = new ConChatDetailsEntity();
                                            chatDetails.setMomentObjIdStr(AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
                                            chatDetails.setMomentAdHocCodeStr(AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.adHocCode));
                                            chatDetails.setPhotographerObjIdStr(AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getObjectId());
                                            PreferenceUtil.storeConsumerChatDetails(getActivity(), chatDetails);

                                            AppConstants.SEND_BIRD_GROUP_CHANNEL = null;

                                            cancelMomentTimer();
                                            ((HomeScreen) getActivity()).addFragment(new ChatFragment());
                                        }

                                        @Override
                                        public void onNoClick() {
                                            DialogManager.getInstance().showProgress(getActivity());
                                            momentObject.put(ParseAPIConstants.enabled, false);
                                            momentObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    DialogManager.getInstance().hideProgress();
                                                    if (e != null) {
                                                        momentObject.saveEventually();
                                                    }
                                                    disconnectChat();
                                                    PreferenceUtil.storeConsumerChatDetails(getActivity(), null);
                                                }
                                            });
                                        }
                                    });
                                } catch (Exception ex) {
                                    Log.e(AppConstants.TAG, ex.toString());
                                }

                            }
                        }
                    });
                } else if (e != null) {
                    PreferenceUtil.storeConsumerChatDetails(getActivity(), null);
                    DialogManager.getInstance().hideProgress();
                }
            }
        });
    }

    private void getConnectToChat(String momentObjIDStr) {
        ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
        momentQuery.whereEqualTo(ParseAPIConstants.objectId, momentObjIDStr);

        momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject momentObject, ParseException e) {
                if (e == null && momentObject != null && momentObject.get(ParseAPIConstants.photographer) != null&& momentObject.getParseObject(ParseAPIConstants.photographer) != null
                        && momentObject.get(ParseAPIConstants.accepted) != null&& momentObject.getDate(ParseAPIConstants.accepted) != null) {
                    cancelCheckMomentForAcceptanceTimer();
                    cancelContactPhotographer();
                    momentObject.getParseObject(ParseAPIConstants.photographer).fetchInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject photographerObject, ParseException e) {
                            DialogManager.getInstance().hideProgress();
                            if (e == null && photographerObject != null) {
                                cancelMomentTimer();
                                baseFragmentAlertDismiss(mCancelPhotographerDialog);
                        /* after the acceptance of moment, direct the screen to chat screen */
                                AppConstants.CHAT_PHOTOGRAPHER_DETAILS = photographerObject;
                                AppConstants.CHAT_MOMENT_DETAILS = momentObject;
                                AppConstants.CHAT_CONSUMER_DETAILS = ParseUser.getCurrentUser();
                                AppConstants.SHOW_CON_PHOTO_DIALOG = AppConstants.SUCCESS_CODE;


                                ConChatDetailsEntity chatDetails = new ConChatDetailsEntity();
                                chatDetails.setMomentObjIdStr(AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
                                chatDetails.setMomentAdHocCodeStr(AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.adHocCode));
                                chatDetails.setPhotographerObjIdStr(AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getObjectId());

                                mContactAnotherPhotographerHandler.removeCallbacks(mContactAnotherPhotographerRunnable);

                                PreferenceUtil.storeConsumerChatDetails(getActivity(), chatDetails);
                                AppConstants.SEND_BIRD_GROUP_CHANNEL = null;
                                ((HomeScreen) getActivity()).addFragment(new ChatFragment());
                            }
                        }
                    });
                } else if (e != null) {
                    DialogManager.getInstance().hideProgress();
                }
            }
        });
    }

    /*Cancelling the moment acceptance timer */
    private void cancelCheckMomentForAcceptanceTimer() {
        if (mMomentAcceptanceTimer != null) {
            mMomentAcceptanceTimer.cancel();
            mMomentAcceptanceTimer.purge();
        }

    }

    /*Cancelling the moment acceptance timer */
    private void cancelMapPhotographerMarkerTimer() {
        if (mMapPhotographerTimer != null) {
            mMapPhotographerTimer.cancel();
            mMapPhotographerTimer.purge();
        }

    }


    /* getting the moment using Code*/
    private void getMomentsUsingCode(String momentCodeStr) {
        DialogManager.getInstance().showProgress(getActivity());

        ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
        momentQuery.whereEqualTo(ParseAPIConstants.adHocCode, momentCodeStr.toUpperCase());
        momentQuery.whereEqualTo(ParseAPIConstants.closed, true);
        momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    List<Object> userList = object.getList(ParseAPIConstants.authorizedUsers);

                    if (!userList.contains(ParseUser.getCurrentUser())) {
                        userList.add(ParseUser.getCurrentUser());
                        object.put(ParseAPIConstants.authorizedUsers, userList);
                        object.saveInBackground();
                    }


                    ParseQuery<ParseObject> photoQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photo);
                    photoQuery.whereEqualTo(ParseAPIConstants.moment, object);
                    photoQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> momentPhotosObjList, ParseException e) {
                            DialogManager.getInstance().hideProgress();
                            if (e == null && momentPhotosObjList != null) {
                                if (momentPhotosObjList.size() > 0) {
                                    ArrayList<PhotoEntity> ParseFileObj = new ArrayList<>();
                                    int purchasedPhotosCountInt = 0;
                                    for (int j = 0; j < momentPhotosObjList.size(); j++) {
                                        PhotoEntity photoFile = new PhotoEntity();
                                        photoFile.setPhotoObj(momentPhotosObjList.get(j));
                                        photoFile.setPhoto(momentPhotosObjList.get(j).getParseFile(ParseAPIConstants.photo));
                                        photoFile.setPhoto_purchased(momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased) ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
                                        if (momentPhotosObjList.get(j).getBoolean(ParseAPIConstants.purchased)) {
                                            purchasedPhotosCountInt += 1;
                                        }
                                        ParseFileObj.add(photoFile);
                                    }
                                    /* direct to moment details screen */
                                    AppConstants.MOMENT_PHOTO_ENTITY = new MomentPhotoEntity();
                                    AppConstants.MOMENT_PHOTO_ENTITY.setPurchasedPhotosCount(String.valueOf(purchasedPhotosCountInt));
                                    AppConstants.MOMENT_ALREADY_BOUGHT = ParseFileObj.size() == purchasedPhotosCountInt ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                                    AppConstants.MOMENT_PHOTO_ENTITY.setMoment(object);
                                    AppConstants.MOMENT_PHOTO_ENTITY.setPhoto(ParseFileObj);
                                    AppConstants.MOMENT_FROM_LIST = AppConstants.FAILURE_CODE;
                                    ((HomeScreen) getActivity()).addFragment(new MomentDetailsFragment());
                                }

                            } else if (e != null) {
                                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), ConsHomeFragment.this);
                            }


                        }
                    });

                } else {
                    DialogManager.getInstance().hideProgress();
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getResources().getString(R.string.no_moment), ConsHomeFragment.this);
                }
            }
        });

    }


    /* initialization for SendBird chat SDK */
    private void sendBirdSDKInit() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            String senderIdStr = user.getString(ParseAPIConstants.username) != null ? user.getString(ParseAPIConstants.username) : getResources().getString(R.string.user_one);
            SendBird.connect(senderIdStr, new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {

                }
            });
        }
    }


    @Override
    public void onOkClick() {

    }

    /*Showing Map info*/
    private class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        Context context;

        private MarkerInfoWindowAdapter(Context ctx) {
            context = ctx;
        }

        @Override
        public View getInfoWindow(final Marker marker) {

            final ViewGroup nullParent = null;
            View v = null;
            if (getActivity() != null && mPhotoPolMarkersHashMapList.get(marker) != null) {
                v = getActivity().getLayoutInflater().inflate(R.layout.adap_map_info_view, nullParent, false);
                final TextView photoOppNameTxt = v.findViewById(R.id.photo_opp_name_txt);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPhotoPolMarkersHashMapList.get(marker) != null) {
                            photoOppNameTxt.setText(mPhotoPolMarkersHashMapList.get(marker).getAttraction());
                        }
                    }
                });
            }
            return v;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

    }


    private void getChatDetails(final ConChatDetailsEntity chatEntityRes) {
        DialogManager.getInstance().showProgress(getActivity());
        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        photographerQuery.whereEqualTo(ParseAPIConstants.objectId, chatEntityRes.getPhotographerObjIdStr());
        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject photoObject, ParseException e) {
                if (photoObject != null) {
                    ParseQuery<ParseObject> momentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
                    momentQuery.whereEqualTo(ParseAPIConstants.objectId, chatEntityRes.getMomentObjIdStr());
                    momentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject momentObject, ParseException e) {
                            DialogManager.getInstance().hideProgress();
                            if (momentObject != null) {
                                AppConstants.CHAT_PHOTOGRAPHER_DETAILS = photoObject;
                                AppConstants.CHAT_MOMENT_DETAILS = momentObject;
                                AppConstants.CHAT_CONSUMER_DETAILS = momentObject.getParseObject(ParseAPIConstants.creator);
                                AppConstants.SHOW_CON_PHOTO_DIALOG = AppConstants.FAILURE_CODE;
                                ((HomeScreen) getActivity()).addFragment(new ChatFragment());
                            }
                        }
                    });
                } else {
                    DialogManager.getInstance().hideProgress();
                }
            }
        });
    }

    /* Disconnect SendBird Chat SDK */
    private void disconnectChat() {
        if (AppConstants.SEND_BIRD_GROUP_CHANNEL != null) {
            AppConstants.SEND_BIRD_GROUP_CHANNEL.leave(new GroupChannel.GroupChannelLeaveHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    SendBird.disconnect(new SendBird.DisconnectHandler() {
                        @Override
                        public void onDisconnected() {
                        }
                    });
                }
            });
        }
    }
}
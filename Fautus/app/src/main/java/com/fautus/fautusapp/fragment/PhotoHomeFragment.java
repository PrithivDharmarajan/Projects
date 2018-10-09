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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.ParsePhotoPolEntity;
import com.fautus.fautusapp.entity.ParsePhotographerEntity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.ui.HomeScreen;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.ImageUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.NumberUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.apptik.widget.MultiSlider;

import static com.fautus.fautusapp.R.id.ava_not_ava_lay;
import static com.fautus.fautusapp.utils.ParseAPIConstants.currentLocation;
import static com.fautus.fautusapp.utils.ParseAPIConstants.location;
import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * Created by sys on 03-May-17.
 */

public class PhotoHomeFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, InterfaceBtnCallback {


    @BindView(R.id.consumer_lay)
    LinearLayout mConsumerLay;

    @BindView(R.id.photo_gallery_lay)
    RelativeLayout mPhotoGalleryLay;

    @BindView(R.id.photographer_lay)
    LinearLayout mPhotographerLay;

    @BindView(ava_not_ava_lay)
    RelativeLayout mAvaNotAvaLay;

    @BindView(R.id.not_ava_img)
    ImageView mNotAvaImg;

    @BindView(R.id.ava_img)
    ImageView mAvaImg;

    @BindView(R.id.photo_select_avid_img)
    ImageView mPhotoSelectAvidImg;

    @BindView(R.id.photo_select_skill_img)
    ImageView mPhotoSelectSkillImg;

    @BindView(R.id.photo_select_pro_img)
    ImageView mPhotoSelectProImg;

    @BindView(R.id.photo_select_skill_pointer_img)
    ImageView mPhotoSelectSkillPointerImg;

    @BindView(R.id.first_view)
    View mFirstView;

    @BindView(R.id.second_view)
    View mSecondView;

    @BindView(R.id.photographer_skill_slider)
    MultiSlider mPhotographerSkillSlider;

    @BindView(R.id.photo_un_select_avid_pointer_img)
    ImageView mPhotoUnSelectAvidPointerImg;

    @BindView(R.id.photo_un_select_skill_pointer_img)
    ImageView mPhotoUnSelectSkillPointerImg;

    @BindView(R.id.photo_un_select_pro_pointer_img)
    ImageView mPhotoUnSelectProPointerImg;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<ParsePhotoPolEntity> mPhotoPolList;
    private ArrayList<ParsePhotographerEntity> mPhotographerList;
    private ParseObject mPhotographerParseObj;
    private int mPhotographerMinSkillInt = 0, mPhotographerMaxSkillInt = 0, mMaxRequestRadiusInt = 50;
    private Timer mNewCustomerTimer;
    private Dialog mNewUserDialog;
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
        /*set header text and header right img*/
        ((HomeScreen) getActivity()).setHeaderText(getString(R.string.fautus));
        ((HomeScreen) getActivity()).setDrawerAction(true);
        ((HomeScreen) getActivity()).setHeadRightImgVisible(false, R.mipmap.app_icon);
        if (permissionsAccessLocation(0)) {
            initView();
        } else {
            mConsumerLay.setVisibility(View.GONE);
            mPhotoGalleryLay.setVisibility(View.GONE);
            mPhotographerLay.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {

        /*Init Google API client for location update*/
        initGoogleAPIClient();
        mAvaNotAvaLay.setTag(AppConstants.FAILURE_CODE);

        /*init Google map*/
        FragmentManager fragmentManager = getChildFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map_frag);

        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map_frag, fragment).commit();
        }

        fragment.getMapAsync(this);



        /*Set Seek bar default data*/
        mPhotographerSkillSlider.getThumb(0).setMax(0);
        mPhotographerSkillSlider.getThumb(0).setValue(0);
        mPhotographerSkillSlider.getThumb(0).setValue(0).setEnabled(true);
        mPhotographerSkillSlider.getThumb(1).setMax(0);
        mPhotographerSkillSlider.getThumb(1).setValue(0);
        mPhotographerSkillSlider.getThumb(1).setValue(0).setEnabled(true);


        mPhotoSelectSkillPointerImg.setVisibility(View.GONE);
        mPhotoUnSelectAvidPointerImg.setVisibility(View.GONE);
        mPhotoUnSelectSkillPointerImg.setVisibility(View.VISIBLE);
        mPhotoUnSelectProPointerImg.setVisibility(View.VISIBLE);


        mConsumerLay.setVisibility(View.GONE);
        mPhotoGalleryLay.setVisibility(View.GONE);
        mPhotographerLay.setVisibility(View.VISIBLE);
        ((HomeScreen) getActivity()).setHeaderRightText(getResources().getString(R.string.dollar_sym) + "" + NumberUtil.afterTwoPointVal(AppConstants.PHOTOGRAPHER_TOTAL_BAL_AMT));
        getAccountBal();

        ((HomeScreen) getActivity()).mHeaderRightLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void setSeekListener() {
        mPhotographerSkillSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {

                mPhotoSelectSkillPointerImg.setVisibility(mPhotographerSkillSlider.getThumb(0).getValue() > 50 || mPhotographerSkillSlider.getThumb(1).getValue() < 50 ? View.GONE : View.VISIBLE);
                mPhotoUnSelectSkillPointerImg.setVisibility(mPhotographerSkillSlider.getThumb(0).getValue() > 50 || mPhotographerSkillSlider.getThumb(1).getValue() < 50 ? View.VISIBLE : View.GONE);
                mPhotoUnSelectAvidPointerImg.setVisibility(mPhotographerSkillSlider.getThumb(0).getValue() > 3 ? View.VISIBLE : View.GONE);
                mPhotoUnSelectProPointerImg.setVisibility(mPhotographerSkillSlider.getThumb(1).getValue() < 98 ? View.VISIBLE : View.GONE);

            }
        });

        mPhotographerSkillSlider.setOnTrackingChangeListener(new MultiSlider.OnTrackingChangeListener() {
            @Override
            public void onStartTrackingTouch(MultiSlider multiSlider, MultiSlider.Thumb thumb, int value) {

            }

            @Override
            public void onStopTrackingTouch(MultiSlider multiSlider, MultiSlider.Thumb thumb, int value) {


                if (mPhotographerSkillSlider.getThumb(0).getValue() < 25) {
                    mPhotographerMinSkillInt = 0;
                    mPhotographerSkillSlider.getThumb(0).setValue(0).setEnabled(true);
                }
                if ((mPhotographerSkillSlider.getThumb(0).getValue() <= 50 && mPhotographerSkillSlider.getThumb(0).getValue() >= 25) || (mPhotographerSkillSlider.getThumb(0).getValue() > 50 && mPhotographerSkillSlider.getThumb(0).getValue() <= 75)) {
                    mPhotographerMinSkillInt = 1;
                    mPhotographerSkillSlider.getThumb(0).setValue(50).setEnabled(true);
                }
                if (mPhotographerSkillSlider.getThumb(0).getValue() > 75) {
                    mPhotographerMinSkillInt = 2;
                    mPhotographerSkillSlider.getThumb(0).setValue(100).setEnabled(true);
                }

                if (mPhotographerSkillSlider.getThumb(1).getValue() > 75) {
                    mPhotographerMaxSkillInt = 2;
                    mPhotographerSkillSlider.getThumb(1).setValue(100).setEnabled(true);
                }
                if ((mPhotographerSkillSlider.getThumb(1).getValue() >= 50 && mPhotographerSkillSlider.getThumb(1).getValue() <= 75) || (mPhotographerSkillSlider.getThumb(1).getValue() < 50 && mPhotographerSkillSlider.getThumb(1).getValue() >= 25)) {
                    mPhotographerMaxSkillInt = 1;
                    mPhotographerSkillSlider.getThumb(1).setValue(50).setEnabled(true);
                }
                if (mPhotographerSkillSlider.getThumb(1).getValue() < 25) {
                    mPhotographerMaxSkillInt = 0;
                    mPhotographerSkillSlider.getThumb(1).setValue(0).setEnabled(true);
                }

                if (mPhotographerParseObj != null) {
                    ArrayList<Integer> advertisedSkillLevelStrArr = new ArrayList<>();
                    for (int i = mPhotographerMinSkillInt; i <= mPhotographerMaxSkillInt; i++) {
                        advertisedSkillLevelStrArr.add(i);
                    }
                    mPhotographerParseObj.put(ParseAPIConstants.advertisedSkillLevel, advertisedSkillLevelStrArr);
                    if (getCurrentLatLong() != null && getCurrentLatLong().getLongitude() != 0.0 && getCurrentLatLong().getLongitude() != 0.0) {
                        mPhotographerParseObj.put(ParseAPIConstants.currentLocation, getCurrentLatLong());
                    }
                    mPhotographerParseObj.saveInBackground();
                }
                getPhotographerLocMarker();
            }
        });

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
        mGoogleMap.getUiSettings().setCompassEnabled(false);
    }

    /*set photographer available details*/
    private void setPhotographerAvaDetails() {

        boolean tagNotAvaBool = mAvaNotAvaLay.getTag().toString().equalsIgnoreCase(AppConstants.FAILURE_CODE);
        if (getActivity() != null) {
            mAvaNotAvaLay.setBackground(tagNotAvaBool ? ContextCompat.getDrawable(getActivity(), R.drawable.rounded_sky_blue_bg) : ContextCompat.getDrawable(getActivity(), R.drawable.rounded_white_bg));
            mNotAvaImg.setVisibility(tagNotAvaBool ? View.GONE : View.VISIBLE);
            mAvaImg.setVisibility(tagNotAvaBool ? View.VISIBLE : View.GONE);
            mAvaNotAvaLay.setTag(tagNotAvaBool ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE);
        }
    }

    /*Set photographer details*/
    private void setPhotographerData(int skillLevelInt, int skillMiniLevelInt, int skillMaxLevelInt) {

        mPhotoSelectAvidImg.setVisibility(View.VISIBLE);
        mPhotoSelectSkillImg.setVisibility(skillLevelInt > 0 ? View.VISIBLE : View.INVISIBLE);
        mPhotoSelectProImg.setVisibility(skillLevelInt == 2 ? View.VISIBLE : View.INVISIBLE);

        int skillTotMaxVal = 0;
        if (skillLevelInt > 0) {
            skillTotMaxVal = skillLevelInt == 1 ? 50 : 100;
        }

        int skillSelectedMiniVal = 0;
        int skillSelectedMaxVal = 0;

        if (skillMiniLevelInt > 0) {
            skillSelectedMiniVal = skillMiniLevelInt == 1 ? 50 : 100;
        }
        if (skillMaxLevelInt > 0) {
            skillSelectedMaxVal = skillMaxLevelInt == 1 ? 50 : 100;
        }


        mPhotographerSkillSlider.getThumb(0).setMax(skillTotMaxVal);
        mPhotographerSkillSlider.getThumb(0).setValue(skillSelectedMiniVal);
        mPhotographerSkillSlider.getThumb(1).setMax(skillTotMaxVal);
        mPhotographerSkillSlider.getThumb(1).setValue(skillSelectedMaxVal);

        setSeekListener();

        mPhotographerSkillSlider.getThumb(0).setMax(skillTotMaxVal);
        mPhotographerSkillSlider.getThumb(0).setValue(skillSelectedMiniVal);
        mPhotographerSkillSlider.getThumb(1).setMax(skillTotMaxVal);
        mPhotographerSkillSlider.getThumb(1).setValue(skillSelectedMaxVal);


    }


    /*Set current location to map view*/
    private void setCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsAccessLocation(4);
            return;
        }
        if (mGoogleMap != null) {
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
                    .addConnectionCallbacks(PhotoHomeFragment.this)
                    .addOnConnectionFailedListener(PhotoHomeFragment.this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /*View OnClick*/
    @OnClick({R.id.current_loc_img, R.id.ava_not_ava_lay, R.id.photo_avid_txt, R.id.photo_select_skill_img,
            R.id.photo_skill_txt, R.id.photo_select_pro_img, R.id.photo_pro_txt, R.id.create_snap_photo_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_loc_img:
                setCurrentLocation();
                break;
            case R.id.photo_avid_txt:
                showPhotographerInfo(getResources().getString(R.string.avid_photo), getResources().getString(R.string.avid_photo_info));
                break;
            case R.id.photo_skill_txt:
                showPhotographerInfo(getResources().getString(R.string.skill_photo), getResources().getString(R.string.skill_photo_info));
                break;
            case R.id.photo_pro_txt:
                showPhotographerInfo(getResources().getString(R.string.pro_photo), getResources().getString(R.string.pro_photo_info));
                break;
            case ava_not_ava_lay:
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    /*updatePhotographerAvailabilityToDB API call*/
                    updatePhotographerAvailabilityToDB();
                } else {
                    /*Internet alert*/
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), PhotoHomeFragment.this);

                }

                break;
            case R.id.photo_select_skill_img:
                if (mPhotographerMaxSkillInt < 1) {
                    String maxSkillLevelStr = getResources().getString(R.string.avid);
                    if (mPhotographerMaxSkillInt > 0) {
                        maxSkillLevelStr = mPhotographerMaxSkillInt == 1 ? getResources().getString(R.string.skill) : getResources().getString(R.string.pro);
                    }
                    DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.oops), String.format(getResources().getString(R.string.you_reg), maxSkillLevelStr.toUpperCase(Locale.US)), PhotoHomeFragment.this);
                }
                break;
            case R.id.photo_select_pro_img:
                if (mPhotographerMaxSkillInt < 2) {
                    String maxSkillLevelStr = getResources().getString(R.string.avid);
                    if (mPhotographerMaxSkillInt > 0) {
                        maxSkillLevelStr = mPhotographerMaxSkillInt == 1 ? getResources().getString(R.string.skill) : getResources().getString(R.string.pro);
                    }

                    DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.oops), String.format(getResources().getString(R.string.you_reg), maxSkillLevelStr.toUpperCase(Locale.US)), PhotoHomeFragment.this);
                }
                break;
            case R.id.create_snap_photo_txt:
                 /*Check internet connection*/
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    DialogManager.getInstance().showProgress(getActivity());
                    if (mPhotographerParseObj != null) {
                        createSnapMoment();
                    } else {
                        ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
                        photographerQuery.whereEqualTo(ParseAPIConstants.user, ParseUser.getCurrentUser());
                        photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null && object != null) {
                                    mPhotographerParseObj = object;
                                    createSnapMoment();
                                } else {
                                    DialogManager.getInstance().hideProgress();
                                    if (e != null)
                                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.toString(), PhotoHomeFragment.this);

                                }
                            }
                        });
                    }


                } else {
                    DialogManager.getInstance().hideProgress();
                    /*Alert message will be appeared if there is no internet connection*/
                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), PhotoHomeFragment.this);
                }
                break;

        }

    }

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

    private ParseGeoPoint getCurrentLatLong() {
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint();
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*Ask location access permission
            * Set flag for call back continue this process
            * */
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
        if (user != null) {
            user.put(currentLocation, new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
            user.saveInBackground();
        }
        if (mPhotographerParseObj != null) {
            mPhotographerParseObj.put(currentLocation, new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
            mPhotographerParseObj.saveInBackground();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            checkForNewCustomers();
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
                                // Show the dialog by calling startResolutionForResult(),
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

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);              // milli sec
        locationRequest.setFastestInterval(1000);      // milli sec
        locationRequest.setSmallestDisplacement(25f);  // in fet
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void screenAPICall() {
            /*Check internet connection*/
        if (getActivity() != null) {
            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                setCurrentLocation();
                getPhotographerDetailsFromDB();
                getPhotographerLocMarker();
                startLocationUpdate();
                checkForNewCustomers();
            } else {
            /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), getString(R.string.no_internet), this);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        cancelNewCustomerTimer();
    }

    @Override
    public void onConnectionSuspended(int i) {
        DialogManager.getInstance().showToast(getActivity(), "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogManager.getInstance().showToast(getActivity(), connectionResult.getErrorMessage());

    }

    private void startLocationUpdate() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setSmallestDisplacement(25f);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           /*Ask location permission*/
            permissionsAccessLocation(1);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, PhotoHomeFragment.this);

    }

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
            photographerQuery.whereEqualTo(ParseAPIConstants.advertisedSkillLevel, mPhotographerMinSkillInt);
            photographerQuery.whereEqualTo(ParseAPIConstants.isAvailable, true);
            photographerQuery.include(ParseAPIConstants.user);
            ParseQuery<ParseObject> userQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_User);
            userQuery.whereWithinMiles(ParseAPIConstants.currentLocation, getCurrentLatLong(), mMaxRequestRadiusInt);
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
                                if (mPhotographerMinSkillInt > 0) {
                                    markerImg = mPhotographerMinSkillInt == 1 ? R.drawable.skill_img : R.drawable.pro_img;
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


    /*get photographer details from DB*/
    private void updatePhotographerAvailabilityToDB() {
        DialogManager.getInstance().showProgress(getActivity());
        if (mPhotographerParseObj == null) {
            final ParseQuery<ParseObject> photographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
            photographerQuery.whereEqualTo(user, ParseUser.getCurrentUser());
            photographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null && object != null) {
                        mPhotographerParseObj = object;
                        checkPhotographerAva();
                    } else {
                        DialogManager.getInstance().hideProgress();
                    }
                }
            });
        } else {
            checkPhotographerAva();
        }
    }

    /*Update photographer availability to DB*/
    private void checkPhotographerAva() {

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();

        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_getStripePhotographerAccountDetails, params, new FunctionCallback<HashMap<String, String>>() {
            @Override
            public void done(HashMap object, ParseException e) {
                if (e == null && object != null) {
                    boolean isAccVerified = true;

                    if (mAvaNotAvaLay.getTag().toString().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                        try {
                            isAccVerified = object.get(ParseAPIConstants.transfers_enabled) != null && (boolean) object.get(ParseAPIConstants.transfers_enabled);

                        } catch (Exception ex) {
                            isAccVerified = false;
                            Log.e(getActivity().getClass().getSimpleName(), ex.getMessage());
                        }
                    }
                    if (isAccVerified) {
                        mPhotographerParseObj.put(ParseAPIConstants.isAvailable, mAvaNotAvaLay.getTag().toString().equalsIgnoreCase(AppConstants.FAILURE_CODE));
                        mPhotographerParseObj.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                DialogManager.getInstance().hideProgress();
                                if (e == null) {
                                    mAvaNotAvaLay.setTag(mAvaNotAvaLay.getTag().toString().equalsIgnoreCase(AppConstants.FAILURE_CODE) ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE);
                                    setPhotographerAvaDetails();
                                    mPhotographerParseObj.saveEventually();
                                } else {
                                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), PhotoHomeFragment.this);
                                }
                            }
                        });

                    } else {
                        DialogManager.getInstance().hideProgress();
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.email_verify_topic), getString(R.string.email_verify_msg), PhotoHomeFragment.this);
                    }
                } else {
                    DialogManager.getInstance().hideProgress();
                    if (e != null) {
                        try {
                            JSONObject errorJsonObj = new JSONObject(e.getMessage());
                            String messageStr = errorJsonObj.getString(getString(R.string.message));
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), PhotoHomeFragment.this);
                        } catch (Exception ex) {
                            DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), PhotoHomeFragment.this);
                            Log.e(AppConstants.TAG, ex.getMessage());
                        }
                    }
                }
            }
        });


    }


    private void checkForNewCustomers() {
        cancelNewCustomerTimer();
        mNewCustomerTimer = new Timer();
        mNewCustomerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getAccountBal();
                getPhotographerLocMarker();
            }
        }, 3000, 3000);
    }

    private void getPhotographerDetailsFromDB() {
        ParseQuery<ParseObject> currentPhotographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        currentPhotographerQuery.whereEqualTo(user, ParseUser.getCurrentUser());
        currentPhotographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    mPhotographerParseObj = object;
                    List<Integer> advertisedSkillList = object.getList(ParseAPIConstants.advertisedSkillLevel);
                    mPhotographerMinSkillInt = 0;
                    mPhotographerMaxSkillInt = 0;
                    if (advertisedSkillList != null && advertisedSkillList.size() > 0) {
                        if (object.getInt(ParseAPIConstants.maxSkillLevel) >= advertisedSkillList.get(0)) {
                            mPhotographerMinSkillInt = advertisedSkillList.get(0);
                        }
                        if (object.getInt(ParseAPIConstants.maxSkillLevel) >= advertisedSkillList.get(advertisedSkillList.size() - 1)) {
                            mPhotographerMaxSkillInt = advertisedSkillList.get(advertisedSkillList.size() - 1);
                        }
                    } else {
                        mPhotographerMaxSkillInt = object.getInt(ParseAPIConstants.maxSkillLevel);
                    }

                    ArrayList<Integer> advertisedSkillLevelStrArr = new ArrayList<>();
                    for (int i = mPhotographerMinSkillInt; i <= mPhotographerMaxSkillInt; i++) {
                        advertisedSkillLevelStrArr.add(i);
                    }
                    mPhotographerParseObj.put(ParseAPIConstants.advertisedSkillLevel, advertisedSkillLevelStrArr);
                    mPhotographerParseObj.saveEventually();

                    setPhotographerData(object.getInt(ParseAPIConstants.maxSkillLevel), mPhotographerMinSkillInt, mPhotographerMaxSkillInt);

                    mPhotographerMaxSkillInt = object.getInt(ParseAPIConstants.maxSkillLevel);
                    mAvaNotAvaLay.setTag(object.getBoolean(ParseAPIConstants.isAvailable) ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE);
                    setPhotographerAvaDetails();
                }
            }
        });


    }

    private void cancelNewCustomerTimer() {
        if (mNewCustomerTimer != null) {
            mNewCustomerTimer.cancel();
            mNewCustomerTimer.purge();
        }

    }

    private void createSnapMoment() {
        ParseUser user = ParseUser.getCurrentUser();
        user.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject userObject, ParseException e) {
                if (e == null && userObject != null) {
                    HashMap<String, Object> params = new HashMap<>();
                    ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_getStripePhotographerAccountDetails, params, new FunctionCallback<HashMap<String, String>>() {
                        @Override
                        public void done(HashMap parseCloudObject, ParseException e) {
                            if (e == null && parseCloudObject != null) {
                                boolean isAccVerified;

                                try {
                                    isAccVerified = parseCloudObject.get(ParseAPIConstants.transfers_enabled) != null && (boolean) parseCloudObject.get(ParseAPIConstants.transfers_enabled);

                                } catch (Exception ex) {
                                    isAccVerified = false;
                                    Log.e(getActivity().getClass().getSimpleName(), ex.getMessage());
                                }
                                if (isAccVerified) {
                                    final ParseObject momentObject = new ParseObject(ParseAPIConstants.Parse_Moment);
                                    final ParseGeoPoint currentLoc = getCurrentLatLong();
                                    momentObject.put(ParseAPIConstants.creator, userObject);
                                    momentObject.put(ParseAPIConstants.photographer, mPhotographerParseObj);
                                    momentObject.put(ParseAPIConstants.authorizedUsers, Collections.singletonList(ParseUser.getCurrentUser()));
                                    momentObject.put(location, currentLoc);
                                    momentObject.put(ParseAPIConstants.enabled, true);
                                    momentObject.put(ParseAPIConstants.momentDate, new Date());

                                    try {
                                        ParseGeoPoint parseGeoPoint = getCurrentLatLong();
                                        Geocoder geocoder = new Geocoder(getActivity(), Locale.US);
                                        List<Address> addresses = geocoder.getFromLocation(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude(), 1);
                                        String address = addresses.get(0).getAddressLine(0) != null ? addresses.get(0).getAddressLine(0) : "";
                                        String city = addresses.get(0).getLocality() != null ? addresses.get(0).getLocality() : "";
                                        String state = addresses.get(0).getAdminArea() != null ? addresses.get(0).getAdminArea() : "";
                                        String country = addresses.get(0).getCountryName() != null ? addresses.get(0).getCountryName() : "";

                                        momentObject.put(ParseAPIConstants.locationName, address);
                                        momentObject.put(ParseAPIConstants.locationCity, city);
                                        momentObject.put(ParseAPIConstants.locationState, state);
                                        momentObject.put(ParseAPIConstants.locationCountry, country);
                                    } catch (Exception ex) {
                                        Log.e(getActivity().getClass().getSimpleName(), ex.getMessage());
                                    }

                                    momentObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            DialogManager.getInstance().hideProgress();
                                            if (e == null) {
                                                DialogManager.getInstance().showCreatePhotoSessionPopup(getActivity(), momentObject.getString(ParseAPIConstants.adHocCode), new InterfaceBtnCallback() {
                                                    @Override
                                                    public void onOkClick() {
                                                        AppConstants.MOMENT_PHOTO_ENTITY = new MomentPhotoEntity();
                                                        AppConstants.MOMENT_PHOTO_ENTITY.setMoment(momentObject);

                                                        /*Set flag for right side image visible and it control from home screen - checkAndSetScreenStatus*/
                                                        AppConstants.MOMENT_UPLOAD_FROM_CHAT = AppConstants.FAILURE_CODE;
                                                        AppConstants.HEADER_RIGHT_CLICK = AppConstants.FAILURE_CODE;
                                                        AppConstants.SEND_BIRD_GROUP_CHANNEL = null;
                                                        AppConstants.PHOTOGRAPHER_AVA = mPhotographerParseObj.getBoolean(ParseAPIConstants.isAvailable);
                                                        ((HomeScreen) getActivity()).addFragment(new MomentUploadFragment());
                                                    }
                                                });

                                            } else {
                                                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), PhotoHomeFragment.this);

                                            }

                                        }
                                    });
                                } else {
                                    DialogManager.getInstance().hideProgress();
                                    DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.email_verify_topic), getString(R.string.email_verify_msg), PhotoHomeFragment.this);
                                }
                            } else {
                                DialogManager.getInstance().hideProgress();
                                if (e != null) {
                                    try {
                                        JSONObject errorJsonObj = new JSONObject(e.getMessage());
                                        String messageStr = errorJsonObj.getString(getString(R.string.message));
                                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), (messageStr != null && !messageStr.isEmpty() ? messageStr : e.getMessage()), PhotoHomeFragment.this);

                                    } catch (Exception ex) {
                                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), PhotoHomeFragment.this);
                                        Log.e(AppConstants.TAG, ex.getMessage());
                                    }
                                }
                            }
                        }
                    });

                } else {
                    DialogManager.getInstance().hideProgress();
                    if (e != null) {
                        DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.app_name), e.getMessage(), PhotoHomeFragment.this);
                    }
                }

            }
        });


    }

    /*Permission for access location*/
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

    private void getAccountBal() {
        getParseConfigFromParseDB();
        HashMap<String, Object> params = new HashMap<>();
        ParseCloud.callFunctionInBackground(ParseAPIConstants.PC_getStripePhotographerAccountBalance, params, new FunctionCallback<HashMap<String, String>>() {
            public void done(HashMap resObject, ParseException e) {
                if (getActivity() != null && getActivity() != null && ((HomeScreen) getActivity()).mFragment != null && ((HomeScreen) getActivity()).mFragment instanceof PhotoHomeFragment && e == null && resObject != null) {
                    try {
                        ArrayList<HashMap<String, String>> bankAccAvailableDetailsRes = (ArrayList<HashMap<String, String>>) resObject.get(ParseAPIConstants.available);
                        ArrayList<HashMap<String, String>> bankAccPendingDetailsRes = (ArrayList) resObject.get(ParseAPIConstants.pending);
                        String availableAmtStr = AppConstants.FAILURE_CODE, pendingAmtStr = AppConstants.FAILURE_CODE;
                        if (getActivity() != null && bankAccAvailableDetailsRes.size() > 0) {
                            availableAmtStr = String.valueOf(bankAccAvailableDetailsRes.get(0).get(ParseAPIConstants.amount));
                        }
                        if (getActivity() != null && bankAccPendingDetailsRes.size() > 0) {
                            pendingAmtStr = String.valueOf(bankAccPendingDetailsRes.get(0).get(ParseAPIConstants.amount));
                        }
                        String totalCostStr = NumberUtil.afterTwoPointVal(String.valueOf((Double.valueOf(NumberUtil.afterTwoPointVal(availableAmtStr)) / 100d) + (Double.valueOf(NumberUtil.afterTwoPointVal(pendingAmtStr)) / 100d)));
                        AppConstants.PHOTOGRAPHER_TOTAL_BAL_AMT = totalCostStr;
                        ((HomeScreen) getActivity()).setHeaderRightText(getResources().getString(R.string.dollar_sym) + "" + NumberUtil.afterTwoPointVal(totalCostStr));
                    } catch (Exception ex) {
                        Log.e(AppConstants.TAG, ex.getMessage());
                    }
                }
            }
        });
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

    /*Get amount Configuration*/
    private void getParseConfigFromParseDB() {
        ParseConfig.getInBackground(new ConfigCallback() {
            @Override
            public void done(ParseConfig config, ParseException e) {
                if (e == null && config != null && config.get(ParseAPIConstants.MaxRequestRadius) != null) {
                    try {
                        mMaxRequestRadiusInt = Integer.valueOf(config.getNumber(ParseAPIConstants.MaxRequestRadius) + "");
                    } catch (Exception ex) {
                        Log.e(AppConstants.TAG, ex.toString());
                    }
                }
            }
        });

    }
}
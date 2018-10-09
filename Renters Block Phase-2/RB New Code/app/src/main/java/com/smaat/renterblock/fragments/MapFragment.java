package com.smaat.renterblock.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapters.MapSavedSearchAdapter;
import com.smaat.renterblock.adapters.PropertyDetailAdapter;
import com.smaat.renterblock.entity.AgentDetailsResultEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.entity.MapFragmentResponse;
import com.smaat.renterblock.entity.MarkerID;
import com.smaat.renterblock.entity.PlaceBufferResponse;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyReviewCommentEntity;
import com.smaat.renterblock.entity.UserDetailsEntity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.AddressResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.PlaceDescription;
import com.smaat.renterblock.model.PlacePredictionResponse;
import com.smaat.renterblock.model.SavedSearchResponse;
import com.smaat.renterblock.model.SettingResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.ui.HomeScreen;
import com.smaat.renterblock.ui.LoginScreen;
import com.smaat.renterblock.utils.AddressUtil;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.ComparatorUtil;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.ImageUtil;
import com.smaat.renterblock.utils.InterfaceAPICommonCallback;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.NetworkUtil;
import com.smaat.renterblock.utils.NumberUtil;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.renterblock.utils.NumberUtil.calculationByDistance;


public class MapFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener
        , GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, MapSavedSearchAdapter.SearchParamsInterface {

    @BindView(R.id.arc_slide_image_lay)
    RelativeLayout mArcSlideLay;

    @BindView(R.id.arc_slide_image)
    ImageView mArcSlideImg;

    @BindView(R.id.list_txt)
    TextView mListTxt;

    @BindView(R.id.layers_txt)
    TextView mLayersTxt;

    @BindView(R.id.show_current_location_btn)
    Button mCurrentLocationBtn;

    @BindView(R.id.draw_btn)
    Button mDrawBtn;

    @BindView(R.id.saved_search_list)
    RecyclerView mSavedSearchRecyclerView;

    @BindView(R.id.property_detail_recycle_view)
    RecyclerView mPropertyDetailRecyclerView;

    @BindView(R.id.property_detail_single_list_view)
    RecyclerView mPropertySingleDetailRecyclerView;

    @BindView(R.id.map_lay)
    RelativeLayout mMapRelativeLay;

    @BindView(R.id.ad_view_agent_lay)
    RelativeLayout mAgentAdViewLay;

    @BindView(R.id.agent_profile_img)
    ImageView mAgentProfileImg;

    @BindView(R.id.agent_user_name_txt)
    TextView mAgentUserNameTxt;

    @BindView(R.id.agent_first_name_txt)
    TextView mAgentFirstNameTxt;

    private Paint mPaint;
    private double lat, lon;
    private DrawingView mDrawingViews;

    private boolean mIsTouchEnableBool = false;

    @BindView(R.id.bottom_lay)
    LinearLayout mBottomViewLay;

    private PropertyEntity mPropertyEntity;

    private ArrayList<PropertyEntity> mPropertyArrListRes = new ArrayList<>();
    private ArrayList<LatLng> mLatLangList = new ArrayList<LatLng>();
    private GoogleMap mGoogleMap;
    LatLngBounds mBounds;

    public double latitude;
    public double longitude;
    private GoogleApiClient mGoogleApiClient;
    private double mSavedLatitudeDouble = 0.0, mSavedLongitudeDouble = 0.0;
    private boolean isFirstTimeLatLngBool = false;
    private float MAP_DISTANCE = 0;
    private final int REQUEST_CHECK_SETTINGS = 300;
    private String mSearchLocationStr = "";
    private String searchArea = "";
    private ArrayList<LocalSavedSearchEntity> mLocalSavedSearchList = new ArrayList<>();

    private PropertyDetailAdapter mPropertyDetailAdapter = null;

    private Handler mMapMoveHandler, mAgentAdHandler;
    private Runnable mMapMoveRunnable;
    private boolean isMapMoveBool = false, isMarkerClickBool = false, isPlaceEdtBool = false, isHandDrawBool = false, isMapApiCallBool = false, isSearchEdtVisible = false;
    private Marker mPrevMarker;
    private int source = 0, mPrevPosInt = -1, mAgentAdPosInt = 0;
    private Dialog mSaveSearchDialog;
    private boolean isSavedSearchScreenCallBool = false;

    private Timer mAgentAdTimer = new Timer();
    private Runnable mAgentAdRunnable;
    private InterstitialAd mInterstitialAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_map, container, false);
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this, rootView);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(rootView);


        // Log.d("PUSH_TOKEN", "" + FirebaseInstanceId.getInstance().getToken());

        /*For focus current fragment*/
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    default:
                        v.performClick();
                }
                return true;
            }
        });


        return rootView;
    }


    /*Fragment manual onResume*/
    @Override
    public void onFragmentResume() {

         /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set gone*/
        if (getActivity() != null) {
            mSavedLatitudeDouble = 0.0;
            mSavedLongitudeDouble = 0.0;
            isMarkerClickBool = false;

             /*Slide menu action*/
            ((HomeScreen) getActivity()).setDrawerAction(AppConstants.MAP_CURRENT_BACK_FRAGMENT instanceof MapFragment);

            /*set header first  img*/
            ((HomeScreen) getActivity()).setHeaderLeftFirstImgLayVisible(1);
            ((HomeScreen) getActivity()).setHeaderRightFirstImgLayVisible(R.drawable.search_icon, 1);

            /*set header second img*/
            ((HomeScreen) getActivity()).setHeaderLeftSecondImgLayVisible(R.mipmap.app_icon, 2);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 1, "");

            /*set header third txt*/
            ((HomeScreen) getActivity()).setHeaderLeftThirdTxtLayVisible(getString(R.string.app_name), 0);
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.app_name), 0);

//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //Do something after 100ms
//                                /*set header txt*/
            ((HomeScreen) getActivity()).setHeaderTxt(getString(R.string.settings), 0);
            ((HomeScreen) getActivity()).setHeaderEdt(mSearchLocationStr.isEmpty() ? getActivity().getString(R.string.enter_your_city)
                    : mSearchLocationStr, 1);
//                }
//            }, 100);




            /* interstitial Ad part */
            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("deviceid").build();
            mInterstitialAd.loadAd(adRequest);


            initView();
        }
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

    /*InitViews*/
    private void initView() {

        AppConstants.TAG = this.getClass().getSimpleName();
        setDrawingView();
//        SavedSearchList mSavedSearchRes = PreferenceUtil.getSavedSearchList(getActivity());
//        mLocalSavedSearchList = mSavedSearchRes.getLocalSavedSearchEntityArrayList();

        mLocalSavedSearchList = PreferenceUtil.getUserDetailsRes(getActivity()).getSavedsearch();

        if (AppConstants.IS_FROM_LIST_ITEM_CLICK) {

            setListClickAdapter();
            AppConstants.IS_FROM_LIST_ITEM_CLICK = false;

        } else {


            initGoogleAPIClient();


            SupportMapFragment fragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);

        /* Map synchronization */
            fragment.getMapAsync(this);

        /*setting String to header*/
            mSearchLocationStr = getActivity().getString(R.string.enter_your_city);

            mMapRelativeLay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    isPlaceEdtBool = false;
                    return false;
                }
            });

            ((HomeScreen) getActivity()).mHeaderRightSecondImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeScreen) getActivity()).addFragment(new TabFilterFragment());
                }
            });
            ((HomeScreen) getActivity()).mHeaderRightThirdTxtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((HomeScreen) getActivity()).mHeaderEdt.setText("");
                    isPlaceEdtBool = false;
                    isSearchEdtVisible = false;
                    hideSoftKeyboard();
                    mSavedSearchRecyclerView.setVisibility(View.GONE);
                    ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 1, "");
                    ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.cancel), 0);
                }
            });

            ((HomeScreen) getActivity()).mHeaderEdt.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    isPlaceEdtBool = true;
                    isMapApiCallBool = false;
                    // saved_search_frame_view.startAnimation(mFromBottom);
                    ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 0, "");
                    ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.cancel), 1);
               /*Call SAVED search location in ListView*/
                    isSearchEdtVisible = true;
                    setPlaceSearchAdapter(mLocalSavedSearchList, true);
                }
            });

            ((HomeScreen) getActivity()).mHeaderEdt.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!isMapApiCallBool) {
                        searchArea = s.toString().trim();
                        isPlaceEdtBool = true;
                /*Calling Google API client to get place suggestion*/
                        if (!searchArea.isEmpty()) {
                            APIRequestHandler.getInstance().callPlaceSuggestionAPI(s.toString(), MapFragment.this);
                        } else {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handler.removeCallbacks(this);
                                    setPlaceSearchAdapter(mLocalSavedSearchList, true);
                                }
                            }, 1000);


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
            ((HomeScreen) getActivity()).mHeaderEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        isPlaceEdtBool = true;
                        searchArea = ((HomeScreen) getActivity()).mHeaderEdt.getText().toString().trim();
                        if (searchArea.isEmpty()) {
                            DialogManager.getInstance().showToast(getActivity(), getString(R.string.err_empty_location));
                        } else {
                            LatLng mLatLong = AddressUtil.getLocationFromAddress(getActivity(), searchArea);
                            if (mLatLong != null) {
                                AppConstants.saved_search_Latitude = String.valueOf(mLatLong.latitude);
                                AppConstants.saved_search_Longitude = String.valueOf(mLatLong.longitude);
                                moveMapCamera(String.valueOf(mLatLong.latitude), String.valueOf(mLatLong.longitude));
                                //callAPI(String.valueOf(mLatLong.latitude), String.valueOf(mLatLong.longitude));
//                            callPropertyListAPI(AppConstants.TYPE_OF_FILTER,
//                                    String.valueOf(mLatLong.latitude), String.valueOf(mLatLong.longitude));
                            } else {
                                DialogManager.getInstance().showToast(getActivity(), getString(R.string.not_valid_location));
                            }
                        }
                        hideSoftKeyboard();
                        ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 1, "");
                        ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.cancel), 0);

                    }
                    return false;
                }
            });
//        mSavedSearchRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                ((HomeScreen) getActivity()).mHeaderEdt.setText(mMapSavedAdapterList.get(position).getLocation());
//                /*Setting header with filter icon*/
//                mSavedSearchListView.setVisibility(View.GONE);
//                ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 1);
//                ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.cancel), 0);
//
//            }
//        });

            setPlaceSearchAdapter(mLocalSavedSearchList, true);
        }

    }

    private void setListClickAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isListVisibleBool = mPropertyDetailRecyclerView.getVisibility() == View.VISIBLE;
                mLayersTxt.setText(isListVisibleBool ? getString(R.string.layers) : getString(R.string.sort));
                mListTxt.setText(isListVisibleBool ? getString(R.string.list) : getString(R.string.map));
                mPropertyDetailRecyclerView.setVisibility(isListVisibleBool ? View.GONE : View.VISIBLE);
                mPropertyDetailRecyclerView.startAnimation(isListVisibleBool ? mAnimInTop : mAnimOutBottom);
                AppConstants.IS_FROM_LIST_CLICK = true;
                setPropertyDetailAdapter(mPropertyArrListRes);
            }
        });
    }

    /*Click Event*/
    @OnClick({R.id.layers_txt, R.id.save_search_txt, R.id.list_txt, R.id.arc_slide_image_lay, R.id.draw_btn, R.id.show_current_location_btn, R.id.ad_view_agent_lay})
    public void onClick(View v) {
        if (getActivity() != null) {
            switch (v.getId()) {

                case R.id.ad_view_agent_lay:
                    ((HomeScreen) getActivity()).addFragment(new FindAgentDetailsFragment());
                    break;
                case R.id.layers_txt:
                    if (mLayersTxt.getText().toString().trim().equals(getString(R.string.layers))) {
                        DialogManager.getInstance().showMapLayersDialog(getActivity(), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                                mGoogleMap.setMapType(Integer.valueOf(PreferenceUtil.getStringValue(getActivity(), AppConstants.MAP_TYPE)));
                            }
                        });
                    } else {
                        DialogManager.getInstance().sortingDialog(getActivity(), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                                setPropertyDetailAdapter(mPropertyArrListRes);
                            }
                        });
                    }
                    break;
                case R.id.list_txt:
                    setListClickAdapter();
                    break;
                case R.id.save_search_txt:
                    if (PreferenceUtil.getBoolPreferenceValue(getActivity(), AppConstants.LOGIN_STATUS)) {

                        mSaveSearchDialog = DialogManager.getInstance().saveSearchDialog(getActivity(),
                                ((HomeScreen) getActivity()).mHeaderEdt.getText().toString(), new InterfaceEdtWithBtnCallback() {

                                    @Override
                                    public void onFirstEdtBtnClick(String firstEdtStr) {

                                        String addressURLStr = String.format(AppConstants.GET_DETAILS_ADDRESS_URL, firstEdtStr);
                                        final String placeName = firstEdtStr;

                                        APIRequestHandler.getInstance().callGetUserAddressAPICallback(getActivity(), addressURLStr, new InterfaceAPICommonCallback() {
                                            @Override
                                            public void onRequestSuccess(Object obj) {
                                                AddressResponse userAddressRes = (AddressResponse) obj;

                                                FilterEntity filterEntity = AppConstants.TYPE_OF_FILTER.equals(AppConstants.RENT) ?
                                                        PreferenceUtil.getRentFilterValues(getActivity()) : PreferenceUtil.getSaleFilterValues(getActivity());


                                /*assign filter values to avoid null*/
                                                filterEntity.setMLS(filterEntity.getMLS());
                                                filterEntity.setNew_construction(filterEntity.getNew_construction());
                                                filterEntity.setPrice_range_min(filterEntity.getPrice_range_min());
                                                filterEntity.setPrice_range_max(filterEntity.getPrice_range_max());
                                                filterEntity.setKeywords(filterEntity.getKeywords());
                                                filterEntity.setBaths(filterEntity.getBaths());
                                                filterEntity.setBeds(filterEntity.getBeds());
                                                filterEntity.setDays_on_RB(filterEntity.getDays_on_RB());
                                                filterEntity.setResale(filterEntity.getResale());
                                                filterEntity.setReduced_prices(filterEntity.getReduced_prices());
                                                filterEntity.setFore_closure(filterEntity.getFore_closure());
                                                filterEntity.setSquare_footage_min(filterEntity.getSquare_footage_min());
                                                filterEntity.setSquare_footage_max(filterEntity.getSquare_footage_max());
                                                filterEntity.setYear_build_max(filterEntity.getYear_build_max());
                                                filterEntity.setYear_build_min(filterEntity.getYear_build_min());
                                                filterEntity.setNo_fee(filterEntity.getNo_fee());
                                                filterEntity.setSold_within(filterEntity.getSold_within());
                                                filterEntity.setOpen_house(filterEntity.getOpen_house());
                                                filterEntity.setLot_size(filterEntity.getLot_size());
                                                filterEntity.setDistance(filterEntity.getDistance());
                                /*assigning values on current saving search details*/

                                                filterEntity.setFilter_name(placeName);
                                                filterEntity.setProperty_type(getString(R.string.rent));
                                                if (userAddressRes.getResults().size() > 0) {
                                                    filterEntity.setLatitude(String.valueOf(userAddressRes.getResults().get(0).getGeometry().getLocation().getLat()));
                                                    filterEntity.setLongitude(String.valueOf(userAddressRes.getResults().get(0).getGeometry().getLocation().getLng()));
                                                }
                                                String filterType = AppConstants.TYPE_OF_FILTER;
                                                String filterObj = new Gson().toJson(filterEntity);


                                                APIRequestHandler.getInstance().saveSearchAPICall(MapFragment.this, filterObj, filterType);
                                            }

                                            @Override
                                            public void onRequestFailure(Throwable r) {
                                                DialogManager.getInstance().showToast(getActivity(), r.getMessage());

                                            }
                                        });


                                    }
                                });
                    } else {
                        nextScreen(LoginScreen.class, false);
                    }
                    break;
                case R.id.arc_slide_image_lay:

                    DialogManager.getInstance().callArcPopUp(getContext());

                    break;
                case R.id.show_current_location_btn:
                    setCurrentLocation();
                    break;
                case R.id.draw_btn:
                    isHandDrawBool = !isHandDrawBool;
                    mDrawBtn.setBackgroundResource(isHandDrawBool ? R.drawable.finger_draw_disable : R.drawable.finger_draw_disable1);
                    if (isHandDrawBool) {
                        mIsTouchEnableBool = !mIsTouchEnableBool;
                        if (mIsTouchEnableBool) {
                            mGoogleMap.clear();
                            mDrawingViews = new DrawingView(getContext());
                            mMapRelativeLay.addView(mDrawingViews, 0);
                        }

                    } else {
                        mGoogleMap.clear();
                        VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
                        double lat = vr.latLngBounds.getCenter().latitude;
                        double longitude = vr.latLngBounds.getCenter().longitude;
                        callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, String.valueOf(lat), String.valueOf(longitude));

                    }


                    break;

            }
        }
    }

    private void setPropertyDetailAdapter(ArrayList<PropertyEntity> propertyArrListRes) {

        if (getActivity() != null) {
            String sortStr = PreferenceUtil.getStringValue(getActivity(), AppConstants.PROPERTY_SORT_TYPE);

            if (sortStr.isEmpty()) {
                PreferenceUtil.storeStringValue(getActivity(), AppConstants.PROPERTY_SORT_TYPE, AppConstants.SORT_BEST);
                sortStr = PreferenceUtil.getStringValue(getActivity(), AppConstants.PROPERTY_SORT_TYPE);
            }

            if (propertyArrListRes.size() > 0) {
                if (sortStr.equals(AppConstants.SORT_LATEST)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.DATE_SORT);
                } else if (sortStr.equals(AppConstants.SORT_RATING)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.RATING_SORT);
                } else if (sortStr.equals(AppConstants.SORT_PRICE_LOW)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.PRICE_MIN_TO_MAX_SORT);
                } else if (sortStr.equals(AppConstants.SORT_PRICE_HIGH)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.PRICE_MAX_TO_MIN_SORT);
                } else if (sortStr.equals(AppConstants.SORT_BATH)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.BATH_SORT);
                } else if (sortStr.equals(AppConstants.SORT_BED)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.BED_SORT);
                } else if (sortStr.equals(AppConstants.SORT_SQRT)) {
                    Collections.sort(propertyArrListRes, ComparatorUtil.SQUARE_FEET_SORT);
                }

            }

            mPropertyDetailAdapter = new PropertyDetailAdapter(getActivity(), propertyArrListRes, new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    showInterstitialAd();
                }
            });
            mPropertyDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mPropertyDetailRecyclerView.setAdapter(mPropertyDetailAdapter);
        }
    }


    /* API Call to get Property*/
    private void callPropertyListAPI(String PropertyType, String latStr, final String langStr) {
        String filterType = "";
        if (AppConstants.TYPE_OF_FILTER.equals(AppConstants.RENT)) {
            if (AppConstants.saved_search_json.equalsIgnoreCase("")) {
                filterType = "{\"Rent\" :" + new Gson().toJson(PreferenceUtil.getRentFilterValues(getActivity())) + "}";
            } else {
                filterType = "{\"Rent\" :" + AppConstants.saved_search_json + "}";
            }
        } else if (AppConstants.TYPE_OF_FILTER.equals(AppConstants.SALE)) {
            if (AppConstants.saved_search_json.equalsIgnoreCase("")) {
                filterType = "{\"Sale\" :" + new Gson().toJson(PreferenceUtil.getSaleFilterValues(getActivity())) + "}";
            } else {
                filterType = "{\"Sale\" :" + AppConstants.saved_search_json + "}";
            }
        }


        APIRequestHandler.getInstance().propertyListAPICall(filterType, PropertyType, latStr, langStr, String.valueOf(MAP_DISTANCE), AppConstants.SEARCH_RESULT_COUNT, AppConstants.FAILURE_CODE, MapFragment.this);

    }


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if ((resObj instanceof MapFragmentResponse) && !isMarkerClickBool) {
            if (isSavedSearchScreenCallBool) {
                AppConstants.saved_search_Latitude = "0.0";
                AppConstants.saved_search_Longitude = "0.0";
                AppConstants.saved_search_json = "";
                AppConstants.saved_location_name = "";
                isSavedSearchScreenCallBool = false;
            }

            MapFragmentResponse mRes = (MapFragmentResponse) resObj;
            mSavedSearchRecyclerView.setVisibility(View.GONE);

            mAgentAdPosInt = 0;

            if (mRes.getResult().size() > 0) {

                setMapMarker(mGoogleMap, mRes.getResult());


               /*Agent ad */
                if (!mRes.getAgent().getTotal_records().isEmpty() && mRes.getAgent().getResult().size() > 0) {
                    startAgentAdTimer(mRes.getAgent().getResult());
                } else {
                    stopAgentAdTimer();
                }

            } else {
                if (!isHandDrawBool) {
                    mGoogleMap.clear();
                }
                mPropertyArrListRes.clear();
            }
        } else if (resObj instanceof PlacePredictionResponse) {

            PlacePredictionResponse mResponse = (PlacePredictionResponse) resObj;

            ArrayList<PlaceDescription> mPlaceList = mResponse.getPredictions();

            ArrayList<LocalSavedSearchEntity> mSuggestionList = new ArrayList<>();
            if (mPlaceList.size() > 0) {
                for (int i = 0; i < mPlaceList.size(); i++) {
                    LocalSavedSearchEntity entity = new LocalSavedSearchEntity();
                    entity.setLocation(mPlaceList.get(i).getDescription());
                    entity.setLatitude(mPlaceList.get(i).getLatitude());
                    entity.setLatitude(mPlaceList.get(i).getLongitude());
                    entity.setPlaceID(mPlaceList.get(i).getPlace_id());
                    mSuggestionList.add(entity);
                }
                setPlaceSearchAdapter(mSuggestionList, false);
            }


        } else if (resObj instanceof PlaceBufferResponse) {

            PlaceBufferResponse mResponse = (PlaceBufferResponse) resObj;
            mSavedSearchRecyclerView.setVisibility(View.GONE);
            setPlaceNameEdt(mResponse.getResult().getAddress_components());

            moveMapCamera(mResponse.getResult().getGeometry().getLocation().getLat(),
                    mResponse.getResult().getGeometry().getLocation().getLng());

            //Distance
            MAP_DISTANCE = 50;


            callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, mResponse.getResult().getGeometry().getLocation().getLat(),
                    mResponse.getResult().getGeometry().getLocation().getLng());

        } else if (resObj instanceof SettingResponse) {

            mSaveSearchDialog.dismiss();

            SettingResponse mSaveSearchResponse = (SettingResponse) resObj;
            if (mSaveSearchResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                DialogManager.getInstance().showAlertPopup(getActivity(), mSaveSearchResponse.getMsg(), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        APIRequestHandler.getInstance().savedSearchAPICall(MapFragment.this);

                    }
                });

            }
        } else if (resObj instanceof SavedSearchResponse) {
            SavedSearchResponse mSavedSearchResponse = (SavedSearchResponse) resObj;
            if (mSavedSearchResponse.getError_code().equals(AppConstants.SUCCESS_CODE)) {
                UserDetailsEntity userDetailsEntity = PreferenceUtil.getUserDetailsRes(getActivity());
                userDetailsEntity.setSavedsearch(mSavedSearchResponse.getResult());
                PreferenceUtil.storeUserDetails(getActivity(), userDetailsEntity);

            }

        }


    }


    private void setPlaceSearchAdapter(ArrayList<LocalSavedSearchEntity> mList, boolean isSavedLocation) {
        if (getActivity() != null) {
            if (mList.size() > 0) {
                if (!isSearchEdtVisible) {
                    mSavedSearchRecyclerView.setVisibility(View.GONE);
                } else {
                    mSavedSearchRecyclerView.setVisibility(View.VISIBLE);
                }

                if (isSavedLocation) {
                    Location location = getCurrentLatLong();
                    FilterEntity filterEntity = new FilterEntity();
                    filterEntity.setLatitude(String.valueOf(location.getLatitude()));
                    filterEntity.setLongitude(String.valueOf(location.getLongitude()));
                    filterEntity.setFilter_name("Current Location");
                    LocalSavedSearchEntity m = new LocalSavedSearchEntity();
                    m.setFilter_object(new Gson().toJson(filterEntity));
                    ArrayList<LocalSavedSearchEntity> currentLocList = new ArrayList<>();
                    currentLocList.add(m);
                    currentLocList.addAll(mList);

                    MapSavedSearchAdapter mAdapter = new MapSavedSearchAdapter(currentLocList, getActivity(), isSavedLocation, this);
                    mSavedSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mSavedSearchRecyclerView.setAdapter(mAdapter);

                } else {
                    MapSavedSearchAdapter mAdapter = new MapSavedSearchAdapter(mList, getActivity(), isSavedLocation, this);
                    mSavedSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mSavedSearchRecyclerView.setAdapter(mAdapter);
                }

            } else {
                mSavedSearchRecyclerView.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
        isPlaceEdtBool = false;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsAccessLocation(2);
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                isMapMoveBool = true;
            }
        });

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                // Cleaning all the markers.

                if (isMapMoveBool && !isMarkerClickBool && !isPlaceEdtBool && !isHandDrawBool) {
                    isMapMoveBool = false;
                    mapMoveStopped();
                }

            }
        });


    }


    private void mapMoveStopped() {
        mMapMoveRunnable = new Runnable() {
            @Override
            public void run() {
                {
                    if (getActivity() != null && !isMapMoveBool) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String areaName = "";
                                removeMapHandler();

                                VisibleRegion vr = mGoogleMap.getProjection().getVisibleRegion();
                                double latDouble = vr.latLngBounds.getCenter().latitude;
                                double longitudeDouble = vr.latLngBounds.getCenter().longitude;


                                if (NumberUtil.calculationByDistance(mSavedLatitudeDouble, mSavedLongitudeDouble, latDouble, longitudeDouble) > 5) {

                                    mSavedLatitudeDouble = vr.latLngBounds.getCenter().latitude;
                                    mSavedLongitudeDouble = vr.latLngBounds.getCenter().longitude;
                                    if (AppConstants.saved_search_Latitude.equals("0.0")) {

                                        if (!isFirstTimeLatLngBool) {
                                            isFirstTimeLatLngBool = true;
                                            getScreenCoordinates(0.0, 0.0);
                                        } else {
                                            getScreenCoordinates(latDouble, longitudeDouble);
                                        }
                                        areaName = AddressUtil.getAddressFromLatLng(latDouble, longitudeDouble, getActivity());


                                    } else {
                                        getScreenCoordinates(Double.valueOf(AppConstants.saved_search_Latitude),
                                                Double.valueOf(AppConstants.saved_search_Longitude));
                                        areaName = AppConstants.saved_location_name;
                                    }

                                    isMapApiCallBool = true;

                                    if (!areaName.isEmpty()) {
                                        ((HomeScreen) getActivity()).mHeaderEdt.setText(areaName);
                                        ((HomeScreen) getActivity()).mHeaderEdt.setSelection(((HomeScreen) getActivity()).mHeaderEdt.getText().length());
                                    }


                                    if (AppConstants.saved_search_Latitude.equals("0.0")) {
                                        callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, String.valueOf(latDouble), String.valueOf(longitudeDouble));
                                    } else {
                                        isSavedSearchScreenCallBool = true;
                                        callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, AppConstants.saved_search_Latitude, AppConstants.saved_search_Longitude);
                                    }

                                }

                            }
                        });
                    } else {
                        removeMapHandler();
                    }
                }
            }
        };

        /*set handler to wait for 1 seconds */
        mMapMoveHandler = new Handler();
        mMapMoveHandler.postDelayed(mMapMoveRunnable, 500);

    }

    private void removeMapHandler() {
        if (mMapMoveHandler != null) {
            mMapMoveHandler.removeCallbacks(mMapMoveRunnable);
        }
    }


    Runnable mAgentAdRunnableGone = new Runnable() {

        @Override
        public void run() {
            mAgentAdViewLay.setVisibility(View.GONE);
            mAgentAdHandler.removeCallbacks(mAgentAdRunnableGone);
        }
    };
    Runnable mAgentAdRunnableVisible = new Runnable() {

        @Override
        public void run() {
            mAgentAdViewLay.setVisibility(View.VISIBLE);
            mAgentAdHandler.removeCallbacks(mAgentAdRunnableVisible);
        }
    };


    /*Init Google API clients*/
    private void initGoogleAPIClient() {
        if (getActivity() != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(MapFragment.this)
                    .addOnConnectionFailedListener(MapFragment.this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }


    private void getScreenCoordinates(double lat, double longitude) {
        if (getActivity() != null) {
            Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
            Point mdispSize = new Point();
            mdisp.getSize(mdispSize);
            int maxY = mdispSize.y;

            Point x_y_points = new Point(0, maxY / 2);

            LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(x_y_points);
            double latitudes = latLng.latitude;

            double longitudes = latLng.longitude;

            MAP_DISTANCE = calculationByDistance(lat, longitude, latitudes, longitudes);
        }
    }


    private void setMapMarker(final GoogleMap googleMap, final ArrayList<PropertyEntity> propertyMarkerArrayListRes) {
        if (getActivity() != null && propertyMarkerArrayListRes.size() > 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isHandDrawBool) {
                        googleMap.clear();
                    }
                    String amountStr, amountRangeStr, propertyType = "";

                    TextView numTxt;
                    ImageView propTypeImg;
                    mPropertyArrListRes = new ArrayList<>();
                    mPropertyArrListRes.clear();
                    for (int i = 0; i < propertyMarkerArrayListRes.size(); i++) {


                        if ((!propertyMarkerArrayListRes.get(i).getLatitude().isEmpty() || !propertyMarkerArrayListRes.get(i).getLatitude().equals(AppConstants.FAILURE_CODE)) && (!propertyMarkerArrayListRes.get(i).getLongitude().isEmpty() ||
                                !propertyMarkerArrayListRes.get(i).getLongitude().equals(AppConstants.FAILURE_CODE))) {

                            LatLng markerLatLngPoint = new LatLng(Double.valueOf(propertyMarkerArrayListRes.get(i).getLatitude()), Double.valueOf(propertyMarkerArrayListRes.get(i).getLongitude()));
                            if (!isHandDrawBool || (isHandDrawBool && mBounds.contains(markerLatLngPoint))) {

                                final View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.map_marker_blue, null, false);
                                numTxt = markerView.findViewById(R.id.num_txt);
                                propTypeImg = markerView.findViewById(R.id.property_type_img);
                                MarkerID markerId = new MarkerID();
                                try {
                                    if (propertyMarkerArrayListRes.get(i).getPrice_range().equals("")) {
                                        numTxt.setText(getString(R.string.na));
                                    } else {
                                        amountRangeStr = propertyMarkerArrayListRes.get(i).getPrice_range();
                                        amountStr = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amountRangeStr));
                                        if (AppConstants.TYPE_OF_PROPERTY.equals(AppConstants.RENT)) {
                                            Integer validate = Integer.parseInt(propertyMarkerArrayListRes.get(i).getPrice_range());
                                            if (validate <= 2) {
                                                numTxt.setText(getString(R.string.dollor) + "" + amountRangeStr);
                                                markerId.setMarkername(getString(R.string.dollor) + amountRangeStr);
                                            } else {
                                                numTxt.setText(getString(R.string.dollor) + "" + amountStr);
                                                markerId.setMarkername(getString(R.string.dollor) + amountStr);

                                            }
                                        } else {
                                            float amount_conversion = (Integer.parseInt(propertyMarkerArrayListRes.get(i).getPrice_range()) / 1000.0f);
                                            double round_off = Math.floor(amount_conversion * 10) / 10;
                                            numTxt.setText(getString(R.string.dollor) + round_off + getString(R.string.k));
                                            markerId.setMarkername(getString(R.string.dollor) + round_off + getString(R.string.k));

                                        }
                                    }
                                } catch (Exception e) {
                                    Log.d(AppConstants.TAG, e.toString());
                                }

                                propTypeImg.setVisibility(View.GONE);
                                //property marker image check
//                                if (propertyMarkerArrayListRes.get(i).getRb_block().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                                    propTypeImg.setVisibility(View.VISIBLE);
//                                    propTypeImg.setImageResource(R.drawable.exclusives_icon);
//                                    propertyType = "exclusive";
//
//                                } else if (propertyMarkerArrayListRes.get(i).getOpen_house().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                                    propTypeImg.setVisibility(View.VISIBLE);
//                                    propTypeImg.setImageResource(R.drawable.open_houses_icon);
//                                    propertyType = "openhouse";
//
//                                } else if (propertyMarkerArrayListRes.get(i).getIsfavourite().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                                    propTypeImg.setVisibility(View.VISIBLE);
//                                    propTypeImg.setImageResource(R.drawable.favourite_disable);
//                                    propertyType = "favourite";
//                                }
//                                markerId.setMarkerpropType(propertyType);

                                if (propertyMarkerArrayListRes.get(i).getProperty_api().
                                        equalsIgnoreCase("RB")) {
                                    propTypeImg.setVisibility(View.VISIBLE);
                                    propTypeImg.setImageResource(R.drawable.exclusives_icon);
                                    propertyType = "RB";
                                } else {
                                    propTypeImg.setVisibility(View.INVISIBLE);
                                    propertyType = "";
                                }
                                markerId.setMarkerpropType(propertyType);


                                MarkerOptions markerOptions = new MarkerOptions().position(markerLatLngPoint);
                                Marker marker1 = googleMap.addMarker(new MarkerOptions().position(markerOptions.getPosition())
                                        .icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.loadBitmapFromView(markerView))));


                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.loadBitmapFromView(markerView)));
                                googleMap.addMarker(markerOptions);
                                markerId.setMarkerid(marker1.getId());
                                propertyMarkerArrayListRes.get(i).setMarker(marker1);
                                propertyMarkerArrayListRes.get(i).setMarkers(markerId);
                                marker1.setTag(propertyMarkerArrayListRes.get(i));

                                mPropertyArrListRes.add(propertyMarkerArrayListRes.get(i));
                            }
                        }
//
                    }

                    setPropertyDetailAdapter(mPropertyArrListRes);
                    setSingleItemPropertyAdapter(mPropertyArrListRes);
                }
            });


        }
    }


    /* Location update */
    private void startLocationUpdate() {
        if (getActivity() != null) {
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MapFragment.this);
        }
    }

    /*Set current location to map view*/
    private void setCurrentLocation() {
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsAccessLocation(4);
                return;
            }
            if (mGoogleMap != null) {
      /* Enable current location */
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                Location currentLoc = getCurrentLatLong();
                LatLng coordinate = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
                hideSoftKeyboard();
                if (!AppConstants.saved_search_Latitude.equals("0.0")) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(AppConstants.saved_search_Latitude), Double.valueOf(AppConstants.saved_search_Longitude)), 17));
                } else {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));
                }
                // callAPI(String.valueOf(currentLoc.getLatitude()), String.valueOf(currentLoc.getLongitude()));
//            callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, String.valueOf(currentLoc.getLatitude()),
//                    String.valueOf(currentLoc.getLongitude()));


            }
        }
    }


    /* Get current location */
    private Location getCurrentLatLong() {

        Location location = new Location("");
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*Ask for permission on locatio access
            * Set flag for call back to continue this process*/
                permissionsAccessLocation(3);
            }

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                location.setLatitude(mLastLocation.getLatitude());
                location.setLongitude(mLastLocation.getLongitude());

            }
        }

        return location;
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
                startLocationUpdate();
            } else {
            /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(getActivity(), getString(R.string.no_internet), new InterfaceBtnCallback() {
                    @Override
                    public void onPositiveClick() {

                    }
                });

            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (getActivity() != null) {

            LocationManager mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                    initGoogleAPIClient();
                } else {
                    screenAPICall();
                }
            } else {
                LocationSettingsRequest.Builder locSettingsReqBuilder = new LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest());
                PendingResult<LocationSettingsResult> pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locSettingsReqBuilder.build());
                pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        if (getActivity() != null) {
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
                    }
                });
            }
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

    @Override
    public void onConnectionSuspended(int i) {
        if (getActivity() != null) {
            DialogManager.getInstance().showToast(getActivity(), "onConnectionSuspended");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (getActivity() != null) {
            DialogManager.getInstance().showToast(getActivity(), connectionResult.getErrorMessage());
        }
    }

    /* to stop the location updates */
    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, MapFragment.this);
        }

    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        stopAgentAdTimer();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            AppConstants.CURRENT_LATITUDE = location.getLatitude();
            AppConstants.CURRENT_LONGITUDE = location.getLongitude();
        }
    }

    /* Ask for permission on Location access*/
    private boolean permissionsAccessLocation(final int askPermissionFromIntFlag) {
        if (getActivity() != null) {
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
                addPermission = askAccessPermission(listPermissionsNeeded, 1, new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {
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

                    @Override
                    public void onPositiveClick() {

                    }


                });
            }

            return addPermission;
        } else {
            return false;
        }
    }


    private void markerClick(final Marker marker) {


        if (marker.getTag() != null && getActivity() != null) {
            final PropertyEntity propertyEntity = (PropertyEntity) marker.getTag();

            setClickedMarkerData(propertyEntity, marker);

            for (int i = 0; i < mPropertyArrListRes.size(); i++) {
                if (propertyEntity != null && propertyEntity.getMarkers() != null && propertyEntity.getMarkers().getMarkerid().equals(mPropertyArrListRes.get(i).getMarkers().getMarkerid())) {
                    final int propertyInt = i;

                    mPropertySingleDetailRecyclerView.clearFocus();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mPropertySingleDetailRecyclerView.getLayoutManager() != null) {
                                ((LinearLayoutManager) mPropertySingleDetailRecyclerView.getLayoutManager()).scrollToPositionWithOffset(propertyInt, 0);
                                mPropertySingleDetailRecyclerView.requestFocus();
                            }

                        }
                    });
                    break;
                }
            }

            /*bottom recycler scroll finder for property*/
            mPropertySingleDetailRecyclerView.getViewTreeObserver().
                    addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mPropertySingleDetailRecyclerView.getLayoutManager() != null) {
                                            int positionView = ((LinearLayoutManager) mPropertySingleDetailRecyclerView.getLayoutManager()).
                                                    findFirstVisibleItemPosition();

                                            if (mPropertyArrListRes.size() > positionView && positionView >= 0 && mPrevPosInt != positionView) {
                                                mPrevPosInt = positionView;
                                                Marker mPrevMarker = mPropertyArrListRes.get(positionView).getMarker();
                                                moveMapCamera(mPropertyArrListRes.get(positionView).getLatitude(), mPropertyArrListRes.get(positionView).getLongitude());
                                                setClickedMarkerData(mPropertyArrListRes.get(positionView), mPrevMarker);
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    });

        }


    }


    private void setSingleItemPropertyAdapter(ArrayList<PropertyEntity> propertyArrListRes) {

        if (getActivity() != null) {
            ArrayList<PropertyEntity> propertyArrList = new ArrayList<>();
            propertyArrList.addAll(propertyArrListRes);
            mPropertyDetailAdapter = new PropertyDetailAdapter(getActivity(), propertyArrList, new InterfaceBtnCallback() {
                @Override
                public void onPositiveClick() {
                    showInterstitialAd();
                }
            });
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            mPropertySingleDetailRecyclerView.setLayoutManager(manager);
            mPropertySingleDetailRecyclerView.setAdapter(mPropertyDetailAdapter);
            SnapHelper snapHelper = new LinearSnapHelper();
            mPropertySingleDetailRecyclerView.setOnFlingListener(null);
            snapHelper.attachToRecyclerView(mPropertySingleDetailRecyclerView);
        }


    }


    private void setClickedMarkerData(final PropertyEntity propertyEntity, final Marker marker) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {   /*set previous marker data*/
                    if (mPrevMarker != null) {

                        final View markerView1 = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.map_marker_light, null);
                        TextView numTxt1 = markerView1.findViewById(R.id.num_txt);
                        // numTxt1.setTypeface(helvetica_bold);
                        ImageView prop_type_icon = markerView1.findViewById(R.id.property_type_img);
                        numTxt1.setText(mPropertyEntity.getMarkers().getMarkername());
//                        switch (mPropertyEntity.getMarkers().getMarkerpropType()) {
//                            case AppConstants.EXCLUSIVE:
//                                prop_type_icon.setVisibility(View.VISIBLE);
//                                prop_type_icon.setImageResource(R.drawable.exclusives_icon);
//                                break;
//                            case "openhouse":
//                                prop_type_icon.setVisibility(View.VISIBLE);
//                                prop_type_icon.setImageResource(R.drawable.open_houses_icon);
//                                break;
//                            case "favourite":
//                                prop_type_icon.setVisibility(View.VISIBLE);
//                                prop_type_icon.setImageResource(R.drawable.favourite_disable);
//                                break;
//                            default:
//                                prop_type_icon.setVisibility(View.GONE);
//                                break;
//                        }

                        if (mPropertyEntity.getMarkers().getMarkerpropType()
                                .equalsIgnoreCase("RB")) {
                            prop_type_icon.setVisibility(View.VISIBLE);
                            prop_type_icon.setImageResource(R.drawable.exclusives_icon);
                        } else {
                            prop_type_icon.setVisibility(View.INVISIBLE);
                        }


                        mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.loadBitmapFromView(markerView1)))
                                .position(new LatLng(Double.parseDouble(mPropertyEntity.getLatitude()),
                                        Double.parseDouble(mPropertyEntity.getLongitude()))));


                    }

        /*clicked marker item*/
                    final View markerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.map_marker_dark, null);
                    TextView numTxt = markerView.findViewById(R.id.num_txt);
                    // numTxt.setTypeface(helvetica_bold);
                    ImageView prop_type_icon = markerView.findViewById(R.id.property_type_img);
                    if (marker.getId().equals(propertyEntity.getMarkers().getMarkerid())) {
                        mPrevMarker = marker;
                        mPropertyEntity = propertyEntity;
                        numTxt.setText(propertyEntity.getMarkers().getMarkername());

//                        switch (propertyEntity.getMarkers().getMarkerpropType()) {
//                            case "exclusive":
//                                prop_type_icon.setVisibility(View.VISIBLE);
//                                prop_type_icon.setImageResource(R.drawable.exclusives_icon);
//                                break;
//                            case "openhouse":
//                                prop_type_icon.setVisibility(View.VISIBLE);
//                                prop_type_icon.setImageResource(R.drawable.open_houses_icon);
//                                break;
//                            case "favourite":
//                                prop_type_icon.setVisibility(View.VISIBLE);
//                                prop_type_icon.setImageResource(R.drawable.favourite_disable);
//                                break;
//                            default:
//                                prop_type_icon.setVisibility(View.GONE);
//                                break;
//                        }


                        if (propertyEntity.getMarkers().getMarkerpropType()
                                .equalsIgnoreCase("RB")) {
                            prop_type_icon.setVisibility(View.VISIBLE);
                            prop_type_icon.setImageResource(R.drawable.exclusives_icon);
                        } else {
                            prop_type_icon.setVisibility(View.INVISIBLE);
                        }


                    }

                    mGoogleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(ImageUtil.loadBitmapFromView(markerView)))
                            .position(new LatLng(Double.parseDouble(mPropertyEntity.getLatitude()),
                                    Double.parseDouble(mPropertyEntity.getLongitude()))));
                }
            });

        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (getActivity() != null) {
            mPropertySingleDetailRecyclerView.setVisibility(View.GONE);
            mBottomViewLay.setVisibility(View.VISIBLE);
            mSavedSearchRecyclerView.setVisibility(View.GONE);
            ((HomeScreen) getActivity()).setHeaderRightSecondImgLayVisible(R.drawable.filter_icon, 1, "");
            ((HomeScreen) getActivity()).setHeaderRightThirdTxtLayVisible(getString(R.string.cancel), 0);
            final Handler mapHandler = new Handler();
            Runnable mapRunnable = new Runnable() {
                @Override
                public void run() {
                    mapHandler.removeCallbacks(this);
                    isMarkerClickBool = false;
                }
            };
            mapHandler.postDelayed(mapRunnable, 3000);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        isMarkerClickBool = true;
        mPropertySingleDetailRecyclerView.setVisibility(View.VISIBLE);
        mBottomViewLay.setVisibility(View.INVISIBLE);
        markerClick(marker);
        return false;
    }

    public void moveMapCamera(final String lat, final String lang) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng moveLoc = new LatLng(Double.parseDouble(!lat.isEmpty() ?
                            lat : "0.0"),
                            Double.parseDouble(!lang.isEmpty() ? lang : "0.0"));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(moveLoc, 17));
                }
            });
        }

    }

    @Override
    public void searchProperty(String lat, String langStr, String locNameStr) {
        isHandDrawBool = false;
        mDrawBtn.setBackgroundResource(R.drawable.finger_draw_disable1);
        mSavedSearchRecyclerView.setVisibility(View.GONE);
        setPlaceNameEdt(locNameStr);
        moveMapCamera(lat, langStr);
        //Distance
        MAP_DISTANCE = 50;
        callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, lat, langStr);

    }

    private void setPlaceNameEdt(String locNameStr) {
        if (getActivity() != null) {
            // ((HomeScreen) getActivity()).setHeaderEdt(locNameStr, 1);
            ((HomeScreen) getActivity()).mHeaderEdt.setText(locNameStr);
            ((HomeScreen) getActivity()).mHeaderEdt.setSelection(((HomeScreen) getActivity()).mHeaderEdt.getText().length());
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

            Projection mProjection = mGoogleMap.getProjection();
            Point x_y_points = new Point(x_co, y_co);

            LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(x_y_points);
            double latitude = latLng.latitude;

            double longitude = latLng.longitude;

            Point xy = new Point(x_co, y_co);
            LatLng mlat = mGoogleMap.getProjection().fromScreenLocation(xy);
            lat = mlat.latitude;
            lon = mlat.longitude;

            if (mIsTouchEnableBool) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mLatLangList = new ArrayList<LatLng>();
                        mLatLangList.add(new LatLng(latitude, longitude));

                        touch_start(x, y);
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touch_move(x, y);
                        invalidate();
                        mLatLangList.add(new LatLng(latitude, longitude));
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


    public void drawMap() {

        PolygonOptions drawShapeOptions = new PolygonOptions();
        drawShapeOptions.addAll(mLatLangList);
        drawShapeOptions.strokeColor(Color.BLACK);
        drawShapeOptions.strokeWidth(5);
        drawShapeOptions.fillColor(Color.GRAY);
        Polygon mDrawShape = mGoogleMap.addPolygon(drawShapeOptions);
        mIsTouchEnableBool = !mIsTouchEnableBool;
        mMapRelativeLay.removeView(mDrawingViews);
//        position_map = new ArrayList<String>();
//        for (int i = 0; i < mValidate1.size(); i++) {
//            is_add = false;
//            if (coordinateInRegion(mLatLangList, mValidate1.get(i))) {
//                is_add = true;
//
//            }
//            if (!is_add) {
//                position_map.add(i + "");
//            }
//        }
//
//        for (int idx = position_map.size() - 1; idx >= 0; idx--) {
//            try {
//                mProperty.remove(Integer.parseInt(position_map.get(idx)));
//
//            } catch (Exception e) {
//                break;
//            }
//        }
//        isBoundBool = true;
//        setMapMarker(mGoogleMap, mProperty);
//        setPropertyDetailAdapter(mProperty);
//        setSingleItemPropertyAdapter(mProperty);


            /*bound the drawing marker*/
//        if (isBoundBool) {
//            isBoundBool = false;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        double latitudeDouble = 0;
        double longitudeDouble = 0;
        int sizeInt = drawShapeOptions.getPoints().size();
        for (LatLng marker : drawShapeOptions.getPoints()) {
            builder.include(marker);
            latitudeDouble += marker.latitude;
            longitudeDouble += marker.longitude;
        }
        mBounds = builder.build();
        sysOut("latitudeDouble/sizeInt " + latitudeDouble / sizeInt);
        sysOut("mBounds.getCenter().latitude " + mBounds.getCenter().latitude);

        sysOut("longitudeDouble/sizeInt " + longitudeDouble / sizeInt);
        sysOut("mBounds.getCenter().longitude " + mBounds.getCenter().longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mBounds, 17));
        callPropertyListAPI(AppConstants.TYPE_OF_PROPERTY, String.valueOf(latitudeDouble / sizeInt), String.valueOf(longitudeDouble / sizeInt));

//        }


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

    private void startAgentAdTimer(final ArrayList<AgentDetailsResultEntity> agentList) {
        stopAgentAdTimer();
        mAgentAdTimer = new Timer();
        mAgentAdTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                /*Need to show the AD*/
                            showAgentAd(agentList);


                            mAgentAdHandler = new Handler();
                            mAgentAdRunnable = new Runnable() {
                                @Override
                                public void run() {
                        /*need to gone the AD*/
                                    mAgentAdViewLay.setVisibility(View.GONE);
                                    mAgentAdHandler.removeCallbacks(mAgentAdRunnable);
                                }
                            };

                            mAgentAdHandler.postDelayed(mAgentAdRunnable, 20000);
                        }
                    });
                }
            }
        }, 5000, 35000);
    }

    private void stopAgentAdTimer() {
        mAgentAdViewLay.setVisibility(View.GONE);
        if (mAgentAdTimer != null) {
            mAgentAdTimer.cancel();
            mAgentAdTimer.purge();
        }
        if (mAgentAdHandler != null) {
            mAgentAdHandler.removeCallbacks(mAgentAdRunnable);
        }
    }

    private void showAgentAd(ArrayList<AgentDetailsResultEntity> agentList) {
        if (mAgentAdPosInt < agentList.size() && agentList.get(mAgentAdPosInt) != null) {
            mAgentAdViewLay.setVisibility(View.VISIBLE);
            AgentDetailsResultEntity agentDetail = agentList.get(mAgentAdPosInt);
            AppConstants.FILE_AGENT_FILTER_RESULT_ENTITY.setUser_id(agentDetail.getUser_id());
            mAgentUserNameTxt.setText(agentDetail.getUser_name());
            mAgentFirstNameTxt.setText(agentDetail.getFirst_name());

            if (agentDetail.getUser_pic().isEmpty()) {
                mAgentProfileImg.setImageResource(R.drawable.default_prop_icon);
            } else {
                try {
                    Glide.with(getActivity())
                            .load(agentDetail.getUser_pic()).into(mAgentProfileImg);
                } catch (Exception e) {
                    mAgentProfileImg.setImageResource(R.drawable.default_prop_icon);
                }
            }

            mAgentAdPosInt = mAgentAdPosInt == agentList.size() ? 0 : (mAgentAdPosInt += 1);
        } else {
            mAgentAdPosInt = 0;
            mAgentAdViewLay.setVisibility(View.GONE);
            stopAgentAdTimer();
        }


    }

    /*Ad Part interstital before property details fragment*/

    private void showInterstitialAd() {


        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (AppConstants.MAP_CURRENT_BACK_FRAGMENT instanceof ReviewsListFragment) {
                        AppConstants.CURRENT_REVIEW_DETAILS = new PropertyReviewCommentEntity();
                        AppConstants.CURRENT_REVIEW_DETAILS.setProperty_id(AppConstants.DETAIL_PROPERTY_ID);
                        AppConstants.CURRENT_REVIEW_DETAILS.setReview_header_txt(getString(R.string.post_review));
                    /*this comment id assignment to check whether we have to call Update API or PostReview API...in postREview it assigned postreview str and
                    * edit review it would have  particular comment id and for Update it was empty str*/
                        AppConstants.CURRENT_REVIEW_DETAILS.setProperty_review_comment_id(getString(R.string.post_review));
                        ((HomeScreen) getActivity()).addFragment(new ReviewsWriteFragment());
                    } else if (AppConstants.MAP_CURRENT_BACK_FRAGMENT instanceof MyFavouriteFragment) {
                        APIRequestHandler.getInstance().addBoardAPICall(AppConstants.DETAIL_PROPERTY_ID, getActivity(), new InterfaceAPICommonCallback() {
                            @Override
                            public void onRequestSuccess(Object obj) {
                                CommonResponse mRes = (CommonResponse) obj;
                                ((HomeScreen) getActivity()).onBackPressed();
                            }

                            @Override
                            public void onRequestFailure(Throwable r) {
                                DialogManager.getInstance().showToast(getActivity(), r.getMessage());
                                ((HomeScreen) getActivity()).onBackPressed();
                            }
                        });

                    } else if (PreferenceUtil.getStringValue(getActivity(), AppConstants.MAP_AD_COUNT).isEmpty() || Integer.valueOf(PreferenceUtil.getStringValue(getActivity(), AppConstants.MAP_AD_COUNT)) < 5) {
                        int adCountInt = PreferenceUtil.getStringValue(getActivity(), AppConstants.MAP_AD_COUNT).isEmpty() ? 0 : Integer.valueOf(PreferenceUtil.getStringValue(getActivity(), AppConstants.MAP_AD_COUNT));
                        PreferenceUtil.storeStringValue(getActivity(), AppConstants.MAP_AD_COUNT, (adCountInt + 1) + "");
                        ((HomeScreen) getActivity()).addFragment(new PropertyDetailsFragment());
                    } else {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                        mInterstitialAd.setAdListener(new AdListener() {

                            @Override
                            public void onAdClosed() {

                                final Handler m = new Handler();
                                Runnable ss = new Runnable() {
                                    @Override
                                    public void run() {

                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    m.removeCallbacks(this);
                                                    PreferenceUtil.storeStringValue(getActivity(), AppConstants.MAP_AD_COUNT, AppConstants.FAILURE_CODE);
                                                    ((HomeScreen) getActivity()).addFragment(new PropertyDetailsFragment());
                                                }
                                            });
                                        }
                                    }
                                };
                                m.postDelayed(ss, 50);
                            }

                        });
                    }
                }
            });
        }


    }

}


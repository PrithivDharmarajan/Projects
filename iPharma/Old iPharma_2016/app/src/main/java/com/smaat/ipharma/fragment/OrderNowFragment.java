package com.smaat.ipharma.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.CustomMapFragment;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.ProfileImageSelectionUtil;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.text.DecimalFormat;

import retrofit.RetrofitError;

public class OrderNowFragment extends BaseFragment implements OnClickListener, OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private ImageView mPharmacyImage;
    @SuppressWarnings("unused")
    private TextView mPharmacyName, mPharmacyAddress, mPharmacyDistance,
            mPhone, mEmail, mWebsite, mREviewsCount, mOpenTime, mCloseTime,
            mDeliverTime, mMinOrder, mReviews;
    private LinearLayout mReviewLayout, mRatingLayout;
    private Button mPlaceOrder, mFavBtn;
    private RatingBar mRate;
    public static Bitmap mSelectedImgBitmap;
    private float downX, downY, upX, upY;
    FrameLayout mImageFrame;
    int i = 0;

    static final int MIN_DISTANCE = 100;

    MapScreenFragment mMap = new MapScreenFragment();
    private boolean isClicked = true;
    private View rootview;
    public static MapPropertyEntity mMapDetailEntity;
    Bundle extras;
    private String UserID;
    private Typeface mTypeface_bold;
    private String mShopId, mRedirection;
    private Bundle bundle;
    private Uri mUri;
    private Intent mUrl;
    private ScrollView mParentLayout;
    private SurfaceView mSurfaceView;
    private LinearLayout mCall_click, mEmail_click, mWebsite_click;

    static String mSelectedImgLocalPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            rootview = inflater.inflate(R.layout.fragment_order_now, container,
                    false);
            mSurfaceView = new SurfaceView(getActivity());
            mSurfaceView.setZOrderOnTop(true);
            SurfaceHolder holder = mSurfaceView.getHolder();
            holder.setFormat(PixelFormat.TRANSPARENT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup mViewGroup = (ViewGroup) view
                .findViewById(R.id.parent_layout);
        Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
                getActivity());
        setFont(mViewGroup, mTypeface);
        setupUI(mViewGroup);
        mTypeface_bold = TypefaceSingleton.getInstance().getHelveticaBold(
                getActivity());
        extras = this.getArguments();
        if (extras != null) {
            mMapDetailEntity = (MapPropertyEntity) extras
                    .getSerializable(AppConstants.PHARMCAY_DETAILS);
            mRedirection = extras.getString(AppConstants.REDIRECTION);
            AppConstants.mMapDetailEntity = mMapDetailEntity;
            mShopId = mMapDetailEntity.getPharmacyID();
            AppConstants.Pharmacy_id = mShopId;
        } else {
            mMapDetailEntity = AppConstants.mMapDetailEntity;
            mShopId = mMapDetailEntity.getPharmacyID();
            AppConstants.Pharmacy_id = mShopId;
        }
        UserID = GlobalMethods.getUserID(getActivity());
        hideSoftKeyboard(getActivity());
        initComponents(view);
        if (mRedirection != null
                && mRedirection.equalsIgnoreCase(AppConstants.OFFERS)) {
            AppConstants.FRAG = AppConstants.OFFERS_SCREEN;
        } else {
            if (AppConstants.FROM_MAPFAVORITE_SCREEN
                    .equalsIgnoreCase(AppConstants.MAP_SCREEN)) {
                AppConstants.FRAG = AppConstants.MAP_SCREEN;
            } else if (AppConstants.FROM_MAPFAVORITE_SCREEN
                    .equalsIgnoreCase(AppConstants.FAVORITE_SCREEN)) {

                AppConstants.FRAG = AppConstants.FAVORITE_SCREEN;
            } else {

                AppConstants.FRAG = AppConstants.OFFERS_SCREEN;
            }
        }
        try {
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                HomeScreen.onBackMove(getActivity());

            }
        });
        setValues();
    }

    private void setValues() {
        mPharmacyName.setText(mMapDetailEntity.getShopName());
        mPharmacyAddress.setText(mMapDetailEntity.getAddress());
        DecimalFormat distance_roundoff = new DecimalFormat("#.##");
        mPharmacyDistance.setText(Double.valueOf(distance_roundoff
                .format(Double.valueOf(mMapDetailEntity.getDistance())))
                + " "
                + getString(R.string.km_away));
        // mPharmacyState.setText(mMapDetailEntity.getCity());
        mPhone.setText(method(mMapDetailEntity.getPhone()));
        mEmail.setText(mMapDetailEntity.getEmail());
        mWebsite.setText(mMapDetailEntity.getWebsite());
        mRate.setRating(Float.valueOf(mMapDetailEntity.getAvgRating()));
        mReviews.setText(mMapDetailEntity.getTotalReviews());
        mOpenTime.setText(mMapDetailEntity.getOpenTime());
        mCloseTime.setText(mMapDetailEntity.getCloseTime());
        mDeliverTime.setText(mMapDetailEntity.getDeliveryTime());
        mMinOrder.setText(mMapDetailEntity.getMinimumOrderValue());

    }

    public String method(String str) {
        if (str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void initComponents(View view) {
        mParentLayout = (ScrollView) view.findViewById(R.id.parent_layout);

        mCall_click = (LinearLayout) view.findViewById(R.id.mcall_click);
        mEmail_click = (LinearLayout) view.findViewById(R.id.mEmail_address);
        mWebsite_click = (LinearLayout) view.findViewById(R.id.mWebsite);

        mCall_click.setOnClickListener(this);
        mEmail_click.setOnClickListener(this);
        mWebsite_click.setOnClickListener(this);

        mPharmacyImage = (ImageView) view.findViewById(R.id.pharmacy_image);
        aq().id(mPharmacyImage).image(
                AppConstants.PHARMACY_IMAGE_BASE_URL
                        + mMapDetailEntity.getProfilePic(), true, true, 0,
                R.drawable.deafult_pharmacy_image);


        mPharmacyName = (TextView) view.findViewById(R.id.pharmacy_name);
        mPharmacyName.setTypeface(mTypeface_bold);
        mPharmacyAddress = (TextView) view.findViewById(R.id.pharmacy_address);
        mPharmacyDistance = (TextView) view
                .findViewById(R.id.pharmacy_distance);
        // mPharmacyState = (TextView) view.findViewById(R.id.state);
        // mPhotoCount = (TextView) view.findViewById(R.id.photo_count);
        mPhone = (TextView) view.findViewById(R.id.phone_num);
        mEmail = (TextView) view.findViewById(R.id.email);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mReviews = (TextView) view.findViewById(R.id.reviews);
        mOpenTime = (TextView) view.findViewById(R.id.open_time);
        mCloseTime = (TextView) view.findViewById(R.id.close_time);
        mDeliverTime = (TextView) view.findViewById(R.id.deliver_time);
        mMinOrder = (TextView) view.findViewById(R.id.min_order);
        mPlaceOrder = (Button) view.findViewById(R.id.place_order);
        mPlaceOrder.setTypeface(HomeScreen.mHelveticaBold);
        mPlaceOrder.setOnClickListener(this);
        mFavBtn = (Button) view.findViewById(R.id.fav_button);
        mFavBtn.setOnClickListener(this);
        String isFav = mMapDetailEntity.getIsFav();

        if (isFav != null && isFav.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
            mFavBtn.setBackgroundResource(R.drawable.order_now_fave_unselected);
            isClicked = false;
        } else if (isFav != null
                && isFav.equalsIgnoreCase(AppConstants.SUCCESS_CODE)
                || AppConstants.FROM_MAPFAVORITE_SCREEN
                .equalsIgnoreCase(AppConstants.FAVORITE_SCREEN)) {
            mFavBtn.setBackgroundResource(R.drawable.order_now_fav_selected);
            isClicked = true;
        } else if (isFav != null
                && isFav.equalsIgnoreCase(getString(R.string.two))) {
            mFavBtn.setBackgroundResource(R.drawable.order_now_fave_unselected);
            isClicked = false;
        }
        // if(mMapDetailEntity.getIsFav()) {
        //
        // }
        mReviewLayout = (LinearLayout) view.findViewById(R.id.reviews_layout);
        mReviewLayout.setOnClickListener(this);
        mRate = (RatingBar) view.findViewById(R.id.fav_ratingbar);
        mRate.setOnClickListener(this);
        mRate.setClickable(true);
        mRatingLayout = (LinearLayout) view.findViewById(R.id.rate_layout);
        mRatingLayout.setOnClickListener(this);

        mImageFrame = (FrameLayout) view.findViewById(R.id.image_frame);

    }


    public void showRateDialog() {

        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_rate);

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        mDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Button rate = (Button) mDialog.findViewById(R.id.rate_it);
        Button cancel = (Button) mDialog.findViewById(R.id.cancel);
        rate.setTypeface(HomeScreen.mHelveticaBold);
        cancel.setTypeface(HomeScreen.mHelveticaBold);
        TextView mRateText = (TextView) mDialog.findViewById(R.id.rate_text);
        final TextView mRating_avg_txt = (TextView) mDialog
                .findViewById(R.id.rating_avg_txt);
        mRating_avg_txt.setTypeface(HomeScreen.mHelveticaBold);
        mRateText.setTypeface(HomeScreen.mHelveticaNormal);
        final RatingBar mFav_ratingbar = (RatingBar) mDialog
                .findViewById(R.id.fav_ratingbar);
        final Button mRating_icons = (Button) mDialog
                .findViewById(R.id.rating_icons);

        mFav_ratingbar
                .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

                    public void onRatingChanged(RatingBar ratingBar,
                                                float rating, boolean fromUser) {
                        switch ((int) mFav_ratingbar.getRating()) {
                            case 1:
                                mRating_avg_txt.setText(getString(R.string.poor));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.poor);
                                break;
                            case 2:
                                mRating_avg_txt.setText(getString(R.string.bad));
                                mRating_icons.setBackgroundResource(R.drawable.bad);
                                break;
                            case 3:
                                mRating_avg_txt
                                        .setText(getString(R.string.average));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.average);
                                break;
                            case 4:
                                mRating_avg_txt.setText(getString(R.string.good));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.good);
                                break;
                            case 5:
                                mRating_avg_txt
                                        .setText(getString(R.string.excellent));
                                mRating_icons
                                        .setBackgroundResource(R.drawable.excellent);
                                break;
                        }
                    }
                });

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        });

        rate.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                float rating_value = mFav_ratingbar.getRating();
                callPharmacyRatingService(rating_value);// API Call
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void callPharmacyRatingService(float rating_value) {

        String rating = String.valueOf(rating_value);
        APIRequestHandler.getInstance().shopRating(mShopId, UserID, rating,
                this);

    }

    private void initializeMap() {
        try {


            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_view);
            if (fragment == null) {
                fragment = SupportMapFragment.newInstance();
                fm.beginTransaction().replace(R.id.map_view, fragment).commit();
            }

            fragment.getMapAsync(this);

            ((CustomMapFragment) fm
                    .findFragmentById(R.id.map_view))
                    .setListener(new CustomMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            hideSoftKeyboard(getActivity());
                            mParentLayout
                                    .requestDisallowInterceptTouchEvent(true);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap == null) {
            Toast.makeText(getActivity(),
                    getString(R.string.unable_to_create_maps),
                    Toast.LENGTH_SHORT).show();
        } else {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings()
                    .setMyLocationButtonEnabled(false);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            getCurrentLocation();
        }


        //	mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getCurrentLocation() {
        GPSTracker tracker = new GPSTracker(getActivity());
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
        } else {
            // mCurrentLatitude = tracker.getLatitude();
            // mCurrentLongitude = tracker.getLongitude();
        }


        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double
                .valueOf(mMapDetailEntity.getLatitude()), Double
                .valueOf(mMapDetailEntity.getLongitude()))));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        MarkerOptions marker = new MarkerOptions().position(new LatLng(Double
                .valueOf(mMapDetailEntity.getLatitude()), Double
                .valueOf(mMapDetailEntity.getLongitude())));

        marker.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.pharmacy_marker));
        mGoogleMap.addMarker(marker);

    }


    private String TAG = "IPharma";

    public void setImageListener(View v) {

        v.setOnTouchListener(new OnTouchListener() {

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
                                    // onRightToLeftSwipe();
                                    return true;
                                }
                                if (deltaX > 0) {
                                    // onLeftToRightSwipe();
                                    return true;
                                }
                            } else {

                                return false; // We don't consume the event
                            }
                        }
                        // swipe vertical?
                        else {
                            if (Math.abs(deltaY) > MIN_DISTANCE) {
                                // top or down
                                if (deltaY < 0) {

                                    return true;
                                }
                                if (deltaY > 0) {
                                    return true;
                                }
                            } else {

                                return false; // We don't consume the event
                            }
                        }

                        return true;
                    }
                }
                return true;
            }
        });

    }


    private void callFavouriteservice(String isCheck) {

        APIRequestHandler.getInstance().addFavourite(mShopId, UserID, isCheck,
                this);
    }

    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof RateResponseEntity) {
            RateResponseEntity mRatingResponse = (RateResponseEntity) responseObj;
            if (mRatingResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)
                    || mRatingResponse.getStatus().equalsIgnoreCase("success")) {
                DialogManager
                        .showCustomAlertDialog(getActivity(),
                                OrderNowFragment.this,
                                getString(R.string.rating_added));
                mRate.setRating(Float.valueOf(mRatingResponse.getResult()));
            } else {

                DialogManager.showCustomAlertDialog(getActivity(), this,
                        mRatingResponse.getMsg());
            }

        } else if (responseObj instanceof WriteReviewEntity) {

            WriteReviewEntity mWriteReviewResponse = (WriteReviewEntity) responseObj;
            if (mWriteReviewResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                DialogManager.hideProgress(getActivity());
            } else {
                DialogManager.showCustomAlertDialog(getActivity(), this,
                        mWriteReviewResponse.getMsg());
            }

        }

        super.onRequestSuccess(responseObj);

    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        super.onRequestFailure(errorCode);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mcall_click:
                if (mPhone.getText().toString().equalsIgnoreCase(null)
                        || mPhone.getText().toString().equalsIgnoreCase("")) {
                    showToast(getActivity(), getString(R.string.ph_not_available));
                } else {
                    Intent dial = new Intent();
                    dial.setAction("android.intent.action.DIAL");
                    dial.setData(Uri.parse("tel:" + mMapDetailEntity.getPhone()));
                    startActivity(dial);


                }

                break;
            case R.id.mEmail_address:
                if (mEmail.getText().toString().equalsIgnoreCase(null)
                        || mEmail.getText().toString().equalsIgnoreCase("")) {
                    showToast(getActivity(),
                            getString(R.string.email_not_available));
                } else {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", mMapDetailEntity.getEmail(),
                                    null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "iPharma");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "iPharma");
                    getActivity().startActivity(
                            Intent.createChooser(emailIntent, "Send email..."));
                }

                break;
            case R.id.mWebsite:
                if (mWebsite.getText().toString().equalsIgnoreCase(null)
                        || mWebsite.getText().toString().equalsIgnoreCase("")) {
                    showToast(getActivity(),
                            getString(R.string.website_not_available));
                } else {
                    try {

                        mUri = Uri.parse(getString(R.string.http)
                                + (mMapDetailEntity.getWebsite().toString()));
                        mUrl = new Intent(android.content.Intent.ACTION_VIEW, mUri);
                        v.getContext().startActivity(mUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.fav_button:
                // Tap to add favourite
                String isfav = "0";
                if (!isClicked) {
                    isClicked = true;
                    mFavBtn.setBackgroundResource(R.drawable.order_now_fav_selected);
                    isfav = "1";
                } else {
                    isClicked = false;
                    mFavBtn.setBackgroundResource(R.drawable.order_now_fave_unselected);
                    isfav = "2";
                }
                callFavouriteservice(isfav);// API Call
                break;
            case R.id.place_order:


                ProfileImageSelectionUtil.showscanPopUp(getActivity());


                break;
            case R.id.reviews_layout:
                ((HomeScreen) getActivity()).replaceFragment(new ReviewsFragment(),
                        true);
                HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
                HomeScreen.mBottombar.setVisibility(View.GONE);
                HomeScreen.mFooterText.setText(R.string.write_review);
                HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
                break;
            case R.id.rate_layout:
                showRateDialog();
                break;
            default:
                break;
        }
    }


    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        // TODO Auto-generated method stub

    }
}

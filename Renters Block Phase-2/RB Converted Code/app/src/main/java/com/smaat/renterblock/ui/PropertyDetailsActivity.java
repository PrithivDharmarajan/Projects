package com.smaat.renterblock.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.RecommendedReviewsAdapter;
import com.smaat.renterblock.entity.PostPropertyUserDetails;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.entity.PropertyReview;
import com.smaat.renterblock.entity.UserPropertyPics;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.FriendRequestResponse;
import com.smaat.renterblock.model.PropertyDetailResponse;
import com.smaat.renterblock.model.PropertyResponse;
import com.smaat.renterblock.myfavourite.BoardsChatActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.ExtendedViewPager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ProfileImageSelectionUtil;
import com.smaat.renterblock.util.ScaleImageView;
import com.smaat.renterblock.util.TouchImageView;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PropertyDetailsActivity extends BaseActivity implements OnMapReadyCallback, DialogMangerCallback {

    private String UserID = "", mPropertyId = "", mPropertyReviewId = "", mPropertyTyp = "", mCallFrom = "";
    private boolean isClicked = false, isFir = false, isDonshow = false, isfavourite = true;
    private int height = 0, newHeight = 0, frameHeight = 0;
    private double mLat = 0.0, mLong = 0.0;
    private Button mFav;
    private TextView amnt_txt, photos_count_txt, location_txt, proprty_type, property_types, square_footage, beds,
            bathsrooms, build_year, detail_txt, more_txt, detail_text_full, number_of_images;
    private ImageView mPropertyImg, mPropertyBlurImg, user_image;
    private ProgressBar progress, user_progress, vid_progress;
    private RelativeLayout mTopLay, mBackArrow, vid_lay;
    private FrameLayout mFrameLay;
    private ImageLoader imageLoader;
    private DirectionalViewPager pager;
    private DisplayMetrics dm;
    private DetailOnPageChangeListener listener;
    private VideoDetailOnPageChangeListener list_ner;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private MapFragment mMapFragment;
    private Bundle mBundle;
    private ArrayList<String> imageUrls = null;
    public static ArrayList<PropertyEntity> mPropertyDetailsResponse;
    private ArrayList<PostPropertyUserDetails> mPropertyPostedUserDetailsResponse = new ArrayList<>();
    private ArrayList<PropertyPictures> mPropertyPicturesResponse;
    private ArrayList<PropertyReview> mPropertyReviewResponse;

    private DisplayImageOptions options;
    private Bitmap[] bitmap;
    private URL url;
    private Bitmap myBitmap;
    ParallaxScrollView mScrollView;
    Dialog alertOptionDialog;
    File file;
    FileInputStream fileInputStream = null;
    byte[] bFile;
    private Bitmap mainImage;
    private LinearLayout user_image_lay, mReviewLay;
    private VideoView user_video;
    private Button vid_play_btn, chat_icon;
    private String img_url, video_url;
    private Dialog d2;
    ProgressBar image_prog;
    ScaleImageView profile_pic;
    Button play_btn, pause_btn, view_all, bck_btn;
    private TextView bck_txt;
    boolean istouched = false;
    int stopPosition;
    private int page_no_count = 0;
    private int count = 1;
    private int currentPage;
    PropertyDetailResponse propertydetailResponse;
    private RelativeLayout mDescripLay, mRecommend_lay;
    private FrameLayout mVideoFrameLay;
    private TextView review_user_name, recommand_review_comment, recommand_review_time;
    private RatingBar recommand_revie_rating;
    private RecommendedReviewsAdapter mReviewsAdapter;
    private Dialog mReviewDialog;
    private TextView mRequestBusinessName, mRequestUserName, mRequestPhone, mRequestAddress;

    private LinearLayout mCallIcon;

    private String Amountproperty;
    private Marker marker;
    // private MarkerOptions marker;
    private View marker_view_shown;
    private TextView numTxt;
    private View marker_view, selected_marker;
    private ImageView mMarkerImg;
    private Button prop_typ_icon;
    private String mSplitedAmount = "";
    private TextView mMsgAddress, mMsgReviews;
    private ProgressBar mMsgProgress;
    private RatingBar mMsgRatingBar;
    private Button mMsgAddBtn, mPropertyShare;
    private RelativeLayout mMsgBoardLay;
    float initialX, initialY;
    private Handler handler = new Handler();
    private RelativeLayout like_lay;

    VideoView profile_video;
    RelativeLayout adap_vidl1;
    ProgressBar video_prog;
    FrameLayout frame;
    MediaController controller;

    DirectionalViewPager photo_pager;
    TextView last_updated;

    static Context mContext;
    static TouchImageView img;
    private static AQuery aq;
    TouchImageView profile_pics;

    ExtendedViewPager mViewPager;
    PropertyResponse mResponse;
    public static String for_image_act_property_id;
    private Button mAddToMyBoardsBig;
    private String ison;

    private RelativeLayout rb_lay;

    private RatingBar property_rating_main;
    private TextView property_review_coun;

    private Button seemore_button, report_fraud, friend_icon, mReport_Send, mReport_Info;
    private Rect scrollBounds;

    private ImageView request_user_img;
    private LinearLayout mContactInfoLay;
    private Dialog mReportFraudDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShowPopuptimer();

        setContentView(R.layout.activity_property_details);
        aq = aq();
        mBundle = getIntent().getExtras();
        UserID = GlobalMethods.getUserID(this);
        mContext = PropertyDetailsActivity.this;
        getDeviceHeight();
        InitializeViews();
        scrollBounds = new Rect();
        mScrollView.getHitRect(scrollBounds);
        report_fraud = (Button) findViewById(R.id.report_fraud);
        mContactInfoLay = (LinearLayout) findViewById(R.id.text_lay);
        rb_lay = (RelativeLayout) findViewById(R.id.rb_lay);
        mReport_Send = (Button) findViewById(R.id.request_send);
        mReport_Info = (Button) findViewById(R.id.request_info);
        if (mBundle != null) {
            mPropertyId = mBundle.getString("PropertyId");
            mPropertyTyp = mBundle.getString("PropType");
            mCallFrom = mBundle.getString("CallFrom");
        }

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        fragment.getMapAsync(PropertyDetailsActivity.this);

        getPropertyDetails();
        handler.postDelayed(Runn, 10000);
        includeAdMob();


    }

    private void includeAdMob() {

        AdView mAdView = (AdView) findViewById(R.id.adView);
        // mAdView.setVisibility(View.GONE);
        // mAdView.clearFocus();
        ison = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
                AppConstants.SHOWADS);
        if (mEnhancedProfile.equals("0")) {
            rb_lay.setVisibility(View.VISIBLE);
        } else {
            if (ison.equals("false")) {
                rb_lay.setVisibility(View.GONE);
            } else {
                rb_lay.setVisibility(View.VISIBLE);
            }
        }
        AdRequest adRequest = new AdRequest.Builder().build();

        // mAdView.clearFocus();
        if (mAdView != null) {
            mAdView.loadAd(adRequest);
        }

        mAdView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setGoogleAnalytics(mContext);
            }
        });
        // mAdView.clearFocus();
        // mAdView.setVisibility(View.VISIBLE);
    }

    private Runnable Runn = new Runnable() {

        @Override
        public void run() {

            if (report_fraud.getLocalVisibleRect(scrollBounds)) {
                like_lay.setVisibility(View.GONE);
            } else {
                like_lay.setVisibility(View.VISIBLE);
            }

            int pos = mScrollView.getScrollY();
            System.out.println("scroll pos " + pos);
            if (pos < 200) {
                // like_lay.setVisibility(View.VISIBLE);
                like_lay.setBackgroundColor(Color.parseColor("#B3000000"));
                if (mPropertyDetailsResponse != null && mPropertyDetailsResponse.get(0).getIsfavourite() != null
                        && !mPropertyDetailsResponse.get(0).getIsfavourite().equals("")) {
                    if (mPropertyDetailsResponse.get(0).getIsfavourite().equals("0")) {
                        mFav.setBackgroundResource(R.drawable.like_icon_normal);
                    } else {
                        mFav.setBackgroundResource(R.drawable.like_icon_over);
                    }
                    mPropertyShare.setBackgroundResource(R.drawable.share_icon);
                } else {
                    mFav.setBackgroundResource(R.drawable.like_icon_normal);
                    mPropertyShare.setBackgroundResource(R.drawable.share_icon);
                }
            } else if (pos > 180) {
                like_lay.setBackgroundColor(Color.parseColor("#ffffff"));
                if (mPropertyDetailsResponse != null && mPropertyDetailsResponse.get(0).getIsfavourite() != null
                        && mPropertyDetailsResponse.get(0).getIsfavourite().equals("0")) {
                    mFav.setBackgroundResource(R.drawable.fav_icon_black);
                } else {
                    mFav.setBackgroundResource(R.drawable.fav_enable_icon_black);
                }
                mPropertyShare.setBackgroundResource(R.drawable.share_icon_black);
            }
            handler.postDelayed(Runn, 200);
        }
    };
    public RelativeLayout close_lay;
    private Dialog rev_dialog;

    private void ShowPopuptimer() {
        final Dialog mProgressDialog = new Dialog(PropertyDetailsActivity.this,
                android.R.style.Theme_Translucent_NoTitleBar);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setContentView(R.layout.propertylist_load);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                if (mScrollView != null) {
                    // mScrollView.fullScroll(View.FOCUS_UP);
                }
            }
        }, 8000);

    }

    private void getPropertyDetails() {

        String Url = AppConstants.BASE_URL + "property/view";

        GsonTransformer t = new GsonTransformer();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserID);
        params.put("property_id", mPropertyId);
        aq().transformer(t).ajax(Url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                if (json != null) {
                    mResponse = new Gson().fromJson(json.toString(), PropertyResponse.class);

                    onSuccessPropResponse(mResponse);
                } else {
                    statusErrorCode(status);
                }
            }
        });
    }

    protected void onSuccessPropResponse(PropertyResponse mResponse) {

        if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            mPropertyDetailsResponse = mResponse.getResult();

            if (mResponse.getResult().get(0).getUserpropertypic().size() == 0) {
                seemore_button.setText("Add");
            } else {
                seemore_button.setText(getString(R.string.see_more));
            }

            if (mResponse.getResult().get(0).getProperty_rating() != null) {
                property_rating_main.setRating(Integer.parseInt(mResponse.getResult().get(0).getProperty_rating()));
            }
            if (mResponse.getResult().get(0).getReview_count() != null) {
                property_review_coun.setText(" (" + mResponse.getResult().get(0).getReview_count() + ")");
            } else {
                property_review_coun.setText(" (0)");
            }

            mPropertyId = mPropertyDetailsResponse.get(0).getProperty_id();
            mPropertyPostedUserDetailsResponse = mPropertyDetailsResponse.get(0).getProperty_posted_user_details();

            mPropertyPicturesResponse = mPropertyDetailsResponse.get(0).getProperty_pics();
            Amountproperty = mPropertyDetailsResponse.get(0).getPrice_range();
            mPropertyReviewResponse = mPropertyDetailsResponse.get(0).getProperty_review();
            if (mPropertyReviewResponse.size() != 0) {
                mPropertyReviewId = mPropertyReviewResponse.get(0).getProperty_review_id();
            }

            last_updated.setText(
                    "Last Updated " + GlobalMethods.timeDiff(mResponse.getResult().get(0).getProperty_datetime()));
            if (mPropertyPicturesResponse.size() != 0) {
                new convertBitmap().execute();
            } else {
                mPropertyImg.setImageResource(R.drawable.default_prop_icon);
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_prop_icon);
                BlurImage(largeIcon);

            }
            if (mPropertyDetailsResponse.get(0).getLatitude().equals("")) {

            } else if (mPropertyDetailsResponse.get(0).getLatitude().equals("0.0")) {

            } else {
                mLat = Double.parseDouble(mPropertyDetailsResponse.get(0).getLatitude());
                mLong = Double.parseDouble(mPropertyDetailsResponse.get(0).getLongitude());

                initializeMap(mLat, mLong);
            }
            setDetails();
        }

    }

    private void callAddMyBoard() {
        String Url = AppConstants.BASE_URL + "addtoboards";

        GsonTransformer t = new GsonTransformer();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserID);
        params.put("property_id", mPropertyId);

        aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);
                            if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                                mMsgAddBtn.setVisibility(View.GONE);
                                mAddToMyBoardsBig.setVisibility(View.GONE);
                                getPropertyDetails();
                            }
                        } else {
                            statusErrorCode(status);
                        }
                    }
                });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }

    private class convertBitmap extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // if (mCallFrom != null && mCallFrom.equals("Listing")) {
                url = new URL(mPropertyPicturesResponse.get(0).getFile());
                // } else {
                // url = new URL(AppConstants.IMAGE_BASE_URL
                // + mPropertyPicturesResponse.get(0).getFile());
                // }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                // myBitmap = BitmapFactory.decodeResource(getResources(),
                // R.drawable.house_two);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPropertyImg.setImageBitmap(myBitmap);

            Matrix matrix = new Matrix();
            matrix.preScale(1, -1);
            if (myBitmap != null) {
                Bitmap rotated = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix,
                        true);
                BlurImage(rotated);
            }
            super.onPostExecute(result);
        }

    }

    private void InitializeViews() {

        request_user_img = (ImageView) findViewById(R.id.request_user_img);
        request_user_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Friend_UserID = mResponse.getResult().get(0).getProperty_posted_user_details().get(0).getUser_id();
                AppConstants.prop_friend_id = Friend_UserID;
                if (AppConstants.prop_friend_id.equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
                    DialogManager.showCustomAlertDialog(PropertyDetailsActivity.this, PropertyDetailsActivity.this,
                            "This is Your Profile");
                } else {
                    Intent inte = new Intent(PropertyDetailsActivity.this, ProfileScreen.class);
                    inte.putExtra("call_from", "1");
                    startActivity(inte);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mDescripLay = (RelativeLayout) findViewById(R.id.detail_lay);

        mScrollView = (ParallaxScrollView) findViewById(R.id.parallax_scroll);
        mPropertyImg = (ImageView) findViewById(R.id.property_image);
        mPropertyBlurImg = (ImageView) findViewById(R.id.property_blur_image);

        seemore_button = (Button) findViewById(R.id.seemore_button);

        chat_icon = (Button) findViewById(R.id.chat_icon);

        friend_icon = (Button) findViewById(R.id.friend_icon);

        mPropertyImg.getLayoutParams().height = newHeight;
        mPropertyBlurImg.getLayoutParams().height = newHeight;
        mBackArrow = (RelativeLayout) findViewById(R.id.back_arrow);
        user_image_lay = (LinearLayout) findViewById(R.id.image_lay);
        mReviewLay = (LinearLayout) findViewById(R.id.review_lay);
        amnt_txt = (TextView) findViewById(R.id.amount_txt);
        photos_count_txt = (TextView) findViewById(R.id.photos_count_txt);
        location_txt = (TextView) findViewById(R.id.location_txt);
        proprty_type = (TextView) findViewById(R.id.proprty_type);
        property_types = (TextView) findViewById(R.id.property_types);
        square_footage = (TextView) findViewById(R.id.square_footage);
        beds = (TextView) findViewById(R.id.beds);
        bathsrooms = (TextView) findViewById(R.id.bathrooms);
        build_year = (TextView) findViewById(R.id.build_year);
        detail_txt = (TextView) findViewById(R.id.detail_txt);
        more_txt = (TextView) findViewById(R.id.more_txt);
        last_updated = (TextView) findViewById(R.id.last_updated_txt);

        property_rating_main = (RatingBar) findViewById(R.id.property_ratingbar);
        property_review_coun = (TextView) findViewById(R.id.review_count_rat);

        like_lay = (RelativeLayout) findViewById(R.id.like_lay);
        mPropertyShare = (Button) findViewById(R.id.property_share);
        mFav = (Button) findViewById(R.id.favourites);

        mMsgAddress = (TextView) findViewById(R.id.msg_address);
        mMsgReviews = (TextView) findViewById(R.id.msg_reviews);
        mMsgProgress = (ProgressBar) findViewById(R.id.msg_progress);
        mMsgRatingBar = (RatingBar) findViewById(R.id.msg_ratingbar);
        mMsgAddBtn = (Button) findViewById(R.id.add_msg_board_btn);
        mAddToMyBoardsBig = (Button) findViewById(R.id.add_to_my_board_big);
        mMsgBoardLay = (RelativeLayout) findViewById(R.id.msg_view);
        mMsgBoardLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    Intent inte = new Intent(PropertyDetailsActivity.this, BoardsChatActivity.class);
                    inte.putExtra("property_id", mPropertyId);
                    startActivity(inte);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });
        mRequestBusinessName = (TextView) findViewById(R.id.request_business_name);

        mRequestBusinessName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Friend_UserID = mResponse.getResult().get(0).getProperty_posted_user_details().get(0).getUser_id();
                AppConstants.prop_friend_id = Friend_UserID;
                if (AppConstants.prop_friend_id.equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
                    DialogManager.showCustomAlertDialog(PropertyDetailsActivity.this, PropertyDetailsActivity.this,
                            "This is Your Profile");
                } else {
                    Intent inte = new Intent(PropertyDetailsActivity.this, ProfileScreen.class);
                    inte.putExtra("call_from", "1");
                    startActivity(inte);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        mRequestUserName = (TextView) findViewById(R.id.request_user_name);
        mRequestPhone = (TextView) findViewById(R.id.request_user_phone);
        mRequestAddress = (TextView) findViewById(R.id.request_property_address);
        mCallIcon = (LinearLayout) findViewById(R.id.call_icon);
        detail_text_full = (TextView) findViewById(R.id.detail_txt_full_view);
        view_all = (Button) findViewById(R.id.view_all);
        mTopLay = (RelativeLayout) findViewById(R.id.top_lay);
        mTopLay.bringToFront();

        mFrameLay = (FrameLayout) findViewById(R.id.frame_lay);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mFrameLay.getLayoutParams();
        params.setMargins(0, -newHeight + frameHeight, 0, 0);
        mFrameLay.setLayoutParams(params);

        mPropertyImg.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {

                    case MotionEvent.ACTION_DOWN:
                        initialX = event.getX();
                        initialY = event.getY();
                        if (mResponse.getResult().size() != 0) {
                            showPropertDetailDialog(PropertyDetailsActivity.this,
                                    mPropertyDetailsResponse.get(0).getPrice_range(),
                                    mPropertyDetailsResponse.get(0).getAddress());
                        }
                        break;

                }

                return false;
            }
        });
        mBackArrow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
                    // Intent inte = new Intent(PropertyDetailsActivity.this,
                    // MapFragmentActivity.class);
                    // startActivity(inte);
                    // overridePendingTransition(R.anim.slide_in_right,
                    // R.anim.slide_out_left);
                    finish();
                    AppConstants.push_notification_call = "false";
                } else {
                    if (mCallFrom != null && mCallFrom.equals("Listing")) {
                        launchActivity(ListingActivity.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (mCallFrom != null && mCallFrom.equals("profile")) {
                        launchActivity(ProfileScreen.class);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else if (mCallFrom != null && mCallFrom.equals("GCM")) {
                        // onBackPressed();
                        callMapScreen();
                    } else if (mCallFrom != null && mCallFrom.equals("Notification")) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        // onBackPressed();
                        callMapScreen();
                    }
                }

            }
        });

    }

    private void initializeMap(final double lat, final double lng) {

        if (googleMap != null) {

            //googleMap = mMapFragment.getMap();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            if (googleMap != null) {
                if (!isFir) {
                    isFir = true;

                    marker_view_shown = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.map_marker_blue, null);

                    numTxt = (TextView) marker_view_shown.findViewById(R.id.num_txt);
                    mMarkerImg = (ImageView) marker_view_shown.findViewById(R.id.marker);
                    prop_typ_icon = (Button) marker_view_shown.findViewById(R.id.property_type_icon);

                    if (mPropertyTyp.equals("exclusive")) {
                        prop_typ_icon.setVisibility(View.VISIBLE);
                        prop_typ_icon.setBackgroundResource(R.drawable.exclusives_icon);
                    } else if (mPropertyTyp.equals("openhouse")) {
                        prop_typ_icon.setVisibility(View.VISIBLE);
                        prop_typ_icon.setBackgroundResource(R.drawable.open_houses_icon);
                    } else if (mPropertyTyp.equals("favourite")) {
                        prop_typ_icon.setVisibility(View.VISIBLE);
                        prop_typ_icon.setBackgroundResource(R.drawable.favourite_disable);
                    } else {
                        prop_typ_icon.setVisibility(View.GONE);
                    }
                    if (Amountproperty.equalsIgnoreCase("0")) {

                    } else {
                        viewamount();
                    }

                    LatLng latLng = new LatLng(lat, lng);
                    marker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                            .fromBitmap(createDrawableFromView(PropertyDetailsActivity.this, marker_view_shown))));

                    LatLng currentPos = new LatLng(lat, lng);
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPos, 11.0f);
                    googleMap.animateCamera(yourLocation);

                }
            }
        }
    }

    private void viewamount() {
        System.out.println(NumberFormat.getIntegerInstance().format(Integer.parseInt(Amountproperty)));
        String amount = NumberFormat.getIntegerInstance().format(Integer.parseInt(Amountproperty));
        int sizes = Integer.parseInt(Amountproperty);
        if (sizes <= 2) {
            mSplitedAmount = amount.substring(0, amount.lastIndexOf(","));
            numTxt.setText("$" + mSplitedAmount);
        } else {
            numTxt.setText("$" + amount);
        }
    }

    private void setDetails() {
        String am = "";
        String amount_ex = "";
        amount_ex = mPropertyDetailsResponse.get(0).getPrice_range();
        am = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_ex));
        Integer validate = Integer.parseInt(mPropertyDetailsResponse.get(0).getPrice_range());
        if (validate <= 2) {
            amnt_txt.setText("$" + amount_ex + " / mo");
        } else {
            amnt_txt.setText("$" + am + " / mo");
        }
        photos_count_txt.setText("1/" + mPropertyDetailsResponse.get(0).getProperty_pics().size());
        location_txt.setText(mPropertyDetailsResponse.get(0).getAddress());
        proprty_type.setText("For " + mPropertyDetailsResponse.get(0).getType());
        property_types.setText(mPropertyDetailsResponse.get(0).getProperty_type());
        square_footage.setText(mPropertyDetailsResponse.get(0).getSquare_footage() + " " + "Sqft");
        beds.setText(mPropertyDetailsResponse.get(0).getBeds() + " " + "Bedrooms");
        bathsrooms.setText(mPropertyDetailsResponse.get(0).getBaths() + " " + "Bathrooms");
        build_year.setText(mPropertyDetailsResponse.get(0).getBuild_year() + " " + "Build Year");
        detail_txt.setText(mPropertyDetailsResponse.get(0).getDescription());
        detail_txt.setVisibility(View.VISIBLE);
        detail_text_full.setText(mPropertyDetailsResponse.get(0).getDescription());
        if (mPropertyDetailsResponse.get(0).getDescription().equals("")) {
            mDescripLay.setVisibility(View.GONE);
        } else {
            mDescripLay.setVisibility(View.VISIBLE);
        }

        if (mPropertyPostedUserDetailsResponse.size() != 0) {

            mRequestBusinessName.setText(mPropertyPostedUserDetailsResponse.get(0).getFirst_name() + " "
                    + mPropertyPostedUserDetailsResponse.get(0).getLast_name());
            if (mPropertyPostedUserDetailsResponse.get(0).getRb_user().equalsIgnoreCase("1")) {
                mRequestUserName.setText(mPropertyPostedUserDetailsResponse.get(0).getEmail_id());
                mRequestUserName.setTextColor(getResources().getColor(R.color.blue_color));
                mRequestUserName.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", mPropertyPostedUserDetailsResponse.get(0).getEmail_id(), null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                });
            } else {
//				mRequestUserName.setText(mPropertyPostedUserDetailsResponse.get(0).getFirst_name());
                mRequestUserName.setText("N/A");
            }
            if (mPropertyPostedUserDetailsResponse.get(0).getBusiness_name() == null || mPropertyPostedUserDetailsResponse.get(0).getBusiness_name().equalsIgnoreCase("")) {
                mRequestPhone.setText("N/A");
            } else {
                mRequestPhone.setText(mPropertyPostedUserDetailsResponse.get(0).getBusiness_name());

            }
//			mRequestAddress.setText("Hi, I found this listing in Renter's Block and would like to know more about "
//					+ mPropertyDetailsResponse.get(0).getProperty_name() + ", "
//					+ mPropertyDetailsResponse.get(0).getAddress() + ". Thank You");

            if (mPropertyPostedUserDetailsResponse.get(0).getPhone() == null || mPropertyPostedUserDetailsResponse.get(0).getPhone().equalsIgnoreCase("")) {
                mRequestAddress.setText("N/A");
            } else {
                mRequestAddress.setText(mPropertyPostedUserDetailsResponse.get(0).getPhone());
                mRequestAddress.setTextColor(getResources().getColor(R.color.blue_color));
                mRequestAddress.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                                mPropertyPostedUserDetailsResponse.get(0).getPhone(), null)));
                    }
                });
            }

            if (mPropertyDetailsResponse.get(0).getUser_id().equalsIgnoreCase(UserID)) {
                chat_icon.setVisibility(View.GONE);
            } else {
                chat_icon.setVisibility(View.VISIBLE);
            }

            if (mPropertyDetailsResponse.get(0).getProperty_posted_user_details().get(0).getIs_request_already_sent().equalsIgnoreCase("1")) {
                mContactInfoLay.setVisibility(View.VISIBLE);
                mReport_Send.setVisibility(View.VISIBLE);
                mReport_Info.setVisibility(View.INVISIBLE);
            } else {
                mContactInfoLay.setVisibility(View.GONE);
                mReport_Send.setVisibility(View.INVISIBLE);
                mReport_Info.setVisibility(View.VISIBLE);
            }
            if (mPropertyPostedUserDetailsResponse.get(0).getPhone().equals("")) {
                mCallIcon.setVisibility(View.GONE);
            } else {
                mCallIcon.setVisibility(View.VISIBLE);
            }
            aq().id(R.id.request_user_img).image(mPropertyPostedUserDetailsResponse.get(0).getUser_pic(), true, true, 0,
                    R.drawable.profile_pic, null, 0, 1.0f);
        }

        if (mPropertyDetailsResponse.get(0).getDescription().equals("")) {
            mDescripLay.setVisibility(View.GONE);
        } else {
            mDescripLay.setVisibility(View.VISIBLE);
        }

        if (mPropertyDetailsResponse.get(0).getIsfavourite().equals("0")) {
            mFav.setBackgroundResource(R.drawable.favourite_enable);
            isfavourite = true;
        } else {
            mFav.setBackgroundResource(R.drawable.favourites_icon);
            isfavourite = false;
        }
        if (mPropertyDetailsResponse.get(0).getIsmymessageboard().equals("0")) {
            mMsgAddBtn.setVisibility(View.VISIBLE);
            if (mPropertyDetailsResponse.get(0).getMy_message_board().size() == 0) {
                mMsgAddBtn.setVisibility(View.GONE);
                mAddToMyBoardsBig.setVisibility(View.VISIBLE);
                mMsgBoardLay.setVisibility(View.GONE);
            } else {
                mAddToMyBoardsBig.setVisibility(View.GONE);
                mMsgAddBtn.setVisibility(View.VISIBLE);
                mMsgAddBtn.setBackgroundResource(R.drawable.minus_icon);

                mMsgBoardLay.setVisibility(View.VISIBLE);
                mMsgAddress.setText(mPropertyDetailsResponse.get(0).getAddress());
                mMsgReviews.setText(
                        " (" + mPropertyDetailsResponse.get(0).getMy_message_board().get(0).getReview_count() + ")");

                // ImageOptions options = new ImageOptions();

                aq().id(R.id.msg_img).image(mPropertyPicturesResponse.get(0).getFile(), true, true, 0,
                        R.drawable.default_prop_icon, null, 0, 1.0f);

                float rating = Float
                        .parseFloat(mPropertyDetailsResponse.get(0).getMy_message_board().get(0).getProperty_rating());
                mMsgRatingBar.setRating(rating);
            }
        } else {
            mAddToMyBoardsBig.setVisibility(View.GONE);
            mMsgAddBtn.setVisibility(View.VISIBLE);
            mMsgAddBtn.setBackgroundResource(R.drawable.minus_icon);

            mMsgBoardLay.setVisibility(View.VISIBLE);
            mMsgAddress.setText(mPropertyDetailsResponse.get(0).getAddress());
            mMsgReviews.setText(
                    " (" + mPropertyDetailsResponse.get(0).getMy_message_board().get(0).getReview_count() + ")");
            if (mPropertyPicturesResponse.size() != 0) {
                aq().id(R.id.msg_img).image(mPropertyPicturesResponse.get(0).getFile(), true, true, 0,
                        R.drawable.default_prop_icon, null, 0, 1.0f);
            } else {
                aq().id(R.id.msg_img).image(R.drawable.default_prop_icon);
            }

            float rating = Float
                    .parseFloat(mPropertyDetailsResponse.get(0).getMy_message_board().get(0).getProperty_rating());
            mMsgRatingBar.setRating(rating);
        }
        showPhotosVideos(mPropertyDetailsResponse.get(0).getUserpropertypic());
        if (mPropertyReviewResponse.size() != 0) {
            showRecommendedReviews(mPropertyReviewResponse);
        }
        if (mPropertyPostedUserDetailsResponse.get(0).getRb_user() != null
                && mPropertyPostedUserDetailsResponse.get(0).getRb_user().equalsIgnoreCase("1")) {
            mCallIcon.setVisibility(View.VISIBLE);
        } else {
            mCallIcon.setVisibility(View.GONE);
        }
    }


    private void showPhotosVideos(final ArrayList<UserPropertyPics> mUserPropertyPicsList) {
        user_image_lay.removeAllViews();
        for (int i = 0; i < mUserPropertyPicsList.size(); i++) {
            final UserPropertyPics mUserPropertyPics = mUserPropertyPicsList.get(i);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View convertView = inflater.inflate(R.layout.adapter_photos_videos, null);

            AQuery aq = new AQuery(this).recycle(convertView);

            user_image = (ImageView) convertView.findViewById(R.id.user_img_view);
            user_video = (VideoView) convertView.findViewById(R.id.video_view);
            user_progress = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            vid_progress = (ProgressBar) convertView.findViewById(R.id.video_progress_bar);
            vid_play_btn = (Button) convertView.findViewById(R.id.video_play_btn);
            vid_lay = (RelativeLayout) convertView.findViewById(R.id.video_lay);

            vid_lay.setTag(i);
            user_image.setTag(i);

            mVideoFrameLay = (FrameLayout) convertView.findViewById(R.id.adapter_frame_lay);
            if (mUserPropertyPicsList.get(i).getFile_type().equals("image")) {
                user_image.setVisibility(View.VISIBLE);
                user_video.setVisibility(View.GONE);
                vid_progress.setVisibility(View.GONE);
                vid_lay.setVisibility(View.GONE);

                aq.id(R.id.user_img_view).progress(R.id.progress_bar).image(mUserPropertyPics.getFile(), true, true, 0,
                        R.drawable.profile_pic, null, 0, 1.0f);
            } else {
                user_image.setVisibility(View.GONE);
                user_video.setVisibility(View.VISIBLE);
                vid_progress.setVisibility(View.VISIBLE);
                vid_lay.setVisibility(View.VISIBLE);
                user_progress.setVisibility(View.GONE);
                String link = mUserPropertyPics.getFile();
                Uri video = Uri.parse(link);
                user_video.setVideoURI(video);
                vid_progress.setVisibility(View.GONE);
                vid_play_btn.setVisibility(View.VISIBLE);

            }
            user_video.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    user_video.seekTo(100);
                    user_progress.setVisibility(View.GONE);
                    vid_play_btn.setVisibility(View.VISIBLE);
                    vid_progress.setVisibility(View.GONE);
                }
            });
            user_video.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return true;
                }
            });
            vid_lay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));

                    // video_url = imgUrl;
                    // showCustomDialog(video_url, "video");
                    // showImageFullView(pos);

                }
            });
            user_image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(String.valueOf(v.getTag()));
                    // video_url = imgUrl;
                    // showCustomDialog(video_url, "image");
                    // showImageFullView(pos);
                }
            });
            user_image_lay.addView(convertView);
        }
    }

    private void showRecommendedReviews(final ArrayList<PropertyReview> mPropertyReviewResponse) {
        mReviewLay.removeAllViews();
        if (mPropertyReviewResponse.size() < 5) {
            view_all.setVisibility(View.GONE);
            for (int i = 0; i < mPropertyReviewResponse.size(); i++) {

                final PropertyReview mPropertyReview = mPropertyReviewResponse.get(i);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View convertView = inflater.inflate(R.layout.adapter_recommended_reviews, null);

                AQuery aq = new AQuery(this).recycle(convertView);

                review_user_name = (TextView) convertView.findViewById(R.id.review_user_name);
                recommand_review_comment = (TextView) convertView.findViewById(R.id.recommand_review_comment);
                recommand_review_time = (TextView) convertView.findViewById(R.id.recommand_review_time);
                recommand_revie_rating = (RatingBar) convertView.findViewById(R.id.recommand_revie_rating);
                aq.id(R.id.user_img_view).image(mPropertyReview.getReview_user_details().get(0).getUser_pic(), true,
                        true, 0, R.drawable.profile_pic, null, 0, 1.0f);

                mRecommend_lay = (RelativeLayout) convertView.findViewById(R.id.recommend_lay1);
                mRecommend_lay.setTag(i);
                mRecommend_lay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        showReviewDialog(mPropertyReviewResponse.get(pos), pos);
                    }
                });

                float rating = Float.parseFloat(mPropertyReview.getRating());
                recommand_revie_rating.setRating(rating);
                review_user_name.setText(mPropertyReview.getReview_user_details().get(0).getUser_name());
                recommand_review_comment.setText(mPropertyReview.getComments());
                recommand_review_time.setText(GlobalMethods.timeDiff(mPropertyReview.getDate_time()));

                Button chat_icon = (Button) convertView.findViewById(R.id.chat_icon_adapter);
                chat_icon.setTag(i);

                if (mPropertyReviewResponse.get(i).getReview_user_id().equalsIgnoreCase(UserID)) {
                    chat_icon.setVisibility(View.GONE);
                } else {
                    chat_icon.setVisibility(View.VISIBLE);
                }

                chat_icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                            moveToLogin();
                        } else {
                            callGroupIdService(UserID,
                                    mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_id(), pos);
                        }

                    }
                });

                mReviewLay.addView(convertView);
            }
        } else {
            view_all.setVisibility(View.VISIBLE);
            for (int i = 0; i < 5; i++) {

                final PropertyReview mPropertyReview = mPropertyReviewResponse.get(i);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View convertView = inflater.inflate(R.layout.adapter_recommended_reviews, null);

                AQuery aq = new AQuery(this).recycle(convertView);

                review_user_name = (TextView) convertView.findViewById(R.id.review_user_name);
                recommand_review_comment = (TextView) convertView.findViewById(R.id.recommand_review_comment);
                recommand_review_time = (TextView) convertView.findViewById(R.id.recommand_review_time);
                recommand_revie_rating = (RatingBar) convertView.findViewById(R.id.recommand_revie_rating);
                mRecommend_lay = (RelativeLayout) convertView.findViewById(R.id.recommend_lay1);
                mRecommend_lay.setTag(i);
                mRecommend_lay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        showReviewDialog(mPropertyReviewResponse.get(pos), pos);
                    }
                });

                aq.id(R.id.user_img_view).image(mPropertyReview.getReview_user_details().get(0).getUser_pic(), true,
                        true, 0, R.drawable.profile_pic, null, 0, 1.0f);

                float rating = Float.parseFloat(mPropertyReview.getRating());
                recommand_revie_rating.setRating(rating);
                review_user_name.setText(mPropertyReview.getReview_user_details().get(0).getUser_name());
                recommand_review_comment.setText(mPropertyReview.getComments());
                recommand_review_time.setText(GlobalMethods.timeDiff(mPropertyReview.getDate_time()));

                Button chat_icon = (Button) convertView.findViewById(R.id.chat_icon_adapter);
                chat_icon.setTag(i);

                if (mPropertyReviewResponse.get(i).getReview_user_id().equalsIgnoreCase(UserID)) {
                    chat_icon.setVisibility(View.GONE);
                } else {
                    chat_icon.setVisibility(View.VISIBLE);
                }
                chat_icon.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(String.valueOf(v.getTag()));
                        if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                            moveToLogin();
                        } else {
                            callGroupIdService(UserID,
                                    mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_id(), pos);
                        }
                    }
                });

                mReviewLay.addView(convertView);
            }
        }

    }

    private void showReviewDialog(final PropertyReview propertyReview, final int pos) {
        TextView dialog_review_user_name, dialog_recommand_review_comment, dialog_recommand_review_time, mHeader_txt;
        RatingBar dialog_recommand_revie_rating;
        ImageView dialog_recommand_review_image;
        Button dialog_chat_icon_adapter;

        rev_dialog = new Dialog(PropertyDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        rev_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rev_dialog.setContentView(R.layout.dialog_recommended_review);
        rev_dialog.setCancelable(true);

        dialog_review_user_name = (TextView) rev_dialog.findViewById(R.id.review_user_name);
        dialog_review_user_name.setTypeface(helvetica_bold);
        dialog_review_user_name.setText(propertyReview.getReview_user_details().get(0).getUser_name());

        dialog_recommand_review_comment = (TextView) rev_dialog.findViewById(R.id.recommand_review_comment);
        dialog_recommand_review_comment.setTypeface(helvetica_normal);
        dialog_recommand_review_comment.setText(propertyReview.getComments());

        dialog_recommand_review_time = (TextView) rev_dialog.findViewById(R.id.recommand_review_time);
        dialog_recommand_review_time.setTypeface(helvetica_normal);
        dialog_recommand_review_time.setText(GlobalMethods.timeDiff(propertyReview.getDate_time()));

        mHeader_txt = (TextView) rev_dialog.findViewById(R.id.header_txt);
        mHeader_txt.setTypeface(helvetica_bold);
        mHeader_txt.setText(propertyReview.getReview_user_details().get(0).getUser_name() + " Review");

        dialog_recommand_revie_rating = (RatingBar) rev_dialog.findViewById(R.id.recommand_revie_rating);
        float rating = Float.parseFloat(propertyReview.getRating());
        dialog_recommand_revie_rating.setRating(rating);

        dialog_chat_icon_adapter = (Button) rev_dialog.findViewById(R.id.chat_icon_adapter);

        LinearLayout dialog_back_icon = (LinearLayout) rev_dialog.findViewById(R.id.back_icon);

        dialog_back_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                rev_dialog.dismiss();
            }
        });

        if (propertyReview.getReview_user_id().equalsIgnoreCase(UserID)) {
            dialog_chat_icon_adapter.setVisibility(View.GONE);
        } else {
            dialog_chat_icon_adapter.setVisibility(View.VISIBLE);
        }

        dialog_chat_icon_adapter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PropertyDetailsActivity.adapterChatService(UserID,
                        propertyReview.getReview_user_details().get(pos).getUser_id(), pos);
                // mPropertyReviewList.get(pos).getReview_user_details()
                // .get(0).getUser_id(), pos);
            }
        });

        rev_dialog.show();

    }

    private void showAllReviewsDialog(Context context, ArrayList<PropertyReview> mPropertyReviewList) {

        mReviewDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        mReviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mReviewDialog.setCancelable(false);
        mReviewDialog.setContentView(R.layout.dialog_recommended_reviews);
        LinearLayout mClose_Icon = (LinearLayout) mReviewDialog.findViewById(R.id.close_icon);

        ListView mReviewsList = (ListView) mReviewDialog.findViewById(R.id.reviews_list);

        mReviewsAdapter = new RecommendedReviewsAdapter(context, mPropertyReviewList);
        mReviewsList.setAdapter(mReviewsAdapter);

        mClose_Icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mReviewDialog.dismiss();
            }
        });
        mReviewDialog.show();
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

    private static final float BITMAP_SCALE = 0.3f;
    private static final float BLUR_RADIUS = 20.5f;

    private Bitmap BlurImage(Bitmap input) {

        Bitmap outBitmap = input.copy(input.getConfig(), true);
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            RenderScript rs = RenderScript.create(this);
            Allocation alloc = Allocation.createFromBitmap(rs, input, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, alloc.getType());

            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(4f);
            script.setInput(alloc);
            script.forEach(output);
            output.copyTo(outBitmap);
            mPropertyBlurImg.setImageBitmap(outBitmap);
        }
        return outBitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
        // Intent inte = new Intent(PropertyDetailsActivity.this,
        // MapFragmentActivity.class);
        // startActivity(inte);
        // overridePendingTransition(R.anim.slide_in_right,
        // R.anim.slide_out_left);
        finish();
        AppConstants.push_notification_call = "false";
        // } else {
        // AppConstants.CALL_MAP = "true";
        // launchActivity(MapFragmentActivity.class);
        // overridePendingTransition(R.anim.slide_in_right,
        // R.anim.slide_out_left);
        // }

    }

    private void callMapScreen() {
        // Intent inte = new Intent(PropertyDetailsActivity.this,
        // MapFragmentActivity.class);
        // startActivity(inte);
        // overridePendingTransition(R.anim.slide_in_right,
        // R.anim.slide_out_left);
        finish();
        AppConstants.push_notification_call = "false";
    }

    @Override
    protected void onDestroy() {
        // handler.removeCallbacks(Runn);
        finish();
        super.onDestroy();
    }

    private void callGroupsIdService() {
        String Url = AppConstants.BASE_URL + "group";
        GsonTransformer t = new GsonTransformer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("owner_id", UserID);
        params.put("users_id", mPropertyDetailsResponse.get(0).getProperty_posted_user_details().get(0).getUser_id());
        params.put("name", "");
        aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
                            // onSuccessRequest(obj);
                            String is_friend = "0";
                            if (obj.msg.equalsIgnoreCase("A group with these members already exists")) {
                                is_friend = "1";
                            }
                            callChatsService(obj.result, is_friend);
                        } else {
                            statusErrorCode(status);
                        }
                    }
                });
    }

    private void callChatsService(String group_id, final String is_friend) {
        Intent intent = new Intent(PropertyDetailsActivity.this, FriendChatScreen.class);
        intent.putExtra("ids", mPropertyDetailsResponse.get(0).getProperty_posted_user_details().get(0).getUser_id());
        intent.putExtra("names",
                mPropertyDetailsResponse.get(0).getProperty_posted_user_details().get(0).getUser_name());
        intent.putExtra("groupId", group_id);
        intent.putExtra("type", "group");
        intent.putExtra("enhanced_profile_ids",
                mPropertyDetailsResponse.get(0).getProperty_posted_user_details().get(0).getEnhanced_profile());
        if (is_friend.equalsIgnoreCase("1")) {
            intent.putExtra("from_call", "");
        } else {
            intent.putExtra("from_call", "hotleads");
        }
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_icon:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    callGroupsIdService();
                }
                break;
            case R.id.add_msg_board_btn:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    callAddMyBoard();
                }
                break;
            case R.id.add_to_my_board_big:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    callAddMyBoard();
                }
                break;
            case R.id.property_share:
                // Intent sharingIntent = new Intent(
                // android.content.Intent.ACTION_SEND);
                // startActivity(Intent.createChooser(sharingIntent, "Share via"));
//			String bodys = "Here's the property ID - " + mPropertyId + " & address - "
//					+ mPropertyDetailsResponse.get(0).getAddress()
//					+ " for a MUST see property. DL the app now goo.gl/DGDC1v";

                String address = mPropertyDetailsResponse.get(0).getAddress();
                AppConstants.prop_det_share_address = address;

                String bodys = mPropertyDetailsResponse.get(0).getProperty_id();
                AppConstants.Prop_det_share_text = bodys;
                Intent inte = new Intent(PropertyDetailsActivity.this, ShareAppActivity.class);
                inte.putExtra("call_from", "prop_detail");
                startActivity(inte);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.friend_icon:
                if (UserID.equalsIgnoreCase(mPropertyDetailsResponse.get(0).getUser_id())) {
                    DialogManager.showCustomAlertDialog(PropertyDetailsActivity.this, PropertyDetailsActivity.this,
                            "You cannot send Request Info for your own property.");
                } else if (UserID.equalsIgnoreCase("0") || UserID.equalsIgnoreCase("")) {
                    moveToLogin();
                } else {
                    callFriendRequestService();
                }
                break;
            case R.id.view_all:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    showAllReviewsDialog(PropertyDetailsActivity.this, mPropertyReviewResponse);
                }
                break;
            case R.id.more_txt:
                if (!isClicked) {
                    more_txt.setText("Less");
                    detail_text_full.setVisibility(View.VISIBLE);
                    detail_txt.setVisibility(View.GONE);
                    isClicked = true;
                } else {
                    more_txt.setText("...More");
                    detail_text_full.setVisibility(View.GONE);
                    detail_txt.setVisibility(View.VISIBLE);
                    isClicked = false;
                }
                break;
            case R.id.loc_img:
                Intent in = new Intent(PropertyDetailsActivity.this, PropertyMapView.class);
                in.putExtra("PropertyLatitude", mLat);
                in.putExtra("PropertyLongtitude", mLong);
                in.putExtra("Propertymap", "Propertymap");
                in.putExtra("PropertyId", mPropertyId);
                in.putExtra("PropertyAmount", Amountproperty);
                in.putExtra("isFavourite", String.valueOf(isfavourite));
                in.putExtra("PropertyTyp", mPropertyTyp);
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.favourites:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    if (isfavourite) {
                        callADDFavouriteService();
                        mFav.setBackgroundResource(R.drawable.favourites_icon);
                        isfavourite = false;
                    } else {
                        callADDFavouriteService();
                        mFav.setBackgroundResource(R.drawable.favourite_enable);
                        isfavourite = true;
                    }
                }
                break;

            case R.id.review_button:
                if (UserID.equalsIgnoreCase("0") || UserID.equalsIgnoreCase("")) {
                    moveToLogin();
                } else {
                    Intent intent = new Intent(PropertyDetailsActivity.this, WriteReviewActivity.class);
                    intent.putExtra("ReviewType", "Post");
                    intent.putExtra("Rating", "0.0");
                    intent.putExtra("Comments", "");
                    intent.putExtra("PropertyId", mPropertyId);
                    intent.putExtra("PropertyReviewId", mPropertyReviewId);
                    intent.putExtra("PropType", mPropertyTyp);
                    AppConstants.fromPropert = true;
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
            case R.id.request_button:
                mScrollView.fullScroll(View.FOCUS_DOWN);
                break;
            case R.id.request_info:
                if (UserID.equalsIgnoreCase(mPropertyDetailsResponse.get(0).getUser_id())) {
                    DialogManager.showCustomAlertDialog(PropertyDetailsActivity.this, PropertyDetailsActivity.this,
                            "You cannot send Request Info for your own property.");
                } else if (UserID.equalsIgnoreCase("0") || UserID.equalsIgnoreCase("")) {
                    moveToLogin();
                } else if (mResponse != null && mResponse.getResult() != null && mResponse.getResult().size() > 0) {
                    if (mResponse.getResult().get(0).getProperty_posted_user_details().get(0).getRb_user()
                            .equalsIgnoreCase("1")) {
                        callRequestInfoService();
                    } else {
                        String body = "<HTML><Body>Hi, I saw your available "
                                + "listings on Renter's Block and would like to learn more about "
                                + "a specific listing. Please <a href=\"goo.gl/DGDC1v\">LOGIN!</a> "
                                + "and feel free to message me through the app or website. "
                                + "Looking forward to connecting with you!</BODY></HTML>";

                        // String body = "Here's the property ID - " + mPropertyId +
                        // " & address - "
                        // + mPropertyDetailsResponse.get(0).getAddress()
                        // + " for a MUST see property. DL the app now
                        // goo.gl/DGDC1v";

                        // Here's the property ID - (insert property ID) & address -
                        // (insert property address)
                        // for a MUST see property. DL the app now
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{
                                mResponse.getResult().get(0).getProperty_posted_user_details().get(0).getEmail_id()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Hot Lead from Renter's Block for - "
                                + mResponse.getResult().get(0).getProperty_name());
                        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
                        emailIntent.setType("text/plain");
                        startActivity(emailIntent);
                    }
                }
                break;
            case R.id.add_button:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    showOptionDialog(this);
                }
                break;
            case R.id.seemore_button:
                if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
                    moveToLogin();
                } else {
                    Intent inte1 = new Intent(PropertyDetailsActivity.this, UserPropertyImagesandVideos.class);
                    for_image_act_property_id = mPropertyId;
                    inte1.putExtra("user_prop", mPropertyDetailsResponse.get(0).getUserpropertypic());
                    inte1.putExtra("PropertyId", mPropertyId);
                    inte1.putExtra("PropType", mPropertyTyp);
                    startActivity(inte1);
                }
                break;
            case R.id.report_fraud:
//			String property_id = "Property Id:" + mPropertyId + "\n\n";
//			String property_add = "Property Address:" + mPropertyDetailsResponse.get(0).getAddress();
//			Intent email = new Intent(Intent.ACTION_SEND);
//			email.putExtra(Intent.EXTRA_EMAIL, new String[] { "admin@rentersblock.com" });
//			email.putExtra(Intent.EXTRA_SUBJECT, "Report Fraud about the Property");
//			email.putExtra(Intent.EXTRA_TEXT, property_id + property_add);
//			email.setType("message/rfc822");
//			startActivity(Intent.createChooser(email, "Choose an Email client :"));

                onReportFraudDialog();
                break;

        }
    }

    private void onReportFraudDialog() {
        mReportFraudDialog = new Dialog(PropertyDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        mReportFraudDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mReportFraudDialog.setContentView(R.layout.popup_report_fraud);
        mReportFraudDialog.setCancelable(true);

        Button mYesClick = (Button) mReportFraudDialog.findViewById(R.id.yes_report_fraud);
        Button mNoClick = (Button) mReportFraudDialog.findViewById(R.id.no_report_fraud);

        mNoClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mReportFraudDialog.dismiss();
            }
        });
        mYesClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mReportFraudDialog.dismiss();
                sendReportFraud();
            }
        });

        mReportFraudDialog.show();
    }

    private void sendReportFraud() {
        String property_id = "Property Id:" + mPropertyId + "\n\n";
        String property_add = "Property Address:" + mPropertyDetailsResponse.get(0).getAddress();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@rentersblock.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Report Fraud about the Property");
        email.putExtra(Intent.EXTRA_TEXT, property_id + property_add);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void callFriendRequestService() {
        String agent_user_id = "";
        String Url = AppConstants.BASE_URL + "friend/sendfrindrequest";

        GsonTransformer t = new GsonTransformer();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserID);
        try {
            agent_user_id = mResponse.getResult().get(0).getProperty_posted_user_details().get(0).getUser_id();
        } catch (Exception e) {
            agent_user_id = "1";
        }
        params.put("friend_user_id",
                mResponse.getResult().get(0).getProperty_posted_user_details().get(0).getUser_id());
        aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            friend_icon.setVisibility(View.GONE);
                            DialogManager.showCustomAlertDialog(PropertyDetailsActivity.this,
                                    PropertyDetailsActivity.this, "Friend Request Sent Successfully");
                        } else {
                            statusErrorCode(status);
                        }
                    }
                });
    }

    private void callRequestInfoService() {
        String Url = AppConstants.BASE_URL + "requestinfo";

        GsonTransformer t = new GsonTransformer();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", UserID);
        params.put("propertyId", mPropertyId);
        aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            FriendRequestResponse mResponse = new Gson().fromJson(json.toString(),
                                    FriendRequestResponse.class);

                            onRequest(mResponse);
                        } else {
                            statusErrorCode(status);
                        }
                    }
                });
    }

    private void onRequest(FriendRequestResponse mResponse) {

        getPropertyDetails();
        DialogManager.showCustomAlertDialog(this, this, mResponse.getMsg());
    }

    class VideoDetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mViewPager.getParent().requestDisallowInterceptTouchEvent(true);

        }

    }

    class DetailOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pager.getParent().requestDisallowInterceptTouchEvent(true);

        }

    }

    class DetailOnPageChange extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            photo_pager.getParent().requestDisallowInterceptTouchEvent(true);

        }

    }

    class PhotoPageAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        public ArrayList<UserPropertyPics> user_prop_pics = new ArrayList<UserPropertyPics>();
        Context context;

        public PhotoPageAdapter(ArrayList<UserPropertyPics> user_pics) {
            this.user_prop_pics = user_pics;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return user_prop_pics.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(View view, final int position) {

            final View photo_video_view = inflater.inflate(R.layout.photo_video_image_pager_adapter, null);

            profile_pic = (ScaleImageView) photo_video_view.findViewById(R.id.adap_img1);
            profile_video = (VideoView) photo_video_view.findViewById(R.id.adap_video1);
            adap_vidl1 = (RelativeLayout) photo_video_view.findViewById(R.id.adap_vidl1);
            image_prog = (ProgressBar) photo_video_view.findViewById(R.id.adap_progress1);
            video_prog = (ProgressBar) photo_video_view.findViewById(R.id.adap_vid_progress1);
            play_btn = (Button) photo_video_view.findViewById(R.id.adap_video_play_btn1);
            frame = (FrameLayout) photo_video_view.findViewById(R.id.main_frame);

            // RelativeLayout main_lay = (RelativeLayout) photo_video_view
            // .findViewById(R.id.main_layout);
            pause_btn = (Button) photo_video_view.findViewById(R.id.adap_video_pause_btn1);

            if (user_prop_pics.get(position).getFile_type().equals("image")) {
                image_prog.setVisibility(View.VISIBLE);
                profile_pic.setVisibility(View.VISIBLE);
                new ImageLoadTask().execute(user_prop_pics.get(position).getFile());
            } else {
                adap_vidl1.setVisibility(View.VISIBLE);
                profile_pic.setVisibility(View.GONE);
                image_prog.setVisibility(View.GONE);
                // main_lay.setBackgroundColor(Color.parseColor("#000000"));
                String link = user_prop_pics.get(position).getFile();
                Uri video = Uri.parse(link);
                profile_video.setVideoURI(video);
                profile_video.setOnPreparedListener(new OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        profile_video.start();
                        video_prog.setVisibility(View.GONE);
                    }
                });
                profile_video.setOnErrorListener(new OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        video_prog.setVisibility(View.GONE);
                        Toast.makeText(PropertyDetailsActivity.this, "Sorry! Video Cannot be Played.",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                profile_video.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!istouched) {
                            pause_btn.setVisibility(View.VISIBLE);
                            istouched = true;
                        } else {
                            pause_btn.setVisibility(View.GONE);
                            istouched = false;
                        }

                        return false;
                    }
                });

                pause_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        stopPosition = profile_video.getCurrentPosition();
                        play_btn.setVisibility(View.VISIBLE);
                        pause_btn.setVisibility(View.GONE);
                        profile_video.pause();
                    }
                });

                play_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pause_btn.setVisibility(View.GONE);
                        play_btn.setVisibility(View.GONE);
                        profile_video.seekTo(stopPosition);
                        profile_video.start();
                    }
                });

                profile_video.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_btn.setVisibility(View.VISIBLE);
                    }
                });
            }

            return photo_video_view;
        }

    }

    private void showImageFullView(int pos) {

        d2 = new Dialog(PropertyDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        d2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d2.setContentView(R.layout.photo_video_full_view);
        d2.setCancelable(true);

        mViewPager = (ExtendedViewPager) d2.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new VideoImagePagerAdapter(mPropertyDetailsResponse.get(0).getUserpropertypic()));
        list_ner = new VideoDetailOnPageChangeListener();
        mViewPager.setCurrentItem(pos);
        mViewPager.setOnPageChangeListener(list_ner);

        d2.show();
    }

    class VideoImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private ArrayList<UserPropertyPics> prop_pic;

        VideoImagePagerAdapter(ArrayList<UserPropertyPics> user_img) {
            inflater = getLayoutInflater();
            this.prop_pic = user_img;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ExtendedViewPager) container).removeView((View) object);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return prop_pic.size();
        }

        @Override
        public Object instantiateItem(View view, final int position) {

            final View imageLayout = inflater.inflate(R.layout.profile_photo_video_full_view, null);

            final VideoView profile_video;
            RelativeLayout adap_vidl1;
            final ProgressBar video_prog;

            profile_pics = (TouchImageView) imageLayout.findViewById(R.id.adap_img1);
            profile_video = (VideoView) imageLayout.findViewById(R.id.adap_video1);
            adap_vidl1 = (RelativeLayout) imageLayout.findViewById(R.id.adap_vidl1);
            image_prog = (ProgressBar) imageLayout.findViewById(R.id.adap_progress1);
            video_prog = (ProgressBar) imageLayout.findViewById(R.id.adap_vid_progress1);
            play_btn = (Button) imageLayout.findViewById(R.id.adap_video_play_btn1);
            frame = (FrameLayout) imageLayout.findViewById(R.id.main_frame);
            close_lay = (RelativeLayout) imageLayout.findViewById(R.id.close_dia);
            Button close_btn = (Button) imageLayout.findViewById(R.id.close_dialog);
            RelativeLayout main_lay = (RelativeLayout) imageLayout.findViewById(R.id.parent_layout);
            pause_btn = (Button) imageLayout.findViewById(R.id.adap_video_pause_btn1);
            close_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    d2.dismiss();
                }
            });

            close_lay.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    d2.dismiss();
                }
            });
            if (prop_pic.get(position).getFile_type().equals("image")) {
                // image_prog.setVisibility(View.VISIBLE);
                profile_pics.setVisibility(View.VISIBLE);
                String url = prop_pic.get(position).getFile();
                // new ImageLoadTask().execute(url);
                aq().id(profile_pics).progress(image_prog).image(url, true, true, 0, R.drawable.profile_pic, null, 0,
                        1.0f);

            } else {
                adap_vidl1.setVisibility(View.VISIBLE);
                profile_pics.setVisibility(View.GONE);
                image_prog.setVisibility(View.GONE);
                main_lay.setBackgroundColor(Color.parseColor("#000000"));
                String file = prop_pic.get(position).getFile();
                String link = file;
                Uri video = Uri.parse(link);
                profile_video.setVideoURI(video);
                profile_video.setOnPreparedListener(new OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        profile_video.setBackground(null);
                        profile_video.start();
                        video_prog.setVisibility(View.GONE);
                    }
                });

                profile_video.setOnErrorListener(new OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        video_prog.setVisibility(View.GONE);
                        Toast.makeText(PropertyDetailsActivity.this, "Sorry! Video Cannot be Played.",
                                Toast.LENGTH_SHORT).show();
                        // d2.dismiss();
                        return true;
                    }
                });

                profile_video.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View arg0, MotionEvent arg1) {

                        if (!istouched) {
                            pause_btn.setVisibility(View.VISIBLE);
                            istouched = true;
                        } else {
                            pause_btn.setVisibility(View.GONE);
                            istouched = false;
                        }
                        return false;
                    }
                });

                pause_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        stopPosition = profile_video.getCurrentPosition();
                        play_btn.setVisibility(View.VISIBLE);
                        pause_btn.setVisibility(View.GONE);
                        profile_video.pause();
                    }
                });

                play_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pause_btn.setVisibility(View.GONE);
                        play_btn.setVisibility(View.GONE);
                        profile_video.seekTo(stopPosition);
                        profile_video.start();
                    }
                });

                profile_video.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        play_btn.setVisibility(View.VISIBLE);
                    }
                });
            }
            ((ExtendedViewPager) view).addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }

    }

    private void getImageDrawable(String url) {
        new DrawableImageParsing().execute(url);
    }

    class DrawableImageParsing extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... params) {

            Drawable d = null;
            try {
                InputStream is = (InputStream) new URL(params[0]).getContent();
                d = Drawable.createFromStream(is, "src name");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(Drawable result) {

            super.onPostExecute(result);
            if (result != null) {
                img.setImageDrawable(result);
            }
        }

    }

    class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL urlConnection = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            image_prog.setVisibility(View.GONE);
            profile_pics.setImageBitmap(result);
        }

    }

    private void showOptionDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.image_selection);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        Button mPhoto, mVideo, mCancel;
        mPhoto = (Button) mDialog.findViewById(R.id.photo);
        mVideo = (Button) mDialog.findViewById(R.id.video);
        mCancel = (Button) mDialog.findViewById(R.id.cancel);
        mPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                cameraOption("pic");
            }
        });
        mVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                cameraOption("video");
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

    private void cameraOption(String option) {
        ProfileImageSelectionUtil.showOptionNew(this, option);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            if (resultCode == RESULT_OK) {

                if (requestCode == ProfileImageSelectionUtil.CAMERA
                        || requestCode == ProfileImageSelectionUtil.GALLERY) {

                    Bitmap image = ProfileImageSelectionUtil.getImage(data, this);

                    if (image != null) {
                        if (requestCode == ProfileImageSelectionUtil.CAMERA) {
                            if (ProfileImageSelectionUtil.isUriTrue) {
                                image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, data.getData(),
                                        image);
                            } else {
                                image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, image);
                            }
                        } else {

                            Uri selectedImage = data.getData();

                            image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, selectedImage, image);
                        }

                        mainImage = image;

                    } else {
                        DialogManager.showCustomAlertDialog(this, this, "Unsupported file format");
                    }

                    updatePropertyFile("image");

                } else if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO
                        || requestCode == ProfileImageSelectionUtil.VIDEO_GALLERY) {

                    if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO) {
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(getFilename().getAbsolutePath(), 70);
                    } else {
                        Uri selectedVideo = data.getData();
                        file = new File(convertMediaUriToPath(selectedVideo));
                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(convertMediaUriToPath(selectedVideo), 70);
                    }

                    updatePropertyFile("video");

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePropertyFile(String filetype) {
        long length = 0;
        String url = AppConstants.BASE_URL + "photovideo";
        GsonTransformer t = new GsonTransformer();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserID);
        params.put("property_id", mPropertyId);
        params.put("agent_or_broker", "0");
        params.put("file_type", filetype);
        params.put("description", "");
        if (filetype.equals("video")) {
            if (file != null && !file.equals("")) {

                try {
                    bFile = new byte[(int) file.length()];
                    fileInputStream = new FileInputStream(file);
                    fileInputStream.read(bFile);
                    fileInputStream.close();
                    length = file.length();
                    length = length / 1024;
                    params.put("data", bFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
            mainImage.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
            byte[] mainImageData = (oututStream).toByteArray();
            params.put("data", mainImageData);
        }
        if (filetype.equals("video") && length > 25360) {
            mDialog = DialogManager.showDialog(this, "Video Limit Exceeded. Max size is 25 MB.", getString(R.string.ok),
                    null, 0, 0);
        } else {
            aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
                    new AjaxCallback<JSONObject>() {

                        @Override
                        public void callback(String url, JSONObject json, AjaxStatus status) {

                            if (json != null) {
                                ShowPopuptimer();
                                getPropertyDetails();
                            } else {
                                statusErrorCode(status);
                            }

                        }
                    });
        }
    }

    protected String convertMediaUriToPath(Uri uri) {
        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    private File getFilename() {

        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "cameravideo.mp4");

        System.out.println("audio " + file);
        return file;
    }

    private void callADDFavouriteService() {
        String url = AppConstants.BASE_URL + "addtofavorite";
        GsonTransformer t = new GsonTransformer();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", UserID);
        params.put("property_id", mPropertyId);

        aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
                new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);

                            onSuccessFavResponse(mResponse);
                        } else {
                            statusErrorCode(status);
                        }
                    }
                });

    }

    protected void onSuccessFavResponse(CommonResponse mResponse) {
        if (mResponse.getResult().toString().equalsIgnoreCase("Property added to favorite")) {
            mPropertyDetailsResponse.get(0).setIsfavourite("1");
        } else if (mResponse.getResult().toString().equalsIgnoreCase("Property removed from favorite")) {
            mPropertyDetailsResponse.get(0).setIsfavourite("0");
        }
        if (isDonshow) {
            if (mResponse != null) {
                CommonResponse commonresponse = (CommonResponse) mResponse;
                if (commonresponse.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                }
            }
            isDonshow = true;
        }
    }

    private void showPropertDetailDialog(Context context, String amount_prop, String address_prop) {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.view_pager_image_adapter);
        progress = (ProgressBar) dialog.findViewById(R.id.progress_view);
        Button close = (Button) dialog.findViewById(R.id.close_btn);
        TextView amount = (TextView) dialog.findViewById(R.id.property_amount);
        Button req_btn = (Button) dialog.findViewById(R.id.prop_request_button);
        Button review_btn = (Button) dialog.findViewById(R.id.prop_review_button);

        TextView desc_txt = (TextView) dialog.findViewById(R.id.descs_txt);
        TextView time_txt = (TextView) dialog.findViewById(R.id.times_txt);

        // if (mResponse.getResult().get(0).getUserpropertypic().get(0)
        // .getDescription() != null) {

        desc_txt.setText(mResponse.getResult().get(0).getProperty_pics().get(0).getDescription());
        // }
        time_txt.setText("Last Updated " + GlobalMethods.timeDiff(mResponse.getResult().get(0).getProperty_datetime()));

        review_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (UserID.equalsIgnoreCase("0") || UserID.equalsIgnoreCase("")) {
                    Intent intent = new Intent(PropertyDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Intent intent = new Intent(PropertyDetailsActivity.this, WriteReviewActivity.class);
                    intent.putExtra("ReviewType", "Post");
                    intent.putExtra("Rating", "0.0");
                    intent.putExtra("Comments", "");
                    intent.putExtra("PropertyId", mPropertyId);
                    intent.putExtra("PropertyReviewId", mPropertyReviewId);
                    AppConstants.fromPropert = true;
                    startActivity(intent);
                }

            }
        });

        req_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        TextView address = (TextView) dialog.findViewById(R.id.property_address);

        number_of_images = (TextView) dialog.findViewById(R.id.number_of_images);

        amount.setText("$ " + amount_prop + "/mo");
        address.setText(address_prop);

        imageUrls = new ArrayList<String>();
        for (int i = 0; i < mResponse.getResult().get(0).getProperty_pics().size(); i++) {
            imageUrls.add(mResponse.getResult().get(0).getProperty_pics().get(i).getFile());
        }

        pager = (DirectionalViewPager) dialog.findViewById(R.id.pager);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher).cacheOnDisc()
                .cacheInMemory().imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

        bitmap = new Bitmap[6];
        number_of_images.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        pager.setAdapter(new ImagePagerAdapter(imageUrls));
        pager.setOffscreenPageLimit(1);
        listener = new DetailOnPageChangeListener();
        pager.setOnPageChangeListener(listener);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        pager.getLayoutParams().height = (int) (dm.heightPixels - ((dm.heightPixels * 2) / 3));

        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    class ImagePagerAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private ArrayList<String> images;

        ImagePagerAdapter(ArrayList<String> imageUrls) {
            this.images = imageUrls;
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
        public Object instantiateItem(View view, int position) {

            final View imageLayout = inflater.inflate(R.layout.image_item_pager, null);
            final TouchImageView imageView = (TouchImageView) imageLayout.findViewById(R.id.image_mask);
            new DownloadImageTask(imageView).execute(images.get(position));
            if (position == 0) {
                number_of_images.setText("1" + " of " + images.size());
            } else {
                number_of_images.setText(position + " of " + images.size());
            }

            ((DirectionalViewPager) view).addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        TouchImageView bmImage;

        public DownloadImageTask(TouchImageView bmsImage) {
            this.bmImage = bmsImage;
        }

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            progress.setVisibility(View.GONE);
        }
    }

    public static void adapterChatService(String UserID, String Friend_UserID, int pos) {
        ((PropertyDetailsActivity) mContext).callGroupIdService(UserID, Friend_UserID, pos);
    }

    private void callGroupIdService(String UserID, String Friend_UserID, final int pos) {
        String Url = AppConstants.BASE_URL + "group";
        GsonTransformer t = new GsonTransformer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("owner_id", UserID);
        params.put("users_id", Friend_UserID);
        params.put("name", "");
        aq().transformer(t).progress(DialogManager.getProgressDialog(PropertyDetailsActivity.this)).ajax(Url, params,
                JSONObject.class, new AjaxCallback<JSONObject>() {

                    @Override
                    public void callback(String url, JSONObject json, AjaxStatus status) {

                        if (json != null) {
                            ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
                            String is_friend = "0";
                            if (obj.msg.equalsIgnoreCase("A group with these members already exists")) {
                                is_friend = "1";
                            }
                            callChatService(obj.result, pos, is_friend);
                        } else {
                            // statusErrorCode(status);
                        }
                    }
                });
    }

    private void callChatService(String group_id, int pos, final String is_friend) {
        Intent intent = new Intent(PropertyDetailsActivity.this, FriendChatScreen.class);
        intent.putExtra("ids", mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_id());
        intent.putExtra("names", mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getUser_name());
        intent.putExtra("groupId", group_id);
        intent.putExtra("type", "group");
        intent.putExtra("enhanced_profile_ids",
                mPropertyReviewResponse.get(pos).getReview_user_details().get(0).getEnhanced_profile());
        if (is_friend.equalsIgnoreCase("1")) {
            intent.putExtra("from_call", "");
        } else {
            intent.putExtra("from_call", "hotleads");
        }
        startActivity(intent);
    }

    private void getDeviceHeight() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {

            case DisplayMetrics.DENSITY_MEDIUM:
                height = metrics.heightPixels;
                frameHeight = 100;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                height = metrics.heightPixels;
                frameHeight = 20;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                height = metrics.heightPixels;
                frameHeight = 140;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                height = metrics.heightPixels;
                frameHeight = 190;
                break;

        }
        newHeight = height / 2;
    }
}

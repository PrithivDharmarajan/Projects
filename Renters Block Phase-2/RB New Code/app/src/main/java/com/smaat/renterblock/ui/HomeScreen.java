package com.smaat.renterblock.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.AgentFilterLocalEntity;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.ShareThisAppEntity;
import com.smaat.renterblock.entity.UserDetailsEntity;
import com.smaat.renterblock.fragments.AlertsFragment;
import com.smaat.renterblock.fragments.ChatFragment;
import com.smaat.renterblock.fragments.FeedbackFragment;
import com.smaat.renterblock.fragments.FindAgentFragment;
import com.smaat.renterblock.fragments.FriendsFragment;
import com.smaat.renterblock.fragments.HotLeadsPropertyFragment;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.fragments.MyFavouriteFragment;
import com.smaat.renterblock.fragments.NotificationFragment;
import com.smaat.renterblock.fragments.ProfileFragment;
import com.smaat.renterblock.fragments.PropertyDetailsFragment;
import com.smaat.renterblock.fragments.PropertyListingFragment;
import com.smaat.renterblock.fragments.ReviewsListFragment;
import com.smaat.renterblock.fragments.SavedSearchFragment;
import com.smaat.renterblock.fragments.SchedulingFragment;
import com.smaat.renterblock.fragments.SettingFragment;
import com.smaat.renterblock.fragments.ShareThisAppFragment;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.NotificationResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeScreen extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    /*Header*/
    @BindView(R.id.center_lay)
    RelativeLayout mCenterLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_edt_lay)
    LinearLayout mHeaderEdtLay;

    @BindView(R.id.header_edt)
    public EditText mHeaderEdt;

    @BindView(R.id.header_left_first_img_lay)
    RelativeLayout mHeaderLeftFirstImgLay;

    @BindView(R.id.header_left_first_img)
    ImageView mHeaderLeftFirstImg;

    @BindView(R.id.header_left_second_img_lay)
    RelativeLayout mHeaderLeftSecondImgLay;

    @BindView(R.id.header_left_second_img)
    ImageView mHeaderLeftSecondImg;

    @BindView(R.id.header_left_third_txt_lay)
    public RelativeLayout mHeaderLeftThirdTxtLay;

    @BindView(R.id.header_left_third_txt)
    TextView mHeaderLeftThirdTxt;

    @BindView(R.id.header_right_first_img_lay)
    public RelativeLayout mHeaderRightFirstImgLay;

    @BindView(R.id.header_right_first_img)
    ImageView mHeaderRightFirstImg;

    @BindView(R.id.header_right_second_img_lay)
    public RelativeLayout mHeaderRightSecondImgLay;

    @BindView(R.id.header_right_second_img)
    ImageView mHeaderRightSecondImg;

    @BindView(R.id.header_right_third_txt_lay)
    public RelativeLayout mHeaderRightThirdTxtLay;

    @BindView(R.id.header_right_third_txt)
    TextView mHeaderRightThirdTxt;

    @BindView(R.id.req_count_txt)
    public TextView mFriendReqCountTxt;

    /*Slide menu*/
    @BindView(R.id.slide_menu_lay)
    LinearLayout mSlideMenuLay;

    @BindView(R.id.header_full_lay)
    public RelativeLayout mHeaderFullLay;

    private TextView mUserNameTxt;
    private ImageView mNotificationImg;
    private LinearLayout mListingLay, mHotLeadsLay, mReviewsLay, mFriendsLay, mSchedulingLay, mProfileLay, mExclusivesLay,
            mHomesForRentLay, mHomesForSaleLay, mSavedSearchesLay, mMyFavouritesLay, mAlertsLay,
            mOpenHousesLay, mFindAnAgentLay, mFeedbackLay, mRateThisAppLay, mShareThisAppLay, mSettingsLay;

    /*Current Fragment*/
    private BaseFragment mFragment;
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_home_screen);
        initView();
    }

    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mDrawerLayout);


        /*Set slide menu list*/
        setSlideMenu();

        /*Add Back stack*/
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        /*set drawer action*/
        setDrawerAction(true);

        /*Add default fragment screen*/
        AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
        addFragment(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.LOGIN_STATUS) ? AppConstants.LAST_CURRENT_FRAG : new MapFragment());


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(this);
                pushFlow();
            }
        }, 1000);


        Log.d("Device_ID",""+ FirebaseInstanceId.getInstance().getToken());

    }


    private void pushFlow() {

        if (AppConstants.NOTIFICATION_STATUS.equals(AppConstants.SUCCESS_CODE)) {
            if (AppConstants.PUSH_TYPE.equalsIgnoreCase("friendchat")) {
                try {
                    JSONObject json = new JSONObject(AppConstants.PUSH_MSG);

                    AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
                    AppConstants.CHAT_INPUT_ENTITY.setUser_id(PreferenceUtil.getUserID(this));
                    AppConstants.CHAT_INPUT_ENTITY.setFriend_id("");
                    AppConstants.CHAT_INPUT_ENTITY.setSchedule_id(json.getString("group_id"));
                    addFragment(new ChatFragment());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (AppConstants.PUSH_TYPE.equalsIgnoreCase("request")) {

                try {
                    JSONObject json = new JSONObject(AppConstants.PUSH_MSG);
                    addFragment(new HotLeadsPropertyFragment());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*set slide menu views*/
    private void setSlideMenu() {
        UserDetailsEntity userDetails = PreferenceUtil.getUserDetailsRes(this);
        String loginTypeStr = userDetails.getUser_type();


        View myHeaderView = getLayoutInflater().inflate(R.layout.ui_slide_menu_header, mSlideMenuLay, false);
        View myBlockView = getLayoutInflater().inflate(R.layout.ui_slide_menu_my_block, mSlideMenuLay, false);
        View mySearchView = getLayoutInflater().inflate(R.layout.ui_slide_menu_search, mSlideMenuLay, false);
        View myMoreView = getLayoutInflater().inflate(R.layout.ui_slide_menu_more, mSlideMenuLay, false);

        mSlideMenuLay.removeAllViews();
        mSlideMenuLay.addView(myHeaderView);
        mSlideMenuLay.addView(loginTypeStr.isEmpty() || loginTypeStr.equals(AppConstants.BUYER) || loginTypeStr.equals(AppConstants.RENTER) ? mySearchView : myBlockView);
        mSlideMenuLay.addView(loginTypeStr.isEmpty() || loginTypeStr.equals(AppConstants.BUYER) || loginTypeStr.equals(AppConstants.RENTER) ? myBlockView : mySearchView);
        mSlideMenuLay.addView(myMoreView);

        TextView listingTxt;
        View listingView, hotLeadsView;


        /*slide variable initialization*/
        /*Slide header lay*/
        mUserNameTxt = (TextView) findViewById(R.id.user_name_txt);
        mNotificationImg = (ImageView) findViewById(R.id.notification_img);

        /*My block init*/
        listingTxt = (TextView) findViewById(R.id.listing_txt);
        mListingLay = (LinearLayout) findViewById(R.id.listing_lay);
        mHotLeadsLay = (LinearLayout) findViewById(R.id.hot_leads_lay);
        mReviewsLay = (LinearLayout) findViewById(R.id.reviews_lay);
        mFriendsLay = (LinearLayout) findViewById(R.id.friends_lay);
        mSchedulingLay = (LinearLayout) findViewById(R.id.scheduling_lay);
        mProfileLay = (LinearLayout) findViewById(R.id.profile_lay);

        /*view(divider line)*/
        listingView = findViewById(R.id.listing_view);
        hotLeadsView = findViewById(R.id.hot_leads_view);


        /*Search menu init*/
        mExclusivesLay = (LinearLayout) findViewById(R.id.exclusives_lay);
        mHomesForRentLay = (LinearLayout) findViewById(R.id.homes_for_rent_lay);
        mHomesForSaleLay = (LinearLayout) findViewById(R.id.homes_for_sale_lay);
        mSavedSearchesLay = (LinearLayout) findViewById(R.id.saved_searches_lay);
        mMyFavouritesLay = (LinearLayout) findViewById(R.id.my_favourites_lay);
        mAlertsLay = (LinearLayout) findViewById(R.id.alerts_lay);
        mOpenHousesLay = (LinearLayout) findViewById(R.id.open_houses_lay);
        mFindAnAgentLay = (LinearLayout) findViewById(R.id.find_an_agent_lay);

        /*More menu init*/
        mFeedbackLay = (LinearLayout) findViewById(R.id.feedback_lay);
        mRateThisAppLay = (LinearLayout) findViewById(R.id.rate_this_app_lay);
        mShareThisAppLay = (LinearLayout) findViewById(R.id.share_this_app_lay);
        mSettingsLay = (LinearLayout) findViewById(R.id.settings_lay);

        mListingLay.setVisibility(loginTypeStr.isEmpty() || loginTypeStr.equals(AppConstants.BUYER) || loginTypeStr.equals(AppConstants.RENTER) ? View.GONE : View.VISIBLE);
        mHotLeadsLay.setVisibility(loginTypeStr.isEmpty() || loginTypeStr.equals(AppConstants.BUYER) || loginTypeStr.equals(AppConstants.RENTER) ? View.GONE : View.VISIBLE);
        listingView.setVisibility(loginTypeStr.isEmpty() || loginTypeStr.equals(AppConstants.BUYER) || loginTypeStr.equals(AppConstants.RENTER) ? View.GONE : View.VISIBLE);
        hotLeadsView.setVisibility(loginTypeStr.isEmpty() || loginTypeStr.equals(AppConstants.BUYER) || loginTypeStr.equals(AppConstants.RENTER) ? View.GONE : View.VISIBLE);

        /*set Data*/
        mUserNameTxt.setText(userDetails.getUser_name().isEmpty() ? getString(R.string.login) : userDetails.getUser_name());
        listingTxt.setText(loginTypeStr.equals(AppConstants.BROKER) ? getString(R.string.listing) : getString(R.string.property_header));
        setMenuBackground(mHomesForRentLay);

        /*set click Onclick listener*/
        mUserNameTxt.setOnClickListener(this);
        mNotificationImg.setOnClickListener(this);
        mListingLay.setOnClickListener(this);
        mHotLeadsLay.setOnClickListener(this);
        mReviewsLay.setOnClickListener(this);
        mFriendsLay.setOnClickListener(this);
        mSchedulingLay.setOnClickListener(this);
        mProfileLay.setOnClickListener(this);
        mExclusivesLay.setOnClickListener(this);
        mHomesForRentLay.setOnClickListener(this);
        mHomesForSaleLay.setOnClickListener(this);
        mSavedSearchesLay.setOnClickListener(this);
        mMyFavouritesLay.setOnClickListener(this);
        mAlertsLay.setOnClickListener(this);
        mOpenHousesLay.setOnClickListener(this);
        mFindAnAgentLay.setOnClickListener(this);
        mFeedbackLay.setOnClickListener(this);
        mRateThisAppLay.setOnClickListener(this);
        mShareThisAppLay.setOnClickListener(this);
        mSettingsLay.setOnClickListener(this);
    }


    /*Fragment addFragment*/
    public void addFragment(final BaseFragment fmt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fmt != null) {
                    if (PreferenceUtil.getBoolPreferenceValue(HomeScreen.this, AppConstants.LOGIN_STATUS) || fmt instanceof MapFragment || fmt instanceof PropertyDetailsFragment || fmt instanceof SettingFragment) {
                        mFragment = fmt;
                        AppConstants.LAST_CURRENT_FRAG = mFragment;
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        if ((mFragment instanceof MapFragment && AppConstants.MAP_CURRENT_BACK_FRAGMENT instanceof MapFragment)
                                || mFragment instanceof SavedSearchFragment
                                || mFragment instanceof MyFavouriteFragment || mFragment instanceof AlertsFragment && AppConstants.ALERT_BACK_FRAGMENT instanceof AlertsFragment
                                || mFragment instanceof FindAgentFragment || mFragment instanceof FeedbackFragment
                                || mFragment instanceof ShareThisAppFragment || mFragment instanceof SettingFragment
                                || (mFragment instanceof PropertyListingFragment && AppConstants.PROPERTY_LIST_CURRENT_BACK_FRAGMENT instanceof PropertyListingFragment)
                                || mFragment instanceof HotLeadsPropertyFragment && AppConstants.HOT_LEADS_BACK_FRAGMENT instanceof HotLeadsPropertyFragment
                                || (mFragment instanceof ReviewsListFragment && AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT instanceof ReviewsListFragment)
                                || mFragment instanceof SchedulingFragment && AppConstants.SCHEDULE_BACK_FRAGMENT instanceof SchedulingFragment
                                || mFragment instanceof FriendsFragment
                                || (mFragment instanceof ProfileFragment && AppConstants.PROFILE_CURRENT_BACK_FRAGMENT instanceof ProfileFragment)
                                || (mFragment instanceof NotificationFragment && AppConstants.NOTIFICATION_CURRENT_BACK_FRAGMENT instanceof NotificationFragment)) {
                            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        fragmentManager.beginTransaction()
                                .addToBackStack(fmt.getClass().getSimpleName())
                                .setCustomAnimations(R.anim.slide_in_right,
                                        R.anim.slide_out_left, R.anim.slide_out_right,
                                        R.anim.slide_in_left)
//                                .add(R.id.content_frame_lay, fmt)
                                .replace(R.id.content_frame_lay, fmt, fmt.getClass().getSimpleName())
                                .commit();
                    } else {
                        AppConstants.LAST_CURRENT_FRAG = fmt;
                        nextScreen(LoginScreen.class, true);
                    }
                } else {
                    Log.e(AppConstants.TAG, getString(R.string.err_create_frag));
                }
            }
        });

    }


    /*Fragment popBackStack Listener*/
    private FragmentManager.OnBackStackChangedListener getListener() {
        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount > 0) {
                        BaseFragment currFrag = (BaseFragment) manager.findFragmentById(R.id.content_frame_lay);
                        if (currFrag != null) {
                            hideSoftKeyboard(HomeScreen.this);
                            mFragment = currFrag;
                            setHeaderVisible();
                            currFrag.onFragmentResume();
                        } else {
                            Log.e(AppConstants.TAG, getString(R.string.err_create_frag));
                        }
                    }
                }
            }
        };

    }

    private void setHeaderVisible() {
        mHeaderFullLay.setVisibility((mFragment instanceof PropertyDetailsFragment) ? View.GONE : View.VISIBLE);
    }


    /*Slide drawer action*/
    public void setDrawerAction(boolean isMenuScreenBool) {

        /*set drawer mode*/
        mDrawerLayout.setDrawerLockMode(isMenuScreenBool ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (isMenuScreenBool) {
            /*set header left menu icon*/
            mHeaderLeftFirstImg.setImageResource(R.drawable.menu_icon);
            mHeaderLeftFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });
        } else {
            /*set header left back icon*/
            mHeaderLeftFirstImg.setImageResource(R.drawable.back_icon);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            mHeaderLeftFirstImgLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }



     /* If the value of visibleInt is zero,  the view will set gone. Or if the value of visibleInt is one,  the view will set visible. Or else, the view will set invisible*/

    /*set left side header */
    public void setHeaderLeftSecondImgLayVisible(int drawableImgInt, int visibleInt) {
        mHeaderLeftSecondImg.setImageResource(drawableImgInt);
        mHeaderLeftSecondImgLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }

    public void setHeaderLeftThirdTxtLayVisible(String headerLeftTxtStr, int visibleInt) {
        mHeaderLeftThirdTxt.setText(headerLeftTxtStr);
        mHeaderLeftThirdTxtLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }

    /*set left side header */
    public void setHeaderLeftFirstImgLayVisible(int visibleInt) {
        mHeaderLeftFirstImgLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }

    /*set left side header */
    public void setHeaderRightFirstImgLayVisible(int drawableImgInt, int visibleInt) {
        mHeaderRightFirstImg.setImageResource(drawableImgInt);
        mHeaderRightFirstImgLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }

    public void setHeaderRightSecondImgLayVisible(int drawableImgInt, int visibleInt, String countStr) {
        mHeaderRightSecondImg.setImageResource(drawableImgInt);
        mHeaderRightSecondImgLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
        mFriendReqCountTxt.setVisibility(countStr.isEmpty() || countStr.equals(AppConstants.FAILURE_CODE) ? View.GONE : View.VISIBLE);
        mFriendReqCountTxt.setText(countStr);
    }

    public void setHeaderRightThirdTxtLayVisible(String headerLeftTxtStr, int visibleInt) {
        mHeaderRightThirdTxt.setText(headerLeftTxtStr);
        mHeaderRightThirdTxtLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }


    /*set center side header */
    public void setHeaderTxt(String headerTxtStr, int visibleInt) {
        mHeaderTxt.setText(headerTxtStr);
        mHeaderTxt.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }

    public void setHeaderEdt(String headerTxtStr, int visibleInt) {
        mHeaderEdt.setHint(headerTxtStr);
        mHeaderEdtLay.setVisibility(visibleInt == 0 ? View.GONE : (visibleInt == 1 ? View.VISIBLE : View.INVISIBLE));
    }

//    public void setFriendPendingReqCount(String countStr, int visibleInt) {
//        mFriendReqCountTxt.setVisibility(visibleInt == 0 ? View.GONE : View.VISIBLE);
//        mFriendReqCountTxt.setText(countStr);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        getNotification();

    }


    public void getNotification() {
        cancelTimer();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (PreferenceUtil.getBoolPreferenceValue(HomeScreen.this, AppConstants.LOGIN_STATUS)) {
                    APIRequestHandler.getInstance().notificationAPICall2(HomeScreen.this);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNotificationImg.setImageResource(R.drawable.notification_icon);
                        }
                    });

                }

            }
        }, 0, 1000);


    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }


    /*Slide menu onClick*/
    public void onClick(View v) {
        setMenuBackground(v);
        switch (v.getId()) {
            case R.id.user_name_txt:
                if (mUserNameTxt.getText().toString().trim().equals(getString(R.string.login))) {
                    nextScreen(LoginScreen.class, true);
                } else {
                    DialogManager.getInstance().showOptionPopup(this, getString(R.string.logout_msg), getString(R.string.sign_out), getString(R.string.cancel), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            /*Logout Api call*/
                            APIRequestHandler.getInstance().logoutAPICall(HomeScreen.this);
                        }

                        @Override
                        public void onNegativeClick() {

                        }
                    });
                }
                break;
            case R.id.notification_img:

                if (!(mFragment instanceof NotificationFragment)) {
                    AppConstants.NOTIFICATION_CURRENT_BACK_FRAGMENT = new NotificationFragment();
                    addFragment(new NotificationFragment());
                }
                break;
            case R.id.listing_lay:
                if (!(mFragment instanceof PropertyListingFragment)) {
                    AppConstants.PROPERTY_LIST_CURRENT_BACK_FRAGMENT = new PropertyListingFragment();
                    AppConstants.PROPERTY_LIST_USER_ID = PreferenceUtil.getUserID(this);
                    addFragment(new PropertyListingFragment());
                }
                break;
            case R.id.hot_leads_lay:
                if (!(mFragment instanceof HotLeadsPropertyFragment))
                    AppConstants.HOT_LEADS_BACK_FRAGMENT = new HotLeadsPropertyFragment();
                addFragment(new HotLeadsPropertyFragment());
                break;
            case R.id.reviews_lay:
                if (!(mFragment instanceof ReviewsListFragment)) {
                    AppConstants.REVIEW_LIST_CURRENT_BACK_FRAGMENT = new ReviewsListFragment();
                    AppConstants.PROFILE_ID = PreferenceUtil.getUserID(this);
                    addFragment(new ReviewsListFragment());
                }
                break;
            case R.id.friends_lay:
                if (!(mFragment instanceof FriendsFragment))
                    addFragment(new FriendsFragment());
                break;
            case R.id.scheduling_lay:
//                AppConstants.CHAT_INPUT_ENTITY = new ChatInputEntity();
//                AppConstants.CHAT_INPUT_ENTITY.setUser_id("1");
//                AppConstants.CHAT_INPUT_ENTITY.setFriend_id("2");
//                AppConstants.CHAT_INPUT_ENTITY.setSchedule_id("7");
//                addFragment(new ChatFragment());


                if (!(mFragment instanceof SchedulingFragment))
                    AppConstants.SCHEDULE_BACK_FRAGMENT = new SchedulingFragment();
                addFragment(new SchedulingFragment());
                break;
            case R.id.profile_lay:

                if (!(mFragment instanceof ProfileFragment)) {
                    AppConstants.PROFILE_CURRENT_BACK_FRAGMENT = new ProfileFragment();
                    AppConstants.PROFILE_ID = PreferenceUtil.getUserID(this);
                    addFragment(new ProfileFragment());
                }
                break;
            case R.id.exclusives_lay:
                if (!(mFragment instanceof MapFragment) || !AppConstants.TYPE_OF_PROPERTY.equals(AppConstants.EXCLUSIVE)) {
                    AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
                    AppConstants.TYPE_OF_PROPERTY = AppConstants.EXCLUSIVE;
                    addFragment(new MapFragment());
                }
                break;
            case R.id.homes_for_rent_lay:
                if (!(mFragment instanceof MapFragment) || !AppConstants.TYPE_OF_PROPERTY.equals(AppConstants.RENT)) {
                    AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
                    AppConstants.TYPE_OF_PROPERTY = AppConstants.RENT;
                    addFragment(new MapFragment());
                }
                break;
            case R.id.homes_for_sale_lay:
                if (!(mFragment instanceof MapFragment) || !AppConstants.TYPE_OF_PROPERTY.equals(AppConstants.SALE)) {
                    AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
                    AppConstants.TYPE_OF_PROPERTY = AppConstants.SALE;
                    addFragment(new MapFragment());
                }
                break;
            case R.id.saved_searches_lay:
                if (!(mFragment instanceof SavedSearchFragment))
                    addFragment(new SavedSearchFragment());
                break;
            case R.id.my_favourites_lay:
                if (!(mFragment instanceof MyFavouriteFragment))
                    addFragment(new MyFavouriteFragment());

                break;
            case R.id.alerts_lay:
                if (!(mFragment instanceof AlertsFragment))
                    AppConstants.ALERT_BACK_FRAGMENT = new AlertsFragment();
                addFragment(new AlertsFragment());
                break;
            case R.id.open_houses_lay:
                if (!(mFragment instanceof MapFragment) || !AppConstants.TYPE_OF_PROPERTY.equals(AppConstants.OPEN_HOUSES)) {
                    AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
                    AppConstants.TYPE_OF_PROPERTY = AppConstants.OPEN_HOUSES;
                    addFragment(new MapFragment());
                }
                break;
            case R.id.find_an_agent_lay:
                if (!(mFragment instanceof FindAgentFragment)) {

                    addFragment(new FindAgentFragment());
                }
                break;
            case R.id.feedback_lay:
                if (!(mFragment instanceof FeedbackFragment))
                    addFragment(new FeedbackFragment());
                break;
            case R.id.rate_this_app_lay:
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException an) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + appPackageName)));
                }
                break;
            case R.id.share_this_app_lay:

                AppConstants.SHARE_DETAILS = new ShareThisAppEntity();
                AppConstants.SHARE_DETAILS.setSms_share(getString(R.string.app_content) + " " + AppConstants.APP_LINK);
                AppConstants.SHARE_DETAILS.setEmail_share_subject(getString(R.string.app_content));
                AppConstants.SHARE_DETAILS.setEmail_share_text(AppConstants.APP_LINK);
                AppConstants.SHARE_DETAILS.setFacebook_share_title(getString(R.string.app_content));
                AppConstants.SHARE_DETAILS.setFacebook_share_description(getString(R.string.app_content));
                AppConstants.SHARE_DETAILS.setFacebook_share_link(AppConstants.APP_LINK);

                if (!(mFragment instanceof ShareThisAppFragment))
                    addFragment(new ShareThisAppFragment());
                break;

            case R.id.settings_lay:
                if (!(mFragment instanceof SettingFragment))
                    addFragment(new SettingFragment());
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }


    /*set slide bg color*/
    private void setMenuBackground(final View view) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                /*set default color*/
                if (view.getId() != R.id.rate_this_app_lay) {
                    mUserNameTxt.setBackgroundResource(R.color.slide_black_bg);
                    mNotificationImg.setBackgroundResource(R.color.slide_black_bg);
                    mListingLay.setBackgroundResource(R.color.slide_black_bg);
                    mHotLeadsLay.setBackgroundResource(R.color.slide_black_bg);
                    mReviewsLay.setBackgroundResource(R.color.slide_black_bg);
                    mFriendsLay.setBackgroundResource(R.color.slide_black_bg);
                    mSchedulingLay.setBackgroundResource(R.color.slide_black_bg);
                    mProfileLay.setBackgroundResource(R.color.slide_black_bg);
                    mExclusivesLay.setBackgroundResource(R.color.slide_black_bg);
                    mHomesForRentLay.setBackgroundResource(R.color.slide_black_bg);
                    mHomesForSaleLay.setBackgroundResource(R.color.slide_black_bg);
                    mSavedSearchesLay.setBackgroundResource(R.color.slide_black_bg);
                    mMyFavouritesLay.setBackgroundResource(R.color.slide_black_bg);
                    mAlertsLay.setBackgroundResource(R.color.slide_black_bg);
                    mOpenHousesLay.setBackgroundResource(R.color.slide_black_bg);
                    mFindAnAgentLay.setBackgroundResource(R.color.slide_black_bg);
                    mFeedbackLay.setBackgroundResource(R.color.slide_black_bg);
                    mRateThisAppLay.setBackgroundResource(R.color.slide_black_bg);
                    mShareThisAppLay.setBackgroundResource(R.color.slide_black_bg);
                    mSettingsLay.setBackgroundResource(R.color.slide_black_bg);

                    /*selected view*/
                    if (view.getId() != R.id.user_name_txt && view.getId() != R.id.notification_img)
                        view.setBackgroundResource(R.color.app_blue);
                }
            }
        });
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CommonResponse) {
            CommonResponse commonResponse = (CommonResponse) responseObj;
            if (commonResponse.getError_code().equals(AppConstants.FAILURE_CODE)) {
                DialogManager.getInstance().showAlertPopup(this, commonResponse.getMsg(), this);
            } else {
                clearSocialMediaSessions();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                DialogManager.getInstance().showToast(this, getString(R.string.logged_out));
                UserDetailsEntity loginEntity = new UserDetailsEntity();
                PreferenceUtil.storeUserDetails(this, loginEntity);
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, false);

                setSlideMenu();
                AppConstants.MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
                addFragment(new MapFragment());
            }

        }
        if (responseObj instanceof NotificationResponse) {
            final NotificationResponse mNotificationRes = (NotificationResponse) responseObj;
            if (mNotificationRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mNotificationRes.getResult() != null && mNotificationRes.getResult().size() > 0) {
                            mNotificationImg.setImageResource(R.drawable.notification_red_icon);
                        } else {
                            mNotificationImg.setImageResource(R.drawable.notification_icon);

                        }
                    }
                });

            }

        }


    }

    /*Default back button action*/
    @Override
    public void onBackPressed() {
        hideSoftKeyboard(HomeScreen.this);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            backButtonClick();
        }
    }

    /*get pr class from popBackStack*/
    private void backButtonClick() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager() != null) {
            if (count == 1) {
                exitFromApp();
            } else {
                /*get previous class from popBackStack*/
                getSupportFragmentManager().popBackStack();
            }
        }
    }


    /*App exit popup*/
    private void exitFromApp() {
        DialogManager.getInstance().showOptionPopup(this, getString(R.string.exit_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
            @Override
            public void onPositiveClick() {
                ActivityCompat.finishAffinity(HomeScreen.this);
            }

            @Override
            public void onNegativeClick() {

            }
        });

    }

    private void clearSocialMediaSessions() {

        /*Clear fb session*/
        FacebookSdk.sdkInitialize(this);
        LoginManager.getInstance().logOut();

        /*Clear twitter session*/
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            TwitterCore.getInstance().getSessionManager().clearActiveSession();
        }
    }


    /*activity result callback */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mFragment != null)
            mFragment.onActivityResult(requestCode, resultCode, data);
    }

    /*Request access permission callback*/
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (mFragment != null)
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

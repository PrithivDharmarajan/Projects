package com.fautus.fautusapp.ui;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.entity.ConChatDetailsEntity;
import com.fautus.fautusapp.fragment.ChatFragment;
import com.fautus.fautusapp.fragment.ConsHomeFragment;
import com.fautus.fautusapp.fragment.ConsMomentCheckFragment;
import com.fautus.fautusapp.fragment.ConsProfileFragment;
import com.fautus.fautusapp.fragment.ImageUploadFragment;
import com.fautus.fautusapp.fragment.MomentDetailsFragment;
import com.fautus.fautusapp.fragment.MomentFragment;
import com.fautus.fautusapp.fragment.MomentPayFragment;
import com.fautus.fautusapp.fragment.MomentUploadFragment;
import com.fautus.fautusapp.fragment.PhotoHomeFragment;
import com.fautus.fautusapp.fragment.PhotoMomentUploadFragment;
import com.fautus.fautusapp.fragment.PhotographerProfileFragment;
import com.fautus.fautusapp.fragment.SettingsFragment;
import com.fautus.fautusapp.fragment.StripConsCardListFragment;
import com.fautus.fautusapp.fragment.StripConsPaymentFragment;
import com.fautus.fautusapp.fragment.StripPhotoBankAccFragment;
import com.fautus.fautusapp.fragment.TermsCondFragment;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.main.BaseFragment;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.zendesk.sdk.support.SupportActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fautus.fautusapp.utils.ParseAPIConstants.location;
import static com.fautus.fautusapp.utils.ParseAPIConstants.user;

/**
 * This class implements UI and functions for base home process
 */
public class HomeScreen extends BaseActivity {

    /*Variable initialization using bind view*/
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.header_left_lay)
    public RelativeLayout mHeaderLeftLay;

    @BindView(R.id.header_left_btn_lay)
    RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.header_left_second_btn_lay)
    RelativeLayout mHeaderLeftSecondBtnLay;

    @BindView(R.id.header_right_lay)
    public RelativeLayout mHeaderRightLay;

    @BindView(R.id.header_right_btn_lay)
    RelativeLayout mHeaderRightBtnLay;

    @BindView(R.id.header_right_second_btn_lay)
    public RelativeLayout mHeaderRightSecondBtnLay;

    @BindView(R.id.header_left_txt)
    TextView mHeaderLeftTxt;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.header_right_img)
    ImageView mHeaderRightImg;

    @BindView(R.id.header_right_txt)
    TextView mHeaderRightTxt;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.photographer_view)
    View photographerView;

    @BindView(R.id.photographer_lay)
    LinearLayout mPhotographerLay;

    private boolean mAddFrgBool = false;
    public BaseFragment mFragment;
    private Timer mNewCustomerTimer;
    private ParseObject mPhotographerParseObj;
    private Dialog mNewUserDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_home_screen);
        initView();
    }


    private void initView() {

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mDrawerLayout);

        /*Update user device*/
        updateDeviceUser();

        /*Set header view*/
        mHeaderRightImg.setImageResource(R.drawable.back_img);
        mHeaderLeftBtnLay.setVisibility(View.VISIBLE);
        mHeaderLeftTxt.setVisibility(View.GONE);
        mHeaderRightTxt.setVisibility(View.GONE);
        mHeaderRightBtnLay.setVisibility(View.INVISIBLE);

        /*If Logged in user is a photographer, a photographer option in the slide menu will be hidden*/
        photographerView.setVisibility(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER) ? View.VISIBLE : View.GONE);
        mPhotographerLay.setVisibility(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER) ? View.VISIBLE : View.GONE);

        /*Add Back stack*/
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        setDrawerAction(true);

        /*Add fragment screen*/
        addFragment(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER) ? new ConsHomeFragment() : new PhotoHomeFragment());


    }


    /*View onClick*/
    @OnClick({R.id.home_lay, R.id.payment_lay, R.id.profile_lay, R.id.moments_lay, R.id.photographer_lay, R.id.help_faq_lay, R.id.settings_lay, R.id.legal_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!(mFragment instanceof ConsHomeFragment) && !(mFragment instanceof PhotoHomeFragment))
                    addFragment(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER) ? new ConsHomeFragment() : new PhotoHomeFragment());
                break;
            case R.id.payment_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!(mFragment instanceof StripConsCardListFragment) && !(mFragment instanceof StripPhotoBankAccFragment))
                    addFragment(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER) ? new StripConsCardListFragment() : new StripPhotoBankAccFragment());
                break;
            case R.id.profile_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!(mFragment instanceof ConsProfileFragment) && !(mFragment instanceof PhotographerProfileFragment))
                    addFragment(PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER) ? new ConsProfileFragment() : new PhotographerProfileFragment());
                break;
            case R.id.moments_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!(mFragment instanceof MomentFragment)) {
                    AppConstants.MOMENT_FROM_MENU = AppConstants.SUCCESS_CODE;
                    addFragment(new MomentFragment());
                }
                break;
            case R.id.photographer_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);

                DialogManager.getInstance().showBasicInfoAlertPopup(this, getString(R.string.menu_photographer_topic).toUpperCase(Locale.US), getString(R.string.menu_photographer_msg), getString(R.string.cancel_underline), true, true, new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {
                       /*Set flag for screen from menu*/
                        AppConstants.PHOTOGRAPHER_FROM_MENU = AppConstants.SUCCESS_CODE;

                       /*Set flag - Act as photographer*/
                        AppConstants.WELCOME_SCREEN_TYPE = AppConstants.SUCCESS_CODE;
                        nextScreen(TutorialScreen.class, false);
                    }

                    @Override
                    public void onNoClick() {
                    }
                });

                break;
            case R.id.help_faq_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                new SupportActivity.Builder().show(this);
                break;
            case R.id.settings_lay:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!(mFragment instanceof SettingsFragment))
                    addFragment(new SettingsFragment());
                break;
            case R.id.legal_txt:
                mDrawerLayout.closeDrawer(GravityCompat.START);

                DialogManager.getInstance().showLegalPopup(this, new InterfaceTwoBtnCallback() {
                    @Override
                    public void onYesClick() {

                        AppConstants.TERMS_COND_SCREEN = AppConstants.SUCCESS_CODE;
                        addFragment(new TermsCondFragment());
                    }

                    @Override
                    public void onNoClick() {
                        AppConstants.TERMS_COND_SCREEN = AppConstants.FAILURE_CODE;
                        addFragment(new TermsCondFragment());
                    }
                });

                break;
        }

    }

    /*set header text*/
    public void setHeaderText(String headerTxtStr) {
        mHeaderTxt.setText((mFragment instanceof ConsHomeFragment || mFragment instanceof PhotoHomeFragment || mFragment instanceof StripConsPaymentFragment) ? headerTxtStr : headerTxtStr.toUpperCase(Locale.US));
    }

    /*set header left text*/
    private void setHeaderLeftText(String headerLeftTxtStr) {
        mHeaderLeftTxt.setText(headerLeftTxtStr);
    }

    /*set header right text*/
    public void setHeaderRightText(String headerRightTxtStr) {
        mHeaderRightTxt.setText(headerRightTxtStr);
    }

    /*set header left side image*/
    public void setHeadLeftImg(int imgName) {
        mHeaderLeftImg.setImageResource(imgName);
    }

    /*set header right side image*/
    public void setHeadRightImgVisible(boolean isVisible, int imgName) {
        mHeaderRightBtnLay.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        if (isVisible) {
            mHeaderRightLay.setVisibility(View.VISIBLE);
            mHeaderRightImg.setImageResource(imgName);
        }
    }


    /*Slide drawer action*/
    public void setDrawerAction(boolean isMenuScreen) {
        if (isMenuScreen) {
            /*set header left menu icon*/
            mHeaderLeftImg.setImageResource(R.drawable.menu_img);
            mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
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
            mHeaderLeftImg.setImageResource(R.drawable.back_img);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            mHeaderLeftLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }


    /*Fragment popBackStack*/
    public void addFragment(BaseFragment fmt) {
        if (fmt != null) {
            mAddFrgBool = true;
            mFragment = fmt;
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (mFragment instanceof ConsHomeFragment || mFragment instanceof PhotoHomeFragment || mFragment instanceof ConsProfileFragment || mFragment instanceof PhotographerProfileFragment || (mFragment instanceof MomentFragment && AppConstants.MOMENT_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) || mFragment instanceof SettingsFragment || mFragment instanceof StripPhotoBankAccFragment || mFragment instanceof PhotoMomentUploadFragment || (mFragment instanceof MomentDetailsFragment && AppConstants.MOMENT_FROM_LIST.equalsIgnoreCase(AppConstants.FAILURE_CODE))) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fragmentManager.beginTransaction()
                    .addToBackStack(fmt.getClass().getSimpleName())
                    .add(R.id.content_frame_lay, fmt)
                    .commit();
        } else {
            Log.e(this.getClass().getSimpleName(), getString(R.string.err_create_frag));
        }
    }


    /*Fragment popBackStack Listener*/
    private FragmentManager.OnBackStackChangedListener getListener() {
        return new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    if (mAddFrgBool) {
                        mAddFrgBool = false;
                    } else {
                        int backStackEntryCount = manager.getBackStackEntryCount();
                        if (backStackEntryCount > 0) {
                            BaseFragment currFrag = (BaseFragment) manager.findFragmentById(R.id.content_frame_lay);
                            if (currFrag != null) {
                                hideSoftKeyboard(HomeScreen.this);
                                mFragment = currFrag;
                                checkAndSetScreenStatus();
                                currFrag.onFragmentResume();
                            } else {
                                Log.e(this.getClass().getSimpleName(), getString(R.string.err_create_frag));
                            }
                        }
                    }
                }
            }

        };

    }

    private void checkAndSetScreenStatus() {

        mDrawerLayout.setDrawerLockMode(mFragment instanceof ConsHomeFragment || mFragment instanceof PhotoHomeFragment || mFragment instanceof ConsProfileFragment || mFragment instanceof StripPhotoBankAccFragment || mFragment instanceof PhotographerProfileFragment ||
                (mFragment instanceof MomentFragment
                        && AppConstants.MOMENT_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) || mFragment instanceof SettingsFragment ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mHeaderLeftTxt.setVisibility((mFragment instanceof ImageUploadFragment || mFragment instanceof StripConsPaymentFragment || mFragment instanceof StripConsCardListFragment) ? View.VISIBLE : View.GONE);
        mHeaderRightTxt.setVisibility((mFragment instanceof PhotoHomeFragment || mFragment instanceof ImageUploadFragment || mFragment instanceof StripConsPaymentFragment) ? View.VISIBLE : View.GONE);
        mHeaderLeftImg.setVisibility((mFragment instanceof ImageUploadFragment || mFragment instanceof StripConsCardListFragment || mFragment instanceof StripConsPaymentFragment) ? View.GONE : View.VISIBLE);

        mHeaderRightLay.setVisibility(mFragment instanceof ConsHomeFragment || mFragment instanceof MomentFragment || mFragment instanceof MomentDetailsFragment || mFragment instanceof MomentPayFragment || mFragment instanceof SettingsFragment || mFragment instanceof StripConsCardListFragment || (mFragment instanceof MomentUploadFragment && AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.FAILURE_CODE)) ? View.INVISIBLE : View.VISIBLE);

        /*set map btn visibility*/
        boolean isPhotographerBool = !PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER);
        mHeaderLeftSecondBtnLay.setVisibility(isPhotographerBool && mFragment instanceof ChatFragment || AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE) && mFragment instanceof MomentUploadFragment ? View.INVISIBLE : View.GONE);
        mHeaderRightSecondBtnLay.setVisibility(isPhotographerBool && mFragment instanceof ChatFragment || AppConstants.MOMENT_UPLOAD_FROM_CHAT.equalsIgnoreCase(AppConstants.SUCCESS_CODE) && mFragment instanceof MomentUploadFragment ? View.VISIBLE : View.GONE);

        /*Trigger local image path*/
//        if(mFragment instanceof MomentUploadFragment){
//            ImageUtil.refreshLocalImages();
//        }

        setHeaderLeftText(getResources().getString(R.string.cancel));
        setHeaderRightText(getResources().getString(R.string.done));
    }


    /*Default back button action*/
    @Override
    public void onBackPressed() {
        hideSoftKeyboard(HomeScreen.this);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mFragment instanceof ChatFragment || (mFragment instanceof ConsMomentCheckFragment && AppConstants.HEADER_RIGHT_CLICK.equalsIgnoreCase(AppConstants.FAILURE_CODE)) || (mFragment instanceof MomentUploadFragment && AppConstants.HEADER_RIGHT_CLICK.equalsIgnoreCase(AppConstants.FAILURE_CODE)) || mFragment instanceof PhotoMomentUploadFragment) {
            mFragment.onFragmentBackPressed();
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
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void updateDeviceUser() {
        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        if (parseInstallation != null && ParseUser.getCurrentUser() != null) {
            parseInstallation.put(ParseAPIConstants.GCMSenderId, PreferenceUtil.getStringValue(this, AppConstants.PARSE_DEVICE_ID));
            parseInstallation.put(user, ParseUser.getCurrentUser());
            parseInstallation.saveInBackground();
        }
    }

    /*App exit popup*/
    private void exitFromApp() {
        DialogManager.getInstance().showOptionAlertPopup(this, getString(R.string.app_name), getString(R.string.exit_msg), getString(R.string.no), getString(R.string.yes), new InterfaceTwoBtnCallback() {
            @Override
            public void onYesClick() {
                ActivityCompat.finishAffinity(HomeScreen.this);
            }

            @Override
            public void onNoClick() {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        Bundle notificationBundle = getIntent().getExtras();
        String pushMomentIdStr = AppConstants.FAILURE_CODE;
        String pushNotificationStatusStr = AppConstants.FAILURE_CODE;

        if (notificationBundle != null) {
            pushMomentIdStr = notificationBundle.getString(AppConstants.PUSH_MOMENT_ID);
            pushNotificationStatusStr = notificationBundle.getString(AppConstants.PUSH_CHAT_STATUS);
        }

        if (!PreferenceUtil.getBoolPreferenceValue(this, AppConstants.USER_IS_CONSUMER)) {
            if (pushMomentIdStr != null && !pushMomentIdStr.equalsIgnoreCase(AppConstants.FAILURE_CODE) && pushMomentIdStr.length() > 2) {
                showPhotoRequestFromPush(pushMomentIdStr);
            } else {
                checkForNewCustomers();
            }
        }
        final ConChatDetailsEntity chatEntityRes = PreferenceUtil.getConsumerDetails(this);
        if (pushNotificationStatusStr != null && pushNotificationStatusStr.equalsIgnoreCase(AppConstants.SUCCESS_CODE) && chatEntityRes != null && chatEntityRes.getMomentObjIdStr() != null && chatEntityRes.getMomentAdHocCodeStr() != null && chatEntityRes.getPhotographerObjIdStr() != null) {
            getChatDetails(chatEntityRes);
        }
        cancelNotification();

        super.onResume();
    }

    @Override
    protected void onPause() {
        SendBird.removeAllChannelHandlers();
        cancelNewCustomerTimer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelNewCustomerTimer();
    }

    private void checkForNewCustomers() {
        cancelNewCustomerTimer();

        mNewCustomerTimer = new Timer();
        mNewCustomerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mPhotographerParseObj == null) {
                    getPhotographerDetailsFromDB();
                } else {
                    ParseQuery<ParseObject> momentLogQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_MomentLog);
                    momentLogQuery.whereEqualTo(ParseAPIConstants.photographer, mPhotographerParseObj);
                    momentLogQuery.include(ParseAPIConstants.moment);

                    ParseQuery<ParseObject> innerMomentQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
                    innerMomentQuery.whereDoesNotExist(ParseAPIConstants.accepted);
                    momentLogQuery.whereMatchesQuery(ParseAPIConstants.moment, innerMomentQuery);

                    momentLogQuery.whereDoesNotExist(ParseAPIConstants.response);

                    momentLogQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null && object != null) {
                                final ParseObject momentObj = object.getParseObject(ParseAPIConstants.moment);
                                if (momentObj != null) {
                                    momentObj.fetchInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(final ParseObject object, ParseException e) {
                                            if (e == null && object != null) {
                                                if (mPhotographerParseObj != null && object.getNumber(ParseAPIConstants.skillLevelRequested) != null && object.getParseGeoPoint(location) != null && (mNewUserDialog == null || !mNewUserDialog.isShowing())) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            cancelNewCustomerTimer();
                                                            sendBirdSDKInit();
                                                            mNewUserDialog = DialogManager.getInstance().showPhotoShootReqAlertPopup(HomeScreen.this, object.getNumber(ParseAPIConstants.skillLevelRequested) + "", object.getParseGeoPoint(location), momentObj, new InterfaceTwoBtnCallback() {
                                                                @Override
                                                                public void onYesClick() {
                                                                    AppConstants.CHAT_CONSUMER_DETAILS = object.getParseObject(ParseAPIConstants.creator);
                                                                    AppConstants.CHAT_PHOTOGRAPHER_DETAILS = mPhotographerParseObj;
                                                                    AppConstants.CHAT_MOMENT_DETAILS = object;
                                                                    AppConstants.SHOW_CON_PHOTO_DIALOG = AppConstants.FAILURE_CODE;

                                                                    ConChatDetailsEntity chatDetails = new ConChatDetailsEntity();
                                                                    chatDetails.setMomentObjIdStr(AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
                                                                    chatDetails.setMomentAdHocCodeStr(AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.adHocCode));
                                                                    chatDetails.setPhotographerObjIdStr(AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getObjectId());

                                                                    PreferenceUtil.storeConsumerChatDetails(HomeScreen.this, chatDetails);

                                                                    mPhotographerParseObj = null;
                                                                    baseActivityAlertDismiss(mNewUserDialog);
                                                                    cancelNewCustomerTimer();
                                                                    checkForNewCustomers();

                                                                    AppConstants.SEND_BIRD_GROUP_CHANNEL = null;

                                                                    addFragment(new ChatFragment());
                                                                }

                                                                @Override
                                                                public void onNoClick() {
                                                                    mPhotographerParseObj = null;
                                                                    baseActivityAlertDismiss(mNewUserDialog);
                                                                    cancelNewCustomerTimer();
                                                                    checkForNewCustomers();

                                                                }
                                                            });
                                                        }
                                                    });
                                                } else {
                                                    cancelNewCustomerTimer();
                                                }
                                            } else if (e != null) {
                                                DialogManager.getInstance().showAlertPopup(HomeScreen.this, getString(R.string.app_name), e.getMessage(), HomeScreen.this);
                                            }
                                        }
                                    });
                                } else {
                                    baseActivityAlertDismiss(mNewUserDialog);
                                }
                            } else {
                                baseActivityAlertDismiss(mNewUserDialog);
                            }
                        }
                    });
                }
            }
        }, 0, 3000);
    }

    private void showPhotoRequestFromPush(final String momentStr) {

        cancelNewCustomerTimer();

        mNewCustomerTimer = new Timer();
        mNewCustomerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mPhotographerParseObj == null) {
                    getPhotographerDetailsFromDB();
                } else {
                    ParseQuery<ParseObject> momentParseQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Moment);
                    momentParseQuery.whereEqualTo(ParseAPIConstants.adHocCode, momentStr);
                    momentParseQuery.whereDoesNotExist(ParseAPIConstants.photographer);
                    momentParseQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null && object != null) {
                                final ParseObject momentObj = object.getParseObject(ParseAPIConstants.moment);
                                if (momentObj != null) {
                                    momentObj.fetchInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(final ParseObject object, ParseException e) {
                                            if (e == null && object != null) {
                                                if (object.getNumber(ParseAPIConstants.skillLevelRequested) != null && object.getParseGeoPoint(location) != null && (mNewUserDialog == null || !mNewUserDialog.isShowing())) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            cancelNewCustomerTimer();
                                                            sendBirdSDKInit();
                                                            mNewUserDialog = DialogManager.getInstance().showPhotoShootReqAlertPopup(HomeScreen.this, object.getNumber(ParseAPIConstants.skillLevelRequested) + "", object.getParseGeoPoint(location), momentObj, new InterfaceTwoBtnCallback() {
                                                                @Override
                                                                public void onYesClick() {
                                                                    AppConstants.CHAT_CONSUMER_DETAILS = object.getParseObject(ParseAPIConstants.creator);
                                                                    AppConstants.CHAT_PHOTOGRAPHER_DETAILS = mPhotographerParseObj;
                                                                    AppConstants.CHAT_MOMENT_DETAILS = object;
                                                                    AppConstants.SHOW_CON_PHOTO_DIALOG = AppConstants.FAILURE_CODE;


                                                                    ConChatDetailsEntity chatDetails = new ConChatDetailsEntity();
                                                                    chatDetails.setMomentObjIdStr(AppConstants.CHAT_MOMENT_DETAILS.getObjectId());
                                                                    chatDetails.setMomentAdHocCodeStr(AppConstants.CHAT_MOMENT_DETAILS.getString(ParseAPIConstants.adHocCode));
                                                                    chatDetails.setPhotographerObjIdStr(AppConstants.CHAT_PHOTOGRAPHER_DETAILS.getObjectId());
                                                                    PreferenceUtil.storeConsumerChatDetails(HomeScreen.this, chatDetails);

                                                                    mPhotographerParseObj = null;
                                                                    baseActivityAlertDismiss(mNewUserDialog);


                                                                    cancelNewCustomerTimer();
                                                                    checkForNewCustomers();

                                                                    AppConstants.SEND_BIRD_GROUP_CHANNEL = null;
                                                                    addFragment(new ChatFragment());
                                                                }

                                                                @Override
                                                                public void onNoClick() {
                                                                    cancelNewCustomerTimer();
                                                                    mPhotographerParseObj = null;
                                                                    baseActivityAlertDismiss(mNewUserDialog);
                                                                    checkForNewCustomers();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            } else {
                                                baseActivityAlertDismiss(mNewUserDialog);
                                                cancelNewCustomerTimer();
                                                checkForNewCustomers();

                                                if (e != null)
                                                    DialogManager.getInstance().showAlertPopup(HomeScreen.this, getString(R.string.app_name), e.getMessage(), HomeScreen.this);
                                            }
                                        }
                                    });
                                } else {
                                    baseActivityAlertDismiss(mNewUserDialog);
                                    cancelNewCustomerTimer();
                                    checkForNewCustomers();
                                }
                            } else {

                                baseActivityAlertDismiss(mNewUserDialog);
                                cancelNewCustomerTimer();
                                checkForNewCustomers();
                            }
                        }
                    });
                }
            }
        }, 0, 2000);

    }

    private void cancelNewCustomerTimer() {
        if (mNewCustomerTimer != null) {
            mNewCustomerTimer.cancel();
            mNewCustomerTimer.purge();
        }
    }

    private void getPhotographerDetailsFromDB() {
        ParseQuery<ParseObject> currentPhotographerQuery = ParseQuery.getQuery(ParseAPIConstants.Parse_Photographer);
        currentPhotographerQuery.whereEqualTo(ParseAPIConstants.user, ParseUser.getCurrentUser());
        currentPhotographerQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    mPhotographerParseObj = object;
                }
            }
        });
    }

    /* initialization for SendBird chat SDK */
    private void sendBirdSDKInit() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            String senderIdStr = (user.getString(ParseAPIConstants.username) != null && !user.getString(ParseAPIConstants.username).isEmpty()) ? user.getString(ParseAPIConstants.username) : getResources().getString(R.string.user_one);
            SendBird.connect(senderIdStr, new SendBird.ConnectHandler() {
                @Override
                public void onConnected(User user, SendBirdException e) {

                }
            });
        }
    }

    private void getChatDetails(final ConChatDetailsEntity chatEntityRes) {
        DialogManager.getInstance().showProgress(this);
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
                                AppConstants.SEND_BIRD_GROUP_CHANNEL = null;
                                addFragment(new ChatFragment());
                            }
                        }
                    });
                } else {
                    DialogManager.getInstance().hideProgress();
                }
            }
        });
    }

    public void cancelNotification() {
        /*0 is notification Id*/
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
    }
}

package com.calix.calixgigaspireapp.ui.dashboard;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.calix.calixgigaspireapp.BuildConfig;
import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.dashboard.DashboardAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdEntity;
import com.calix.calixgigaspireapp.output.model.AlexaAppIdResponse;
import com.calix.calixgigaspireapp.output.model.CalixAgentResponse;
import com.calix.calixgigaspireapp.output.model.CategoryEntity;
import com.calix.calixgigaspireapp.output.model.DashboardResponse;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.output.model.RouterMapResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.devices.DevicesList;
import com.calix.calixgigaspireapp.ui.guest.GuestNetwork;
import com.calix.calixgigaspireapp.ui.iot.IOTDeviceList;
import com.calix.calixgigaspireapp.ui.loginregconfig.Login;
import com.calix.calixgigaspireapp.ui.loginregconfig.RouterConfiguration;
import com.calix.calixgigaspireapp.ui.network.NetworkUsage;
import com.calix.calixgigaspireapp.ui.parentalcontrol.ParentalControlDashBoard;
import com.calix.calixgigaspireapp.ui.settings.Settings;
import com.calix.calixgigaspireapp.ui.wifi.InternetSpeedTest;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;
import com.calix.calixgigaspireapp.utils.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Dashboard extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    @BindView(R.id.slide_menu_lay)
    LinearLayout mSlideMenuLay;

    @BindView(R.id.version_txt)
    TextView mVersionTxt;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mHeaderRightImgLay;

    @BindView(R.id.header_right_img)
    ImageView mHeaderRightImg;

    @BindView(R.id.dashboard_header_bg_lay)
    RelativeLayout mDashboardHeaderBgLay;

    @BindView(R.id.dashboard_header_msg_lay)
    RelativeLayout mDashboardHeaderMsgLay;

    @BindView(R.id.upload_speed_txt)
    TextView mUploadSpeedTxt;

    @BindView(R.id.upload_scale_txt)
    TextView mUploadScaleTxt;

    @BindView(R.id.download_speed_txt)
    TextView mDownloadSpeedTxt;

    @BindView(R.id.download_scale_txt)
    TextView mDownloadScaleTxt;

    @BindView(R.id.user_profile_img)
    ImageView mUserProfileImg;

    @BindView(R.id.name_txt)
    TextView mNameTxt;

    @BindView(R.id.devices_viewpager)
    ViewPager pager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.more_txt)
    TextView mMoreTxt;

    /* Footer Variables */
    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_one_txt)
    TextView mFooterOneTxt;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_second_txt)
    TextView mFooterSecondTxt;

    @BindView(R.id.footer_third_img)
    ImageView mFooterThirdImg;

    @BindView(R.id.footer_third_txt)
    TextView mFooterThirdTxt;


    private boolean mIsIOTFlowBool = false, mIsWelComeOnBoardFlowBool = false;
    private String mAlexaAppIdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.ui_dash_board);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mDrawerLayout);
        mVersionTxt.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));


        setHeaderView();
        setFooterView();

        dashboardAPICall();


    }


    /*Set header*/

    private void setHeaderView() {

        /*set header changes*/
        mDashboardHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderLeftImg.setImageResource(R.drawable.menu_icon);
        mHeaderRightImgLay.setVisibility(View.VISIBLE);
        mHeaderRightImg.setImageResource(R.drawable.ic_notification);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.dashboard));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDashboardHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size145 : R.dimen.size45);
                    mSlideMenuLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Dashboard.this), 0, 0);
                    mDashboardHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Dashboard.this)));
                    mDashboardHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Dashboard.this), 0, 0);
                    mDashboardHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
                }

            });
        }

    }

    /*Set Footer View */
    private void setFooterView() {
        mFooterFirstImg.setImageResource(R.drawable.ic_default_device);
        mFooterOneTxt.setText(getString(R.string.devices));

        mFooterSecondImg.setImageResource(R.drawable.setting_icon);
        mFooterSecondTxt.setText(getString(R.string.settings));

        mFooterThirdImg.setBackground(getResources().getDrawable(R.drawable.ic_router_map));
        mFooterThirdTxt.setText(getString(R.string.footer_router_map));
    }


    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();
    }

    /*View onClick*/
    @OnClick({R.id.header_right_img_lay, R.id.header_left_img_lay,
            R.id.dashboard_lay, R.id.parental_control_lay, R.id.guest_network_lay,
            R.id.device_list_lay, R.id.iot_device_lay, R.id.network_usage_lay,
            R.id.wifi_speed_lay, R.id.alexa_lay, R.id.settings_lay, R.id.logout_lay,
            R.id.footer_first_lay, R.id.footer_second_lay, R.id.footer_third_lay})
    public void onClick(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()) {
                    case R.id.header_left_img_lay:
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        break;

                    case R.id.header_right_img_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(Alert.class);
                        break;

                    case R.id.dashboard_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.parental_control_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(ParentalControlDashBoard.class);
                        break;

                    case R.id.guest_network_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(GuestNetwork.class);
                        break;

                    case R.id.device_list_lay:
                    case R.id.footer_first_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        if (AppConstants.DEVICE_COUNT > 0) {
                            nextScreen(DevicesList.class);
                        } else {
                            showAlertPopup();
                        }
                        break;

                    case R.id.iot_device_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        mIsWelComeOnBoardFlowBool=false;
                        mIsIOTFlowBool = true;
                        routerMapAPICall();
                        break;

                    case R.id.network_usage_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(NetworkUsage.class);
                        break;

                    case R.id.wifi_speed_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(SpeedTest.class);
                        break;

                    case R.id.alexa_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        mIsWelComeOnBoardFlowBool=false;
                        mIsIOTFlowBool = false;
                        routerMapAPICall();
                        break;

                    case R.id.settings_lay:
                    case R.id.footer_second_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(Settings.class);
                        break;

                    case R.id.logout_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        logoutFromApp();
                        break;

//                    case R.id.my_media_lay:
//                        mDrawerLayout.closeDrawer(GravityCompat.START);
////                        nextScreenWithFinish(MyMedia.class);
//                        break;
//                    case R.id.speed_test_lay:
//                        mDrawerLayout.closeDrawer(GravityCompat.START);
////                        nextScreenWithFinish(SpeedTest.class);
//                        break;

                    case R.id.footer_third_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(RouterMap.class);
                        break;

                }
            }
        });

    }

    private void showAlertPopup() {
        DialogManager.getInstance().showAlertPopup(this, getString(R.string.empty_devices_alert), new InterfaceBtnCallback() {
            @Override
            public void onPositiveClick() {

            }
        });
    }

    /*Populate the values*/
    private void setData(DashboardResponse dashboardResponse) {

        mNameTxt.setText(dashboardResponse.getUser().getFirstName() + " " + dashboardResponse.getUser().getLastName());
        Log.d("alert count", String.valueOf(dashboardResponse.getNotifUnreadCount()));
        AppConstants.ALERT_COUNT = dashboardResponse.getNotifUnreadCount();
        if (dashboardResponse.getNotifUnreadCount() > 0) {
            mNotificationCountLay.setVisibility(View.VISIBLE);
            mNotificationCountTxt.setText(String.valueOf(dashboardResponse.getNotifUnreadCount()));
            mNotificationCountTempTxt.setText(String.valueOf(dashboardResponse.getNotifUnreadCount()));
        } else
            mNotificationCountLay.setVisibility(View.GONE);
        Log.d("not_count", String.valueOf(dashboardResponse.getNotifUnreadCount()));
        mUploadScaleTxt.setText(getString(R.string.mbps));
        mDownloadScaleTxt.setText(getString(R.string.mbps));
        mUploadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(dashboardResponse.getSpeed().getUpload()));
        mDownloadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(dashboardResponse.getSpeed().getDownload()));

        if (dashboardResponse.getUser().getAvatarURL().isEmpty()) {
            mUserProfileImg.setImageResource(R.drawable.default_profile_white);
        } else {
            try {
                Glide.with(this)
                        .load(dashboardResponse.getUser().getAvatarURL())
                        .apply(new RequestOptions().placeholder(R.drawable.default_profile_white).error(R.drawable.default_profile_white))
                        .into(mUserProfileImg);
            } catch (Exception ex) {
                mUserProfileImg.setImageResource(R.drawable.default_profile_white);
                Log.e(AppConstants.TAG, ex.getMessage());
            }
        }
		
        ArrayList<CategoryEntity> categories = dashboardResponse.getCategories();
        ArrayList<CategoryEntity> nonemptycategories = new ArrayList<>();
        for (int j = 0; j < categories.size(); j++) {
            if(categories.get(j).getCount() > 0){
                nonemptycategories.add(categories.get(j));
            }
        }
//        AppConstants.DEVICE_COUNT = dashboardResponse.getDeviceCount();
//        setAdapter(dashboardResponse.getCategories(), dashboardResponse.getDeviceCount());
        AppConstants.DEVICE_COUNT = nonemptycategories.size();
        setAdapter(nonemptycategories, nonemptycategories.size());
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<CategoryEntity> categories, final int deviceCount) {
        ArrayList<List<CategoryEntity>> splittedArray = new ArrayList<>();//This list will contain all the splitted arrays.

        for (int i = 0; i < categories.size(); i += 8) {
            int end = 0;

            if (i + 8 < categories.size()) {
                end = (i + 8);
                Log.d("start", String.valueOf(i));
                Log.d("end", String.valueOf(end));
            } else {
                end = categories.size();
                Log.d("start1", String.valueOf(i));
                Log.d("end1", String.valueOf(end));
            }
            splittedArray.add(categories.subList(i, end));
        }

        pager.setAdapter(new DashboardAdapter(this, splittedArray, deviceCount));
        tabLayout.setupWithViewPager(pager, true);
        if(deviceCount < 8) {
            mMoreTxt.setVisibility(View.INVISIBLE);

        }
    }

    /*Dashboard API call*/
    private void dashboardAPICall() {
        APIRequestHandler.getInstance().dashboardTypeAPICall(this);
    }

    /*routerMapAPICall*/
    private void routerMapAPICall() {
        APIRequestHandler.getInstance().routerMapAPICall(this);
    }

    /*Alexa App API calls*/
    private void alexaAppIdAPICall() {
        APIRequestHandler.getInstance().alexaAppIdsAPICall(this);
    }

    /*calixAgentTokenAPICall*/
    private void calixAgentTokenAPICall() {
        APIRequestHandler.getInstance().calixAgentTokenAPICall(mAlexaAppIdStr, AppConstants.ALEXA_ROUTER_ID, this);
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DashboardResponse) {
            DashboardResponse dashboardResponse = (DashboardResponse) resObj;
            setData(dashboardResponse);
            mIsWelComeOnBoardFlowBool = true;
            routerMapAPICall();
        } else if (resObj instanceof RouterMapResponse) {
            RouterMapResponse routerMapResponse = (RouterMapResponse) resObj;
            if (routerMapResponse.getRouters().size() > 0) {
                AppConstants.ALEXA_FSN = routerMapResponse.getRouters().get(0).getFsanSerialNumber();
                AppConstants.ALEXA_ROUTER_ID = routerMapResponse.getRouters().get(0).getRouterId();

                if (AppConstants.ALEXA_ROUTER_ID.isEmpty()) {

                    if (mIsWelComeOnBoardFlowBool) {
                        DialogManager.getInstance().showOptionPopup(this, getString(R.string.please_on_board_router), getString(R.string.ok), getString(R.string.cancel), new InterfaceTwoBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                                AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS = true;
                                AppConstants.ROUTER_ON_BOARD_FROM_WELCOME = false;
                                AppConstants.ROUTER_DETAILS_ENTITY = new RouterMapEntity();
                                nextScreen(RouterConfiguration.class);
                            }

                            @Override
                            public void onNegativeClick() {

                            }
                        });
                    } else {
                        DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router_settings), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {


                            }
                        });
                    }
                } else if (!mIsWelComeOnBoardFlowBool) {
                    if (mIsIOTFlowBool)
                        alexaAppIdAPICall();
                    else
                        nextScreen(Alexa.class);
                }
            } else {
                if (mIsWelComeOnBoardFlowBool) {
                    DialogManager.getInstance().showOptionPopup(this, getString(R.string.please_on_board_router), getString(R.string.ok), getString(R.string.cancel), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS = true;
                            AppConstants.ROUTER_ON_BOARD_FROM_WELCOME = false;
                            AppConstants.ROUTER_DETAILS_ENTITY = new RouterMapEntity();
                            nextScreen(RouterConfiguration.class);
                        }

                        @Override
                        public void onNegativeClick() {

                        }
                    });
                } else {
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_on_board_router_settings), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {


                        }
                    });
                }
            }
        } else if (resObj instanceof AlexaAppIdResponse) {
            AlexaAppIdResponse alexaAppIdResponse = (AlexaAppIdResponse) resObj;

            ArrayList<AlexaAppIdEntity> deviceArrList = alexaAppIdResponse.getApps();
            for (int posInt = 0; posInt < deviceArrList.size(); posInt++) {
                if (deviceArrList.get(posInt).getName().equalsIgnoreCase(getString(R.string.iothub))) {
                    mAlexaAppIdStr = deviceArrList.get(posInt).getId();
                    break;
                }
            }
            calixAgentTokenAPICall();
        } else if (resObj instanceof CalixAgentResponse) {
            CalixAgentResponse calixAgentResponse = (CalixAgentResponse) resObj;
            if (calixAgentResponse.getToken().isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.calix_agent_not_found), this);
            } else {
                AppConstants.WEB_SOCKET_CALIX_TOKEN=calixAgentResponse.getToken();
                nextScreen(IOTDeviceList.class);
            }
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof DashboardResponse)
                                dashboardAPICall();
                            else if (resObj instanceof RouterMapResponse)
                                routerMapAPICall();
                            else if (resObj instanceof AlexaAppIdResponse)
                                alexaAppIdAPICall();
                            else if (resObj instanceof CalixAgentResponse)
                                calixAgentTokenAPICall();


                        }
                    });
        }
    }

    /*Default back button action*/
    @Override
    public void onBackPressed() {
        hideSoftKeyboard(Dashboard.this);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitFromApp();
        }
    }

    /*App exit popup*/
    private void exitFromApp() {
        DialogManager.getInstance().showOptionPopup(this, getString(R.string.exit_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
            @Override
            public void onPositiveClick() {
                ActivityCompat.finishAffinity(Dashboard.this);
            }

            @Override
            public void onNegativeClick() {

            }
        });

    }

    /*App logout popup*/
    private void logoutFromApp() {
        DialogManager.getInstance().showLogoutPopup(this, new InterfaceTwoBtnCallback() {
            @Override
            public void onNegativeClick() {

            }

            @Override
            public void onPositiveClick() {
                PreferenceUtil.storeBoolPreferenceValue(Dashboard.this, AppConstants.LOGIN_STATUS, false);
                previousScreen(Login.class);
            }
        });

    }


}

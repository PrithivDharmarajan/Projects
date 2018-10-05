package com.calix.calixgigamanage.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.DashboardAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.main.CalixApplication;
import com.calix.calixgigamanage.output.model.AlexaAppIdEntity;
import com.calix.calixgigamanage.output.model.AlexaAppIdResponse;
import com.calix.calixgigamanage.output.model.CalixAgentResponse;
import com.calix.calixgigamanage.output.model.CategoriesEntity;
import com.calix.calixgigamanage.output.model.DashboardResponse;
import com.calix.calixgigamanage.output.model.RouterMapEntity;
import com.calix.calixgigamanage.output.model.RouterMapResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.ui.device.Devices;
import com.calix.calixgigamanage.ui.guest.GuestNetwork;
import com.calix.calixgigamanage.ui.iot.IOTDeviceList;
import com.calix.calixgigamanage.ui.loginregconfig.Login;
import com.calix.calixgigamanage.ui.loginregconfig.RouterWelcome;
import com.calix.calixgigamanage.ui.network.NetworkUsage;
import com.calix.calixgigamanage.ui.parentalcontrol.ParentalControl;
import com.calix.calixgigamanage.ui.settings.Settings;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DateUtil;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;

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

    @BindView(R.id.dashboard_header_bg_lay)
    RelativeLayout mDashboardHeaderBgLay;

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

    @BindView(R.id.slide_profile_img)
    ImageView mSlideProfileImg;

    @BindView(R.id.device_count_txt)
    TextView mDeviceCountTxt;

    /*@BindView(R.id.issue_status_img)
    ImageView mIssueStatusImg;

    @BindView(R.id.issues_count_txt)
    TextView mIssuesCountTxt;

    @BindView(R.id.issues_txt)
    TextView mIssuesTxt;*/


    @BindView(R.id.dash_item_recycler_view)
    RecyclerView mDashBoardItemRecyclerView;

    @BindView(R.id.name_txt)
    TextView mNameTxt;

    @BindView(R.id.wishes_txt)
    TextView mWishesTxt;

    @BindView(R.id.iot_device_view)
    View mIotDeviceView;

    @BindView(R.id.alexa_view)
    View mAlexaView;

    @BindView(R.id.iot_device_lay)
    RelativeLayout mIotDeviceLay;

    @BindView(R.id.alexa_lay)
    RelativeLayout mAlexaLay;

    private String mIOTHubIdStr = "";
    private boolean mIsWelComeOnBoardFlowBool = false, mIsIOTFlowBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_dash_board);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mDrawerLayout);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setHeaderView();

        dashBoardAPICall();


        if (AppConstants.PRODUCTION_BASE_URL.equalsIgnoreCase(PreferenceUtil.getBaseURL(this))) {
            mIotDeviceView.setVisibility(View.GONE);
            mIotDeviceLay.setVisibility(View.GONE);
        }

    }


    /*Set header*/
    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderLeftImg.setImageResource(R.drawable.menu_icon);
        mHeaderTxt.setText(getString(R.string.dashboard));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDashboardHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size190);
                    mDashboardHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Dashboard.this)));
                    mDashboardHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Dashboard.this), 0, 0);
                    mDashboardHeaderBgLay.setBackground(getResources().getDrawable(R.drawable.header_violet_bg));
                }

            });
        }


    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.dashboard_lay, R.id.iot_device_lay, R.id.device_list_lay,
            R.id.my_media_lay, R.id.speed_test_lay, R.id.network_usage_lay, R.id.parental_control_lay,
            R.id.guest_network_lay, R.id.settings_lay, R.id.alexa_lay, R.id.logout_lay, R.id.profile_img_lay,
            R.id.devices_lay, R.id.router_lay})
    public void onClick(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (v.getId()) {
                    case R.id.header_left_img_lay:
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        break;
                    case R.id.dashboard_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.iot_device_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        mIsWelComeOnBoardFlowBool = false;
                        mIsIOTFlowBool = true;
                        routerMapAPICall();
                        break;
                    case R.id.device_list_lay:
                    case R.id.devices_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(Devices.class);
                        break;
                    case R.id.my_media_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
//                        nextScreenWithFinish(MyMedia.class);
                        break;
                    case R.id.speed_test_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(SpeedTest.class);
                        break;
                    case R.id.network_usage_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(NetworkUsage.class);
                        break;
                    case R.id.parental_control_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(ParentalControl.class);
                        break;
                    case R.id.guest_network_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(GuestNetwork.class);
                        break;
                    case R.id.settings_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(Settings.class);
                        break;
                    case R.id.alexa_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        mIsWelComeOnBoardFlowBool = false;
                        mIsIOTFlowBool = false;
                        routerMapAPICall();
                        break;
                    case R.id.logout_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        logoutFromApp();
                        break;
                    case R.id.profile_img_lay:
                    case R.id.router_lay:
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        nextScreen(RouterMap.class);
                        break;

                }
            }
        });

    }

    /*Populate the values*/
    private void setData(DashboardResponse dashboardResponse) {

        mNameTxt.setText(String.format(getString(R.string.hello_user_name), dashboardResponse.getUser().getLastName(), dashboardResponse.getUser().getFirstName()));
        mWishesTxt.setText(DateUtil.getWishesMsg(this));

        mUploadScaleTxt.setText(getString(R.string.mbps));
        mDownloadScaleTxt.setText(getString(R.string.mbps));
        mUploadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(dashboardResponse.getSpeed().getUpload()));
        mDownloadSpeedTxt.setText(NumberUtil.getInstance().speedTwoDigitsValStr(dashboardResponse.getSpeed().getDownload()));

        mDeviceCountTxt.setText(dashboardResponse.getDeviceCount());
//        mIssueStatusImg.setImageResource(Integer.valueOf(dashboardResponse.getNotifUnreadCount()) == 0 ? R.drawable.fixed : R.drawable.issue);
//        mIssuesCountTxt.setText(dashboardResponse.getNotifUnreadCount());
//        mIssuesTxt.setText(getString(Integer.valueOf(dashboardResponse.getNotifUnreadCount()) > 1 ? R.string.issues_found : R.string.issue_found));
        if (PreferenceUtil.getUserId(this).isEmpty() && !dashboardResponse.getUser().getId().isEmpty()) {
            PreferenceUtil.storeStringValue(this, AppConstants.USER_ID, dashboardResponse.getUser().getId());
            CalixApplication.getInstance().pushMsg();
        }

        if (dashboardResponse.getUser().getAvatarURL().isEmpty()) {
            mUserProfileImg.setImageResource(R.drawable.default_profile_white);
            mSlideProfileImg.setImageResource(R.drawable.default_profile_white);
        } else {
            try {
                Glide.with(this)
                        .load(dashboardResponse.getUser().getAvatarURL())
                        .apply(new RequestOptions().placeholder(R.drawable.default_profile_white).error(R.drawable.default_profile_white))
                        .into(mUserProfileImg);
                Glide.with(this)
                        .load(dashboardResponse.getUser().getAvatarURL())
                        .apply(new RequestOptions().placeholder(R.drawable.default_profile_white).error(R.drawable.default_profile_white))
                        .into(mSlideProfileImg);
            } catch (Exception ex) {
                mUserProfileImg.setImageResource(R.drawable.default_profile_white);
                mSlideProfileImg.setImageResource(R.drawable.default_profile_white);
                Log.e(AppConstants.TAG, ex.getMessage());
            }
        }
        setAdapter(dashboardResponse.getCategories());

    }

    /*Set adapter*/
    private void setAdapter(ArrayList<CategoriesEntity> categoriesRes) {
        mDashBoardItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mDashBoardItemRecyclerView.setAdapter(new DashboardAdapter(categoriesRes, this));
        mDashBoardItemRecyclerView.setNestedScrollingEnabled(false);
    }

    /*Dashboard API calls*/
    private void dashBoardAPICall() {

        APIRequestHandler.getInstance().dashboardAPICall(this);
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
        APIRequestHandler.getInstance().calixAgentTokenAPICall(mIOTHubIdStr, AppConstants.ALEXA_ROUTER_ID, this);
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
                                nextScreen(RouterWelcome.class);
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
                            nextScreen(RouterWelcome.class);
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
                    mIOTHubIdStr = deviceArrList.get(posInt).getId();
                    break;
                }
            }
            calixAgentTokenAPICall();
        } else if (resObj instanceof CalixAgentResponse) {
            CalixAgentResponse calixAgentResponse = (CalixAgentResponse) resObj;
            if (calixAgentResponse.getToken().isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.calix_agent_not_found), this);
            } else {
                AppConstants.WEB_SOCKET_CALIX_TOKEN = calixAgentResponse.getToken();
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
                                dashBoardAPICall();
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

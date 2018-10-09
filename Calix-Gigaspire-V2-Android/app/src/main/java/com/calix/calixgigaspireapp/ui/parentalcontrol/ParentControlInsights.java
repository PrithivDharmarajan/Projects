package com.calix.calixgigaspireapp.ui.parentalcontrol;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.devices.DevicesList;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentControlInsights extends BaseActivity {

    @BindView(R.id.parent_control_dashboard_header_bg_lay)
    ViewGroup mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.pc_bg_insights_lay)
    LinearLayout mPcBgHeaderLay;

    @BindView(R.id.pc_total_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.pause_play_internet_img)
    ImageView mPlayPauseImag;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mNotificationLay;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    private boolean mIsChangePlayPauseBtn = false;
    private boolean mPauseAllBool = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_pc_insights);

        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);
        setHeaderView();
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlLay);

        mNotificationLay.setVisibility(View.VISIBLE);

        /*Notification function*/
        if (AppConstants.ALERT_COUNT > 0) {
            mNotificationCountLay.setVisibility(View.VISIBLE);
            mNotificationCountTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
            mNotificationCountTempTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
        } else {
            mNotificationCountLay.setVisibility(View.GONE);
        }
        /*Parent Control Insights Api*/
        //parentControlInsightsAPI();
    }

    private void setHeaderView() {

        /*set header changes*/
        mPcBgHeaderLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.john_insights));


        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentControlInsights.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentControlInsights.this), 0, 0);
                    mPCHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
                }
            });
        }
    }


    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.devices_connect_lay, R.id.insights_connect_lay, R.id.reward_lay, R.id.set_time_lay, R.id.filter_lay, R.id.dashboard_lay, R.id.pause_play_internet_img, R.id.header_right_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;

            case R.id.devices_connect_lay:
                nextScreen(DevicesList.class);
                break;

            case R.id.insights_connect_lay:
                nextScreen(ParentControlInsightsCategory.class);
                break;

            case R.id.reward_lay:
                nextScreen(ParentControlRewards.class);
                break;

            case R.id.set_time_lay:
                nextScreen(ParentControlTime.class);
                break;

            case R.id.filter_lay:
                nextScreen(ParentControlFilter.class);
                break;

            case R.id.dashboard_lay:
                nextScreen(Dashboard.class);
                break;
            case R.id.pause_play_internet_img:
                if (!mIsChangePlayPauseBtn) {
                    mPauseAllBool = true;
                    mPlayPauseImag.setImageResource(R.drawable.play_icon);
                    mIsChangePlayPauseBtn = true;
                } else {
                    mPauseAllBool = true;
                    mPlayPauseImag.setImageResource(R.drawable.pause_icon);
                    mIsChangePlayPauseBtn = false;
                }
                break;
            case R.id.header_right_img_lay:
                nextScreen(Alert.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(ParentalControlDashBoard.class);
    }
}

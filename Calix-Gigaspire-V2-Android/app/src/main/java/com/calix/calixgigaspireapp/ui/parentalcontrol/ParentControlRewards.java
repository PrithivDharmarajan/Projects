package com.calix.calixgigaspireapp.ui.parentalcontrol;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DashboardResponse;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.warkiz.widget.IndicatorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentControlRewards extends BaseActivity {

    @BindView(R.id.rewards_header_bg_lay)
    ViewGroup mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.rewards_status_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.discrete_hours)
    IndicatorSeekBar mSeekBarHour;

    @BindView(R.id.discrete_times)
    IndicatorSeekBar mSeekBarMinutes;

    @BindView(R.id.hours_minutes_txt)
    TextView mHoursTxt;

    @BindView(R.id.minutes_txt)
    TextView mMinutesTxt;

    @BindView(R.id.enable_btn)
    Button mEnableBtn;

    @BindView(R.id.pc_head_bg_lay)
    RelativeLayout mPcHeaderLay;

    @BindView(R.id.pc_insights_bg_lay)
    LinearLayout mPcBgImagLay;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mNotificationLay;

    private DashboardResponse dashboardResponse = new DashboardResponse();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_pc_rewards);

        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

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
        /*Header Lay*/
        setHeaderView();

        /*Hour SeekBar*/
        indiactorSeekBarHour();

        /*Minute SeekBar*/
        indicatorSeekBarMinutes();

        /*User Reward Api*/
        //userRewardAPI();
    }

    private void setHeaderView() {

        /*Header*/
        mPcBgImagLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.rewards));
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPcHeaderLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mPcHeaderLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentControlRewards.this)));
                    mPcHeaderLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentControlRewards.this), 0, 0);
                    mPcHeaderLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
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

    public void indiactorSeekBarHour() {
        mSeekBarHour.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                mHoursTxt.setText(progress + " hrs");
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    public void indicatorSeekBarMinutes() {
        mSeekBarMinutes.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                mMinutesTxt.setText(progress + " min");
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.header_right_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;

            case R.id.header_right_img_lay:
                nextScreen(Alert.class);
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(ParentControlInsights.class);
    }


}

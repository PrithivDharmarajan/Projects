package com.calix.calixgigamanage.ui.parentalcontrol;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.ui.device.Devices;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ParentalControlUser extends BaseActivity {

    @BindView(R.id.control_user_par_lay)
    RelativeLayout mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img)
    ImageView mHeaderLeftImg;

    @BindView(R.id.control_user_header_bg_lay)
    RelativeLayout mControlUserHeaderBgLay;

    @BindView(R.id.control_user_header_msg_lay)
    RelativeLayout mControlUserHeaderLay;

    @BindView(R.id.reward_bottom_lay)
    LinearLayout mRewardBottomLay;

    @BindView(R.id.play_pause_btn_img)
    ImageView mPlayPauseBtnImg;

    private boolean mIsChangePlayPauseBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control_user);
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

        setHeaderView();
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.reward_bottom_lay,R.id.pc_insight_lay, R.id.pc_off_time_lay, R.id.pc_time_limit_lay, R.id.pc_bed_time_lay,R.id.pc_filter_lay,R.id.pc_device_lay,R.id.play_pause_btn_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.reward_bottom_lay:
                nextScreen(ParentalControlReward.class);
                break;
            case R.id.pc_insight_lay:
                nextScreen(ParentalControlInsight.class);
                break;
            case R.id.pc_off_time_lay:
                nextScreen(ParentalControlOffTime.class);
                break;
            case R.id.pc_time_limit_lay:
                nextScreen(ParentalControlTimeLimit.class);
                break;
            case R.id.pc_bed_time_lay:
                nextScreen(ParentalControlBedTime.class);
                break;
            case R.id.pc_filter_lay:
                nextScreen(ParentalControlFilter.class);
                break;
            case R.id.pc_device_lay:
                nextScreen(Devices.class);
                break;
            case R.id.play_pause_btn_img:
                if (!mIsChangePlayPauseBool){
                    mPlayPauseBtnImg.setImageResource(R.drawable.play_img);
                    mIsChangePlayPauseBool = true;
                }else {
                    mPlayPauseBtnImg.setImageResource(R.drawable.pause_img);
                    mIsChangePlayPauseBool = false;
                }
                break;
        }
    }


    private void setHeaderView() {

        /*set header changes*/
        mRewardBottomLay.setVisibility(View.VISIBLE);
        mControlUserHeaderLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.samantha));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mControlUserHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mControlUserHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlUser.this)));
                    mControlUserHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlUser.this), 0, 0);
                    mControlUserHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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

    /*Check screen orientation*/
    protected boolean IsScreenModePortrait() {
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}

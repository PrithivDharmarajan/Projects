package com.calix.calixgigaspireapp.ui.loginregconfig;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.devices.DevicesList;
import com.calix.calixgigaspireapp.ui.guest.GuestNetwork;
import com.calix.calixgigaspireapp.ui.iot.IOTDeviceList;
import com.calix.calixgigaspireapp.ui.settings.PinCodeFingerPrintLogin;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;
import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


public class Splash extends BaseActivity {

    /*Variable initialization using bind view*/

    @BindView(R.id.parent_lay)
    ViewGroup mSplashViewGroup;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_splash);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a Click/touch made outside the edit text*/
        setupUI(mSplashViewGroup);

        /*next screen move*/
        nextScreenCheck();

    }


    private void nextScreenCheck() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                removeHandler();

                Class<?> nextScreenClass = Login.class;

                if (PreferenceUtil.getBoolPreferenceValue(Splash.this, AppConstants.PASS_CODE_ENABLE_STATUS)) {
                    nextScreenClass = PinCodeFingerPrintLogin.class;
                } else
                if (PreferenceUtil.getBoolPreferenceValue(Splash.this, AppConstants.LOGIN_STATUS)) {
                    nextScreenClass = Dashboard.class;
                }

                AppConstants.BASE_URL = PreferenceUtil.getBaseURL(Splash.this);
                sysOut("Base URL---"+AppConstants.BASE_URL);
                nextScreen(nextScreenClass);
//                nextScreen(DevicesList.class);

            }
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 3000);
    }

    private void removeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler();
    }
}

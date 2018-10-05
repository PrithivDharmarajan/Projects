package com.calix.calixgigamanage.ui.loginregconfig;

import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.ui.dashboard.Dashboard;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashScreen extends BaseActivity {

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

                if (PreferenceUtil.getBoolPreferenceValue(SplashScreen.this, AppConstants.PASS_CODE_ENABLE_STATUS)) {
                    nextScreenClass = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M ? PinCodeFingerPrintLogin.class : PinCodeFingerPrintLoginBelow5.class;
                } else if (PreferenceUtil.getBoolPreferenceValue(SplashScreen.this, AppConstants.LOGIN_STATUS)) {
                    nextScreenClass = Dashboard.class;
                }

                AppConstants.BASE_URL = PreferenceUtil.getBaseURL(SplashScreen.this);
//                sysOut("BASE_URL---" + AppConstants.BASE_URL);
                nextScreen(nextScreenClass);
//                nextScreen(IOTDeviceList.class);

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

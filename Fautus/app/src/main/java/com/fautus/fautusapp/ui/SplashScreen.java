package com.fautus.fautusapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.InterfaceTwoBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.parse.ParseInstallation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class implements  UI for loading screen and it wait for 3 sec
 */

public class SplashScreen extends BaseActivity {


    /*Variable initialization using bind view*/

    @BindView(R.id.parent_lay)
    ViewGroup mSplashViewGroup;

    private final Handler mHandler = new Handler();
    private Runnable mRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_splash_screen);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a Click/touch made outside the edit text*/
        setupUI(mSplashViewGroup);


        if (isGooglePlayServicesAvailable(this)) {
            /*Check for internet connection*/
            if (NetworkUtil.isNetworkAvailable(this)) {
                new getDeviceToken().execute();
            } else {
             /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), new InterfaceBtnCallback() {
                    @Override
                    public void onOkClick() {
                        finish();
                    }
                });
            }
        } else {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.play_service_err), getString(R.string.device_not_support), new InterfaceBtnCallback() {
                @Override
                public void onOkClick() {
                    finish();
                }
            });
        }

    }


    private void nextScreenCheck() {

        mRunnable = new Runnable() {
            @Override
            public void run() {
                {
                    ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                    if (parseInstallation != null) {
                        parseInstallation.put(ParseAPIConstants.GCMSenderId, PreferenceUtil.getStringValue(SplashScreen.this, AppConstants.PARSE_DEVICE_ID));
                        parseInstallation.saveInBackground();
                    }
                    /*Remove handler After 3 seconds */
                    mHandler.removeCallbacks(mRunnable);

                   /* If the value of AppConstants.WELCOME_SCREEN_TYPE is one, Then the screen can react in a Consumer signUp . or else, it reacts in a photographer signUp*/
                    AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
                    AppConstants.PROFILE_FROM_MENU = AppConstants.FAILURE_CODE;
                    AppConstants.PHOTOGRAPHER_FROM_MENU = AppConstants.FAILURE_CODE;
                    AppConstants.PUSH_MOMENT_ID = null;
                    AppConstants.PUSH_CHAT_STATUS = null;

                    if (PreferenceUtil.getBoolPreferenceValue(SplashScreen.this, AppConstants.LOGIN_STATUS)) {
                    /*If user logged in already, this process will happen*/
                        nextScreen(HomeScreen.class, true);
                    } else if (PreferenceUtil.getBoolPreferenceValue(SplashScreen.this, AppConstants.CONSUMER_WELCOME_SCREEN_SEEN)) {
                    /*If the user already looked into tutorial screen, it will be directed to signUp screen */
                        nextScreen(SignUpScreen.class, true);
                    } else {
                    /*direct to consumer tutorial Screen */
                        nextScreen(TutorialScreen.class, true);
                    }

                }
            }
        };



        /*set handler to wait for 3 seconds */
        mHandler.postDelayed(mRunnable, 3000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*remove handler*/
        mHandler.removeCallbacks(mRunnable);
    }

    /*To get permission for access image camera and storage*/
    private boolean askPermissions() {
        boolean addPermission = true;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int readStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            addPermission = askAccessPermission(listPermissionsNeeded, new InterfaceTwoBtnCallback() {
                @Override
                public void onYesClick() {
                    nextScreenCheck();
                }

                public void onNoClick() {
                    nextScreenCheck();
                }
            });
        }

        return addPermission;
    }

    private class getDeviceToken extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            return getPushToken();
        }

        @Override
        protected void onPostExecute(String mResult) {
            super.onPostExecute(mResult);
            if (mResult == null || mResult.isEmpty()) {
                new getDeviceToken().execute();
            } else {

                PreferenceUtil.storeStringValue(SplashScreen.this, AppConstants.PARSE_DEVICE_ID, mResult);
                /*direct to next screen*/
                if (askPermissions()) {
                    nextScreenCheck();
                }
            }

        }
    }

    private String getPushToken() {
        String deviceToken = "";
        try {
            deviceToken = InstanceID.getInstance(this)
                    .getToken(getString(R.string.sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deviceToken;
    }


    public boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }
}

package com.smaat.renterblock.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.smaat.renterblock.R;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceTwoBtnCallback;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements InterfaceTwoBtnCallback {

    private Activity mActivity;
    private List<String> mAppPermissionsStrArrList;
    private InterfaceTwoBtnCallback mPermissionCallback = null;
    private int mAskPermissionCountInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*Default Init*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = this;

        /*Init default font*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Helvetica-Light.otf").build());
    }


    /*Apply font plugin default class*/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    /*This method is used to check, if the current view can be focused in the edit text */
    protected void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(mActivity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View mInnerView = ((ViewGroup) view).getChildAt(i);
                setupUI(mInnerView);
            }
        }
    }

    /*Keypad to be hidden when a touch made outside the edit text*/
    protected void hideSoftKeyboard(Activity mActivity) {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (mActivity.getCurrentFocus() != null
                        && mActivity.getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromWindow(mActivity
                            .getCurrentFocus().getWindowToken(), 0);

                }
            }
        } catch (Exception e) {
            Log.e(mActivity.getClass().getSimpleName(), e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        RBApplication.activityResumed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        RBApplication.activityStopped();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*Application control*/
    protected RBApplication app() {
        return ((RBApplication) mActivity.getApplication());
    }


    /*Direct to next activity*/
    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getApplicationContext(), clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        if (finish)
            mActivity.finish();

    }

    /*Redirect to previous activity*/
    protected void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getApplicationContext(), clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

        if (finish)
            mActivity.finish();
    }

    /*Finish current activity*/
    protected void finishScreen() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

    }

    // API Call back Success
    public void onRequestSuccess(Object responseObj) {
    }


    /*API call back failure*/
    public void onRequestFailure(Throwable t) {

        if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {
            DialogManager.getInstance().showAlertPopup(mActivity, getString(R.string.no_internet), this);
        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.getInstance().showAlertPopup(mActivity, getString(R.string.connect_time_out), this);
        } else if (t.getMessage() != null && !t.getMessage().isEmpty()) {
            DialogManager.getInstance().showAlertPopup(mActivity, t.getMessage(), this);
        }
    }


    /*Ask permission for device access*/
    public boolean askAccessPermission(List<String> permissionStrList, int askPermissionCountInt, InterfaceTwoBtnCallback permissionCallback) {
        mAppPermissionsStrArrList = new ArrayList<>();
        mAppPermissionsStrArrList.addAll(permissionStrList);
        mAskPermissionCountInt = askPermissionCountInt;
        mPermissionCallback = permissionCallback;

        if (!mAppPermissionsStrArrList.isEmpty()) {
            ActivityCompat.requestPermissions(this, mAppPermissionsStrArrList.toArray(new String[mAppPermissionsStrArrList.size()]), 200);
            return false;
        }

        return true;
    }

    /*Permission call back*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200: {
                Map<String, Integer> perms = new HashMap<>();
                if (grantResults.length > 0) {
                    boolean isGrantAllPermissionBool = true;
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    for (int j = 0; j < mAppPermissionsStrArrList.size(); j++) {
                        if (perms.get(mAppPermissionsStrArrList.get(j)) == PackageManager.PERMISSION_GRANTED) {
                            if (j == mAppPermissionsStrArrList.size() - 1) {
                                if (isGrantAllPermissionBool)
                                    mPermissionCallback.onPositiveClick();
                                else if (mAskPermissionCountInt == 2)
                                    mPermissionCallback.onNegativeClick();
                                else
                                    askAccessPermission(mAppPermissionsStrArrList, mAskPermissionCountInt + 1, mPermissionCallback);
                            }
                        } else {
                            isGrantAllPermissionBool = false;
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, mAppPermissionsStrArrList.get(j))) {
                                if (perms.get(mAppPermissionsStrArrList.get(j)) == PackageManager.PERMISSION_DENIED) {
                                    DialogManager.getInstance().showOptionPopup(mActivity, mActivity.getString(R.string.go_settings_per), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                                        @Override
                                        public void onPositiveClick() {
                                            Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                            myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                            myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivityForResult(myAppSettings, 168);
                                        }

                                        @Override
                                        public void onNegativeClick() {
                                            mPermissionCallback.onNegativeClick();
                                        }
                                    });
                                    break;

                                } else if (j == mAppPermissionsStrArrList.size() - 1) {
                                    if (mAskPermissionCountInt == 2)
                                        mPermissionCallback.onNegativeClick();
                                    else
                                        askAccessPermission(mAppPermissionsStrArrList, mAskPermissionCountInt + 1, mPermissionCallback);
                                }

                            } else {
                                if (j == mAppPermissionsStrArrList.size() - 1) {
                                    if (mAskPermissionCountInt == 2)
                                        mPermissionCallback.onNegativeClick();
                                    else
                                        askAccessPermission(mAppPermissionsStrArrList, mAskPermissionCountInt + 1, mPermissionCallback);
                                }
                            }
                        }
                    }

                } else if (mPermissionCallback != null) {
                    mPermissionCallback.onNegativeClick();
                }
            }
        }
    }

    /*Interface default ok click*/
    @Override
    public void onPositiveClick() {

    }

    @Override
    public void onNegativeClick() {

    }

    protected void baseActivityAlertDismiss(Dialog dialog) {
        /*To check if the dialog is shown, if the dialog is shown it will be cancelled */
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
    }
}

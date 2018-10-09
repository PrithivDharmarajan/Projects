package com.smaat.spark.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.smaat.spark.R;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.DialogManager;

import java.io.IOException;
import java.net.ConnectException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {


    protected Activity mActivity;
    protected Dialog mBaseDialog;
    protected Animation mShakeAnimation;
    protected Vibrator mVibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Default Init
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = this;

        mShakeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.anim_shake);
        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);

        //Init default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Montserrat-Regular.otf").build());
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


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
            Log.d(AppConstants.TAG, e.toString());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SparkApplication.activityResumed();
        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

    }

    @Override
    protected void onPause() {
        super.onPause();

        SparkApplication.activityStopped();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void sysOut(String msg) {
        System.out.println(msg);
        Log.d(getString(R.string.app_name), msg);
    }

    protected SparkApplication app() {
        return ((SparkApplication) mActivity.getApplication());
    }


    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getApplicationContext(), clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            mActivity.finish();

    }

    protected void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getApplicationContext(), clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        if (finish)
            mActivity.finish();
    }

    protected void finishScreen() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

    }

    public void onRequestSuccess(Object resObj) {
    }

    public void onRequestFailure(Throwable t) {
        try {
            sysOut("errorCode.getCause() Msg--------" + t.getMessage());
            sysOut("errorCode.getCause() --------" + t.getCause().toString());
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }
        if (t instanceof IOException || t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.getInstance().showAlertPopup(mActivity, getString(R.string.no_internet));
        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            DialogManager.getInstance().showAlertPopup(mActivity, getString(R.string.connect_time_out));
        } else {
            DialogManager.getInstance().showAlertPopup(mActivity, t.getMessage());

        }
    }

    protected void shakeAnimEdt(EditText editTextBox, String errorStr) {
        editTextBox.startAnimation(mShakeAnimation);
        editTextBox.requestFocus();
        editTextBox.setError(errorStr);
        mVibrator.vibrate(100);
    }

    protected void baseAlertDismiss() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            try {
                mBaseDialog.dismiss();
            } catch (Exception e) {
                Log.e(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
    }

}

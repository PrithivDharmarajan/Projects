package com.smaat.virtualtrainer.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;

import java.io.IOException;
import java.net.ConnectException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {


    protected Activity mActivity;

    protected RelativeLayout mHeaderLeftBtnLay, mHeaderRightBtnLay, mAdvLay;
    protected ImageView mHeaderLeftImg, mHeaderRightImg;
    protected TextView mHeaderTxt;
    protected AdView mAdView;
    protected AdRequest mAdRequest;
    protected static int mHeaderType;
    protected Animation mEnterFromTop, mExitToTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Default Init
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = this;

        //Init Animation
        mEnterFromTop = AnimationUtils.loadAnimation(mActivity,
                R.anim.slide_in_up);
        mExitToTop = AnimationUtils.loadAnimation(mActivity,
                R.anim.slide_out_up);

        //Init Google Mob Adv
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        //Init default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Roboto-Regular.otf").build());

    }

    private void setHeaderAction() {

        //Init General Header
        mHeaderLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeaderRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

        mHeaderLeftImg = (ImageView) findViewById(R.id.header_left_img);
        mHeaderRightImg = (ImageView) findViewById(R.id.header_right_img);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);


        //Init Google Adv
        mAdView = (AdView) findViewById(R.id.adView);
        mAdvLay = (RelativeLayout) findViewById(R.id.adv_lay);

        //Google Adv loaded
        if (mAdView != null && mAdvLay != null) {
            if (isNetworkAvailable(mActivity)) {
                if (mAdRequest == null) {
                    mAdRequest = new AdRequest.Builder().build();
                }
                mAdvLay.setVisibility(View.VISIBLE);
                mAdView.loadAd(mAdRequest);
            }

            //Google Adv Listener
            mAdView.setAdListener(new AdListener() {

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    mAdvLay.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdvLay.setVisibility(View.VISIBLE);
                }

            });
        }
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


    @Override
    protected void onResume() {
        super.onResume();

        mActivity = this;
        setHeaderAction();

        if (mAdView != null) {
            mAdView.resume();
        }

        VirtualTrainerApplication.activityResumed();

        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
        mActivity.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
        VirtualTrainerApplication.activityStoped();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
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
            e.printStackTrace();
        }

    }

    protected VirtualTrainerApplication app() {
        return ((VirtualTrainerApplication) mActivity.getApplication());
    }


    protected void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(mActivity, clazz);
        if (clazz.getSimpleName().equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            mActivity.finish();

    }

    protected void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(mActivity, clazz);
        if (clazz.getSimpleName().equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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


    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    public void onRequestSuccess(Object responseObj) {
    }


    public void onRequestFailure(Throwable t) {

        try {
            System.out.println("errorCode.getCause() Msg--------" + t.getMessage());
            System.out.println("errorCode.getCause() --------" + t.getCause().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t instanceof IOException ||t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.showBasicAlertDialog(this, getString(R.string.no_internet), getString(R.string.ok), false, getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                @Override
                public void onYesClick() {

                }

                @Override
                public void onNoClick() {

                }
            });

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            DialogManager.showBasicAlertDialog(this, getString(R.string.connect_time_out), getString(R.string.ok), false, getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                @Override
                public void onYesClick() {

                }

                @Override
                public void onNoClick() {

                }
            });
        } else {

            DialogManager.showBasicAlertDialog(this, t.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, new DialogMangerTwoBtnCallback() {
                @Override
                public void onYesClick() {

                }

                @Override
                public void onNoClick() {

                }
            });
        }


    }

    protected boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            @SuppressWarnings("deprecation")
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

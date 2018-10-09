package com.smaat.ipharma.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.androidquery.AQuery;
import com.smaat.ipharma.R;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class BaseActivity extends AppCompatActivity {
    public static Activity mActivity;
    private AQuery aq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Ubuntu-R.ttf").build());
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


    protected AQuery aq() {
        if (aq == null) {
            aq = new AQuery(mActivity);
        }
        return aq;
    }


    public boolean IsConnecttointernet()
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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
    public Bitmap ResizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable",  getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public  void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void launchScreenNoStack(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        finish();
    }


    public void launchScreen(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        mActivity.finish();
    }
    public void launchScreenNoAnim(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.finish();
    }

    public void nextScreen(Class<?> clazz) {

        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    public void nextScreenNoAnim(Class<?> clazz) {

        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);

    }
    public void previousScreen(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        mActivity.finish();
    }

    public void finishScreen() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
    }

    public void sysOut(String msg) {
        System.out.println(msg);
        Log.d(getString(R.string.app_name), msg);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Application.activityStoped();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
        Application.activityResumed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Application.activityStoped();
    }

    public void onrequestSuccess(Object response) {

    }

   /* public void onrequestFailure(RetrofitError retrofitError) {

        //DialogManager.showToast(this, getString(R.string.server_error));

    }*/



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

            DialogManager.showMsgPopup(this,"",getString(R.string.no_internet));

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {


            DialogManager.showMsgPopup(this,"",getString(R.string.connect_time_out));

        } else {
           DialogManager.showMsgPopup(this,"",t.getMessage());

        }



    }
}


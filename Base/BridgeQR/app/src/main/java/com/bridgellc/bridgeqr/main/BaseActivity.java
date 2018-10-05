package com.bridgellc.bridgeqr.main;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridgeqr.R;
import com.bridgellc.bridgeqr.utils.DialogManager;
import com.bridgellc.bridgeqr.utils.DialogMangerOkCallback;
import com.bridgellc.bridgeqr.utils.TypefaceSingleton;

import java.net.ConnectException;

import retrofit.RetrofitError;

public class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    public ViewGroup mViewGroup;
    public RelativeLayout mHeadeLeftBtnLay, mHeadeRightBtnLay;
    public ImageView mHeadeLeftImg, mHeadeRightImg;
    public TextView mHeaderTxt;
    public Typeface mLightFont, mRegularFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mActivity = this;
        mLightFont = TypefaceSingleton.getTypeface().getLightFont(mActivity);
        mRegularFont = TypefaceSingleton.getTypeface().getRegularFont(mActivity);
    }


    public void setupUI(View view) {

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
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
        BridgeQRApplication.activityResumed();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        BridgeQRApplication.activityStoped();
    }


    public void hideSoftKeyboard(Activity mActivity) {
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

    public void setFont(ViewGroup mViewGroup, Typeface mTypeface) {

        int mCount = mViewGroup.getChildCount();
        View mView;
        for (int i = 0; i < mCount; i++) {
            mView = mViewGroup.getChildAt(i);

            if (mView instanceof TextView || mView instanceof Button
                    || mView instanceof EditText/* etc. */) {
                ((TextView) mView).setTypeface(mTypeface);
            }
            if (mView instanceof ViewGroup) {
                setFont((ViewGroup) mView, mTypeface);
            }
        }

    }

    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(mActivity, clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            mActivity.finish();
    }

    public void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(mActivity, clazz);

        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);


        if (finish)
            mActivity.finish();
    }


    public void onRequestSuccess(Object responseObj) {
    }

    public void onRequestFailure(RetrofitError errorCode) {


        if (errorCode.getCause() instanceof ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.app_name), getString(R.string.no_internet), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }

                @Override
                public void onYesClick() {

                }


                @Override
                public void onCancelClick() {

                }
            });
        } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.app_name),
                    getString(R.string.connection_timeout), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }

                        @Override
                        public void onYesClick() {

                        }


                        @Override
                        public void onCancelClick() {

                        }
                    });

        } else {
            DialogManager.showBasicAlertDialog(mActivity, getString(R.string.app_name),
                    getString(R.string.connection_timeout), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }

                        @Override
                        public void onYesClick() {

                        }


                        @Override
                        public void onCancelClick() {

                        }
                    });

        }
    }

}


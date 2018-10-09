package com.smaat.spark.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.INPUT_METHOD_SERVICE;


public class BaseFragment extends Fragment {

    protected Dialog mBaseFragDialog;
    protected Animation mShakeAnimation;
    protected Vibrator mVibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_shake);
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        //Init default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Montserrat-Regular.otf").build());
    }


    protected void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
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

    protected void hideSoftKeyboard() {
        try {
            if (getActivity() != null && !getActivity().isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getActivity()
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (getActivity().getCurrentFocus() != null
                        && getActivity().getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(), 0);

                }
            }
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }

    }

    public void sysOut(String msg) {
        try {

            System.out.println(msg);
            Log.d(getString(R.string.app_name), msg);
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }
    }

    protected SparkApplication app() {
        return ((SparkApplication) getActivity().getApplication());
    }


    public void nextScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getActivity(), clazz);

        getActivity().startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        if (finish)
            getActivity().finish();

    }

    protected void previousScreen(Class<?> clazz, boolean finish) {
        Intent mIntent = new Intent(getActivity(), clazz);

        getActivity().startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        if (finish)
            getActivity().finish();
    }

    protected void finishScreen() {
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.slide_out_right,
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

            DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.no_internet));

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {

            DialogManager.getInstance().showAlertPopup(getActivity(), getResources().getString(R.string.connect_time_out));

        } else {
            DialogManager.getInstance().showAlertPopup(getActivity(), t.getMessage());

        }
    }


    protected void shakeAnimEdt(EditText editTextBox) {
        editTextBox.startAnimation(mShakeAnimation);
        editTextBox.requestFocus();
        mVibrator.vibrate(100);
    }

    protected void baseAlertDismiss() {
        if (mBaseFragDialog != null && mBaseFragDialog.isShowing()) {
            try {
                mBaseFragDialog.dismiss();
            } catch (Exception e) {
                Log.d(AppConstants.DIALOG_TAG, e.getMessage());
            }
        }
    }
}

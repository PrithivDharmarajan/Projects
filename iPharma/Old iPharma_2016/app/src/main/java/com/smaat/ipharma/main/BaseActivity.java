package com.smaat.ipharma.main;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.ipharma.R;
import com.smaat.ipharma.database.DatabaseHelper;
import com.smaat.ipharma.database.DatabaseManager;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.TypefaceSingleton;

import retrofit.RetrofitError;

public class BaseActivity extends AppCompatActivity implements DialogMangerCallback {

    private static Activity mActivity;
    public static Dialog mDialog;
    protected Typeface mHelvetica, mHelveticaBold, mHelveticaLight, mHighTower;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mActivity = this;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        this.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        DatabaseManager.initialize(getApplicationContext(), new DatabaseHelper(
                this));

        mHelvetica = TypefaceSingleton.getInstance().getHelvetica(
                mActivity);
        mHelveticaBold = TypefaceSingleton.getInstance().getHelveticaBold(
                mActivity);
        mHelveticaLight = TypefaceSingleton.getInstance().getHelveticaLight(
                mActivity);
        mHighTower = TypefaceSingleton.getInstance().getHighTower(
                mActivity);

    }


    protected AQuery aq() {
        if (aq == null) {
            aq = new AQuery(mActivity);
        }
        return aq;
    }

    //Keypad call method checking
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

    //KeyPad Hidden Call
    public void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null && !activity.isFinishing()) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);

                if (activity.getCurrentFocus() != null
                        && activity.getCurrentFocus().getWindowToken() != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity
                            .getCurrentFocus().getWindowToken(), 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Font Style Apply call
    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button /* etc. */)
                ((TextView) v).setTypeface(font);
            else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public void launchActivity(Class<?> clazz) {
        Intent intent = new Intent(mActivity, clazz);

        mActivity.startActivity(intent);

        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        mActivity.finish();
    }

    public void launchActivityBackAnim(Class<?> clazz) {
        Intent intent = new Intent(mActivity, clazz);

        mActivity.startActivity(intent);

        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        mActivity.finish();
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

    public void finishScreen() {

        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);

    }

    @Override
    protected void onPause() {
        IPharmaApplication.activityStoped();
        super.onPause();
    }

    @Override
    protected void onResume() {
        IPharmaApplication.activityResumed();
        super.onResume();
    }

    public void onRequestSuccess(Object responseObj) {

    }


        public void onRequestFailure(RetrofitError errorCode) {


            try {
                System.out.println("errorCode.getCause() --------" + errorCode.getCause().toString());
                System.out.println("errorCode.getCause() Msg--------" + errorCode.getCause().getMessage().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (errorCode.getCause() instanceof java.net.ConnectException || errorCode.getCause() instanceof java.net.UnknownHostException) {

                //No internet connection and  UnknownHostException
                DialogManager.showCustomAlertDialog(mActivity, this, getString(R.string.no_network));

            } else if (errorCode.getCause() instanceof java.net.SocketTimeoutException) {
                //SocketTimeoutException
                DialogManager.showCustomAlertDialog(mActivity, this, getString(R.string.conn_time_out));

            } else if (errorCode.getCause() instanceof retrofit.converter.ConversionException) {
                //ConversionException - Response is different fomat and decl format is different
                DialogManager.showCustomAlertDialog(mActivity, this, getString(R.string.serv_con_exce));
            } else {
                //Other Exception...
                DialogManager.showCustomAlertDialog(mActivity, this, getString(R.string.serv_not_res));

            }


        }




    public void onRequestFailure(String errorCode) {

        if (errorCode == null) {
            DialogManager.showPopUpDialog(mActivity, this,
                    getString(R.string.data_issue));
        } else if (errorCode.equals("-101")) {
            DialogManager.showPopUpDialog(mActivity, this,
                    getString(R.string.no_network));
        } else {
            DialogManager.showPopUpDialog(mActivity, this,
                    getString(R.string.server_unreachable));
        }
    }

    @Override
    public void onItemclick(String SelctedItem, int pos) {

    }

    @Override
    public void onOkclick() {

    }

}
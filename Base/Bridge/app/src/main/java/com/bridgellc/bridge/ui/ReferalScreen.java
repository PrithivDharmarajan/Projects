package com.bridgellc.bridge.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.utils.AppConstants;

/**
 * Created by admin on 7/15/2016.
 */
public class ReferalScreen extends BaseActivity {


    private boolean shareOpened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_referal_screen);

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        String shareBody = "Join Base! Come to one place to get everything you need & make money!";
//        //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    protected void onResume() {
        super.onResume();


        if (shareOpened) {
            shareOpened = false;
//            GlobalMethods.shareTxt(this);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string
                    .share_txt) + "\n\n\t" + getString(R.string.android_app) + " " + AppConstants.BASE_APP
                    + "\n\n\t" + getString(R.string.ios_app) + " " + AppConstants.IOS_BASE_APP);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else {
            shareOpened = true;

//            DialogManager.showProgress(mActivity);
//            DialogManager.hideProgress(mActivity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) ReferalScreen.this
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    finish();
                }
            }, 300);
        }


    }
//    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//        super.onActivityResult(arg0, arg1, arg2);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                System.out.println("Keypad Hidden---");
//                InputMethodManager inputManager = (InputMethodManager) mActivity
//                        .getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(ReferalScreen.this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//
//            }
//        }, 300);
//    }


}

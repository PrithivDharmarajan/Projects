package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.adapter.ReviewAdapter;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.ReviewResponce;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

/**
 * Created by USER on 3/15/2016.
 */
public class ReviewScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private ListView mReviewList;
    private ReviewAdapter mAdapter;
    private ImageView mArrowImage;
    public static String mOtherUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_screen);
        initComponents();
        callReviewApi();
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mReviewList = (ListView) findViewById(R.id.review_list);

        mHeaderTxt.setText(getString(R.string.reviews).toUpperCase(Locale.ENGLISH));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);


    }

    private void callReviewApi() {

        APIRequestHandler.getInstance().getRatings(mOtherUserId, ReviewScreen.this);
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof ReviewResponce) {

            ReviewResponce mResponse = (ReviewResponce) responseObj;
            if (mResponse.getResponse_code().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                if (mResponse.getResult() != null && mResponse.getResult().size() > 0) {
                    mAdapter = new ReviewAdapter(this, mResponse.getResult());
                    mReviewList.setAdapter(mAdapter);
                } else {

                    String msg = getString(R.string.no_reviews);
                    if (GlobalMethods.getUserID(ReviewScreen.this).equalsIgnoreCase(mOtherUserId)) {
//                        msg = getString(R.string.you_dont_any_rev);
                        msg=getString(R.string.no_reviews);
                    }
                    DialogManager.showBasicAlertDialog(this, msg, new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            onBackPressed();
                        }
                    });
                }
            } else {
                DialogManager.showBasicAlertDialog(this,
                        mResponse.getMessage(), this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.one))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(HomeScreenActivity.class, true);
        } else if (AppConstants.FROME_NOTIFICATION.equalsIgnoreCase(getString(R.string.two))) {
            AppConstants.FROME_NOTIFICATION = AppConstants.FAILURE_CODE;
            previousScreen(NotificationScreen.class, true);
        } else {
            finishScreen();
        }
    }

    @Override
    public void onOkClick() {

    }
}

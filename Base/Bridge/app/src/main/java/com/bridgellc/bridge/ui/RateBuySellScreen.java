package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

import retrofit.RetrofitError;

/**
 * Created by Dell on 3/22/2016.
 */
public class RateBuySellScreen extends BaseActivity implements View.OnClickListener,
        DialogMangerOkCallback {

    private Button mPostbtn;
    public static String mHeader;
    private EditText mRatCmdEdt;
    private String mRatCmd;
    private RatingBar mUser_ratingbar;
    private String mRatingCount;
    private TextView mProfileNameTxt;
    private String mItemId;
    private String mOtherUserId;
    private String mRateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_ratebuyer_screen);

        initComponents();
    }

    private void initComponents() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup,mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mPostbtn = (Button) findViewById(R.id.footer_btn);

        mProfileNameTxt = (TextView) findViewById(R.id.profile_name_txt);
        mUser_ratingbar = (RatingBar) findViewById(R.id.user_ratingbar);
        mRatCmdEdt = (EditText) findViewById(R.id.rat_cmd_edt);

        mPostbtn.setText(getString(R.string.post));
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);


        mItemId = AppConstants.RATING_ITEM_ID;
        mOtherUserId = AppConstants.RATING_USER_ID;
        mRateUser = AppConstants.RATING_USER_NAME;


        //Set Data
        mHeaderTxt.setText(mHeader.toUpperCase(Locale.ENGLISH));
        mProfileNameTxt.setText(mRateUser);
        mUser_ratingbar.setRating(GlobalMethods.isRating(AppConstants.RATING_RATE));
        mRatCmdEdt.setText(AppConstants.RATING_CMD);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_btn:

                mRatCmd = mRatCmdEdt.getText().toString().trim();
                mRatingCount = String.valueOf(mUser_ratingbar.getRating());

               if (mRatCmd.isEmpty()) {
                    DialogManager.showBasicAlertDialog(this, getString(R.string
                            .enter_comment), this);
                } else if (mRatingCount.equalsIgnoreCase("") || mRatingCount.equalsIgnoreCase(AppConstants.FAILURE_CODE) || mRatingCount.equalsIgnoreCase("0.0")) {
                    DialogManager.showBasicAlertDialog(this, getString(R.string
                            .enter_rate), this);
                } else {
                    APIRequestHandler.getInstance().postRating(mOtherUserId, mRatingCount, mRatCmd, mItemId,
                            this);
                }

                break;
        }
    }



    @Override
    public void onRequestFailure(RetrofitError errorCode) {
        super.onRequestFailure(errorCode);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CommonResponse) {
            CommonResponse mUserResponse = (CommonResponse) responseObj;
            if (mUserResponse.getResponse_code().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                DialogManager.showBasicAlertDialog(this, mUserResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {
                        onBackPressed();
                    }
                });
            } else {
                DialogManager.showBasicAlertDialog(this, mUserResponse.getMessage(), this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (AppConstants.RATING_BACK.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
            AppConstants.RATING_BACK = AppConstants.FAILURE_CODE;
            finishScreen();
        } else {
            previousScreen(HomeScreenActivity.class, true);
        }
    }

    @Override
    public void onOkClick() {
    }
}

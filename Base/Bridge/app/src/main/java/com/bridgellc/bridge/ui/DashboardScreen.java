package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.ProfileEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.ProfileResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

/**
 * Created by USER on 3/18/2016.
 */
public class DashboardScreen extends BaseActivity implements View.OnClickListener,
        DialogMangerOkCallback {

    private ImageView mHeaderRightImage, mVerifyImg;
    private TextView mProfileNameTxt, mBecmPartnerTxt, mUniversityNameTxt, mSellingItemTxt, mBuyingItemTxt, mRequestItemTxt,
            mBidingItemTxt, mPaymentItemTxt;
    private RatingBar mUserRatingbar;
    public static ProfileEntity mProfiledata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_screen);
        initComponents();
    }


    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);


        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderRightImage.setImageResource(R.drawable.notification_icon);

        mUserRatingbar = (RatingBar) findViewById(R.id.user_ratingbar);
        mProfileNameTxt = (TextView) findViewById(R.id.profile_name_txt);
        mUniversityNameTxt = (TextView) findViewById(R.id.university_name_txt);
        mBecmPartnerTxt = (TextView) findViewById(R.id.becm_partner_txt);

        mSellingItemTxt = (TextView) findViewById(R.id.selling_item_txt);
        mBuyingItemTxt = (TextView) findViewById(R.id.buying_txt);
        mRequestItemTxt = (TextView) findViewById(R.id.request_txt);
        mBidingItemTxt = (TextView) findViewById(R.id.biding_txt);
        mPaymentItemTxt = (TextView) findViewById(R.id.payment_txt);

        mVerifyImg = (ImageView) findViewById(R.id.verify_img);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.INVISIBLE);
        mHeaderTxt.setText(getString(R.string.my_dashboard).toUpperCase(Locale.ENGLISH));


        //Profile API
        APIRequestHandler.getInstance().getMyProfileResponse(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;

            case R.id.buying_lay:

                AppConstants.PROFILE_HEADER = getString(R.string.my_buying);
                nextScreen(ProfileListScreen.class, true);
                break;
            case R.id.selling_lay:

                AppConstants.PROFILE_HEADER = getString(R.string.my_selling);
                nextScreen(ProfileListScreen.class, true);
                break;
            case R.id.request_lay:

                AppConstants.PROFILE_HEADER = getString(R.string.my_req);
                nextScreen(ProfileListScreen.class, true);
                break;
            case R.id.bidding_lay:

                AppConstants.PROFILE_HEADER = getString(R.string.my_bidding);
                nextScreen(ProfileListScreen.class, true);
                break;
            case R.id.payment_lay:

                AppConstants.PROFILE_HEADER = getString(R.string.payments_c);
                //nextScreen(PaymentScreen.class, true);
                nextScreen(RecentActivityScreen.class, true);
                break;

            case R.id.view_al_review_txt:
                ReviewScreen.mOtherUserId = GlobalMethods.getUserID(this);
                nextScreen(ReviewScreen.class, false);
                break;

            case R.id.header_right_btn_lay:
                nextScreen(NotificationScreen.class, true);
                break;
            case R.id.becm_partner_txt:
                if (mProfiledata != null && mProfiledata.getPartner() != null && mProfiledata.getPartner().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    nextScreen(PartnerScreen.class, true);
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {

        previousScreen(HomeScreenActivity.class, true);
//        finishScreen();
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof ProfileResponse) {
            ProfileResponse profileres = (ProfileResponse) responseObj;
            if (profileres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                mProfiledata = profileres.getResult();
                setData();

            } else {
                DialogManager.showBasicAlertDialog(this,
                        profileres.getMessage(), this);
            }
        }
    }

    private void setData() {
        if (mProfiledata.getUser_rating() != null && !mProfiledata.getUser_rating()
                .equalsIgnoreCase("")) {
            mUserRatingbar.setRating(Float.parseFloat(mProfiledata.getUser_rating()));
        } else {
            mUserRatingbar.setRating(0.0f);
        }

//        if (mProfiledata.getPartner().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
        mBecmPartnerTxt.setText(mProfiledata.getPartner().equalsIgnoreCase(AppConstants
                .FAILURE_CODE) ? getString(R.string.become_par) : getString(R.string.you_r_partner));
//        } else {
//            mBecmPartnerTxt.setText(getString(R.string.you_r_partner));
        GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.PARTNER, mProfiledata.getPartner());
//        }


        mVerifyImg.setVisibility(mProfiledata.getUser_verified().equalsIgnoreCase(getString(R
                .string.one)) ? View.VISIBLE : View.INVISIBLE);
        mProfileNameTxt.setText(mProfiledata.getFirst_name());
        mUniversityNameTxt.setText(GlobalMethods.getStringValue(this, AppConstants.USER_UNIVERSITY_NAME));
        mSellingItemTxt.setText(itemCount(mProfiledata.getSell_count()));
        mBuyingItemTxt.setText(itemCount(mProfiledata.getBuy_count()));
        mRequestItemTxt.setText(itemCount(mProfiledata.getRequest_count()));
        mBidingItemTxt.setText(itemCount(mProfiledata.getBid_count()));
        mPaymentItemTxt.setText(itemCount(mProfiledata.getPaymnet_count()));
        mPaymentItemTxt.setVisibility(View.INVISIBLE);

    }

    private String itemCount(String mItem) {
        String returnItem = mItem + " " + getString(R.string.items);

        if (mItem.equalsIgnoreCase(getString(R.string.zero)) || mItem.equalsIgnoreCase(getString(R.string.one)))
            returnItem = mItem + " " + getString(R.string.ite);
        return returnItem;
    }

    @Override
    public void onOkClick() {

    }
}

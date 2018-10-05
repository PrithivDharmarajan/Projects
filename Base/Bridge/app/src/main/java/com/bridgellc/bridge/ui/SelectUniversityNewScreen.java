package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.SignInEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.SignUpResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

/**
 * Created by admin on 7/15/2016.
 */
public class SelectUniversityNewScreen extends BaseActivity {

    private TextView mTopTxt, mEduHintTxt;
    public static TextView mSelectUnivTxt;
    private EditText mEditTxt;
    private String mDeviceId;
    private SignInEntity mSignin;
    public static String mFromSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_signup_otp_screen);

        initComponents();
    }

    private void initComponents() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);

        mFooterOneLay = (RelativeLayout) findViewById(R.id.footer_one_lay);
        mFooterTwoLay = (RelativeLayout) findViewById(R.id.footer_two_lay);
        mFooterThreeLay = (RelativeLayout) findViewById(R.id.footer_three_lay);

        mFooterOneBtn = (Button) findViewById(R.id.footer_one_btn);
        mFooterTwoBtn = (Button) findViewById(R.id.footer_two_btn);
        mFooterThreeBtn = (Button) findViewById(R.id.footer_three_btn);

        mTopTxt = (TextView) findViewById(R.id.title_txt);
        mEditTxt = (EditText) findViewById(R.id.input_edt);
        mEduHintTxt = (TextView) findViewById(R.id.edu_hint_txt);
        mSelectUnivTxt = (TextView) findViewById(R.id.input_txt);
        mSelectUnivTxt.setText(getString(R.string.sel_univ_sc));

        if (AppConstants.SIGNUP_DIRECT.equalsIgnoreCase(getString(R.string.sign_up))) {
            mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        } else {
            mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
        }

        mTopTxt.setVisibility(View.VISIBLE);
        mEduHintTxt.setVisibility(View.GONE);
        mEditTxt.setVisibility(View.GONE);
        mSelectUnivTxt.setVisibility(View.VISIBLE);
        mFooterOneLay.setVisibility(View.VISIBLE);
        mFooterTwoLay.setVisibility(View.GONE);
        mFooterThreeLay.setVisibility(View.GONE);

        mHeaderTxt.setText(getString(R.string.sele_school_unv).toUpperCase(Locale.ENGLISH));
        mTopTxt.setText(getString(R.string.sele_unvi));
        mFooterOneBtn.setText(getString(R.string.next));

        SelectUniversityScreen.mFromSignup = getString(R.string.sign_up);
        mFromSignup = getString(R.string.sign_up);

        if (!AppConstants.SELECT_SCH_UNI.equalsIgnoreCase(getString(R.string.zero)) && !AppConstants.SELECT_SCH_UNI.equalsIgnoreCase("SELECT_SCH_UNI")) {
            mSelectUnivTxt.setText(AppConstants.SELECT_SCH_UNI);
        }

        mDeviceId = GlobalMethods.getStringValue(this, AppConstants.DEVICE_ID);
    }


    public void onClick(View v) {
        String mSelectUniv = mSelectUnivTxt.getText().toString().trim();
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.input_txt:
                nextScreen(SelectUniversityScreen.class, false);
                break;
            case R.id.footer_one_btn:

                if (mSelectUniv.isEmpty() || mSelectUnivTxt.getText().toString().trim().equalsIgnoreCase(getString(R.string.sel_univ_sc))) {
                    DialogManager.showBasicAlertDialog(SelectUniversityNewScreen.this, getString(R.string
                            .enter_university), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });
                } else {
                    if (AppConstants.SIGNUP_DIRECT.equalsIgnoreCase(getString(R.string.sign_up))) {
                        APIRequestHandler.getInstance().getSignUpResponse(AppConstants.SIGNUP_FIRSTNAME, AppConstants.SIGNUP_LASTNAME, AppConstants.SIGNUP_EMAIL,
                                GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM), AppConstants.SIGNUP_PWD, AppConstants.SELECT_SCH_UNI_ID, "01-01-1990", getString(R.string.one), GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE), mDeviceId, "", getString(R.string.three),
                                this);

                    } else {
                        APIRequestHandler.getInstance().getUpdateProfileResponse(AppConstants
                                        .SIGNUP_FIRSTNAME, AppConstants.SIGNUP_LASTNAME, AppConstants.SIGNUP_EMAIL, "",
                                GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM), AppConstants.SIGNUP_PWD, AppConstants.SELECT_SCH_UNI_ID, "01-01-1990", getString(R.string.one), GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE), GlobalMethods.getStringValue(this, AppConstants.FB_ID), getString(R.string.three),
                                this);
                    }
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (AppConstants.SIGNUP_DIRECT.equalsIgnoreCase(getString(R.string.sign_up))) {
            previousScreen(SignUpScreen.class, true);
        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof SignUpResponse) {
            final SignUpResponse signupres = (SignUpResponse) responseObj;

            if (signupres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                UpdateProfileScreen.mHeader = getString(R.string.sign_up);
                mSignin = signupres.getResult();
//                DialogManager.showBasicAlertDialog(UpdateProfileScreen.this, mHeader, getString(R.string
//                        .serv_res_success), new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {
//                        GlobalMethods.userDetails(true, signupres.getUser_id(), mFirstName,
//                                mLastName, mEmail, mPhNum, mPwd, mSchoolUnivTxt.getText()
//                                        .toString(), mDOB, mGender, UpdateProfileScreen.this);
                GlobalMethods.userDetails(true, mSignin.getUser_id(), mSignin.getFirst_name(),
                        mSignin.getLast_name(), mSignin.getEmail_id(), mSignin.getEdu_email_id(), mSignin.getPhone_number(), mSignin.getPassword(), mSignin.getUniversity_name(), mSignin.getUniversity_id(), mSignin.getDob(), mSignin.getGender(), mSignin.getLogin_type(), mSignin.getBank_details(), mSignin.getCard_details(), mSignin.getPayment_mode(), mSignin.getPayment_details(), mSignin.getPartner(), SelectUniversityNewScreen.this);
                AppConstants.SIGNUP_DIRECT = AppConstants.FAILURE_CODE;
                if (mSignin.getMobile_verify().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    PhOTPEmailScreen.mScreenShows = 2;
                    PhOTPEmailScreen.mFBSignupReg = getString(R.string.three);
                    PhOTPEmailScreen.mOTP = mSignin.getOtp();
                    PhOTPEmailScreen.mOldPh = mSignin.getPhone_number();
                    GlobalMethods.storeValuetoPreference(SelectUniversityNewScreen.this,
                            GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM,
                            mSignin.getNew_phone());
                    nextScreen(PhOTPEmailScreen.class, false);
                } else {

                    if (!GlobalMethods.isEmailValid(AppConstants.SIGNUP_EMAIL,
                            true)) {

                        if (mFromSignup.equalsIgnoreCase(getString(R.string.sign_up))) {
                            AppConstants.SIGNUPVERIFYBACK = getString(R.string.sign_up);
                            PhOTPEmailScreen.mScreenShows = 3;
                            nextScreen(PhOTPEmailScreen.class, true);
                        } else {
                            if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                                nextScreen(HomeScreenActivity.class, true);
                            } else {
                                onBackPressed();
                            }
                        }
                    } else {
                        if (mFromSignup.equalsIgnoreCase(getString(R.string.sign_up)) || AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                            nextScreen(HomeScreenActivity.class, true);
                        } else
                            onBackPressed();
                    }
                }
//                    }
//                });
            } else {
                DialogManager.showBasicAlertDialog(this,
                        signupres.getMessage(), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });

            }
        }
    }
}

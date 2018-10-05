package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.CommonResponse;
import com.bridgellc.bridge.model.UpdatePhNumResponse;
import com.bridgellc.bridge.model.VerifyNumResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

/**
 * Created by SmaatApps on 24-Mar-16.
 */
public class PhOTPEmailScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private TextView mTopTxt, mEduHintTxt;
    private EditText mEditTxt;
    private String mEdit;
    public static int mScreenShows;
    public static String mFBSignupReg = "";
    public static String mOTP = "", mOldPh = "";

    private String mOTPCall = AppConstants.FAILURE_CODE;


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

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        setData();

    }

    private void setData() {
        mEduHintTxt.setVisibility(View.GONE);
        if (mScreenShows == 1) {
            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.GONE);
            mFooterThreeLay.setVisibility(View.GONE);


            if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("Fb")) {

                mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
            } else {

                mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
            }


            mHeaderTxt.setText(getString(R.string.phone_num).toUpperCase(Locale.ENGLISH));
            mTopTxt.setText(getString(R.string.enter_your_ph));
            mEditTxt.setHint(R.string.ph_num_hint);
            mEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            if (!GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM).equalsIgnoreCase("")) {
                mEditTxt.setText(GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM));
            } else {
                mEditTxt.setText("");
            }

            mEditTxt.setInputType(InputType.TYPE_CLASS_PHONE);
            mFooterOneBtn.setText(getString(R.string.submit));

        } else if (mScreenShows == 2) {
            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.GONE);
            mFooterThreeLay.setVisibility(View.GONE);
            if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("Fb")) {

                mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
            } else {

                mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
            }

            mHeaderTxt.setText(getString(R.string.verification).toUpperCase(Locale.ENGLISH));
            mTopTxt.setText(getString(R.string.otp_msg));
            mEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            mEditTxt.setInputType(InputType.TYPE_CLASS_PHONE);
            mEditTxt.setText("");
            mEditTxt.setHint(R.string.enter_otp);
            mFooterOneBtn.setText(getString(R.string.verify));
        } else {
            mFooterOneLay.setVisibility(View.VISIBLE);
            mFooterTwoLay.setVisibility(View.VISIBLE);
//            mEduHintTxt.setVisibility(View.VISIBLE);
            mFooterThreeLay.setVisibility(View.GONE);

//            if (UpdateProfileScreen.mHeader.equalsIgnoreCase(getString(R.string.sign_up_c))) {

            mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);
//            } else {
//
//                mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
//            }

            mFooterOneLay.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg));
            mFooterTwoLay.setBackgroundColor(getResources().getColor(R.color.green));
//            mFooterTwoBtn.setTextColor(getResources().getColor(R.color.blue_btn_bg));

            mHeaderTxt.setText(getString(R.string.edu_email).toUpperCase(Locale.ENGLISH));
            mTopTxt.setText(getString(R.string.edu_email_ph));
            mEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
            mEditTxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            mEditTxt.setText("");
            mEditTxt.setHint(R.string.type_here);
            mFooterOneBtn.setText(getString(R.string.verify));
            mFooterTwoBtn.setText(getString(R.string.skip));

        }
    }

    public void onClick(View v) {
        mEdit = mEditTxt.getText().toString().trim();
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.footer_one_btn:
                if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.submit))) {

                    if (mEdit.isEmpty()) {
                        DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, getString(R.string.enter_phone), this);
                    } else {
                        APIRequestHandler.getInstance().getUpdatePhNumResponse(mEdit, this);
                    }
                } else if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.verify))) {

                    if (mScreenShows == 2) {

//                        if (mEdit.isEmpty() || !mEdit.equalsIgnoreCase(mOTP)) {
                        if (mEdit.isEmpty()) {
                            DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, getString(R.string.enter_val_otp), this);
                        } else {
                            mOTPCall = getString(R.string.two);
                            APIRequestHandler.getInstance().getVerifyNumResponse(GlobalMethods.getStringValue(this, AppConstants.PHONE_NUM), mEdit, PhOTPEmailScreen.this);
                        }
                    } else {
                        if (mFooterOneBtn.getText().toString().trim().equalsIgnoreCase(getString(R.string.verify))) {
                            if (mEdit.isEmpty() || !GlobalMethods.isEmailValid(mEdit, true)) {
                                DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, getString(R.string.enter_edu_email), this);
                            } else {
                                APIRequestHandler.getInstance().getUpdateMailResponse(mEdit, this);
                            }
                        }
                    }
                }

                break;
            case R.id.footer_two_btn:

//                GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
//                        GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
//                        true);

                if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb") || UpdateProfileScreen.mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
                    if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                        UpdateProfileScreen.mHeader = getString(R.string.update_profile);
                        UpdateProfileScreen.mSignUp = getString(R.string.update);
                        finishScreen();
                    } else
                        nextScreen(HomeScreenActivity.class, true);
                } else {
                    onBackPressed();
                }
                break;

        }
    }

    private void nxtScreen(int screennum) {
        mScreenShows = screennum;
        setData();
    }

    @Override
    public void onBackPressed() {
        if (mScreenShows == 1) {
            if (!AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("Fb")) {
                previousScreen(SignInScreen.class, true);
            }
        } else if (mScreenShows == 2) {
            if (mFBSignupReg.equalsIgnoreCase(getString(R.string.three))) {
                GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
                        GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM,
                        mOldPh);
                PhOTPEmailScreen.mFBSignupReg = AppConstants.FAILURE_CODE;
                finishScreen();
            } else if (!AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("Fb")) {
                nxtScreen(1);
            }
        } else if (mScreenShows == 3) {
            if (!UpdateProfileScreen.mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
//                previousScreen(UpdateProfileScreen.class, true);
                finishScreen();
            }
        }


    }

//    private void nxtHomeScreen() {
//        DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, mHeaderTxt.getText().toString().trim(), getString(R.string.serv_res_success), new DialogMangerOkCallback() {
//            @Override
//            public void onOkClick() {
//                GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
//                        GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
//                        true);
//                GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
//                        GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
//                        mEdit);
//                nextScreen(HomeScreenActivity.class, true);
//            }
//        });
//    }

    @Override
    public void onOkClick() {

    }

//    @Override
//    protected void onResume() {
//        super.onResume();+
//        nxtScreen(mScreenShows);
//    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof UpdatePhNumResponse) {
            final UpdatePhNumResponse updatephnumres = (UpdatePhNumResponse) responseObj;

            if (updatephnumres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {


//                DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, mHeaderTxt.getText().toString().trim(), getString(R.string.otp_sent), new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {

                mOTPCall = getString(R.string.one);
                APIRequestHandler.getInstance().getVerifyNumResponse(mEdit, "", PhOTPEmailScreen.this);
//                        nxtScreen(2);
//                        GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
//                                GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM,
//                                mEdit);

//                    }


//                });


            } else {
                DialogManager.showBasicAlertDialog(this, updatephnumres.getMessage(), this);

            }
        } else if (responseObj instanceof CommonResponse) {
            final CommonResponse updateemailres = (CommonResponse) responseObj;

            if (updateemailres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {


                DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, updateemailres.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                        GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
                                GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
                                mEdit);
                        if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb") || UpdateProfileScreen.mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {
                            if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                                UpdateProfileScreen.mHeader = getString(R.string.update_profile);
                                UpdateProfileScreen.mSignUp = getString(R.string.update);
                                finishScreen();
                            } else
                                nextScreen(HomeScreenActivity.class, true);
                        } else {
                            onBackPressed();
                        }


                    }


                });


            } else {
                DialogManager.showBasicAlertDialog(this, updateemailres.getMessage(), this);

            }
        } else if (responseObj instanceof VerifyNumResponse) {
            final VerifyNumResponse phOtpres = (VerifyNumResponse) responseObj;

            if (phOtpres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                if (mOTPCall.equalsIgnoreCase(getString(R.string.two))) {

                    DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, getString(R.string.otp_verif), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {
                            if (AppConstants.SIGNUPVERIFYBACK.equalsIgnoreCase("fb")) {
                                UpdateProfileScreen.mHeader = getString(R.string.update_profile);
                                UpdateProfileScreen.mSignUp = getString(R.string.update);
                            }

                            if (mFBSignupReg.equalsIgnoreCase(getString(R.string.one))) {
                                mFBSignupReg = AppConstants.FAILURE_CODE;
                                nextScreen(HomeScreenActivity.class, true);
                            } else if (mFBSignupReg.equalsIgnoreCase(getString(R.string.three))) {
                                mFBSignupReg = AppConstants.FAILURE_CODE;
                                finishScreen();
                            } else {

                                if (AppConstants.SIGNUP_DIRECT.equalsIgnoreCase(getString(R.string.update))) {

                                    SelectUniversityNewScreen.mFromSignup = getString(R.string.sign_up);
                                    nextScreen(SelectUniversityNewScreen.class, true);
                                } else {
                                    if (UpdateProfileScreen.mHeader.equalsIgnoreCase(getString(R.string.sign_up))) {

                                        nextScreen(SignUpScreen.class, true);
                                    } else {
                                        nextScreen(UpdateProfileScreen.class, true);
                                    }
                                }
                            }

                        }


                    });

                } else if (mOTPCall.equalsIgnoreCase(getString(R.string.one))) {
                    DialogManager.showBasicAlertDialog(PhOTPEmailScreen.this, getString(R.string.otp_sent), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                            mOTP = phOtpres.getResult().getOtp();
//                            System.out.println("mOtp--" + mOTP);

                            GlobalMethods.storeValuetoPreference(PhOTPEmailScreen.this,
                                    GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM,
                                    mEdit);
                            nxtScreen(2);
                        }
                    });


                }


            } else {
                DialogManager.showBasicAlertDialog(this, phOtpres.getMessage(), this);

            }
        }
    }
}

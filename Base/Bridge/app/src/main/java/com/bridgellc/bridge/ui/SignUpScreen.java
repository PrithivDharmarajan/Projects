package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.util.Locale;

public class SignUpScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {


    private Button mSignUpBtn;
    private EditText mFirstNameEdt, mLastNameEdt, mEmailEdt, mPwdEdt, mConfmPwdEdt;
    private String mFirstName, mLastName, mEmail, mPwd, mConfmPwd;
    private ImageView mHeaderLeftImage;
    public static String mHeader, mSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_signup_screen);

        initComponents();
    }

    private void initComponents() {


        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mSignUpBtn = (Button) findViewById(R.id.footer_btn);
        mFooterLay = (LinearLayout) findViewById(R.id.footer_parent_one_lay);

        mFirstNameEdt = (EditText) findViewById(R.id.first_name_edt);
        mLastNameEdt = (EditText) findViewById(R.id.last_name_edt);
        mEmailEdt = (EditText) findViewById(R.id.email_edt);
        mPwdEdt = (EditText) findViewById(R.id.pwd_edt);
        mConfmPwdEdt = (EditText) findViewById(R.id.confm_pwd_edt);

        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mHeaderLeftImage.getLayoutParams());
        lp.setMargins(15, 15, 15, 15);

        mHeaderLeftImage.setLayoutParams(lp);

        mHeaderLeftImage.setImageResource(R.drawable.close_img);


        mHeader = getString(R.string.sign_up);
        mSignUp = getString(R.string.sign_up);
        AppConstants.SIGNUP_DIRECT = getString(R.string.sign_up);

        if (!AppConstants.SIGNUP_PWD.equalsIgnoreCase("")) {
            mFirstNameEdt.setText(AppConstants.SIGNUP_FIRSTNAME);
            mLastNameEdt.setText(AppConstants.SIGNUP_LASTNAME);
            mEmailEdt.setText(AppConstants.SIGNUP_EMAIL);
            mPwdEdt.setText(AppConstants.SIGNUP_PWD);
            mConfmPwdEdt.setText(AppConstants.SIGNUP_PWD);
        } else {

            AppConstants.SELECT_SCH_UNI = AppConstants.FAILURE_CODE;
            AppConstants.SELECT_SCH_UNI_ID = AppConstants.FAILURE_CODE;
        }

        mHeaderTxt.setText(mHeader.toUpperCase(Locale.ENGLISH));
        mSignUpBtn.setText(mSignUp);
        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.term_cond_lay:
                nextScreen(TermsCondScreen.class, false);
                break;
            case R.id.email_verify_btn:
                AppConstants.SIGNUPVERIFYBACK = getString(R.string.sign_up);
                PhOTPEmailScreen.mScreenShows = 3;
                nextScreen(PhOTPEmailScreen.class, false);
                break;
            case R.id.footer_btn:
                validateFields();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(SignInScreen.class, true);
    }

    private void validateFields() {

        mFirstName = mFirstNameEdt.getText().toString().trim();
        mLastName = mLastNameEdt.getText().toString().trim();
        mEmail = mEmailEdt.getText().toString().trim();
        mPwd = mPwdEdt.getText().toString().trim();
        mConfmPwd = mConfmPwdEdt.getText().toString().trim();

        if (mFirstName.isEmpty()) {
            mFirstNameEdt.requestFocus();
            DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string
                    .enter_first_name), this);
        } else if (mLastName.isEmpty()) {

            mLastNameEdt.requestFocus();
            DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string
                    .enter_last_name), this);

        } else if (mEmail.isEmpty() || (!GlobalMethods.isEmailValid(mEmail, false))) {

            mEmailEdt.requestFocus();
            DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string
                    .signup_enter_email), this);

        } else if (!pwd()) {


        } else {

            AppConstants.SIGNUP_FIRSTNAME = mFirstName;
            AppConstants.SIGNUP_LASTNAME = mLastName;
            AppConstants.SIGNUP_EMAIL = mEmail;
            AppConstants.SIGNUP_PWD = mPwd;

            SelectUniversityNewScreen.mFromSignup = mHeader;
            SelectUniversityScreen.mFromSignup = mHeader;

            GlobalMethods.storeValuetoPreference(SignUpScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, false);
            nextScreen(SelectUniversityNewScreen.class, true);
        }
    }


    private boolean validateMatchPass() {

//        if (!mConfmPwd.equals(mPwd)) {
//            return false;
//        } else {
//            return true;
//        }
        return mConfmPwd.equals(mPwd);
    }

    private boolean pwd() {
        boolean pwd = true;
        if (GlobalMethods.getStringValue(this, AppConstants.LOGINTYPE).equalsIgnoreCase(getString(R.string.one))) {
            if (mPwd.isEmpty()) {

                mPwdEdt.requestFocus();
                DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string
                        .signup_enter_pwd), this);
                pwd = false;

//            } else if (!GlobalMethods.isPasswordValid(this, mPwd).equalsIgnoreCase
//                    (getString
//                            (R.string.password_valid))) {
//                mPwdEdt.requestFocus();
//                DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string.app_name),
//                        GlobalMethods
//                                .isPasswordValid(this, mPwd), this);
//                pwd = false;
            } else if (mConfmPwd.isEmpty()) {

                mConfmPwdEdt.requestFocus();
                DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string
                        .enter_confm_pwd), this);
                pwd = false;
            } else if (!validateMatchPass()) {

                DialogManager.showBasicAlertDialog(SignUpScreen.this, getString(R.string
                        .enter_diff_pwd_confm), this);
                pwd = false;
            }
        }
        return pwd;
    }


    @Override
    public void onOkClick() {

    }


}

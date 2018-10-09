package com.fautus.fautusapp.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.EmailUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.NetworkUtil;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * This class implements UI and Functions for consumer and photographer login to our application
 */

public class LoginScreen extends BaseActivity implements InterfaceBtnCallback {

    /*Variable initialization using bind view*/

    @BindView(R.id.header_left_btn_lay)
    RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.parent_lay)
    ViewGroup mLogInViewGroup;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login_screen);

        initView();
    }

    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mLogInViewGroup);

        mHeaderLeftBtnLay.setVisibility(View.INVISIBLE);

        //Keypad Button action 
        mPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

    }


    /*View OnClick*/
    @OnClick({R.id.login_lay, R.id.sign_up_acc_txt, R.id.forgot_pwd_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_lay:
                validateFields();
                break;
            case R.id.sign_up_acc_txt:
                previousScreen(SignUpScreen.class, true);
                break;
            case R.id.forgot_pwd_txt:
                nextScreen(ForgotPwdScreen.class, true);
                break;
        }

    }

    /*Validate the edit text fields and then do API call */
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard(this);

        String emailStr = mEmailEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();

        if (emailStr.isEmpty() || !EmailUtil.isEmailValid(emailStr)) {
            mEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.email_req), getString(R.string.login_mail_req), LoginScreen.this);
        } else if (pwdStr.isEmpty()) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_req), getString(R.string.login_pwd_req), LoginScreen.this);
        } else {
            /*Check internet connection*/
            if (NetworkUtil.isNetworkAvailable(this)) {
                /*Login API call*/
                APIRequestHandler.getInstance().parseLoginUser(emailStr, pwdStr, this);
            } else {
                /*Alert message will be appeared if there is no internet connection*/
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.app_name), getString(R.string.no_internet), this);

            }
        }
    }

    /*Login parse API callback will be resulted*/
    @Override
    public void onParseSuccess(ParseUser user) {
        super.onParseSuccess(user);
        ParseUser currentUser = ParseUser.getCurrentUser();
        boolean isPhotographerBool = currentUser.getBoolean(ParseAPIConstants.isPhotographer);


        ParseInstallation.getCurrentInstallation();

        /*Crashlytics*/
        Crashlytics.setUserEmail(mEmailEdt.getText().toString().trim());
        Crashlytics.setUserName(user.getString(ParseAPIConstants.username));

        getUserStatus(isPhotographerBool);

    }

    /*Set user details*/
    private void getUserStatus(boolean userIsPhotographerBool) {
        /*Set flag for our local purpose*/
        PreferenceUtil.storeBoolPreferenceValue(LoginScreen.this, AppConstants.USER_IS_CONSUMER, !userIsPhotographerBool);
        PreferenceUtil.storeBoolPreferenceValue(LoginScreen.this, AppConstants.LOGIN_STATUS, true);
        PreferenceUtil.storeBoolPreferenceValue(LoginScreen.this, AppConstants.SETTINGS_STRIP_ON, true);



        /*Direct to next screen*/
        nextScreen(HomeScreen.class, true);
    }

    /*Redirect to Previous Screen*/
    @Override
    public void onBackPressed() {
        previousScreen(SignUpScreen.class, true);
    }

    /*Interface default ok button click*/
    @Override
    public void onOkClick() {

    }
}

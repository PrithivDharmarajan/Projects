package com.fautus.fautusapp.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.services.APIRequestHandler;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.DialogManager;
import com.fautus.fautusapp.utils.EmailUtil;
import com.fautus.fautusapp.utils.InterfaceBtnCallback;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * This class implements  UI and functions for consumer and photographer signUp
 */

public class SignUpScreen extends BaseActivity implements InterfaceBtnCallback {

    /*Variable initialization using bind view*/
    @BindView(R.id.parent_lay)
    ViewGroup SignUpViewGroup;

    @BindView(R.id.header_left_btn_lay)
    RelativeLayout mHeaderLeftBtnLay;

    @BindView(R.id.sign_up_hint_txt)
    TextView mSignUpHintTxt;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.am_photographer_txt)
    TextView mAmPhotographerTxt;

    @BindView(R.id.cancel_txt)
    TextView mCancelTxt;

    private boolean isPhotographerBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_signup_screen);
        initView();
    }

    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a Click/touch made outside the edit text*/
        setupUI(SignUpViewGroup);

        /* If the value of AppConstants.WELCOME_SCREEN_TYPE is zero, Then the screen can react in a Consumer signUp . or else, it reacts in a photographer signUp*/
        isPhotographerBool = AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE);

        mSignUpHintTxt.setText(isPhotographerBool ? getString(R.string.sign_up_photo) : getString(R.string.sign_up_free));
        mAmPhotographerTxt.setVisibility(isPhotographerBool ? View.GONE : View.VISIBLE);
        mCancelTxt.setVisibility(isPhotographerBool ? View.VISIBLE : View.GONE);

        /*to Set the left side button of the Header visible*/
        mHeaderLeftBtnLay.setVisibility(View.INVISIBLE);


        //Keypad button action
        mPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        AppConstants.PHOTOGRAPHER_FROM_MENU = AppConstants.FAILURE_CODE;
        AppConstants.PROFILE_FROM_MENU = AppConstants.FAILURE_CODE;
    }


    /*View OnClick*/
    @OnClick({R.id.header_left_btn_lay, R.id.sign_up_lay, R.id.already_acc_txt, R.id.am_photographer_txt, R.id.cancel_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:

                onBackPressed();
                break;
            case R.id.sign_up_lay:

                validateFields();
                break;
            case R.id.already_acc_txt:

                nextScreen(LoginScreen.class, true);
                break;
            case R.id.am_photographer_txt:
                /*Set flag - Act as photographer*/
                AppConstants.WELCOME_SCREEN_TYPE = AppConstants.SUCCESS_CODE;
                nextScreen(TutorialScreen.class, true);
                break;
            case R.id.cancel_txt:
                /*Set flag - Act as consumer*/
                AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
                previousScreen(SignUpScreen.class, true);
                break;
        }

    }

    /*Validate the edit text fields and API call*/
    private void validateFields() {
        /*hiding keypad for user interaction*/
        hideSoftKeyboard(this);

        String emailStr = mEmailEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();

        if (emailStr.isEmpty() || !EmailUtil.isEmailValid(emailStr)) {
            mEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.email_req), getString(R.string.sign_up_email_req), SignUpScreen.this);
        } else if (pwdStr.isEmpty()) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_req), getString(R.string.sign_up_pwd_req), SignUpScreen.this);
        } else {
            //SignUp API Call
            APIRequestHandler.getInstance().callEmailValidAPI(emailStr, pwdStr, isPhotographerBool, this);
        }
    }


    /*SignUp parse API callback result*/
    @Override
    public void onParseRequestSuccess() {
        super.onParseRequestSuccess();

        /*Set user id to parse DB*/
        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
        parseInstallation.put(AppConstants.USER_ID, ParseUser.getCurrentUser().getObjectId());
        parseInstallation.saveInBackground();

        /*Set flag for local purpose*/
        PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, true);
        PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.SETTINGS_STRIP_ON, true);
        PreferenceUtil.storeBoolPreferenceValue(SignUpScreen.this, AppConstants.USER_IS_CONSUMER, AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.FAILURE_CODE));

        /*Direct to next screen*/
        nextScreen(AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE) ? PhotographerProfileScreen.class : HomeScreen.class, true);

    }


    @Override
    public void onBackPressed() {
        if (AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE)) {
            /*Set flag for Consumer */
            AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
            previousScreen(SignUpScreen.class, true);
        }
        if (!PreferenceUtil.getBoolPreferenceValue(this, AppConstants.CONSUMER_WELCOME_SCREEN_SEEN)) {
            previousScreen(TutorialScreen.class, true);
        } else {
            /*Close the current screen*/
            finish();
        }
    }

    /*Interface default ok click*/
    @Override
    public void onOkClick() {

    }
}

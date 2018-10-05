package com.calix.calixgigamanage.ui.loginregconfig;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.input.model.LoginRegistrationInputModel;
import com.calix.calixgigamanage.input.model.LoginRegistrationMobileInputModel;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.main.CalixApplication;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.LoginResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.ui.dashboard.Dashboard;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.PatternMatcherUtil;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Login extends BaseActivity {

    @BindView(R.id.login_parent_lay)
    ViewGroup mLoginViewGroup;

    @BindView(R.id.login_header_bg_lay)
    RelativeLayout mLoginHeaderBgLay;

    @BindView(R.id.login_header_msg_lay)
    RelativeLayout mLoginHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_left_img_lay)
    RelativeLayout mHeaderLeftImgLay;

    @BindView(R.id.email_id_edt)
    EditText mEmailAddressEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    private String mForgotEmailStr = "";
    private LoginRegistrationInputModel mLoginInputModelEntity = new LoginRegistrationInputModel();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mLoginViewGroup);

        /*Keypad button action*/
        mPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        setHeaderView();
    }

    private void setHeaderView() {

        /*set header changes*/
        mLoginHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);
        mHeaderLeftImgLay.setVisibility(View.INVISIBLE);
        mHeaderTxt.setText(getString(R.string.login));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mLoginHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mLoginHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Login.this)));
                    mLoginHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Login.this), 0, 0);
                    mLoginHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
                }

            });
        }

        mEmailAddressEdt.setText(PreferenceUtil.getStringValue(this, AppConstants.USER_EMAIL));

    }

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();
    }

    /*View onClick*/
    @OnClick({R.id.forgot_pwd_txt, R.id.new_user_txt, R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_pwd_txt:
                DialogManager.getInstance().showForgotPwdPopup(this, "", new InterfaceEdtBtnCallback() {
                    @Override
                    public void onPositiveClick(String emailStr) {
                        forgotPwdAPICall(emailStr);
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });
                break;
            case R.id.new_user_txt:
                nextScreen(Registration.class);
                break;
            case R.id.submit_btn:
                validateFields();
                break;
        }
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String emailAddressStr = mEmailAddressEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();

        if (emailAddressStr.isEmpty()) {
            mEmailAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_email_id), this);
        } else if (!PatternMatcherUtil.isEmailValid(emailAddressStr)) {
            mEmailAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_valid_email_id), this);
        } else if (pwdStr.isEmpty()) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_pwd), this);
        } else {
            LoginRegistrationInputModel loginInputModel = new LoginRegistrationInputModel();
            LoginRegistrationMobileInputModel loginMobileInputModel = new LoginRegistrationMobileInputModel();

            loginInputModel.setEmail(emailAddressStr);
            loginInputModel.setPassword(pwdStr);

            loginMobileInputModel.setNotificationToken("12345");
            loginMobileInputModel.setMsisdn(getPhone());
            loginMobileInputModel.setOs(AppConstants.ANDROID);
            loginMobileInputModel.setLocale(getResources().getConfiguration().locale.getCountry());
            loginInputModel.setMobileDevice(loginMobileInputModel);
            loginAPICall(loginInputModel);
        }
    }

    /*API calls*/
    private void loginAPICall(LoginRegistrationInputModel loginInputModel) {
        mLoginInputModelEntity = loginInputModel;
        AppConstants.BASE_URL = PreferenceUtil.getBaseURL(Login.this);
        sysOut("  AppConstants.BASE_URL"+AppConstants.BASE_URL);
        APIRequestHandler.getInstance().loginAPICall(mLoginInputModelEntity, this);
    }

    private void forgotPwdAPICall(String emailStr) {
        mForgotEmailStr = emailStr;
        APIRequestHandler.getInstance().resetAPICall(mForgotEmailStr, Login.this);
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) resObj;
            PreferenceUtil.storeStringValue(this, AppConstants.AUTHORIZATION, loginResponse.getToken());
            PreferenceUtil.storeStringValue(this, AppConstants.USER_ID, loginResponse.getUserId());
            PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, true);
            PreferenceUtil.storeStringValue(this, AppConstants.USER_EMAIL, mEmailAddressEdt.getText().toString().trim());
            CalixApplication.getInstance().pushMsg();
            nextScreen(Dashboard.class);
        } else if (resObj instanceof CommonResponse) {
            DialogManager.getInstance().showAlertPopup(this, "Success", this);
        }
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {

            if (resObj instanceof LoginResponse) {
                DialogManager.getInstance().showAlertPopup(this,
                        (t instanceof java.net.ConnectException ||t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                                .connect_time_out)), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
            } else if (resObj instanceof CommonResponse) {
                DialogManager.getInstance().showNetworkErrorPopup(this,
                        (t instanceof java.net.ConnectException ||t instanceof java.net.UnknownHostException? getString(R.string.no_internet) : getString(R.string
                                .connect_time_out)), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                                DialogManager.getInstance().showForgotPwdPopup(Login.this, mForgotEmailStr, new InterfaceEdtBtnCallback() {
                                    @Override
                                    public void onPositiveClick(String emailStr) {
                                        forgotPwdAPICall(emailStr);
                                    }

                                    @Override
                                    public void onNegativeClick() {

                                    }
                                });
                            }

                        });
            }

        }
    }
}


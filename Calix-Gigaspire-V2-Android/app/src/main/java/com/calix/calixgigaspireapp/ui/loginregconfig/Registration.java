package com.calix.calixgigaspireapp.ui.loginregconfig;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.input.model.LoginRegistrationInputModel;
import com.calix.calixgigaspireapp.input.model.LoginRegistrationMobileInputModel;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.LoginResponse;
import com.calix.calixgigaspireapp.output.model.RegistrationResponse;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.PatternMatcherUtil;
import com.calix.calixgigaspireapp.utils.PreferenceUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Registration extends BaseActivity {

    @BindView(R.id.reg_parent_lay)
    ViewGroup mRegViewGroup;

    @BindView(R.id.reg_header_bg_lay)
    RelativeLayout mRegHeaderBgLay;

    @BindView(R.id.reg_header_img)
    ImageView mRegHeaderImg;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.first_name_edt)
    EditText mFirstNameEdt;

    @BindView(R.id.last_name_edt)
    EditText mLastNameEdt;

    @BindView(R.id.email_id_edt)
    EditText mEmailAddressEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.pwd_in_visible_img)
    ImageView mPwdInVisibleImg;

    @BindView(R.id.confirm_pwd_edt)
    EditText mConfirmPwdEdt;

    @BindView(R.id.confirm_pwd_in_visible_img)
    ImageView mConfirmPwdInVisibleImg;

    @BindView(R.id.mob_edt)
    EditText mMobEdt;

    @BindView(R.id.terms_conditions_checkbox)
    CheckBox mContactCheckBox;

    private boolean isRegAPICallBool = false;
    private LoginRegistrationInputModel mRegInputModelEntity = new LoginRegistrationInputModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_registartion);
        initView();
    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mRegViewGroup);

        /*Keypad button action*/
        mMobEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                }
                return true;
            }
        });

        setHeaderView();
        mPwdInVisibleImg.setTag(1);
        mConfirmPwdInVisibleImg.setTag(1);
    }

    private void setHeaderView() {

        /*set header changes*/
        mRegHeaderImg.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.registration));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRegHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mRegHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Registration.this)));
                    mRegHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Registration.this), 0, 0);
                    mRegHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
                }

            });
        }

    }

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.pwd_in_visible_img, R.id.confirm_pwd_in_visible_img, R.id.terms_conditions_txt , R.id.conditions_txt ,R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.pwd_in_visible_img:
                int pwdSelectedInt=mPwdEdt.getText().toString().length();
                mPwdEdt.setTransformationMethod(mPwdInVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mPwdEdt.setSelection(pwdSelectedInt);
                mPwdInVisibleImg.setImageResource(mPwdInVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mPwdInVisibleImg.setTag(mPwdInVisibleImg.getTag().equals(1)?0:1);
                 break;
            case R.id.confirm_pwd_in_visible_img:
                int confirmPwdSelectedInt=mConfirmPwdEdt.getText().toString().length();
                mConfirmPwdEdt.setTransformationMethod(mConfirmPwdInVisibleImg.getTag().equals(1)?null:new PasswordTransformationMethod());
                mConfirmPwdEdt.setSelection(confirmPwdSelectedInt);
                mConfirmPwdInVisibleImg.setImageResource(mConfirmPwdInVisibleImg.getTag().equals(1)?R.drawable.visible:R.drawable.in_visible);
                mConfirmPwdInVisibleImg.setTag(mConfirmPwdInVisibleImg.getTag().equals(1)?0:1);
                break;
            case R.id.terms_conditions_txt:
                DialogManager.getInstance().showTermsAndConditionpopup(this, getString(R.string.terms_msg), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {
                        mContactCheckBox.setChecked(false);
                    }

                    @Override
                    public void onPositiveClick() {
                        mContactCheckBox.setChecked(true);
                    }
                });
                break;
            case R.id.conditions_txt:
                DialogManager.getInstance().showTermsAndConditionpopup(this, getString(R.string.terms_msg), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {
                        mContactCheckBox.setChecked(false);
                    }

                    @Override
                    public void onPositiveClick() {
                        mContactCheckBox.setChecked(true);
                    }
                });
                break;
            case R.id.submit_btn:
                validateFields();
                break;
        }
    }

    /*validate fields*/
    private void validateFields() {
        hideSoftKeyboard(this);
        String firstNameStr = mFirstNameEdt.getText().toString().trim();
        String lastNameStr = mLastNameEdt.getText().toString().trim();
        String emailAddressStr = mEmailAddressEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();
        String confirmPwdStr = mConfirmPwdEdt.getText().toString().trim();
        String mobStr = mMobEdt.getText().toString().trim();


        if (firstNameStr.isEmpty()) {
            mFirstNameEdt.requestFocus();
            mFirstNameEdt.setSelection(firstNameStr.length());
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_first_name), this);
        } else if (lastNameStr.isEmpty()) {
            mLastNameEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_last_name), this);
        } else if (emailAddressStr.isEmpty()) {
            mEmailAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_email_id), this);
        } else if (!PatternMatcherUtil.isEmailValid(emailAddressStr)) {
            mEmailAddressEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_valid_email_id), this);
        } else if (pwdStr.isEmpty()) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.enter_pwd), this);
        } else if (pwdStr.length() < 8) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pass_contains_eight_char), this);
        } else if (!pwdStr.equals(confirmPwdStr)) {
            mConfirmPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.pwd_does_not_match), this);
        } else if (!mContactCheckBox.isChecked()) {
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_tick_tc), this);
        }
        else {
            LoginRegistrationInputModel registrationInputModel = new LoginRegistrationInputModel();
            LoginRegistrationMobileInputModel registrationMobileInputModel = new LoginRegistrationMobileInputModel();

            registrationInputModel.setFirstName(firstNameStr);
            registrationInputModel.setLastName(lastNameStr);
            registrationInputModel.setEmail(emailAddressStr);
            registrationInputModel.setPassword(pwdStr);

            registrationMobileInputModel.setNotificationToken("12345");
            registrationMobileInputModel.setMsisdn(mobStr);
            registrationMobileInputModel.setOs(AppConstants.ANDROID);
            registrationMobileInputModel.setLocale(getResources().getConfiguration().locale.getCountry());
            registrationInputModel.setMobileDevice(registrationMobileInputModel);

            registrationAPICall(registrationInputModel);
        }
    }

    /*API calls*/
    private void registrationAPICall(LoginRegistrationInputModel loginInputModel) {
        mRegInputModelEntity = loginInputModel;
        isRegAPICallBool = true;
        APIRequestHandler.getInstance().registrationAPICall(mRegInputModelEntity, this);
    }

    private void loginAPICall(LoginRegistrationInputModel loginInputModel) {
        isRegAPICallBool = false;
        mRegInputModelEntity = loginInputModel;
        APIRequestHandler.getInstance().loginAPICall(mRegInputModelEntity, this);
    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof RegistrationResponse) {
            loginAPICall(mRegInputModelEntity);
        } else if (resObj instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) resObj;
            PreferenceUtil.storeStringValue(this, AppConstants.AUTHORIZATION, loginResponse.getToken());
            PreferenceUtil.storeStringValue(this, AppConstants.USER_ID, loginResponse.getUserId());
            PreferenceUtil.storeStringValue(this, AppConstants.USER_EMAIL, mEmailAddressEdt.getText().toString().trim());
            AppConstants.ROUTER_ON_BOARD_FROM_SETTINGS = false;

            AppConstants.ROUTER_DETAILS_ENTITY = new RouterMapEntity();
            AppConstants.ROUTER_DETAILS_ENTITY.setName(String.format(getString(R.string.router_default_name), mFirstNameEdt.getText().toString().trim()));
            nextScreen(RouterConfiguration.class);
        }
    }

    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            if (isRegAPICallBool) {
                DialogManager.getInstance().showAlertPopup(this,
                        (t instanceof java.net.ConnectException ||t instanceof java.net.UnknownHostException? getString(R.string.no_internet) : getString(R.string
                                .connect_time_out)), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {

                            }
                        });
            } else {
                DialogManager.getInstance().showNetworkErrorPopup(this,
                        (t instanceof java.net.ConnectException||t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                                .connect_time_out)), new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                                loginAPICall(mRegInputModelEntity);
                            }
                        });
            }

        }
    }

    /*onBackPressed*/
    @Override
    public void onBackPressed() {
        backScreen();
    }
}
package com.smaat.renterblock.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.UserDetailsEntity;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.model.LoginResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.InterfaceBtnCallback;
import com.smaat.renterblock.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.smaat.renterblock.utils.EmailUtil.isEmailValid;

public class RegisterScreen extends BaseActivity {

    @BindView(R.id.register_parent_lay)
    RelativeLayout mRegisterParentLay;

    @BindView(R.id.business_name_edt)
    EditText mBusinessNameEdt;

    @BindView(R.id.first_name_edt)
    EditText mFirstNameEdt;

    @BindView(R.id.last_name_edt)
    EditText mLastNameEdt;

    @BindView(R.id.user_name_edt)
    EditText mUserNameEdt;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.password_edt)
    EditText mPasswordEdt;

    @BindView(R.id.zip_edt)
    EditText mZipEdt;

    @BindView(R.id.phone_num_edt)
    EditText mPhoneNumberEdt;

    @BindView(R.id.address_layout)
    LinearLayout mAddressLay;

    @BindView(R.id.address_one_edt)
    EditText mAddressOneEdt;

    @BindView(R.id.address_two_edt)
    EditText mAddressTwoEdt;

    @BindView(R.id.city_state_zip_edt)
    EditText mCityStateZipEdt;

    @BindView(R.id.buyer_lay)
    RelativeLayout mBuyerLay;

    @BindView(R.id.buyer_cancel_btn)
    Button mBuyerCancelBtn;

    @BindView(R.id.Buyer_register_btn)
    Button mBuyerRegisterBtn;

    @BindView(R.id.agree_terms_buy_txt)
    TextView mAgreeTermsBuyerTxt;

    @BindView(R.id.broker_bottom_layout)
    RelativeLayout mBrokerBottomLay;

    @BindView(R.id.profile_option)
    LinearLayout mProfileOptionLay;

    @BindView(R.id.enhanced_lay)
    LinearLayout mEnhancedLay;

    @BindView(R.id.enhanced_img)
    ImageView mEnhancedImg;

    @BindView(R.id.standard_lay)
    LinearLayout mStandardLay;

    @BindView(R.id.standard_img)
    ImageView mStandardImg;

    @BindView(R.id.home_option_lay)
    LinearLayout mHomeOptionLay;

    @BindView(R.id.home_lay)
    LinearLayout mHomeLay;

    @BindView(R.id.home_img)
    ImageView mHomeImg;

    @BindView(R.id.broker_lay)
    LinearLayout mBrokerLay;

    @BindView(R.id.broker_img)
    ImageView mBrokerImg;

    @BindView(R.id.account_option_lay)
    LinearLayout mAccountOptionLay;

    @BindView(R.id.broker_cancel_btn)
    Button mBrokerCancelBtn;

    @BindView(R.id.broker_register_btn)
    Button mBrokerRegisterBtn;

    @BindView(R.id.broker_agree_terms)
    TextView mBrokerAgreeTermsTxt;

    private String mSelectedTypeStr = "";
    String mBusinessNameStr, mFirstNameStr, mLastNameStr, mUserNameStr, mEmailStr, mPasswordStr, mZipStr, mPhoneNumberStr, mAddressOneStr, mAddressTwoStr, mCityStateStr;
    private String mHomeOptionStr = "1", mBrokerOptionStr = "0", mEnhancedOptionStr = "1", mStandardOptionStr = "0";
    private UserDetailsEntity mUserDetailsRes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

         /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mRegisterParentLay);

        mUserDetailsRes = PreferenceUtil.getUserDetailsRes(this);

        mSelectedTypeStr = mUserDetailsRes.getType();

        initView();
        spannableText();

    }

    private void initView() {
        if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.BUYER)) {
            mBusinessNameEdt.setVisibility(View.GONE);
            mBuyerLay.setVisibility(View.VISIBLE);
            mAddressLay.setVisibility(View.GONE);
            mPhoneNumberEdt.setHint(R.string.phone_optional);
        } else if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.RENTER)) {
            mBusinessNameEdt.setVisibility(View.GONE);
            mBuyerLay.setVisibility(View.VISIBLE);
            mAddressLay.setVisibility(View.GONE);
            mPhoneNumberEdt.setHint(R.string.phone_optional);
        } else if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.SELLER)) {
            mBrokerBottomLay.setVisibility(View.VISIBLE);
            mHomeOptionLay.setVisibility(View.VISIBLE);
            mAccountOptionLay.setVisibility(View.GONE);
            mZipEdt.setVisibility(View.GONE);
            mBusinessNameEdt.setHint(R.string.business_name_optional);
        } else if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.AGENT) || mSelectedTypeStr.equalsIgnoreCase(AppConstants.BROKER)) {
            mBrokerBottomLay.setVisibility(View.VISIBLE);
            mAccountOptionLay.setVisibility(View.GONE);
            mHomeOptionLay.setVisibility(View.GONE);
            mZipEdt.setVisibility(View.GONE);
        }

        if (mUserDetailsRes.getLogin_type().equals(AppConstants.FACEBOOK) || mUserDetailsRes.getLogin_type().equals(AppConstants.GOOGLE)) {
            mFirstNameEdt.setText(mUserDetailsRes.getFirst_name());
            mLastNameEdt.setText(mUserDetailsRes.getLast_name());
            mEmailEdt.setText(mUserDetailsRes.getEmail_id());
        }
        mPasswordEdt.setVisibility(mUserDetailsRes.getLogin_type().equals(AppConstants.EMAIL)?View.VISIBLE:View.GONE);
        mEmailEdt.setFocusable(mUserDetailsRes.getLogin_type().equals(AppConstants.EMAIL));
        mEmailEdt.setFocusableInTouchMode(mUserDetailsRes.getLogin_type().equals(AppConstants.EMAIL));
    }

    private void spannableText() {
        SpannableString SpanString = new SpannableString(
                "By tapping Register you agree to our \nTerms of Use and Privacy Policy");

        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                AppConstants.CONTENT_HEADER = getString(R.string.terms_of_service);
                AppConstants.CONTENT_URL = AppConstants.TC;
                nextScreen(ContentURLScreen.class, false);

            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                AppConstants.CONTENT_HEADER = getString(R.string.privacy_policy);
                AppConstants.CONTENT_URL = AppConstants.PP;
                nextScreen(ContentURLScreen.class, false);

            }
        };

        SpanString.setSpan(termsAndCondition, 37, 50, 0);
        SpanString.setSpan(privacy, 55, 69, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 37, 50, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 55, 69, 0);
        SpanString.setSpan(new UnderlineSpan(), 37, 50, 0);
        SpanString.setSpan(new UnderlineSpan(), 55, 69, 0);

        mAgreeTermsBuyerTxt.setMovementMethod(LinkMovementMethod.getInstance());
        mAgreeTermsBuyerTxt.setText(SpanString, TextView.BufferType.SPANNABLE);
        mAgreeTermsBuyerTxt.setSelected(true);

        mBrokerAgreeTermsTxt.setMovementMethod(LinkMovementMethod.getInstance());
        mBrokerAgreeTermsTxt.setText(SpanString, TextView.BufferType.SPANNABLE);
        mBrokerAgreeTermsTxt.setSelected(true);
    }

    @OnClick({R.id.buyer_cancel_btn, R.id.Buyer_register_btn, R.id.broker_cancel_btn, R.id.broker_register_btn,
            R.id.enhanced_lay, R.id.standard_lay, R.id.home_lay, R.id.broker_lay, R.id.account_option_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyer_cancel_btn:
            case R.id.broker_cancel_btn:
                onBackPressed();
                break;
            case R.id.Buyer_register_btn:
            case R.id.broker_register_btn:
                Validation();
                break;
            case R.id.enhanced_lay:
            case R.id.standard_lay:
                mEnhancedOptionStr = v.getId() == R.id.enhanced_lay ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                mStandardOptionStr = v.getId() == R.id.enhanced_lay ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE;
                mEnhancedImg.setImageResource(v.getId() == R.id.enhanced_lay ? R.drawable.check_enable : R.drawable.check_disable);
                mStandardImg.setImageResource(v.getId() == R.id.enhanced_lay ? R.drawable.check_disable : R.drawable.check_enable);
                break;
            case R.id.home_lay:
            case R.id.broker_lay:
                mHomeOptionStr = v.getId() == R.id.home_lay ? AppConstants.SUCCESS_CODE : AppConstants.FAILURE_CODE;
                mBrokerOptionStr = v.getId() == R.id.home_lay ? AppConstants.FAILURE_CODE : AppConstants.SUCCESS_CODE;
                mHomeImg.setImageResource(v.getId() == R.id.home_lay ? R.drawable.check_enable : R.drawable.check_disable);
                mBrokerImg.setImageResource(v.getId() == R.id.home_lay ? R.drawable.check_disable : R.drawable.check_enable);
                break;
            case R.id.account_option_lay:
                break;

        }
    }

    private void Validation() {

        mBusinessNameStr = mBusinessNameEdt.getText().toString().trim();
        mFirstNameStr = mFirstNameEdt.getText().toString().trim();
        mLastNameStr = mLastNameEdt.getText().toString().trim();
        mUserNameStr = mUserNameEdt.getText().toString().trim();
        mEmailStr = mEmailEdt.getText().toString().trim();
        mPasswordStr = mPasswordEdt.getText().toString().trim();
        mZipStr = mZipEdt.getText().toString().trim();
        mPhoneNumberStr = mPhoneNumberEdt.getText().toString().trim();
        mAddressOneStr = mAddressOneEdt.getText().toString().trim();
        mAddressTwoStr = mAddressTwoEdt.getText().toString().trim();
        mCityStateStr = mCityStateZipEdt.getText().toString().trim();

        if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.BUYER) || mSelectedTypeStr.equalsIgnoreCase(AppConstants.RENTER)) {
            if (mFirstNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_first_name), this);
            } else if (mLastNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_last_name), this);
            } else if (mUserNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_user_name), this);
            } else if (mEmailStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_email), this);
            } else if (!isEmailValid(mEmailStr)) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_valid_email), this);
            } else if (mPasswordStr.isEmpty()&&mUserDetailsRes.getLogin_type().equals(AppConstants.EMAIL)) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_password), this);
            } else if (mZipStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_zipcode), this);
            } else {
                CallRegisterAPI();
            }
        } else if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.SELLER)) {
            if (mFirstNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_first_name), this);
            } else if (mLastNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_last_name), this);
            } else if (mUserNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_user_name), this);
            } else if (mEmailStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_email), this);
            } else if (!isEmailValid(mEmailStr)) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_valid_email), this);
            } else if (mPasswordStr.isEmpty()&&mUserDetailsRes.getLogin_type().equals(AppConstants.EMAIL)) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_password), this);
            } else if (mPhoneNumberStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_phone_number), this);
            } else if (mAddressOneStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_address_line1), this);
            } else if (mAddressTwoStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_address_line2), this);
            } else if (mCityStateStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_city_state), this);
            } else {
                CallRegisterAPI();
            }
        } else if (mSelectedTypeStr.equalsIgnoreCase(AppConstants.AGENT) || mSelectedTypeStr.equalsIgnoreCase(AppConstants.BROKER)) {
            if (mBusinessNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_business_name), this);
            } else if (mFirstNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_first_name), this);
            } else if (mLastNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_last_name), this);
            } else if (mUserNameStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_user_name), this);
            } else if (mEmailStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_email), this);
            } else if (!isEmailValid(mEmailStr)) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_valid_email), this);
            } else if (mPasswordStr.isEmpty()&&mUserDetailsRes.getLogin_type().equals(AppConstants.EMAIL)) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_password), this);
            } else if (mPhoneNumberStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_phone_number), this);
            } else if (mAddressOneStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_address_line1), this);
            } else if (mAddressTwoStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_address_line2), this);
            } else if (mCityStateStr.isEmpty()) {
                DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_city_state), this);
            } else {
                CallRegisterAPI();
            }
        }
    }

    private void CallRegisterAPI() {

        APIRequestHandler.getInstance().registerAPICall(mBusinessNameStr, mFirstNameStr, mLastNameStr, mUserNameStr, mEmailStr, mPasswordStr, mZipStr
                , mPhoneNumberStr, mAddressOneStr, mAddressTwoStr, mCityStateStr, "", "", mHomeOptionStr, mBrokerOptionStr, mEnhancedOptionStr, mStandardOptionStr, mUserDetailsRes.getLogin_type(),
                mUserDetailsRes.getGoogle_id(), mUserDetailsRes.getFacebook_id(), mUserDetailsRes.getType(), "", "", RegisterScreen.this);
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof LoginResponse) {
            LoginResponse signUpEntityRes = (LoginResponse) responseObj;
            if (signUpEntityRes.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (signUpEntityRes.getResult().getLogin_type().equals(AppConstants.EMAIL)) {
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.verification_mail), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            previousScreen(LoginScreen.class, true);
                        }
                    });
                } else {
                    PreferenceUtil.storeUserDetails(this, signUpEntityRes.getResult());
                    PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, true);
                    DialogManager.getInstance().showToast(this, getString(R.string.registered));
                    nextScreen(HomeScreen.class, true);
                }
            } else {
                DialogManager.getInstance().showAlertPopup(this, signUpEntityRes.getMsg(), this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(CreateAccountScreen.class, true);
    }
}

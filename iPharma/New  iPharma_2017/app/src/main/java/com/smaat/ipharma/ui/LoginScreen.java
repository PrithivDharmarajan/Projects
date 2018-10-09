package com.smaat.ipharma.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.apiinterface.AppApiConstants;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.ForgotResponse;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GlobalMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

import static com.smaat.ipharma.utils.GlobalMethods.BOOLEAN_PREFERENCE;

/**
 * Created by admin on 1/5/2017.
 */

public class LoginScreen extends BaseActivity {


    @BindView(R.id.login_btn)
    Button login_button;

    @BindView(R.id.signup_btn)
    Button Signup_btn;

    @BindView(R.id.email_address)
    EditText mEmail_id;
    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.parent_layout)
    LinearLayout m_Mainlayout;

    @BindView(R.id.forgot_password)
    TextView mForgotPassword;

    protected String mRegId;

    private String m_strEmailid = "";
    private String m_strPhoneno = "";
    private String m_strPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.ui_login_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        setupUI(m_Mainlayout);
        logUser();
        if(GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DEVICE_ID).isEmpty())
        {
            GlobalMethods.storeValuetoPreference(this, 1, AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
        }
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("parthasarathy@smaatapps.com");
        Crashlytics.setUserName("Test User");
    }

    @OnClick({R.id.login_btn, R.id.signup_btn,R.id.forgot_password,R.id.backbutton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbutton:
                onBackPressed();
                break;

            case R.id.login_btn:
                if(IsConnecttointernet())
                {
                    ValidRegister();
                }else{
                    DialogManager.showMsgPopup(LoginScreen.this,"",getString(R.string.no_internet));
                }

                break;
            case R.id.signup_btn:
                nextScreen(SignupScreen.class);
                break;
            case R.id.forgot_password:
                showForgotPassDialog();
                break;
            default:
                break;
        }
    }


    private boolean ValidRegister() {
        m_strPassword = mPassword.getText().toString().trim();
        String device_type = getString(R.string.android);
        if(isNumeric(mEmail_id.getText().toString().trim()))
        {
            m_strPhoneno = mEmail_id.getText().toString().trim();
            m_strEmailid = "";
        }else{
            m_strEmailid = mEmail_id.getText().toString().trim();
            m_strPhoneno = "";

        }
        if (mEmail_id.getText().toString().trim().isEmpty()) {
            mEmail_id.requestFocus();
            DialogManager.showMsgPopup(LoginScreen.this,"",getString(R.string.enter_email_phone));
        }  else if (mEmail_id.getText().toString().trim().equalsIgnoreCase("")) {
            DialogManager.showMsgPopup(LoginScreen.this,"",getString(R.string.error_empty_email));
        }else if (!GlobalMethods.isEmailValid(m_strEmailid)&&!isNumeric(mEmail_id.getText().toString().trim())) {
            mEmail_id.requestFocus();
            DialogManager.showMsgPopup(LoginScreen.this,"",getString(R.string.error_valid_email));
        } else if (m_strPassword.isEmpty()) {
            mPassword.requestFocus();
            DialogManager.showMsgPopup(LoginScreen.this,getString(R.string.app_name),getString(R.string.error_empty_password));
        } else {
            APIRequestHandler.getInstance().signinAPICall(m_strEmailid,m_strPhoneno,m_strPassword,device_type,GlobalMethods.getStringValue(getApplicationContext(),AppConstants.DEVICE_ID),LoginScreen.this);

        }

        return true;
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof CommonResponse) {
            CommonResponse signInEntityRes = (CommonResponse) responseObj;

            if (signInEntityRes != null) {

                if (signInEntityRes.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    String communication_address = signInEntityRes.getResult()
                            .getAddress()
                            + ", "
                            + signInEntityRes.getResult().getArea()
                            + ", "
                            + signInEntityRes.getResult().getCity()
                            + ", "
                            + signInEntityRes.getResult().getPincode();
                    GlobalMethods.UpdateUserDetails(LoginScreen.this,
                            signInEntityRes.getResult());
                    GlobalMethods.storeValuetoPreference(
                            LoginScreen.this,
                            GlobalMethods.STRING_PREFERENCE,
                            AppConstants.COMMUNICATION_ADDRESS,
                            communication_address);
                    if(signInEntityRes.getResult().getShop_details().size()>0)
                    {
                        GlobalMethods.UpdateShopDetails(getApplicationContext(),signInEntityRes.getResult().getShop_details().get(0));
                    }
                    GlobalMethods.storeValuetoPreference(LoginScreen.this, GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD, m_strPassword);

                    if (signInEntityRes.getMsg().trim().equalsIgnoreCase(getString(R.string.account_not_confirmed))) {

                        nextScreen(OneTimePassword.class);

                    }else{
                        GlobalMethods.storeValuetoPreference(getApplicationContext(), BOOLEAN_PREFERENCE,AppConstants.IS_LOGGEDIN,true);
                        //launchScreen(HomeScreen.class);
                        finishScreen();

                    }
                }else if (signInEntityRes.getStatus().equalsIgnoreCase(
                        AppConstants.FAILURE_CODE)) {
                    /*Toast.makeText(getApplicationContext(),
                            signInEntityRes.getMsg(),Toast.LENGTH_SHORT).show();*/
                    DialogManager.showMsgPopup(LoginScreen.this,"",signInEntityRes.getMsg());
                    //DialogManager.showCustomAlertDialog(LoginActivity.this, LoginActivity.this, getString(R.string.login_failed), response.getMsg());

                }

            }

        }else if(responseObj instanceof ForgotResponse){

            ForgotResponse response = (ForgotResponse) responseObj;
            if(response.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE)){
                DialogManager.showMsgPopup(LoginScreen.this,
                        getString(R.string.app_name), response.getMsg());
            } else {
                DialogManager.showMsgPopup(LoginScreen.this,
                        getString(R.string.app_name), getString(R.string.email_not_registerd));
            }

        }
        DialogManager.hideProgress();
    }



    public void showForgotPassDialog() {

        final Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_forgotpass);

        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

        mDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideSoftKeyboard(this);
        TextView header_txt = (TextView) mDialog.findViewById(R.id.alert_title);
        ImageView ok = (ImageView) mDialog.findViewById(R.id.submit);
        final EditText Email = (EditText) mDialog.findViewById(R.id.email_edit);
        final ImageView close_btn = (ImageView) mDialog.findViewById(R.id.close_button);

        close_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDialog.dismiss();
            }

        });

        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (Email.getText().toString().equalsIgnoreCase("")) {

                    DialogManager.showMsgPopup(LoginScreen.this,
                            getString(R.string.forgot_pwd_failed),
                            getString(R.string.enter_email_phone));
                }else if (!GlobalMethods.isEmailValid(Email.getText().toString()
                        .trim())) {

                    DialogManager.showMsgPopup(LoginScreen.this,
                            getString(R.string.forgot_pwd_failed),
                            getString(R.string.enter_email));
                } else {
                    mDialog.dismiss();
                    APIRequestHandler.getInstance().Forgotpassword(Email.getText().toString().trim(), LoginScreen.this);

                }
            }

        });

        mDialog.show();
    }



    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }



}

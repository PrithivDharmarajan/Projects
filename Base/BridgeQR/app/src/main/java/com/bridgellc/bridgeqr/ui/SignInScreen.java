package com.bridgellc.bridgeqr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridgeqr.BuildConfig;
import com.bridgellc.bridgeqr.R;
import com.bridgellc.bridgeqr.apiinterface.APIRequestHandler;
import com.bridgellc.bridgeqr.main.BaseActivity;
import com.bridgellc.bridgeqr.model.SignInEntity;
import com.bridgellc.bridgeqr.model.SignInResponse;
import com.bridgellc.bridgeqr.utils.AppConstants;
import com.bridgellc.bridgeqr.utils.DialogManager;
import com.bridgellc.bridgeqr.utils.DialogMangerOkCallback;
import com.bridgellc.bridgeqr.utils.GlobalMethods;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Locale;

public class SignInScreen extends BaseActivity implements View.OnClickListener,
        DialogMangerOkCallback {

    Button mSignInBtn;
    EditText mEmailEdt, mPwdEdt;
    SignInEntity mSignin;
    LoginButton fb_login_img;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    String mEmail, mPwd, fbUser_Id = "", fbUser_Email = "";
    boolean isFb = false;


    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject,
                                                GraphResponse graphResponse) {

                            try {
                                fbUser_Id = jsonObject.getString("id");
                                fbUser_Email = jsonObject.getString("email");
                                APIRequestHandler.getInstance().getSignInResponse(fbUser_Email, "", getString(R.string.two), fbUser_Id, SignInScreen.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (isFb) {
                                    FacebookSdk.sdkInitialize(getApplicationContext());
                                    LoginManager.getInstance().logOut();
                                }

                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields",
                    "id,name,email,birthday,gender,picture.width(300)");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
        }

        @Override
        public void onError(FacebookException e) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
            GlobalMethods.userDetails(false, "", "", "", "", "", "", "", "", "", "", "", SignInScreen.this);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk
                    .addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setContentView(R.layout.signin_screen);
        initComponents();
    }


    private void initComponents() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mSignInBtn = (Button) findViewById(R.id.footer_btn);

        mEmailEdt = (EditText) findViewById(R.id.email_edt);
        mPwdEdt = (EditText) findViewById(R.id.pwd_edt);


        fb_login_img = (LoginButton) findViewById(R.id.login_fb);
        fb_login_img.setBackgroundResource(R.drawable.fb_img);
        fb_login_img.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        mHeaderTxt.setText(getString(R.string.sign_in).toUpperCase(Locale.ENGLISH));
        mSignInBtn.setText(getString(R.string.sign_in));
        mHeadeLeftBtnLay.setVisibility(View.INVISIBLE);


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.login_fb:
                isFb = true;
                openFacebookDialog();
                break;

            case R.id.footer_btn:
                validateFields();
                break;


        }
    }

    private void openFacebookDialog() {
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken,
                                                       AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile newProfile) {
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        fb_login_img.setReadPermissions("email");
        fb_login_img.registerCallback(callbackManager, callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void validateFields() {
        mEmail = mEmailEdt.getText().toString().trim();
        mPwd = mPwdEdt.getText().toString().trim();

        if (mEmail.isEmpty() || (!GlobalMethods.isEmailValid(mEmail, false))) {
            DialogManager.showBasicAlertDialog(this, getString(R.string.app_name), getString(R.string.enter_email), this);
        } else if (mPwd.isEmpty()) {
            DialogManager.showBasicAlertDialog(SignInScreen.this, getString(R.string.app_name), getString(R.string.enter_pwd), this);
        } else {
            isFb = false;
            APIRequestHandler.getInstance().getSignInResponse(mEmail, mPwd, AppConstants.SUCCESS_CODE, "", this);
        }
    }

    @Override
    public void onOkClick() {

    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof SignInResponse) {
            SignInResponse signinres = (SignInResponse) responseObj;
            mSignin = signinres.getResult();
            if (isFb) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
            }
            if (signinres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                GlobalMethods.userDetails(true, mSignin.getUser_id(), mSignin.getFirst_name(), mSignin.getLast_name(), mSignin.getEmail_id(), mSignin.getPhone_number(), mSignin.getPassword(), mSignin.getUniversity_name(), mSignin.getUniversity_id(), mSignin.getDob(), mSignin.getGender(), mSignin.getLogin_type(), this);
                nextScreen(HomeScreen.class, true);
            } else {
                GlobalMethods.userDetails(false, "", "", "", "", "", "", "", "", "", "", "", SignInScreen.this);
                DialogManager.showBasicAlertDialog(this, getString(R.string.app_name), signinres.getMessage(), this);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }


}

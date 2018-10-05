package com.bridgellc.bridge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.BuildConfig;
import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.SignInEntity;
import com.bridgellc.bridge.main.BaseActivity;
import com.bridgellc.bridge.model.SignInResponse;
import com.bridgellc.bridge.model.SignUpResponse;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;
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

import retrofit.RetrofitError;

public class SignInScreen extends BaseActivity implements View.OnClickListener, DialogMangerOkCallback {

    private Button mSignInBtn;
    private EditText mEmailEdt, mPwdEdt;
    private String mEmail, mPwd;
    private SignInEntity mSignin;
    private LoginButton fb_login_img;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private String fbUser_Id = "";
    private String fbUser_Name = "";
    private String fbUser_Email = "";
    private String fbUser_Birthday = "";
    private String fbUser_Gender = "";
    private String fbUser_FirstName = "";
    private String fbUser_LastName = "";
    private String mDeviceId = "";
    int mLoginCount=0;


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

        //Facebook Init
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //UI Init
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

        mDeviceId = GlobalMethods.getStringValue(this, AppConstants.DEVICE_ID);

    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
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
                                fbUser_Name = jsonObject.getString("name");
                                fbUser_Email = jsonObject.getString("email");
//                                String fbUser_Birthday = jsonObject.getString("birthday");
                                fbUser_Gender = jsonObject.getString("gender");

                                String[] userName = fbUser_Name.split("\\s+");
                                if (userName.length == 2) {
                                    fbUser_FirstName = userName[0];
                                    fbUser_LastName = userName[1];

                                } else {
                                    fbUser_FirstName = userName[0];
                                    fbUser_LastName = userName[0];
                                }
                                if (fbUser_Gender.equalsIgnoreCase(getString(R.string.male))) {
                                    fbUser_Gender = getString(R.string.one);
                                } else {
                                    fbUser_Gender = getString(R.string.two);

                                }


                                APIRequestHandler.getInstance().getSignUpResponse(fbUser_FirstName, fbUser_LastName, fbUser_Email, "", "", "", "", fbUser_Gender, getString(R.string.two), mDeviceId, fbUser_Id, getString(R.string.three), SignInScreen.this);

                            } catch (Exception e) {
                                e.printStackTrace();

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
            GlobalMethods.userDetails(false, "", "", "", "", "", "", "", "", "", "", "", "", "",
                    "", "", "", "", SignInScreen.this);

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.sign_up_txt:
                UpdateProfileScreen.mHeader = getString(R.string.sign_up);
                UpdateProfileScreen.mSignUp = getString(R.string.sign_up);
                SignUpScreen.mHeader = getString(R.string.sign_up);
                SignUpScreen.mSignUp = getString(R.string.sign_up);

                AppConstants.SIGNUP_FIRSTNAME = "";
                AppConstants.SIGNUP_LASTNAME = "";
                AppConstants.SIGNUP_EMAIL = "";
                AppConstants.SIGNUP_PWD = "";


                GlobalMethods.storeValuetoPreference(SignInScreen.this,
                        GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM,
                        "");
                PhOTPEmailScreen.mScreenShows = 1;

                PhOTPEmailScreen.mFBSignupReg = getString(R.string.two);
                AppConstants.SIGNUPVERIFYBACK = getString(R.string.sign_in);
                GlobalMethods.storeValuetoPreference(SignInScreen.this, GlobalMethods.STRING_PREFERENCE, AppConstants.LOGINTYPE, getString(R.string.one));
                GlobalMethods.storeValuetoPreference(SignInScreen.this,
                        GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID, " ");
                nextScreen(PhOTPEmailScreen.class, true);
                break;
            case R.id.forgot_pwd_txt:
                nextScreen(ForgotPwdScreen.class, true);
                break;
            case R.id.login_fb:
                if (isNetworkAvailable(this)) {
                    openFacebookDialog();
                } else {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.no_internet), new DialogMangerOkCallback() {
                        @Override
                        public void onOkClick() {

                        }
                    });

                }
                break;

            case R.id.footer_btn:
                validateFields();

                break;


        }
    }

    private void openFacebookDialog() {
        // ((LoginActivity) mContext).onClickLogin();
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
            DialogManager.showBasicAlertDialog(this, getString(R.string.enter_email), this);
        } else if (mPwd.isEmpty()) {
            DialogManager.showBasicAlertDialog(SignInScreen.this, getString(R.string.enter_pwd), this);
        } else {

//            if (GlobalMethods.isPasswordValid(SignInScreen.this, mPwd).equalsIgnoreCase(getString
//                    (R.string.password_valid))) {
            mLoginCount=mLoginCount+1;
            APIRequestHandler.getInstance().getSignInResponse(mEmail, mPwd, mDeviceId,
                    String.valueOf(mLoginCount), this);
//            } else {
//                DialogManager.showBasicAlertDialog(SignInScreen.this, getString(R.string.app_name), GlobalMethods.isPasswordValid(SignInScreen.this, mPwd), this);
//
//            }
        }


    }

    @Override
    public void onOkClick() {

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof SignInResponse) {
            SignInResponse signinres = (SignInResponse) responseObj;
            mSignin = signinres.getResult();

            if (signinres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                GlobalMethods.userDetails(true, mSignin.getUser_id(), mSignin.getFirst_name(),
                        mSignin.getLast_name(), mSignin.getEmail_id(), mSignin.getEdu_email_id(), mSignin.getPhone_number(), mSignin.getPassword(), mSignin.getUniversity_name(), mSignin.getUniversity_id(), mSignin.getDob(), mSignin.getGender(), mSignin.getLogin_type(), mSignin.getBank_details(), mSignin.getCard_details(), mSignin.getPayment_mode(), mSignin.getPayment_details(), mSignin.getPartner(), this);
                GlobalMethods.storeValuetoPreference(SignInScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);

//                DialogManager.showBasicAlertDialog(SignInScreen.this, getString(R.string.sign_in), getString(R.string.serv_res_success), new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {
                GlobalMethods.storeValuetoPreference(SignInScreen.this,
                        GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
                        true);
                nextScreen(HomeScreenActivity.class, true);

//                    }
//                });
            } else {
                DialogManager.showBasicAlertDialog(this, signinres.getMessage(), this);
            }
        } else if (responseObj instanceof SignUpResponse) {
            final SignUpResponse signupres = (SignUpResponse) responseObj;
            mSignin = signupres.getResult();
            if (signupres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
//                DialogManager.showBasicAlertDialog(this, mHeaderTxt.getText().toString(), getString(R.string.serv_res_success), new DialogMangerOkCallback() {
//                    @Override
//                    public void onOkClick() {
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                GlobalMethods.userDetails(true, mSignin.getUser_id(), mSignin.getFirst_name(),
                        mSignin.getLast_name(), mSignin.getEmail_id(), mSignin.getEdu_email_id(), mSignin.getPhone_number(), mSignin.getPassword(), mSignin.getUniversity_name(), mSignin.getUniversity_id(), mSignin.getDob(), mSignin.getGender(), mSignin.getLogin_type(), mSignin.getBank_details(), mSignin.getCard_details(), mSignin.getPayment_mode(), mSignin.getPayment_details(), mSignin.getPartner(), SignInScreen.this);

//                        AppConstants.FB_ID = fbUser_Id;

                GlobalMethods.storeValuetoPreference(SignInScreen.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.FB_ID, fbUser_Id);
//                        if (mSignin.getIs_new_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) || GlobalMethods.isEmailValid(fbUser_Email, true)) {
                if (mSignin.getIs_new_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) && mSignin.getMobile_verify().equalsIgnoreCase(AppConstants.SUCCESS_CODE) && !mSignin.getUniversity_id().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    GlobalMethods.storeValuetoPreference(SignInScreen.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);

                    nextScreen(HomeScreenActivity.class, true);
                } else {

                    GlobalMethods.storeValuetoPreference(SignInScreen.this,
                            GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
                            false);

                    AppConstants.BACK_FB = "Fb";
                    AppConstants.SIGNUPVERIFYBACK = "Fb";
                    PhOTPEmailScreen.mScreenShows = 1;

                    AppConstants.SELECT_SCH_UNI = AppConstants.FAILURE_CODE;
                    AppConstants.SELECT_SCH_UNI_ID = AppConstants.FAILURE_CODE;


                    AppConstants.SIGNUP_FIRSTNAME = mSignin.getFirst_name();
                    AppConstants.SIGNUP_LASTNAME = mSignin.getLast_name();
                    AppConstants.SIGNUP_EMAIL = mSignin.getEmail_id();
                    AppConstants.SIGNUP_PWD = "";

                    if (mSignin.getIs_new_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) && !mSignin.getMobile_verify().equalsIgnoreCase(AppConstants.SUCCESS_CODE) && !mSignin.getUniversity_id().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

                        PhOTPEmailScreen.mFBSignupReg = getString(R.string.one);
                        GlobalMethods.storeValuetoPreference(SignInScreen.this,
                                GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM, "");
                        AppConstants.SIGNUP_DIRECT = getString(R.string.update);

                        nextScreen(PhOTPEmailScreen.class, true);
                    } else if (mSignin.getIs_new_user().equalsIgnoreCase(AppConstants.FAILURE_CODE) && mSignin.getMobile_verify().equalsIgnoreCase(AppConstants.SUCCESS_CODE) && mSignin.getUniversity_id().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {

//                        UpdateProfileScreen.mHeader = getString(R.string.update_profile);
//                        UpdateProfileScreen.mSignUp = getString(R.string.update);
//                        nextScreen(UpdateProfileScreen.class, true);


//                        AppConstants.SIGNUP_FIRSTNAME = mSignin.getFirst_name();
//                        AppConstants.SIGNUP_LASTNAME = mSignin.getLast_name();
//                        AppConstants.SIGNUP_EMAIL = mSignin.getEmail_id();
//                        AppConstants.SIGNUP_PWD = "";
//

                        AppConstants.SIGNUP_DIRECT = getString(R.string.update);
                        nextScreen(SelectUniversityNewScreen.class, true);

                    } else {

                        PhOTPEmailScreen.mFBSignupReg = getString(R.string.two);
                        GlobalMethods.storeValuetoPreference(SignInScreen.this,
                                GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUM, "");
                        AppConstants.SIGNUP_DIRECT = getString(R.string.update);
                        nextScreen(PhOTPEmailScreen.class, true);
                    }
                }
//                    }
//                });
            } else {

//                AppConstants.FB_ID = getString(R.string.zero);
                GlobalMethods.storeValuetoPreference(SignInScreen.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.FB_ID, AppConstants.FAILURE_CODE);

                DialogManager.showBasicAlertDialog(this, signupres.getMessage(), this);

            }
        }
    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {
        super.onRequestFailure(errorCode);
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        GlobalMethods.userDetails(false, "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", this);

    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

package com.smaat.renterblock.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.UserDetailsEntity;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.LoginResponse;
import com.smaat.renterblock.services.APIRequestHandler;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.DialogManager;
import com.smaat.renterblock.utils.EmailUtil;
import com.smaat.renterblock.utils.InterfaceEdtWithBtnCallback;
import com.smaat.renterblock.utils.NetworkUtil;
import com.smaat.renterblock.utils.PreferenceUtil;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Prithiv on 8/17/2017.
 * This class implements UI and Functions for login to our application
 */


public class LoginScreen extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.cancel_text)
    TextView mCancelText;

    @BindView(R.id.sign_up_btn)
    Button mSignUpBtn;

    @BindView(R.id.fb_lay)
    LinearLayout mFbLay;

    @BindView(R.id.google_lay)
    LinearLayout mGoogleLay;

    @BindView(R.id.email_edt)
    EditText mEmailEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;


    @BindView(R.id.ok_btn)
    Button mOkBtn;

    @BindView(R.id.agree_terms_txt)
    TextView mAgreeTxt;

    @BindView(R.id.login_parent_lay)
    ViewGroup mLoginViewGroup;

    private String loginTypeStr = AppConstants.EMAIL;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFbCallBackManager;
    private UserDetailsEntity mLoginEntityRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_login_screen);

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);
        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mLoginViewGroup);
        /*View Init*/
        initView();
    }

    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

         /*Google Login*/
        initGoogleLoginService();

        /*Facebook Login*/
        initFbLoginService();

        /*Spannable Text*/
        setSpannableTxt();

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

        facebookHashKey(this);
    }

    private void facebookHashKey(BaseActivity context) {
        PackageInfo packageInfo;
        String key;
        try {
            String packageName = context.getApplicationContext().getPackageName();

            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                System.out.println("Hash Key-----" + key);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Init Google service*/
    private void initGoogleLoginService() {
        GoogleSignInOptions googleSigninOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSigninOption)
                .build();
    }


    /*Init Fb service*/
    private void initFbLoginService() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        //noinspection deprecation
        AppEventsLogger.activateApp(this);

        mFbCallBackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFbCallBackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        DialogManager.getInstance().hideProgress();
                        getFacebookProfileDetails(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        DialogManager.getInstance().hideProgress();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        DialogManager.getInstance().hideProgress();
                    }
                });

    }

    /*View OnClick*/
    @OnClick({R.id.cancel_text, R.id.sign_up_btn, R.id.fb_lay, R.id.google_lay, R.id.forgot_pwd_txt, R.id.ok_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_text:
                mLoginEntityRes = new UserDetailsEntity();
                PreferenceUtil.storeUserDetails(this, mLoginEntityRes);
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, false);
                previousScreen(HomeScreen.class, true);
                break;
            case R.id.sign_up_btn:

                mLoginEntityRes = new UserDetailsEntity();
                mLoginEntityRes.setLogin_type(AppConstants.EMAIL);

                PreferenceUtil.storeUserDetails(this, mLoginEntityRes);
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, false);

                nextScreen(CreateAccountScreen.class, true);
                break;
            case R.id.fb_lay:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    loginTypeStr = AppConstants.FACEBOOK;
                    DialogManager.getInstance().showProgress(this);
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                } else {
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.no_internet), LoginScreen.this);
                }

                break;
            case R.id.google_lay:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    loginTypeStr = AppConstants.GOOGLE;
                    DialogManager.getInstance().showProgress(this);
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, 99);
                } else {
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.no_internet), LoginScreen.this);
                }

                break;
            case R.id.forgot_pwd_txt:
                DialogManager.getInstance().showForgotPwdPopup(this, new InterfaceEdtWithBtnCallback() {
                    @Override
                    public void onFirstEdtBtnClick(String emailIdStr) {
                        /*forgot pwd api call*/
                        APIRequestHandler.getInstance().forgotPwdAPICall(emailIdStr, LoginScreen.this);
                    }
                });
                break;
            case R.id.ok_btn:
                validateFields();
                break;
        }
    }

    private void setSpannableTxt() {
        SpannableString spanString = new SpannableString(
                "By tapping Register you agree to our \nTerms of Use and Privacy Policy");

        ClickableSpan termsOfUseSpanClick = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                AppConstants.CONTENT_HEADER = getString(R.string.terms_of_service);
                AppConstants.CONTENT_URL = AppConstants.TC;
                nextScreen(ContentURLScreen.class, false);
            }
        };

        ClickableSpan privacySpanClick = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                AppConstants.CONTENT_HEADER = getString(R.string.privacy_policy);
                AppConstants.CONTENT_URL = AppConstants.PP;
                nextScreen(ContentURLScreen.class, false);
            }
        };

        spanString.setSpan(termsOfUseSpanClick, 37, 50, 0);
        spanString.setSpan(privacySpanClick, 55, 69, 0);
        spanString.setSpan(new ForegroundColorSpan(Color.BLUE), 37, 50, 0);
        spanString.setSpan(new ForegroundColorSpan(Color.BLUE), 55, 69, 0);
        spanString.setSpan(new UnderlineSpan(), 37, 50, 0);
        spanString.setSpan(new UnderlineSpan(), 55, 69, 0);

        mAgreeTxt.setMovementMethod(LinkMovementMethod.getInstance());
        mAgreeTxt.setText(spanString, TextView.BufferType.SPANNABLE);
        mAgreeTxt.setSelected(false);

    }


    /*get user details from FB*/
    private void getFacebookProfileDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject,
                                            GraphResponse graphResponse) {

                        String fbUserNameStr, fbUserFirstNameStr, fbUserLastNameStr, fbIdStr, fbEmailStr;

                        try {

                            fbUserNameStr = (jsonObject.get("name") != null && !jsonObject.getString("name").isEmpty()) ? jsonObject.getString("name") : "";
                            fbIdStr = (jsonObject.get("id") != null && !jsonObject.getString("id").isEmpty()) ? jsonObject.getString("id") : "";
                            fbEmailStr = (jsonObject.get(AppConstants.EMAIL) != null && !jsonObject.getString(AppConstants.EMAIL).isEmpty()) ? jsonObject.getString(AppConstants.EMAIL) : "";
                            String[] userName = fbUserNameStr.split("\\s+");

                            fbUserFirstNameStr = userName[1];
                            fbUserLastNameStr = userName[userName.length - 1];


                            if (fbEmailStr.isEmpty()) {
                                DialogManager.getInstance().showAlertPopup(LoginScreen.this, getString(R.string.no_email_id_fb), LoginScreen.this);
                            } else {
                                mLoginEntityRes = new UserDetailsEntity();
                                mLoginEntityRes.setFirst_name(fbUserLastNameStr);
                                mLoginEntityRes.setLast_name(fbUserFirstNameStr);
                                mLoginEntityRes.setFacebook_id(fbIdStr);
                                mLoginEntityRes.setEmail_id(fbEmailStr);
                                mLoginEntityRes.setLogin_type(AppConstants.FACEBOOK);

                                /*login api call*/
                                APIRequestHandler.getInstance().loginAPICall(AppConstants.FACEBOOK, fbEmailStr, "", "", fbIdStr, "", LoginScreen.this);

                            }

                        } catch (Exception e) {
                            DialogManager.getInstance().showAlertPopup(LoginScreen.this, getString(R.string.no_email_id_fb), LoginScreen.this);
                            Log.e(AppConstants.TAG, e.toString());
                        }

                       /*logout from fb acc*/
                        FacebookSdk.sdkInitialize(LoginScreen.this);
                        LoginManager.getInstance().logOut();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (loginTypeStr.equals(AppConstants.FACEBOOK)) {
            mFbCallBackManager.onActivityResult(requestCode, resultCode, data);
        } else if (loginTypeStr.equals(AppConstants.GOOGLE)) {
            handleSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
        }
    }

    /*google after success*/
    private void handleSignInResult(GoogleSignInResult result) {

        DialogManager.getInstance().hideProgress();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();

            if (googleSignInAccount == null) {
                DialogManager.getInstance().showAlertPopup(LoginScreen.this, getString(R.string.please_enter_password), LoginScreen.this);
            } else {
                mLoginEntityRes = new UserDetailsEntity();
                mLoginEntityRes.setFirst_name(googleSignInAccount.getDisplayName());
                mLoginEntityRes.setEmail_id(googleSignInAccount.getEmail());
                mLoginEntityRes.setGoogle_id(googleSignInAccount.getId());
                mLoginEntityRes.setLogin_type(AppConstants.GOOGLE);

                /*login api call*/
                APIRequestHandler.getInstance().loginAPICall(AppConstants.GOOGLE, googleSignInAccount.getEmail(), "", "", "", googleSignInAccount.getId(), LoginScreen.this);
            }

        } else {
            // Signed out, show unauthenticated UI.
            DialogManager.getInstance().showToast(this, getString(R.string.google_unauthenticated));
        }

        /*logout from google acc*/
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    /*validate fields*/
    private void validateFields() {
        String emailStr = mEmailEdt.getText().toString().trim();
        String pwdStr = mPwdEdt.getText().toString().trim();
        if (emailStr.isEmpty()) {
            mEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_email), this);
        } else if (!EmailUtil.isEmailValid(emailStr)) {
            mEmailEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_valid_email), this);
        } else if (pwdStr.isEmpty()) {
            mPwdEdt.requestFocus();
            DialogManager.getInstance().showAlertPopup(this, getString(R.string.please_enter_password), this);
        } else {
            mLoginEntityRes = new UserDetailsEntity();

            mLoginEntityRes.setEmail_id(emailStr);
            mLoginEntityRes.setPassword(pwdStr);
            mLoginEntityRes.setLogin_type(AppConstants.EMAIL);

            /*login api call*/
            APIRequestHandler.getInstance().loginAPICall(AppConstants.EMAIL, emailStr, pwdStr, "", "", "", LoginScreen.this);

        }
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        if (responseObj instanceof LoginResponse) {
            LoginResponse loginResponse = (LoginResponse) responseObj;
            if (loginResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                if (loginResponse.getMsg().equals(getString(R.string.success))) {
                    PreferenceUtil.storeUserDetails(this, loginResponse.getResult());

                    PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, true);
                    DialogManager.getInstance().showToast(this,getString(R.string.logged_in));
                    nextScreen(HomeScreen.class, true);
                } else {
                    DialogManager.getInstance().showAlertPopup(this, loginResponse.getMsg(), this);
                }

            } else if (loginResponse.getError_code().equalsIgnoreCase(getString(R.string.two))) {
               /*new user - Fb signIn adn Google*/
                PreferenceUtil.storeUserDetails(this, mLoginEntityRes);
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.LOGIN_STATUS, false);
                nextScreen(CreateAccountScreen.class, true);

            } else {
                DialogManager.getInstance().showAlertPopup(this, loginResponse.getMsg(), this);
            }
        } else if (responseObj instanceof CommonResponse) {
            CommonResponse forgotPwdResponse = (CommonResponse) responseObj;
            DialogManager.getInstance().showAlertPopup(this, forgotPwdResponse.getMsg(), this);

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        DialogManager.getInstance().hideProgress();
    }
}

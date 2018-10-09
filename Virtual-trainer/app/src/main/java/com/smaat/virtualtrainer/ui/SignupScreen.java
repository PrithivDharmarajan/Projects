package com.smaat.virtualtrainer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.smaat.virtualtrainer.R;
import com.smaat.virtualtrainer.apiinterface.APIRequestHandler;
import com.smaat.virtualtrainer.main.BaseActivity;
import com.smaat.virtualtrainer.model.SignUpEntity;
import com.smaat.virtualtrainer.utils.AppConstants;
import com.smaat.virtualtrainer.utils.DialogManager;
import com.smaat.virtualtrainer.utils.DialogMangerTwoBtnCallback;
import com.smaat.virtualtrainer.utils.GlobalMethods;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tyrantgit.explosionfield.ExplosionField;

import static com.smaat.virtualtrainer.utils.GlobalMethods.resetExplosionFields;


public class SignupScreen extends BaseActivity implements DialogMangerTwoBtnCallback, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.parent_lay)
    ViewGroup mSignupViewGroup;

    @BindView(R.id.user_name_edt)
    EditText mUserNameEdt;

    @BindView(R.id.mail_id_edt)
    EditText mEmailEdt;

    @BindView(R.id.pwd_edt)
    EditText mPwdEdt;

    @BindView(R.id.signup_btn)
    Button mSignupBtn;

    private GoogleSignInOptions mGoogleSigninOption;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFbcallbackManager;
    private int mloginWithtypeInt;
    private static final String sUrlStr = "https://" + "api.linkedin.com"
            + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,picture-urls::(original))";

    private String mPwdStr;
    private ExplosionField mExplosionField;
    private Runnable mRunnable;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_signup_screen);

        mHandler = new Handler();
        initComponent();


    }

    private void initComponent() {

        //Init ExplosionAnimation
        mExplosionField = ExplosionField.attach2Window(this);

        ButterKnife.bind(this);
        setupUI(mSignupViewGroup);

        initGoogleLoginService();
        initFbLoginService();

        setEditActionListener();
    }

    private void setEditActionListener() {

        mPwdEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateFields();
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void initGoogleLoginService() {
        mGoogleSigninOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSigninOption)
                .build();
    }

    private void initFbLoginService() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mFbcallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mFbcallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        DialogManager.hideProgress();
                        getFacebookProfileDetails(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        DialogManager.hideProgress();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        DialogManager.hideProgress();
                        // App code
                    }
                });

    }
// set the permission to retrieve basic
//    information of User's linkedIn account

    public void linkedinSignIn() {
        //Linkedin Acc Login
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        DialogManager.showProgress(SignupScreen.this);
                        getLinkededinUserDetails();

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void getLinkededinUserDetails() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(SignupScreen.this, sUrlStr, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                DialogManager.hideProgress();
                try {
                    //get Acc Details scccess
                    showResult(result.getResponseDataAsJson());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                DialogManager.hideProgress();

            }
        });
    }

    public void showResult(JSONObject response) {

        try {
            APIRequestHandler.getInstance().signupAPICall(response.get("formattedName").toString(), response.get("emailAddress").toString(), "", mloginWithtypeInt + "", "", "", getString(R.string.two), SignupScreen.this);
            LISessionManager.getInstance(getApplicationContext()).clearSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.signup_btn, R.id.fb_lay, R.id.google_lay, R.id.linkedin_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn:
                mloginWithtypeInt = 1;
                validateFields();
                break;

            case R.id.fb_lay:
                if (GlobalMethods.isNetworkAvailable(this)) {
                    mloginWithtypeInt = 2;
                    DialogManager.showProgress(this);
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                } else {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.no_internet), getString(R.string.ok), false, getString(R.string.ok), true, true, SignupScreen.this);
                }
                break;
            case R.id.google_lay:
                if (GlobalMethods.isNetworkAvailable(this)) {
                    mloginWithtypeInt = 3;
                    DialogManager.showProgress(this);
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, 99);
                } else {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.no_internet), getString(R.string.ok), false, getString(R.string.ok), true, true, SignupScreen.this);
                }
                break;

            case R.id.linkedin_lay:
                if (GlobalMethods.isNetworkAvailable(this)) {
                    mloginWithtypeInt = 4;
                    linkedinSignIn();
                } else {
                    DialogManager.showBasicAlertDialog(this, getString(R.string.no_internet), getString(R.string.ok), false, getString(R.string.ok), true, true, SignupScreen.this);
                }
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (mloginWithtypeInt == 2) {
            mFbcallbackManager.onActivityResult(requestCode, resultCode, data);

        } else if (mloginWithtypeInt == 3) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        DialogManager.hideProgress();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            APIRequestHandler.getInstance().signupAPICall(acct.getDisplayName(), acct.getEmail(), "", mloginWithtypeInt + "", "", acct.getId(), "", this);
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        } else {
            // Signed out, show unauthenticated UI.
            DialogManager.showToastMessage(this, "unauthenticated");
        }
    }

    private void validateFields() {
        final String usernameStr = mUserNameEdt.getText().toString().trim();
        final String emailIdStr = mEmailEdt.getText().toString().trim();
        mPwdStr = mPwdEdt.getText().toString().trim();

        if (usernameStr.isEmpty()) {
            mUserNameEdt.requestFocus();
            DialogManager.showBasicAlertDialog(this, getString(R.string.enter_name), getString(R.string.ok), false, getString(R.string.ok), true, true, SignupScreen.this);
        } else if (emailIdStr.isEmpty() || (!GlobalMethods.isEmailValid(emailIdStr))) {
            mEmailEdt.requestFocus();
            DialogManager.showBasicAlertDialog(this, getString(R.string.enter_email), getString(R.string.ok), false, getString(R.string.ok), true, true, SignupScreen.this);
        } else if (mPwdStr.isEmpty()) {
            mPwdEdt.requestFocus();
            DialogManager.showBasicAlertDialog(this, getString(R.string.enter_pwd), getString(R.string.ok), false, getString(R.string.ok), true, true, SignupScreen.this);
        } else {

            mExplosionField.explode(mSignupBtn);

            mRunnable = new Runnable() {
                @Override
                public void run() {
                    {
                        mHandler.removeCallbacks(mRunnable);
                        APIRequestHandler.getInstance().signupAPICall(usernameStr, emailIdStr, mPwdStr, mloginWithtypeInt + "", "", "", "", SignupScreen.this);
                    }
                }
            };

            mHandler.postDelayed(mRunnable, 500);


        }
    }

    private void getFacebookProfileDetails(LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject,
                                            GraphResponse graphResponse) {

                        String fbUser_Id, fbUser_Name, fbUser_Email, fbUser_FirstName, fbUser_LastName;
                        try {

                            fbUser_Id = jsonObject.getString("id");
                            fbUser_Name = jsonObject.getString("name");
                            fbUser_Email = jsonObject.getString("email");
                            String[] userName = fbUser_Name.split("\\s+");
                            if (userName.length == 2) {
                                fbUser_FirstName = userName[0];
                                fbUser_LastName = userName[1];

                            } else {
                                fbUser_FirstName = userName[0];
                                fbUser_LastName = userName[0];
                            }


                            APIRequestHandler.getInstance().signupAPICall(fbUser_FirstName, fbUser_Email, "", mloginWithtypeInt + "", fbUser_Id, "", "", SignupScreen.this);

                            FacebookSdk.sdkInitialize(getApplicationContext());
                            LoginManager.getInstance().logOut();


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof SignUpEntity) {
            final SignUpEntity signupEntityRes = (SignUpEntity) responseObj;

            if (signupEntityRes.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                String userStatus = (signupEntityRes.getResult().getUser_account_type().equalsIgnoreCase(AppConstants.FAILURE_CODE)) ? getString(R.string.two) : AppConstants.SUCCESS_CODE;

                GlobalMethods.userDetails(userStatus, signupEntityRes.getResult().getSvt_user_id(), signupEntityRes.getResult().getUser_name(), signupEntityRes.getResult().getEmail_id(), mPwdStr, mloginWithtypeInt + "", SignupScreen.this);

                DialogManager.showToastMessage(this, getString(R.string.succ_login));

                if (signupEntityRes.getResult().getUser_account_type().equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
                    nextScreen(UserTypeScreen.class, true);
                } else {
                    nextScreen(HomeScreen.class, true);
                }

            } else {

                mExplosionField.clear();
                resetExplosionFields(mSignupViewGroup);
                DialogManager.showBasicAlertDialog(this, signupEntityRes.getMessage(), getString(R.string.ok), false, getString(R.string.ok), true, true, this);

            }
        }
    }

    @Override
    public void onRequestFailure(Throwable t) {
        super.onRequestFailure(t);
        mExplosionField.clear();
        resetExplosionFields(mSignupViewGroup);
    }

    @Override
    public void onBackPressed() {
        previousScreen(EntryScreen.class, true);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        DialogManager.hideProgress();
    }

    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}

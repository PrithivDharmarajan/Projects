package com.smaat.renterblock.ui;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.gson.Gson;
import com.smaat.renterblock.BuildConfig;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.FilterObjectEntity;
import com.smaat.renterblock.entity.SavedSearchLoginEntity;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.ForgetPasswordResponse;
import com.smaat.renterblock.model.LoginResponse;
import com.smaat.renterblock.savedsearch.SavedSearchResponseEntity;
import com.smaat.renterblock.sqlite.DatabaseManager;
import com.smaat.renterblock.sqlite.RentersBlockDatabase;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GPSTracker;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;
import com.smaat.renterblock.webservice.ServiceRequestHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends BaseActivity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener, DialogMangerCallback {

	// private Session.StatusCallback statusCallback = new
	// SessionStatusCallback();
	private ProgressDialog progressDialog;
	private TextView mSignup, mAgree, mLoginGoogle;
//	, mLoginFacebook;
	private EditText mEmail, mPass;
	private Button mOk;
	// private SignInButton mLoginGoogle;
	private LinearLayout mForgotLay, mCancel;
	private boolean mIntentInProgress;
	private static final int RC_SIGN_IN = 9000;
	private int service;
	private String loginType = "";
	private String personName, personPhotoUrl, personGooglePlusProfile, email,
			ide;
	public static String mFilterApiObject, mFilterDefaultObject, mUserID,
			mFilterType;
	public static GoogleApiClient mGoogleApiClient;
	private boolean mSignInClicked, mIsButtoClicked = false;
	private ConnectionResult mConnectionResult;
	private String fbFirstName;
	private String fb_user_email;
	private String fbUserName;
	private String fbLastName;
	private String fbId;
	private String fbEmailID;
	private static final String TAG = "VideoChatMainActivity";
	private boolean isGoogle = true;
	private static Context mContext;
	private Bundle mBundle;
	private String loginFor = "FOR_EMAIL";
	private Dialog mAlertPopUp;
	private ArrayList<SavedSearchLoginEntity> saved_login_list;
	private SavedSearchResponseEntity mSavedSearch;
	private FilterObjectEntity filter_obj_ent;
	private String DeviceID, Device;

	private boolean is_moved = false;

	private EasyTracker easyTracker;
	private Tracker mTracker;

	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	private ProfileTracker profileTracker;
	// Facebook Login
	private LoginButton loginbtn;

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
								fbFirstName = jsonObject.getString("name");
								if (jsonObject.get("email") != null&&!jsonObject.getString("email").isEmpty()) {
									fb_user_email = jsonObject.get("email") != null ? jsonObject.getString("email") : " ";
									fbUserName = jsonObject.getString("name");
									fbLastName = "";
									fbId = jsonObject.getString("id");

									if (fb_user_email == null) {
										fbEmailID = "";
									} else {
										fbEmailID = fb_user_email;
									}
									if (progressDialog != null) {
										progressDialog.dismiss();
									}
									loginType = "facebook";
									String url = AppConstants.BASE_URL + "login";
									String type = (String) GlobalMethods
											.getValueFromPreference(
													LoginActivity.this,
													GlobalMethods.STRING_PREFERENCE,
													AppConstants.pref_type);
									service = 1;
									HashMap<String, Object> params = new HashMap<String, Object>();
									params.put("facebook_id", fbId);
									params.put("email_id", fbEmailID);
									params.put("login_type", loginType);
									params.put("device_id", DeviceID);
									params.put("device", Device);
									params.put("type", type);
									ServiceRequestHandler.getInstance().getLogin(
											url, aq(), LoginActivity.this, params);
								} else {
									onShowAlertPopUp();

								}
							}catch (Exception e) {
								e.printStackTrace();
							}
							// try {
							// String email = jsonObject.getString("email");
							// String name = jsonObject.getString("name");
							// String id = jsonObject.getString("id");
							// String profile_pic = "http://graph.facebook.com/"
							// + id + "/picture?type=large";
							// callUserRegistrationService(name, email, "",
							// "FB", profile_pic);
							// } catch (org.json.JSONException e) {
							// e.printStackTrace();
							// }
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

		}

		@Override
		public void onError(FacebookException e) {

		}
	};

	private void onShowAlertPopUp() {
		mAlertPopUp = new Dialog(LoginActivity.this,android.R.style.Theme_Translucent_NoTitleBar);
		mAlertPopUp.setCancelable(false);
		mAlertPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mAlertPopUp.setContentView(R.layout.alert_popup);

		Button mOkclick = (Button)mAlertPopUp.findViewById(R.id.ok);
		TextView mMessageTxt = (TextView)mAlertPopUp.findViewById(R.id.alert_txt_facebook_report);

		mMessageTxt.setText(getString(R.string.facebook_report));

		mOkclick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mAlertPopUp.dismiss();
			}
		});
		mAlertPopUp.show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		FacebookSdk.sdkInitialize(getApplicationContext());
		if (BuildConfig.DEBUG) {
			FacebookSdk.setIsDebugEnabled(true);
			FacebookSdk
					.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		}
		setContentView(R.layout.login_screen);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout_login);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);

		initComponents();
		GlobalMethods.storeValuetoPreference(LoginActivity.this,
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.SYNC_CONTACTS, "1");
		setGoogleAnalytics(LoginActivity.this);
		// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		// Build a GoogleApiClient with access to the Google Sign-In API and the
// options specified by gso.
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			loginFor = mBundle.getString("login");
			if (loginFor == null) {
				loginFor = "FOR_EMAIL";
			}
		}

		/* Push Notification */

		String deviceId = (String) GlobalMethods
				.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_deviceReg_Id);

		boolean isRegistered = (Boolean) GlobalMethods.getValueFromPreference(
				this, GlobalMethods.BOOLEAN_PREFERENCE,
				AppConstants.pref_device_reg_status);

		boolean isDeveiceIdChanged = (Boolean) GlobalMethods
				.getValueFromPreference(this, GlobalMethods.BOOLEAN_PREFERENCE,
						AppConstants.pref_device_id_changed);
		if (!isRegistered || isDeveiceIdChanged) {
			if (deviceId != null && !deviceId.equals("")) {
				if (deviceId.equalsIgnoreCase("0")) {
					//GCMRegistrar.register(this, AppConstants.SENDER_ID);
				}
			} else {
				//GCMRegistrar.register(this, AppConstants.SENDER_ID);
			}
		}
		DeviceID = (String) GlobalMethods
				.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_deviceReg_Id);
		Device = "Android";

	}

	@Override
	protected void onResume() {
		// Session.getActiveSession().removeCallback(statusCallback);
		super.onResume();
	}

	public void initComponents() {
		Typeface helvetica_bold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		Typeface helvetica_light = TypefaceSingleton.getInstance()
				.getHelveticaLight(this);
		Typeface signika_bold = TypefaceSingleton.getInstance().getSignikaBold(
				this);

		TextView mTitletext = (TextView) findViewById(R.id.renters);
		mTitletext.setTypeface(signika_bold);

		mContext = LoginActivity.this;
		progressDialog = new ProgressDialog(this);

		mSignup = (TextView) findViewById(R.id.signup);
		mLoginGoogle = (TextView) findViewById(R.id.login_google);
		loginbtn = (LoginButton) findViewById(R.id.login_fb);
		loginbtn.setBackgroundResource(R.drawable.sign_up_btn);
		loginbtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
//		mLoginFacebook = (TextView) findViewById(R.id.login_fb);
		mOk = (Button) findViewById(R.id.ok_btn);
		mEmail = (EditText) findViewById(R.id.email_id);
		mEmail.setHintTextColor(Color.parseColor("#969696"));
		mPass = (EditText) findViewById(R.id.pasword);
		mPass.setHintTextColor(Color.parseColor("#969696"));
		mCancel = (LinearLayout) findViewById(R.id.cancel);
		mAgree = (TextView) findViewById(R.id.agree_text);
		mForgotLay = (LinearLayout) findViewById(R.id.forgot);
		mAgree.setText(handleSpannableTextString());
		mAgree.setMovementMethod(LinkMovementMethod.getInstance());
		// mLoginGoogle.setTypeface(helvetica_light);
//		mLoginFacebook.setTypeface(helvetica_light);
		mOk.setTypeface(helvetica_bold);
		mSignup.setTypeface(helvetica_bold);
		mAgree.setTypeface(helvetica_light);

		setClickListener();

	}

	public void setClickListener() {
		mSignup.setOnClickListener(this);
		mLoginGoogle.setOnClickListener(this);
		mOk.setOnClickListener(this);
		mForgotLay.setOnClickListener(this);
		mCancel.setOnClickListener(this);
	}

	// private void onClickLogin() {
	//
	// Session session = Session.getActiveSession();
	//
	// if (session == null) {
	// if (session == null) {
	// session = new Session(this);
	// }
	// Session.setActiveSession(session);
	// if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
	// session.openForRead(new Session.OpenRequest(this)
	// .setCallback(statusCallback));
	// }
	// }
	// if (!session.isOpened() && !session.isClosed()) {
	// OpenRequest op = new Session.OpenRequest((Activity) this);
	// op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
	// op.setCallback(statusCallback);
	//
	// List<String> permissions = new ArrayList<String>();
	// permissions.add("email");
	// permissions.add("user_birthday");
	// permissions.add("user_location");
	//
	// op.setPermissions(permissions);
	// Session.setActiveSession(session);
	// session.openForRead(op);
	// } else {
	// Log.d("INSIDE ", "B");
	// Session.openActiveSession(this, true, statusCallback);
	// }
	// }

	// private class SessionStatusCallback implements Session.StatusCallback {
	// @Override
	// public void call(Session session, SessionState state,
	// Exception exception) {
	// getUserEmailFromFacebook();
	// }
	// }

	// private void getUserEmailFromFacebook() {
	// Session session = Session.getActiveSession();
	// if (session!=null && session.isOpened()) {
	// if (progressDialog != null) {
	// progressDialog.setCancelable(false);
	// progressDialog.setMessage("Facebook");
	// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// progressDialog.show();
	// }
	//
	// Request.newMeRequest(session, new Request.GraphUserCallback() {
	//
	// @Override
	// public void onCompleted(final GraphUser user,
	// final Response response) {
	//
	// if (user != null) {
	//
	// fbFirstName = user.getFirstName();
	// fb_user_email = (String) response.getGraphObject()
	// .getProperty("email");
	// fbUserName = user.getUsername();
	// fbLastName = user.getLastName();
	// fbId = user.getId();
	//
	// if (fb_user_email == null) {
	// fbEmailID = "";
	// } else {
	// fbEmailID = fb_user_email;
	// }
	// if (progressDialog != null) {
	// progressDialog.dismiss();
	// }
	// loginType = "facebook";
	// String url = AppConstants.BASE_URL + "login";
	// String type = (String) GlobalMethods
	// .getValueFromPreference(LoginActivity.this,
	// GlobalMethods.STRING_PREFERENCE,
	// AppConstants.pref_type);
	// service = 1;
	// HashMap<String, Object> params = new HashMap<String, Object>();
	// params.put("facebook_id", fbId);
	// params.put("email_id", fbEmailID);
	// params.put("login_type", loginType);
	// params.put("device_id", DeviceID);
	// params.put("device", Device);
	// params.put("type", type);
	// ServiceRequestHandler.getInstance().getLogin(url, aq(),
	// LoginActivity.this, params);
	// } else {
	// progressDialog.dismiss();
	// }
	// }
	// }).executeAsync();
	// }
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
//		 if (Session.getActiveSession() != null) {
//		 Session.getActiveSession().onActivityResult(this, requestCode,
//		 resultCode, data);
//		 }
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
			mSignInClicked = false;
			mIntentInProgress = false;
		}
		else {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {

		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
			try {

				AppConstants.Login_From_Map="true";
				Intent inte = new Intent(LoginActivity.this,
						CreateAccountScreen.class);
				inte.putExtra("name", acct.getDisplayName());
				inte.putExtra("email", acct.getEmail());
				inte.putExtra("from", "fromGoogle");
				inte.putExtra("googleID", acct.getId());
				inte.putExtra("loginType", "google");
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				startActivity(inte);

			} catch (Exception e) {
				DialogManager.showToast(LoginActivity.this, e.toString());
				e.printStackTrace();
			}
			//            DialogManager.showToast(this, "Name : "+acct.getDisplayName()+"\nEmail ID : "+acct.getEmail());
		} else {

		DialogManager.showToast(getApplicationContext(),"Google Sign in failed");
			// Signed out, show unauthenticated UI.
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// if (isGoogle) {
		// if (!mGoogleApiClient.isConnected()) {
		// mGoogleApiClient.connect();
		// isGoogle = false;
		// }
		// } else {
		// // Session.getActiveSession().addCallback(statusCallback);
		// isGoogle = true;
		// }
	}

	@Override
	public void onStop() {

		super.onStop();
		if (!isGoogle) {
			if (mGoogleApiClient.isConnected()) {
				mGoogleApiClient.disconnect();
			}
			isGoogle = true;
		} else {
			// Session.getActiveSession().removeCallback(statusCallback);
			isGoogle = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Session session = Session.getActiveSession();
		// if (session != null) {
		// session.close();
		// }
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.signup:
			AppConstants.MYFAVPLUS = "ISFIRST";
			loginType = "email";
			if (AppConstants.Login_From_Map.equals("true")) {
				AppConstants.GET_START = "LOGIN";
				Intent inte = new Intent(LoginActivity.this,
						CreateAccountScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			} else {
				Intent inte = new Intent(LoginActivity.this,
						RegisterScreen.class);
				inte.putExtra("loginType", "email");
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);

			}
			// finish();
			break;
		case R.id.login_fb:
			loginType = "facebook";
			openFacebookDialog();
			break;
		case R.id.login_google:
			loginType = "google";
			mIsButtoClicked = true;

			/*if (!mGoogleApiClient.isConnected()) {
				signInWithGplus();
			} else {
				getProfileInformation();
			}*/

			Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
			startActivityForResult(signInIntent, RC_SIGN_IN);

			break;
		case R.id.ok_btn:
			validateLogin();
			break;
		case R.id.cancel:
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.forgot:
			showForgotPasswordDialog();
			break;
		default:
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

		loginbtn.setReadPermissions("email");
		loginbtn.registerCallback(callbackManager, callback);
	}

	private void validateLogin() {
		String username = mEmail.getText().toString().trim();
		String password = mPass.getText().toString().trim();
		loginType = "email";
		if (username.length() == 0) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.please_enter_email));
			return;
		} else if ((!GlobalMethods.isEmailValid(username))) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.please_enter_valid_email));
		} else if (password.length() == 0) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.please_enter_password));
			return;
		} else {
			callLoginService(username, password);

		}
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		if (service == 1) {
			LoginResponse loginResponse = (LoginResponse) responseObj;
			if (loginResponse.error_code
					.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
				mUserID = loginResponse.getResult().getUser_id();
				if (mUserID != null) {
					if (loginResponse.getResult().getFilter_object() != null
							&& !loginResponse.getResult().getFilter_object()
									.equals("")) {
						updateFiltetablewithuserId();
					}
					mFilterApiObject = loginResponse.getResult()
							.getFilter_object();
					saved_login_list = loginResponse.getResult()
							.getSavedsearch();
					GlobalMethods.storeValuetoPreference(LoginActivity.this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.pref_userId, loginResponse.getResult()
									.getUser_id());
					GlobalMethods.storeValuetoPreference(LoginActivity.this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.pref_user_name,
							loginResponse.result.getUser_name());

					GlobalMethods.storeValuetoPreference(LoginActivity.this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.pref_type,
							loginResponse.result.getUser_type());
					GlobalMethods.storeValuetoPreference(LoginActivity.this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.ENHANCED_PROFILE,
							loginResponse.result.getEnhanced_profile());

					System.out.println("type of profile "
							+ loginResponse.result.getEnhanced_profile());
					if (saved_login_list != null && !saved_login_list.isEmpty()) {
						for (int i = 0; i < saved_login_list.size(); i++) {
							mSavedSearch = new SavedSearchResponseEntity();
							Gson gson = new Gson();
							try {
								mSavedSearch = gson.fromJson(saved_login_list
										.get(i).getFilter_object(),
										SavedSearchResponseEntity.class);
							} catch (Exception e) {
								e.printStackTrace();
							}
							String user_id = saved_login_list.get(i)
									.getUser_id();
							String filter_type = saved_login_list.get(i)
									.getFilter_type();
							addSavedSearchValiesinDb(user_id,
									mSavedSearch.getFilter_name(), filter_type,
									mSavedSearch.getLatitude(),
									mSavedSearch.getLongitude());
						}
					}

					if (mFilterApiObject != null && mFilterApiObject.equals("")) {
						Gson gson = new Gson();
						mFilterDefaultObject = gson
								.toJson(SplashScreen.mLocaleSellFilterObjectEntity);
						setFilterApply();
					} else {
						filter_obj_ent = new FilterObjectEntity();
						Gson gson = new Gson();
						filter_obj_ent = gson.fromJson(mFilterApiObject,
								FilterObjectEntity.class);
						if (filter_obj_ent != null) {
							SplashScreen.mLocaleSellFilterObjectEntity = filter_obj_ent
									.getSale();
							SplashScreen.mLocaleRentFilterObjectEntity = filter_obj_ent
									.getRent();
							SplashScreen.mLocaleSoldFilterObjectEntity = filter_obj_ent
									.getSold();
						}
						if (SplashScreen.mTargetClass != null) {
							launchActivity(SplashScreen.mTargetClass);
						} else {
							Intent mapIntent = new Intent(LoginActivity.this,
									MapFragmentActivity.class);
							startActivity(mapIntent);
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							finish();
						}
						// finish();
					}
				} else {
					DialogManager.showCustomAlertDialog(this, this,
							loginResponse.getMsg());
				}
				sendDeviceID(mUserID, DeviceID, Device);
			} else {
				// DialogManager.showCustomAlertDialog(this, this,
				// loginResponse.getMsg());
				if (!is_moved) {
					is_moved = true;
					if (AppConstants.Login_From_Map.equalsIgnoreCase("true")) {
						if (loginType == "facebook") {
							Intent inte = new Intent(LoginActivity.this,
									CreateAccountScreen.class);
							inte.putExtra("mName", fbUserName);
							inte.putExtra("from", "fromFB");
							inte.putExtra("FBID", fbId);
							inte.putExtra("FirstName", fbFirstName);
							inte.putExtra("LastName", fbLastName);
							inte.putExtra("FBEmail", fbEmailID);
							inte.putExtra("loginType", "facebook");
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							startActivity(inte);
							is_moved = false;
						} else if (loginType == "google") {
							Intent inte = new Intent(LoginActivity.this,
									CreateAccountScreen.class);
							inte.putExtra("name", personName);
							inte.putExtra("email", email);
							inte.putExtra("from", "fromGoogle");
							inte.putExtra("googleID", ide);
							inte.putExtra("loginType", "google");
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							startActivity(inte);
							is_moved = false;
						}
						sendDeviceID(mUserID, DeviceID, Device);
					} else {
						if (loginType == "facebook") {
							Intent inte = new Intent(LoginActivity.this,
									RegisterScreen.class);
							inte.putExtra("mName", fbUserName);
							inte.putExtra("from", "fromFB");
							inte.putExtra("FBID", fbId);
							inte.putExtra("FirstName", fbFirstName);
							inte.putExtra("LastName", fbLastName);
							inte.putExtra("FBEmail", fbEmailID);
							inte.putExtra("loginType", "facebook");
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							startActivity(inte);
							is_moved = false;
						} else if (loginType == "google") {
							Intent inte = new Intent(LoginActivity.this,
									RegisterScreen.class);
							inte.putExtra("name", personName);
							inte.putExtra("email", email);
							inte.putExtra("from", "fromGoogle");
							inte.putExtra("googleID", ide);
							inte.putExtra("loginType", "google");
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
							startActivity(inte);
							is_moved = false;
						} else {
							DialogManager.showCustomAlertDialog(this, this,
									loginResponse.getMsg());
						}
						sendDeviceID(mUserID, DeviceID, Device);
					}

				}
			}
		} else if (service == 2) {
			ForgetPasswordResponse commonResponse = (ForgetPasswordResponse) responseObj;
			if (commonResponse.error_code
					.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
				DialogManager.showCustomAlertDialog(this, this,
						commonResponse.getMsg());
			}
		}
	}

	private void updateFiltetablewithuserId() {
		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();

		String query = "UPDATE local_rent_property SET user_id=" + mUserID;
		String query1 = "UPDATE local_sell_property SET user_id=" + mUserID;
		String query2 = "UPDATE local_sold_property SET user_id=" + mUserID;

		try {
			db.execSQL(query);
			db.execSQL(query1);
			db.execSQL(query2);
			System.out.println("Rent DB " + query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DatabaseManager.getInstance().closeDatabase();
	}

	private void addSavedSearchValiesinDb(String user_id, String filter_name,
			String filter_type, String lat, String longi) {
		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("location", filter_name);
			values.put("property_type", filter_type);
			values.put("user_id", user_id);
			values.put("latitude", String.valueOf(lat));
			values.put("Longitude", String.valueOf(longi));
			db.insert(RentersBlockDatabase.saved_search, null, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager.getInstance().closeDatabase();
	}

	private void setFilterApply() {

		String Url = AppConstants.BASE_URL + "filtersearch";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", mUserID);
		params.put("filter_type", "Rent");
		params.put("filter_object", mFilterDefaultObject);

		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json != null) {
							if (SplashScreen.mTargetClass != null) {
								launchActivity(SplashScreen.mTargetClass);
							} else {
								Intent mapIntent = new Intent(
										LoginActivity.this,
										MapFragmentActivity.class);
								startActivity(mapIntent);
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
								finish();
							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

	private void callLoginService(String username, String password) {

		double latitude = 0.0;
		double longitude = 0.0;
		GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation() != false) {
			latitude = tracker.getLatitude();
			longitude = tracker.getLongitude();
		}
		service = 1;
		// String url = AppConstants.LOGIN_API;

		String url = AppConstants.BASE_URL + "login";

		HashMap<String, Object> params = new HashMap<String, Object>();

		String type = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);

		params.put("email_id", username);
		params.put("password", password);
		params.put("login_type", loginType);
		params.put("type", type);
		params.put("device_id", DeviceID);
		params.put("device", Device);
		params.put("latitude", latitude);
		params.put("longitude", longitude);

		ServiceRequestHandler.getInstance().getLogin(url, aq(), this, params);

	}

	public void showForgotPasswordDialog() {
		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.forgot_password);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		wmlp.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		final EditText mForgotEmail = (EditText) mDialog
				.findViewById(R.id.forgot_pass_edit);

		Button mForgotSend = (Button) mDialog
				.findViewById(R.id.forgot_send_button);
		Button mCancel = (Button) mDialog.findViewById(R.id.forgot_pass_button);
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
		mForgotSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String emailId = mForgotEmail.getText().toString().trim();
				if (emailId.length() == 0) {
					DialogManager.showCustomAlertDialog(LoginActivity.this,
							LoginActivity.this,
							getString(R.string.please_enter_email));
					return;
				} else if ((!GlobalMethods.isEmailValid(emailId))) {
					DialogManager.showCustomAlertDialog(LoginActivity.this,
							LoginActivity.this,
							getString(R.string.please_enter_valid_email));
				} else {
					sendForgotPasswordWS(emailId);
					mDialog.dismiss();
				}

			}
		});

		mDialog.show();
	}

	private void sendForgotPasswordWS(String emailId) {
		String url = AppConstants.BASE_URL + "forgotpassword";
		service = 2;
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("email_id", emailId);

		ServiceRequestHandler.getInstance().processForgotPassword(url, aq(),
				this, params);
	}

	public SpannableString handleSpannableTextString() {
		String activityText = "";
		SpannableString spannableString;

		activityText = getString(R.string.agree_text);
		activityText += " " + getString(R.string.terms_of_use);

		activityText += " " + getString(R.string.and_text);

		activityText += " " + getString(R.string.privacy_policy);
		spannableString = new SpannableString(activityText);

		ClickableSpan clickableSpan = new ClickableSpan() {

			@Override
			public void onClick(View arg0) {

				// Intent tc = new Intent(LoginActivity.this,
				// SettingsCommonActivity.class);
				// tc.putExtra("option", "1");
				// overridePendingTransition(R.anim.slide_in_right,
				// R.anim.slide_out_left);
				// startActivity(tc);
				showTermsConditionsDialog(getString(R.string.terms_of_use),
						"content/tc");

			}

		};

		ClickableSpan clickableSpan1 = new ClickableSpan() {

			@Override
			public void onClick(View arg0) {
				// Intent tc = new Intent(LoginActivity.this, TCFragment.class);
				// tc.putExtra("option", "2");
				// overridePendingTransition(R.anim.slide_in_right,
				// R.anim.slide_out_left);
				// startActivity(tc);
				showTermsConditionsDialog(getString(R.string.privacy_policy),
						"content/pp");
			}

		};
		int startLength = activityText.length();

		spannableString.setSpan(clickableSpan, (startLength - 31),
				startLength - 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(
				new ForegroundColorSpan(Color.parseColor("#007afc")),
				(startLength - 31), startLength - 19,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(clickableSpan1, (startLength - 14),
				startLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(
				new ForegroundColorSpan(Color.parseColor("#007afc")),
				(startLength - 14), startLength,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spannableString;
	}

	public void googleLogin() {
		service = 1;
		// String url = AppConstants.LOGIN_API;

		String url = AppConstants.BASE_URL + "login";
		String type = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("google_id", ide);
		params.put("email_id", email);
		params.put("login_type", loginType);
		params.put("device_id", DeviceID);
		params.put("device", Device);
		params.put("type", type);

		ServiceRequestHandler.getInstance().getLogin(url, aq(), this, params);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}


	@Override
	public void onConnected(@Nullable Bundle bundle) {

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}
		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			}
		}

	}



	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}


	public static void signOutFromGplus() {
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		is_moved = false;
	}

	public void showTermsConditionsDialog(String headertext, String url) {

		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.activity_settings_common);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		mDialog.setCancelable(true);
		LinearLayout mBack = (LinearLayout) mDialog
				.findViewById(R.id.back_icon);
		TextView mContentText = (TextView) mDialog
				.findViewById(R.id.content_txt);
		mContentText.setMovementMethod(new ScrollingMovementMethod());
		TextView mHeaderTxt = (TextView) mDialog.findViewById(R.id.header_txt);
		mHeaderTxt.setText(headertext);

		getDetails(url, mContentText);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void getDetails(String url, final TextView mContentText) {

		String URL = AppConstants.BASE_URL + url;

		GsonTransformer t = new GsonTransformer();
		aq().transformer(t).progress(DialogManager.getProgressDialog(this))
				.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(
									json.toString(), CommonResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(
									AppConstants.SUCCESS_CODE)) {
								mContentText.setText(mResponse.getResult()
										.toString());
							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

}

package com.smaat.jolt.ui;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APICommonInterface;
import com.smaat.jolt.model.SignInResponse;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.DialogMangerCallback;
import com.smaat.jolt.util.GlobalMethods;
import com.smaat.jolt.util.TypefaceSingleton;

public class SignInScreen extends BaseActivity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener, DialogMangerCallback {
	private TextView mheader_txt, mTopTExt, mSignLater, mFacebookTxt,
			mGoogleTxt, mEmailTxt;

	// Sign in With Google
	private static final int RC_SIGN_IN = 0,PROFILE_PIC_SIZE = 400;
	private static GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress,mSignInClicked;
	private ConnectionResult mConnectionResult;
	private LinearLayout google_lay;
	private String mUserName, mEmail, mUserPhotoID, mUserPhotoURL,mRegId;

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sign_in_screen);
		pushNotification();
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_login_lay);
		Typeface mTypeface = TypefaceSingleton.getTypeface()
				.getHelveticaNeueMedium(this);
		setFont(root, mTypeface);
		initComponents();
		googlePlusLogin();

	}

	private void callFBLogin() {
		faceBookLogin();
	}

	private void faceBookLogin() {

		Session session = Session.getActiveSession();

		if (session == null) {
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForRead(new Session.OpenRequest(this)
						.setCallback(statusCallback));
			}
		}
		if (!session.isOpened() && !session.isClosed()) {
			OpenRequest op = new Session.OpenRequest((Activity) this);
			op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
			op.setCallback(statusCallback);

			List<String> permissions = new ArrayList<String>();
			permissions.add(getString(R.string.email));
			permissions.add(getString(R.string.user_birthday));
			permissions.add(getString(R.string.user_location));

			op.setPermissions(permissions);

			Session.setActiveSession(session);
			session.openForRead(op);
		} else {
			Session.openActiveSession(this, true, statusCallback);
		}
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			getUserEmailFromFacebook();
		}
	}

	private void getUserEmailFromFacebook() {
		Session session = Session.getActiveSession();
		if (session.isOpened()) {

			DialogManager.showProgress(SignInScreen.this);
			Request mRequest = Request.newMeRequest(session,
					new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								com.facebook.Response response) {
							DialogManager.hideProgress(SignInScreen.this);
							if (user != null) {

								mUserName = user.getName();
								mEmail = (String) response.getGraphObject()
										.getProperty(getString(R.string.email));
								mUserPhotoID = user.getId();

								mUserPhotoURL = "https://graph.facebook.com/"
										+ mUserPhotoID + "/picture?width=500&height=500";

								callUserRegistrationService(
										mUserName,
										mEmail,
										mUserPhotoURL,
										AppConstants.LOGIN_TYPE_FB,
										AppConstants.DEVICE_TYPE,
										GlobalMethods
												.getDeviceToken(SignInScreen.this));
							}

						}
					});
			String REQUEST_FIELDS = TextUtils.join(",", new String[] { getString(R.string.id),
					getString(R.string.name), getString(R.string.picture), getString(R.string.email) });

			Bundle parameters = new Bundle();
			parameters.putString(getString(R.string.fields), REQUEST_FIELDS);
			mRequest.setParameters(parameters);
			Request.executeBatchAsync(mRequest);
		}
	}

	private void pushNotification() {

		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);

			mRegId = GCMRegistrar.getRegistrationId(this);

			if (mRegId.equals("")) {
				GCMRegistrar.register(this, AppConstants.SENDER_ID);
			} else {
				if (GCMRegistrar.isRegisteredOnServer(this)) {

					GlobalMethods.storeValuetoPreference(this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.DEVICE_ID, mRegId);

					GlobalMethods.storeValuetoPreference(this,
							GlobalMethods.BOOLEAN_PREFERENCE,
							AppConstants.ISREGISTERED, false);

					GlobalMethods
							.storeValuetoPreference(this,
									GlobalMethods.BOOLEAN_PREFERENCE,
									AppConstants.ISDEVICEIDCHANGED,
									AppConstants.DEVICE_ID
											.equalsIgnoreCase(mRegId) ? false
											: true);
				} else {
					GCMRegistrar.register(this, AppConstants.SENDER_ID);
				}
			}
		} catch (UnsupportedOperationException e) {
			DialogManager
					.showToast(
							this,getString(R.string.not_support_google_services));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		}
		DialogManager.hideProgress(SignInScreen.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Session session = Session.getActiveSession();
//		session.closeAndClearTokenInformation();
		if (session != null) {
			session.close();
		}
	}

	@Override
	public void onBackPressed() {

		finish();
	}

	public void initComponents() {
		mheader_txt = (TextView) findViewById(R.id.jolt_text);
		mSignLater = (TextView) findViewById(R.id.sign_later);
		Typeface mTypeface = TypefaceSingleton.getTypeface().getCorbel(this);
		mheader_txt.setTypeface(mTypeface);
		mFacebookTxt = (TextView) findViewById(R.id.facebook_txt);
		mGoogleTxt = (TextView) findViewById(R.id.google_txt);
		mEmailTxt = (TextView) findViewById(R.id.email_txt);

		Typeface HelveticaNeueMedium = TypefaceSingleton.getTypeface()
				.getHelveticaNeueMedium(this);
		mFacebookTxt.setTypeface(HelveticaNeueMedium);
		mGoogleTxt.setTypeface(HelveticaNeueMedium);
		mEmailTxt.setTypeface(HelveticaNeueMedium);
		mSignLater.setTypeface(HelveticaNeueMedium);
		mTopTExt = (TextView) findViewById(R.id.top_txt);
		Typeface kg = TypefaceSingleton.getTypeface().getKGBlankSpace(this);
		mTopTExt.setTypeface(kg);

	}

	private void googlePlusLogin() {
		google_lay = (LinearLayout) findViewById(R.id.google_lay);

		google_lay.setOnClickListener(this);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(SignInScreen.this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API, Plus.PlusOptions.builder().build())
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		mGoogleApiClient.connect();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.facebook_lay:
			GlobalMethods.storeValuetoPreference(SignInScreen.this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.LOGIN_TYPE,
					AppConstants.LOGIN_TYPE_FB);
			callFBLogin();
			break;
		case R.id.google_lay:
			GlobalMethods.storeValuetoPreference(SignInScreen.this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.LOGIN_TYPE,
					AppConstants.LOGIN_TYPE_GOOGLE);
			signInWithGplus();

			break;
		case R.id.email_lay:
			launchActivity(MailSignInScreen.class);

			break;
		case R.id.sign_later:
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
					true);

			// Userid 0 for non- registered user
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID, AppConstants.DEFAULT_USERID);
			GlobalMethods.storeValuetoPreference(SignInScreen.this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.LOGIN_TYPE,
					AppConstants.LOGIN_TYPE_GUEST);
			launchActivity(HomeScreen.class);
			break;
		default:
			break;
		}

	}

	private void signInWithGplus() {
		// googlePlusLogin();
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;

			resolveSignInError();
		}
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {

		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
		}
		super.onStop();
	}

	private void resolveSignInError() {
		if (mConnectionResult != null) {

			if (mConnectionResult.hasResolution()) {
				try {
					mIntentInProgress = true;
					mConnectionResult
							.startResolutionForResult(this, RC_SIGN_IN);
				} catch (SendIntentException e) {
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		}
	}

	private void updateUI(boolean isSignedIn) {
	}

	/**
	 * Sign-out from google
	 * */
	public static void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
		}
	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				callUserRegistrationService(personName, email, personPhotoUrl,
						AppConstants.LOGIN_TYPE_GOOGLE,
						AppConstants.DEVICE_TYPE,
						GlobalMethods.getDeviceToken(this));

			} else {
				DialogManager.showToast(SignInScreen.this,
						getString(R.string.login_failed));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void callUserRegistrationService(String personName, String email,
			String profilepic, String regtype, String deviceType,
			String deviceToken) {

		DialogManager.showProgress(this);

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.BASE_URL).build();

		APICommonInterface interfaces = restAdapter
				.create(APICommonInterface.class);

		interfaces.getRegistration(email, regtype, personName, deviceType,
				deviceToken, profilepic, new Callback<SignInResponse>() {

					@Override
					public void success(SignInResponse mResponse, Response arg1) {

						DialogManager.hideProgress(SignInScreen.this);
						if (mResponse != null
								&& mResponse.getError_code().equalsIgnoreCase(
										AppConstants.SUCCESS_CODE)) {
							GlobalMethods.storeValuetoPreference(
									SignInScreen.this,
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.LOGIN_TYPE,
									AppConstants.LOGIN_TYPE_EMAIL);

							GlobalMethods.updateUserDetails(SignInScreen.this,
									mResponse.getResult());

							launchActivity(HomeScreen.class);

							DialogManager.showToast(SignInScreen.this,
									getString(R.string.login_success));
						} else {
							DialogManager.showToast(SignInScreen.this,
									getString(R.string.login_failed));
						}

					}

					@Override
					public void failure(RetrofitError error) {
						DialogManager.hideProgress(SignInScreen.this);
						DialogManager.showToast(SignInScreen.this,
								getString(R.string.login_failed));

					}
				});
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		// Get user's information
		getProfileInformation();
		// Update the UI after signin
		updateUI(true);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);

	}

}

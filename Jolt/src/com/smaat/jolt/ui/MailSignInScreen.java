package com.smaat.jolt.ui;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APICommonInterface;
import com.smaat.jolt.model.SignInResponse;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;
import com.smaat.jolt.util.TypefaceSingleton;

public class MailSignInScreen extends BaseActivity implements OnClickListener{
	private TextView mHeaderText, mContinue;
	private Bundle mBundle;
	String Email, User, mEmailId, mUserName;
	private EditText EmailIdValidate, UserNameValidate;
	private RelativeLayout mSlideMenuHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_with_email);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.email_sign_in_lay);
		Typeface mTypeface = TypefaceSingleton.getTypeface().getHelveticaNeue(
				this);
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		initcomponents();

		String action = getIntent().getAction();
		String data = getIntent().getDataString();
		if (Intent.ACTION_VIEW.equals(action) && data != null) {
			String recipeId = data;
			Uri uri = Uri.parse(recipeId);
			String token = uri.getQueryParameter(getString(R.string.token));
			if (token != null) {
				callVerifyEmail(token);
			}
		}
		mBundle = new Bundle();
	}

	void initcomponents() {
		mHeaderText = (TextView) findViewById(R.id.top_txt);
		mContinue = (TextView) findViewById(R.id.continue_txt);
		mSlideMenuHeader = (RelativeLayout) findViewById(R.id.slide_menu_header);
		mSlideMenuHeader.setOnClickListener(this);
		Typeface corbel = TypefaceSingleton.getTypeface().getKGBlankSpace(this);
		mHeaderText.setTypeface(corbel);

		Typeface bold = TypefaceSingleton.getTypeface().getHelveticaNeueBold(
				this);
		mContinue.setTypeface(bold);
		EmailIdValidate = (EditText) findViewById(R.id.mail_id_txt);
		UserNameValidate = (EditText) findViewById(R.id.full_name_txt);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.why_dont_need:
			Intent mWhyNeedMailSignIn = new Intent(MailSignInScreen.this,
					WhyNeedMailSignIn.class);
			mWhyNeedMailSignIn.putExtras(mBundle);
			startActivity(mWhyNeedMailSignIn);

			break;
		case R.id.continue_txt:

			validateFields();

			break;
		case R.id.slide_menu_header:
			launchActivity(SignInScreen.class);
			finish();
			break;
		}

	}

	private void callVerifyEmail(String tokenID) {

		RestAdapter mRestAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.BASE_URL).build();

		APICommonInterface mApiCommonInterface = mRestAdapter
				.create(APICommonInterface.class);

		DialogManager.showProgress(this);

		mApiCommonInterface.verifyEmail(tokenID,
				new Callback<SignInResponse>() {

					@Override
					public void success(SignInResponse mResponse, Response arg1) {
						DialogManager.hideProgress(MailSignInScreen.this);
						if (mResponse != null
								&& mResponse.getError_code().equalsIgnoreCase(
										AppConstants.SUCCESS_CODE)) {
							GlobalMethods.storeValuetoPreference(
									MailSignInScreen.this,
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.LOGIN_TYPE,
									AppConstants.LOGIN_TYPE_EMAIL);

							GlobalMethods.updateUserDetails(
									MailSignInScreen.this,
									mResponse.getResult());

							launchActivity(HomeScreen.class);

						} else {
							DialogManager.showToast(MailSignInScreen.this,
									mResponse.getMsg());
						}
					}

					@Override
					public void failure(RetrofitError error) {
						DialogManager.hideProgress(MailSignInScreen.this);
						DialogManager.showToast(MailSignInScreen.this,
								getString(R.string.verify_email_failed));
					}
				});
	}

	private void validateFields() {
		String Email = EmailIdValidate.getText().toString().trim();
		String User = UserNameValidate.getText().toString().trim();

		if (User.isEmpty()) {
			DialogManager.showToast(this, getString(R.string.enter_fullname));
		}

		else if (Email.isEmpty()) {
			DialogManager.showToast(this, getString(R.string.enter_email_add));
		} else if (!GlobalMethods.isEmailValid(Email)) {

			DialogManager.showToast(this, getString(R.string.enter_email));
		} else {
			callUserRegistrationService(User, Email, "",
					AppConstants.LOGIN_TYPE_EMAIL, AppConstants.DEVICE_TYPE,
					GlobalMethods.getDeviceToken(this));
		}
	}

	private void callUserRegistrationService(String personName, String email,
			String profilepic, String regtype, String deviceType,
			String deviceToken) {
		Email = email;
		User = personName;
		DialogManager.showProgress(this);
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.BASE_URL).build();

		APICommonInterface interfaces = restAdapter
				.create(APICommonInterface.class);

		interfaces.getRegistration(email, regtype, personName, deviceType,
				deviceToken, profilepic, new Callback<SignInResponse>() {

					@Override
					public void success(SignInResponse mResponse, Response arg1) {

						DialogManager.hideProgress(MailSignInScreen.this);
						if (mResponse != null
								&& mResponse.getError_code().equalsIgnoreCase(
										AppConstants.SUCCESS_CODE)) {

							mBundle.putString(AppConstants.EMAIL, String.valueOf(Email));
							mBundle.putString(AppConstants.USER, String.valueOf(User));
							Intent whyneedsignin = new Intent(
									MailSignInScreen.this,
									AfterMailSignIn.class);
							whyneedsignin.putExtras(mBundle);
							startActivity(whyneedsignin);
							// finish();
						} else {
							DialogManager.showToast(MailSignInScreen.this,
									getString(R.string.login_failed));
						}

					}

					@Override
					public void failure(RetrofitError error) {

						DialogManager.hideProgress(MailSignInScreen.this);
						DialogManager.showToast(MailSignInScreen.this,
								getString(R.string.login_failed));

					}
				});
	}

	@Override
	public void onBackPressed() {
		launchActivity(SignInScreen.class);
		finish();

	}

}

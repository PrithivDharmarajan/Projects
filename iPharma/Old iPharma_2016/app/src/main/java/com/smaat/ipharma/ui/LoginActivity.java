package com.smaat.ipharma.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.ForgotResponse;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.DialogMangerSucessCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;
import com.smaat.ipharma.apiinterface.APIRequestHandler;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity implements
		DialogMangerCallback, DialogMangerSucessCallback {

	private EditText mEmail, mPass, mPhone;
	private Button mLogin;
	@SuppressWarnings("unused")
	private TextView mForgotPass, mNewUser, mHeaderText;
	Typeface helvetica_normal, helvetica_bold, helvetica_light, hightower;

	protected String mRegId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(this);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				this);
		hightower = TypefaceSingleton.getInstance().getHighTower(this);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		hideSoftKeyboard(LoginActivity.this);

		initview();
	}

	private void initview() {
		AppConstants.FROMINTLOADMAP = AppConstants.FROMMAP;
		mEmail = (EditText) findViewById(R.id.email);
		mPass = (EditText) findViewById(R.id.password);
		mPhone = (EditText) findViewById(R.id.phone);
		mLogin = (Button) findViewById(R.id.btnLogin);
		mLogin.setTypeface(helvetica_bold);
		mForgotPass = (TextView) findViewById(R.id.forgot_psd);
		mNewUser = (TextView) findViewById(R.id.txtNewUser);
		mHeaderText = (TextView) findViewById(R.id.header_text);
		mHeaderText.setTypeface(hightower);

	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnLogin:
			if (ValidRegister()) {
				mRegId = (String) GlobalMethods.getValueFromPreference(this,
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_deviceReg_Id);
				callLoginActivityAPI();
			}
			break;
		case R.id.forgot_psd:
			showForgotPassDialog();
			break;
		case R.id.txtNewUser:
			GlobalMethods.storeValuetoPreference(getApplication(),
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.REGISTER_SCREEN, AppConstants.LOGIN_SCREEN);
			launchActivity(RegisterScreen.class);
			break;

		default:
			break;
		}
	}

	private void callLoginActivityAPI() {
		DialogManager.showProgress(LoginActivity.this);
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.Base_Url).build();
		APICommonInterface interfaces = restAdapter
				.create(APICommonInterface.class);
		String email = mEmail.getText().toString().trim();
		String phone = mPhone.getText().toString().trim();
		final String password = mPass.getText().toString().trim();
		String device_type = getString(R.string.android);
		String device_id = mRegId;

		interfaces.getLogin(email, phone, password, device_type, device_id,
				new Callback<CommonResponse>() {

					public void failure(RetrofitError arg0) {
						DialogManager.hideProgress(LoginActivity.this);
						DialogManager.showCustomAlertDialog(LoginActivity.this,
								LoginActivity.this,
								getString(R.string.no_network));
					}

					public void success(CommonResponse response, Response obj) {

						DialogManager.hideProgress(LoginActivity.this);
						if (response.getStatus().equalsIgnoreCase(
								AppConstants.FAILURE_CODE)) {
							DialogManager.showCustomAlertDialog(
									LoginActivity.this, LoginActivity.this,
									getString(R.string.login_failed),
									response.getMsg());

						} else if (response.getStatus().equalsIgnoreCase(
								AppConstants.SUCCESS_CODE)) {
							String communication_address = response.getResult()
									.getAddress()
									+ ", "
									+ response.getResult().getArea()
									+ ", "
									+ response.getResult().getCity()
									+ ", "
									+ response.getResult().getPincode();
							GlobalMethods.UpdateUserDetails(LoginActivity.this,
									response.getResult());
							GlobalMethods.storeValuetoPreference(
									LoginActivity.this,
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.communication_address,
									communication_address);
							GlobalMethods.storeValuetoPreference(
									LoginActivity.this,
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.USER_PASSWORD, password);

							if (response
									.getMsg()
									.trim()
									.equalsIgnoreCase(
											getString(R.string.account_not_confirmed))) {

								Intent in = new Intent(LoginActivity.this,
										OneTimePassword.class);
								startActivity(in);
								finish();
							} else {

								Intent in = new Intent(LoginActivity.this,
										HomeScreen.class);
								startActivity(in);
								finish();
								overridePendingTransition(
										R.anim.slide_in_right,
										R.anim.slide_out_left);
							}
						}

					}

				});
	}

	private boolean ValidRegister() {

		if ((mEmail.getText().toString().trim().length() > 0 && !mEmail
				.getText().toString().trim().equalsIgnoreCase(""))
				|| (mPhone.getText().toString().trim().length() > 0 && !mPhone
						.getText().toString().trim().equalsIgnoreCase(""))) {
			if ((mPhone.getText().toString().trim().length() > 0 && !mPhone
					.getText().toString().trim().equalsIgnoreCase(""))) {

			} else if (!GlobalMethods.isEmailValid(mEmail.getText().toString()
					.trim())) {
				DialogManager.showCustomAlertDialog(this, this,
						getString(R.string.login_failed),
						getString(R.string.enter_email));
				return false;
			}
		}

		else {
			DialogManager.showCustomAlertDialog(LoginActivity.this, this,
					getString(R.string.login_failed),
					getString(R.string.enter_email_or_phone));
			return false;
		}
		if (mPass.getText().toString().trim().isEmpty()
				&& mPass.getText().toString().trim().length() < 1
				&& mPass.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(LoginActivity.this, this,
					getString(R.string.login_failed),
					getString(R.string.enter_password));
			return false;
		}

		return true;
	}

	public void showForgotPassDialog() {

		mDialog = new Dialog(this);
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
		header_txt.setTypeface(helvetica_normal);
		Button ok = (Button) mDialog.findViewById(R.id.submit);
		ok.setTypeface(helvetica_bold);
		final EditText Email = (EditText) mDialog.findViewById(R.id.email_edit);
		Email.setTypeface(helvetica_normal);

		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (!GlobalMethods.isEmailValid(Email.getText().toString()
						.trim())) {

					DialogManager.showCustomAlertDialog(LoginActivity.this,
							LoginActivity.this,
							getString(R.string.forgot_pwd_failed),
							getString(R.string.enter_email));
				} else {

					mDialog.dismiss();
					APIRequestHandler.getInstance().getForgotPasasword(
							Email.getText().toString().trim(),
							LoginActivity.this);

				}
			}

		});

		mDialog.show();
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(responseObj);
		if (responseObj instanceof CommonResponse) {
			CommonResponse response = (CommonResponse) responseObj;

			if (response.getStatus()
					.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
				DialogManager.showSuccessDialog(LoginActivity.this,
						LoginActivity.this, getString(R.string.mail_sucess));
			} else if (response.getStatus().equalsIgnoreCase(
					AppConstants.FAILURE_CODE)) {

				DialogManager.showCustomAlertDialog(LoginActivity.this,
						LoginActivity.this,
						getString(R.string.forgot_pwd_failed),
						getString(R.string.user_not_exit) + "\n\t\t"
								+ getString(R.string.enter_email));
			}
		} else if(responseObj instanceof ForgotResponse){

			ForgotResponse response = (ForgotResponse) responseObj;
			if(response.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE)){
				DialogManager.showSuccessDialog(LoginActivity.this,
						LoginActivity.this, response.getMsg());
			} else {
				DialogManager.showSuccessDialog(LoginActivity.this,
						LoginActivity.this, "Email id not registered with iPharma");
			}

		}
	}

	@Override
	public void onRequestFailure(RetrofitError errorCode) {
		// TODO Auto-generated method stub
		super.onRequestFailure(errorCode);

	}

	public void callLoginService() {

	}

	public void onItemclick(String SelctedItem, int pos) {

	}

	public void onOkclick() {

	}


}

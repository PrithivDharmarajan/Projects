package com.smaat.renterblock.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
//import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.RegisterResponse;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GPSTracker;
import com.smaat.renterblock.util.GlobalMethods;

public class RegisterScreen extends BaseActivity implements DialogMangerCallback {
	private EditText mFirstName, mLastName, mEmail, mPass, mZip, mPhone, mAddress1, mAddress2, mCityState,
			mBusinessName, mUserName;
	public String email = "", name = "", fromGoogle = "", mName = "", from = "", fbID = "", googleID = "",
			firstname = "", lastname = "", FBEmail = "", mOptional = "";

	private LinearLayout mProfileLay, mHomeLay, mOptionLay, mAddressLay, mCancelLay, mCancelLay_Buy_Rent;
	private Button mEnhancedRadio, mStandardRadio, mHomeRadio, mBrokerRadio;
	private TextView mAgree, mAgree_Buy_Rent;
	private String buy, sell, rent, agent, broker;
	private String selectedType;
	private String type, loginType = "email";
	private String homeoption = "1", brokeroption = "0", enhancedoption = "1", standardoption = "0", accoption = "0";
	private String DeviceID, Device;

	private int from_reg = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.register_screen);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		setupUI(root);
		hideSoftKeyboard(this);
		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);
		Bundle extra = getIntent().getExtras();

		if (extra != null) {

			email = extra.getString("email");
			name = extra.getString("name");
			mName = extra.getString("mName");
			from = extra.getString("from");
			buy = extra.getString("buy");
			sell = extra.getString("sell");
			rent = extra.getString("rent");
			agent = extra.getString("agent");
			broker = extra.getString("broker");
			fbID = extra.getString("FBID");
			googleID = extra.getString("googleID");
			firstname = extra.getString("FirstName");
			lastname = extra.getString("LastName");
			FBEmail = extra.getString("FBEmail");
			loginType = extra.getString("loginType");
		}

		initComponents();
		setGoogleAnalytics(RegisterScreen.this);
		/* Push Notification */

		String deviceId = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_deviceReg_Id);

		boolean isRegistered = (Boolean) GlobalMethods.getValueFromPreference(this, GlobalMethods.BOOLEAN_PREFERENCE,
				AppConstants.pref_device_reg_status);

		boolean isDeveiceIdChanged = (Boolean) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.pref_device_id_changed);
		if (!isRegistered || isDeveiceIdChanged) {
			if (deviceId != null && !deviceId.equals("")) {
				if (deviceId.equalsIgnoreCase("0")) {
					//GCMRegistrar.register(this, AppConstants.SENDER_ID);
				}
			} else {
				//GCMRegistrar.register(this, AppConstants.SENDER_ID);
			}
		}
		DeviceID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_deviceReg_Id);
		Device = "Android";

	}

	public void initComponents() {

		mBusinessName = (EditText) findViewById(R.id.business_name);
		mFirstName = (EditText) findViewById(R.id.first_name);
		mUserName = (EditText) findViewById(R.id.user_name);
		mLastName = (EditText) findViewById(R.id.last_name);
		mEmail = (EditText) findViewById(R.id.email);
		mPass = (EditText) findViewById(R.id.pasword);
		mZip = (EditText) findViewById(R.id.zip);
		mPhone = (EditText) findViewById(R.id.phone_num);

		mAddress1 = (EditText) findViewById(R.id.address_one);
		mAddress2 = (EditText) findViewById(R.id.address_two);
		mCityState = (EditText) findViewById(R.id.city_state_zip);

		mOptionLay = (LinearLayout) findViewById(R.id.option_layout);
		mProfileLay = (LinearLayout) findViewById(R.id.profile_option);
		mHomeLay = (LinearLayout) findViewById(R.id.home_option);
		mAddressLay = (LinearLayout) findViewById(R.id.address_layout);
		mEnhancedRadio = (Button) findViewById(R.id.enhanced_radio);
		mStandardRadio = (Button) findViewById(R.id.standard_radio);
		mHomeRadio = (Button) findViewById(R.id.home_radio);
		mBrokerRadio = (Button) findViewById(R.id.broker_radio);

		mCancelLay = (LinearLayout) findViewById(R.id.cancel_layout);
		mCancelLay_Buy_Rent = (LinearLayout) findViewById(R.id.cancel_layout_buy_rent);
		mAgree = (TextView) findViewById(R.id.agree_terms);
		mAgree.setMovementMethod(LinkMovementMethod.getInstance());
		mAgree_Buy_Rent = (TextView) findViewById(R.id.agree_terms_buy_rent);

		mAgree.setText(handleSpannableTextString());
		mAgree_Buy_Rent.setText(handleSpannableTextString());

		if (from != null && from.equalsIgnoreCase("fromGoogle")) {
			mPass.setVisibility(View.GONE);
			mFirstName.setText(name);
			mLastName.setText(name);
			mEmail.setText(email);

			if (name.equals("")) {
				mFirstName.setEnabled(true);
				mLastName.setEnabled(true);
			} else {
				mFirstName.setEnabled(false);
				mLastName.setEnabled(false);
			}
			if (email.equals("")) {
				mEmail.setEnabled(true);
			} else {
				mEmail.setEnabled(false);
			}

		}
		if (from != null && from.equalsIgnoreCase("fromFB")) {
			mPass.setVisibility(View.GONE);
			mFirstName.setText(firstname);
			mLastName.setText(lastname);
			mEmail.setText(FBEmail);

			if (firstname.equals("")) {
				mFirstName.setEnabled(true);
			} else {
				mFirstName.setEnabled(false);
			}
			if (lastname.equals("")) {
				mLastName.setEnabled(true);
			} else {
				mLastName.setEnabled(false);
			}
			if (FBEmail.equals("")) {
				mEmail.setEnabled(true);
			} else {
				mEmail.setEnabled(false);
			}

		}
		if (loginType.equalsIgnoreCase("facebook")) {
			loginType = "facebook";
		}
		if (loginType.equalsIgnoreCase("google")) {
			loginType = "google";
		}
		if (loginType.equalsIgnoreCase("email")) {

			loginType = "email";
		}
		mPhone.setHint(R.string.phone_text);

		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)) {
			type = "buyer";
			mBusinessName.setVisibility(View.GONE);
			mOptionLay.setVisibility(View.GONE);
			mAddressLay.setVisibility(View.GONE);
			mPhone.setHint(R.string.phone_optional);

			mCancelLay.setVisibility(View.GONE);
			mCancelLay_Buy_Rent.setVisibility(View.VISIBLE);
			mAgree.setVisibility(View.GONE);
			mAgree_Buy_Rent.setVisibility(View.VISIBLE);

		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			type = "seller";
			mBusinessName.setVisibility(View.VISIBLE);
			mAddressLay.setVisibility(View.VISIBLE);
			mBusinessName.setHint("Business Name(optional)");
			mOptionLay.setVisibility(View.VISIBLE);
			mHomeLay.setVisibility(View.VISIBLE);
			mZip.setVisibility(View.GONE);
			mProfileLay.setVisibility(View.VISIBLE);

			mCancelLay.setVisibility(View.VISIBLE);
			mCancelLay_Buy_Rent.setVisibility(View.GONE);
			mAgree.setVisibility(View.VISIBLE);
			mAgree_Buy_Rent.setVisibility(View.GONE);

		} else if (selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			type = "renter";
			mBusinessName.setVisibility(View.GONE);
			mOptionLay.setVisibility(View.GONE);
			mAddressLay.setVisibility(View.GONE);
			mPhone.setHint(R.string.phone_optional);

			mCancelLay.setVisibility(View.GONE);
			mCancelLay_Buy_Rent.setVisibility(View.VISIBLE);
			mAgree.setVisibility(View.GONE);
			mAgree_Buy_Rent.setVisibility(View.VISIBLE);

		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)) {
			type = "agent";
			mBusinessName.setVisibility(View.VISIBLE);
			mAddressLay.setVisibility(View.VISIBLE);
			mOptionLay.setVisibility(View.VISIBLE);
			mProfileLay.setVisibility(View.VISIBLE);
			mZip.setVisibility(View.GONE);
			mHomeLay.setVisibility(View.GONE);

			mCancelLay.setVisibility(View.VISIBLE);
			mCancelLay_Buy_Rent.setVisibility(View.GONE);
			mAgree.setVisibility(View.VISIBLE);
			mAgree_Buy_Rent.setVisibility(View.GONE);
		} else {
			type = "broker";
			mBusinessName.setVisibility(View.VISIBLE);
			mAddressLay.setVisibility(View.VISIBLE);
			mOptionLay.setVisibility(View.VISIBLE);
			mProfileLay.setVisibility(View.VISIBLE);
			mZip.setVisibility(View.GONE);
			mHomeLay.setVisibility(View.GONE);

			mCancelLay.setVisibility(View.VISIBLE);
			mCancelLay_Buy_Rent.setVisibility(View.GONE);
			mAgree.setVisibility(View.VISIBLE);
			mAgree_Buy_Rent.setVisibility(View.GONE);
		}
	}

	private void callRegisterService() {
		double latitude = 0.0;
		double longitude = 0.0;
		GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation() != false) {
			latitude = tracker.getLatitude();
			longitude = tracker.getLongitude();
		}

		String url = AppConstants.BASE_URL + "registration";

		String phone_number = mPhone.getText().toString().trim().replace(" ", "").replace("-", "").replace("+", "")
				.replace("(", "").replace(")", "");

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("business_name", mBusinessName.getText().toString().trim());
		params.put("first_name", mFirstName.getText().toString().trim());
		params.put("user_name", mUserName.getText().toString().trim());
		params.put("last_name", mLastName.getText().toString().trim());
		params.put("email_id", mEmail.getText().toString().trim());
		params.put("password", mPass.getText().toString().trim());
		params.put("zip", mZip.getText().toString().trim());
		params.put("phone", phone_number);
		params.put("address1", mAddress1.getText().toString().trim());
		params.put("address2", mAddress2.getText().toString().trim());
		params.put("city", mCityState.getText().toString().trim());
		params.put("state", " ");
		params.put("account_manager", accoption);
		params.put("home_owner", homeoption);
		params.put("broker", brokeroption);
		params.put("enhanced_profile", enhancedoption);
		params.put("standard_profile", standardoption);
		params.put("login_type", loginType);
		params.put("google_id", googleID);
		params.put("facebook_id", fbID);
		params.put("type", type);
		params.put("device_id", DeviceID);
		params.put("device", Device);
		params.put("latitude", latitude);
		params.put("longitude", longitude);
		GsonTransformer t = new GsonTransformer();
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							RegisterResponse obj = new Gson().fromJson(json.toString(), RegisterResponse.class);
							onSuccessRequest(obj);
						} else {
							statusErrorCode(status);
						}

					}
				});

	}

	private void onSuccessRequest(RegisterResponse responseObj) {
		super.onRequestSuccess(responseObj);
		RegisterResponse register = (RegisterResponse) responseObj;
		if (!loginType.equalsIgnoreCase("email")) {
			if (register.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

				GlobalMethods.storeValuetoPreference(RegisterScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_Login, "true");

				GlobalMethods.storeValuetoPreference(RegisterScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_userId, register.result.getUser_id());
				UserID = GlobalMethods.getUserID(this);
				GlobalMethods.storeValuetoPreference(RegisterScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_user_name, register.result.getUser_name());
				GlobalMethods.storeValuetoPreference(RegisterScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_type, register.result.getUser_type());
				GlobalMethods.storeValuetoPreference(RegisterScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.ENHANCED_PROFILE, register.result.getEnhanced_profile());

				sendDeviceID(UserID, DeviceID, Device);
				launchActivity(MapFragmentActivity.class);

			} else {
				DialogManager.showCustomAlertDialog(this, this, register.msg);
			}
		} else {
			if (register.error_code.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
				from_reg = 1;
				DialogManager.showCustomAlertDialog(this, this,
						"Registered successfully, Verification mail sent to your mail id.");
			} else {
				DialogManager.showCustomAlertDialog(this, this, register.msg);
			}
		}
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
				// Intent tc = new Intent(RegisterScreen.this,
				// TCFragment.class);
				// tc.putExtra("option", "1");
				// overridePendingTransition(R.anim.slide_in_right,
				// R.anim.slide_out_left);
				// startActivity(tc);
				showTermsConditionsDialog(getString(R.string.terms_of_use), "content/tc");
			}

		};

		ClickableSpan clickableSpan1 = new ClickableSpan() {

			@Override
			public void onClick(View arg0) {
				// Intent tc = new Intent(RegisterScreen.this,
				// TCFragment.class);
				// tc.putExtra("option", "2");
				// overridePendingTransition(R.anim.slide_in_right,
				// R.anim.slide_out_left);
				// startActivity(tc);
				showTermsConditionsDialog(getString(R.string.privacy_policy), "content/pp");

			}

		};
		int startLength = activityText.length();

		spannableString.setSpan(clickableSpan, (startLength - 31), startLength - 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007afc")), (startLength - 31),
				startLength - 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(clickableSpan1, (startLength - 14), startLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007afc")), (startLength - 14), startLength,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spannableString;
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.cancel:
			finish();
			break;
		case R.id.register:
			if (ValidateRegistration()) {
				callRegisterService();
			}
			break;

		case R.id.enhanced_radio_lay:
			enhancedoption = "1";
			standardoption = "0";
			mEnhancedRadio.setBackgroundResource(R.drawable.check_enable);
			mStandardRadio.setBackgroundResource(R.drawable.check_disable);

			break;
		case R.id.standard_radio_lay:
			enhancedoption = "0";
			standardoption = "1";
			mStandardRadio.setBackgroundResource(R.drawable.check_enable);
			mEnhancedRadio.setBackgroundResource(R.drawable.check_disable);

			break;
		case R.id.home_radio_lay:
			homeoption = "1";
			brokeroption = "0";
			mHomeRadio.setBackgroundResource(R.drawable.check_enable);
			mBrokerRadio.setBackgroundResource(R.drawable.check_disable);
			mBusinessName.setHint("Business Name(optional)");
			mOptional = "Optional";
			break;
		case R.id.broker_radio_lay:
			homeoption = "0";
			brokeroption = "1";
			mBrokerRadio.setBackgroundResource(R.drawable.check_enable);
			mHomeRadio.setBackgroundResource(R.drawable.check_disable);
			mBusinessName.setHint("Business Name");
			mOptional = "NotOptional";
			break;
		}

	}

	public void sendIntent() {
		if (buy != null && buy.equalsIgnoreCase("buy")) {
			Intent buy = new Intent(RegisterScreen.this, CreateAccountScreen.class);
			buy.putExtra("buy", "buy");
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(buy);
		}
		if (sell != null && sell.equalsIgnoreCase("sell")) {
			Intent sell = new Intent(RegisterScreen.this, CreateAccountScreen.class);
			sell.putExtra("sell", "sell");
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(sell);
		}
		if (rent != null && rent.equalsIgnoreCase("rent")) {
			Intent rent = new Intent(RegisterScreen.this, CreateAccountScreen.class);
			rent.putExtra("rent", "rent");
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(rent);
		}
		if (agent != null && agent.equalsIgnoreCase("agent")) {
			Intent agent = new Intent(RegisterScreen.this, CreateAccountScreen.class);
			agent.putExtra("agent", "agent");
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(agent);
		}
		if (broker != null && broker.equalsIgnoreCase("broker")) {
			Intent broker = new Intent(RegisterScreen.this, CreateAccountScreen.class);
			broker.putExtra("broker", "broker");
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			startActivity(broker);
		}

	}

	private boolean ValidateRegistration() {
		if (selectedType.equalsIgnoreCase(AppConstants.RENTER) || selectedType.equalsIgnoreCase(AppConstants.BUYER)) {

			if (mFirstName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_first_name));
				return false;
			} else if (mLastName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_last_name));
				return false;
			} else if (mUserName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_user_name));
				return false;
			} else if (mEmail.getText().toString().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_email));
				return false;
			} else if (!GlobalMethods.isEmailValid(mEmail.getText().toString())) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_valid_email));
				return false;
			} else if (loginType.equals("email") && mPass.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_password));
				return false;
			} else if (mZip.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_zipcode));
				return false;
			}
		}
		if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {

			if (mOptional.equals("NotOptional") && mBusinessName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_business_name));
				return false;
			} else if (mFirstName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_first_name));
				return false;
			} else if (mLastName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_last_name));
				return false;
			} else if (mEmail.getText().toString().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_email));
				return false;
			} else if (!GlobalMethods.isEmailValid(mEmail.getText().toString())) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_valid_email));
				return false;
			} else if (loginType.equals("email") && mPass.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_password));
				return false;
			} else if (mPhone.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_phone_number));
				return false;
			} else if (mAddress1.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_address_line1));
				return false;
			}
			// else if (mAddress2.getText().toString().trim().isEmpty()) {
			// DialogManager.showCustomAlertDialog(this, this,
			// getString(R.string.please_enter_address_line2));
			// return false;
			// }
			else if (mCityState.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_zipcode));
				return false;
			}
		}
		if (selectedType.equalsIgnoreCase(AppConstants.AGENT)) {
			if (mBusinessName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_business_name));
				return false;
			} else if (mFirstName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_first_name));
				return false;
			} else if (mLastName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_last_name));
				return false;
			} else if (mEmail.getText().toString().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_email));
				return false;
			} else if (!GlobalMethods.isEmailValid(mEmail.getText().toString())) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_valid_email));
				return false;
			} else if (loginType.equals("email") && mPass.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_password));
				return false;
			} else if (mPhone.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_phone_number));
				return false;
			} else if (mAddress1.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_address_line1));
				return false;
			}
			// else if (mAddress2.getText().toString().trim().isEmpty()) {
			// DialogManager.showCustomAlertDialog(this, this,
			// getString(R.string.please_enter_address_line2));
			// return false;
			// }
			else if (mCityState.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_zipcode));
				return false;
			}
		}
		if (selectedType.equalsIgnoreCase(AppConstants.BROKER)) {

			if (mBusinessName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_business_name));
				return false;
			} else if (mFirstName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_first_name));
				return false;
			} else if (mLastName.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_last_name));
				return false;
			} else if (mEmail.getText().toString().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_email));
				return false;
			} else if (!GlobalMethods.isEmailValid(mEmail.getText().toString())) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_valid_email));
				return false;
			} else if (loginType.equals("email") && mPass.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_password));
				return false;
			} else if (mPhone.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_phone_number));
				return false;
			} else if (mAddress1.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_address_line1));
				return false;
			}
			// else if (mAddress2.getText().toString().trim().isEmpty()) {
			// DialogManager.showCustomAlertDialog(this, this,
			// getString(R.string.please_enter_address_line2));
			// return false;
			// }
			else if (mCityState.getText().toString().trim().isEmpty()) {
				DialogManager.showCustomAlertDialog(this, this, getString(R.string.please_enter_zipcode));
				return false;
			}
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		launchActivity(CreateAccountScreen.class);
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		if (from_reg == 1) {
			from_reg = 0;
			launchActivity(LoginActivity.class);
		}
	}

	public void showTermsConditionsDialog(String headertext, String url) {

		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.activity_settings_common);

		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		mDialog.setCancelable(true);
		LinearLayout mBack = (LinearLayout) mDialog.findViewById(R.id.back_icon);
		TextView mContentText = (TextView) mDialog.findViewById(R.id.content_txt);
		mContentText.setMovementMethod(new ScrollingMovementMethod());
		TextView mHeaderTxt = (TextView) mDialog.findViewById(R.id.header_txt);
		mHeaderTxt.setText(headertext);

		getDetails(url, mContentText);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void getDetails(String url, final TextView mContentText) {

		String URL = AppConstants.BASE_URL + url;

		GsonTransformer t = new GsonTransformer();
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(URL, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mContentText.setText(mResponse.getResult().toString());
							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

}

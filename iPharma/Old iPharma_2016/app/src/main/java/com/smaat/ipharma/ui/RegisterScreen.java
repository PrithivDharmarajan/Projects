package com.smaat.ipharma.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.entity.CommonResponse;
import com.smaat.ipharma.entity.GoogleApiEntity;
import com.smaat.ipharma.entity.Register;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterScreen extends BaseActivity implements
		DialogMangerCallback {
	@SuppressWarnings("unused")
	private EditText mName, mEmail, mPhone, mPass, mLocation, mCity, mArea,
			mPincode, mAddress;
	private Button mLocationCheck, mRegister_btn;
	boolean isCheck;
	Register mRegister = new Register();
	@SuppressWarnings("unused")
	private Context context;
	String strDate = "";
	private TextView header_text;
	Typeface helvetica_normal, helvetica_bold, helvetica_light, hightower;
	private boolean is_check = false;
	private double latitude, longitude;

	String lat_txt = "";
	String long_txt = "";
	String email_id, pass_txt, username, phone_text, address_txt, city_txt,
			area_txt, pin_code;


	protected Object mRegId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(this);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				this);
		hightower = TypefaceSingleton.getInstance().getHighTower(this);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		hideSoftKeyboard(RegisterScreen.this);
		Window window = this.getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		context = RegisterScreen.this;

		mRegId = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_deviceReg_Id);

		initview();
	}

	private void initview() {
		header_text = (TextView) findViewById(R.id.header_text);
		header_text.setTypeface(hightower);
		mRegister_btn = (Button) findViewById(R.id.register);
		if (((String) GlobalMethods.getValueFromPreference(getApplication(),
				GlobalMethods.STRING_PREFERENCE, AppConstants.REGISTER_SCREEN))
				.equalsIgnoreCase(AppConstants.LOGIN_SCREEN)) {
			mRegister_btn.setText(R.string.register);
		}

		mRegister_btn.setTypeface(helvetica_bold);
		mName = (EditText) findViewById(R.id.name);
		mEmail = (EditText) findViewById(R.id.email);
		mPhone = (EditText) findViewById(R.id.phone);
		mPass = (EditText) findViewById(R.id.pass);
		mLocation = (EditText) findViewById(R.id.current_location);
		mCity = (EditText) findViewById(R.id.city);
		mArea = (EditText) findViewById(R.id.area);
		mPincode = (EditText) findViewById(R.id.pincoe);
		mAddress = (EditText) findViewById(R.id.address);
		mLocationCheck = (Button) findViewById(R.id.location_check);

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
		strDate = sdf.format(c.getTime());

	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back_layout:
			launchActivityBackAnim(LoginActivity.class);
			break;
		case R.id.register:
			if (((String) GlobalMethods.getValueFromPreference(
					getApplication(), GlobalMethods.STRING_PREFERENCE,
					AppConstants.REGISTER_SCREEN))
					.equalsIgnoreCase(AppConstants.LOGIN_SCREEN))

			{
				if (ValidRegister()) {
					callRegisterService();
				}
			}

			break;
		case R.id.area_layout:
			// Shows dialog for city

			break;
		case R.id.city_layout:
			// Shows dialog for area
			break;
		case R.id.location_check:
			if (!is_check) {
				mLocationCheck
						.setBackgroundResource(R.drawable.register_till_fill);
				showCurrentLocation();
				is_check = true;
			} else {
				mCity.setText("");
				mArea.setText("");
				mPincode.setText("");
				mAddress.setText("");
				mLocationCheck
						.setBackgroundResource(R.drawable.regsiter_tick_empty);
				is_check = false;
			}
			break;
		default:
			break;
		}
	}

	private void showCurrentLocation() {
		GPSTracker tracker = new GPSTracker(this);
		if (tracker.canGetLocation() == false) {
			tracker.showSettingsAlert();
		} else {
			latitude = tracker.getLatitude();
			longitude = tracker.getLongitude();
			callGoogleApiService(latitude, longitude);
		}
	}

	private void callGoogleApiService(double latitude, double longitude) {

		String url = AppConstants.LATLNG_LINK + latitude + "," + longitude
				+ "&sensor=true";
		aq().ajax(url, JSONObject.class, this,
				getString(R.string.addresslocation));
	}

	public void addresslocation(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				GoogleApiEntity obj = new Gson().fromJson(json.toString(),
						GoogleApiEntity.class);
				onRequest(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onRequest(GoogleApiEntity response) {
		try {
			if (response != null) {
				if (response.getResults().size() > 0) {
					String[] addresses = response.getResults().get(0)
							.getFormatted_address().toString().split(",");
					if (addresses.length >= 4) {
						String add_txt = response
								.getResults()
								.get(0)
								.getFormatted_address()
								.replace(
										(addresses[(addresses.length - 3)] + ","),
										"")
								.replace(
										(addresses[(addresses.length - 2)] + ","),
										"")
								.replace(addresses[(addresses.length - 1)], "");
						String city = addresses[(addresses.length - 3)].trim();
						String pincode = addresses[(addresses.length - 2)]
								.replaceAll("\\D", "");
						mArea.setText(addresses[(addresses.length - 4)].trim());
						mAddress.setText(add_txt.trim());
						mCity.setText(city.trim());
						mPincode.setText(pincode.trim());
					}
				} else {
					DialogManager.showCustomAlertDialog(RegisterScreen.this,
							RegisterScreen.this,
							getString(R.string.unable_to_find_location));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		launchActivityBackAnim(LoginActivity.class);
	}

	private boolean ValidRegister() {
		if (mName.getText().toString().trim().isEmpty()
				&& mName.getText().toString().trim().length() < 1
				&& mName.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_name));
			return false;
		} else if (!GlobalMethods.isEmailValid(mEmail.getText().toString()
				.trim())) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_email));
			return false;
		} else if (mPhone.getText().toString().trim().isEmpty()
				&& mPhone.getText().toString().trim().length() < 1
				&& mPhone.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_phone));
			return false;
		}

		else if (mPass.getText().toString().trim().isEmpty()
				&& mPass.getText().toString().trim().length() < 1
				&& mPass.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_password));
			return false;
		}

		else if (mCity.getText().toString().trim().isEmpty()
				&& mCity.getText().toString().trim().length() < 1
				&& mCity.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_city));
			return false;
		} else if (mArea.getText().toString().trim().isEmpty()
				&& mArea.getText().toString().trim().length() < 1
				&& mArea.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_area));
			return false;
		} else if (mPincode.getText().toString().trim().isEmpty()
				&& mPincode.getText().toString().trim().length() < 1
				&& mPincode.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_pincode));
			return false;
		} else if (mAddress.getText().toString().trim().isEmpty()
				&& mAddress.getText().toString().trim().length() < 1
				&& mAddress.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(this, this,
					getString(R.string.registration_failed),
					getString(R.string.enter_address));
			return false;
		}

		return true;
	}

	private void callRegisterService() {

		lat_txt = "";
		long_txt = "";

		email_id = mEmail.getText().toString().trim();
		pass_txt = mPass.getText().toString().trim();
		username = mName.getText().toString().trim();
		phone_text = mPhone.getText().toString().trim();
		address_txt = mAddress.getText().toString().trim();
		city_txt = mCity.getText().toString().trim();
		area_txt = mArea.getText().toString().trim();
		pin_code = mPincode.getText().toString().trim();

		String location_address = address_txt + "," + area_txt + "," + pin_code;
		if (is_check) {
			lat_txt = latitude + "";
			long_txt = longitude + "";
			callRegistrationAPI();
		} else if (!is_check) {
			generateLatLong(location_address);
		}

	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(responseObj);
		CommonResponse resultObj = (CommonResponse) responseObj;

		DialogManager.showCustomAlertDialog(this, this, resultObj.getMsg());
	}

	private void generateLatLong(String address) {
		String add_ress = address.replace(" ", "%20");
		String url = AppConstants.GOOGLE_MAP_LINK + add_ress + "&sensor=true";
		aq().ajax(url, JSONObject.class, this, getString(R.string.latlocation));
	}

	public void latlocation(String url, JSONObject json, AjaxStatus status) {
		if (json != null) {
			try {
				GoogleApiEntity obj = new Gson().fromJson(json.toString(),
						GoogleApiEntity.class);
				String latitude = obj.getResults().get(0).getGeometry()
						.getLocation().getLat();
				String longitude = obj.getResults().get(0).getGeometry()
						.getLocation().getLat();
				this.latitude = Double.valueOf(latitude);
				this.longitude = Double.valueOf(longitude);
				callRegistrationAPI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void callRegistrationAPI() {

		DialogManager.showProgress(RegisterScreen.this);
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.Base_Url).build();
		APICommonInterface interfaces = restAdapter
				.create(APICommonInterface.class);

		String device_type = getString(R.string.android);
		String device_id = mRegId + "";

		interfaces.getRegistration(username, email_id, phone_text, pass_txt,
				address_txt, city_txt, area_txt, pin_code, lat_txt, long_txt,
				device_type, device_id, new Callback<CommonResponse>() {

					public void failure(RetrofitError arg0) {

						DialogManager.hideProgress(RegisterScreen.this);
						DialogManager.showCustomAlertDialog(
								RegisterScreen.this, RegisterScreen.this,
								getString(R.string.no_network));
					}

					public void success(CommonResponse response, Response obj) {

						DialogManager.hideProgress(RegisterScreen.this);
						if (response.getStatus().equalsIgnoreCase(
								AppConstants.FAILURE_CODE)) {
							DialogManager.showCustomAlertDialog(
									RegisterScreen.this, RegisterScreen.this,
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
							GlobalMethods.UpdateUserDetails(
									RegisterScreen.this, response.getResult());
							GlobalMethods.storeValuetoPreference(
									RegisterScreen.this,
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.communication_address,
									communication_address);
							GlobalMethods.storeValuetoPreference(
									RegisterScreen.this,
									GlobalMethods.STRING_PREFERENCE,
									AppConstants.USER_PASSWORD, pass_txt);
							Intent in = new Intent(RegisterScreen.this,
									OneTimePassword.class);
							startActivity(in);
							finish();
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
						}

					}

				});
	}

	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	public void onOkclick() {
		// TODO Auto-generated method stub

	}





}

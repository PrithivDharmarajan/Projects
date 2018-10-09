package com.smaat.ipharma.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import retrofit.RetrofitError;

public class OneTimePassword extends BaseActivity implements
		DialogMangerCallback {

	Typeface helvetica_normal, helvetica_bold, helvetica_light, hightower;
	protected Object mRegId;
	private TextView header_txt;
	private Button mResendotp_btn, mSubmitotp;
	private String Userid;
	private EditText mOTP;
	private boolean from_resend = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otp_screen);
		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(this);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				this);

		hightower = TypefaceSingleton.getInstance().getHighTower(this);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		Userid = GlobalMethods.getUserID(this);
		initview();
	}

	public void onRequestSuccess(Object responseObj) {
		if (responseObj instanceof OtpEntity) {
			OtpEntity mOtpResponse = (OtpEntity) responseObj;
			if (mOtpResponse.getResult().equalsIgnoreCase(
					getString(R.string.Success))) {
				if (!from_resend) {
					Intent in = new Intent(OneTimePassword.this,
							TermsandConditions.class);
					startActivity(in);
					finish();
				}
			} else {
				DialogManager.showCustomAlertDialog(this, OneTimePassword.this,
						mOtpResponse.getResult());
			}
		}
		super.onRequestSuccess(responseObj);
	}

	public void onRequestFailure(RetrofitError errorCode) {
		super.onRequestFailure(errorCode);
	}

	private void initview() {
		header_txt = (TextView) findViewById(R.id.header_text);
		header_txt.setTypeface(helvetica_bold);
		mResendotp_btn = (Button) findViewById(R.id.resend_otp);
		mResendotp_btn.setTypeface(helvetica_bold);
		mOTP = (EditText) findViewById(R.id.otp_text);
		mSubmitotp = (Button) findViewById(R.id.submit_otp);
		mSubmitotp.setTypeface(helvetica_bold);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.submit_otp:
			from_resend = false;
			String otp_txt = mOTP.getText().toString();
			if (otp_txt.equalsIgnoreCase("")) {
				DialogManager.showCustomAlertDialog(this, OneTimePassword.this,
						getString(R.string.enter_OTP));
			} else {
				APIRequestHandler.getInstance().getOTPconfirmtion(Userid,
						otp_txt, this);
			}
			break;
		case R.id.resend_otp:
			from_resend = true;
			APIRequestHandler.getInstance().getResendOTPconfirmtion(Userid,
					this);

			break;
		case R.id.back_layout:
			finish();
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		GlobalMethods.storeValuetoPreference(OneTimePassword.this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
				AppConstants.FAILURE_CODE);
		super.onBackPressed();
	}

	public void onItemclick(String SelctedItem, int pos) {

	}

	public void onOkclick() {

	}

}

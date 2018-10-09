package com.smaat.renterblock.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class CreateAccountScreen extends BaseActivity implements
		OnClickListener {
	private TextView mBuy, mSell, mRent, mAgent, mBroker;
	private Button mSkip;
	public String email = "", name = "", from = "", googleID = "",
			firstname = "", lastname = "", fbid = "", fbemail = "",
			loginType = "", fbname = "", fromFB = "";
	private String selectedType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_acc_screen);

		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);

		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			email = extra.getString("email");
			name = extra.getString("name");
			from = extra.getString("from");
			googleID = extra.getString("googleID");
			fbemail = extra.getString("FBEmail");
			firstname = extra.getString("FirstName");
			lastname = extra.getString("LastName");
			fbname = extra.getString("mName");
			fbid = extra.getString("FBID");
			loginType = extra.getString("loginType");

		}

		initComponents();
		setGoogleAnalytics(CreateAccountScreen.this);
	}

	public void initComponents() {
		Typeface helvetica_bold= TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		mBuy = (TextView) findViewById(R.id.buy);
		mSell = (TextView) findViewById(R.id.sell);
		mRent = (TextView) findViewById(R.id.rent);
		mAgent = (TextView) findViewById(R.id.agent);
		mBroker = (TextView) findViewById(R.id.broker);
		mSkip = (Button) findViewById(R.id.skip);
		mSkip.setTypeface(helvetica_bold);

		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)) {
			mBuy.setBackgroundResource(R.drawable.enable_bg);
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSell.setBackgroundResource(R.drawable.enable_bg);
		} else if (selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mRent.setBackgroundResource(R.drawable.enable_bg);
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)) {
			mAgent.setBackgroundResource(R.drawable.enable_bg);
		} else if (selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mBroker.setBackgroundResource(R.drawable.enable_bg);
		} else {
			mBuy.setBackgroundResource(R.drawable.enable_bg);
		}
		setClickListners();
	}

	public void setClickListners() {
		mBuy.setOnClickListener(this);
		mRent.setOnClickListener(this);
		mSell.setOnClickListener(this);
		mAgent.setOnClickListener(this);
		mBroker.setOnClickListener(this);
		mSkip.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.buy:
			Intent buy = null;
			mBuy.setBackgroundResource(R.drawable.enable_bg);
			mSell.setBackgroundResource(R.drawable.disable_bg);
			mRent.setBackgroundResource(R.drawable.disable_bg);
			mAgent.setBackgroundResource(R.drawable.disable_bg);
			mBroker.setBackgroundResource(R.drawable.disable_bg);
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type,
					AppConstants.BUYER);
			if (AppConstants.Login_From_Map.equals("false")) {
				buy = new Intent(CreateAccountScreen.this, LoginActivity.class);
				startActivity(buy);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				if (loginType.equals("facebook")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("mName", fbname);
					inte.putExtra("from", "fromFB");
					inte.putExtra("FBID", fbid);
					inte.putExtra("FirstName", firstname);
					inte.putExtra("LastName", lastname);
					inte.putExtra("FBEmail", fbemail);
					inte.putExtra("loginType", "facebook");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (loginType.equals("google")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("name", name);
					inte.putExtra("email", email);
					inte.putExtra("from", "fromGoogle");
					inte.putExtra("googleID", googleID);
					inte.putExtra("loginType", "google");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					buy = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					buy.putExtra("loginType", "email");
					startActivity(buy);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}
			break;
		case R.id.sell:
			Intent sell = null;
			mSell.setBackgroundResource(R.drawable.enable_bg);
			mBuy.setBackgroundResource(R.drawable.disable_bg);
			mRent.setBackgroundResource(R.drawable.disable_bg);
			mAgent.setBackgroundResource(R.drawable.disable_bg);
			mBroker.setBackgroundResource(R.drawable.disable_bg);
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type,
					AppConstants.SELLER);
			if (AppConstants.Login_From_Map.equals("false")) {
				sell = new Intent(CreateAccountScreen.this, LoginActivity.class);
				startActivity(sell);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				if (loginType.equals("facebook")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("mName", fbname);
					inte.putExtra("from", "fromFB");
					inte.putExtra("FBID", fbid);
					inte.putExtra("FirstName", firstname);
					inte.putExtra("LastName", lastname);
					inte.putExtra("FBEmail", fbemail);
					inte.putExtra("loginType", "facebook");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (loginType.equals("google")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("name", name);
					inte.putExtra("email", email);
					inte.putExtra("from", "fromGoogle");
					inte.putExtra("googleID", googleID);
					inte.putExtra("loginType", "google");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					sell = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					sell.putExtra("loginType", "email");
					startActivity(sell);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}

			break;
		case R.id.rent:
			Intent rent = null;
			mRent.setBackgroundResource(R.drawable.enable_bg);
			mBuy.setBackgroundResource(R.drawable.disable_bg);
			mSell.setBackgroundResource(R.drawable.disable_bg);
			mAgent.setBackgroundResource(R.drawable.disable_bg);
			mBroker.setBackgroundResource(R.drawable.disable_bg);
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type,
					AppConstants.RENTER);
			if (AppConstants.Login_From_Map.equals("false")) {
				rent = new Intent(CreateAccountScreen.this, LoginActivity.class);
				startActivity(rent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				if (loginType.equals("facebook")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("mName", fbname);
					inte.putExtra("from", "fromFB");
					inte.putExtra("FBID", fbid);
					inte.putExtra("FirstName", firstname);
					inte.putExtra("LastName", lastname);
					inte.putExtra("FBEmail", fbemail);
					inte.putExtra("loginType", "facebook");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (loginType.equals("google")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("name", name);
					inte.putExtra("email", email);
					inte.putExtra("from", "fromGoogle");
					inte.putExtra("googleID", googleID);
					inte.putExtra("loginType", "google");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					rent = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					rent.putExtra("loginType", "email");
					startActivity(rent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}

			break;
		case R.id.agent:
			Intent agent = null;
			mBuy.setBackgroundResource(R.drawable.disable_bg);
			mSell.setBackgroundResource(R.drawable.disable_bg);
			mRent.setBackgroundResource(R.drawable.disable_bg);
			mBroker.setBackgroundResource(R.drawable.disable_bg);
			mAgent.setBackgroundResource(R.drawable.enable_bg);
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type,
					AppConstants.AGENT);
			if (AppConstants.Login_From_Map.equals("false")) {
				agent = new Intent(CreateAccountScreen.this,
						LoginActivity.class);
				startActivity(agent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				if (loginType.equals("facebook")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("mName", fbname);
					inte.putExtra("from", "fromFB");
					inte.putExtra("FBID", fbid);
					inte.putExtra("FirstName", firstname);
					inte.putExtra("LastName", lastname);
					inte.putExtra("FBEmail", fbemail);
					inte.putExtra("loginType", "facebook");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (loginType.equals("google")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("name", name);
					inte.putExtra("email", email);
					inte.putExtra("from", "fromGoogle");
					inte.putExtra("googleID", googleID);
					inte.putExtra("loginType", "google");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					agent = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					agent.putExtra("loginType", "email");
					startActivity(agent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}

			break;
		case R.id.broker:
			Intent broker = null;
			mBroker.setBackgroundResource(R.drawable.enable_bg);
			mBuy.setBackgroundResource(R.drawable.disable_bg);
			mSell.setBackgroundResource(R.drawable.disable_bg);
			mRent.setBackgroundResource(R.drawable.disable_bg);
			mAgent.setBackgroundResource(R.drawable.disable_bg);
			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type,
					AppConstants.BROKER);
			if (AppConstants.Login_From_Map.equals("false")) {
				broker = new Intent(CreateAccountScreen.this,
						LoginActivity.class);
				startActivity(broker);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				if (loginType.equals("facebook")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("mName", fbname);
					inte.putExtra("from", "fromFB");
					inte.putExtra("FBID", fbid);
					inte.putExtra("FirstName", firstname);
					inte.putExtra("LastName", lastname);
					inte.putExtra("FBEmail", fbemail);
					inte.putExtra("loginType", "facebook");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else if (loginType.equals("google")) {
					Intent inte = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					inte.putExtra("name", name);
					inte.putExtra("email", email);
					inte.putExtra("from", "fromGoogle");
					inte.putExtra("googleID", googleID);
					inte.putExtra("loginType", "google");
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					broker = new Intent(CreateAccountScreen.this,
							RegisterScreen.class);
					broker.putExtra("loginType", "email");
					startActivity(broker);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}

			break;
		case R.id.skip:
//			if (AppConstants.GET_START.equalsIgnoreCase("GET_START")) {
				Intent mapIntent = new Intent(CreateAccountScreen.this,
						MapFragmentActivity.class);
				startActivity(mapIntent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				finish();
//			} else {
//				Intent logIntent = new Intent(CreateAccountScreen.this,
//						LoginActivity.class);
//				startActivity(logIntent);
//				overridePendingTransition(R.anim.slide_in_right,
//						R.anim.slide_out_left);
//			}

			break;

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}

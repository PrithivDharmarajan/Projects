package com.smaat.renterblock.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.RentFilterObjectEntity;
import com.smaat.renterblock.entity.SellFilterObjectEntity;
import com.smaat.renterblock.entity.SoldFilterObjectEntity;
import com.smaat.renterblock.findagent.AgentFilterLocalEntity;
import com.smaat.renterblock.sqlite.DatabaseManager;
import com.smaat.renterblock.sqlite.LocalRentPropertyFilter;
import com.smaat.renterblock.sqlite.LocalSellPropertyFilter;
import com.smaat.renterblock.sqlite.LocalSoldPropertyFilter;
import com.smaat.renterblock.sqlite.RentersBlockDatabase;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.OptionDialogInterfaceListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import com.google.android.gcm.GCMRegistrar;

public class SplashScreen extends BaseActivity {
	public static long Splash = 500;
	private static final int STOPSPLASH = 0;
	private String UserID;
	SharedPreferences settings;
	public static Class<?> mTargetClass;
	Editor editor;
	public static SellFilterObjectEntity mLocaleSellFilterObjectEntity = new SellFilterObjectEntity();
	public static RentFilterObjectEntity mLocaleRentFilterObjectEntity = new RentFilterObjectEntity();
	public static SoldFilterObjectEntity mLocaleSoldFilterObjectEntity = new SoldFilterObjectEntity();
	public static AgentFilterLocalEntity mAgentFilterLocalEntity = new AgentFilterLocalEntity();

	double latitude, longitude;

	protected Object mRegId;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		settings = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
		editor = settings.edit();
		Message msg = new Message();
		msg.what = STOPSPLASH;
		splashHandler.sendMessageDelayed(msg, Splash);

		mContext = SplashScreen.this;

//		facebookHashKey();
		//pushNotification();



		System.out.println("FirebaseInstanceId.getInstance().getToken()"+ FirebaseInstanceId.getInstance().getToken());
//		Toast.makeText(this,"Id---"+ FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_LONG).show();
		System.out.println("IDD"+FirebaseInstanceId.getInstance().getToken());
		System.out.println("Id---=="+ FirebaseInstanceId.getInstance().getToken());
		if (AppConstants.IS_SETTINGS.equalsIgnoreCase("true")) {

			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.NEW_SAVED_SEARCH_MATCHES, "OFF");

			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.SEARCH_RESULT_COUNT, "50");

			GlobalMethods.storeValuetoPreference(this,
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.UPDATE_RESULT_AS_MAP_MOVES, "ON");

			AppConstants.IS_SETTINGS = "false";
		}
		setSellFilterObject();
		setRentFilterObject();
		setSoldFilterObject();

		setAgentFilterObject();
		UserID = (String) GlobalMethods.getValueFromPreference(
				SplashScreen.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);

		GlobalMethods.storeValuetoPreference(SplashScreen.this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.Latitude,
				"12.9651512");

		GlobalMethods.storeValuetoPreference(SplashScreen.this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.Longitude,
				"80.2147728");

		facebookHashKey();
	}

	private void facebookHashKey() {

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.smaat.renterblock", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String hashCode = Base64.encodeToString(md.digest(),
						Base64.DEFAULT);
				System.out.println("Print the hashKey for Facebook :"
						+ hashCode);
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}
	}



	/*private void pushNotification() {

		*//* Push Notifications *//*
		try {

			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);

			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(this);

			final String regId = GCMRegistrar.getRegistrationId(this);

			System.out.println("RegId:" + regId);
			// Check if regid already presents
			if (regId.equals("")) {
				// Registration is not present, register now with GCM
//				GCMRegistrar.register(this, AppConstants.SENDER_ID);
				registerInBackground();
			} else {
				// Device is already registered on GCM
				if (GCMRegistrar.isRegisteredOnServer(this)) {

					GlobalMethods.storeValuetoPreference(this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.pref_deviceReg_Id, regId);

					GlobalMethods.storeValuetoPreference(this,
							GlobalMethods.BOOLEAN_PREFERENCE,
							AppConstants.pref_device_reg_status, false);

					GlobalMethods.storeValuetoPreference(this,
							GlobalMethods.BOOLEAN_PREFERENCE,
							AppConstants.pref_device_id_changed,
							AppConstants.pref_deviceReg_Id
									.equalsIgnoreCase(regId) ? false : true);

				} else {
					GCMRegistrar.register(this, AppConstants.SENDER_ID);
				}
			}
		} catch (UnsupportedOperationException e) {
			DialogManager
					.showMessageDialog(
							this,
							"Your device doest not support Google Services, So you will not recieve any Push message from this application",
							getString(R.string.ok));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	private Handler splashHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				String isShown = (String) GlobalMethods.getValueFromPreference(
						SplashScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.Get_started);
				String isLogin = (String) GlobalMethods.getValueFromPreference(
						SplashScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.pref_Login);
				if (isShown.equals("true")) {
					Intent mapIntent = new Intent(SplashScreen.this,
							MapFragmentActivity.class);
					startActivity(mapIntent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					finish();
				} else {
					GlobalMethods.storeValuetoPreference(SplashScreen.this,
							GlobalMethods.STRING_PREFERENCE,
							AppConstants.Get_started, "true");
					launchActivity(TutorialScreen.class);
				}

				break;

			default:
				break;
			}
		};
	};

	private void setRentFilterObject() {
		mLocaleRentFilterObjectEntity = LocalRentPropertyFilter
				.rentFilter(SplashScreen.this);
		if (mLocaleRentFilterObjectEntity.getBeds() == null) {
			mLocaleRentFilterObjectEntity.setPrice_range_min("");
			mLocaleRentFilterObjectEntity.setPrice_range_max("");
			mLocaleRentFilterObjectEntity.setProperty_type("");
			mLocaleRentFilterObjectEntity.setBeds("");
			mLocaleRentFilterObjectEntity.setBaths("");
			mLocaleRentFilterObjectEntity.setNo_fee("0");
			mLocaleRentFilterObjectEntity.setSquare_footage_min("");
			mLocaleRentFilterObjectEntity.setSquare_footage_max("");
			mLocaleRentFilterObjectEntity.setYear_build_min("");
			mLocaleRentFilterObjectEntity.setYear_build_max("");
			mLocaleRentFilterObjectEntity.setLot_size("");
			mLocaleRentFilterObjectEntity.setDays_on_RB("");
			mLocaleRentFilterObjectEntity.setResale("0");
			mLocaleRentFilterObjectEntity.setNew_construction("0");
			mLocaleRentFilterObjectEntity.setFore_closure("0");
			mLocaleRentFilterObjectEntity.setOpen_house("0");
			mLocaleRentFilterObjectEntity.setReduced_prices("0");
			mLocaleRentFilterObjectEntity.setKeywords("");
			mLocaleRentFilterObjectEntity.setMLS("");
			mLocaleRentFilterObjectEntity.setSold_within("");
			mLocaleRentFilterObjectEntity.setLatitude("");
			mLocaleRentFilterObjectEntity.setLongitude("");

			addRentFilterObjectsindb();
		} else {
			mLocaleRentFilterObjectEntity = LocalRentPropertyFilter
					.rentFilter(SplashScreen.this);
			mLocaleRentFilterObjectEntity
					.setPrice_range_min(mLocaleRentFilterObjectEntity
							.getPrice_range_min());
			mLocaleRentFilterObjectEntity
					.setPrice_range_max(mLocaleRentFilterObjectEntity
							.getPrice_range_max());
			mLocaleRentFilterObjectEntity
					.setProperty_type(mLocaleRentFilterObjectEntity
							.getProperty_type());
			mLocaleRentFilterObjectEntity.setBeds(mLocaleRentFilterObjectEntity
					.getBeds());
			mLocaleRentFilterObjectEntity
					.setBaths(mLocaleRentFilterObjectEntity.getBaths());
			mLocaleRentFilterObjectEntity
					.setNo_fee(mLocaleRentFilterObjectEntity.getNo_fee());
			mLocaleRentFilterObjectEntity
					.setSquare_footage_min(mLocaleRentFilterObjectEntity
							.getSquare_footage_min());
			mLocaleRentFilterObjectEntity
					.setSquare_footage_max(mLocaleRentFilterObjectEntity
							.getSquare_footage_max());
			mLocaleRentFilterObjectEntity
					.setYear_build_min(mLocaleRentFilterObjectEntity
							.getYear_build_min());
			mLocaleRentFilterObjectEntity
					.setYear_build_max(mLocaleRentFilterObjectEntity
							.getYear_build_max());
			mLocaleRentFilterObjectEntity
					.setLot_size(mLocaleRentFilterObjectEntity.getLot_size());
			mLocaleRentFilterObjectEntity
					.setDays_on_RB(mLocaleRentFilterObjectEntity
							.getDays_on_RB());
			mLocaleRentFilterObjectEntity
					.setResale(mLocaleRentFilterObjectEntity.getResale());
			mLocaleRentFilterObjectEntity
					.setNew_construction(mLocaleRentFilterObjectEntity
							.getNew_construction());
			mLocaleRentFilterObjectEntity
					.setFore_closure(mLocaleRentFilterObjectEntity
							.getFore_closure());
			mLocaleRentFilterObjectEntity
					.setOpen_house(mLocaleRentFilterObjectEntity
							.getOpen_house());
			mLocaleRentFilterObjectEntity
					.setReduced_prices(mLocaleRentFilterObjectEntity
							.getReduced_prices());
			mLocaleRentFilterObjectEntity
					.setKeywords(mLocaleRentFilterObjectEntity.getKeywords());
			mLocaleRentFilterObjectEntity.setMLS(mLocaleRentFilterObjectEntity
					.getMLS());
			mLocaleRentFilterObjectEntity
					.setSold_within(mLocaleRentFilterObjectEntity
							.getSold_within());
		}
	}

	private void addRentFilterObjectsindb() {

		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();
		try {

			ContentValues values = new ContentValues();
			values.put("user_id", "0");
			values.put("price_range_min", "");
			values.put("price_range_max", "");
			values.put("property_type", "");
			values.put("beds", "");
			values.put("baths", "");
			values.put("no_fee", "");
			values.put("square_footage_min", "");
			values.put("square_footage_max", "");
			values.put("year_build_min", "");
			values.put("year_build_max", "");
			values.put("lot_size", "");
			values.put("days_on_RB", "");
			values.put("resale", "");
			values.put("new_construction", "");
			values.put("fore_closure", "");
			values.put("open_house", "");
			values.put("reduced_prices", "");
			values.put("keywords", "");
			values.put("MLS", "");
			values.put("sold_within", "");

			db.insert(RentersBlockDatabase.rent_property_type, null, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager.getInstance().closeDatabase();
	}

	private void setSoldFilterObject() {
		mLocaleSoldFilterObjectEntity = LocalSoldPropertyFilter
				.soldFilter(SplashScreen.this);
		if (mLocaleSoldFilterObjectEntity.getBeds() == null) {
			mLocaleSoldFilterObjectEntity.setPrice_range_min("");
			mLocaleSoldFilterObjectEntity.setPrice_range_max("");
			mLocaleSoldFilterObjectEntity.setProperty_type("");
			mLocaleSoldFilterObjectEntity.setBeds("");
			mLocaleSoldFilterObjectEntity.setBaths("");
			mLocaleSoldFilterObjectEntity.setNo_fee("0");
			mLocaleSoldFilterObjectEntity.setSquare_footage_min("");
			mLocaleSoldFilterObjectEntity.setSquare_footage_max("");
			mLocaleSoldFilterObjectEntity.setYear_build_min("");
			mLocaleSoldFilterObjectEntity.setYear_build_max("");
			mLocaleSoldFilterObjectEntity.setLot_size("");
			mLocaleSoldFilterObjectEntity.setDays_on_RB("");
			mLocaleSoldFilterObjectEntity.setResale("0");
			mLocaleSoldFilterObjectEntity.setNew_construction("0");
			mLocaleSoldFilterObjectEntity.setFore_closure("0");
			mLocaleSoldFilterObjectEntity.setOpen_house("0");
			mLocaleSoldFilterObjectEntity.setReduced_prices("0");
			mLocaleSoldFilterObjectEntity.setKeywords("");
			mLocaleSoldFilterObjectEntity.setMLS("");
			mLocaleSoldFilterObjectEntity.setSold_within("");
			addSoldFilterObjectsindb();
		} else {
			mLocaleSoldFilterObjectEntity = LocalSoldPropertyFilter
					.soldFilter(SplashScreen.this);
			mLocaleSoldFilterObjectEntity
					.setPrice_range_min(mLocaleSoldFilterObjectEntity
							.getPrice_range_min());
			mLocaleSoldFilterObjectEntity
					.setPrice_range_max(mLocaleSoldFilterObjectEntity
							.getPrice_range_max());
			mLocaleSoldFilterObjectEntity
					.setProperty_type(mLocaleSoldFilterObjectEntity
							.getProperty_type());
			mLocaleSoldFilterObjectEntity.setBeds(mLocaleSoldFilterObjectEntity
					.getBeds());
			mLocaleSoldFilterObjectEntity
					.setBaths(mLocaleSoldFilterObjectEntity.getBaths());
			mLocaleSoldFilterObjectEntity
					.setNo_fee(mLocaleSoldFilterObjectEntity.getNo_fee());
			mLocaleSoldFilterObjectEntity
					.setSquare_footage_min(mLocaleSoldFilterObjectEntity
							.getSquare_footage_min());
			mLocaleSoldFilterObjectEntity
					.setSquare_footage_max(mLocaleSoldFilterObjectEntity
							.getSquare_footage_max());
			mLocaleSoldFilterObjectEntity
					.setYear_build_min(mLocaleSoldFilterObjectEntity
							.getYear_build_min());
			mLocaleSoldFilterObjectEntity
					.setYear_build_max(mLocaleSoldFilterObjectEntity
							.getYear_build_max());
			mLocaleSoldFilterObjectEntity
					.setLot_size(mLocaleSoldFilterObjectEntity.getLot_size());
			mLocaleSoldFilterObjectEntity
					.setDays_on_RB(mLocaleSoldFilterObjectEntity
							.getDays_on_RB());
			mLocaleSoldFilterObjectEntity
					.setResale(mLocaleSoldFilterObjectEntity.getResale());
			mLocaleSoldFilterObjectEntity
					.setNew_construction(mLocaleSoldFilterObjectEntity
							.getNew_construction());
			mLocaleSoldFilterObjectEntity
					.setFore_closure(mLocaleSoldFilterObjectEntity
							.getFore_closure());
			mLocaleSoldFilterObjectEntity
					.setOpen_house(mLocaleSoldFilterObjectEntity
							.getOpen_house());
			mLocaleSoldFilterObjectEntity
					.setReduced_prices(mLocaleSoldFilterObjectEntity
							.getReduced_prices());
			mLocaleSoldFilterObjectEntity
					.setKeywords(mLocaleSoldFilterObjectEntity.getKeywords());
			mLocaleSoldFilterObjectEntity.setMLS(mLocaleSoldFilterObjectEntity
					.getMLS());
			mLocaleSoldFilterObjectEntity
					.setSold_within(mLocaleSoldFilterObjectEntity
							.getSold_within());
		}
	}

	private void addSoldFilterObjectsindb() {
		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();
		try {

			ContentValues values = new ContentValues();
			values.put("user_id", "0");
			values.put("price_range_min", "");
			values.put("price_range_max", "");
			values.put("property_type", "");
			values.put("beds", "");
			values.put("baths", "");
			values.put("no_fee", "");
			values.put("square_footage_min", "");
			values.put("square_footage_max", "");
			values.put("year_build_min", "");
			values.put("year_build_max", "");
			values.put("lot_size", "");
			values.put("days_on_RB", "");
			values.put("resale", "");
			values.put("new_construction", "");
			values.put("fore_closure", "");
			values.put("open_house", "");
			values.put("reduced_prices", "");
			values.put("keywords", "");
			values.put("MLS", "");
			// values.put("include_estimate", "");
			values.put("sold_within", "");

			db.insert(RentersBlockDatabase.sold_property_type, null, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager.getInstance().closeDatabase();
	}

	private void setSellFilterObject() {
		mLocaleSellFilterObjectEntity = LocalSellPropertyFilter
				.sellFilter(SplashScreen.this);
		if (mLocaleSellFilterObjectEntity.getBeds() == null) {
			mLocaleSellFilterObjectEntity.setPrice_range_min("");
			mLocaleSellFilterObjectEntity.setPrice_range_max("");
			mLocaleSellFilterObjectEntity.setProperty_type("");
			mLocaleSellFilterObjectEntity.setBeds("");
			mLocaleSellFilterObjectEntity.setBaths("");
			mLocaleSellFilterObjectEntity.setNo_fee("0");
			mLocaleSellFilterObjectEntity.setSquare_footage_min("");
			mLocaleSellFilterObjectEntity.setSquare_footage_max("");
			mLocaleSellFilterObjectEntity.setYear_build_min("");
			mLocaleSellFilterObjectEntity.setYear_build_max("");
			mLocaleSellFilterObjectEntity.setLot_size("");
			mLocaleSellFilterObjectEntity.setDays_on_RB("");
			mLocaleSellFilterObjectEntity.setResale("0");
			mLocaleSellFilterObjectEntity.setNew_construction("0");
			mLocaleSellFilterObjectEntity.setFore_closure("0");
			mLocaleSellFilterObjectEntity.setOpen_house("0");
			mLocaleSellFilterObjectEntity.setReduced_prices("0");
			mLocaleSellFilterObjectEntity.setKeywords("");
			mLocaleSellFilterObjectEntity.setMLS("");
			mLocaleSellFilterObjectEntity.setSold_within("");

			addSellFilterObjectsindb();
		} else {
			mLocaleSellFilterObjectEntity = LocalSellPropertyFilter
					.sellFilter(SplashScreen.this);
			mLocaleSellFilterObjectEntity
					.setPrice_range_min(mLocaleSellFilterObjectEntity
							.getPrice_range_min());
			mLocaleSellFilterObjectEntity
					.setPrice_range_max(mLocaleSellFilterObjectEntity
							.getPrice_range_max());
			mLocaleSellFilterObjectEntity
					.setProperty_type(mLocaleSellFilterObjectEntity
							.getProperty_type());
			mLocaleSellFilterObjectEntity.setBeds(mLocaleSellFilterObjectEntity
					.getBeds());
			mLocaleSellFilterObjectEntity
					.setBaths(mLocaleSellFilterObjectEntity.getBaths());
			mLocaleSellFilterObjectEntity
					.setNo_fee(mLocaleSellFilterObjectEntity.getNo_fee());
			mLocaleSellFilterObjectEntity
					.setSquare_footage_min(mLocaleSellFilterObjectEntity
							.getSquare_footage_min());
			mLocaleSellFilterObjectEntity
					.setSquare_footage_max(mLocaleSellFilterObjectEntity
							.getSquare_footage_max());
			mLocaleSellFilterObjectEntity
					.setYear_build_min(mLocaleSellFilterObjectEntity
							.getYear_build_min());
			mLocaleSellFilterObjectEntity
					.setYear_build_max(mLocaleSellFilterObjectEntity
							.getYear_build_max());
			mLocaleSellFilterObjectEntity
					.setLot_size(mLocaleSellFilterObjectEntity.getLot_size());
			mLocaleSellFilterObjectEntity
					.setDays_on_RB(mLocaleSellFilterObjectEntity
							.getDays_on_RB());
			mLocaleSellFilterObjectEntity
					.setResale(mLocaleSellFilterObjectEntity.getResale());
			mLocaleSellFilterObjectEntity
					.setNew_construction(mLocaleSellFilterObjectEntity
							.getNew_construction());
			mLocaleSellFilterObjectEntity
					.setFore_closure(mLocaleSellFilterObjectEntity
							.getFore_closure());
			mLocaleSellFilterObjectEntity
					.setOpen_house(mLocaleSellFilterObjectEntity
							.getOpen_house());
			mLocaleSellFilterObjectEntity
					.setReduced_prices(mLocaleSellFilterObjectEntity
							.getReduced_prices());
			mLocaleSellFilterObjectEntity
					.setKeywords(mLocaleSellFilterObjectEntity.getKeywords());
			mLocaleSellFilterObjectEntity.setMLS(mLocaleSellFilterObjectEntity
					.getMLS());
			mLocaleSellFilterObjectEntity
					.setSold_within(mLocaleSellFilterObjectEntity
							.getSold_within());
		}
	}

	private void addSellFilterObjectsindb() {
		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();
		try {

			ContentValues values = new ContentValues();
			values.put("user_id", "0");
			values.put("price_range_min", "");
			values.put("price_range_max", "");
			values.put("property_type", "");
			values.put("beds", "");
			values.put("baths", "");
			values.put("no_fee", "");
			values.put("square_footage_min", "");
			values.put("square_footage_max", "");
			values.put("year_build_min", "");
			values.put("year_build_max", "");
			values.put("lot_size", "");
			values.put("days_on_RB", "");
			values.put("resale", "");
			values.put("new_construction", "");
			values.put("fore_closure", "");
			values.put("open_house", "");
			values.put("reduced_prices", "");
			values.put("keywords", "");
			values.put("MLS", "");
			// values.put("include_estimate", "");
			values.put("sold_within", "");

			db.insert(RentersBlockDatabase.sell_property_type, null, values);

		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager.getInstance().closeDatabase();
	}

	private void setAgentFilterObject() {
		if (mAgentFilterLocalEntity.getName() == null) {
			mAgentFilterLocalEntity.setName("");
			mAgentFilterLocalEntity.setLocation("");
			mAgentFilterLocalEntity.setType("");
			mAgentFilterLocalEntity.setPrice_range_min("");
			mAgentFilterLocalEntity.setPrice_range_max("");
			mAgentFilterLocalEntity.setProperty_experties("");
			mAgentFilterLocalEntity.setLatitude("");
			mAgentFilterLocalEntity.setLongitude("");

		} else {
			mAgentFilterLocalEntity.setName(mAgentFilterLocalEntity.getName());
			mAgentFilterLocalEntity.setLocation(mAgentFilterLocalEntity
					.getLocation());
			mAgentFilterLocalEntity.setType(mAgentFilterLocalEntity.getType());
			mAgentFilterLocalEntity.setPrice_range_min(mAgentFilterLocalEntity
					.getPrice_range_min());
			mAgentFilterLocalEntity.setPrice_range_max(mAgentFilterLocalEntity
					.getPrice_range_max());
			mAgentFilterLocalEntity
					.setProperty_experties(mAgentFilterLocalEntity
							.getProperty_experties());
			mAgentFilterLocalEntity.setLatitude(mAgentFilterLocalEntity
					.getLatitude());
			mAgentFilterLocalEntity.setLongitude(mAgentFilterLocalEntity
					.getLongitude());
		}
	}

	private void showNotificationDialog() {
		GlobalMethods.showOptionDialogListener(SplashScreen.this,
				getString(R.string.app_name), "Allow Push Notifications?",
				"Yes", "No", new OptionDialogInterfaceListener() {

					@Override
					public void okClick() {

						showLocationDialog();
					}

					@Override
					public void cancelClick() {
						showLocationDialog();
					}
				});
	}

	private void showLocationDialog() {
		GlobalMethods.showOptionDialogListener(SplashScreen.this,
				getString(R.string.app_name),
				"Turn on Location Services to determine your Location?", "Yes",
				"Cancel", new OptionDialogInterfaceListener() {

					@Override
					public void okClick() {
						launchActivity(TutorialScreen.class);
					}

					@Override
					public void cancelClick() {
						launchActivity(TutorialScreen.class);
					}
				});
	}

}

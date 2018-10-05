package com.smaat.jolt.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.UserDetailsEntity;

public class GlobalMethods {

	public static int STRING_PREFERENCE = 1;
	public static int INT_PREFERENCE = 2;
	public static int BOOLEAN_PREFERENCE = 3;
	public static int ARRAY_LIST_PREFERENCE = 4;
	public static int LONG_PREFERENCE = 5;

	public static void storeValuetoPreference(Context context, int preference,
			String key, Object value) {
		SharedPreferences sharedPreference = context.getSharedPreferences(
				AppConstants.shared_pref_name, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedPreference.edit();
		if (preference == STRING_PREFERENCE) {
			edit.putString(key, (String) value);
		}

		if (preference == INT_PREFERENCE) {
			edit.putInt(key, (Integer) value);
		}

		if (preference == BOOLEAN_PREFERENCE) {
			edit.putBoolean(key, (Boolean) value);
		}
		if (preference == ARRAY_LIST_PREFERENCE) {
			edit.putString(key, (String) value);

		}
		if (preference == LONG_PREFERENCE) {
			edit.putLong(key, (Long) value);
		}

		edit.commit();

	}

	public static String getCurrentDate() {
		String mFormateDate, mFormateDateTime;
		SimpleDateFormat mDateFormate;
		Calendar mCalendar = Calendar.getInstance();
		mDateFormate = new SimpleDateFormat("hh:mm a MMMM dd yyyy", Locale.US);
		mFormateDate = mDateFormate.format(mCalendar.getTime());
		mFormateDateTime = mFormateDate.replace("AM", "am").replace("PM", "pm");
		return mFormateDateTime;
	}

	public static String getDisplayFormat(String date) {
		String result = "";
		try {

			SimpleDateFormat fromformat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss",Locale.US);
			SimpleDateFormat toformatter = new SimpleDateFormat(
					"MMMM dd, yyyy HH:mm",Locale.US);
			fromformat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date parsed = null;
			parsed = fromformat.parse(date);

			TimeZone tz = TimeZone.getDefault();

			toformatter.setTimeZone(tz);

			result = toformatter.format(parsed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Checking for all possible internet providers
	 * **/
	public static boolean isConnectingToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;

		}
		return isValid;
	}

	public static void wetsiteClick(Context context, String link) {
		String url = "";
		if (link.contains("https:\\")) {
			url = link;
		} else {
			url = "https:\\" + link;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		context.startActivity(i);
	}

	public static String getUserID(Context context) {
		String UserID = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID);

		return UserID;
	}

	public static String getUserType(Context context) {
		String UserType = (String) GlobalMethods.getValueFromPreference(
				context, GlobalMethods.STRING_PREFERENCE,
				AppConstants.LOGIN_TYPE);

		return UserType;
	}

	public static void updateUserDetails(Context context,
			UserDetailsEntity response) {

		GlobalMethods
				.storeValuetoPreference(context,
						GlobalMethods.BOOLEAN_PREFERENCE,
						AppConstants.ISLOGGEDIN, true);

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
				response.getUserID());

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
				response.getEmailAddress());
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.FULL_NAME,
				response.getFullName());
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.PROFILE_PIC,
				response.getProfilePicture());
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.AVAIL_DRINKS,
				response.getNoOfDrinksAvailable());
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
				response.getUserID());
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.UNIQUE_CODE,
				response.getMyCoffeCode());

		updateCardDetails(context, response.getCardNumber(),
				response.getCardExpiryMonth(), response.getCardExpiryYear(),
				response.getCardCVV());
	}

	public static void updateCardDetails(Context context, String cardNumber,
			String expiryMonth, String expiryYear, String cvv) {

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.CARD_NUMBER,
				cardNumber);

		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.CARD_EXPIRY_MONTH, expiryMonth);
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.CARD_EXPIRY_YEAR,
				expiryYear);
		GlobalMethods.storeValuetoPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.CARD_CVV, cvv);

	}

	public static String getDeviceToken(Context context) {
		String deviceToken = (String) GlobalMethods.getValueFromPreference(
				context, GlobalMethods.STRING_PREFERENCE,
				AppConstants.DEVICE_TOKEN);

		if (deviceToken == null || deviceToken.isEmpty()) {
			deviceToken = "";
		}

		return deviceToken;
	}

	public static boolean isLoggedIn(Context context) {
		return (Boolean) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN);
	}

	public static boolean isTutorialSeen(Context context) {
		return (Boolean) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN);
	}

	public static Object getValueFromPreference(Context context,
			int preference, String key) {
		SharedPreferences sharedPreference = context.getSharedPreferences(
				AppConstants.shared_pref_name, Context.MODE_PRIVATE);

		if (preference == STRING_PREFERENCE) {
			return (Object) sharedPreference.getString(key, "");
		}

		if (preference == INT_PREFERENCE) {
			return (Object) sharedPreference.getInt(key, 0);
		}

		if (preference == BOOLEAN_PREFERENCE) {
			return (Object) sharedPreference.getBoolean(key, false);
		}
		if (preference == LONG_PREFERENCE) {
			return (Object) sharedPreference.getLong(key, 0L);
		}

		return null;

	}

	public static void setFont(ViewGroup group, Typeface font) {

		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button /* etc. */)
				((TextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	public static int dpToPx(Context context, float dp) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();

		int px = Math.round(dp
				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static String getDistanceString(String distance, Context context) {

		String distanceText = "";
		double miles;

		try {

			if (AppConstants.appDistanceUnit.isEmpty()
					|| AppConstants.appDistanceUnit.equalsIgnoreCase(context
							.getString(R.string.miles))) {
				miles = Double.parseDouble(distance) * 0.621;
				distanceText = String.format("%.1f%n", miles) + " "
						+ context.getString(R.string.miles);
				// SettingsFragment.mToggle.setImageResource(R.drawable.miles_visible);
			} else {
				miles = Double.parseDouble(distance);
				distanceText = String.format("%.1f%n", miles) + " "
						+ context.getString(R.string.km);
				// SettingsFragment.mToggle.setImageResource(R.drawable.km_visible);
			}

		} catch (Exception e) {
			miles = 0.0;
		}

		return distanceText;

	}

	public static void SendMail(Context context, String mToMailId, String mSub,
			String mTxt) {
		String mPhoneBrand, mPhoneModel, mAndroidVersion, mMsgTxt;
		String mUserDetails = null;
		int mApiVersion;

		// Device Brand
		mPhoneBrand = android.os.Build.MANUFACTURER;
		// Device model
		mPhoneModel = android.os.Build.MODEL;
		// Android version
		mAndroidVersion = android.os.Build.VERSION.RELEASE;
		// API version
		mApiVersion = android.os.Build.VERSION.SDK_INT;

		Intent email = new Intent(Intent.ACTION_SENDTO);
		email.setData(Uri.parse("mailto:" + mToMailId)); // mailto:
		email.putExtra(Intent.EXTRA_SUBJECT, mSub);

		try {

			if (mSub.equalsIgnoreCase(context
					.getString(R.string.want_recommend_barista))
					|| mSub.equalsIgnoreCase(context
							.getString(R.string.want_suggest_shop))) {
				mUserDetails = "\n\n"

						+ context.getString(R.string.user)
						+ "\t"
						+ (String) GlobalMethods.getValueFromPreference(
								context, GlobalMethods.STRING_PREFERENCE,
								AppConstants.EMAIL_ADDRESS) + "\n";

			} else {
				mUserDetails = "\n\n"
						+ context.getString(R.string.app_link_txt)
						+ "\t"
						+ context.getString(R.string.App_link)
						+ "\n\n"
						+ context.getString(R.string.device)
						+ "\t"
						+ mPhoneBrand
						+ "\t\t"
						+ mPhoneModel
						+ "\t"
						+ mAndroidVersion
						+ "\t("
						+ context.getString(R.string.api_level)
						+ "\t"
						+ mApiVersion
						+ ")\n"
						+ context.getString(R.string.app_version)
						+ "\t"
						+ context.getPackageManager().getPackageInfo(
								context.getPackageName(), 0).versionName
						+ "\n"
						+ context.getString(R.string.user)
						+ "\t"
						+ (String) GlobalMethods.getValueFromPreference(
								context, GlobalMethods.STRING_PREFERENCE,
								AppConstants.EMAIL_ADDRESS);
			}

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mMsgTxt = mTxt + mUserDetails;
		email.putExtra(Intent.EXTRA_TEXT, mMsgTxt);

		context.startActivity(email);
	}
}

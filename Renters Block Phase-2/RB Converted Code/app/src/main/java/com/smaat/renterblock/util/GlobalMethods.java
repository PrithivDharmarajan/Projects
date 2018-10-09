package com.smaat.renterblock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smaat.renterblock.R;

public class GlobalMethods {

	public static int STRING_PREFERENCE = 1;
	public static int INT_PREFERENCE = 2;
	public static int BOOLEAN_PREFERENCE = 3;
	public static int LONG_PREFERENCE = 4;

	Calendar dateCalendar;
	TimePickerDialog mtimePicker;
	static String time;
	private static Dialog mDialog;

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	/**
	 * Checking for all possible internet providers
	 **/
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

	public static void setupUI(View view, final Activity _activity) {

		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(_activity);
					return false;
				}

			});
		}

		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView, _activity);
			}
		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);

			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
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

	public static void storeValuetoPreference(Context context, int preference, String key, Object value) {
		SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
				Context.MODE_PRIVATE);
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
		if (preference == LONG_PREFERENCE) {
			edit.putLong(key, (Long) value);
		}

		edit.commit();

	}

	public static Object getValueFromPreference(Context context, int preference, String key) {
		SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
				Context.MODE_PRIVATE);

		if (preference == STRING_PREFERENCE) {
			return (Object) sharedPreference.getString(key, "0");
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

	public static int setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		totalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		params.height = totalHeight;
		listView.setLayoutParams(params);
		listView.requestLayout();
		return totalHeight;
	}

	public static String getUserID(Context context) {
		String UserID = (String) GlobalMethods.getValueFromPreference(context, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);

		return UserID;
	}

	public static void showOptionDialogListener(final Context context, String title, String message, String buttonText,
			String buttonText1, final OptionDialogInterfaceListener dialoginterface) {

		mDialog = new Dialog(context, R.anim.slide_in_from_bottom);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.option_dialog);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setCancelable(true);
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		TextView alertMessage = (TextView) mDialog.findViewById(R.id.alert_message);
		Button button = (Button) mDialog.findViewById(R.id.ok);

		alertMessage.setText(message);
		button.setText(buttonText);

		Button cancel = (Button) mDialog.findViewById(R.id.cancel);
		cancel.setText(buttonText1);

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialoginterface.cancelClick();
				mDialog.dismiss();
			}
		});

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialoginterface.okClick();
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	public static String getFormatedDate(String strDate, String sourceFormate, String destinyFormate) {
		SimpleDateFormat df;
		df = new SimpleDateFormat(sourceFormate);
		Date date = null;
		try {
			date = df.parse(strDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		df = new SimpleDateFormat(destinyFormate);
		return df.format(date);

	}

	public static String convertDate(String dateInMilliseconds, String dateFormat) {
		return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
	}

	public static String timeDiff(String dateTime) {

		SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String cDateTime = dateFormats.format(new Date());

		System.out.println(dateTime);
		Date date = new Date();
		Date date1 = new Date();
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Calendar calendar = Calendar.getInstance();
		long daysBetween = 0;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			date = format.parse(dateTime);
			date1 = format.parse(cDateTime);

			Calendar sDate = getDatePart(date);
			Calendar eDate = getDatePart(date1);

			while (sDate.before(eDate)) {
				sDate.add(Calendar.DAY_OF_MONTH, 1);
				daysBetween++;
			}

			calendar.setTime(date);

			long currentTime = System.currentTimeMillis();
			int localOffset = TimeZone.getDefault().getOffset(currentTime);
			int gmtOffset = TimeZone.getTimeZone("GMT").getOffset(currentTime);
			int minDiff = ((localOffset - gmtOffset) / (1000 * 60)) % 60;
			int hourDiff = (localOffset - gmtOffset) / (1000 * 60 * 60);
			calendar.add(Calendar.HOUR, hourDiff);
			calendar.add(Calendar.MINUTE, minDiff);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		String result = (String) DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(),
				System.currentTimeMillis(), 0);

		// String result = "Last updated " + daysBetween+" days ago";
		return result;
	}

	public static Calendar getDatePart(Date date) {
		Calendar cal = Calendar.getInstance(); // get calendar instance
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
		cal.set(Calendar.MINUTE, 0); // set minute in hour
		cal.set(Calendar.SECOND, 0); // set second in minute
		cal.set(Calendar.MILLISECOND, 0); // set millisecond in second

		return cal; // return the date part
	}

	public static String myFavtimeDiff(String dateTime) {
		SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String cDateTime = dateFormats.format(new Date());

		System.out.println(dateTime);
		Date date = new Date();
		Date date1 = new Date();
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Calendar calendar = Calendar.getInstance();
		long daysBetween = 0;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			date = format.parse(dateTime);
			date1 = format.parse(cDateTime);

			Calendar sDate = getDatePart(date);
			Calendar eDate = getDatePart(date1);

			while (sDate.before(eDate)) {
				sDate.add(Calendar.DAY_OF_MONTH, 1);
				daysBetween++;
			}

			calendar.setTime(date);

			long currentTime = System.currentTimeMillis();
			int localOffset = TimeZone.getDefault().getOffset(currentTime);
			int gmtOffset = TimeZone.getTimeZone("GMT").getOffset(currentTime);
			int minDiff = ((localOffset - gmtOffset) / (1000 * 60)) % 60;
			int hourDiff = (localOffset - gmtOffset) / (1000 * 60 * 60);
			calendar.add(Calendar.HOUR, hourDiff);
			calendar.add(Calendar.MINUTE, minDiff);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// String result = (String) DateUtils.getRelativeTimeSpanString(
		// calendar.getTimeInMillis(), System.currentTimeMillis(), 0);

		String result = "Last updated " + daysBetween + " days ago";
		return result;
	}

	public static String recentsTimeCalculation(String dateTime) {

		SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String cDateTime = dateFormats.format(new Date());

		System.out.println(dateTime);
		Date date = new Date();
		Date date1 = new Date();
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Calendar calendar = Calendar.getInstance();
		long daysBetween = 0;
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			date = format.parse(dateTime);
			date1 = format.parse(cDateTime);

			Calendar sDate = getDatePart(date);
			Calendar eDate = getDatePart(date1);

			while (sDate.before(eDate)) {
				sDate.add(Calendar.DAY_OF_MONTH, 1);
				daysBetween++;
			}

			calendar.setTime(date);

			long currentTime = System.currentTimeMillis();
			int localOffset = TimeZone.getDefault().getOffset(currentTime);
			int gmtOffset = TimeZone.getTimeZone("GMT").getOffset(currentTime);
			int minDiff = ((localOffset - gmtOffset) / (1000 * 60)) % 60;
			int hourDiff = (localOffset - gmtOffset) / (1000 * 60 * 60);
			calendar.add(Calendar.HOUR, hourDiff);
			calendar.add(Calendar.MINUTE, minDiff);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// String result = (String) DateUtils.getRelativeTimeSpanString(
		// calendar.getTimeInMillis(), System.currentTimeMillis(), 0);

		String days = String.valueOf(daysBetween);
		String result = "";
		if (days.equalsIgnoreCase("0")) {
			// result = "Last updated "
			// + (String) DateUtils.getRelativeTimeSpanString(
			// calendar.getTimeInMillis(),
			// System.currentTimeMillis(), 0);

			result = (String) DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(),
					System.currentTimeMillis(), 0);

		} else {
			try {
				SimpleDateFormat new_format = new SimpleDateFormat("MMM dd");
				Date da = format.parse(dateTime);
				String time_fo = new_format.format(da);
				result = time_fo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	public static String checkTimeLeft(String dateTime, String currenttime) {
		String minutes_left = "";
		try {
			SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			String cDateTime = currenttime;
			Date added_date = dateFormats.parse(dateTime);
			Date current_date = dateFormats.parse(cDateTime);
			long diff = current_date.getTime() - added_date.getTime();
			long seconds = diff / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;

			long sad = 24 * 60;
			long sd = sad - minutes;
			long dd = sd / 60;
			long sec = dd / 60;

			if (dd <= 1) {
				minutes_left = String.valueOf(sec) + " minutes left";
			} else {
				minutes_left = String.valueOf(dd) + " hours left";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return minutes_left;
	}

	private static String connwebservice(String url) {
		// TODO Auto-generated method stub
		String result = "";
		try {

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpclient = new DefaultHttpClient(httpParameters);

			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Accept", "application/json");
			HttpResponse response;

			try {
				response = httpclient.execute(httpget);

				// Examine the response status
				if (response != null) {
					// response.getStatusLine().toString());
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						// A Simple JSON Response Read
						InputStream instream = entity.getContent();
						result = convertStreamToString(instream);

						instream.close();
					}
				} else {
					return null;
				}

			} catch (Exception exception) {

				exception.printStackTrace();

				return null;
			} finally {

				httpclient.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public static JSONArray getJsonArrayFromJsonObject(JSONObject jsonObject, String key) {
		try {
			if (jsonObject.getString(key) == null || jsonObject.getString(key).equalsIgnoreCase("-1")) {
				return new JSONArray();
			} else {
				return jsonObject.getJSONArray(key);
			}

		} catch (JSONException jSONException)

		{
			return new JSONArray();
		}
	}

	public static String dateFormateNewFormat(Context context, String mDate) {
		String newTime = "";
		TimeZone mTimeZone = TimeZone.getDefault();
		String mDefaultTimeZone = mTimeZone.getDisplayName(false, TimeZone.SHORT);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			dateFormat.setTimeZone(TimeZone.getTimeZone(mDefaultTimeZone));
			SimpleDateFormat convertFormat;
			convertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			convertFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date newDate = dateFormat.parse(mDate);
			newTime = convertFormat.format(newDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newTime;
	}
	
//	03/17/2016 09:00 PM
	
	public static String dateFormat(Context context, String mDate) {
		String newTime = "";
		TimeZone mTimeZone = TimeZone.getDefault();
		String mDefaultTimeZone = mTimeZone.getDisplayName(false, TimeZone.SHORT);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			SimpleDateFormat convertFormat;
			convertFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
//			convertFormat.setTimeZone(TimeZone.getTimeZone(mDefaultTimeZone));
			Date newDate = dateFormat.parse(mDate);
			newTime = convertFormat.format(newDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newTime;
	}
	
	public static String dateFormats(Context context, String mDate) {
		String newTime = "";
		TimeZone mTimeZone = TimeZone.getDefault();
		String mDefaultTimeZone = mTimeZone.getDisplayName(false, TimeZone.SHORT);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			SimpleDateFormat convertFormat;
			convertFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			convertFormat.setTimeZone(TimeZone.getTimeZone(mDefaultTimeZone));
			Date newDate = dateFormat.parse(mDate);
			newTime = convertFormat.format(newDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newTime;
	}

	public static ArrayList<String> getLatlong(String address, Activity homeSearch) {

		ArrayList<String> locationdetails = new ArrayList<String>();
		String add = address.replace(" ", "%20");
		String url = "http://maps.googleapis.com/maps/api/geocode/json?address=" + add + "&sensor=true";

		String result = connwebservice(url);

		try {
			JSONObject json_result = new JSONObject(result);
			JSONArray jsonArray = getJsonArrayFromJsonObject(json_result, "results");

			double lng = (jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng"));

			double lat = (jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat"));

			String longname = (jsonArray.getJSONObject(0).getJSONArray("address_components").getJSONObject(1)
					.getString("long_name"));

			String latLong = lat + "," + lng;

			locationdetails.add(longname);
			locationdetails.add(latLong);

			return locationdetails;

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	public static long getLocTimeInMillis(String time) {

		long timeInMilliseconds = 0;
		String result = "";
		try {

			SimpleDateFormat fromformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat toformatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
			// fromformat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date parsed = null;
			parsed = fromformat.parse(time);

			TimeZone tz = TimeZone.getDefault();

			toformatter.setTimeZone(tz);

			result = toformatter.format(parsed);

			try {
				Date mDate = toformatter.parse(result);
				timeInMilliseconds = mDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
		return timeInMilliseconds;
	}

	public static void saveObject(Context context, Object obj, String key) {
		SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
				Context.MODE_PRIVATE);
		Editor prefsEditor = sharedPreference.edit();
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		prefsEditor.putString(key, json);
		prefsEditor.commit();
	}

	public static <T> T readObject(Context context, String key, Class<T> a) {
		if (context != null) {
			SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
					Context.MODE_PRIVATE);
			Gson gson = new Gson();
			String json = sharedPreference.getString(key, "");
			if (json.length() > 0) {
				return (T) gson.fromJson(json, a);
			} else
				return null;
		} else
			return null;
	}

}

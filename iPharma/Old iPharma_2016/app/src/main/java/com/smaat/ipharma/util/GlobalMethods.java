package com.smaat.ipharma.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.RegisterationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();

        int px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
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

    public static JSONArray getJsonArrayFromJsonObject(JSONObject jsonObject,
                                                       String key) {
        try {
            if (jsonObject.getString(key) == null
                    || jsonObject.getString(key).equalsIgnoreCase("-1")) {
                return new JSONArray();
            } else {
                return jsonObject.getJSONArray(key);
            }

        } catch (JSONException jSONException)

        {
            return new JSONArray();
        }
    }

	/*private static String connwebservice(String url) {
        // TODO Auto-generated method stub
		String result = "";
		try {

			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
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
*/


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

            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);

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
        if (preference == LONG_PREFERENCE) {
            edit.putLong(key, (Long) value);
        }

        edit.commit();

    }

    public static Object getValueFromPreference(Context context,
                                                int preference, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(
                AppConstants.shared_pref_name, Context.MODE_PRIVATE);

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
        totalHeight = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
        return totalHeight;
    }

    public static String getUserID(Context context) {
        String UserID = (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID);

        return UserID;
    }

    public static String getTutorialShown(Context context) {
        String Tut = (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.TUT_SCREEN);

        return Tut;
    }

    public static String getUserName(Context context) {
        String Username = (String) GlobalMethods.getValueFromPreference(
                context, GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_NAME);

        return Username;
    }

    public static String getUserEmailid(Context context) {
        String Username = (String) GlobalMethods.getValueFromPreference(
                context, GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_EMAIL_ID);

        return Username;
    }

    public static void showOptionDialogListener(final Context context,
                                                String title, String message, String buttonText,
                                                String buttonText1,
                                                final OptionDialogInterfaceListener dialoginterface) {

        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.option_dialog);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
        wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
        mDialog.setCancelable(false);

        TextView alertMessage = (TextView) mDialog
                .findViewById(R.id.alert_message);
        Button button = (Button) mDialog.findViewById(R.id.ok);

        alertMessage.setText(message);
        button.setText(buttonText);

        Button cancel = (Button) mDialog.findViewById(R.id.cancel);
        cancel.setText(buttonText1);

        cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialoginterface.cancelClick();
                mDialog.dismiss();
            }
        });

        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                dialoginterface.okClick();
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFormatedDate(String strDate, String sourceFormate,
                                         String destinyFormate) {
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

    public static String convertDate(String dateInMilliseconds,
                                     String dateFormat) {
        return DateFormat
                .format(dateFormat, Long.parseLong(dateInMilliseconds))
                .toString();
    }

    @SuppressLint("SimpleDateFormat")
    public static String timeDiff(String dateTime) {


        Date date = new Date();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            date = format.parse(dateTime);

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

        String result = (String) DateUtils.getRelativeTimeSpanString(
                calendar.getTimeInMillis(), System.currentTimeMillis(), 0);
        return result;
    }

    public static void UpdateUserDetails(Context context,
                                         RegisterationResponse registerationResponse) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
                registerationResponse.getUserID());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_NAME,
                registerationResponse.getFullName());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_EMAIL_ID,
                registerationResponse.getEmail());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_PHONE_NUM,
                registerationResponse.getPhone());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_CITY,
                registerationResponse.getCity());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_AREA,
                registerationResponse.getArea());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_AREA_PINCODE,
                registerationResponse.getPincode());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ADDRESS,
                registerationResponse.getAddress());
    }


    public static String getAppConstantValue(Context context, String appConstantString) {

        return (String) GlobalMethods.getValueFromPreference(context, GlobalMethods.STRING_PREFERENCE, appConstantString);
    }

}

package com.smaat.spark.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.smaat.spark.R;
import com.smaat.spark.entity.outputEntity.UserDetailsEntity;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GlobalMethods {

    private static int STRING_PREFERENCE = 1;
    private static int INT_PREFERENCE = 2;
    public static int BOOLEAN_PREFERENCE = 3;

    public static void storeValueToPreference(Context context, int preference,
                                              String key, Object value) {
        SharedPreferences sharedPreference = context.getSharedPreferences(
                AppConstants.shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreference.edit();
        if (preference == STRING_PREFERENCE) {
            edit.putString(key, (String) value);
        }
        if (preference == INT_PREFERENCE) {
            edit.putInt(key, (int) value);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            edit.putBoolean(key, (boolean) value);
        }
        edit.apply();

    }

    private static Object getValueFromPreference(Context context,
                                                 int preference, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(
                AppConstants.shared_pref_name, Context.MODE_PRIVATE);

        if (preference == STRING_PREFERENCE) {
            return sharedPreference.getString(key, "");
        }
        if (preference == INT_PREFERENCE) {
            return sharedPreference.getInt(key, 0);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            return sharedPreference.getBoolean(key, false);
        }
        return null;

    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }


    public static void storeUserDetails(Context context, UserDetailsEntity userDetailsRes) {

        String userDetailsStr = "";
        boolean isLogin = false;


        if (userDetailsRes != null) {
            storeStringValue(context, AppConstants.USER_ID, userDetailsRes.getUser_id());
            userDetailsStr = new Gson().toJson(userDetailsRes, UserDetailsEntity.class);
            isLogin = true;
        }
        storeValueToPreference(context, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.LOGIN_STATUS, isLogin);
        storeStringValue(context, AppConstants.USER_DETAILS, userDetailsStr);


    }

    public static UserDetailsEntity getUserDetailsRes(Context context) {
        UserDetailsEntity userDetailsEntityRes = new UserDetailsEntity();

        String userDetailsStr = getStringValue(context, AppConstants.USER_DETAILS);
        if (userDetailsStr != null) {
            userDetailsEntityRes = new Gson().fromJson(userDetailsStr, UserDetailsEntity.class);
        }
        return userDetailsEntityRes;
    }

    public static String getUserID(Context context) {
        return getStringValue(context, AppConstants.USER_ID);

    }


    public static void storeStringValue(Context context, String appConstantsVal, String key) {
        storeValueToPreference(context,
                STRING_PREFERENCE,
                appConstantsVal, key);
    }

    public static String getStringValue(Context context, String key) {
        return (String) getValueFromPreference(context,
                STRING_PREFERENCE, key);
    }

    public static boolean isLoggedIn(Context context) {
        return (boolean) getValueFromPreference(context,
                BOOLEAN_PREFERENCE, AppConstants.LOGIN_STATUS);
    }

    public static String getMsgDecoder(String txtMsg) {

        try {
            txtMsg = URLDecoder.decode(txtMsg, "UTF-8");
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }

        return txtMsg;
    }

    public static String setMsgEncoder(String txtMsg) {

        try {

            txtMsg = URLEncoder.encode(txtMsg, "UTF-8");
            txtMsg = txtMsg.replace("+", "%20");

        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }

        return txtMsg;
    }

    public static String getCustomDateFormat(String inputDateStr, SimpleDateFormat targetDateFormat) {
        String returnDateFormat = "";
        if (!inputDateStr.isEmpty()) {
            SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            try {
                serverDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dateObj = serverDateFormat.parse(inputDateStr);
//                targetDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                targetDateFormat.setTimeZone(TimeZone.getDefault());
                returnDateFormat = targetDateFormat.format(dateObj);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, e.toString());
            }
        }
        return returnDateFormat;
    }

    public static String getCurrentDate() {
        String formattedDate = "";
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            serverDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            formattedDate = serverDateFormat.format(c.getTime());
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }
        return formattedDate;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            //noinspection deprecation
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void shareText(Context context, String shareStr) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.video_link) + " " + shareStr);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static void setBadge(Context mContext, int count) {
        if (count != 0) {
            String launcherClassName = getLauncherClassName(mContext);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", mContext.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            mContext.sendBroadcast(intent);
        }
    }

    private static String getLauncherClassName(Context mContext) {

        PackageManager pm = mContext.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(mContext.getPackageName())) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }

    public static String convertLocalUTCtime(Context context, String time) {

        String formattedDate = "";
        try {
            SimpleDateFormat convertFormat;
            boolean is24HourFormat = android.text.format.DateFormat
                    .is24HourFormat(context);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(time);
            if (is24HourFormat) {
                convertFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
            } else {
                convertFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
            }
            convertFormat.setTimeZone(TimeZone.getDefault());
            formattedDate = convertFormat.format(date);
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.toString());
        }

        return formattedDate;
    }

    public static String getTimeDifference(Context context, Date startDate, Date endDate) {

        long different = startDate.getTime() - endDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        String remainTime = context.getString(R.string.just_now);

        if (elapsedDays > 0) {
            remainTime = elapsedDays + " " + (elapsedDays == 1 ? context.getString(R.string.day_ago) : context.getString(R.string.days_ago));
        } else if (elapsedHours > 0) {
            remainTime = elapsedHours + " " + (elapsedHours == 1 ? context.getString(R.string.hour_ago) : context.getString(R.string.hours_ago));
        } else if (elapsedMinutes > 0) {
            remainTime = elapsedMinutes + " " + (elapsedMinutes == 1 ? context.getString(R.string.min_ago) : context.getString(R.string.minutes_ago));
        } else if (elapsedSeconds > 0) {
            remainTime = elapsedSeconds + " " + context.getString(R.string.sec_ago);
        }
        return remainTime;

    }


    public static String getYoutubeVideoID(String videoLink) {

        String vId = "";
        Uri mUrlNew = Uri.parse(videoLink);
        Set<String> paramNames = mUrlNew.getQueryParameterNames();
        for (String key : paramNames) {
            vId = mUrlNew.getQueryParameter("v");
        }
        if (vId.isEmpty()) {
            Pattern pattern = Pattern.compile(
                    "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(videoLink);
            if (matcher.matches()) {
                vId = matcher.group(1);
            }
        }

        return vId;
    }

    public static String getVimeoVideoID(String videoLink) {

        String vId = "";
        Uri mUrlNew = Uri.parse(videoLink);
        Set<String> paramNames = mUrlNew.getQueryParameterNames();
        for (String key : paramNames) {
            vId = mUrlNew.getQueryParameter("video");
        }
        if (vId.isEmpty()) {
            Pattern pattern = Pattern.compile(
//                    "\"^https?://.*(?:player.vimeo/\\w/video=)([^#&?]*).*$",
                    "^https?://.*(?:vimeo/|videos/|u/\\w/|embed/)([^#&?]*).*$",
//                    "(https?:\/\/)?(www\.)?(player\.)?vimeo\.com\/([a-z]*\/)*([‌​0-9]{6,11})[?]?.*",
//                    "/(https?:\/\/)?(www\.)?(player\.)?vimeo\.com\/([a-z]*\/)*([0-9]{6,11})[?]?.*/",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(videoLink);
            if (matcher.matches()) {
                vId = matcher.group(1);
            }
        }
        if (vId.isEmpty()) {

            String[] video = videoLink.split("/");
            vId = video[video.length - 1];
        }

        return vId;
    }

    public static String getYoutubeTitle(String youtubeUrl) {
        try {
            if (youtubeUrl != null) {
                URL embeddedURL = new URL("https://www.youtube.com/oembed?url=https://www.youtube.com/watch?v=" + youtubeUrl + "&format=json");

                return new JSONObject(IOUtils.toString(embeddedURL)).getString("title");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

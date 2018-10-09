package com.smaat.virtualtrainer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;


public class GlobalMethods {

    private static int STRING_PREFERENCE = 1;
    private static int INT_PREFERENCE = 2;
    private static int BOOLEAN_PREFERENCE = 3;

    private static void storeValuetoPreference(Context context, int preference,
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
        edit.apply();
//        edit.commit();

    }

    private static Object getValueFromPreference(Context context,
                                                 int preference, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(
                AppConstants.shared_pref_name, Context.MODE_PRIVATE);

        if (preference == STRING_PREFERENCE) {
            return sharedPreference.getString(key, "");
        }
        if (preference == INT_PREFERENCE) {
            return  sharedPreference.getInt(key, 0);
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


    public static void userDetails(String userStatus, String userId, String
            userName, String emailId, String password, String loginType, Context context) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_STATUS,
                userStatus);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID, userId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_NAME,
                userName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
                emailId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD,
                password);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.LOGIN_TYPE, loginType);

    }


    public static String getUserID(Context context) {

        return (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID);

    }

    public static void storeStringValue(Context context, String appConstantsVal, String key) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                appConstantsVal, key);

    }

    public static String getStringValue(Context context, String key) {

        return (String) getValueFromPreference(context,
                STRING_PREFERENCE, key);

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


    public static void resetExplosionFields(View root) {

        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                resetExplosionFields(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

}

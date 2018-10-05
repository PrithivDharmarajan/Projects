package com.bridgellc.bridgeqr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalMethods {

    private static int STRING_PREFERENCE = 1;
    private static int INT_PREFERENCE = 2;
    private static int BOOLEAN_PREFERENCE = 3;
    public static int ARRAY_LIST_PREFERENCE = 4;
    public static int LONG_PREFERENCE = 5;

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
        edit.commit();

    }

    private static Object getValueFromPreference(Context context,
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
        return null;

    }


    public static boolean isEmailValid(String email, boolean eduMail) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {

            if (eduMail) {
                if (email.endsWith(".edu"))
                    isValid = true;
            } else {
                isValid = true;
            }
        }
        return isValid;
    }


    public static void userDetails(boolean logined, String userId, String firstName, String lastName, String emailId, String phoneNum, String password,
                                   String universityName, String universityId,
                                   String dob, String gender, String loginType, Context context) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
                logined);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID, userId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.FIRST_NAME,
                firstName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.LAST_NAME,
                lastName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
                emailId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.PHONE_NUM, phoneNum);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD,
                password);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_UNIVERSITY_NAME, universityName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_UNIVERSITY_ID, universityId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.DOB, dob);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.GENDER, gender);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.LOGINTYPE, loginType);

    }

    public static String getStringValue(Context context, String key) {

        return (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, key);


    }

    public static boolean isLoggedIn(Context context) {
        return (Boolean) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN);
    }
}

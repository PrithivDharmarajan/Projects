package com.e2infosystems.activeprotective.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.e2infosystems.activeprotective.output.model.LoginResponse;
import com.google.gson.Gson;


public class PreferenceUtil {

    private static int STRING_PREFERENCE = 1;
    private static int INT_PREFERENCE = 2;
    private static int BOOLEAN_PREFERENCE = 3;

    /*store preference*/
    private static void storeValueToPreference(Context context, int preference, String key, Object value) {
        if (context != null) {
            SharedPreferences sharedPreference = context.getSharedPreferences(
                    AppConstants.SHARE_PREFERENCE, Context.MODE_PRIVATE);
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

            if (android.os.Build.VERSION.SDK_INT >= 23) {
                edit.apply();
            } else {
                edit.commit();
            }

        }
    }

    /*get preference value*/
    private static Object getValueFromPreference(Context context, int preference, String key) {
        if (context != null) {
            SharedPreferences sharedPreference = context.getSharedPreferences(
                    AppConstants.SHARE_PREFERENCE, Context.MODE_PRIVATE);

            if (preference == STRING_PREFERENCE) {
                return sharedPreference.getString(key, "");
            }
            if (preference == INT_PREFERENCE) {
                return sharedPreference.getInt(key, 0);
            }
            if (preference == BOOLEAN_PREFERENCE) {
                return sharedPreference.getBoolean(key, false);
            }
        }

        return null;
    }


    /*Store bool value to preference*/
    public static void storeBoolPreferenceValue(Context context, String appConstantsStr, boolean keyBool) {
        if (context != null) {
            storeValueToPreference(context, BOOLEAN_PREFERENCE, appConstantsStr, keyBool);
        }
    }

    /*Get bool value from preference*/
    public static boolean getBoolPreferenceValue(Context context, String appConstantsStr) {
        return (boolean) getValueFromPreference(context,
                BOOLEAN_PREFERENCE, appConstantsStr);
    }


    /*Store String value to preference*/
    public static void storeStringPreferenceValue(Context context, String appConstantsVal, String key) {
        if (context != null) {
            storeValueToPreference(context, STRING_PREFERENCE, appConstantsVal, key);
        }
    }

    /*Get String value from preference*/
    public static String getStringPreferenceValue(Context context, String key) {
        return (String) getValueFromPreference(context, STRING_PREFERENCE, key);
    }

    /*Store user details to preference*/
    public static void storeUserDetails(Context context, LoginResponse userDetailsEntity) {
        String userDetailStr = "";

        Gson gson = new Gson();
        userDetailStr = gson.toJson(userDetailsEntity);

        /*Store login status*/
        storeBoolPreferenceValue(context, AppConstants.LOGIN_STATUS, !userDetailsEntity.getAccessToken().isEmpty());

        /*Store access token*/
        storeStringPreferenceValue(context, AppConstants.AUTHORIZATION_TOKEN, userDetailsEntity.getAccessToken());

        /*Store user name*/
        storeStringPreferenceValue(context, AppConstants.USER_NAME, userDetailsEntity.getUserName());

        /*Store community id*/
        storeStringPreferenceValue(context, AppConstants.COMMUNITY_ID, userDetailsEntity.getCommunityId());

        /*Store user details*/
        storeValueToPreference(context, PreferenceUtil.STRING_PREFERENCE,
                AppConstants.USER_DETAILS, userDetailStr);


    }

    /*Get user details from preference*/
    public static LoginResponse getUserDetails(Context context) {
        LoginResponse userDetailsEntityRes = new LoginResponse();

        String userDetailsStr = getStringPreferenceValue(context, AppConstants.USER_DETAILS);

        if (userDetailsStr != null && !userDetailsStr.isEmpty()) {
            userDetailsEntityRes = new Gson().fromJson(userDetailsStr, LoginResponse.class);
        }

        return userDetailsEntityRes;
    }

    /*Get authorization token from preference*/
    public static String getAuthorizationToken(Context context) {
        return  getStringPreferenceValue(context, AppConstants.AUTHORIZATION_TOKEN);
    }

    /*Get user name from preference*/
    public static String getUserName(Context context) {
        return  getStringPreferenceValue(context, AppConstants.USER_NAME);
    }
}

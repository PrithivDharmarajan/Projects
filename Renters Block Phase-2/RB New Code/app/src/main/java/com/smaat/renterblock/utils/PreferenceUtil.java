package com.smaat.renterblock.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.smaat.renterblock.entity.AgentFilterLocalEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.entity.UserDetailsEntity;

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


    public static void storeUserDetails(Context context, UserDetailsEntity userDetailsRes) {

        String userDetailsStr = "", userIDStr = AppConstants.FAILURE_CODE;


        if (userDetailsRes != null) {
            userIDStr = userDetailsRes.getUser_id();
            userDetailsStr = new Gson().toJson(userDetailsRes, UserDetailsEntity.class);
        }

        storeStringValue(context, AppConstants.USER_ID, userIDStr);
        storeStringValue(context, AppConstants.USER_DETAILS, userDetailsStr);
    }

    public static UserDetailsEntity getUserDetailsRes(Context context) {
        UserDetailsEntity userDetailsEntityRes = new UserDetailsEntity();

        String userDetailsStr = getStringValue(context, AppConstants.USER_DETAILS);
        if (userDetailsStr != null && !userDetailsStr.isEmpty()) {
            userDetailsEntityRes = new Gson().fromJson(userDetailsStr, UserDetailsEntity.class);
        }
        return userDetailsEntityRes;
    }

    public static String getUserID(Context context) {
        return getStringValue(context, AppConstants.USER_ID).isEmpty() ? AppConstants.FAILURE_CODE : getStringValue(context, AppConstants.USER_ID);
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


    public static void storeStringValue(Context context, String appConstantsVal, String key) {
        if (context != null) {
            storeValueToPreference(context,
                    STRING_PREFERENCE,
                    appConstantsVal, key);
        }
    }

    public static String getStringValue(Context context, String key) {
        return (String) getValueFromPreference(context,
                STRING_PREFERENCE, key);
    }

    /*Storing Filter Objects*/
    public static void storeFilterObject(Context context, FilterEntity rentFilterEntity, FilterEntity saleFilterEntity) {

        if (rentFilterEntity != null) {
            storeStringValue(context, AppConstants.RENT_FILTER, new Gson().toJson(rentFilterEntity));
        }

        if (saleFilterEntity != null) {
            storeStringValue(context, AppConstants.SALE_FILTER, new Gson().toJson(saleFilterEntity));

        }

    }

    /*Retriving Filter values*/
    public static FilterEntity getRentFilterValues(Context context) {
        FilterEntity filterEntity = new FilterEntity();
        String rentStr = getStringValue(context, AppConstants.RENT_FILTER);
        if (rentStr != null && !rentStr.isEmpty()) {
            filterEntity = new Gson().fromJson(rentStr, FilterEntity.class);
        }

        return filterEntity;
    }

    public static FilterEntity getSaleFilterValues(Context context) {
        FilterEntity filterEntity = new FilterEntity();
        String saleStr = getStringValue(context, AppConstants.SALE_FILTER);
        if (saleStr != null && !saleStr.isEmpty()) {
            filterEntity = new Gson().fromJson(saleStr, FilterEntity.class);
        }

        return filterEntity;
    }

    /*Storing agent filter detail*/
    public static void storeAgentFilterDetail(Context context,AgentFilterLocalEntity entity){

        if (entity != null) {
            storeStringValue(context, AppConstants.AGENT_FILTER_ENTITY, new Gson().toJson(entity));
        }

    }

    /*Retriving agent filter details*/
    public static AgentFilterLocalEntity getAgentFilterDetail(Context context){
        AgentFilterLocalEntity entity=new AgentFilterLocalEntity();
        String filterStr=getStringValue(context,AppConstants.AGENT_FILTER_ENTITY);
        if(filterStr!=null && !filterStr.isEmpty()){
            entity=new Gson().fromJson(filterStr, AgentFilterLocalEntity.class);
        }
        return entity;
    }
}

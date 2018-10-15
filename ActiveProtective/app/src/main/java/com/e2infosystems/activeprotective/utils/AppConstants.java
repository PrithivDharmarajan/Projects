package com.e2infosystems.activeprotective.utils;


import com.e2infosystems.activeprotective.input.model.AddBeltEntity;

import java.util.ArrayList;

public class AppConstants {

    /*Tag*/
    public static String TAG = "TAG";
    static final String SHARE_PREFERENCE = "SHARE_PREFERENCE";
    public static final int SUCCESS_CODE = 1;

    /*Share Preference Store Tag*/
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    public static String CURRENT_USER_ADMIN = "CURRENT_USER_ADMIN";
    public static String USER_DETAILS = "USER_DETAILS";
    public static String USER_NAME = "USER_NAME";
    public static String AUTHORIZATION_TOKEN = "AUTHORIZATION_TOKEN";
    public static String COMMUNITY_ID = "COMMUNITY_ID";

    /*Previous Screen*/
    public static ArrayList<String> PREVIOUS_SCREEN = new ArrayList<>();

    /*Screen name for clear the hole activity*/
    public static final String GENERAL_WELCOME = "GeneralWelcome";
    public static final String ADMIN_WELCOME = "AdminWelcome";
    public static final String BELT_LIST = "BeltList";

    /*BASE URL*/
    public static final String BASE_URL = "https://18.222.253.58:3009/api/";
//    public static final String BASE_URL = "https://13.58.210.186:3009/api/";

    /*Temp*/
    public static String TEMP_SCREEN = "TEMP_SCREEN";

    /*Store*/
    public static AddBeltEntity BELT_DETAILS = new AddBeltEntity();
    public static String BELT_DEVICE_ID = "";
    public static boolean IS_FROM_BELT_LIST_BOOL = false;
    public static boolean IS_FROM_USER_FlOW_BOOL = false;

}


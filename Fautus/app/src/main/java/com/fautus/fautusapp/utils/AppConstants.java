package com.fautus.fautusapp.utils;

import android.annotation.SuppressLint;

import com.fautus.fautusapp.entity.MomentPhotoEntity;
import com.fautus.fautusapp.entity.PhotoEntity;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.sendbird.android.GroupChannel;

import java.util.ArrayList;

public class AppConstants {

    static final String SHARE_PREFERENCE = "SHARE_PREFERENCE";

    /*Status Code*/
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";

    /*Tag*/
    public static final String TAG = "TAG";

    /*Splash and SignUP*/
    public static String CONSUMER_WELCOME_SCREEN_SEEN = "CONSUMER_WELCOME_SCREEN_SEEN";
    public static String PHOTOGRAPHER_WELCOME_SCREEN_SEEN = "PHOTOGRAPHER_WELCOME_SCREEN_SEEN";
    public static String WELCOME_SCREEN_TYPE = "WELCOME_SCREEN_TYPE";

    /*User Details*/
    public static String USER_ID = "USER_ID";
    public static String USER_IS_CONSUMER = "USER_IS_CONSUMER";
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    public static String PARSE_DEVICE_ID = "PARSE_DEVICE_ID";
    public static String SEND_BIRD_DEVICE_ID = "SEND_BIRD_DEVICE_ID";


    /*Chat Screen*/
    public static String SHOW_CON_PHOTO_DIALOG = AppConstants.FAILURE_CODE;
    public static String MOMENT_UPLOAD_FROM_CHAT = AppConstants.FAILURE_CODE;
    static String CONSUMER_CHAT_DETAILS = null;
    public static ParseObject CHAT_PHOTOGRAPHER_DETAILS = null;
    public static ParseObject CHAT_CONSUMER_DETAILS = null;
    public static ParseObject CHAT_MOMENT_DETAILS = null;

    /*Moment Screen*/
    public static MomentPhotoEntity MOMENT_PHOTO_ENTITY = null;
    public static PhotoEntity MOMENT_PHOTO_FILE = null;
    public static ArrayList<PhotoEntity> MOMENT_SELECTED_PHOTO_FILE = null;
    public static String MOMENT_PAY_AMT = AppConstants.FAILURE_CODE;
    public static String MOMENT_ALREADY_BOUGHT = AppConstants.FAILURE_CODE;
    public static String MOMENT_FROM_MENU = AppConstants.FAILURE_CODE;

    /*Slide Menu*/
    public static String PROFILE_FROM_MENU = AppConstants.FAILURE_CODE;
    public static String PHOTOGRAPHER_FROM_MENU = AppConstants.FAILURE_CODE;
    public static String MOMENT_FROM_LIST = AppConstants.FAILURE_CODE;

    /*Terms and Condition*/
    public static String TERMS_COND_SCREEN = AppConstants.SUCCESS_CODE;


    /*Settings Screen*/
    public static String SETTINGS_STRIP_ON = "SETTINGS_STRIP_ON";
    public final static String HOME_SCREEN = "HomeScreen";

    /*API*/

    /*Parse Domain*/
   /*Live Key*/
    public static String PARSE_DOMAIN = "https://young-tundra-37252.herokuapp.com/parse/";
    /*Test Key*/
//    public static String PARSE_DOMAIN = "https://fautus-sandbox.herokuapp.com/parse/";

    public static final String API_EMAIL_CHECK = "https://api.mailgun.net/v2/address/validate?address=%1$s&api_key=pubkey-cfb7ee5c3dda2d898e19c4b1c971fa71";

    /*Mail Gun*/
    public static final String MAIL_GUN_KEY = "key-1144a7112671bf11f641392d9c5c4cc5";
    public static final String MAIL_GUN_FROM = "Fautus <postmaster@www.fautus.com>";
    @SuppressLint("AuthLeak")
    public static final String MAIL_GUN_MAIL_URL = "https://api.mailgun.net/v3/www.fautus.com/messages";

    /*Zen desk*/
    public static final String ZEN_DESK_APP_ID = "3134184f516cdaa55483b13188d321d4b151d6029bbc5a70";
    public static final String ZEN_DESK_URL = "https://fautus.zendesk.com";
    public static final String ZEN_DESK_CLIENT_ID = "mobile_sdk_client_0acf43ac0c138c996146";
    public static final String SEND_BIRD_LIVE_APP_ID = "8FFAB4F4-51C6-40DF-B538-8DFB742CD2FC";

    /*Strip*/

    /*Live Key*/
    public static final String STRIP_SECRET_KEY = "";
    /*Test Key*/
//    public static final String STRIP_SECRET_KEY = "";
    /*Live Key*/
    public static final String STRIP_PUBLISH_KEY = "";
    /*Test key*/
//    public static final String STRIP_PUBLISH_KEY = "";
    public static final String STRIP_COUNTRY_CODE = "US";
    public static final String STRIP_CURRENCY_CODE = "usd";
    public static final String API_STRIP_CARD = "https://api.stripe.com/v1/customers/%1$s";
    static final String STRIP_LEGAL_WEB_URL = "https://stripe.com/us/legal";
    public static final String BEARER = "Bearer";


    /*Youtube test video id*/
    public static final String YOUTUBE_VIDEO_IMAGE_URL = "http://img.youtube.com/vi/%1$s/0.jpg";
    public static final String YOUTUBE_VIDEO_LD = "elovDGyRN7o";
    public static final String BASIC = "Basic";

    /*Moment pay screen*/
    public static String READY_TO_PAY_SOURCE = AppConstants.FAILURE_CODE;
    public static boolean PHOTO_ALREADY_PURCHASED_BOOL = false;

    /*MomentUpload*/
    public static String PHOTO_NEW_UPLOAD = AppConstants.FAILURE_CODE;

    /*Header right click*/
    public static String HEADER_RIGHT_CLICK = AppConstants.FAILURE_CODE;

    /*Photographer bank acc details*/
    public static String PHOTOGRAPHER_AVAILABLE_BAL_AMT = AppConstants.FAILURE_CODE;
    public static String PHOTOGRAPHER_PENDING_BAL_AMT = AppConstants.FAILURE_CODE;
    public static String PHOTOGRAPHER_TOTAL_BAL_AMT = AppConstants.FAILURE_CODE;

    /*Google map location*/
    public static String PUSH_MOMENT_ID = null;
    public static String PUSH_CHAT_STATUS = null;

    public static ParseGeoPoint USER_MAP_LOC = new ParseGeoPoint();

    public static GroupChannel SEND_BIRD_GROUP_CHANNEL = null;
    public static boolean PHOTOGRAPHER_AVA = true;

}


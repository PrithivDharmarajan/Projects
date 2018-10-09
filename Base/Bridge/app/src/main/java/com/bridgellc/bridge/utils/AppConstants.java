package com.bridgellc.bridge.utils;

import android.net.Uri;

import com.paypal.android.sdk.payments.PayPalConfiguration;


public class AppConstants {
    public static final String shared_pref_name = "Base_Preference";


    /*Status Code*/
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";
    public static String UPLOAD_DATA = "UPLOAD_DATA";
    /*User Details*/
    public static String USER_ID = "USER_ID";
    public static String ISLOGGEDIN = "ISLOGGEDIN";
    public static String ISUSERVERIFIED = "ISUSERVERIFIED";
    public static String FIRST_NAME = "FIRST_NAME";
    public static String LAST_NAME = "LAST_NAME";
    public static String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public static String EDU_EMAIL_ADDRESS = "EDU_EMAIL_ADDRESS";
    public static String PASSWORD = "PASSWORD";
    public static String USER_UNIVERSITY_NAME = "USER_UNIVERSITY_NAME";
    public static String USER_UNIVERSITY_ID = "USER_UNIVERSITY_ID";
    public static String PHONE_NUM = "PHONE_NUM";
    public static String DOB = "DOB";
    public static String GENDER = "GENDER";
    public static String LOGINTYPE = "LOGINTYPE";
    public static String CARD_DETAILS = "CARD_DETAILS";
    public static String TUTORIAL_SEEN = "TUTORIAL_SEEN";
    public static String PAYMENT_DETAILS = "PAYMENT_DETAILS";
    public static String BANK_DETAILS = "BANK_DETAILS";
    public static String PAYMENT_MODE = "PAYMENT_MODE";
    public static String PARTNER = "PARTNER";
    public static String UNIVERSITY_MODE = "UNIVERSITY_MODE";
    public static String BANK_ACC_DET_BACK_SCREEN = "BANK_ACC_DET_BACK_SCREEN";
    public static String CARD_DET_BACK_SCREEN = "CARD_DET_BACK_SCREEN";
    public static String FB_ID = "FB_ID";
    public static String BANK_FROMLIST_BACK = "BANK_FROMLIST_BACK";
    /**
     * Push Notification
     */
    public static final String SENDER_ID = "788467169851";
    public final static String DEVICE_ID = "DEVICE_ID";
    public final static String mDEVICEPLATFORM = "2";
    public final static String HOME_SCREEN = "HomeScreenActivity";
    public final static String SEARCH_SCREEN = "SearchScreenActivity";
    public final static String DASHBOARD_SCREEN = "DashboardScreen";
    public final static String NOTIFICATION_SCREEN = "NotificationScreen";
    public final static String SETTINGS_SCREEN = "SettingsScreen";
    public final static String SING_IN_SCREEN = "SignInScreen";
    public static String TYPE_OF_NOTIFICATION = "Type_of_notification";
    public static String BACK_FB = "FB";

    /**
     * App Inside Temp Values
     */
    public static String PROFILE_HEADER = "PROFILE_HEADER";
    public static String SELECT_SCH_UNI = "SELECT_SCH_UNI";
    public static String SELECT_SCH_UNI_ID = "SELECT_SCH_UNI_ID";
    public static String RATING_BTN = "RATING_BTN";
    public static String SIGNUPVERIFYBACK = "SIGNUPVERIFYBACK";
    public static String CODE_SCREEN = "CODE_SCREEN";
    public static String PAYPAL_DOLL = "PAYPAL_DOLL";


    public static String SIGNUP_FIRSTNAME = "SIGNUP_FIRSTNAME";
    public static String SIGNUP_LASTNAME = "SIGNUP_LASTNAME";
    public static String SIGNUP_EMAIL = "SIGNUP_EMAIL";
    public static String SIGNUP_PWD = "SIGNUP_PWD";
    public static String SIGNUP_DIRECT = "SIGNUP_DIRECT";

    /**
     * API URL
     */


    //AWS Orginal URL
//    public static final String BASE_URL = "http://www.baseapp.us/bridge_api/index.php/";


    //AWS Orginal BASE URL
    public static final String BASE_URL = "http://www.baseapp.us/bridge_api_test/index.php/";
//
//    public static final String BASE_URL = "http://52.27.187.84/base_api/index.php/";

    //AWS Orginal BASE URL
//    public static final String BASE_URL = "http://www.baseapp.us/bridge_api_dev/index.php/";


    //Dropbox API
//    public static final String DROPBOX_APP_KEY = "uioa51rmo9d9y8u";

    //
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX; 
    //Test Acc
    public static final String CONFIG_CLIENT_ID = "";
    public static final int REQUEST_PAYPAL_PAYMENT = 1;
    public static final PayPalConfiguration CONFIG = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT).acceptCreditCards(false)
            .clientId(CONFIG_CLIENT_ID).rememberUser(true)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Bridge LLC")
            .merchantPrivacyPolicyUri(Uri.parse("http://52.27.187.84/terms.html"))
            .merchantUserAgreementUri(Uri.parse("http://52.27.187.84/terms.html"));

    public static String PAYPAL_NOTIFY = "PAYPAL_NOTIFY";


    //Negation Chat ID
    public static String NEGO_USER_ID = "NEGO_USER_ID";
    public static String NEGO_FRIEND_ID = "NEGO_FRIEND_ID";
    public static String NEGO_BID_ID = "NEGO_BID_ID";
    public static String NEGO_ITEM_ID = "NEGO_ITEM_ID";
    public static String NEGO_ITEM_QTY = "NEGO_ITEM_QTY";
    public static String NEGO_ITEM_NOTI = "NEGO_ITEM_NOTI";
    public static String FROME_NOTIFICATION = "FROME_NOTIFICATION";
    public static String PRODUCT_DETAILS_BACK = "PRODUCT_DETAILS_BACK";


    //Normal Chat ID
    public static String CHAT_BACK = "CHAT";
    public static String CHAT_FRIEND_ID = "CHAT_FRIEND_ID";
    public static String CHAT_ITEM_ID = "CHAT_ITEM_ID";

    //Rating screen
    public static String RATING_USER_ID = "RATING_USER_ID";
    public static String RATING_USER_NAME = "RATING_USER_NAME";
    public static String RATING_ITEM_ID = "RATING_ITEM_ID";
    public static String RATING_BACK = "RATING_BACK";
    public static String RATING_RATE = "RATING_RATE";
    public static String RATING_CMD = "RATING_CMD";

    //Payment screen
    public static String CARD_NUMBER = "";
    public static String CARD_NUMBER_WITHX = "";
    public static String CARD_NAME = "";
    public static String EXPIRE_DATE = "";
    public static String SECURITY_CODE = "";
    public static String PAYMENT_TYPE = "";
    public static String CAED_ID = "";


    public static String PAYPAL_FIRSTNAME = "";
    public static String PAYPAL_LASTNAME = "";
    public static String PAYPAL_EMAIL = "";
    public static String PAYPAL_ID = "";


    public static String PAYPAL_BUY_ID = "PAYPAL_BUY_ID";
    public static String PAYPAL_USER_ID = "PAYPAL_USER_ID";
    public static String PAYPAL_USER_NAME = "PAYPAL_USER_NAME";
    public static String PAYPAL_ITEM_ID = "PAYPAL_ITEM_ID";
    public static String PAYPAL_ITEM_TYPE = "PAYPAL_ITEM_TYPE";
    public static String PAYPAL_ITEM_DELV_TYPE = "PAYPAL_ITEM_DELV_TYPE";
    public static String PAYPAL_ITEM_NAME = "PAYPAL_ITEM_NAME";
    public static String PAYPAL_ITEM_COST = "PAYPAL_ITEM_COST";
    public static String PAYPAL_ITEM_QTY = "PAYPAL_ITEM_QTY";
    public static String PAYPAL_TOT_COST = "PAYPAL_TOT_COST";
    public static String PAYPAL_SER_FEES = "PAYPAL_SER_FEES";
    public static String PAYPAL_NEG_ID = "PAYPAL_NEG_ID";
    public static String PAYPAL_NEG = "PAYPAL_NEG";
    public static String PAYPAL_BID_ID = "PAYPAL_BID_ID";
    public static String PAYPAL_TIPS = "PAYPAL_TIPS";


    public static final String PARTNER_EMAIL_ID = "support@gobaseapp.com";

    public static final String BASE_SCANNER_APP = "play.google.com/store/apps/details?id=com" +
            ".bridgellc.bridgeqr&hl=en";

    public static final String BASE_APP = "https://play.google.com/store/apps/details?id=com.bridgellc.bridge";
    public static final String IOS_BASE_APP = "https://itunes.apple.com/us/app/base-buy-and-sell-everything/id1129655028?mt=8";


}


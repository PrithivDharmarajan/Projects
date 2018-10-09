package com.smaat.spark.utils;

public class AppConstants {
    static final String shared_pref_name = "SPARK_PREFERENCE";

    /*Status Code*/
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";

    /*Database SQLite*/
    public static final String DATABASE_NAME = "SPARK_SQL_DB";
    public static final int DATABASE_VERSION = 1;


    /*Youtube Key*/
    public static final String YOUTUBE_API_KEY = "AIzaSyCMIp7iY2Q-ztkcTx9zQmR3TgzlHAOi83c";
    public static String YOUTUBE_VIDEO_ID = "YOUTUBE_VIDEO_ID";
    public static  int BOTTOM_BUTTON_POS = 0;

    /*Tag*/
    public static final String DIALOG_TAG = "DIALOG_TAG";
    public static final String TAG = "SPARK_TAG";

    /*User Details*/
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    static String USER_ID = "USER_ID";
    //    public static String OTHER_USER_ID = "OTHER_USER_ID";
    static String USER_DETAILS = "USER_DETAILS";
    public static String OTHER_USER_DETAILS = "OTHER_USER_DETAILS";
    public static String DEVICE_ID = "DEVICE_ID";

    /*Chat Details*/
    public static String CHAT_DISTANCE = "CHAT_DISTANCE";
    public static String CHAT_SCREEN_TITLE = "CHAT_SCREEN_TITLE";
    public static String CHAT_SUB = "CHAT_SUB";
    public static String CHAT_ID = "CHAT_ID";
    public static String CHAT_FRIEND_ID = "CHAT_FRIEND_ID";
    public static String CHAT_FRIEND_NAME = "CHAT_FRIEND_NAME";
    public static String CHAT_SUB_FRIEND = "SPARK_FRIEND";
    public static String CHAT_BACK_PRESSED = "CHAT_BACK_PRESSED";
    public static final String VIMEO_IMG_API = "http://vimeo.com/api/oembed.json?url=http%3A//vimeo.com/";


    /*Discover*/
    public static String DISCOVER_VIDEO_LINK = "DISCOVER_VIDEO_LINK";
    public static String DISCOVER_VIDEO_NAME = "DISCOVER_VIDEO_NAME";
    public static String VIDEO_SCREEN = "VIDEO_SCREEN";
    public static String HOME_FRAG_POS = "1";

    /*Invite Friends*/
    public static String INVITE_ALL_USER = "INVITE_ALL_USER";


    /*Settings API call*/
    public static String SETTINGS_BACK = "SETTINGS_BACK";
    public static String SETTINGS_ANONYMOUS = "SETTINGS_ANONYMOUS";
    public static String SETTINGS_PUSH = "SETTINGS_PUSH";
    public static String SETTINGS_HIDE_LOC = "SETTINGS_HIDE_LOC";

    /*Base URL*/
    public static final String BASE_URL = " http://50.87.171.216/";

    public static final String GET_ADDRESS_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$s&sensor=true";
    public static final String ANDROID_SPARK_APP = "https://play.google.com";
    public static final String IOS_SPARK_APP = "https://itunes.apple.com/us";

    /*Push Notification*/
    public static String NOTIFICATION_DATA = "NOTIFICATION_DATA";
    public static String TYPE_OF_NOTIFICATION = "TYPE_OF_NOTIFICATION";

    /*API Params*/

    /*Login API*/
    public static final String API_LOGIN = "login";
    public static final String PARAMS_LOGIN = "[\"username\", \"password\",\"lattitude\", \"longitude\", \"address\",\"device_type\", \"device_token\"]";

    /*Registration API*/

    public static final String API_REGISTRATION = "registration";
    public static final String PARAMS_REGISTRATION = "[\"email_id\", \"password\", \"username\", \"lattitude\", \"longitude\", \"address\",\"device_type\", \"device_token\"]";

    /*ResetPassword Screen*/
    public static final String API_RESET_PWD = "forgot_password";
    public static final String PARAMS_EMAIL_ID = "[\"email_id\"]";

    /*Chat Connect API*/
    public static final String API_CHAT_CONNECT = "connect";
    public static final String PARAMS_CHAT_CONNECT = "[\"user_id\",\"search_with_distance\",\"subject\",\"distance\",\"lat\",\"lon\",\"random\",\"anonymous\"]";

    /*Send API*/
    public static final String API_CHAT_SEND = "send_chat";
    public static final String PARAMS_CHAT_SEND = "[\"user_id\",\"friend_id\",\"message\",\"subject\"]";

    /*Receive Chat API*/
    public static final String API_CHAT_RECEIVE = "receive_chat";
    public static final String PARAMS_CHAT_RECEIVE = "[\"user_id\",\"friend_id\",\"subject\",\"max_chat_id\"]";

    /*DisConnect Chat API*/
    public static final String API_CHAT_DISCONNECT = "disconnect";
    public static final String PARAMS_CHAT_DISCONNECT = "[\"user_id\"],\"anonymous\"]";
    public static final String PARAMS_USER_ID = "[\"user_id\"]";


    /*Get Trends Chat API*/
    public static final String API_TRENDS = "getTrends";

    /*DisCover API*/
    public static final String API_DISCOVER = "getDiscoveryDetails";

    /*Get Friends List*/
    public static final String API_FRIEND_LIST = "getMyFriendsList";

    /*Get All User List*/
    public static final String API_ALL_USER_LIST = "get_all_users";

    /*Add Friend*/
    public static final String API_ADD_FRIEND = "addFriend";
    public static final String PARAMS_ADD_FRIEND = "[\"user_id\",\"friend_id\"]";


    /*Remove Friend*/
    public static final String API_REMOVE_FRIEND = "removeFriend";

    /*Login API*/
    public static final String API_CHANGE_NAME = "changeUsername";
    public static final String PARAMS_CHANGE_NAME = "[\"user_id\",\"username\"]";

    /*Update Password API*/
    public static final String API_UPDATE_PWD = "changePassword";
    public static final String PARAMS_UPDATE_PWD = "[\"user_id\",\"password\"]";

    /*Update Profile API*/
    public static final String API_UPDATE_PROFILE = "updateProfile";
    public static final String PARAMS_UPDATE_PROFILE = "[\"user_id\",\"email_id\",\"password\",\"username\",\"lattitude\"" +
            ",\"longitude\",\"address\",\"interests\",\"gender\",\"mainPicture\",\"otherPictures\"]";

    /*Remove Friend*/
    public static final String API_NOTIFICATION = "getNotification";

    /*Accept Friend Request*/
    public static final String API_ACCEPT_REQUEST = "acceptFriendRequest";
    public static final String PARAMS_ACCEPT_REQUEST = "[\"user_id\",\"friend_id\",\"status\"]";

    /*Block Friend*/
    public static final String API_BLOCK_FRIEND = "blockFriend";

    /*Ignore Friend*/
    public static final String API_IGNORE_FRIEND = "ignoreFriend";

    /*Read Notification*/
    public static final String API_READ_NOTIFICATION = "readNotification";
    public static final String PARAMS_READ_NOTIFICATION = "[\"user_id\",\"notification_id\"]";

    /*Background API*/
    public static final String API_BACKGROUND = "backgroundApi";

    /*Profile Pic API*/
    public final static String API_IMAGE_UPLOAD_MODE = "production";
    public final static String API_IMAGE_UPLOAD = "add_picture";

    /*Settings API*/
    public static final String API_UPDATE_SETTINGS = "updateSettings";
    public static final String PARAMS_UPDATE_SETTINGS = "[\"user_id\",\"private_account\",\"push_notifications\",\"hide_location\",\"anonymous\"]";

    /*Settings API*/
    public static final String API_DELETE_ACC = "deleteAccount";

    /*Settings API*/
    public static final String API_INVITE_FRIEND = "inviteFriend";
    public static final String PARAMS_INVITE_FRIEND = "[\"user_id\",\"email_id\"]";

    /*Settings API*/
    public static final String API_GET_INBOX = "get_my_inbox";

    /*Settings API*/
    public static final String API_DELETE_CHAT = "deletechat";
    public static final String PARAMS_DELETE_CHAT = "[\"user_id\",\"friend_id\",\"chat_id\"]";

}


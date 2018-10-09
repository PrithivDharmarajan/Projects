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
    public static String YOUTUBE_VIDEO_URL = "YOUTUBE_VIDEO_URL";
    public static int BOTTOM_BUTTON_POS = 0;

    /*Tag*/
    public static final String DIALOG_TAG = "DIALOG_TAG";
    public static final String TAG = "SPARK_TAG";
    public static final String APP_NAME = "SPARK";

    /*User Details*/
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    public static String TUTORIAL_SEEN = "TUTORIAL_SEEN";
    public static String CHAT_CONNECTED_STATUS = "CHAT_CONNECTED_STATUS";
    static String USER_ID = "USER_ID";
    static String USER_DETAILS = "USER_DETAILS";
    public static String OTHER_USER_DETAILS = "OTHER_USER_DETAILS";
    public static String DEVICE_ID = "DEVICE_ID";

    /*Chat Details*/
    public static String CHAT_DISTANCE = "CHAT_DISTANCE";
    public static String CHAT_SCREEN_TITLE = "CHAT_SCREEN_TITLE";
    public static String CHAT_ID = "CHAT_ID";
    public static String CHAT_FRIEND_ID = "CHAT_FRIEND_ID";
    public static String CHAT_FRIEND_NAME = "CHAT_FRIEND_NAME";
    public static String CHAT_CONNECT_FRIEND_ID = "CHAT_CONNECT_FRIEND_ID";
    public static String CHAT_CONNECT_FRIEND_NAME = "CHAT_CONNECT_FRIEND_NAME";
    public static String CHAT_CONNECT_SUB = "CHAT_CONNECT_SUB";
    public static String CHAT_CONNECT_ORIGINAL_SUB = "CHAT_CONNECT_ORIGINAL_SUB";
    public static String CHAT_SUB_FRIEND = "SPARK_FRIEND";
    public static String CHAT_BACK_PRESSED = "CHAT_BACK_PRESSED";
    public static String CHAT_DISCOVER = "CHAT_DISCOVER";
    public static String CHAT_FRIEND_FROM_CONNECT = "CHAT_FRIEND_FROM_CONNECT";
    public static String CHAT_USER_BACK = "CHAT_USER_BACK";

    public static String CHAT_PIC_FRIEND_ID = "CHAT_PIC_FRIEND_ID";
    public static String CHAT_PIC_LOCAL_PATH = "CHAT_PIC_LOCAL_PATH";
    public static String CHAT_PIC_TXT = "CHAT_PIC_LOCAL_PATH";
    public static String CHAT_PIC_BACK = "CHAT_PIC_BACK";
    public static String CHAT_PIC_SUB = "CHAT_PIC_SUB";
    public static String CHAT_PIC_MSG_FLOW = "CHAT_PIC_MSG_FLOW";
    public static String CHAT_PIC_TYPE = "CHAT_PIC_TYPE";

    /*Discover*/
    public static String DISCOVER_VIDEO_LINK = "DISCOVER_VIDEO_LINK";
    public static String DISCOVER_VIDEO_NAME = "DISCOVER_VIDEO_NAME";
    public static String VIDEO_CHAT_SCREEN = "VIDEO_CHAT_SCREEN";
    public static String CONNECT_USER_ANONYMOUS = "CONNECT_USER_ANONYMOUS";
    public static String CONNECT_FRIEND_ANONYMOUS = "CONNECT_FRIEND_ANONYMOUS";


    /*Settings API call*/
    public static String SETTINGS_BACK = "SETTINGS_BACK";
    public static String SETTINGS_ANONYMOUS = "SETTINGS_ANONYMOUS";
    public static String SETTINGS_PUSH = "SETTINGS_PUSH";
    public static String SETTINGS_HIDE_LOC = "SETTINGS_HIDE_LOC";
    public static String SETTINGS_WEB_TITLE = "SETTINGS_WEB_TITLE";
    public static final String SETTINGS_WEB_SUPPORT = "https://www.sparkmessenger.com/about/";
    public static final String SETTINGS_WEB_PRIVACY = "https://www.sparkmessenger.com/privacy/";
    public static final String SETTINGS_WEB_TERMS = "https://www.sparkmessenger.com/terms/";

    /*Base URL*/
    public static final String BASE_URL = " http://50.87.171.216/";

    public static final String GET_ADDRESS_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$s&sensor=true";
    public static final String ANDROID_SPARK_APP = "https://play.google.com";
    public static final String IOS_SPARK_APP = "https://itunes.apple.com/us";

    /*Push Notification*/
    public static String NOTIFICATION_DATA = "NOTIFICATION_DATA";

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
    public static final String PARAMS_CHAT_SEND = "[\"user_id\",\"friend_id\",\"message\",\"subject\",\"message_flow\",\"is_attachement\",\"attachement_url\"]";

    /*Receive Chat API*/
    public static final String API_CHAT_RECEIVE = "receive_chat";
    public static final String PARAMS_CHAT_RECEIVE = "[\"user_id\",\"friend_id\",\"subject\",\"max_chat_id\"]";

    /*DisConnect Chat API*/
    public static final String API_CHAT_DISCONNECT = "disconnect";
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
    public static final String PARAMS_IGNORE_FRIEND = "[\"user_id\",\"friend_id\",\"type\"]";

    /*Read Notification*/
    public static final String API_READ_NOTIFICATION = "readNotification";
    public static final String PARAMS_READ_NOTIFICATION = "[\"user_id\",\"notification_id\"]";

    /*Background API*/
    public static final String API_BACKGROUND = "backgroundApi";

    /*Profile Pic API*/
    public final static String API_IMAGE_UPLOAD_MODE = "production";
    public final static String API_IMAGE_UPLOAD = "add_picture";
    public final static String API_CHAT_IMAGE_UPLOAD = "chat_image";

    /*Settings API*/
    public static final String API_UPDATE_SETTINGS = "updateSettings";
    public static final String PARAMS_UPDATE_SETTINGS = "[\"user_id\",\"private_account\",\"push_notifications\",\"hide_location\",\"anonymous\"]";

    /*Delete Acc API*/
    public static final String API_DELETE_ACC = "deleteAccount";

    /*Invite API*/
    public static final String API_INVITE_FRIEND = "inviteFriend";
    public static final String PARAMS_INVITE_FRIEND = "[\"user_id\",\"email_id\"]";

    /*Get Inbox API*/
    public static final String API_GET_INBOX = "get_my_inbox";

    /*Delete Chat API*/
    public static final String API_DELETE_CHAT = "deletechat";
    public static final String PARAMS_DELETE_CHAT = "[\"user_id\",\"friend_id\",\"chat_id\"]";

    /*ANONYMOUS API*/
    public static final String API_ANONYMOUS = "change_anonymous_status";
    public static final String PARAMS_ANONYMOUS = "[\"user_id\",\"anonymous\"]";

    /*UserName API*/
    public static final String API_CHECK_USER_NAME = "username_available";
    public static final String PARAMS_CHECK_USER_NAME = "[\"username\"]";



/*FCM Config Acc
  Email ID: prithivdharmaraj@gmail.com **/
}


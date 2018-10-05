package com.calix.calixgigamanage.utils;


import com.calix.calixgigamanage.input.model.ScannerInputModel;
import com.calix.calixgigamanage.output.model.AddIOTDeviceEntity;
import com.calix.calixgigamanage.output.model.CategoriesEntity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.GuestWifiEntity;
import com.calix.calixgigamanage.output.model.IOTDeviceConfigResponse;
import com.calix.calixgigamanage.output.model.RouterMapEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

public class AppConstants {

    /*Screen name for clear the hole activity*/
    public static final String SUCCESS_CODE = "200";
    public static final String LOGIN_SCREEN = "Login";
    public static final String PIN_CODE_SCREEN = "PinCodeFingerPrintLogin";
    public static final String DASH_BOARD_SCREEN = "Dashboard";
    public static final String IOT_DEVICE_LIST = "IOTDeviceList";
    public static final String GUEST_NETWORK = "GuestNetwork";
    public static final String SETTINGS = "Settings";

    /*Screen name for clear the hole activity*/
    /*Pin code with finger print*/
    public static final String ANDROID = "android";
    final public static String PRODUCTION_BASE_URL = "https://rgw.calix.ai/";
    final public static String STAGE_BASE_URL = "https://stage.rgw.calix.ai/";
    final public static String DEVELOPER_BASE_URL = "https://dev.rgw.calix.ai/";
    public static final String API_DEVICE_LIST = "map/v1/mobile/device/list?type=%1$s";
    public static final String API_DEVICE_USAGE = "map/v1/mobile/device/usage/history?deviceId=%1$s&filter=%2$s";
    //    public static final String PARENT_CONTROL_SCREEN = "ParentControl";
//    public static final String DEVICES = "Devices";
//    public static final String NETWORK_USAGE = "NetworkUsage";
//    public static final String ROUTER_MAP = "RouterMap";
    public static final String API_IOT_DEVICE_CONFIG = "map/v1/mobile/iot/device/info?deviceId=%1$s";
    public static final String API_DEVICE_FILTER_LIST = "map/v1/mobile/device/listByFilter?routerId=%1$s";
    public static final String API_INSTALL_ALEXA = "map/v1/ivr/demo/install/app";
    public static final String API_GUEST_CONTACTS = "map/v1/ivr/demo/guest/name";
    public static final SimpleDateFormat CUSTOM_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    public static final SimpleDateFormat GRAPH_DATE_FORMAT = new SimpleDateFormat("MMM-dd", Locale.US);
    public static final SimpleDateFormat GRAPH_MONTH_DATE_FORMAT = new SimpleDateFormat("MMM", Locale.US);
    public static final SimpleDateFormat GRAPH_DAY_DATE_FORMAT = new SimpleDateFormat("MMM dd", Locale.US);
    public static final SimpleDateFormat GRAPH_HOUR_DATE_FORMAT = new SimpleDateFormat("MMM dd HH:mm", Locale.US);
    public static final SimpleDateFormat GRAPH_YEAR_DATE_FORMAT = new SimpleDateFormat("yyyy", Locale.US);
    public static final SimpleDateFormat CUSTOM_12_HRS_TIME_FORMAT = new SimpleDateFormat("hh:mm aa", Locale.US);
    public static final SimpleDateFormat CUSTOM_24_HRS_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    public static final SimpleDateFormat CUSTOM_DATE_TIME_FORMAT = new SimpleDateFormat("MM-dd-yyyy hh:mm aa", Locale.US);
    /*Temp*/
    public static final String WEB_SOCKET_BASE_URL = "ws://10.10.10.254:7681/admin/2a99f679";
    public static final String WEB_SOCKET_CALIX_BASE_URL = "wss://%1$s:%2$s";
    /*Router Connection*/
    final public static String ETHER_NET = "eth";
    /*Password Pattern*/
    final public static String PASS_WORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
    public static final String QR_CODE_GENERATOR = "WIFI:S:%1$s;T:%2$s;P:%3$s;";
    public static final String ALEXA_APP = "com.amazon.dee.app";
    public static final String ALEXA_BASE_URL = "https://api.amazon.com/auth/O2/token";
    public static final String GRANT_TYPE = "authorization_code";
    static final String SHARE_PREFERENCE = "SHARE_PREFERENCE";
    static final SimpleDateFormat SERVER_DATE_TIME_FORMAT = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
    public static String TAG = "TAG";
    /*PREVIOUS_SCREEN*/
    public static ArrayList<String> PREVIOUS_SCREEN = new ArrayList<>();
    /*Login details*/
    public static String LOGIN_PIN_PWD_STATUS = "LOGIN_PIN_PWD_STATUS";
    public static String LOGIN_PIN_PWD = "LOGIN_PIN_PWD";
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    /*Settings*/
    public static String PASS_CODE_ENABLE_STATUS = "PASS_CODE_ENABLE_STATUS";
    public static String TOUCH_ID_ENABLE_STATUS = "TOUCH_ID_ENABLE_STATUS";
    public static String ABOUT_TERMS_HEADER_TEXT = "ABOUT_TERMS_HEADER_TEXT";
    public static String PRODUCTION_MODE_ENABLE_STATUS = "PRODUCTION_MODE_ENABLE_STATUS";
    public static boolean ROUTER_ON_BOARD_FROM_SETTINGS = false;
    public static boolean ROUTER_ON_BOARD_FROM_WELCOME = false;
    /*App URL's*/
    public static String USER_SELECTED_BASE_URL = "https://dev.rgw.calix.ai/";
    public static String BASE_URL = "https://dev.rgw.calix.ai/";
    /*User details*/
    public static String AUTHORIZATION = "AUTHORIZATION";
    public static CategoriesEntity IOT_DEVICE_CATEGORIES = new CategoriesEntity();
    /*Router details*/
    public static RouterMapEntity ROUTER_DETAILS_ENTITY = new RouterMapEntity();
    public static DeviceEntity DEVICE_DETAILS_ENTITY = new DeviceEntity();
    public static AddIOTDeviceEntity ADD_IOT_DEVICE_DETAILS = new AddIOTDeviceEntity();
    public static IOTDeviceConfigResponse IOT_DEVICE_DETAILS = new IOTDeviceConfigResponse();
    public static GuestWifiEntity GUEST_WIFI_DETAILS = new GuestWifiEntity();
    public static String WEB_SOCKET_CALIX_IP = "WEB_SOCKET_CALIX_IP";
    public static String WEB_SOCKET_CALIX_PORT = "7681";
    public static String WEB_SOCKET_SERVICE_TYPE = "_iothub._tcp.";
    public static String WEB_SOCKET_CALIX_TOKEN = "WEB_SOCKET_CALIX_TOKEN";
    /*Guest router_map*/
    public static LinkedHashMap<String, String> SELECTED_CONTACT_LINKED_HASH_MAP = new LinkedHashMap<>();
    /*Router Welcome screen*/
    public static ScannerInputModel SCANNED_DETAILS_RES = new ScannerInputModel();
    public static String ALEXA_FSN = "ALEXA_FSN";
    public static String ALEXA_ROUTER_ID = "ALEXA_ROUTER_ID";
    public static String USER_ID = "USER_ID";
    public static String ALEXA = "alexa";
    public static String USER_EMAIL = "USER_EMAIL";
    public static String ALERT_NOTIFY_ID = "ALERT_NOTIFY_ID";
    public static boolean IS_SPEED_STARTED = false;
    static String BEARER = "Bearer";


}


package com.smaat.ipharma.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.os.Environment;
import android.util.Log;


import com.smaat.ipharma.entity.LocalTimeEntitiy;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.fragments.AddReminderScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class AppConstants {

    public static final String IS_LOGGEDIN = "IS_LOGGEDIN";
    public static final String LOGGED_IN = "LOGGED_IN";
    public static final String LOGGED_OUT = "LOGGED_OUT";

    public static final String USERTYPE = "USERTYPE";
    public static final String USERID = "USERID";

    public static final String LOGIN_USERID = "LOGIN_USERID";
    public static final String LOGIN_USERNAME = "LOGIN_USERNAME";
    public static final String USERNAME = "USERNAME";

    public static final String SELECTED_TONE = "SELECTED_TONE";
    public static final String SELECTED_TONE_EXT = "SELECTED_TONE_EXT";
    public static final String SELECTED_TONE_URI = "SELECTED_TONE_URI";

    public static final String AUDIO_TONE = "AUDIO_TONE";
    public static final String REMAINDER_TONE = "REMAINDER_TONE";


    public static final String SELECTED_RMEINDER_TONE = "SELECTED_RMEINDER_TONE";
    public static final String SELECTED_REMAINDER_TONE_URI = "SELECTED_REMAINDER_TONE_URI";

    public static final String FULL_NAME = "FULL_NAME";
    public static final String shared_pref_name = "Ipharma_Latest";
    public final static String API_KEY = "AIzaSyC3zPFVa8mWbRCTwxFhUbYVDDxe78deKUI";
    public static boolean IS_FAVOURITE = false;
    public static int CLICKED_POSITION = 0;

    //smaatapps server
    //public static final String BASE_URL = "http://smaatapps.com/moos/index.php/";

    //test server
    //public static final String BASE_URL = "http://50.87.171.216/moos/index.php/";

    //live server
    public static final String BASE_URL = "http://ipharmaapp.com/webservice/index.php/";
    public static final String BASE_URL2 = "http://ipharmaapp.com/webservice/";
    public final static String LATLNG_LINK = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";

    public final static String TUTORIAL_SCREEN = "Sample_Screen";
    public final static String TUTORIAL = "Tutorial";

    public static final String SUCCESS_CODE = "1";
    public static final String SUCCESS = "success";
    public static final String USER_BLOCKED = "-1";
    public static final String FAILURE_CODE = "0";
    public static final String SUCCESS_CODE_WITHOUT_APPROVAL = "2";
    public static final String REG_WITH_TWITTER = "3";

    public static final String MESSAGE_TITLE = "MESSAGE_TITLE";
    public static final String MESSAGE_DESCRIPTION = "MESSAGE_DESCRIPTION";

    public static int DISCOUNT_AMOUNT = 15;
    /*User Details*/
    public final static String TUT_SCREEN = "0";
    public static String USER_ID = "USER_ID";
    public static String USER_STATUS = "USER_STATUS";
    public static String USER_NAME = "USER_NAME";
    public static String PASSWORD = "PASSWORD";
    public static String PHONE_NUMBER = "PHONE_NUMBER";
    public static String COUNTRY_CODE = "COUNTRY_CODE";
    public static String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public static String LOGIN_TYPE = "LOGIN_TYPE";
    public static String CITY = "CITY";
    public static String AREA = "AREA";
    public static String USER_AREA_PINCODE = "USER_AREA_PINCODE";
    public static String USER_ADDRESS = "USER_ADDRESS";
    public static String COMMUNICATION_ADDRESS = "COMMUNICATION_ADDRESS";
    public static String DEVICE_ID = "DEVICE_ID";
    public static String REMINDER_NOTIFICATION = "reminder_notification";
    public static String PUSH_NOTIFICATION = "push_notification";
    public static String APPOINMENT_TIME = "appoinment_time";
    public static String ALARM_ARRAY = "alarm_array";
    public static String ADDRESS_ARRAY = "address_array";
    public static String TONE_TEXT = "tonetext";
    public static String TONE_TEXT_REMINDER = "remindertonetext";
    public static String REASON_REJECT = "";
    public static String OPEN_PUSH_NOTIFICATION = "";
    public static String APPOINMENT_ID = "";
    public static String SHOP_ID = "";
    /*public static String ADAPTER_LIST="COMMON";*/
    /**
     * User Type'sf
     */
    /**
     * Push Notification
     */

    public static String Search_text = "";
    public static String ADMIN_BASE_URL = "http://ipharmaapp.com/admin/";
    public static Ringtone ringtone = null;
    public static final String msgTag = "msg";
    public static final String type_of_notificationTag = "type_of_notification";
    public static final String notification_fromTag = "notification_from";
    public static final String notification_idTag = "notification_id";
    public static final String countsTag = "counts";
    public static final String typeIdTag = "typeId";
    public static final String dateTimetag = "datetime";
    public static final String shopNameTag = "name";

    public static boolean enableOneTouch = false;

    public static MapPropertyEntity PharmacyDetails = null;

    public static final SimpleDateFormat InputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    public static final SimpleDateFormat InputTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    public static final SimpleDateFormat DisplayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final SimpleDateFormat DisplayFormat1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.US);
    public static final SimpleDateFormat InputDateFormat2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    public static final SimpleDateFormat InputDateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);

    public static final SimpleDateFormat InputDateFormat3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);


/*
    public final static String pref_deviceReg_Id = "DEVICE_ID";*/
    public final static String pref_device_reg_status = "ISREGISTERED";
    public final static String pref_device_id_changed = "ISDEVICEIDCHANGED";
    public final static String Profile_Pic_url = "profile_image";
    public static int Notification_Click_Pos = 0;




//constant  values



    public static final String DefaultLanguage = "English";
    public static final String DefaultLanguageCode = "en";

    public static String TOAST_MSG = "Will be added in future release";

    public static String Currency = "";
    public static String SHOP_LATITUDE = "";
    public static String SHOP_LONGITUDE = "";
    public static String SHOP_NAME = "";



    public static String SELECTED_LAUNGUAGE = "";
    public static String SELECTED_LAUNGUAGE_CODE = "";
    public static String STORED_LANGUAGE = "sel_lang";
    public static String STORED_LANGUAGE_CODE = "sel_lang_code";

    public static String IS_LOGIN = "";

    public static HashMap<String,String> languageServicelist = null;

    //onetouchconstants
    public static String PharmacyID_OT = "pharmacyid_ot";
    public static String ShopUserName_OT = "shopusername_ot";
    public static String ShopPassword_OT = "shoppassword_ot";
    public static String ShopName_OT = "shpname_ot";
    public static String ShopCategory_OT = "shopcategory_ot";
    public static String OwnerName_OT = "ownername_ot";
    public static String ShopIcon_OT = "shopicon_ot";
    public static String Address_OT = "address_ot";
    public static String City_OT = "city_ot";
    public static String Area_OT = "area_ot";
    public static String Pincode_OT = "pincode_ot";
    public static String Landmark_OT = "landmark_ot";
    public static String Latitude_OT = "latitude_ot";
    public static String Longitude_OT = "longitude_ot";
    public static String Email_OT = "email_ot";
    public static String Phone_OT = "phone_ot";
    public static String Mobile_OT = "mobile_ot";
    public static String Website_OT = "website_ot";
    public static String OpenTime_OT = "opentime_ot";
    public static String CloseTime_OT = "closetime_ot";
    public static String ProfilePic_OT = "profilepic_ot";
    public static String GUARDIAN_NUMBER = "guard_number";
    public static String DOCTOR_NUMBER = "doctor_number";
    public static String DELIVERY_STATUS = "";

    public static boolean FROMHISTORY = false;
    public static String Choosed_Image = "";
    public static int SELECTED_POSITION = 0;
    public static  String SHOP_ID_VAL = "";
    public static  String IPHARMA_MONEY = "";
    public static  boolean FROM_MY_ORDER = false;
    public static  String PHARMACY_ID = "";

    public final static String ABOUT_LINK = "http://ipharmaapp.com/admin/site_templates/about.php";
    public final static String DISCLAIMER_LINK = "http://ipharmaapp.com/admin/site_templates/disclaimer.php";
    public static String Url = "";

    public static int EDITPOS=0;
    public static AddReminderScreen.AddReminderInterface ADD_REMINDER_INTERFACE = null;
    public static AddReminderScreen.EditReminderInterface EDIT_REMINDER_INTERFACE = null;
    public static LocalTimeEntitiy EDITLOCALTIMEENTITIY=null;
    /*public static int UNIQUE_ID = 0;*/

}

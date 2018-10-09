package com.smaat.ipharma.util;

import com.smaat.ipharma.entity.MapPropertyEntity;

public class AppConstants {

	public final static String FIRST_PASS = "FIRST_PASS";
	public final static String SECOND_PASS = "SECOND_PASS";
	public final static String PASSWORD = "PASSWORD";

	public final static String shared_pref_name = "AppLockPreference";
	public static final String SUCCESS_CODE = "1";
	public static final String FAILURE_CODE = "0";
	public static final String SEARCH_SUCCESS_CODE = "No shops found";

	// Push notification data
	public static final String SENDER_ID = "97147526373";
	public final static String pref_deviceReg_Id = "DEVICE_ID";
	public final static String pref_device_reg_status = "ISREGISTERED";
	public final static String pref_device_id_changed = "ISDEVICEIDCHANGED";
	// public final static String api_Key =
	// "AIzaSyBzuiN7qNX9Aais8R0PNEfcilwMt1JO5gU";

	// Shared Preference keys
	public final static String pref_isLoggedIn = "pref_isLoggedIn";
	public final static String pref_isTutorailSeen = "pref_isTutorailSeen";
	public final static String pref_account_type = "pref_account_type";
	public final static String pref_email = "pref_email";
	public final static String communication_address = "";
	public final static String pref_selected_apps = "pref_selected_apps";
	public static int pref_main = 1;
	public final static String islock = "islock";
	public final static String pref_type = "pref_type";
	public final static String pref_selected_acc = "selected_acc";
	public final static String pref_owner = "owner_name";
	public final static String pref_phone = "phone";

	public final static String pref_packagename = "pref_packagename";
	public final static String pref_lock_block = "pref_lock_block";

	public final static String REGISTER_SCREEN = "Register_Screen";
	public final static String LOGIN_SCREEN = "LogIn_Screen";
	public final static String PROFILE_SCREEN = "Profile_Screen";
	public final static String WEB_SCREEN = "Web_Screen";
	public final static String HOME_SCREEN = "Home_Screen";
	public final static String MORE_SCREEN = "More_Screen";
	public final static String TUT_SCREEN = "0";
	public final static String LOGOUT_SCREEN = "Log_Out";
	public final static String LOGOUT = "Logout";
	public final static String TUTORIAL_SCREEN = "Sample_Screen";
	public final static String TUTORIAL = "Tutorial";

	public final static String pref_individual = "pref_individual";
	public final static String FACEBOOK = "facebook";
	public final static String GOOGLE = "google";
	public final static String EMAIL = "email";
	public final static String RENTER = "renter";
	public final static String BUYER = "buyer";
	public final static String SELLER = "seller";
	public final static String AGENT = "agent";
	public final static String BROKER = "broker";
	public static String marker_id = "markerid";
	public static String fromFavourite = "Favourite";
	public static String bread = "bread";

	public final static String Get_started = "false";
	public static String pref_db = "false";

	public static String CALL_MAP = "false";
	public static String CUSTOM_DIALOG_SHOWN = "false";
	public static String categoryName = "category";
	public static String catName = "catname";
	public static String cost = "cost";
	public static String newitem = "newitem";
	public static int positin = 0;
	public static String rowcount = "0";
	public static String FRAG = "fragment";
	public static String photo = "photo";
	public static String map = "maplist";
	public static String popup = "popups";

	public static String Search_text = "";
	public static String from_map_list = "false";

	public static String Pharmacy_id = "";

	public static MapPropertyEntity mMapDetailEntity;

	public static String prescription_image = "";

	public static String history_prec_img = "";

	public static float offer_map_distance = 1;
	public static String offer_search_text = "";
	public static String offer_latitude = "";
	public static String offer_longitude = "";
	public static float offer_map_zoom_level = 0;

	// History Details
	public static String history_mPharmacyName = "";
	public static String history_mPharmacyAddress = "";
	public static String history_mDistance = "";
	public static String history_mShopId = "";
	public static String history_mPrescriptionId = "";
	public static String history_mNotehere = "";
	public static String history_mPhonenum = "";

	// for My Orders
	public static String from_my_order = "";
	public static String delivery_status = "";
	public static String purchase_amount = "";
	public static String IPHARMA_MONEY = "0";
	public static String IPHARMA_TOTAL_MONEY = "null";

	// Maplist to review
	public static String from_map_list_review = "0";
	public final static String FALSE = "false";
	public final static String TRUE = "true";

	// User Details
	public final static String USER_ID = "User_Id";
	public final static String USER_NAME = "User_Name";
	public final static String USER_EMAIL_ID = "User_Email_Id";
	public final static String USER_PASSWORD = "User_PassWord";
	public final static String USER_PHONE_NUM = "User_Ph_Num";
	public final static String USER_ADDRESS = "User_Address";
	public final static String USER_CITY = "User_City";
	public final static String USER_AREA = "User_Area";
	public final static String USER_AREA_PINCODE = "User_Area_Pincode";
	public static String MAP_CONT_VAL="MAP_CONT_VAL";
	// Web Link
	public static String Image_Base_Url = "http://smaatapps.com/iPharma/webservice";
	public static String Base_Url = "http://ipharmaapp.com/webservice";
	public static String ADMIN_BASE_URL = "http://ipharmaapp.com/admin/";
	public static String PHARMACY_IMAGE_BASE_URL = "http://smaatapps.com/iPharma/pharma/";



	// public static String Base_Url =
	// "http://smaatapps.com/iPharma/webservice";

	public final static String ABOUT_LINK = "http://ipharmaapp.com/admin/site_templates/about.php";
	public final static String DISCLAIMER_LINK = "http://ipharmaapp.com/admin/site_templates/disclaimer.php";
	public final static String TERMS_LINK = "http://ipharmaapp.com/admin/site_templates/terms.php";
	public final static String LATLNG_LINK = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
	public final static String SEARCH_PHARMACY_LINK = "/index.php/pharmacy/searchpharmacy";
	public final static String GOOGLE_MAP_LINK = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	public final static String PAYMENT_BASE_URL = "http://ipharmaapp.com/payment/pay/";
	public final static String API_KEY = "AIzaSyC3zPFVa8mWbRCTwxFhUbYVDDxe78deKUI";
	// FRAG Consts
	public final static String MAP_SCREEN = "Map_Screen";
	public final static String REVIEW = "Review";
	public final static String ORDERNOW_SCREEN = "OrderNow_Screen";
	public final static String HISTORY_SCREEN = "History_Screen";
	public final static String CAMERA = "Camera";
	public final static String ORDER_NOW = "OrderNow";
	public final static String PHOTO = "photo";
	public final static String ORDER_DETAILS = "Order_Details";
	public final static String ONE_TOUCH_ORDER = "One_touchOrder";
	public final static String ONE_TOUCH_ORDER_SELECT = "One_touchOrder_select";
	public final static String MYORDER_SCREEN = "MyOrder_Screen";
	public final static String PAYMENT_OPTION_SCREEN = "Payment_Option_Screen";
	public final static String MONEY_SCREEN = "Money_Screen";
	public final static String MORESCREEN = "MoreScreen";
	public final static String OFFERS_SCREEN = "offers_Screen";
	public final static String EXIT = "Exit";
	public final static String FROMMAP = "FROMMAP";
	public static String FROMINTLOADMAP = "FROMINTLOADMAP";
	public static String FROM_MAPFAVORITE_SCREEN = "FROM_MAPFAVORITE_SCREEN";
	public final static String FAVORITE_SCREEN = "FAVORITE_SCREEN";
	public final static String PILL_REM_SCREEEN = "PILL_REM_SCREEEN";

	// Bundle Values
	public final static String PHARMCAY_DETAILS = "pharmcay_details";
	public final static String REDIRECTION = "redirection";
	public final static String OFFERS = "offers";
}

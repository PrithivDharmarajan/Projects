package com.smaat.renterblock.utils;

import com.google.android.gms.maps.model.LatLng;
import com.smaat.renterblock.entity.AgentFilterLocalEntity;
import com.smaat.renterblock.entity.AlertsResultEntity;
import com.smaat.renterblock.entity.ChatInputEntity;
import com.smaat.renterblock.entity.FilterEntity;
import com.smaat.renterblock.entity.FindAgentDetailUserResult;
import com.smaat.renterblock.entity.FindAgentFilterResultEntity;
import com.smaat.renterblock.entity.FriendDetailsEntity;
import com.smaat.renterblock.entity.ImageUploadEntity;
import com.smaat.renterblock.entity.LeadsEntity;
import com.smaat.renterblock.entity.LocalSavedSearchEntity;
import com.smaat.renterblock.entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.entity.PropertyPictures;
import com.smaat.renterblock.entity.PropertyReviewCommentEntity;
import com.smaat.renterblock.entity.ReviewPropertyEntity;
import com.smaat.renterblock.entity.SavedSearchEntity;
import com.smaat.renterblock.entity.ScheduleListArray;
import com.smaat.renterblock.entity.ShareThisAppEntity;
import com.smaat.renterblock.fragments.AlertsFragment;
import com.smaat.renterblock.fragments.HotLeadsPropertyFragment;
import com.smaat.renterblock.fragments.MapFragment;
import com.smaat.renterblock.fragments.NotificationFragment;
import com.smaat.renterblock.fragments.ProfileFragment;
import com.smaat.renterblock.fragments.ProfileMoreAboutMeFragment;
import com.smaat.renterblock.fragments.PropertyListingFragment;
import com.smaat.renterblock.fragments.ReviewsListFragment;
import com.smaat.renterblock.fragments.SchedulingFragment;
import com.smaat.renterblock.main.BaseFragment;
import com.smaat.renterblock.model.UserProfileResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AppConstants {

    static final String SHARE_PREFERENCE = "SHARE_PREFERENCE";
    public static String TAG = "TAG";

    /*Status Code*/
    public static final String SUCCESS_CODE = "1";
    public static final String FAILURE_CODE = "0";

    /*Login*/
    public final static String FACEBOOK = "facebook";
    public final static String GOOGLE = "google";
    public final static String EMAIL = "email";

    public final static String DEVICE = "Android";

    public final static String BUYER = "buyer";
    public final static String SELLER = "seller";
    public final static String AGENT = "agent";
    public final static String BROKER = "broker";
    public final static String RENTER = "renter";


    /*User details*/
    static String USER_ID = "USER_ID";
    static String USER_DETAILS = "USER_DETAILS";
    public static String LOGIN_STATUS = "LOGIN_STATUS";
    public static String TUTORIAL_SEEN = "TUTORIAL_SEEN";

    /*Setting screen details */
    public static String SYNC_CONTACTS = "1";
    public final static String NEW_SAVED_SEARCH_MATCHES = "OFF";
    public final static String UPDATE_RESULT_AS_MAP_MOVES = "ON";
    public static String Settings_current_time = "";

    public static String CONTENT_HEADER = "CONTENT_HEADER";
    public static String CONTENT_URL = "CONTENT_URL";

    public final static String TC = AppConstants.BASE_URL + "content/tc";
    public final static String PP = AppConstants.BASE_URL + "content/pp";
    public final static String AB = AppConstants.BASE_URL + "content/arb";
    private final static String REVIEW_PROPERTY_URL = AppConstants.BASE_URL + "reviewproperty";
    public final static String REVIEW_EDIT_URL = AppConstants.REVIEW_PROPERTY_URL + "/edit";
    public final static String REVIEW_POST_URL = AppConstants.REVIEW_PROPERTY_URL + "/update";
    public final static String CHAT_FILE_BASE_URL = "http://rentersblock.com/chatfile/";
    public static String DETAIL_PROPERTY_ID = "DETAIL_PROPERTY_ID";
    public static boolean CALLED_FROM_ALERT_FRAG = false;
    public static boolean INVITE_FRIENDS_SCHEDULE = false;

    public final static String ON = "ON";
    public final static String OFF = "OFF";

    public static final String TWITTER_API_KEY = "Yret1zxIlLDn131J7lpLBZ9Gv";
    public static final String TWITTER_API_SECRET = "vr6cpthCoo25UNCptgvGQs7PqN2aolY6evUNokRjYbRTYJvXmS";

    public static final String GET_DETAILS_ADDRESS_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=%1$s&sensor=true";


//    public final static String BASE_URL = "http://rentersblock.com/webservice_dev/index.php/";
        public final static String BASE_URL = "http://rentersblock.com/webservice/index.php/";
    public static final String GET_ADDRESS_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$s&sensor=true";
    public final static String API_KEY = "AIzaSyCBS9owmjeiWT0wNh4mjpI3jW2ro8E-S5c";
    public final static String PLACE_URL_START = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    public final static String PLACE_URL_END = "&types=geocode&key=AIzaSyCBS9owmjeiWT0wNh4mjpI3jW2ro8E-S5c";

    public final static String PLACE_URL_LAT_LANG = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
    public final static String MAP_API_KEY = "&key=AIzaSyCBS9owmjeiWT0wNh4mjpI3jW2ro8E-S5c";
    public final static String APP_LINK = "goo.gl/DGDC1v";

    // MAP Fragment
    public static String TYPE_OF_PROPERTY = AppConstants.RENT;
    public final static String EXCLUSIVE = "exclusive";
    public final static String RENT = "Rent";
    public final static String SALE = "Sale";
    public final static String OPEN_HOUSES = "openhouses";
    public static String EMPTY_FILTER_TYPE = "";
    public static Double CURRENT_LATITUDE = 0.0;
    public static Double CURRENT_LONGITUDE = 0.0;
    public static LatLng LAST_SEARCH_LOCATION_LAT_LONG = new LatLng(0.0, 0.0);
    public static String SEARCH_RESULT_COUNT = "50";
    public static String saved_search_json = "";
    public static String saved_search_Latitude = "0.0";
    public static String saved_search_Longitude = "";
    public static String saved_location_name = "";
    public static String MAP_AD_COUNT = AppConstants.FAILURE_CODE;

    //sorting option
    public static String PROPERTY_SORT_TYPE = "PROPERTY_SORT_TYPE";
    public static String SORT_BEST = "best";
    public static String SORT_LATEST = "latest";
    public static String SORT_RATING = "rating";
    public static String SORT_PRICE_LOW = "price_lth";
    public static String SORT_PRICE_HIGH = "price_htl";
    public static String SORT_BED = "bed";
    public static String SORT_BATH = "bath";
    public static String SORT_SQRT = "sqrt";

    public static PropertyEntity PROPERTY_DETAILS = null;
    public static String ADD_PROPERTY = "";
    public static String KEY_TRUE = "true";
    public static String KEY_FALSE = "false";
    public static String SELECTED_PROPERTY_TYPE = "All Types";
    public static BaseFragment LAST_CURRENT_FRAG = new MapFragment();

    /*Review details*/
    public static ReviewPropertyEntity REVIEW_DETAILS_RES = new ReviewPropertyEntity();
    public static PropertyReviewCommentEntity CURRENT_REVIEW_DETAILS = new PropertyReviewCommentEntity();

    final static SimpleDateFormat SERVER_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);

    /*Filter Fragment */
    static String RENT_FILTER = "Rent";
    static String SALE_FILTER = "Sale";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String FOUR = "4";
    public static final String FIVE = "5";
    public static String PROPERTY_TYPE = "";
    public static String TYPE_OF_FILTER = "Rent";

    /*Saved search fragment*/
    public static ArrayList<LocalSavedSearchEntity> SELECTED_SAVED_SEARCH_ARRAY = new ArrayList<>();
    public static ArrayList<String> SELECTED_SAVED_SEARCH_IDS = new ArrayList<>();

    public static String MAP_TYPE = AppConstants.SUCCESS_CODE;


    public static boolean IS_FAVOURITE_DELETE = false;
    public static ArrayList<PropertyPictures> mPropertyPics = new ArrayList<>();

    public static FilterEntity RENT_FILTER_ENTITY = new FilterEntity();
    public static FilterEntity SALE_FILTER_ENTITY = new FilterEntity();

    public static String PROPERTY_DETAILS_LATITUDE = "PROPERTY_DETAILS_LATITUDE";
    public static String PROPERTY_DETAILS_LONGITUDE = "PROPERTY_DETAILS_LONGITUDE";
    public static FindAgentFilterResultEntity FILE_AGENT_FILTER_RESULT_ENTITY = new FindAgentFilterResultEntity();
    public static FindAgentDetailUserResult FindAgent_DetailUser_Result = new FindAgentDetailUserResult();

    public static ImageUploadEntity IMAGE_UPLOAD_ENTITY = null;
    public static ShareThisAppEntity SHARE_DETAILS = new ShareThisAppEntity();

    /*Alerts*/
    public static ArrayList<String> ALERTS_SELECTED_IDS = new ArrayList<>();
    public static ArrayList<AlertsResultEntity> ALERT_SELECTED_LIST = new ArrayList<>();
    public static String ALERT_LOCATION = "ALERT_LOCATION";
    public static String ALERT = "ALERT";
    public static String ALERT_CREATE = "ALERT_CREATE";
    public static AlertsResultEntity ALERT_ENTITY = new AlertsResultEntity();
    public static ArrayList<String> DELETE_SCHEDULE_ID = new ArrayList<>();
    public static ScheduleListArray mSelectedPos = new ScheduleListArray();
    public static String EDIT = "EDIT";
    public static String REMOVE_ADS_TOGGLE = "REMOVE_ADS_TOGGLE";

    public static String PROFILE_ID = "";
    /*Feeds List*/
    public static ArrayList<ProfileMyFeedsEntity> FEEDS_LIST = new ArrayList<>();

    /*Back Screen*/
    public static BaseFragment MAP_CURRENT_BACK_FRAGMENT = new MapFragment();
    public static BaseFragment PROPERTY_LIST_CURRENT_BACK_FRAGMENT = new PropertyListingFragment();
    public static BaseFragment REVIEW_LIST_CURRENT_BACK_FRAGMENT = new ReviewsListFragment();
    public static BaseFragment PROFILE_CURRENT_BACK_FRAGMENT = new ProfileFragment();
    public static BaseFragment NOTIFICATION_CURRENT_BACK_FRAGMENT = new NotificationFragment();
    public static BaseFragment HOT_LEADS_BACK_FRAGMENT = new HotLeadsPropertyFragment();
    public static BaseFragment ALERT_BACK_FRAGMENT = new AlertsFragment();
    public static BaseFragment SCHEDULE_BACK_FRAGMENT = new SchedulingFragment();




    /*Property Listing*/
    public static String PROPERTY_LIST_USER_ID = AppConstants.FAILURE_CODE;

    /*Hot leads*/
    public static ArrayList<LeadsEntity> LEADS_VIEW_LIST = new ArrayList<>();

    /*Agent Filter */
    public static String AGENT_FILTER_ENTITY = "";

    /*Chat screen */
    public static ChatInputEntity CHAT_INPUT_ENTITY = new ChatInputEntity();
    /*Board message Screen*/
    public static String BOARD_MESSAGE_PROPERTY_ID = "";


    public static String PUSH_TYPE = "PUSH_TYPE";
    public static String PUSH_MSG = "PUSH_MSG";
    public static String NOTIFICATION_STATUS = AppConstants.FAILURE_CODE;

    public static HashMap<String, ArrayList<FriendDetailsEntity>> INVITE_FRIENDS_HASH_MAP = new HashMap<>();

    /*Schedule Frag*/
    public static String SCHEDULE_DATE="Date";
    public static String SCHEDULE_TIME="Time";
  
    public static boolean SHARE_THIS_PROFILE = false;

    public static UserProfileResponse UserProfileResponse = new UserProfileResponse();

    public static boolean IS_FROM_LIST_CLICK=false;
    public static  boolean IS_FROM_LIST_ITEM_CLICK=false;

}


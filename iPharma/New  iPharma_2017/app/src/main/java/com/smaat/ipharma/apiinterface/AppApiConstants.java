package com.smaat.ipharma.apiinterface;

/**
 * Created by sys on 11/16/2016.
 */

public class AppApiConstants {


    public final static String API_MODE = "production";

    //login
    public final static String API_NAME = "login";
    public final static String API_PARAMETERS = "[\"email_id\", \"password\", \"login_mode\", \"device_type\", \"device_token\"]";

    //Forgot
    public final static String API_NAME_FORGOT = "forgot_password";
    public final static String API_FORGOT_PARAMETERS = "[\"email_id\"]";

    //Registration
    public final static String API_REGISTRATION = "registration";
    public final static String API_REGISTRATION_PARAMETERS = "[\"email_id\", \"password\", \"registration_mode\",\"customer_name\", \"phone_number\",\"lattitude\",\"longitude\",\"device_type\",\"devicetoken\"],";

    //CustomerProfile
    public final static String API_CUSTOMER_PROFILE = "customer_update_profile";
    public final static String API_CUSTOMER_PROFILE_PARAMETERS = "[\"customer_id\", \"password\", \"registration_mode\",\n" +
            "  \"customer_name\", \"phone_number\", \"lattitude\", \"longitude\", \n" +
            " \"device_type\", \"device_token\"]";

    //CustomerProfile
    public final static String API_CUSTOMER_IMAGE_UPDATE = "add_customer_image";


    //ShopList
    public final static String API_SHOPLIST = "shop_list";
    public final static String API_SHOPLIST_PARAMETERS = "[\"customer_id\",\"shop_name\",\"verified\",\"rating\",\"offers\",\"services\"]";

    //Get ShopDetails
    public final static String API_SHOPDETAILS = "get_shop_details";
    public final static String API_SHOPDETAILS_PARAMETERS = "[\"customer_id\",\"shop_id\"]";

    //Customer RequestList
    public final static String API_CUSTOMER_REQUEST_LIST = "customer_request_list";
    public final static String API_CUSTOMER_REQUEST_PARAMETERS =  "[\"customer_id\"]";

    //check times  slot
    public final static String API_CHECK_TIME_SLOT = "check_appointment_slot";
    public final static String API_HECK_TIME_SLOT_PARAMETERS = "[\"customer_id\",\"shop_id\",\"appoinment_datetime\",\"staff_id\"]";

    //Book Appointment
    public final static String API_BOOKAPPOINTMENT = "book_appointment";
    public final static String API_BOOKAPPOINTMENT_PARAMETERS = "[\"customer_id\",\"shop_id\",\"appoinment_datetime\"," +
            "\"services\",\"staff_id\",\"offer_id\",\"payment_mode\",\"total_amount\"]";


    public final static String API_ADDFAVOURITES = "add_favourite";
    public final static String API_FAVOURITES_PARAMETERS = "[\"customer_id\",\"shop_id\"]";

    //Payment History
    public final static String API_PAYMENTHISTORY = "customer_payment_history";
    public final static String API_PAYMENTHISTORY_PARAMETERS =  "[\"customer_id\"]";

    //Notifications
    public final static String API_NOTIFICATION  = "customer_notifications";
    public final static String API_NOTIFICATION_PARAMETERS =  "[\"customer_id\"]";

    //Delete Notifications
    public final static String API_NOTIFICATION_DEL  = "delete_notifications";
    public final static String API_NOTIFICATION_DEL_PARAMETERS =  "[\"customer_id\",\"notifications_id\"]";

    //Get Favourite
    public final static String API_GET_FAVOURITE = "get_favourite";
    public final static String API_GET_FAVOURITE_PARAMETERS =  "[\"customer_id\"]";

    //Reviews
    public final static String API_REVIEWS = "review";
    public final static String API_REVIEWS_PARAMETERS = "[\"customer_id\",\"shop_id\"]";

    //Reject Appointment
    public final static String API_REJECT_APPOINTMENT = "reject_appointment";
    public final static String API_REJECT_APPOINTMENT_PARAMETERS = "[\"customer_id\",\"appointment_id\",\"reason\"]";

    //contact us
    public final static String API_NAME_CONTACT="contact";
    public final static String API_PARAMETER_CONTACT="[\"email_id\",\"message\"]";

    //Rate Service
    public final static String API_RATE_SERVICE = "add_rating";
    public final static String API_RATE_SERVICE_PARAMETERS = "[\"customer_id\",\"shop_id\",\"appointment_id\"," +
            "\"feedback\",\"rating\"]";

}

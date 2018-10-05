package com.bridgellc.bridge.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bridgellc.bridge.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GlobalMethods {

    public static int STRING_PREFERENCE = 1;
    public static int INT_PREFERENCE = 2;
    public static int BOOLEAN_PREFERENCE = 3;
    public static int ARRAY_LIST_PREFERENCE = 4;
    public static int LONG_PREFERENCE = 5;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "AUeHrasNLeNBkkK5Edo3A7w1J_EvDSc3r2yMBwagCONw9ZnB3hO-nkcAia6wSj40o0xsAFBMxKwPtBXJ";
    private static final int REQUEST_PAYPAL_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("BASE")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    public static void storeValuetoPreference(Context context, int preference,
                                              String key, Object value) {
        SharedPreferences sharedPreference = context.getSharedPreferences(
                AppConstants.shared_pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreference.edit();
        if (preference == STRING_PREFERENCE) {
            edit.putString(key, (String) value);
        }
        if (preference == INT_PREFERENCE) {
            edit.putInt(key, (Integer) value);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            edit.putBoolean(key, (Boolean) value);
        }
        edit.commit();

    }

    public static Object getValueFromPreference(Context context,
                                                int preference, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(
                AppConstants.shared_pref_name, Context.MODE_PRIVATE);

        if (preference == STRING_PREFERENCE) {
            return (Object) sharedPreference.getString(key, "");
        }
        if (preference == INT_PREFERENCE) {
            return (Object) sharedPreference.getInt(key, 0);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            return (Object) sharedPreference.getBoolean(key, false);
        }
        return null;

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static boolean isEmailValid(String email, boolean eduMail) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {

            if (eduMail) {
                if (email.endsWith(".edu"))
                    isValid = true;
            } else {
                isValid = true;
            }
        }
        return isValid;
    }


    public static void userDetails(boolean logined, String userId, String
            firstName, String lastName, String emailId, String eduEmailId, String phoneNum, String
                                           password, String universityName, String universityId, String dob, String gender, String loginType, String bankDetails, String cardDetails, String paymentMode, String paymentDetails, String partner, Context context) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN,
                logined);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID, userId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.FIRST_NAME,
                firstName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.LAST_NAME,
                lastName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
                emailId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.EDU_EMAIL_ADDRESS,
                eduEmailId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.PHONE_NUM, phoneNum);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD,
                password);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_UNIVERSITY_NAME, universityName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_UNIVERSITY_ID, universityId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.DOB, dob);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.GENDER, gender);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.LOGINTYPE, loginType);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.BANK_DETAILS, bankDetails);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.CARD_DETAILS, cardDetails);

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.PAYMENT_DETAILS, paymentDetails);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.PAYMENT_MODE, paymentMode);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.PARTNER, partner);

    }


    public static String getUserID(Context context) {

        return (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID);

    }

    public static String convertUTCtime(Context context, String time) {

        String formattedDate = "";
        try {
            String dateStr = time;
            SimpleDateFormat convertFormat = null;
            boolean is24HourFormat = android.text.format.DateFormat
                    .is24HourFormat(context);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dateStr);
            if (is24HourFormat) {
                convertFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.US);
            } else {
                convertFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.US);
            }
            // SimpleDateFormat convertFormat = new SimpleDateFormat(
            // "dd-MMM-yyyy HH:mm:ss");
            convertFormat.setTimeZone(TimeZone.getDefault());
            formattedDate = convertFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String gettwoDateFormate(String date, SimpleDateFormat dateformate, SimpleDateFormat tformate) {
        Date dateobj;
        //create a new Date object using the UTC timezone

//        SimpleDateFormat mServerFormat = new SimpleDateFormat("yyyy-mm-DD HH:mm:ss");
        SimpleDateFormat mServerFormat = dateformate;

        SimpleDateFormat mTargetFormat = tformate;
        String ruturnDateFormate = "";
        try {

//                        mServerFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = mServerFormat.parse(date);
            mTargetFormat.setTimeZone(TimeZone.getDefault());
            ruturnDateFormate = mTargetFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ruturnDateFormate;
    }

    public static String getCustomDateFormate(String inputDate, SimpleDateFormat mTargetFormatDate) {
        Date dateobj;
        //create a new Date object using the UTC timezone

//        SimpleDateFormat mServerFormat = new SimpleDateFormat("yyyy-mm-DD HH:mm:ss");
        SimpleDateFormat mServerFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        SimpleDateFormat mTargetFormat = mTargetFormatDate;
        String ruturnDateFormate = "";
        try {

            mServerFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = mServerFormat.parse(inputDate);
            mTargetFormat.setTimeZone(TimeZone.getDefault());
            ruturnDateFormate = mTargetFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ruturnDateFormate;
    }

    public static String isPasswordValid(Context context, String password) {
        int numOfSymbol = 0;
        int numOfUpperLetters = 0;
        int numOfLowerLetters = 0;
        int numOfDigits = 0;
        String errorMessage = " ";
        byte[] bytes = password.getBytes();
        for (byte tempByte : bytes) {
            if (tempByte >= 33 && tempByte <= 47) {
                numOfSymbol++;
            }

            char tempChar = (char) tempByte;
            if (Character.isDigit(tempChar)) {
                numOfDigits++;
            }

            if (Character.isUpperCase(tempChar)) {
                numOfUpperLetters++;
            }

            if (Character.isLowerCase(tempChar)) {
                numOfLowerLetters++;
            }

            if (password.length() < 7) {
                errorMessage = context.getString(R.string.password_length_validation);

            } else if (!((numOfUpperLetters > 0) && (numOfLowerLetters > 0))) {
                errorMessage = context.getString(R.string.password_alpha);
            } else if (!(numOfDigits > 0)) {
                errorMessage = context.getString(R.string.password_num);
            } else if (!(numOfSymbol > 0)) {
                errorMessage = context.getString(R.string.password_sym);
            } else {
                errorMessage = context.getString(R.string.password_valid);
            }


        }

        return errorMessage;
    }

    public static String getLocalTime(String inputDate) {
        Date dateobj;
        dateobj = null;
        String dateFormateInLocalTimeZone = "";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("MM-dd-YYYY HH:mm a", Locale.US);
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateFormateInLocalTimeZone;
    }

    public static String getLocalDate(String inputDate) {
        Date dateobj = null;
        String dateFormateInLocalTimeZone = "";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("MM-dd-yy", Locale.US);
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormateInLocalTimeZone;
    }

    public static String getDecodedmessage(String data) {

        try {

            return URLDecoder.decode(data, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        return data;

    }

    public static String getUTCtime() {

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

//Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

//Time in GMT
        //return dateFormatLocal.parse( dateFormatGmt.format(new Date()) );


        String gmtTime = dateFormatGmt.format(new Date());
        return gmtTime;
    }

    public static String encodeMessage(String data) {

        try {

            return URLEncoder.encode(data, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }

        return data;

    }

    public static String checkBetweentime(Context context, String strt) {


        String mStartDateStr = GlobalMethods.getLocalTimeChat(strt);
        SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        String event = mStartDateStr;
        String mCurrentDateStr = displayFormat
                .format(new Date());


        Log.d("current date", mCurrentDateStr);
        Log.d("current date", mStartDateStr);


        Date mCurrentdate = null, mStartDate = null, mEndDate = null;
        try {
            mStartDate = displayFormat.parse(mStartDateStr);
            mCurrentdate = displayFormat.parse(mCurrentDateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(mCurrentdate);
            cal.add(Calendar.DATE, -1);
            mEndDate = (cal.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStartDate.equals(mCurrentdate)) {
            event = context.getString(R.string.to_day);
        } else if (mStartDate.equals(mEndDate)) {
            event = context.getString(R.string.yest_day);
        } else {
            event = mStartDateStr;
        }
        return event;
    }

    public static String getLocalTimeChat(String inputDate) {
        Date dateobj;
        dateobj = null;
        String dateFormateInLocalTimeZone = "";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateFormateInLocalTimeZone;
    }


    public static String utcprintDifferenceAgo(Date startDate, Date endDate) {

        long different = startDate.getTime() - endDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        String remainTime = "";
        if (elapsedDays > 0) {
            if (elapsedDays == 1) {
                remainTime = elapsedDays + " day ago";
            } else {
                remainTime = elapsedDays + " days ago";
            }
        } else if (elapsedHours > 0) {
            // if (elapsedMinutes > 0) {
            // // remainTime = elapsedMinutes + " min remaining";
            // String mins = elapsedMinutes + " mins";
            // remainTime = elapsedHours + " hours \n" + mins;
            //
            // } else {
            if (elapsedHours == 1) {
                remainTime = elapsedHours + " hour ago";
            } else {
                remainTime = elapsedHours + " hours ago";
            }
            // }
        } else if (elapsedMinutes > 0) {
            remainTime = elapsedMinutes + " mins ago";

        } else if (elapsedSeconds > 0) {
            remainTime = elapsedSeconds + " sec ago";
        } else {
            remainTime = "Just now";
        }
        // String remainTime = elapsedDays + " Days " + elapsedHours + " hours "
        // + elapsedMinutes + " Min Remaining";
        return remainTime;

    }

    public static String getStringValue(Context context, String key) {

        return (String) getValueFromPreference(context,
                STRING_PREFERENCE, key);


    }


    public static boolean isLoggedIn(Context context) {
        return (Boolean) getValueFromPreference(context,
                BOOLEAN_PREFERENCE, AppConstants.ISLOGGEDIN);
    }


    public static String getPriValWithTwoPoint(String price, boolean mTwoPoint) {
        String returnPrice = price;
        try {
//            returnPrice = String.valueOf(Math.round(Float.valueOf(returnPrice)));
            returnPrice = round(returnPrice);

        } catch (Exception e) {
            returnPrice = price;
        }

        if (mTwoPoint) {
            returnPrice = String.format(Locale.ENGLISH,"%.2f", Double.parseDouble(returnPrice));
        }
        return returnPrice;

    }

    public static String round(String roun) {
        double d;
        d = Double.valueOf(roun);
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return String.valueOf(d < 0 ? -i : i);
        } else {
            return String.valueOf(d < 0 ? -(i + 1) : i + 1);
        }
    }

    public static String afterTwoPointVal(String price) {
//        String checkZeroPrice = price;
//
//
//        try {
//            checkZeroPrice=String.format("%.2f",price);
////            String pri[] = checkZeroPrice.split("\\.");
////            if (pri[0].equalsIgnoreCase("00") && pri[1].equalsIgnoreCase("00") || pri[0].equalsIgnoreCase("00") && pri[1].equalsIgnoreCase("0")) {
////                checkZeroPrice = AppConstants.FAILURE_CODE;
////
////            } else if (pri[1].equalsIgnoreCase("00") || pri[1].equalsIgnoreCase("0")) {
////                checkZeroPrice = pri[0];
////
////            }
//        } catch (Exception e) {
//            checkZeroPrice = price;
//        }
        return String.format(Locale.ENGLISH,"%.2f", Double.parseDouble(price));
    }


//    public static String afterTwoPointVal(String price) {
//        String checkZeroPrice = price;
//
//        try {
//
//            String pri[] = checkZeroPrice.split("\\.");
//            if (pri[0].equalsIgnoreCase("00") && pri[1].equalsIgnoreCase("00") || pri[0].equalsIgnoreCase("00") && pri[1].equalsIgnoreCase("0")) {
//                checkZeroPrice = AppConstants.FAILURE_CODE;
//
//            } else if (pri[1].equalsIgnoreCase("00") || pri[1].equalsIgnoreCase("0")) {
//                checkZeroPrice = pri[0];
//
//            }
//        } catch (Exception e) {
//            checkZeroPrice = price;
//        }
//        return checkZeroPrice;
//    }


    public static boolean isSeller(Context context, String userId) {
        return userId.equalsIgnoreCase(getUserID(context));
    }

    public static String isGoodsService(Context context, String itemType) {
        return itemType.equalsIgnoreCase(context.getString(R.string.one)) ? context.getString(R.string
                .item_name) : context.getString(R.string.serv_name);
    }

    public static String getDeliveryType(Context context, String deliveryType) {
        return deliveryType.equalsIgnoreCase("1") ? context.getString(R
                .string.deltype_inper) : context.getString(R.string.electron);
    }

    public static boolean isUserVerified(Context context, String userVerify) {
        return userVerify.equalsIgnoreCase(context.getString(R.string.one));
    }

    public static boolean isCertifiedPartner(Context context, String itemType, String patnerType) {
        boolean certPartner = false;

        if (itemType.equalsIgnoreCase(context.getString(R.string.one)) && patnerType.equalsIgnoreCase(context.getString(R.string.one)) || itemType.equalsIgnoreCase(context.getString(R.string.two)) && patnerType.equalsIgnoreCase(context.getString(R.string.two))) {
            //Is Goods Partner or s Service Partner
            certPartner = true;
        }
        return certPartner;
    }

    public static float isRating(String mRating) {
        float mRati = 0.0f;

        try {
            if (mRating != null && !mRating
                    .equalsIgnoreCase("")) {
                mRati = Float.parseFloat(mRating);
            }

        } catch (Exception ignored) {

        }
        return mRati;
    }

    public static void setBadge(Context mContext, int count) {
        if(count!=0){
        String launcherClassName = getLauncherClassName(mContext);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", mContext.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        mContext.sendBroadcast(intent);
    }
    }

    public static String getLauncherClassName(Context mContext) {

        PackageManager pm = mContext.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(mContext.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public static void shareTxt(Context context) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string
                .share_txt) + "\n\n\t" + context.getString(R.string.android_app) + " " + AppConstants.BASE_APP
                + "\n\n\t" + context.getString(R.string.ios_app) + " " + AppConstants.IOS_BASE_APP);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));


    }

}

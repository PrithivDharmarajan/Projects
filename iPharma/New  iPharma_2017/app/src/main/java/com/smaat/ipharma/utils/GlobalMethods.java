package com.smaat.ipharma.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.AlarmEntity;
import com.smaat.ipharma.entity.LocalTimeEntitiy;
import com.smaat.ipharma.entity.OnetouchResult;
import com.smaat.ipharma.entity.RegisterationResponse;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.ui.LoginScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.smaat.ipharma.main.BaseActivity.mActivity;


public class GlobalMethods {
    public static int STRING_PREFERENCE = 1;
    public static int INT_PREFERENCE = 2;
    public static int BOOLEAN_PREFERENCE = 3;
    public static int ARRAY_LIST_PREFERENCE = 4;
    public static int LONG_PREFERENCE = 5;
    public static ArrayList<Boolean> langselect = new ArrayList<Boolean>();


    public static void storeValuetoPreference(Context context, int preference, String key, Object value) {
        SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
                Context.MODE_PRIVATE);
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
        if (preference == ARRAY_LIST_PREFERENCE) {
            Gson gson = new Gson();
            String arrayList1 = gson.toJson(value);
            edit.putString(key, arrayList1);
        }
        edit.commit();

    }
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    public static void UpdateUserDetails(Context context,
                                         RegisterationResponse registerationResponse) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID,
                registerationResponse.getUserID());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_NAME,
                registerationResponse.getFullName());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
                registerationResponse.getEmail());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUMBER,
                registerationResponse.getPhone());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.CITY,
                registerationResponse.getCity());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.AREA,
                registerationResponse.getArea());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.USER_AREA_PINCODE,
                registerationResponse.getPincode());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ADDRESS,
                registerationResponse.getAddress());
    }


    public static void UpdateShopDetails(Context context,
                                         OnetouchResult registerationResponse) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PharmacyID_OT,
                registerationResponse.getPharmacyID());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.ShopUserName_OT,
                registerationResponse.getShopUserName());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.ShopPassword_OT,
                registerationResponse.getShopPassword());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.ShopName_OT,
                registerationResponse.getShopName());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.ShopCategory_OT,
                registerationResponse.getShopCategory());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.OwnerName_OT,
                registerationResponse.getOwnerName());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.ShopIcon_OT,
                registerationResponse.getShopIcon());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Address_OT,
                registerationResponse.getAddress());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.City_OT,
                registerationResponse.getCity());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Area_OT,
                registerationResponse.getArea());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Pincode_OT,
                registerationResponse.getPincode());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Landmark_OT,
                registerationResponse.getLandmark());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Latitude_OT,
                registerationResponse.getLatitude());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Longitude_OT,
                registerationResponse.getLongitude());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Email_OT,
                registerationResponse.getEmail());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Phone_OT,
                registerationResponse.getPhone());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Mobile_OT,
                registerationResponse.getMobile());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.Website_OT,
                registerationResponse.getWebsite());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.OpenTime_OT,
                registerationResponse.getOpenTime());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.CloseTime_OT,
                registerationResponse.getCloseTime());
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.ProfilePic_OT,
                registerationResponse.getProfilePic());

    }


    public static JSONArray getJsonArrayFromJsonObject(JSONObject jsonObject,
                                                       String key) {
        try {
            if (jsonObject.getString(key) == null
                    || jsonObject.getString(key).equalsIgnoreCase("-1")) {
                return new JSONArray();
            } else {
                return jsonObject.getJSONArray(key);
            }

        } catch (JSONException jSONException)

        {
            return new JSONArray();
        }
    }

    public static String getUserID(Context context) {
        String UserID = (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID);

        return UserID;
    }

    public static void clearAllPreferences(Context context)
    {


        SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.clear();
        edit.commit();
        edit.putString(AppConstants.TUT_SCREEN,AppConstants.SUCCESS_CODE);
        edit.commit();
        Intent mIntent = new Intent(context, HomeScreen.class);
        BaseFragment.mActivity.startActivity(mIntent);
        BaseFragment.mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        BaseFragment.mActivity.finish();

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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static boolean isLoggedIn(Context mcontext) {
        boolean islogin = false;
        if(!GlobalMethods.getStringValue(mcontext,AppConstants.USER_ID).isEmpty())
        {
            islogin = true;
        }else{
            islogin = false;
        }
        return islogin;
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;

        }
        return isValid;
    }

    public static float setRatingValue(String mRating) {
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


    public static String convertingcurrenttimetoutc(String time) {

        //String inputPattern = "dd-MM-yyyy hh:mm:ss a";
        //String outputPattern = "dd-MM-yyyy hh:mm:ss a";

        String inputPattern = "";

        inputPattern = "dd-MM-yy";



        String outputPattern = "";

        outputPattern = "dd-MM-yyyy";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertingcurrenttimetoutctime(String time) {

        //String inputPattern = "dd-MM-yyyy hh:mm:ss a";
        //String outputPattern = "dd-MM-yyyy hh:mm:ss a";

        String inputPattern = "";

        inputPattern = "dd-MM-yy";



        String outputPattern = "";

        outputPattern = "dd-MM-yyyy";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String convertingtimeformat(String time) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String str = "";
        Date date = null;
        try {
            DesiredFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = sourceFormat.parse(time);
            str = DesiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }


    public static String convertingutctocurrenttime(String time) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("hh:mm a,dd-MMM-yyyy", Locale.US);
        String str = "";
        Date date = null;
        try {
            sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = sourceFormat.parse(time);
            str = DesiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str.toUpperCase(Locale.ENGLISH);
    }




    public static boolean isValidMobile(String phone)
    {
        boolean check;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 20)
            {
                check = false;

            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }

    public static Object getValueFromPreference(Context context, int preference, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
                Context.MODE_PRIVATE);

        if (preference == STRING_PREFERENCE) {
            return (Object) sharedPreference.getString(key, "");
        }
        if (preference == INT_PREFERENCE) {
            return (Object) sharedPreference.getInt(key, 0);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            return (Object) sharedPreference.getBoolean(key, false);
        }
        if (preference == ARRAY_LIST_PREFERENCE) {


            String arrayList = sharedPreference.getString(key, null);

            return (Object) arrayList;
        }

        return null;

    }

    public static void userDetails(String userId, String
            userName, String emailId, String password, String phoneNumber, String countryCode, String loginType, String profileurl, Context context) {

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID, userId);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_NAME,
                userName);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.EMAIL_ADDRESS,
                emailId);

        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PASSWORD,
                password);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.PHONE_NUMBER,
                phoneNumber);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.COUNTRY_CODE,
                countryCode);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.LOGIN_TYPE, loginType);
        GlobalMethods.storeValuetoPreference(context,
                GlobalMethods.STRING_PREFERENCE,
                AppConstants.Profile_Pic_url, profileurl);

    }


    public static String getLocalTime(String inputDate) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        String lv_localTimeZone = "GMT";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
//            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a,dd-MMM-yyyy", Locale.US);

            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }

    public static String getLocalTimeinAMPM(String inputDate) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        String lv_localTimeZone = "GMT";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }

    public static String getUTCTime(String inputDate) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Inputformat.setTimeZone(TimeZone.getDefault());
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");
            displayFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }


    @SuppressWarnings("deprecation")
    public static String getLocalCorrectTime(String inputDate, String inputTime) {
        String returnTime = "";
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);

            inputDate = inputDate.split("T")[0];

            Date d = new Date();
            Date d1 = new Date();
            try {
                d = inputDateFormat.parse(inputDate);
                d1 = inputTimeFormat.parse(inputTime);

                d.setHours(d1.getHours());
                d.setMinutes(d1.getMinutes());

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(d);

                String timeZone = getDeviceTimeZone();

                if (timeZone.substring(0, 1).toString().equalsIgnoreCase("+")) {

                    calendar.add(Calendar.HOUR, Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, Integer.parseInt(timeZone.substring(3, 5)));
                } else {

                    calendar.add(Calendar.HOUR, -Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, -Integer.parseInt(timeZone.substring(3, 5)));
                }

                d = calendar.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            returnTime = displayFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnTime;

    }

    public static String getLocalDisplayformat(String inputDate) {
        String returnTime = "";
        try {

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.US);


            Date d = new Date();

            try {
                d = inputDateFormat.parse(inputDate);

                returnTime = displayFormat.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    public static String getLocalDisplayformat1(String inputDate) {
        String returnTime = "";
        try {

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.US);


            Date d = new Date();

            try {
                d = inputDateFormat.parse(inputDate);

                returnTime = displayFormat.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnTime;
    }


    @SuppressWarnings("deprecation")
    public static String getServerCorrectTime(String inputDate, String inputTime) {
        String returnTime = "";
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

            SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm", Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            inputDate = inputDate.split("T")[0];

            Date d = new Date();
            Date d1 = new Date();
            try {
                d = inputDateFormat.parse(inputDate);
                d1 = inputTimeFormat.parse(inputTime);

                d.setHours(d1.getHours());
                d.setMinutes(d1.getMinutes());

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(d);

                String timeZone = getDeviceTimeZone();

                if (timeZone.substring(0, 1).toString().equalsIgnoreCase("+")) {

                    calendar.add(Calendar.HOUR, -Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, -Integer.parseInt(timeZone.substring(3, 5)));
                } else {

                    calendar.add(Calendar.HOUR, Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, Integer.parseInt(timeZone.substring(3, 5)));
                }

                d = calendar.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            returnTime = displayFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnTime;

    }

    public static String getDeviceTimeZone() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("Z");
        String localTime = dateFormat.format(currentLocalTime);

        return localTime;
    }

    public static String getUTCtime() {

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

//Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//Time in GMT
        //return dateFormatLocal.parse( dateFormatGmt.format(new Date()) );


        String gmtTime = dateFormatGmt.format(new Date());
        return gmtTime;
    }

    /*public static File storeImage(Bitmap imageData, String filename) {
        // get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory().getPath() + File.separator;
        String filePath = iconsStoragePath + filename;

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            // choose another format if PNG doesn't suit you
            imageData.compress(CompressFormat.PNG, 100, bos);
            Log.e("Succ", "Succ");
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
        }
        return new File(filePath);

    }*/
    public static WebView mTemsWebView;







    public static String getStringValue(Context context, String Key) {

        String mString = (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, Key);

        return mString;

    }

    public static void blink_animation(Context context, View view){
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(context,
                R.anim.fade);
        view.startAnimation(myFadeInAnimation);
    }


    public  static String storeImage(Bitmap image, String fileName,Context ctx) {
        File pictureFile = getOutputMediaFile(fileName,ctx);
        if (pictureFile == null) {
            //Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return "";
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            //Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            //Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return pictureFile.getAbsolutePath();
    }

    public static  File getOutputMediaFile(String fileName,Context ctxt) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + "/Android/data/"
                        + ctxt.getPackageName() + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name

        File mediaFile;
        String mImageName = "IPharma_" + fileName + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + mImageName);
        return mediaFile;
    }


    public static ArrayList<LocalTimeEntitiy> getAlarmlist(Context context) {
        SharedPreferences settings;
        List<LocalTimeEntitiy> favorites = new ArrayList<>();

        String jsonFavorites = GlobalMethods.getStringValue(context, AppConstants.ALARM_ARRAY);
        if (!jsonFavorites.isEmpty()) {
            Gson gson = new Gson();
            LocalTimeEntitiy[] favoriteItems = gson.fromJson(jsonFavorites, LocalTimeEntitiy[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<LocalTimeEntitiy>(favorites);
        }

        return (ArrayList<LocalTimeEntitiy>) favorites;
    }

    public static ArrayList<String> getAddressList(Context context) {
        SharedPreferences settings;
        List<String> favorites = new ArrayList<>();

        String jsonFavorites = GlobalMethods.getStringValue(context, AppConstants.ADDRESS_ARRAY);
        if (!jsonFavorites.isEmpty()) {
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites, String[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<String>(favorites);
        }

        return (ArrayList<String>) favorites;
    }

    public static void storeAddress(Context context,String address)
    {
        boolean addresscontains = false;
        List<String> alarmlist = GlobalMethods.getAddressList(context);
        Log.e("LISTADDRESS","LIST"+alarmlist.size());
        if (alarmlist.size()==0)
        {
        alarmlist = new ArrayList<String>();
        alarmlist.add(GlobalMethods.getStringValue(context,AppConstants.COMMUNICATION_ADDRESS));
        Gson gson = new Gson();
        String Alarmdates = gson.toJson(alarmlist);
        GlobalMethods.storeValuetoPreference(context, STRING_PREFERENCE, AppConstants.ADDRESS_ARRAY, Alarmdates);
        }
        if(!address.equalsIgnoreCase(""))
        {

            for(int i = 0;i<alarmlist.size();i++)
            {
                Log.e("LISTADDRESS","LIST"+alarmlist.get(i));
                Log.e("LISTADDRESS","VALUE"+address);
                Log.e("LISTADDRESS","LIST"+alarmlist.size());
                if(alarmlist.get(i).equalsIgnoreCase(address))
                {
                    addresscontains = true;
                    break;
                }
            }

            if(!addresscontains)
            {
                alarmlist.add(address);
                Gson gson = new Gson();
                String Alarmdates = gson.toJson(alarmlist);
                GlobalMethods.storeValuetoPreference(context, STRING_PREFERENCE, AppConstants.ADDRESS_ARRAY, Alarmdates);
            }
        }
    }

    public static boolean movetologinscreen(Activity ctx)
    {
        Log.e("IS_LOGGEDINIS_LOGGEDIN","IS_LOGGEDINIS_LOGGEDIN"+(Boolean)GlobalMethods.getValueFromPreference(ctx,BOOLEAN_PREFERENCE,AppConstants.IS_LOGGEDIN));
        if(!(Boolean)GlobalMethods.getValueFromPreference(ctx,BOOLEAN_PREFERENCE,AppConstants.IS_LOGGEDIN))
        {
            Intent i = new Intent(ctx,LoginScreen.class);
            ctx.startActivity(i);
            ctx.overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            return true;
        }

        return  false;
    }

    public static String CalculationByDistancedouble(String lat, String lang) {

        GPSTracker gpstrack;
        gpstrack = new GPSTracker(mActivity);
        double lat1 = Double.parseDouble(lat);
        double lat2 = gpstrack.getLatitude();
        double lon1 = Double.parseDouble(lang);
        double lon2 = gpstrack.getLongitude();

        Location startPoint=new Location("locationA");
        startPoint.setLatitude(lat1);
        startPoint.setLongitude(lon1);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat2);
        endPoint.setLongitude(lon2);

        //return startPoint.distanceTo(endPoint)/1000;
        return String.valueOf(String.format(Locale.US,"%.1f", startPoint.distanceTo(endPoint)/1000));
    }


}
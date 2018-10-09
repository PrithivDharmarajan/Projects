package com.fautus.fautusapp.utils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by sys on 19-Apr-17.
 */

public class DateUtil {

    /*custom date format*/
    public static String getCustomDateFormat(String inputDateStr, SimpleDateFormat inputDateFormat, SimpleDateFormat targetDateFormat) {
        String returnDateFormat = "";
        if (!inputDateStr.isEmpty()) {

            try {
                inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dateObj = inputDateFormat.parse(inputDateStr);
                targetDateFormat.setTimeZone(TimeZone.getDefault());
                returnDateFormat = targetDateFormat.format(dateObj);
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
        return returnDateFormat;
    }

    /*Convert string to date*/
    public static Date getDateFromString(String inputDateStr, SimpleDateFormat inputDateFormat) {

        Date returnDate = null;
        try {
            returnDate = inputDateFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    /*Convert date to string*/
    public static String getStringFromDate(Date inputDateStr, SimpleDateFormat inputDateFormat) {
        return inputDateFormat.format(inputDateStr);
    }
}

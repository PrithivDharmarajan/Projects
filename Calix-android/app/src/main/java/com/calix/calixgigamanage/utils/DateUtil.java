package com.calix.calixgigamanage.utils;

import android.content.Context;
import android.util.Log;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.CalixApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    /*custom date format*/
    public static String getCustomDateAndTimeFormat(long inputDateStr, SimpleDateFormat targetDateFormat) {
        String returnDateFormat = "";
        try {
            SimpleDateFormat inputDateFormat = AppConstants.SERVER_DATE_TIME_FORMAT;
//                inputDateFormat.setTimeZone(TimeZone.getDefault());
            Date dateObj = inputDateFormat.parse(inputDateFormat.format(inputDateStr));
            targetDateFormat.setTimeZone(TimeZone.getDefault());
            returnDateFormat = targetDateFormat.format(dateObj);
        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.getMessage());
        }

        return returnDateFormat;
    }


    /*custom date format*/
    public static String getCustomDateFormat(String inputDateStr, SimpleDateFormat inputDateFormat, SimpleDateFormat targetDateFormat) {
        String returnDateFormat = "";
        if (!inputDateStr.isEmpty()) {

            try {
//                inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date dateObj = inputDateFormat.parse(inputDateStr);
//                targetDateFormat.setTimeZone(TimeZone.getDefault());
                returnDateFormat = targetDateFormat.format(dateObj);
            } catch (Exception e) {
                Log.e(AppConstants.TAG, e.getMessage());
            }
        }
        return returnDateFormat;
    }

    /*Convert string to date*/
    public static Long getMileSecFromDate(String inputDateStr, SimpleDateFormat inputDateFormat) {

        long timeInMilSecLong = 0;
        try {
            Date mDate = inputDateFormat.parse(inputDateStr);
            timeInMilSecLong = mDate.getTime();
        } catch (ParseException e) {
            Log.e(AppConstants.TAG, e.getMessage());
        }

        return timeInMilSecLong;

    }

    public static String getTimeDifference(String dateFilterTypeStr, long dateLong) {

        String offerTimeStr = "";
        long different = System.currentTimeMillis() - dateLong;
        if (different > 0) {
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long WeekInMilli = daysInMilli * 7;
            long monthInMilli = daysInMilli * 30;
            long yearInMilli = monthInMilli * 12;

            long elapsedYear = different / yearInMilli;

            long elapsedMonth = different / monthInMilli;


            long elapsedDays = different / daysInMilli;

            long elapsedWeek = different / WeekInMilli;

            long elapsedHours = different / hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;

            offerTimeStr = getCustomDateAndTimeFormat(dateLong, AppConstants.GRAPH_DATE_FORMAT);

            if (dateFilterTypeStr.contains(CalixApplication.getContext().getString(R.string.min))) {
                if (elapsedMinutes == 0) offerTimeStr=CalixApplication.getContext().getString(R.string.just_now);
                    else offerTimeStr = elapsedMinutes + " " + (elapsedMinutes == 1 ? CalixApplication.getContext().getString(R.string.min) : CalixApplication.getContext().getString(R.string.mins));
            } else if (dateFilterTypeStr.contains(CalixApplication.getContext().getString(R.string.hour))) {
                if (elapsedHours == 0) offerTimeStr=CalixApplication.getContext().getString(R.string.current_hour);
                else offerTimeStr = elapsedHours + " " + (elapsedHours == 1 ? CalixApplication.getContext().getString(R.string.hour) : CalixApplication.getContext().getString(R.string.hours));
            } else if (dateFilterTypeStr.contains(CalixApplication.getContext().getString(R.string.day))) {
                if (elapsedDays == 0) offerTimeStr=CalixApplication.getContext().getString(R.string.current_day);
                else  offerTimeStr = elapsedDays + " " + (elapsedDays == 1 ? CalixApplication.getContext().getString(R.string.day) : CalixApplication.getContext().getString(R.string.days));
            } else if (dateFilterTypeStr.contains(CalixApplication.getContext().getString(R.string.week))) {
                if (elapsedWeek == 0) offerTimeStr=CalixApplication.getContext().getString(R.string.current_week);
                else   offerTimeStr = elapsedWeek + " " + (elapsedWeek == 1 ? CalixApplication.getContext().getString(R.string.week) : CalixApplication.getContext().getString(R.string.weeks));
            } else if (dateFilterTypeStr.contains(CalixApplication.getContext().getString(R.string.month))) {
                if (elapsedMonth == 0) offerTimeStr=CalixApplication.getContext().getString(R.string.current_month);
                else  offerTimeStr = elapsedMonth + " " + (elapsedMonth == 1 ? CalixApplication.getContext().getString(R.string.month) : CalixApplication.getContext().getString(R.string.months));
            } else if (dateFilterTypeStr.contains(CalixApplication.getContext().getString(R.string.year))) {
                if (elapsedYear == 0) offerTimeStr=CalixApplication.getContext().getString(R.string.current_year);
                else   offerTimeStr = elapsedYear + " " + (elapsedYear == 1 ? CalixApplication.getContext().getString(R.string.year) : CalixApplication.getContext().getString(R.string.years));
            }
        }

        return offerTimeStr;

    }


    /*Get Wishes*/
    public static String getWishesMsg(Context context) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            return context.getString(R.string.gud_morn);
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            return context.getString(R.string.gud_afternoon);
        } else {
            return context.getString(R.string.gud_evn);
        }
    }


}

package com.smaat.renterblock.utils;

import android.text.format.DateUtils;
import android.util.Log;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.RBApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


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


    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0); // set millisecond in second

        return cal; // return the date part
    }

    public static String timeDiff(String dateTime) {

        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String cDateTime = dateFormats.format(new Date());

        System.out.println(dateTime);
        Date date = new Date();
        Date date1 = new Date();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        long daysBetween = 0;
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        try {
            date = format.parse(dateTime);
            date1 = format.parse(cDateTime);

            Calendar sDate = getDatePart(date);
            Calendar eDate = getDatePart(date1);

            while (sDate.before(eDate)) {
                sDate.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }

            calendar.setTime(date);

            long currentTime = System.currentTimeMillis();
            int localOffset = TimeZone.getDefault().getOffset(currentTime);
            int gmtOffset = TimeZone.getTimeZone("GMT").getOffset(currentTime);
            int minDiff = ((localOffset - gmtOffset) / (1000 * 60)) % 60;
            int hourDiff = (localOffset - gmtOffset) / (1000 * 60 * 60);
            calendar.add(Calendar.HOUR, hourDiff);
            calendar.add(Calendar.MINUTE, minDiff);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String result = (String) DateUtils.getRelativeTimeSpanString(calendar.getTimeInMillis(),
                System.currentTimeMillis(), 0);

        // String result = "Last updated " + daysBetween+" days ago";
        return result;
    }
	
    public static Date getDate(String dateString) {
        SimpleDateFormat df;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = null;
        try {

            date = df.parse(dateString);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }
	
	
	 /*get time difference*/
    public static String getTimeDifference(String cmdDateStr) {
        Date mOfferDate = null, mCurrentDate = null;

        String differenceTimeStr = "";
        try {

//            AppConstants.SERVER_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
            String mCurrentDate1 = AppConstants.SERVER_DATE_FORMAT.format(new Date());

            mOfferDate = AppConstants.SERVER_DATE_FORMAT.parse(cmdDateStr);

            mCurrentDate = AppConstants.SERVER_DATE_FORMAT.parse(mCurrentDate1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mOfferDate != null && mCurrentDate != null) {
            //milliseconds
            long different = mCurrentDate.getTime() - mOfferDate.getTime();
            if (different > 0) {
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;
                long monthInMilli = daysInMilli * 30;
                long yearInMilli = monthInMilli * 12;

                long elapsedYear = different / yearInMilli;
                different = different % yearInMilli;

                long elapsedMonth = different / monthInMilli;
                different = different % monthInMilli;

                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;
                differenceTimeStr = RBApplication.getContext().getString(R.string.just_now);

                if (elapsedYear > 0) {
                    differenceTimeStr = elapsedYear + " " + (elapsedYear == 1 ? RBApplication.getContext().getString(R.string.year_ago) : RBApplication.getContext().getString(R.string.years_ago));
                } else if (elapsedMonth > 0) {
                    differenceTimeStr = elapsedMonth + " " + (elapsedMonth == 1 ? RBApplication.getContext().getString(R.string.month_ago) : RBApplication.getContext().getString(R.string.months_ago));
                } else if (elapsedDays > 0) {
                    differenceTimeStr = elapsedDays + " " + (elapsedDays == 1 ? RBApplication.getContext().getString(R.string.day_ago) : RBApplication.getContext().getString(R.string.days_ago));
                } else if (elapsedHours > 0) {
                    differenceTimeStr = elapsedHours + " " + (elapsedHours == 1 ? RBApplication.getContext().getString(R.string.hour_ago) : RBApplication.getContext().getString(R.string.hours_ago));
                } else if (elapsedMinutes > 0) {
                    differenceTimeStr = elapsedMinutes + " " + (elapsedMinutes == 1 ? RBApplication.getContext().getString(R.string.min_ago) : RBApplication.getContext().getString(R.string.minutes_ago));
                } else if (elapsedSeconds > 0) {
                    differenceTimeStr = elapsedSeconds + " " + RBApplication.getContext().getString(R.string.sec_ago);
                }

            }
        }
        return differenceTimeStr;

    }

    public static String getConvertDateTimeFormat(String inputDateStr) {
        String outputText = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat
                ("MM/dd/yyyy hh:mm a", Locale.US);

        SimpleDateFormat outputFormat =
                new SimpleDateFormat("yyyy-MM-dd  HH:mm",Locale.US);
        // Adjust locale and zone appropriately
        try {
            Date date = inputFormat.parse(inputDateStr);
            outputText = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputText;
    }

}

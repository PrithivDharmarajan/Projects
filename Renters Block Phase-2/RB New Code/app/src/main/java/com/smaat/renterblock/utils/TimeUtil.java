package com.smaat.renterblock.utils;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.RBApplication;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {

    public static String lastSearchTimeCalculation(String time) {
        String PropertyTime = "", CurrentTime = "";
        int days, hours, min, sec;
        String strFinalTime = "";

//         Last Searching time
            PropertyTime = getLocalTime(time);

            Date Time = new Date();
            String currentTime = String.valueOf(CurrentTime);

            CurrentTime = getLocalTime(currentTime);
            SimpleDateFormat TimeDifference = new SimpleDateFormat("HH:mm:ss", Locale.US);


            try {
                Date Time1 = TimeDifference.parse(time);
                Date Time2 = TimeDifference.parse(PropertyTime);

                long FinalTime = Time1.getTime() - Time2.getTime();


                days = (int) (FinalTime / (1000 * 60 * 60 * 24));
                hours = (int) ((FinalTime - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                min = (int) (FinalTime - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                sec = (int) (FinalTime - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours) - (1000 * 60 * min)) / (60 * 60);

                if (days > 0) {
                    if (days == 1) {
                        strFinalTime = days + " day ago";
                    } else {
                        strFinalTime = days + " days ago";
                    }
                } else if (hours > 0) {
                    if (hours == 1) {
                        strFinalTime = hours + " hour ago";
                    } else {
                        strFinalTime = hours + " hours ago";
                    }
                } else if (min > 0) {
                    if (min == 1) {
                        strFinalTime = min + " min ago";
                    } else {
                        strFinalTime = min + " mins ago";
                    }
                } else if (sec > 0) {
                    if (sec == 1) {
                        strFinalTime = sec + " sec ago";
                    } else {
                        strFinalTime = sec + " secs ago";
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return strFinalTime;
        }

    public static String getLocalTime(String dateTime) {
        String time = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat
                ("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        SimpleDateFormat output = new SimpleDateFormat("HH:mm:ss", Locale.US);

        try {
            Date date = inputFormat.parse(dateTime);
            time = output.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String timeDiff(String dateTime) {

        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String cDateTime = dateFormats.format(new Date());

        System.out.println(dateTime);
        Date date = new Date();
        Date date1 = new Date();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);

        try {
            date = format.parse(dateTime);
            date1 = format.parse(cDateTime);

            calendar.setTime(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = String.valueOf(date);
//		 String result = "Last updated " + daysBetween+" days ago";
        return time;
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
}

package com.smaat.renterblock.utils;

import android.location.Location;
import android.util.Log;

import java.util.Locale;

/**
 * Created by sys on 26-Apr-17.
 */

public class NumberUtil {


    public static String afterTwoPointValueStr(String priceStr) {
        return String.format(Locale.US, "%.2f", Double.parseDouble(priceStr));
    }

    public static String withOutPointValusStr(String priceStr) {
        return String.format(Locale.US, "%.0f", Double.parseDouble(priceStr));
    }

    public static String roundValueStr(String roundStr) {
        double normalDouble = Double.valueOf(roundStr);
        double mathDouble = Math.abs(normalDouble);
        int mathInt = (int) mathDouble;
        double result = mathDouble - (double) mathInt;
        if (result < 0.5) {
            return String.valueOf(normalDouble < 0 ? -mathInt : mathInt);
        } else {
            return String.valueOf(normalDouble < 0 ? -(mathInt + 1) : mathInt + 1);
        }
    }

    public static float calculationByDistance(double firstPointLatDouble, double firstPointLngDouble,
                                              double secondPointLatDouble, double secondPointLngDouble) {

        Location firstPointLocation = new Location(AppConstants.FACEBOOK);
        firstPointLocation.setLatitude(firstPointLatDouble);
        firstPointLocation.setLongitude(firstPointLngDouble);

        Location secondPointLocation = new Location(AppConstants.GOOGLE);
        secondPointLocation.setLatitude(secondPointLatDouble);
        secondPointLocation.setLongitude(secondPointLngDouble);

        // distance = locationA.distanceTo(locationB);   // in meters
        return firstPointLocation.distanceTo(secondPointLocation) / 1000;   // in km
    }


    public static float getRatingVal(String ratingStr) {
        float ratingFloat = 0.0f;

        try {
            if (ratingStr != null && !ratingStr
                    .equalsIgnoreCase("")) {
                ratingFloat = Float.parseFloat(ratingStr);
            }

        } catch (Exception ignored) {
            Log.e(AppConstants.TAG, ignored.toString());
        }
        return ratingFloat;
    }

}

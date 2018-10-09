package com.fautus.fautusapp.utils;

import java.util.Locale;
import java.util.Random;

/**
 * Created by sys on 26-Apr-17.
 */

public class NumberUtil {


    private static final double mAssumedInitLatLongDiff = 1.0;
    private static final float mAccuracy = 0.01f;

    public static String afterTwoPointVal(String price) {
        return String.format(Locale.US, "%.2f", Double.parseDouble(price));
    }

    public static String withOutPointVal(String price) {
        return String.format(Locale.US, "%.0f", Double.parseDouble(price));
    }
    public static int generateRandomNum() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}

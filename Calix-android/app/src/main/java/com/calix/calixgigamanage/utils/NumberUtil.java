package com.calix.calixgigamanage.utils;

import android.content.Context;

import java.text.DecimalFormat;


public class NumberUtil {

    /*Init dialog instance*/
    private static final NumberUtil sNumberUtilInstance = new NumberUtil();


    public static NumberUtil getInstance() {
        return sNumberUtilInstance;
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /*find scale type name*/
    public String usageScaleValStr(String speedStr) {
        double speedDouble = Double.valueOf(speedStr);
        return String.valueOf(new DecimalFormat("##.##").format((speedDouble >= 1024 ? speedDouble / 1024 : speedDouble)));
    }

    /*find scale type name*/
    public String speedTwoDigitsValStr(String speedStr) {
        double speedDouble = Double.valueOf(speedStr);
        return String.valueOf(new DecimalFormat("##.##").format( speedDouble));
    }
}

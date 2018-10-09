package com.calix.calixgigaspireapp.utils;

import android.content.Context;

import com.calix.calixgigaspireapp.R;

public class TextUtil {

    /*Init dialog instance*/
    private static final TextUtil sNumberUtilInstance = new TextUtil();


    public static TextUtil getInstance() {
        return sNumberUtilInstance;
    }

    public String capitalizeStr(String nameStr) {
        String[] strArray = nameStr.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }

        return builder.toString();
    }

    /*find the device sub type name*/
    public String deviceSubTypeNameStr(Context context, int deviceTypeInt, int deviceSubTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                switch (deviceSubTypeInt) {
                    case 1:
                        return context.getString(R.string.android);
                    case 2:
                        return context.getString(R.string.ios);
                    default:
                        return "";
                }
            case 2:
                switch (deviceSubTypeInt) {
                    case 0:
                        return context.getString(R.string.linux);
                    case 1:
                        return context.getString(R.string.windows);
                    case 2:
                        return context.getString(R.string.mac);
                    default:
                        return "";
                }
            default:
                return "";
        }
    }


    /*find scale type name*/
    public String deviceScaleNameStr(Context context, String speedStr) {
        double speedDouble = Double.valueOf(speedStr);
        if(speedDouble>=1024){
           return context.getString(R.string.mbytes);
        }else{
            return context.getString(R.string.mbytes);
        }
    }




}

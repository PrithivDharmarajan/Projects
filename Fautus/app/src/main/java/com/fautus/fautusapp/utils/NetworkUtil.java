package com.fautus.fautusapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sys on 19-Apr-17.
 */

public class NetworkUtil {

    /*Check internet connection */
    public static boolean isNetworkAvailable(Context mContext) {
        if (mContext != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();

        } else {
            return false;
        }
    }
}

package com.calix.calixgigamanage.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

/**
 * Created by user on 3/16/2018.
 */

public class WifiUtil {

    public static String getCurrentSSID(Context context) {
        String SSIDStr = "";
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connManager != null)
            networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo != null && networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = null;
            if (wifiManager != null) {
                connectionInfo = wifiManager.getConnectionInfo();
            }
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                SSIDStr = connectionInfo.getSSID();
            }
        }
        return SSIDStr;
    }
}

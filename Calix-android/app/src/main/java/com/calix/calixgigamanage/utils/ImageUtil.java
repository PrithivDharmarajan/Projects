package com.calix.calixgigamanage.utils;


import com.calix.calixgigamanage.R;

/**
 * Created by user on 2/13/2018.
 */

public class ImageUtil {

    /*Init dialog instance*/
    private static final ImageUtil sImageUtilInstance = new ImageUtil();


    public static ImageUtil getInstance() {
        return sImageUtilInstance;
    }

     /*deviceTypeInt type
      0 for unknown, 1 for phone, 2 for computer, 3 for console, 4 for storage, 5 for printer, 6 for television
     */

    /*find the device Image*/
    public int deviceImg(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                return R.drawable.phones;
            case 2:
                return R.drawable.computer;
            case 3:
                return R.drawable.console_violet;
            case 4:
                return R.drawable.storage_violet;
            case 5:
                return R.drawable.printer_violet;
            case 6:
                return R.drawable.television_violet;
            case 7:
                return R.drawable.network_violet;
            case 8:
                return R.drawable.camera_violet;
            default:
                return R.mipmap.ic_launcher;
        }
    }

    /*find the device Image*/
    public int connectedStatusViaRouterImg(boolean isDeviceConnectedStatusBool, String deviceConnectedStr) {
        return deviceConnectedStr.contains(AppConstants.ETHER_NET) ? (isDeviceConnectedStatusBool ? (R.drawable.ethernet_connection) : (R.drawable.ethernet_disconnection)):(isDeviceConnectedStatusBool ? (R.drawable.wifi_connection) : (R.drawable.wifi_disconnection));
    }


}

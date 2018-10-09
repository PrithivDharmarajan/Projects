package com.calix.calixgigaspireapp.utils;

import com.calix.calixgigaspireapp.R;

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
                return R.drawable.ic_devices_phone;
            case 2:
                return R.drawable.ic_device_computer;
            case 3:
                return R.drawable.ic_device_console;
            case 4:
                return R.drawable.ic_devices_storage;
            case 5:
                return R.drawable.ic_device_printer;
            case 6:
                return R.drawable.ic_device_television;
            default:
                return R.mipmap.push_launcher;
        }
    }

    /*find the device Image*/
    public int connectedStatusViaRouterImg(boolean isDeviceConnectedStatusBool, String deviceConnectedStr) {
        return deviceConnectedStr.contains(AppConstants.ETHER_NET) ? (isDeviceConnectedStatusBool ? (R.drawable.ethernet_connection) : (R.drawable.ethernet_disconnection)):(isDeviceConnectedStatusBool ? (R.drawable.wifi_icon) : (R.drawable.wifi_disconnect));
    }


}

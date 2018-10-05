package com.calix.calixgigamanage.utils;

/**
 * Created by user on 2/13/2018.
 */

public class DeviceTypeUtil {

    /*Init dialog instance*/
    private static final DeviceTypeUtil sDeviceTypeInstance = new DeviceTypeUtil();


    public static DeviceTypeUtil getInstance() {
        return sDeviceTypeInstance;
    }

     /*deviceTypeInt type
      0 for unknown, 1 for phone, 2 for computer, 3 for console, 4 for storage, 5 for printer, 6 for television
     */

    /*find the device Image*/
    private String deviceONOFFStatus(String deviceTypeStr) {
        switch (deviceTypeStr) {
            case "BinarySwitch":
            case "SmartACSwitch":
            case "Siren":
            case "UnknownOnOffModule":
            case "BinaryPowerSwitch":
            case "SecurifiSmartSwitch":
                return "1";
            case "MultilevelSwitchOnOff":
            case "ColorDimmableLight":
            case "Almond Siren":
            case "HueLamp":
                return "2";
            default:
                return "1";
        }
    }


}

package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/7/2018.
 */

public class IOTDeviceConfigResponse implements Serializable {

    private String securifiId = "";
    private String deviceId = "";
    private String deviceName = "";
    private String locationId = "";
    private String locationName = "";

    public String getSecurifiId() {
        return securifiId;
    }

    public void setSecurifiId(String securifiId) {
        this.securifiId = securifiId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}

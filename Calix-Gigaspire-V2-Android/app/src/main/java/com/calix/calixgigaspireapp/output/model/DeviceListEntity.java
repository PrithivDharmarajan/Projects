package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DeviceListEntity implements Serializable {

    private int deviceType;
    private ArrayList<DeviceEntity> devicesList;

    public int getDeviceType() {
        return deviceType;
    }

    public DeviceListEntity setDeviceType(int deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public ArrayList<DeviceEntity> getDevicesList() {
        return devicesList;
    }

    public DeviceListEntity setDevicesList(ArrayList<DeviceEntity> devicesList) {
        this.devicesList = devicesList;
        return this;
    }
}

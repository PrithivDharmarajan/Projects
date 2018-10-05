package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class DeviceListResponse implements Serializable {

    private ArrayList<DeviceEntity> devices;

    public ArrayList<DeviceEntity> getDevices() {
        return devices == null ? new ArrayList<DeviceEntity>() : devices;
    }

    public void setDevices(ArrayList<DeviceEntity> devices) {
        this.devices = devices;
    }
}

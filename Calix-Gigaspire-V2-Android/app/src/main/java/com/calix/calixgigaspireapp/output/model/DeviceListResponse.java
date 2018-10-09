package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DeviceListResponse implements Serializable {



    private int type = 0;
    private ArrayList<DeviceEntity> devices = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public ArrayList<DeviceEntity> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<DeviceEntity> devices) {
        this.devices = devices;
    }


}

package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class AddIOTDeviceResponse implements Serializable {

    private ArrayList<AddIOTDeviceEntity> deviceTypes = new ArrayList<>();

    public ArrayList<AddIOTDeviceEntity> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(ArrayList<AddIOTDeviceEntity> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }


}

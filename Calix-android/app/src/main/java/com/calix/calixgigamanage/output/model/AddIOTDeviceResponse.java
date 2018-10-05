package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class AddIOTDeviceResponse implements Serializable {

    private ArrayList<AddIOTDeviceEntity> deviceTypes;

    public ArrayList<AddIOTDeviceEntity> getDeviceTypes() {
        return deviceTypes == null ? new ArrayList<AddIOTDeviceEntity>() : deviceTypes;
    }

    public void setDeviceTypes(ArrayList<AddIOTDeviceEntity> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }


}

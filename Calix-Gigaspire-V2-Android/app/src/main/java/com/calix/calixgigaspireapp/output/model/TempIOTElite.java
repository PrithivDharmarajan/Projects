package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

public class TempIOTElite implements Serializable{

   private int deviceType;
   private int deviceImage;

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getDeviceImage() {
        return deviceImage;
    }

    public void setDeviceImage(int deviceImage) {
        this.deviceImage = deviceImage;
    }
}

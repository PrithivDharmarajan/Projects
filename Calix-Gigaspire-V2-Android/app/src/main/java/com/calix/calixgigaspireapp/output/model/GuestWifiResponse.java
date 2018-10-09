package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;


public class GuestWifiResponse implements Serializable {

    private ArrayList<GuestWifiEntity> wifis;

    public ArrayList<GuestWifiEntity> getWifis() {
        return wifis == null ? new ArrayList<GuestWifiEntity>() : wifis;
    }

    public void setWifis(ArrayList<GuestWifiEntity> wifis) {
        this.wifis = wifis;
    }

}

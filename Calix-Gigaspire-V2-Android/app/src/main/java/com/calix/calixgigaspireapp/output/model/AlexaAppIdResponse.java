package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class AlexaAppIdResponse implements Serializable {

   private  String total="";
    private ArrayList<AlexaAppIdEntity> apps=new ArrayList<>();

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<AlexaAppIdEntity> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AlexaAppIdEntity> apps) {
        this.apps = apps;
    }


}

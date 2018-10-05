package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class RouterAgentListResponse implements Serializable {

    private ArrayList<AlexaAppIdEntity> apps=new ArrayList<>();

    public ArrayList<AlexaAppIdEntity> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AlexaAppIdEntity> apps) {
        this.apps = apps;
    }


}

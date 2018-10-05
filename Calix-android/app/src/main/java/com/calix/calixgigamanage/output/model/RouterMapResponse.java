package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;


public class RouterMapResponse implements Serializable {

    private ArrayList<RouterMapEntity> routers;

    public ArrayList<RouterMapEntity> getRouters() {
        return routers == null ? new ArrayList<RouterMapEntity>() : routers;
    }

    public void setRouters(ArrayList<RouterMapEntity> routers) {
        this.routers = routers;
    }


}

package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sibaprasad on 2/7/2018.
 */

public class RouterMapResponse implements Serializable {

    private ArrayList<RouterMapEntity> routers = new ArrayList<>();

    public ArrayList<RouterMapEntity> getRouters() {
        return routers;
    }

    public void setRouters(ArrayList<RouterMapEntity> routers) {
        this.routers = routers;
    }


}

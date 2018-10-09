package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/22/2018.
 */

public class RouterSetupResponse implements Serializable {

    private String routerId = "";

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

}

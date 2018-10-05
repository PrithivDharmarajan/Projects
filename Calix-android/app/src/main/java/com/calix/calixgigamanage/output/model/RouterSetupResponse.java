package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/22/2018.
 */

public class RouterSetupResponse implements Serializable {

    private String routerId;

    public String getRouterId() {
        return routerId == null ? "" : routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

}

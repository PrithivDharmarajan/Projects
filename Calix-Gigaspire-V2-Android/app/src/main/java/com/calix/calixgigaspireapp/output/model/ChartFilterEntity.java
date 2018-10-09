package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;


public class ChartFilterEntity implements Serializable {

    private String type;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

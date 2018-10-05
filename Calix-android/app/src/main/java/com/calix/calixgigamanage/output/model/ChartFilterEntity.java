package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/21/2018.
 */

public class ChartFilterEntity implements Serializable {

    private String type;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

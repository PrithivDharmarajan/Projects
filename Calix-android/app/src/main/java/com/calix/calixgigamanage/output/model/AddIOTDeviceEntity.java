package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/20/2018.
 */

public class AddIOTDeviceEntity implements Serializable {

    private int id;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

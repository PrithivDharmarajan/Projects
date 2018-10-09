package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;


public class DeviceFilterEntity implements Serializable {

    private String name;
    private String type;
    private String id;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

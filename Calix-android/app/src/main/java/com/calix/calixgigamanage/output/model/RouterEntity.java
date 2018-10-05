package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/6/2018.
 */

public class RouterEntity implements Serializable {

    private String id;
    private String name;
    private String type;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }


}

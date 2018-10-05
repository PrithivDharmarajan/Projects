package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/24/2018.
 */

public class AlexaAppIdEntity implements Serializable {
   private  String id="";
    private  String name="";
    private  String status="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/20/2018.
 */

public class AddIOTDeviceEntity implements Serializable {

    private int id;
    private int deviceImage;
    private String name = "";

    public int getDeviceImage() {
        return deviceImage;
    }

    public void setDeviceImage(int deviceImage) {
        this.deviceImage = deviceImage;
    }


    private Class<?> class_name;

    public Class<?> getClass_name() {
        return class_name;
    }

    public void setClass_name(Class<?> class_name) {
        this.class_name = class_name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

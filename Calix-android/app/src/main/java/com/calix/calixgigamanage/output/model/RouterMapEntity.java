package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/22/2018.
 */

public class RouterMapEntity implements Serializable {

    private String order;
    private String routerId;
    private String name;
    private String type;
    private SpeedEntity speed;
    private String status;
    private String defaultSsid;
    private String password;
    private int encryptType=-1;
    private String fsanSerialNumber="";

    public String getFsanSerialNumber() {
        return fsanSerialNumber;
    }

    public void setFsanSerialNumber(String fsanSerialNumber) {
        this.fsanSerialNumber = fsanSerialNumber;
    }


    public int getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(int encryptType) {
        this.encryptType = encryptType;
    }


    public String getDefaultSsid() {
        return defaultSsid == null ? "" : defaultSsid;
    }

    public void setDefaultSsid(String defaultSsid) {
        this.defaultSsid = defaultSsid;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getOrder() {
        return order == null ? "" : order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getRouterId() {
        return routerId == null ? "" : routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
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


    public SpeedEntity getSpeed() {
        return speed == null ? new SpeedEntity() : speed;
    }

    public void setSpeed(SpeedEntity speed) {
        this.speed = speed;
    }


    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}

package com.smaat.renterblock.entity;


import java.io.Serializable;

public class SettingsEntity implements Serializable {

    public String result;

    public String getResult() {
        return result == null ? "" : result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
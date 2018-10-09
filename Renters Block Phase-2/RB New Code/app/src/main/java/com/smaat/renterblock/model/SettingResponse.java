package com.smaat.renterblock.model;


import com.smaat.renterblock.entity.SettingsEntity;

import java.io.Serializable;

public class SettingResponse implements Serializable {
    private String error_code;
    private String msg;
    private SettingsEntity result;

    public String getError_code() {
        if (error_code == null){
            error_code = "";
        }
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        if (msg == null){
            msg = "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SettingsEntity getResult() {
        return result;
    }

    public void setResult(SettingsEntity result) {
        this.result = result;
    }

}

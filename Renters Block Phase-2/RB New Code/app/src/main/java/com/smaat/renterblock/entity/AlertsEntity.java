package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

public class AlertsEntity implements Serializable {
    private String error_code;
    private String msg;
    private ArrayList<AlertsResultEntity> result;

    public String getError_code() {
        return error_code == null ? AppConstants.FAILURE_CODE : error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg == null ? "null" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<AlertsResultEntity> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void setResult(ArrayList<AlertsResultEntity> result) {
        this.result = result;
    }


}

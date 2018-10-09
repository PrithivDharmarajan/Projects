package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;


public class PropertyDetailsResponse implements Serializable {

    private String error_code;
    private String msg;
    private ArrayList<PropertyEntity> result;

    public String getError_code() {
        return error_code == null ? AppConstants.FAILURE_CODE : error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<PropertyEntity> getResult() {
        return result == null ? new ArrayList<PropertyEntity>() : result;
    }

    public void setResult(ArrayList<PropertyEntity> result) {
        this.result = result;
    }
}

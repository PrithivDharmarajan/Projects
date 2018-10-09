package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.PendingDetailsEntity;

import java.io.Serializable;
import java.util.ArrayList;


public class PendingRequestResponse implements Serializable {

    private String error_code;
    private String msg;
    private ArrayList<PendingDetailsEntity>result;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<PendingDetailsEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<PendingDetailsEntity> result) {
        this.result = result;
    }
}
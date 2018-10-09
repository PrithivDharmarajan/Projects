package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.HotLeadsEntity;

import java.io.Serializable;
import java.util.ArrayList;


public class HotLeadsResponse implements Serializable {
    private String msg;
    private String error_code;
    private String currentdatetime;
    private ArrayList<HotLeadsEntity>result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentdatetime) {
        this.currentdatetime = currentdatetime;
    }

    public ArrayList<HotLeadsEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<HotLeadsEntity> result) {
        this.result = result;
    }
}

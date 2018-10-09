package com.smaat.ipharma.entity;

import java.util.ArrayList;

/**
 * Created by admin on 1/24/2017.
 */

public class OneTouchResponse {
    String status = "";
    String msg = "";
    private ArrayList<OnetouchResult> result;

    public ArrayList<OnetouchResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<OnetouchResult> result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

package com.smaat.ipharma.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2/9/2017.
 */

public class AlarmObject {

    String status = "";
    String msg = "";
    ArrayList<String> result;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }


}

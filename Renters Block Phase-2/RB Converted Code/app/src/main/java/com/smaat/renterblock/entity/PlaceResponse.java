package com.smaat.renterblock.entity;


import java.util.ArrayList;

public class PlaceResponse {

    public String error_code;
    public String msg;
    public ArrayList<Places> predictions;

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

    public ArrayList<Places> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<Places> predictions) {
        this.predictions = predictions;
    }


}

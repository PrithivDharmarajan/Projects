package com.smaat.renterblock.model;


import com.smaat.renterblock.entity.NotificationEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationResponse implements Serializable{

    public String error_code;
    public String msg;
    public ArrayList<NotificationEntity> result;
    public String getError_code() {
        if (error_code == null){
            error_code = AppConstants.FAILURE_CODE;
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

    public ArrayList<NotificationEntity> getResult() {
        if (result == null){
            result = new ArrayList<>();
        }
        return result;
    }

    public void setResult(ArrayList<NotificationEntity> result) {
        this.result = result;
    }


}

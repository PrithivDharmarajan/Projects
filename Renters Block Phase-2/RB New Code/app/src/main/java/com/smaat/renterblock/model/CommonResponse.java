package com.smaat.renterblock.model;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class CommonResponse implements Serializable {

    private String error_code;
    private String msg;

    public String getError_code() {
        if (error_code == null || error_code.isEmpty()) {
            error_code = AppConstants.FAILURE_CODE;
        }
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        if (msg == null) {
            msg = "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}

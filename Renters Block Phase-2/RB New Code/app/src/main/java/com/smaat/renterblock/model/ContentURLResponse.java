package com.smaat.renterblock.model;


import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class ContentURLResponse implements Serializable {
    private String error_code;
    private String msg;
    private String result;

    public String getError_code() {
        return error_code == null ? AppConstants.FAILURE_CODE : error_code;
    }

    private void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result == null ? "" : result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}

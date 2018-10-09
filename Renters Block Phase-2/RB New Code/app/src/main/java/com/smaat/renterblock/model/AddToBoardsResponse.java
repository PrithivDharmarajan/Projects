package com.smaat.renterblock.model;

import java.io.Serializable;

/**
 * Created by user on 8/29/2017.
 */

public class AddToBoardsResponse implements Serializable {

    private String error_code;
    private String msg;
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.Result;
import com.smaat.renterblock.entity.ResultEntity;

/**
 * Created by sys on 10/6/2017.
 */

public class UserProfileResponse {

    private String error_code;
    private String msg;
    private ResultEntity result;

    public String getError_code() {
        if (error_code == null) {
            error_code = "";
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

    public ResultEntity getResult() {
        if (result == null) {
            result = new ResultEntity();
        }
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }
}

package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.ReviewPropertyEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

/**
 * Created by sys on 28-Aug-17.
 */

public class ReviewPropertyResponse implements Serializable{

    private String error_code;
    private String msg;
    private ReviewPropertyEntity result;

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

    public ReviewPropertyEntity getResult() {
        if (result == null) {
            result = new ReviewPropertyEntity();
        }
        return result;
    }

    public void setResult(ReviewPropertyEntity result) {
        this.result = result;
    }

}

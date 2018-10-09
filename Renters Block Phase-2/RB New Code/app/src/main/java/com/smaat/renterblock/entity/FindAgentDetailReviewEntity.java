package com.smaat.renterblock.entity;


import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class FindAgentDetailReviewEntity implements Serializable {
    private String error_code;
    private String msg;
    private FindAgentDetailReviewResult result;

    public String getError_code() {
        return error_code == null ? AppConstants.FAILURE_CODE : error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FindAgentDetailReviewResult getResult() {
        return result == null ? new FindAgentDetailReviewResult() : result;
    }

    public void setResult(FindAgentDetailReviewResult result) {
        this.result = result;
    }


}

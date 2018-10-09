package com.smaat.renterblock.model;


import com.smaat.renterblock.entity.ScheduleListObject;

public class ScheduleListResponse {

    private String error_code;
    private String msg;
    private ScheduleListObject result;

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

    public ScheduleListObject getResult() {
        return result;
    }

    public void setResult(ScheduleListObject result) {
        this.result = result;
    }
}

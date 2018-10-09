package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;


public class FindAgentFilterEntity implements Serializable {
    private String error_code;
    private String msg;
    private String search_result_count;
    private String location;
    private ArrayList<FindAgentFilterResultEntity> result;

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

    public String getSearch_result_count() {
        return search_result_count;
    }

    public void setSearch_result_count(String search_result_count) {
        this.search_result_count = search_result_count;
    }

    public String getLocation() {
        return location == null ? "" : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<FindAgentFilterResultEntity> getResult() {
        return result == null ? new ArrayList<FindAgentFilterResultEntity>() : result;
    }

    public void setResult(ArrayList<FindAgentFilterResultEntity> result) {
        this.result = result;
    }


}

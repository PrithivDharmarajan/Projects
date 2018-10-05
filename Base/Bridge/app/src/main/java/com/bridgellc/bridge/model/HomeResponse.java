package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.HomeResponseEntity;

public class HomeResponse extends CommonResponse {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private HomeResponseEntity result;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public HomeResponseEntity getResult() {
        return result;
    }

    public void setResult(HomeResponseEntity result) {
        this.result = result;
    }
}

package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/24/2018.
 */

public class CommonResponse implements Serializable {

    private String errorType = "";
    private String errorDesc = "";

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }


}

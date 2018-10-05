package com.calix.calixgigamanage.output.model;

import java.io.Serializable;


public class CommonModuleResponse implements Serializable {

    private String errorType;
    private String errorDesc;

    public String getErrorType() {
        return errorType == null ? "" : errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc == null ? "" : errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }


}

package com.smaat.renterblock.model;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

/**
 * Created by Prithiv on 11/15/2017.
 */

public class ChatSendResponse implements Serializable {
    private String error_code;
    private String msg;
    private String result;
    private String url;
    private String file;

    public String getError_code() {
        return error_code==null? AppConstants.FAILURE_CODE:error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg==null? "":msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result==null? "":result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUrl() {
        return url==null? "":url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile() {
        return file==null? "":file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}

package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.UserPropertyPicsEntity;

import java.io.Serializable;
import java.util.ArrayList;


public class ImageVideoUploadResponse implements Serializable {

    private String error_code;
    private String msg;
    private ArrayList<UserPropertyPicsEntity>userpropertypic;

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

    public ArrayList<UserPropertyPicsEntity> getUserpropertypic() {
        return userpropertypic;
    }

    public void setUserpropertypic(ArrayList<UserPropertyPicsEntity> userpropertypic) {
        this.userpropertypic = userpropertypic;
    }
}

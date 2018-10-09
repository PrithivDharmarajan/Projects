package com.smaat.renterblock.entity;


import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateViewEntity  implements Serializable{

    private String error_code;
    private String msg;
    private ArrayList<UpdateViewResponse> result;
    private String about;

    public String getAbout() {
        if (about==null){
            about ="";
        }
        return about;
    }

    public void setAbout(String about) {
        if (about == null){
            about = "";
        }
        this.about = about;
    }


    public ArrayList<UpdateViewResponse> getResult() {
        return result;
    }

    public void setResult(ArrayList<UpdateViewResponse> result) {
        this.result = result;
    }



    public String getError_code() {
        if (error_code == null){
            error_code = AppConstants.FAILURE_CODE;
        }
        return error_code;
    }

    public void setError_code(String error_code) {
        if (error_code == null){
            error_code = "";
        }
        this.error_code = error_code;
    }

    public String getMsg() {
        if (msg == null){
            msg = "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        if (msg == null){
            msg = "";
        }
        this.msg = msg;
    }




}

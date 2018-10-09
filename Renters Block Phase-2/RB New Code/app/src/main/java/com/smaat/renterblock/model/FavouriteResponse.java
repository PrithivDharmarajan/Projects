package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.FavouriteEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 19-Sep-17.
 */

public class FavouriteResponse implements Serializable {

    private String error_code;
    private String msg;
    private ArrayList<FavouriteEntity> result;

    public String getError_code() {

        return error_code==null? AppConstants.FAILURE_CODE:error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg==null?"":msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<FavouriteEntity> getResult() {
        return result==null?new ArrayList<FavouriteEntity>():result;
    }

    public void setResult(ArrayList<FavouriteEntity> result) {
        this.result = result;
    }


}


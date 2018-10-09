package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.BoardsEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 19-Sep-17.
 */

public class BoardsResponse implements Serializable {

    private String error_code;
    private String msg;
    private ArrayList<BoardsEntity> result;

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

    public ArrayList<BoardsEntity> getResult() {
        return result==null?new ArrayList<BoardsEntity>():result;
    }

    public void setResult(ArrayList<BoardsEntity> result) {
        this.result = result;
    }


}


package com.smaat.ipharma.entity;

import java.util.ArrayList;

/**
 * Created by admin on 1/24/2017.
 */

public class RecentOrderResponse {
    private static final long serialVersionUID = 1L;
    private String status;
    private   String msg;
//    private ArrayList<RecentOrderDetailList> result;
    RecentOrderDetailList result;

    public RecentOrderDetailList getResult() {
        return result;
    }

    public void setResult(RecentOrderDetailList result) {
        this.result = result;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
//
//    public ArrayList<RecentOrderDetailList> getResult() {
//        return result;
//    }
//
//    public void setResult(ArrayList<RecentOrderDetailList> result) {
//        this.result = result;
//    }
}

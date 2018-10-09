package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;


public class AlertResponse implements Serializable {

    private int total;
    private int from;
    private int size;
    private int unreadCount;
    private ArrayList<AlertEntity> datas;


    public String getTotal() {
        return String.valueOf(total);
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFrom() {
        return String.valueOf(from);

    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getSize() {
        return String.valueOf(size);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUnreadCount() {
        return String.valueOf(unreadCount);
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public ArrayList<AlertEntity> getDatas() {
        return datas == null ? new ArrayList<AlertEntity>() : datas;
    }

    public void setDatas(ArrayList<AlertEntity> datas) {
        this.datas = datas;
    }


}

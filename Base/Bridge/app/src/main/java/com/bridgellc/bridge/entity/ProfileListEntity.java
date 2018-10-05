package com.bridgellc.bridge.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class ProfileListEntity implements Serializable {

    private ArrayList<HomeSingleItemEntity> uploaded;
    private ArrayList<HomeSingleItemEntity> approved;
    private ArrayList<HomeSingleItemEntity> negotiation;
    private ArrayList<HomeSingleItemEntity> history;
    private ArrayList<HomeSingleItemEntity> buying_order;
    private ArrayList<HomeSingleItemEntity> request_bid;

    public ArrayList<HomeSingleItemEntity> getRequest_bid() {
        return request_bid;
    }

    public void setRequest_bid(ArrayList<HomeSingleItemEntity> request_bid) {
        this.request_bid = request_bid;
    }


    public ArrayList<HomeSingleItemEntity> getBuying_order() {
        return buying_order;
    }

    public void setBuying_order(ArrayList<HomeSingleItemEntity> buying_order) {
        this.buying_order = buying_order;
    }


    public ArrayList<HomeSingleItemEntity> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<HomeSingleItemEntity> history) {
        this.history = history;
    }

    public ArrayList<HomeSingleItemEntity> getNegotiation() {
        return negotiation;
    }

    public void setNegotiation(ArrayList<HomeSingleItemEntity> negotiation) {
        this.negotiation = negotiation;
    }

    public ArrayList<HomeSingleItemEntity> getApproved() {
        return approved;
    }

    public void setApproved(ArrayList<HomeSingleItemEntity> approved) {
        this.approved = approved;
    }

    public ArrayList<HomeSingleItemEntity> getUploaded() {
        return uploaded;
    }

    public void setUploaded(ArrayList<HomeSingleItemEntity> uploaded) {
        this.uploaded = uploaded;
    }



}

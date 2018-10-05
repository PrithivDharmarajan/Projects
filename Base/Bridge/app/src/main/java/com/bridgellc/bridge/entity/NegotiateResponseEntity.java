package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by sys on 3/25/2016.
 */
public class NegotiateResponseEntity implements Serializable{

    private String negotiate_id;
    private String user_id;
    private String to_user_id;
    private String item_id;
    private String negitation_amount;

    public String getHeaderakey() {
        return Headerakey;
    }

    public void setHeaderakey(String headerakey) {
        Headerakey = headerakey;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    private String date_sent;
    private String approve;
    private String user_name;
    private String Headerakey;
    private int headId;

    public String getNegotiate_id() {
        return negotiate_id;
    }

    public void setNegotiate_id(String negotiate_id) {
        this.negotiate_id = negotiate_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getNegitation_amount() {
        return negitation_amount;
    }

    public void setNegitation_amount(String negitation_amount) {
        this.negitation_amount = negitation_amount;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}

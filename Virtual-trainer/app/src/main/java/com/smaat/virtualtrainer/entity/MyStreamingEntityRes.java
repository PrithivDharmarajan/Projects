package com.smaat.virtualtrainer.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class MyStreamingEntityRes implements Serializable {

    private String stream_id;
    private String stream_name;
    private String status;
    private String owner_id;
    private String date_time;
    private String stream_owner;
    private ArrayList<InviteStreamingEntityRes> joiness;

    public ArrayList<InviteStreamingEntityRes> getJoiness() {
        return joiness;
    }

    public void setJoiness(ArrayList<InviteStreamingEntityRes> joiness) {
        this.joiness = joiness;
    }

    public String getStream_owner() {
        return stream_owner;
    }

    public void setStream_owner(String stream_owner) {
        this.stream_owner = stream_owner;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

}

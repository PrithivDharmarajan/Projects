package com.smaat.virtualtrainer.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class InviteStreamingEntityRes implements Serializable {

    private String invite_id;
    private String stream_id;
    private String joine_user_id;
    private String status;
    private String date_time;
    private String joinee_name;
    private  String stream_name;
    private ArrayList<MyStreamingEntityRes> stream;

    public ArrayList<MyStreamingEntityRes> getStream() {
        return stream;
    }

    public void setStream(ArrayList<MyStreamingEntityRes> stream) {
        this.stream = stream;
    }

    public String getJoinee_name() {
        return joinee_name;
    }

    public void setJoinee_name(String joinee_name) {
        this.joinee_name = joinee_name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJoine_user_id() {
        return joine_user_id;
    }

    public void setJoine_user_id(String joine_user_id) {
        this.joine_user_id = joine_user_id;
    }

    public String getStream_id() {
        return stream_id;
    }

    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getInvite_id() {
        return invite_id;
    }

    public void setInvite_id(String invite_id) {
        this.invite_id = invite_id;
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

}

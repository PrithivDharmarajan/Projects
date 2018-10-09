package com.smaat.virtualtrainer.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class StreamingEntityRes implements Serializable {

    private ArrayList<MyStreamingEntityRes> mystream;
    private ArrayList<InviteStreamingEntityRes> invite_stream;

    public ArrayList<InviteStreamingEntityRes> getInvite_stream() {
        return invite_stream;
    }

    public void setInvite_stream(ArrayList<InviteStreamingEntityRes> invite_stream) {
        this.invite_stream = invite_stream;
    }

    public ArrayList<MyStreamingEntityRes> getMystream() {
        return mystream;
    }

    public void setMystream(ArrayList<MyStreamingEntityRes> mystream) {
        this.mystream = mystream;
    }

}

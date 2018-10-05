package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.ChatResponseEntity;

import java.util.ArrayList;

public class ChatResponse extends CommonResponse {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<ChatResponseEntity> result;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ArrayList<ChatResponseEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<ChatResponseEntity> result) {
        this.result = result;
    }
}

package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.BoardMessageEntity;

import java.util.ArrayList;



public class BoardMessageResponse extends CommonResponse {
    private ArrayList<BoardMessageEntity>result;

    public ArrayList<BoardMessageEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<BoardMessageEntity> result) {
        this.result = result;
    }
}

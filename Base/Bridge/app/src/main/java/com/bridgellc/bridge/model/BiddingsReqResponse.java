package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.HomeSingleItemEntity;

import java.util.ArrayList;

/**
 * Created by admin on 4/26/2016.
 */
public class BiddingsReqResponse extends CommonModelResponse {

    private ArrayList<HomeSingleItemEntity> result;

    public ArrayList<HomeSingleItemEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<HomeSingleItemEntity> result) {
        this.result = result;
    }

}

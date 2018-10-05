package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.ReviewEntity;

import java.util.ArrayList;

/**
 * Created by Karthi on 3/31/2016.
 */
public class ReviewResponce extends CommonResponse {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ArrayList<ReviewEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<ReviewEntity> result) {
        this.result = result;
    }

    private ArrayList<ReviewEntity> result;

}

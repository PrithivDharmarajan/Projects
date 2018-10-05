package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.UniversityEntity;

import java.util.ArrayList;

/**
 * Created by USER on 3/25/2016.
 */
public class UniversityResponse extends CommonModelResponse {


    public ArrayList<UniversityEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<UniversityEntity> result) {
        this.result = result;
    }

    private ArrayList<UniversityEntity> result;

}

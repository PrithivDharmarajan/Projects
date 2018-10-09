package com.smaat.spark.model;

import com.smaat.spark.entity.outputEntity.AddressEntity;

import java.io.Serializable;
import java.util.ArrayList;


public class AddressResponse implements Serializable {

    ArrayList<AddressEntity> results;

    public ArrayList<AddressEntity> getResults() {
        if (results == null) {
            results = new ArrayList<>();
        }
        return results;
    }

    public void setResults(ArrayList<AddressEntity> results) {
        this.results = results;
    }

}

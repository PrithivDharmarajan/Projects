package com.smaat.renterblock.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 8/24/2017.
 */

public class PlacePredictionResponse implements Serializable {
    private ArrayList<PlaceDescription> predictions;

    public ArrayList<PlaceDescription> getPredictions() {

        return predictions == null ? predictions = new ArrayList<>() : predictions;
    }

    public void setPredictions(ArrayList<PlaceDescription> predictions) {
        this.predictions = predictions;
    }
}

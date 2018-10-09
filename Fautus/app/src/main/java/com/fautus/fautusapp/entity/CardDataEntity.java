package com.fautus.fautusapp.entity;

import com.fautus.fautusapp.model.CardDeatilsResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 25-May-17.
 */

public class CardDataEntity implements Serializable {
    private ArrayList<CardDeatilsResponse> data;

    public ArrayList<CardDeatilsResponse> getData() {
        return data;
    }

    public void setData(ArrayList<CardDeatilsResponse> data) {
        this.data = data;
    }

}

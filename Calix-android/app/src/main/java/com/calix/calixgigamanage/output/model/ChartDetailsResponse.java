package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 2/21/2018.
 */

public class ChartDetailsResponse implements Serializable {

    private int size;
    private String type;
    private ArrayList<ChartDetailsEntity> data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ChartDetailsEntity> getData() {
        return data == null ? new ArrayList<ChartDetailsEntity>() : data;
    }

    public void setData(ArrayList<ChartDetailsEntity> data) {
        this.data = data;
    }


}

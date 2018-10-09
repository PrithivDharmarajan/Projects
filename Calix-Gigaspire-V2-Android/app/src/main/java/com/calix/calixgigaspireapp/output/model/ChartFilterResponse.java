package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 2/21/2018.
 */

public class ChartFilterResponse implements Serializable {

    private ArrayList<ChartFilterEntity> filters;

    public ArrayList<ChartFilterEntity> getFilters() {
        return filters == null ? new ArrayList<ChartFilterEntity>() : filters;
    }

    public void setFilters(ArrayList<ChartFilterEntity> filters) {
        this.filters = filters;
    }


}

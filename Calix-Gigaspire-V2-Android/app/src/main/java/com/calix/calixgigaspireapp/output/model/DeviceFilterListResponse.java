package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class DeviceFilterListResponse implements Serializable {

    private ArrayList<DeviceFilterEntity> filters;

    public ArrayList<DeviceFilterEntity> getFilters() {
        return filters == null ? new ArrayList<DeviceFilterEntity>() : filters;
    }

    public void setFilters(ArrayList<DeviceFilterEntity> filters) {
        this.filters = filters;
    }

}

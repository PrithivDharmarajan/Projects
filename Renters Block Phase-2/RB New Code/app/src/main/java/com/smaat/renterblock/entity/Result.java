package com.smaat.renterblock.entity;

/**
 * Created by Smaat on 9/21/2017.
 */

public class Result {

    private Geometry geometry;
    private String formatted_address;

    public String getAddress_components() {
        if(formatted_address == null){
            formatted_address = "";
        }
        return formatted_address;
    }

    public void setAddress_components(String address_components) {
        this.formatted_address = address_components;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}

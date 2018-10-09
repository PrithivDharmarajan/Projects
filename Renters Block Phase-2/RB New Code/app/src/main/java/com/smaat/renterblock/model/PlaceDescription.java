package com.smaat.renterblock.model;

import java.io.Serializable;

public class PlaceDescription implements Serializable {
    private String description;
    private String latitude;
    private String longitude;
    private String place_id;
    public String getLatitude() {
        return latitude;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        if(description==null){
            description="";

        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

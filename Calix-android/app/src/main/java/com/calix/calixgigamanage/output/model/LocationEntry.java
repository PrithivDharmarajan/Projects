package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by sibaprasad on 2/9/2018.
 */

public class LocationEntry implements Serializable {

    private String locationId;
    private String locationName;


    public String getLocationId() {
        return locationId == null ? "" :locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}

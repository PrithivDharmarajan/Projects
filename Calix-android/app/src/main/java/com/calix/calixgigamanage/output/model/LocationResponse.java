package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sibaprasad on 2/9/2018.
 */

public class LocationResponse implements Serializable {

private ArrayList<LocationEntry> locations;


    public ArrayList<LocationEntry> getLocations() {
        return locations == null ? new ArrayList<LocationEntry>() :locations ;

    }

    public void setLocations(ArrayList<LocationEntry> locations) {
        this.locations = locations;


    }
}

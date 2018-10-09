package com.fautus.fautusapp.entity;

import com.parse.ParseGeoPoint;

import java.io.Serializable;

/**
 * Created by sys on 12-Jun-17.
 */

public class ParsePhotoPolEntity implements Serializable {
    private String objectId;
    private String Attraction;
    private  String City;
    private String FoursquareURL;
    private String State;
    private ParseGeoPoint Location;
    private String Type;
    private String FoursquareID;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAttraction() {
        return Attraction;
    }

    public void setAttraction(String attraction) {
        Attraction = attraction;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getFoursquareURL() {
        return FoursquareURL;
    }

    public void setFoursquareURL(String foursquareURL) {
        FoursquareURL = foursquareURL;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public ParseGeoPoint getLocation() {
        return Location;
    }

    public void setLocation(ParseGeoPoint location) {
        Location = location;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFoursquareID() {
        return FoursquareID;
    }

    public void setFoursquareID(String foursquareID) {
        FoursquareID = foursquareID;
    }

}

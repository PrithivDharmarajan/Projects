package com.smaat.renterblock.entity;

import android.util.Log;

import com.google.gson.Gson;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class LocalSavedSearchEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private boolean isSelected;


    private String location;
    private String property_type;
    private String user_id;
    private String latitude;
    private String longitude;
    private String save_search_id;
    private String filter_object;
    private String filter_type;
    private String date;
    private String email_notification;
    private String inquiry;
    private String placeID;

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getSave_search_id() {
        return save_search_id;
    }

    public void setSave_search_id(String save_search_id) {
        this.save_search_id = save_search_id;
    }

    public String getFilter_object() {
        return filter_object;
    }

    public void setFilter_object(String filter_object) {
        this.filter_object = filter_object;
    }

    public String getFilter_type() {
        return filter_type;
    }

    public FilterEntity getFilter_object1() {
        FilterEntity filterEntity = new FilterEntity();
        if (filter_object != null ) {
            try{
                filterEntity = new Gson().fromJson(filter_object, FilterEntity.class);
            }catch (Exception e){
                filterEntity = new FilterEntity();
                Log.d(AppConstants.TAG,e.toString());
            }
        }
        return filterEntity;
    }

    public void setFilter_type(String filter_type) {
        this.filter_type = filter_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail_notification() {
        return email_notification;
    }

    public void setEmail_notification(String email_notification) {
        this.email_notification = email_notification;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public String getLatitude() {
        return latitude;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

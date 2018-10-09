package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;


public class HotLeadsEntity implements Serializable {
    private String property_id;
    private String user_id;
    private String price_range;
    private String beds;
    private String baths;
    private String square_footage;
    private String build_year;
    private String address;
    private String property_name;
    private String overallcount;
    private String propertyImage;
    private String leads_count;
    private String property_rating;
    private String review_count;
    private LeadsViewEntity leadslist;

    public String getProperty_id() {
        return property_id==null? AppConstants.FAILURE_CODE:property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getUser_id() {
        return user_id==null? AppConstants.FAILURE_CODE:user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPrice_range() {
        return price_range==null? AppConstants.FAILURE_CODE:price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getBeds() {
        return beds==null?"":beds;
    }

    public void setBeds(String beds) {
        this.beds = beds;
    }

    public String getBaths() {
        return baths==null?"":baths;
    }

    public void setBaths(String baths) {
        this.baths = baths;
    }

    public String getSquare_footage() {
        return square_footage==null? AppConstants.FAILURE_CODE:square_footage;
    }

    public void setSquare_footage(String square_footage) {
        this.square_footage = square_footage;
    }

    public String getBuild_year() {
        return build_year==null? AppConstants.FAILURE_CODE:build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public String getAddress() {
        return address==null? "":address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProperty_name() {
        return property_name==null? "":property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getOverallcount() {
        return overallcount==null? AppConstants.FAILURE_CODE:overallcount;
    }

    public void setOverallcount(String overallcount) {
        this.overallcount = overallcount;
    }

    public String getPropertyImage() {
        return propertyImage==null? "":propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
    }

    public String getLeads_count() {
        return leads_count==null? AppConstants.FAILURE_CODE:leads_count;
    }

    public void setLeads_count(String leads_count) {
        this.leads_count = leads_count;
    }

    public String getProperty_rating() {
        return property_rating==null? AppConstants.FAILURE_CODE:property_rating;
    }

    public void setProperty_rating(String property_rating) {
        this.property_rating = property_rating;
    }

    public String getReview_count() {
        return review_count==null? AppConstants.FAILURE_CODE:review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public LeadsViewEntity getLeadslist() {
        return leadslist;
    }

    public void setLeadslist(LeadsViewEntity leadslist) {
        this.leadslist = leadslist;
    }
}

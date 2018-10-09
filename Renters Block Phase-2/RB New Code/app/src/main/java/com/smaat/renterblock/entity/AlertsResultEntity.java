package com.smaat.renterblock.entity;


import java.io.Serializable;

import static com.smaat.renterblock.utils.AppConstants.FAILURE_CODE;

public class AlertsResultEntity implements Serializable {

    private String alert_id;
    private String user_id;
    private String alert_name;
    private String type;
    private String price_range_min;
    private String price_range_max;
    private String property_type;
    private String beds;
    private String baths;
    private String no_fee;
    private String square_footage_min;
    private String square_footage_max;
    private String year_build_min;
    private String year_build_max;
    private String lot_size;
    private String days_on_RB;
    private String resale;
    private String new_construction;
    private String fore_closure;
    private String open_house;
    private String reduced_prices;
    private String keywords;
    private String MLS;
    private String sold_within;
    private String time;
    private String count;
    private String latitude;
    private String longitude;
    private String location;
    private String alert_object;
    private String distance;
    private boolean isEditbool = false;
    public boolean isEditbool() {
        return isEditbool;
    }

    public void setEditbool(boolean editbool) {
        isEditbool = editbool;
    }


    public String getAlert_id() {
        return alert_id == null ? "" : alert_id;
    }

    public void setAlert_id(String alert_id) {
        this.alert_id = alert_id;
    }

    public String getUser_id() {
        return user_id == null ? FAILURE_CODE : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAlert_name() {
        return alert_name == null ? " " : alert_name;
    }

    public void setAlert_name(String alert_name) {
        this.alert_name = alert_name;
    }

    public String getType() {
        return type == null ? " " : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice_range_min() {
        return price_range_min == null ? FAILURE_CODE : price_range_min;
    }

    public void setPrice_range_min(String price_range_min) {
        this.price_range_min = price_range_min;
    }

    public String getPrice_range_max() {
        return price_range_max == null ? FAILURE_CODE : price_range_max;
    }

    public void setPrice_range_max(String price_range_max) {
        this.price_range_max = price_range_max;
    }

    public String getProperty_type() {
        return property_type == null ? "" : property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getBeds() {
        return beds == null ? FAILURE_CODE : beds;
    }

    public void setBeds(String beds) {
        this.beds = beds;
    }

    public String getBaths() {
        return baths == null ? FAILURE_CODE : baths;
    }

    public void setBaths(String baths) {
        this.baths = baths;
    }

    public String getNo_fee() {
        return no_fee == null ? FAILURE_CODE : no_fee;
    }

    public void setNo_fee(String no_fee) {
        this.no_fee = no_fee;
    }

    public String getSquare_footage_min() {
        return square_footage_min == null ? FAILURE_CODE : square_footage_min;
    }

    public void setSquare_footage_min(String square_footage_min) {
        this.square_footage_min = square_footage_min;
    }

    public String getSquare_footage_max() {
        return square_footage_max == null ? FAILURE_CODE : square_footage_max;
    }

    public void setSquare_footage_max(String square_footage_max) {
        this.square_footage_max = square_footage_max;
    }

    public String getYear_build_min() {
        return year_build_min == null ? FAILURE_CODE : year_build_min;
    }

    public void setYear_build_min(String year_build_min) {
        this.year_build_min = year_build_min;
    }

    public String getYear_build_max() {
        return year_build_max == null ? FAILURE_CODE : year_build_max;
    }

    public void setYear_build_max(String year_build_max) {
        this.year_build_max = year_build_max;
    }

    public String getLot_size() {
        return lot_size == null ? FAILURE_CODE : lot_size;
    }

    public void setLot_size(String lot_size) {
        this.lot_size = lot_size;
    }

    public String getDays_on_RB() {
        return days_on_RB == null ? FAILURE_CODE : days_on_RB;
    }

    public void setDays_on_RB(String days_on_RB) {
        this.days_on_RB = days_on_RB;
    }

    public String getResale() {
        return resale == null ? FAILURE_CODE : resale;
    }

    public void setResale(String resale) {
        this.resale = resale;
    }

    public String getNew_construction() {
        return new_construction == null ? FAILURE_CODE : new_construction;
    }

    public void setNew_construction(String new_construction) {
        this.new_construction = new_construction;
    }

    public String getFore_closure() {
        return fore_closure == null ? FAILURE_CODE : fore_closure;
    }

    public void setFore_closure(String fore_closure) {
        this.fore_closure = fore_closure;
    }

    public String getOpen_house() {
        return open_house == null ? FAILURE_CODE : open_house;
    }

    public void setOpen_house(String open_house) {
        this.open_house = open_house;
    }

    public String getReduced_prices() {
        return reduced_prices == null ? FAILURE_CODE : reduced_prices;
    }

    public void setReduced_prices(String reduced_prices) {
        this.reduced_prices = reduced_prices;
    }

    public String getKeywords() {
        return keywords == null ? " " : keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMLS() {
        return MLS == null ? FAILURE_CODE : MLS;
    }

    public void setMLS(String MLS) {
        this.MLS = MLS;
    }

    public String getSold_within() {
        return sold_within == null ? FAILURE_CODE : sold_within;
    }

    public void setSold_within(String sold_within) {
        this.sold_within = sold_within;
    }

    public String getTime() {
        return time == null ? FAILURE_CODE : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count == null ? FAILURE_CODE : count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLatitude() {
        return latitude == null ? FAILURE_CODE : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude == null ? FAILURE_CODE : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location == null ? " " : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAlert_object() {
        return alert_object == null ? FAILURE_CODE : alert_object;
    }

    public void setAlert_object(String alert_object) {
        this.alert_object = alert_object;
    }

    public String getDistance() {
        return distance == null ? FAILURE_CODE : distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


}

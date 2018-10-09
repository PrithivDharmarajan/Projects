package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;


public class FilterEntity implements Serializable {

    private String price_range_min="";
    private String price_range_max="";
    private String property_type="";
    private String beds="";
    private String baths="";
    private String no_fee= AppConstants.FAILURE_CODE;
    private String square_footage_min="";
    private String square_footage_max="";
    private String year_build_min="";
    private String year_build_max="";
    private String lot_size="";
    private String days_on_RB="";
    private String resale = AppConstants.FAILURE_CODE;;
    private String new_construction = AppConstants.FAILURE_CODE;;
    private String fore_closure = AppConstants.FAILURE_CODE;;
    private String open_house= AppConstants.FAILURE_CODE;
    private String reduced_prices = AppConstants.FAILURE_CODE;
    private String keywords="";
    private String MLS="";
    private String filter_name="";
    private String location="";
    private String latitude="";
    private String longitude="";
    private String distance="";
    private String sold_within="";


    public String getSold_within() {
        return sold_within==null?"":sold_within;
    }

    public void setSold_within(String sold_within) {
        this.sold_within = sold_within==null?"":sold_within;
    }



    public String getPrice_range_min() {

        if (price_range_min == null) {
            price_range_min = "";
        }
        return price_range_min;
    }

    public void setPrice_range_min(String price_range_min) {
        this.price_range_min = price_range_min==null?"":price_range_min;
    }

    public String getPrice_range_max() {
        if (price_range_max == null) {
            price_range_max = "";
        }
        return price_range_max;
    }

    public void setPrice_range_max(String price_range_max) {
        this.price_range_max = price_range_max==null?"":price_range_max;
    }

    public String getProperty_type() {
        if (property_type == null) {
            property_type = "";
        }

        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type==null?"":property_type;
    }

    public String getBeds() {
        if (beds == null) {
            beds = "";
        }
        return beds;
    }

    public void setBeds(String beds) {
        this.beds = beds==null?"":beds;
    }

    public String getBaths() {
        if (baths == null) {
            baths = "";
        }
        return baths;
    }

    public void setBaths(String baths) {
        this.baths = baths==null?"":baths;
    }

    public String getNo_fee() {
        if (no_fee == null || no_fee.isEmpty()) {
            no_fee = AppConstants.FAILURE_CODE;
        }
        return no_fee;
    }

    public void setNo_fee(String no_fee) {
        this.no_fee = no_fee==null?"":no_fee;
    }

    public String getSquare_footage_min() {
        if (square_footage_min == null) {
            square_footage_min = "";
        }
        return square_footage_min;
    }

    public void setSquare_footage_min(String square_footage_min) {
        this.square_footage_min = square_footage_min==null?"":square_footage_min;
    }

    public String getSquare_footage_max() {
        if (square_footage_max == null) {
            square_footage_max = "";
        }
        return square_footage_max;
    }

    public void setSquare_footage_max(String square_footage_max) {
        this.square_footage_max = square_footage_max==null?"":square_footage_max;
    }

    public String getYear_build_min() {

        if (year_build_min == null) {
            year_build_min = "";
        }
        return year_build_min;
    }

    public void setYear_build_min(String year_build_min) {
        this.year_build_min = year_build_min==null?"":year_build_min;
    }

    public String getYear_build_max() {
        if (year_build_max == null) {
            year_build_max = "";
        }
        return year_build_max;
    }

    public void setYear_build_max(String year_build_max) {
        this.year_build_max = year_build_max==null?"":year_build_max;
    }

    public String getLot_size() {
        if (lot_size == null) {
            lot_size = "";
        }
        return lot_size;
    }

    public void setLot_size(String lot_size) {
        this.lot_size = lot_size==null?"":lot_size;
    }

    public String getDays_on_RB() {
        if (days_on_RB == null) {
            days_on_RB = "";
        }
        return days_on_RB;
    }

    public void setDays_on_RB(String days_on_RB) {
        this.days_on_RB = days_on_RB==null?"":days_on_RB;
    }

    public String getResale() {

        if (resale == null || resale.isEmpty()) {
            resale = AppConstants.FAILURE_CODE;
        }
        return resale;
    }

    public void setResale(String resale) {
        this.resale = resale==null?"":resale;
    }

    public String getNew_construction() {

        if (new_construction == null ||new_construction.isEmpty()) {
            new_construction = AppConstants.FAILURE_CODE;
        }
        return new_construction;
    }

    public void setNew_construction(String new_construction) {
        this.new_construction = new_construction==null?"":new_construction;
    }

    public String getFore_closure() {
        if (fore_closure == null || fore_closure.isEmpty()) {
            fore_closure = AppConstants.FAILURE_CODE;
        }

        return fore_closure;
    }

    public void setFore_closure(String fore_closure) {

        this.fore_closure = fore_closure==null?"":fore_closure;
    }

    public String getOpen_house() {
        if (open_house == null || open_house.isEmpty()) {
            open_house = AppConstants.FAILURE_CODE;
        }

        return open_house;
    }

    public void setOpen_house(String open_house) {
        this.open_house = open_house==null?"":open_house;
    }

    public String getReduced_prices() {
        if (reduced_prices == null || reduced_prices.isEmpty()) {
            reduced_prices = AppConstants.FAILURE_CODE;
        }
        return reduced_prices;
    }

    public void setReduced_prices(String reduced_prices) {
        this.reduced_prices = reduced_prices==null?"":reduced_prices;
    }

    public String getKeywords() {
        if (keywords == null) {
            keywords = "";
        }
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords==null?"":keywords;
    }

    public String getMLS() {
        if (MLS == null) {
            MLS = "";
        }
        return MLS;
    }

    public void setMLS(String MLS) {
        this.MLS = MLS==null?"":MLS;
    }

    public String getFilter_name() {
        if (filter_name == null) {
            filter_name = "";
        }
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name==null?"":filter_name;
    }

    public String getLocation() {
        if (location == null) {
            location = "";
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location==null?"":location;
    }

    public String getLatitude() {
        if (latitude == null) {
            latitude = "";
        }
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude==null?"":latitude;
    }

    public String getLongitude() {
        if (longitude == null) {
            longitude = "";
        }
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude==null?"":longitude;
    }

    public String getDistance() {
        if (distance == null) {
            distance = "";
        }
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance==null?"":distance;
    }
}


package com.smaat.renterblock.entity;

import com.google.android.gms.maps.model.Marker;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyEntity implements Serializable {


    private String pro_id;
    private String property_id;
    private String user_id;
    private String type;
    private String price_range;
    private String property_type;
    private String beds;
    private String baths;
    private String square_footage;
    private String build_year;
    private String address;
    private String latitude;
    private String longitude;
    private String description;
    private String lot_size;
    private String fee;
    private String resale;
    private String new_construction;
    private String foreclosure;
    private String open_house;
    private String reduced_prices;
    private String rb_block;
    private String property_datetime;
    private String review_count;
    private String isfavourite;
    private String property_rating;
    private String property_name;
    private ArrayList<PostPropertyUserDetails> property_posted_user_details;
    private ArrayList<PropertyPictures> property_pics;
    private ArrayList<PropertyPictures> userpropertypic;
    private ArrayList<PropertyReview> property_review;
    private String MLS;
    private String description1;
    private String description2;
    private String description3;
    private String description4;
    private String description5;
    private Marker marker;
    private String overallcount;
    private String property_api;
    private String is_archived;
    private String new_property;
    private ArrayList<PropertyPictures> property_video;
    private String ismymessageboard;

    public String getIsmymessageboard() {
        return ismymessageboard == null ? AppConstants.FAILURE_CODE : ismymessageboard;
    }

    public void setIsmymessageboard(String ismymessageboard) {
        this.ismymessageboard = ismymessageboard;
    }


    public ArrayList<PostPropertyUserDetails> getProperty_posted_user_details() {
        if (property_posted_user_details == null) {
            property_posted_user_details = new ArrayList<>();
        }
        return property_posted_user_details;
    }

    public void setProperty_posted_user_details(ArrayList<PostPropertyUserDetails> property_posted_user_details) {
        this.property_posted_user_details = property_posted_user_details;
    }

    private MarkerID Markers;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public MarkerID getMarkers() {
        return Markers;
    }

    public void setMarkers(MarkerID markers) {
        Markers = markers;
    }

    public String getIs_archived() {
        return is_archived == null ? AppConstants.FAILURE_CODE : is_archived;
    }

    public void setIs_archived(String is_archived) {
        this.is_archived = is_archived;
    }


    public String getNew_property() {
        return new_property == null ? AppConstants.FAILURE_CODE : new_property;
    }

    public void setNew_property(String new_property) {
        this.new_property = new_property;
    }


    public String getIs_updated_today() {
        return is_updated_today == null ? AppConstants.FAILURE_CODE : is_updated_today;
    }

    public void setIs_updated_today(String is_updated_today) {
        this.is_updated_today = is_updated_today;
    }

    private String is_updated_today;

    public String getOverallcount() {

        return overallcount == null ? AppConstants.FAILURE_CODE : overallcount;
    }

    public void setOverallcount(String overallcount) {
        this.overallcount = overallcount;
    }


    public String getProperty_api() {
        return property_api == null ? "" : property_api;
    }

    public void setProperty_api(String property_api) {
        this.property_api = property_api;
    }


    public String getRating() {
        return rating == null ? AppConstants.FAILURE_CODE : rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;

    public String getMLS() {
        return MLS == null ? "" : MLS;
    }

    public void setMLS(String MLS) {
        this.MLS = MLS;
    }

    public String getDescription1() {
        return description1 == null ? "" : description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {

        return description2 == null ? "" : description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getDescription3() {
        return description3 == null ? "" : description3;
    }

    public void setDescription3(String description3) {
        this.description3 = description3;
    }

    public String getDescription4() {
        return description4 == null ? "" : description4;
    }

    public void setDescription4(String description4) {
        this.description4 = description4;
    }

    public String getDescription5() {
        return description5 == null ? "" : description5;
    }

    public void setDescription5(String description5) {
        this.description5 = description5;
    }

    public ArrayList<PropertyPictures> getProperty_video() {
        return property_video == null ? property_video = new ArrayList<>() : property_video;
    }

    public void setProperty_video(ArrayList<PropertyPictures> property_video) {
        this.property_video = property_video;
    }

    public ArrayList<BoardsDetails> getMy_message_board() {
        return my_message_board == null ? my_message_board = new ArrayList<>() : my_message_board;
    }

    public void setMy_message_board(ArrayList<BoardsDetails> my_message_board) {
        this.my_message_board = my_message_board;
    }

    private ArrayList<BoardsDetails> my_message_board;
    private boolean isEditbool = false;

    public boolean isEditbool() {
        return isEditbool;
    }

    public void setEditbool(boolean editbool) {
        isEditbool = editbool;
    }

    private static final long serialVersionUID = 1L;


    public String getProperty_name() {

        return property_name == null ? "" : property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_rating() {

        return property_rating == null ? AppConstants.FAILURE_CODE : property_rating;
    }

    public void setProperty_rating(String property_rating) {
        this.property_rating = property_rating;
    }

    public String getPro_id() {
        return pro_id == null ? "" : pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getProperty_id() {
        return property_id == null ? "" : property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getUser_id() {
        return user_id == null ? "" : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice_range() {
        return price_range == null ? AppConstants.FAILURE_CODE : price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getProperty_type() {
        return property_type == null ? "" : property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getBeds() {
        return beds == null ? "" : beds;
    }

    public void setBeds(String beds) {
        this.beds = beds;
    }

    public String getBaths() {

        return baths == null ? "" : baths;
    }

    public void setBaths(String baths) {
        this.baths = baths;
    }

    public String getSquare_footage() {

        return square_footage == null ? "" : square_footage;
    }

    public void setSquare_footage(String square_footage) {
        this.square_footage = square_footage;
    }

    public String getBuild_year() {
        return build_year == null ? "" : build_year;
    }

    public void setBuild_year(String build_year) {
        this.build_year = build_year;
    }

    public String getAddress() {

        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude == null ? AppConstants.FAILURE_CODE : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude == null ? AppConstants.FAILURE_CODE : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLot_size() {
        return lot_size == null ? "" : lot_size;
    }

    public void setLot_size(String lot_size) {
        this.lot_size = lot_size;
    }

    public String getFee() {
        return fee == null ? "" : fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getResale() {
        return resale == null ? "" : resale;
    }

    public void setResale(String resale) {
        this.resale = resale;
    }

    public String getNew_construction() {
        return new_construction == null ? "" : new_construction;
    }

    public void setNew_construction(String new_construction) {
        this.new_construction = new_construction;
    }

    public String getForeclosure() {
        return foreclosure == null ? "" : foreclosure;
    }

    public void setForeclosure(String foreclosure) {
        this.foreclosure = foreclosure;
    }

    public String getOpen_house() {
        return open_house == null ? "" : open_house;
    }

    public void setOpen_house(String open_house) {
        this.open_house = open_house;
    }

    public String getReduced_prices() {
        return reduced_prices == null ? "" : reduced_prices;
    }

    public void setReduced_prices(String reduced_prices) {
        this.reduced_prices = reduced_prices;
    }

    public String getRb_block() {
        return rb_block == null ? "" : rb_block;
    }

    public void setRb_block(String rb_block) {
        this.rb_block = rb_block;
    }

    public String getProperty_datetime() {
        return property_datetime == null || property_datetime.equalsIgnoreCase("0000-00-00 00:00:00") ? "" : property_datetime;
    }

    public void setProperty_datetime(String property_datetime) {
        this.property_datetime = property_datetime;
    }

    public String getReview_count() {
        return review_count == null ? AppConstants.FAILURE_CODE : review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getIsfavourite() {
        return isfavourite == null ? AppConstants.FAILURE_CODE : isfavourite;
    }

    public void setIsfavourite(String isfavourite) {
        this.isfavourite = isfavourite;
    }

    public ArrayList<PropertyPictures> getProperty_pics() {

        return property_pics == null ? property_pics = new ArrayList<>() : property_pics;
    }

    public void setProperty_pics(ArrayList<PropertyPictures> property_pics) {
        this.property_pics = property_pics;
    }

    public ArrayList<PropertyPictures> getUserpropertypic() {
        return userpropertypic == null ? userpropertypic = new ArrayList<>() : userpropertypic;
    }

    public void setUserpropertypic(ArrayList<PropertyPictures> userpropertypic) {
        this.userpropertypic = userpropertypic;
    }

    public ArrayList<PropertyReview> getProperty_review() {

        return property_review == null ? property_review = new ArrayList<>() : property_review;
    }

    public void setProperty_review(ArrayList<PropertyReview> property_review) {
        this.property_review = property_review;
    }

}

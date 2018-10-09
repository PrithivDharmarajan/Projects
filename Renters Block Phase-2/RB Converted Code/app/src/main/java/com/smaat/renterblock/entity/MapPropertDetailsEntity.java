package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MapPropertDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private String property_posted_user_details;
	private ArrayList<PropertyPictures> property_pics;
	private ArrayList<UserPropertyPics> userpropertypic;
	private ArrayList<PropertyReview> property_review;

	// public ArrayList<PostPropertyUserDetails>
	// getProperty_posted_user_details() {
	// return property_posted_user_details;
	// }
	//
	// public void setProperty_posted_user_details(
	// ArrayList<PostPropertyUserDetails> property_posted_user_details) {
	// this.property_posted_user_details = property_posted_user_details;
	// }
	public String getProperty_posted_user_details() {
		return property_posted_user_details;
	}

	public void setProperty_posted_user_details(
			String property_posted_user_details) {
		this.property_posted_user_details = property_posted_user_details;
	}

	public String getProperty_name() {
		return property_name;
	}

	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}

	public String getProperty_rating() {
		return property_rating;
	}

	public void setProperty_rating(String property_rating) {
		this.property_rating = property_rating;
	}

	public String getPro_id() {
		return pro_id;
	}

	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice_range() {
		return price_range;
	}

	public void setPrice_range(String price_range) {
		this.price_range = price_range;
	}

	public String getProperty_type() {
		return property_type;
	}

	public void setProperty_type(String property_type) {
		this.property_type = property_type;
	}

	public String getBeds() {
		return beds;
	}

	public void setBeds(String beds) {
		this.beds = beds;
	}

	public String getBaths() {
		return baths;
	}

	public void setBaths(String baths) {
		this.baths = baths;
	}

	public String getSquare_footage() {
		return square_footage;
	}

	public void setSquare_footage(String square_footage) {
		this.square_footage = square_footage;
	}

	public String getBuild_year() {
		return build_year;
	}

	public void setBuild_year(String build_year) {
		this.build_year = build_year;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLot_size() {
		return lot_size;
	}

	public void setLot_size(String lot_size) {
		this.lot_size = lot_size;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getResale() {
		return resale;
	}

	public void setResale(String resale) {
		this.resale = resale;
	}

	public String getNew_construction() {
		return new_construction;
	}

	public void setNew_construction(String new_construction) {
		this.new_construction = new_construction;
	}

	public String getForeclosure() {
		return foreclosure;
	}

	public void setForeclosure(String foreclosure) {
		this.foreclosure = foreclosure;
	}

	public String getOpen_house() {
		return open_house;
	}

	public void setOpen_house(String open_house) {
		this.open_house = open_house;
	}

	public String getReduced_prices() {
		return reduced_prices;
	}

	public void setReduced_prices(String reduced_prices) {
		this.reduced_prices = reduced_prices;
	}

	public String getRb_block() {
		return rb_block;
	}

	public void setRb_block(String rb_block) {
		this.rb_block = rb_block;
	}

	public String getProperty_datetime() {
		return property_datetime;
	}

	public void setProperty_datetime(String property_datetime) {
		this.property_datetime = property_datetime;
	}

	public String getReview_count() {
		return review_count;
	}

	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}

	public String getIsfavourite() {
		return isfavourite;
	}

	public void setIsfavourite(String isfavourite) {
		this.isfavourite = isfavourite;
	}

	public ArrayList<PropertyPictures> getProperty_pics() {
		return property_pics;
	}

	public void setProperty_pics(ArrayList<PropertyPictures> property_pics) {
		this.property_pics = property_pics;
	}

	public ArrayList<UserPropertyPics> getUserpropertypic() {
		return userpropertypic;
	}

	public void setUserpropertypic(ArrayList<UserPropertyPics> userpropertypic) {
		this.userpropertypic = userpropertypic;
	}

	public ArrayList<PropertyReview> getProperty_review() {
		return property_review;
	}

	public void setProperty_review(ArrayList<PropertyReview> property_review) {
		this.property_review = property_review;
	}

}

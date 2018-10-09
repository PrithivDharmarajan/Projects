package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardsDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private String board_id;
	private String user_id;
	private String property_id;
	private String date_time;
	private String review_count;
	private String property_rating;
	private String enhanced_profile;;
	private ArrayList<PropertyDetails> property_details;
	private ArrayList<PropertyPictures> property_pics;
	
	public String getEnhanced_profile() {
		return enhanced_profile;
	}

	public void setEnhanced_profile(String enhanced_profile) {
		this.enhanced_profile = enhanced_profile;
	}
	
	public String getBoard_id() {
		return board_id;
	}

	public void setBoard_id(String board_id) {
		this.board_id = board_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public ArrayList<PropertyDetails> getProperty_details() {
		return property_details;
	}

	public void setProperty_details(ArrayList<PropertyDetails> property_details) {
		this.property_details = property_details;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReview_count() {
		return review_count;
	}

	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}

	public String getProperty_rating() {
		return property_rating;
	}

	public void setProperty_rating(String property_rating) {
		this.property_rating = property_rating;
	}

	public ArrayList<PropertyPictures> getProperty_pics() {
		return property_pics;
	}

	public void setProperty_pics(ArrayList<PropertyPictures> property_pics) {
		this.property_pics = property_pics;
	}

}

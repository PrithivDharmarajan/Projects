package com.smaat.renterblock.profile.Entity;

import java.io.Serializable;

public class ProfileUserReviews implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String property_review_id;
	private String property_id;
	private String review_user_id;
	private String comments;
	private String rating;
	private String date_time;
	private String property_name;
	private String address;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProperty_review_id() {
		return property_review_id;
	}
	public void setProperty_review_id(String property_review_id) {
		this.property_review_id = property_review_id;
	}
	public String getProperty_id() {
		return property_id;
	}
	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}
	public String getReview_user_id() {
		return review_user_id;
	}
	public void setReview_user_id(String review_user_id) {
		this.review_user_id = review_user_id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getDate_time() {
		return date_time;
	}
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}
	public String getProperty_name() {
		return property_name;
	}
	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}
		
}

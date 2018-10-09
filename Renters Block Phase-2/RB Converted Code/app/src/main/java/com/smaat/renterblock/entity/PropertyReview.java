package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyReview implements Serializable {

	private static final long serialVersionUID = 1L;

	private String property_review_id;
	private String property_id;
	private String review_user_id;
	private String comments;
	private String rating;
	private String date_time;
	private String review_count;
	private ArrayList<PostPropertyUserDetails> review_user_details;

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

	public ArrayList<PostPropertyUserDetails> getReview_user_details() {
		return review_user_details;
	}

	public void setReview_user_details(
			ArrayList<PostPropertyUserDetails> review_user_details) {
		this.review_user_details = review_user_details;
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

}

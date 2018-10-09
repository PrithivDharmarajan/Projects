package com.smaat.renterblock.entity;

import java.io.Serializable;

public class PropertyReviewComments implements Serializable {

	private static final long serialVersionUID = 1L;

	private String property_review_comment_id;
	private String review_user_id;
	private String review_comments;
	private String review_date_time;
	private String review_rating;

	public String getProperty_review_comment_id() {
		return property_review_comment_id;
	}

	public void setProperty_review_comment_id(String property_review_comment_id) {
		this.property_review_comment_id = property_review_comment_id;
	}

	public String getReview_user_id() {
		return review_user_id;
	}

	public void setReview_user_id(String review_user_id) {
		this.review_user_id = review_user_id;
	}

	public String getReview_comments() {
		return review_comments;
	}

	public void setReview_comments(String review_comments) {
		this.review_comments = review_comments;
	}

	public String getReview_date_time() {
		return review_date_time;
	}

	public void setReview_date_time(String review_date_time) {
		this.review_date_time = review_date_time;
	}

	public String getReview_rating() {
		return review_rating;
	}

	public void setReview_rating(String review_rating) {
		this.review_rating = review_rating;
	}

}

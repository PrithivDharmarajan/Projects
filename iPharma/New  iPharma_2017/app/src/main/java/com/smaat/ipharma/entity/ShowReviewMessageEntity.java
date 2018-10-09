package com.smaat.ipharma.entity;

import java.io.Serializable;

public class ShowReviewMessageEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String ReviewID;
	private String ShopID;
	private String UserID;
	private String ReviewRating;
	private String ReviewHeading;
	private String ReviewComment;
	private String ReviewDateTime;
	private String FullName;

	public String getReviewID() {
		return ReviewID;
	}
	public void setReviewID(String reviewID) {
		ReviewID = reviewID;
	}
	public String getShopID() {
		return ShopID;
	}
	public void setShopID(String shopID) {
		ShopID = shopID;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getReviewRating() {
		return ReviewRating;
	}
	public void setReviewRating(String reviewRating) {
		ReviewRating = reviewRating;
	}
	public String getReviewHeading() {
		return ReviewHeading;
	}
	public void setReviewHeading(String reviewHeading) {
		ReviewHeading = reviewHeading;
	}
	public String getReviewComment() {
		return ReviewComment;
	}
	public void setReviewComment(String reviewComment) {
		ReviewComment = reviewComment;
	}
	public String getReviewDateTime() {
		return ReviewDateTime;
	}
	public void setReviewDateTime(String reviewDateTime) {
		ReviewDateTime = reviewDateTime;
	}
	public String getFullName() {
		return FullName;
	}
	public void setFullName(String fullName) {
		FullName = fullName;
	}

}

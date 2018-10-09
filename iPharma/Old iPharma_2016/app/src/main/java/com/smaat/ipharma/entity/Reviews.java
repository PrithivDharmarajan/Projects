package com.smaat.ipharma.entity;

public class Reviews {

	String UserName;
	String DateTime;
	String ReviewHeading;
	String Description;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getDateTime() {
		return DateTime;
	}

	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}

	public String getReviewHeading() {
		return ReviewHeading;
	}

	public void setReviewHeading(String reviewHeading) {
		ReviewHeading = reviewHeading;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

}

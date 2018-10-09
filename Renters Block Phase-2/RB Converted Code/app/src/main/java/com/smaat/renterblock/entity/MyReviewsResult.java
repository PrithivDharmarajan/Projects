package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MyReviewsResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<MyReviews> review;
	private MyReviewUserDetails user_details;

	public ArrayList<MyReviews> getReview() {
		return review;
	}

	public void setReview(ArrayList<MyReviews> review) {
		this.review = review;
	}

	public MyReviewUserDetails getUser_details() {
		return user_details;
	}

	public void setUser_details(MyReviewUserDetails user_details) {
		this.user_details = user_details;
	}

}

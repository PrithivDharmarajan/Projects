package com.smaat.renterblock.entity;

import java.io.Serializable;

public class SavedSearchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String save_search_id;
	private String user_id;
	private String filter_object;
	private String filter_type;
	private String date;
	private String email_notification;
	private String inquiry;

	public String getSave_search_id() {
		return save_search_id;
	}

	public void setSave_search_id(String save_search_id) {
		this.save_search_id = save_search_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFilter_type() {
		return filter_type;
	}

	public void setFilter_type(String filter_type) {
		this.filter_type = filter_type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEmail_notification() {
		return email_notification;
	}

	public void setEmail_notification(String email_notification) {
		this.email_notification = email_notification;
	}

	public String getInquiry() {
		return inquiry;
	}

	public void setInquiry(String inquiry) {
		this.inquiry = inquiry;
	}

	public String getFilter_object() {
		return filter_object;
	}

	public void setFilter_object(String filter_object) {
		this.filter_object = filter_object;
	}

}

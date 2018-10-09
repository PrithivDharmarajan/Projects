package com.smaat.renterblock.entity;

import java.util.ArrayList;

public class Login {

	private String user_id;
	private String first_name;
	private String last_name;
	private String user_name;
	private String email_id;
	private String password;
	private String zip;
	private String phone;
	private String login_type;
	private String user_type;
	private String status;
	private String date;
	private String filter_object;
	private String enhanced_profile;	
	private ArrayList<SavedSearchLoginEntity> savedsearch;
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getEnhanced_profile() {
		return enhanced_profile;
	}

	public void setEnhanced_profile(String enhanced_profile) {
		this.enhanced_profile = enhanced_profile;
	}

	public ArrayList<SavedSearchLoginEntity> getSavedsearch() {
		return savedsearch;
	}

	public void setSavedsearch(ArrayList<SavedSearchLoginEntity> savedsearch) {
		this.savedsearch = savedsearch;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLogin_type() {
		return login_type;
	}

	public void setLogin_type(String login_type) {
		this.login_type = login_type;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFilter_object() {
		return filter_object;
	}

	public void setFilter_object(String filter_object) {
		this.filter_object = filter_object;
	}

}

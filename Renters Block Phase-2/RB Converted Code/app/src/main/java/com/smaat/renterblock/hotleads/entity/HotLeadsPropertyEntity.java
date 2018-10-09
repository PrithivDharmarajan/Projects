package com.smaat.renterblock.hotleads.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HotLeadsPropertyEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String propertyId;
	private String price_range;
	private String property_id;
	private String beds;
	private String baths;
	private String square_footage;
	private String build_year;
	private String address;
	private String property_name;
	private String propertyImage;
	private String review_count;
	private String property_rating;
	private String leads_count;
	private LeadsListEntity leadslist;

	public LeadsListEntity getLeadslist() {
		return leadslist;
	}

	public void setLeadslist(LeadsListEntity leadslist) {
		this.leadslist = leadslist;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPrice_range() {
		return price_range;
	}

	public void setPrice_range(String price_range) {
		this.price_range = price_range;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
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

	public String getProperty_name() {
		return property_name;
	}

	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}

	public String getPropertyImage() {
		return propertyImage;
	}

	public void setPropertyImage(String propertyImage) {
		this.propertyImage = propertyImage;
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

	public String getLeads_count() {
		return leads_count;
	}

	public void setLeads_count(String leads_count) {
		this.leads_count = leads_count;
	}

}

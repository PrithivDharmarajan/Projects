package com.smaat.ipharma.db.model;

import java.io.Serializable;

public class UserDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private String phone_number;
	private String pharmacy_name;
	private String address;
	private String owner_name;
	private String website;
	private String email_id;
	private String opening_time;
	private String closing_time;
	private String delivery_time;
	private String min_purchase;
	private String home_delivery;

	private double latitude;
	private double longitude;
	private String datetime;
	private Image image;
	private String isDelivery;
	private int rowid;
private int	isNew;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPharmacy_name() {
		return pharmacy_name;
	}

	public void setPharmacy_name(String pharmacy_name) {
		this.pharmacy_name = pharmacy_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getOpening_time() {
		return opening_time;
	}

	public void setOpening_time(String opening_time) {
		this.opening_time = opening_time;
	}

	public String getClosing_time() {
		return closing_time;
	}

	public void setClosing_time(String closing_time) {
		this.closing_time = closing_time;
	}

	public String getDelivery_time() {
		return delivery_time;
	}

	public void setDelivery_time(String delivery_time) {
		this.delivery_time = delivery_time;
	}

	public String getMin_purchase() {
		return min_purchase;
	}

	public void setMin_purchase(String min_purchase) {
		this.min_purchase = min_purchase;
	}

	public String getHome_delivery() {
		return home_delivery;
	}

	public void setHome_delivery(String home_delivery) {
		this.home_delivery = home_delivery;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(String isDelivery) {
		this.isDelivery = isDelivery;
	}

	public int getRowid() {
		return rowid;
	}

	public void setRowid(int rowid) {
		this.rowid = rowid;
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}

}

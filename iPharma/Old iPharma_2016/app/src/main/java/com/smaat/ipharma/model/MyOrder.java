package com.smaat.ipharma.model;

import java.io.Serializable;

public class MyOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pharmacyname;
	private String pharmacyimage;
	private String status;
	private String deliverytime;

	public String getPharmacyname() {
		return pharmacyname;
	}

	public void setPharmacyname(String pharmacyname) {
		this.pharmacyname = pharmacyname;
	}

	public String getPharmacyimage() {
		return pharmacyimage;
	}

	public void setPharmacyimage(String pharmacyimage) {
		this.pharmacyimage = pharmacyimage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeliverytime() {
		return deliverytime;
	}

	public void setDeliverytime(String deliverytime) {
		this.deliverytime = deliverytime;
	}

}

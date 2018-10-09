package com.smaat.ipharma.entity;

import java.io.Serializable;

public class MarkerID implements Serializable {

	private static final long serialVersionUID = 1L;
	private String markerid;
	private String markername;
	private String markerpropType;
	private String pharmacyname;
	private String pharmacyaddress;

	public String getMarkerpropType() {
		return markerpropType;
	}

	public void setMarkerpropType(String markerpropType) {
		this.markerpropType = markerpropType;
	}

	public String getMarkerid() {
		return markerid;
	}

	public void setMarkerid(String markerid) {
		this.markerid = markerid;
	}

	public String getMarkername() {
		return markername;
	}

	public void setMarkername(String markername) {
		this.markername = markername;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPharmacyname() {
		return pharmacyname;
	}

	public void setPharmacyname(String pharmacyname) {
		this.pharmacyname = pharmacyname;
	}

	public String getPharmacyaddress() {
		return pharmacyaddress;
	}

	public void setPharmacyaddress(String pharmacyaddress) {
		this.pharmacyaddress = pharmacyaddress;
	}

}

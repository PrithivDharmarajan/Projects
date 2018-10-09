package com.smaat.renterblock.entity;

import java.io.Serializable;

public class MarkerID implements Serializable {

	private static final long serialVersionUID = 1L;
	private String markerid;
	private String markername;
	private String markerpropType;

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

}

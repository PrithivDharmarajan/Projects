package com.smaat.ipharma.entity;

import java.io.Serializable;

public class Geometry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocationEntity location;

	public LocationEntity getLocation() {
		return location;
	}

	public void setLocation(LocationEntity location) {
		this.location = location;
	}

}

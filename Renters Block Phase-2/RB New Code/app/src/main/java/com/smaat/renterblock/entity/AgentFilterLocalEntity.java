package com.smaat.renterblock.entity;

import java.io.Serializable;

public class AgentFilterLocalEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String location;
	private String type;
	private String price_range_min;
	private String price_range_max;
	private String property_experties;
	private String latitude;
	private String longitude;
	private boolean isUpdated=false;

	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean updated) {
		isUpdated = updated;
	}

	public String getName() {
		return name==null?"":name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location==null?"":location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type==null?"":type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice_range_min() {
		return price_range_min==null?"":price_range_min;
	}

	public void setPrice_range_min(String price_range_min) {
		this.price_range_min = price_range_min;
	}

	public String getPrice_range_max() {
		return price_range_max==null?"":price_range_max;
	}

	public void setPrice_range_max(String price_range_max) {
		this.price_range_max = price_range_max;
	}

	public String getProperty_experties() {
		return property_experties==null?"":property_experties;
	}

	public void setProperty_experties(String property_experties) {
		this.property_experties = property_experties;
	}

	public String getLatitude() {
		return latitude==null?"":latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude==null?"":longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}

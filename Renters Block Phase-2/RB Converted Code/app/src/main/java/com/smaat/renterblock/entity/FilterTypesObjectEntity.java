package com.smaat.renterblock.entity;

import java.io.Serializable;

public class FilterTypesObjectEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FilterObjectEntity filterObject;

	public FilterObjectEntity getFilterObject() {
		return filterObject;
	}

	public void setFilterObject(FilterObjectEntity filterObject) {
		this.filterObject = filterObject;
	}

}

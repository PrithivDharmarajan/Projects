package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class GoogleApiEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<FormattedAddressEntity> results;

	public ArrayList<FormattedAddressEntity> getResults() {
		return results;
	}

	public void setResults(ArrayList<FormattedAddressEntity> results) {
		this.results = results;
	}
}

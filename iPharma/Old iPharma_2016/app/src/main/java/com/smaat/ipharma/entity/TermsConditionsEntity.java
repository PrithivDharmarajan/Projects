package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class TermsConditionsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private ArrayList<TermsResults> result;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<TermsResults> getResult() {
		return result;
	}
	public void setResult(ArrayList<TermsResults> result) {
		this.result = result;
	}

}

package com.smaat.ipharma.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.ipharma.entity.AboutUsEntity;

public class AboutUsResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String status;
	private ArrayList<AboutUsEntity> result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<AboutUsEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<AboutUsEntity> result) {
		this.result = result;
	}
}

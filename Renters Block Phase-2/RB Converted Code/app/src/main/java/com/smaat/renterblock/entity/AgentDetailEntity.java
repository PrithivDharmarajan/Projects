package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentDetailEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String total_records;
	private ArrayList<AgentDeatailsResultEntity> result;

	public ArrayList<AgentDeatailsResultEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<AgentDeatailsResultEntity> result) {
		this.result = result;
	}

	public String getTotal_records() {
		return total_records;
	}

	public void setTotal_records(String total_records) {
		this.total_records = total_records;
	}
}

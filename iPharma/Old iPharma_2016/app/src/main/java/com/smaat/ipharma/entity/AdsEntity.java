package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class AdsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private String msg;
	private ArrayList<AdsDetailEntity> result;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList<AdsDetailEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<AdsDetailEntity> result) {
		this.result = result;
	}
	
}

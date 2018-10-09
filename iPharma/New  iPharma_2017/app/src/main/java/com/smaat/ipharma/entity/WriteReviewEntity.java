package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class WriteReviewEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String msg;
	private ArrayList<ShowReviewMessageEntity> result;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<ShowReviewMessageEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<ShowReviewMessageEntity> result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}

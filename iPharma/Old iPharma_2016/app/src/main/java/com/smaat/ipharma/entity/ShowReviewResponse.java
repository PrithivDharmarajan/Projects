package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowReviewResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private ArrayList<ShowReviewMessageEntity> msg;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<ShowReviewMessageEntity> getMsg() {
		return msg;
	}
	public void setMsg(ArrayList<ShowReviewMessageEntity> msg) {
		this.msg = msg;
	}

}

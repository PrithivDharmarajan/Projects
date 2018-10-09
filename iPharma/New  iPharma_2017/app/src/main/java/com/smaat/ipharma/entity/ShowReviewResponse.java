package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowReviewResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private ArrayList<ShowReviewMessageEntity> result;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<ShowReviewMessageEntity> getMsg() {
		return result;
	}
	public void setMsg(ArrayList<ShowReviewMessageEntity> msg) {
		this.result = msg;
	}

}

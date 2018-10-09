package com.smaat.ipharma.entity;

import java.io.Serializable;

public class OrderHistoryCommonResponseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String msg;
	private HistoryResultEntity result;
	
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
	public HistoryResultEntity getResult() {
		return result;
	}
	public void setResult(HistoryResultEntity result) {
		this.result = result;
	}

}

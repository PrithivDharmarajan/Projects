package com.smaat.ipharma.entity;

import java.io.Serializable;

public class NewOrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String status;
	private String msg;
	private NewOrderResultEntity result;
	
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
	public NewOrderResultEntity getResult() {
		return result;
	}
	public void setResult(NewOrderResultEntity result) {
		this.result = result;
	}

}

package com.smaat.renterblock.alerts;

import java.io.Serializable;
import java.util.ArrayList;

public class AlertsResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String msg;
	private ArrayList<AlertsEntity> result;
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList<AlertsEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<AlertsEntity> result) {
		this.result = result;
	}
	
	
}

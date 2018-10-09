package com.smaat.renterblock.hotleads.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HotLeadsModelEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String msg;
	private String currentdatetime;
	public String getCurrentdatetime() {
		return currentdatetime;
	}
	public void setCurrentdatetime(String currentdatetime) {
		this.currentdatetime = currentdatetime;
	}
	private ArrayList<HotLeadsPropertyEntity> result;
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
	public ArrayList<HotLeadsPropertyEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<HotLeadsPropertyEntity> result) {
		this.result = result;
	}
	

}

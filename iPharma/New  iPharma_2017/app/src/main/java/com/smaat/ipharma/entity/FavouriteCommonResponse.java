package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class FavouriteCommonResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private String msg;
	private ArrayList<MapPropertyEntity> result;
	
//	private ArrayList<FavouritesEntity> result;
	
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
//	public ArrayList<FavouritesEntity> getResult() {
//		return result;
//	}
//	public void setResult(ArrayList<FavouritesEntity> result) {
//		this.result = result;
//	}
	public ArrayList<MapPropertyEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<MapPropertyEntity> result) {
		this.result = result;
	}
}

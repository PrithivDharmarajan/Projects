package com.smaat.ipharma.entity;

import com.smaat.ipharma.entity.MapPropertyEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class MapResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	String status;
	String msg;
	ArrayList<MapPropertyEntity> result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<MapPropertyEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<MapPropertyEntity> result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
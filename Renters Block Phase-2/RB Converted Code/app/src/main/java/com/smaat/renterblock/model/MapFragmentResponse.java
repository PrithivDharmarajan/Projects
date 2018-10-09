package com.smaat.renterblock.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.renterblock.entity.AgentDetailEntity;
import com.smaat.renterblock.entity.MapPropertDetailsEntity;

public class MapFragmentResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String msg;
	private String is_purchased;
	private ArrayList<MapPropertDetailsEntity> result;
	private AgentDetailEntity agent;
	
	public String getIs_purchased() {
		return is_purchased;
	}
	public void setIs_purchased(String is_purchased) {
		this.is_purchased = is_purchased;
	}
	public AgentDetailEntity getAgent() {
		return agent;
	}
	public void setAgent(AgentDetailEntity agent) {
		this.agent = agent;
	}
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
	public ArrayList<MapPropertDetailsEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<MapPropertDetailsEntity> result) {
		this.result = result;
	}

}
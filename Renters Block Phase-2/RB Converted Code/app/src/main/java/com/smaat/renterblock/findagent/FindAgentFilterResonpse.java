package com.smaat.renterblock.findagent;

import java.io.Serializable;
import java.util.ArrayList;

public class FindAgentFilterResonpse implements Serializable {

	private static final long serialVersionUID = 1L;

	public String error_code;
	public String msg;
	public String search_result_count;
	public String location;
	public ArrayList<AgentFilterResultEntity> result;

	public String getSearch_result_count() {
		return search_result_count;
	}

	public void setSearch_result_count(String search_result_count) {
		this.search_result_count = search_result_count;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public ArrayList<AgentFilterResultEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<AgentFilterResultEntity> result) {
		this.result = result;
	}

}

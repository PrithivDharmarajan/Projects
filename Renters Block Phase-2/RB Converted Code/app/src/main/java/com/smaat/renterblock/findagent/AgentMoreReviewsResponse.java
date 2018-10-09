package com.smaat.renterblock.findagent;

import java.io.Serializable;

public class AgentMoreReviewsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public String error_code;
	public String msg;
	private AgentUserDetailsResult result;

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

	public AgentUserDetailsResult getResult() {
		return result;
	}

	public void setResult(AgentUserDetailsResult result) {
		this.result = result;
	}


}

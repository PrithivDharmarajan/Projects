package com.bridgellc.bridge.model;


import com.bridgellc.bridge.entity.SignInEntity;

public class SignInResponse extends CommonModelResponse {

	private SignInEntity result;

	public SignInEntity getResult() {
		return result;
	}

	public void setResult(SignInEntity result) {
		this.result = result;
	}




}

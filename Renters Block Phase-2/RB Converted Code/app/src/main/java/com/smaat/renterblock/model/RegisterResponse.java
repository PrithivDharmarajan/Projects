package com.smaat.renterblock.model;

import java.io.Serializable;

import com.smaat.renterblock.entity.Register;

public class RegisterResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	public String error_code;
	public Register result;
	public String msg;

}

package com.smaat.ipharma.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.ipharma.entity.MyOrder;

public class PrescriptionResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	String error_code;
	String msg;
	ArrayList<MyOrder> result;
}

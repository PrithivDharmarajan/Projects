package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryResultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<HistoryOrderEntity> OrderResult;

	public ArrayList<HistoryOrderEntity> getOrderResult() {
		return OrderResult;
	}

	public void setOrderResult(ArrayList<HistoryOrderEntity> orderResult) {
		OrderResult = orderResult;
	}

//	public ArrayList<HistoryOrderEntity> getOrders() {
//		return Orders;
//	}
//
//	public void setOrders(ArrayList<HistoryOrderEntity> orders) {
//		Orders = orders;
//	}
	
}

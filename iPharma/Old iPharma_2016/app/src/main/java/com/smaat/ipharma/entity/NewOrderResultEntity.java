package com.smaat.ipharma.entity;

import java.io.Serializable;

public class NewOrderResultEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private String msg;
	private String result;
	private String PrescriptionUrl;
	private String PrescriptionID;
	private String ShopID;
	private String UserID;
	private String DeliveryAddress;
	private String NewAddress;
	private String OrderDateTime;
	private String OrderID;
	
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
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPrescriptionUrl() {
		return PrescriptionUrl;
	}
	public void setPrescriptionUrl(String prescriptionUrl) {
		PrescriptionUrl = prescriptionUrl;
	}
	public String getPrescriptionID() {
		return PrescriptionID;
	}
	public void setPrescriptionID(String prescriptionID) {
		PrescriptionID = prescriptionID;
	}
	public String getShopID() {
		return ShopID;
	}
	public void setShopID(String shopID) {
		ShopID = shopID;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getDeliveryAddress() {
		return DeliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		DeliveryAddress = deliveryAddress;
	}
	public String getNewAddress() {
		return NewAddress;
	}
	public void setNewAddress(String newAddress) {
		NewAddress = newAddress;
	}
	public String getOrderDateTime() {
		return OrderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		OrderDateTime = orderDateTime;
	}
	public String getOrderID() {
		return OrderID;
	}
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
	
}

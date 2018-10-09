package com.smaat.ipharma.model;

public class PlaceOrderEntity {
	private String PharmacyName;
	private String PharmacyAddress;
	private String PharmacyDistance;
	private String DeliveryAddress;
	private String CustomerName;
	private String CustomerAddress;
	private String Rating;

	public String getRating() {
		return Rating;
	}

	public void setRating(String rating) {
		Rating = rating;
	}

	public String getPharmacyName() {
		return PharmacyName;
	}

	public void setPharmacyName(String pharmacyName) {
		PharmacyName = pharmacyName;
	}

	public String getPharmacyAddress() {
		return PharmacyAddress;
	}

	public void setPharmacyAddress(String pharmacyAddress) {
		PharmacyAddress = pharmacyAddress;
	}

	public String getPharmacyDistance() {
		return PharmacyDistance;
	}

	public void setPharmacyDistance(String pharmacyDistance) {
		PharmacyDistance = pharmacyDistance;
	}

	public String getDeliveryAddress() {
		return DeliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		DeliveryAddress = deliveryAddress;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getCustomerAddress() {
		return CustomerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		CustomerAddress = customerAddress;
	}

}

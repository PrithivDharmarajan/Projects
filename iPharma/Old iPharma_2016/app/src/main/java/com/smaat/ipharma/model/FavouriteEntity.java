package com.smaat.ipharma.model;

public class FavouriteEntity {
	private byte[] PharmacyImage;
	private String PharmacyName;
	private String PharmacyAddress;
	private String PharmacyReviews;
	private String PharmacyDistance;
	private String Favourite;
	private String DeliveryTime;
	private String MinimumOrder;

	public byte[] getPharmacyImage() {
		return PharmacyImage;
	}

	public void setPharmacyImage(byte[] pharmacyImage) {
		PharmacyImage = pharmacyImage;
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

	public String getPharmacyReviews() {
		return PharmacyReviews;
	}

	public void setPharmacyReviews(String pharmacyReviews) {
		PharmacyReviews = pharmacyReviews;
	}

	public String getPharmacyDistance() {
		return PharmacyDistance;
	}

	public void setPharmacyDistance(String pharmacyDistance) {
		PharmacyDistance = pharmacyDistance;
	}

	public String getFavourite() {
		return Favourite;
	}

	public void setFavourite(String favourite) {
		Favourite = favourite;
	}

	public String getDeliveryTime() {
		return DeliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		DeliveryTime = deliveryTime;
	}

	public String getMinimumOrder() {
		return MinimumOrder;
	}

	public void setMinimumOrder(String minimumOrder) {
		MinimumOrder = minimumOrder;
	}

}

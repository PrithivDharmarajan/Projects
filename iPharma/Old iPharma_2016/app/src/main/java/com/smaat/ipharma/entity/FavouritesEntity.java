package com.smaat.ipharma.entity;

import java.io.Serializable;

public class FavouritesEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String PharmacyID;
	private String ShopName;
	private String OwnerName;
	private String ShopUserName;
	private String ShopPassword;
	private String RegisteredDateTime;
	private String ShopIcon;
	private String Address;
	private String City;
	private String Area;
	private String Pincode;
	private String Landmark;
	private String Latitude;
	private String Longitude;
	private String Email;
	private String Phone;
	private String Mobile;
	private String Website;
	private String OpenTime;
	private String CloseTime;
	private String ProfilePic;
	private String ProfileVideo;
	private String HomeDelivery;
	private String IsPremium;
	private String DeliveryTime;
	private String TotalReviews;
	private String Distance;
	
	public String getDistance() {
		return Distance;
	}
	public void setDistance(String distance) {
		Distance = distance;
	}
	private String AvgRating;
	
	public String getTotalReviews() {
		return TotalReviews;
	}
	public void setTotalReviews(String totalReviews) {
		TotalReviews = totalReviews;
	}
	public String getAvgRating() {
		return AvgRating;
	}
	public void setAvgRating(String avgRating) {
		AvgRating = avgRating;
	}
//	public String getFavID() {
//		return FavID;
//	}
//	public void setFavID(String favID) {
//		FavID = favID;
//	}
//	public String getShopID() {
//		return ShopID;
//	}
//	public void setShopID(String shopID) {
//		ShopID = shopID;
//	}
//	public String getUserID() {
//		return UserID;
//	}
//	public void setUserID(String userID) {
//		UserID = userID;
//	}
	public String getPharmacyID() {
		return PharmacyID;
	}
	public void setPharmacyID(String pharmacyID) {
		PharmacyID = pharmacyID;
	}
	public String getShopUserName() {
		return ShopUserName;
	}
	public void setShopUserName(String shopUserName) {
		ShopUserName = shopUserName;
	}
	public String getShopPassword() {
		return ShopPassword;
	}
	public void setShopPassword(String shopPassword) {
		ShopPassword = shopPassword;
	}
	public String getRegisteredDateTime() {
		return RegisteredDateTime;
	}
	public void setRegisteredDateTime(String registeredDateTime) {
		RegisteredDateTime = registeredDateTime;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getOwnerName() {
		return OwnerName;
	}
	public void setOwnerName(String ownerName) {
		OwnerName = ownerName;
	}
	public String getShopIcon() {
		return ShopIcon;
	}
	public void setShopIcon(String shopIcon) {
		ShopIcon = shopIcon;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public String getPincode() {
		return Pincode;
	}
	public void setPincode(String pincode) {
		Pincode = pincode;
	}
	public String getLandmark() {
		return Landmark;
	}
	public void setLandmark(String landmark) {
		Landmark = landmark;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getWebsite() {
		return Website;
	}
	public void setWebsite(String website) {
		Website = website;
	}
	public String getOpenTime() {
		return OpenTime;
	}
	public void setOpenTime(String openTime) {
		OpenTime = openTime;
	}
	public String getCloseTime() {
		return CloseTime;
	}
	public void setCloseTime(String closeTime) {
		CloseTime = closeTime;
	}
	public String getProfilePic() {
		return ProfilePic;
	}
	public void setProfilePic(String profilePic) {
		ProfilePic = profilePic;
	}
	public String getProfileVideo() {
		return ProfileVideo;
	}
	public void setProfileVideo(String profileVideo) {
		ProfileVideo = profileVideo;
	}
	public String getHomeDelivery() {
		return HomeDelivery;
	}
	public void setHomeDelivery(String homeDelivery) {
		HomeDelivery = homeDelivery;
	}
	public String getIsPremium() {
		return IsPremium;
	}
	public void setIsPremium(String isPremium) {
		IsPremium = isPremium;
	}
	public String getDeliveryTime() {
		return DeliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		DeliveryTime = deliveryTime;
	}
}

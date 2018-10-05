package com.smaat.jolt.entity;

import java.util.ArrayList;

public class ShopDetailsEntity {
	private String distance;
	private String ShopId;
	private String ShopName;
	private String ShopStreet;
	private String ShopLogoImageUrl;
	private String ShopImage1;
	private String ShopImage2;
	private String ShopImage3;
	private ShopOpenTimeEntity ShopOpenTime;
	private String ShopCityName;
	private String ShopSubway;
	private String ShopContactNumber;
	private ArrayList<DrinksDetailsEntity> DrinksDetails;
	private String Beans;
	private String ShopFacilities;
	private String ShopLatitude;
	private String ShopLongitude;
	private String ShopWebsite;
	private String Speciality;
	private String Atmosphere;
	
	

	public String getAtmosphere() {
		return Atmosphere;
	}

	public void setAtmosphere(String atmosphere) {
		Atmosphere = atmosphere;
	}

	public String getShopWebsite() {
		return ShopWebsite;
	}

	public void setShopWebsite(String shopWebsite) {
		ShopWebsite = shopWebsite;
	}

	public String getShopId() {
		return ShopId;
	}

	public void setShopId(String shopId) {
		ShopId = shopId;
	}

	public String getShopName() {
		return ShopName;
	}

	public void setShopName(String shopName) {
		ShopName = shopName;
	}

	public String getShopStreet() {
		return ShopStreet;
	}

	public void setShopStreet(String shopStreet) {
		ShopStreet = shopStreet;
	}

	public String getShopLogoImageUrl() {
		return ShopLogoImageUrl;
	}

	public void setShopLogoImageUrl(String shopLogoImageUrl) {
		ShopLogoImageUrl = shopLogoImageUrl;
	}

	public String getShopImage1() {
		return ShopImage1;
	}

	public void setShopImage1(String shopImage1) {
		ShopImage1 = shopImage1;
	}

	public String getShopImage2() {
		return ShopImage2;
	}

	public void setShopImage2(String shopImage2) {
		ShopImage2 = shopImage2;
	}

	public String getShopImage3() {
		return ShopImage3;
	}

	public void setShopImage3(String shopImage3) {
		ShopImage3 = shopImage3;
	}

	public ShopOpenTimeEntity getShopOpenTime() {
		return ShopOpenTime;
	}

	public void setShopOpenTime(ShopOpenTimeEntity shopOpenTime) {
		ShopOpenTime = shopOpenTime;
	}

	public String getShopCityName() {
		return ShopCityName;
	}

	public void setShopCityName(String shopCityName) {
		ShopCityName = shopCityName;
	}

	public String getShopSubway() {
		return ShopSubway;
	}

	public void setShopSubway(String shopSubway) {
		ShopSubway = shopSubway;
	}

	public String getShopContactNumber() {
		return ShopContactNumber;
	}

	public void setShopContactNumber(String shopContactNumber) {
		ShopContactNumber = shopContactNumber;
	}

	public ArrayList<DrinksDetailsEntity> getDrinksDetails() {
		return DrinksDetails;
	}

	public void setDrinksDetails(ArrayList<DrinksDetailsEntity> drinksDetails) {
		DrinksDetails = drinksDetails;
	}

	public String getBeans() {
		return Beans;
	}

	public void setBeans(String beans) {
		Beans = beans;
	}

	public String getShopFacilities() {
		return ShopFacilities;
	}

	public void setShopFacilities(String shopFacilities) {
		ShopFacilities = shopFacilities;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getShopLattitude() {
		return ShopLatitude;
	}

	public void setShopLattitude(String shopLattitude) {
		ShopLatitude = shopLattitude;
	}

	public String getShopLongitude() {
		return ShopLongitude;
	}

	public void setShopLongitude(String shopLongitude) {
		ShopLongitude = shopLongitude;
	}

	public String getSpeciality() {
		return Speciality;
	}

	public void setSpeciality(String speciality) {
		Speciality = speciality;
	}
}

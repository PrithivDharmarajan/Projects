package com.smaat.jolt.entity;

public class HistoryDetailsEntity {

	private String PurchaseDrinksID;
	private String UserID;
	private String NoOfDrinks;
	private String ShopID;
	private String SelectedDrinks;
	private String SelectedDrinksSize;
	private String ShopCode;
	private String TipAmount;
	private String datetime;
	private String ShopName;
	
	

	public String getShopName() {
		return ShopName;
	}

	public void setShopName(String shopName) {
		ShopName = shopName;
	}

	public String getPurchaseDrinksID() {
		return PurchaseDrinksID;
	}

	public void setPurchaseDrinksID(String purchaseDrinksID) {
		PurchaseDrinksID = purchaseDrinksID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getNoOfDrinks() {
		return NoOfDrinks;
	}

	public void setNoOfDrinks(String noOfDrinks) {
		NoOfDrinks = noOfDrinks;
	}

	public String getShopID() {
		return ShopID;
	}

	public void setShopID(String shopID) {
		ShopID = shopID;
	}

	public String getSelectedDrinks() {
		return SelectedDrinks;
	}

	public void setSelectedDrinks(String selectedDrinks) {
		SelectedDrinks = selectedDrinks;
	}

	public String getSelectedDrinksSize() {
		return SelectedDrinksSize;
	}

	public void setSelectedDrinksSize(String selectedDrinksSize) {
		SelectedDrinksSize = selectedDrinksSize;
	}

	public String getShopCode() {
		return ShopCode;
	}

	public void setShopCode(String shopCode) {
		ShopCode = shopCode;
	}

	public String getTipAmount() {
		return TipAmount;
	}

	public void setTipAmount(String tipAmount) {
		TipAmount = tipAmount;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}

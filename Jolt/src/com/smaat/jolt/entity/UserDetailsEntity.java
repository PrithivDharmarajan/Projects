package com.smaat.jolt.entity;

import java.util.ArrayList;

import com.smaat.jolt.model.PlanEntity;

public class UserDetailsEntity {
	private String UserID;
	private String ProfilePicture;
	private String FullName;
	private String NoOfDrinksAvailable;
	private String EmailAddress;
	private String CouponCode;
	private String CardNumber;
	private String CardExpiryMonth;
	private String CardExpiryYear;
	private String CardCVV;
	private String CurrentPlan;
	private String expiry_date;
	private String RefillON;

	private ArrayList<PlanEntity> Plan;

	public String getCurrentPlan() {
		return CurrentPlan;
	}

	public void setCurrentPlan(String currentPlan) {
		CurrentPlan = currentPlan;
	}

	public String getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}

	public String getRefillON() {
		return RefillON;
	}

	public void setRefillON(String refillON) {
		RefillON = refillON;
	}

	public String getCardNumber() {
		return CardNumber;
	}

	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}

	public String getCardExpiryMonth() {
		return CardExpiryMonth;
	}

	public void setCardExpiryMonth(String cardExpiryMonth) {
		CardExpiryMonth = cardExpiryMonth;
	}

	public String getCardExpiryYear() {
		return CardExpiryYear;
	}

	public void setCardExpiryYear(String cardExpiryYear) {
		CardExpiryYear = cardExpiryYear;
	}

	public String getCardCVV() {
		return CardCVV;
	}

	public void setCardCVV(String cardCVV) {
		CardCVV = cardCVV;
	}

	public String getMyCoffeCode() {
		return CouponCode;
	}

	public void setMyCoffeCode(String myCoffeCode) {
		CouponCode = myCoffeCode;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getProfilePicture() {
		return ProfilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		ProfilePicture = profilePicture;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getNoOfDrinksAvailable() {
		return NoOfDrinksAvailable;
	}

	public void setNoOfDrinksAvailable(String noOfDrinksAvailable) {
		NoOfDrinksAvailable = noOfDrinksAvailable;
	}

	public String getEmailAddress() {
		return EmailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		EmailAddress = emailAddress;
	}

	public ArrayList<PlanEntity> getPlan() {
		return Plan;
	}

	public void setPlan(ArrayList<PlanEntity> plan) {
		Plan = plan;
	}

}

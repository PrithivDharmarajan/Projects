package com.smaat.jolt.entity;

public class JoltPlansListEntity {
	private String ClassicPlansID;
    private String PlanLogo;
    private String Cups;
    private String Cost;
    private String Expiration;
    private String IsMostPopular;
    private String IsIncludingTax;
    private String PerCupCostInfo;
    private String PlanIncludesDetails;
    private String  AllDrinksPlansID;
    
    
	public String getAllDrinksPlansID() {
		return AllDrinksPlansID;
	}
	public void setAllDrinksPlansID(String allDrinksPlansID) {
		AllDrinksPlansID = allDrinksPlansID;
	}
	public String getClassicPlansID() {
		return ClassicPlansID;
	}
	public void setClassicPlansID(String classicPlansID) {
		ClassicPlansID = classicPlansID;
	}
	public String getPlanLogo() {
		return PlanLogo;
	}
	public void setPlanLogo(String planLogo) {
		PlanLogo = planLogo;
	}
	public String getDrinks() {
		return Cups;
	}
	public void setDrinks(String drinks) {
		Cups = drinks;
	}
	public String getCost() {
		return Cost;
	}
	public void setCost(String cost) {
		Cost = cost;
	}
	public String getExpiration() {
		return Expiration;
	}
	public void setExpiration(String expiration) {
		Expiration = expiration;
	}
	public String getIsMostPopular() {
		return IsMostPopular;
	}
	public void setIsMostPopular(String isMostPopular) {
		IsMostPopular = isMostPopular;
	}
	public String getIsIncludingTax() {
		return IsIncludingTax;
	}
	public void setIsIncludingTax(String isIncludingTax) {
		IsIncludingTax = isIncludingTax;
	}
	public String getPerCupCostInfo() {
		return PerCupCostInfo;
	}
	public void setPerCupCostInfo(String perCupCostInfo) {
		PerCupCostInfo = perCupCostInfo;
	}
	public String getPlanIncludesDetails() {
		return PlanIncludesDetails;
	}
	public void setPlanIncludesDetails(String planIncludesDetails) {
		PlanIncludesDetails = planIncludesDetails;
	}
	
    
    
}

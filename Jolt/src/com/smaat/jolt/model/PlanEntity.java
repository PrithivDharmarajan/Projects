package com.smaat.jolt.model;

public class PlanEntity {
	private String expiry_date;
	private String RefillON;
	private String PlanType;
	private String cupscount;
	private String CurrentPlan;
	private String IncludesInfo;
	
	

	public String getIncludesInfo() {
		return IncludesInfo;
	}

	public void setIncludesInfo(String includesInfo) {
		IncludesInfo = includesInfo;
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

	public String getPlanType() {
		return PlanType;
	}

	public void setPlanType(String planType) {
		PlanType = planType;
	}

	public String getCupscount() {
		return cupscount;
	}

	public void setCupscount(String cupscount) {
		this.cupscount = cupscount;
	}

	public String getCurrentPlan() {
		return CurrentPlan;
	}

	public void setCurrentPlan(String currentPlan) {
		CurrentPlan = currentPlan;
	}

}

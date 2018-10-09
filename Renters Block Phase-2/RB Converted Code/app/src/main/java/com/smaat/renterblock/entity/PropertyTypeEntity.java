package com.smaat.renterblock.entity;

import java.io.Serializable;

public class PropertyTypeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String Houseboat;
	private String Income_Investment;
	private String Multi_Family;
	private String Lot_land;
	private String Farm_Ranch;
	private String Mobile_Manufactured;
	private String Apartment_Condo_Townhouse;
	private String TIC;
	private String Loft;
	private String Apartment;
	private String Coop;
	private String Townhouse;
	private String Condo;
	private String Single_Family_Home;
	private String AllTypes;
	
	

	public String getAllTypes() {
		return AllTypes;
	}

	public void setAllTypes(String allTypes) {
		AllTypes = allTypes;
	}

	public String getHouseboat() {
		return Houseboat;
	}

	public void setHouseboat(String houseboat) {
		Houseboat = houseboat;
	}

	public String getIncome_Investment() {
		return Income_Investment;
	}

	public void setIncome_Investment(String income_Investment) {
		Income_Investment = income_Investment;
	}

	public String getMulti_Family() {
		return Multi_Family;
	}

	public void setMulti_Family(String multi_Family) {
		Multi_Family = multi_Family;
	}

	public String getLot_land() {
		return Lot_land;
	}

	public void setLot_land(String lot_land) {
		Lot_land = lot_land;
	}

	public String getFarm_Ranch() {
		return Farm_Ranch;
	}

	public void setFarm_Ranch(String farm_Ranch) {
		Farm_Ranch = farm_Ranch;
	}

	public String getMobile_Manufactured() {
		return Mobile_Manufactured;
	}

	public void setMobile_Manufactured(String mobile_Manufactured) {
		Mobile_Manufactured = mobile_Manufactured;
	}

	public String getApartment_Condo_Townhouse() {
		return Apartment_Condo_Townhouse;
	}

	public void setApartment_Condo_Townhouse(String apartment_Condo_Townhouse) {
		Apartment_Condo_Townhouse = apartment_Condo_Townhouse;
	}

	public String getTIC() {
		return TIC;
	}

	public void setTIC(String tIC) {
		TIC = tIC;
	}

	public String getLoft() {
		return Loft;
	}

	public void setLoft(String loft) {
		Loft = loft;
	}

	public String getApartment() {
		return Apartment;
	}

	public void setApartment(String apartment) {
		Apartment = apartment;
	}

	public String getCoop() {
		return Coop;
	}

	public void setCoop(String coop) {
		Coop = coop;
	}

	public String getTownhouse() {
		return Townhouse;
	}

	public void setTownhouse(String townhouse) {
		Townhouse = townhouse;
	}

	public String getCondo() {
		return Condo;
	}

	public void setCondo(String condo) {
		Condo = condo;
	}

	public String getSingle_Family_Home() {
		return Single_Family_Home;
	}

	public void setSingle_Family_Home(String single_Family_Home) {
		Single_Family_Home = single_Family_Home;
	}

}

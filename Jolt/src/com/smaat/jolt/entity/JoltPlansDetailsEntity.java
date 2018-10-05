package com.smaat.jolt.entity;

import java.util.ArrayList;

public class JoltPlansDetailsEntity {
	private String Classicdescription;
    private String AllDrinksdescription;
	private ArrayList<JoltPlansListEntity> ClassicPlansIncludesInfo;
	private ArrayList<JoltPlansListEntity> AllDrinksIncludesInfo;
	public String getClassicdescription() {
		return Classicdescription;
	}
	public void setClassicdescription(String classicdescription) {
		Classicdescription = classicdescription;
	}
	public String getAllDrinksdescription() {
		return AllDrinksdescription;
	}
	public void setAllDrinksdescription(String allDrinksdescription) {
		AllDrinksdescription = allDrinksdescription;
	}
	public ArrayList<JoltPlansListEntity> getClassicPlansIncludesInfo() {
		return ClassicPlansIncludesInfo;
	}
	public void setClassicPlansIncludesInfo(
			ArrayList<JoltPlansListEntity> classicPlansIncludesInfo) {
		ClassicPlansIncludesInfo = classicPlansIncludesInfo;
	}
	public ArrayList<JoltPlansListEntity> getAllDrinksIncludesInfo() {
		return AllDrinksIncludesInfo;
	}
	public void setAllDrinksIncludesInfo(
			ArrayList<JoltPlansListEntity> allDrinksIncludesInfo) {
		AllDrinksIncludesInfo = allDrinksIncludesInfo;
	}
	
	
    
    
}

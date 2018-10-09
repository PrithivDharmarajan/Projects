package com.smaat.renterblock.entity;

import java.io.Serializable;

public class FilterObjectEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SellFilterObjectEntity Sale;
	private RentFilterObjectEntity Rent;
	private SoldFilterObjectEntity Sold;
	
	public RentFilterObjectEntity getRent() {
		return Rent;
	}
	public void setRent(RentFilterObjectEntity rent) {
		Rent = rent;
	}
	public SoldFilterObjectEntity getSold() {
		return Sold;
	}
	public void setSold(SoldFilterObjectEntity sold) {
		Sold = sold;
	}
	public SellFilterObjectEntity getSale() {
		return Sale;
	}
	public void setSale(SellFilterObjectEntity sale) {
		Sale = sale;
	}
	
}

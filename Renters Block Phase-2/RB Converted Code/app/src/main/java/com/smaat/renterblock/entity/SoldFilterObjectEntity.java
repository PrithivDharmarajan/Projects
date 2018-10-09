package com.smaat.renterblock.entity;

import java.io.Serializable;

public class SoldFilterObjectEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String price_range_min;
	private String price_range_max;
	private String property_type;
	private String beds;
	private String baths;
	private String square_footage_min;
	private String square_footage_max;
	private String sold_within;
	private String lot_size;
	private String days_on_RB;
	private String keywords;
	private String no_fee;
	private String year_build_min;
	private String year_build_max;
	private String resale;
	private String new_construction;
	private String fore_closure;
	private String open_house;
	private String reduced_prices;
	private String MLS;
	private String filter_name;
	
	public String getFilter_name() {
		return filter_name;
	}
	public void setFilter_name(String filter_name) {
		this.filter_name = filter_name;
	}
	public String getNo_fee() {
		return no_fee;
	}
	public void setNo_fee(String no_fee) {
		this.no_fee = no_fee;
	}
	public String getYear_build_min() {
		return year_build_min;
	}
	public void setYear_build_min(String year_build_min) {
		this.year_build_min = year_build_min;
	}
	public String getYear_build_max() {
		return year_build_max;
	}
	public void setYear_build_max(String year_build_max) {
		this.year_build_max = year_build_max;
	}
	public String getResale() {
		return resale;
	}
	public void setResale(String resale) {
		this.resale = resale;
	}
	public String getNew_construction() {
		return new_construction;
	}
	public void setNew_construction(String new_construction) {
		this.new_construction = new_construction;
	}
	public String getFore_closure() {
		return fore_closure;
	}
	public void setFore_closure(String fore_closure) {
		this.fore_closure = fore_closure;
	}
	public String getOpen_house() {
		return open_house;
	}
	public void setOpen_house(String open_house) {
		this.open_house = open_house;
	}
	public String getReduced_prices() {
		return reduced_prices;
	}
	public void setReduced_prices(String reduced_prices) {
		this.reduced_prices = reduced_prices;
	}
	public String getMLS() {
		return MLS;
	}
	public void setMLS(String mLS) {
		MLS = mLS;
	}
	public String getPrice_range_min() {
		return price_range_min;
	}
	public void setPrice_range_min(String price_range_min) {
		this.price_range_min = price_range_min;
	}
	public String getPrice_range_max() {
		return price_range_max;
	}
	public void setPrice_range_max(String price_range_max) {
		this.price_range_max = price_range_max;
	}
	public String getProperty_type() {
		return property_type;
	}
	public void setProperty_type(String property_type) {
		this.property_type = property_type;
	}
	public String getBeds() {
		return beds;
	}
	public void setBeds(String beds) {
		this.beds = beds;
	}
	public String getBaths() {
		return baths;
	}
	public void setBaths(String baths) {
		this.baths = baths;
	}
	public String getSquare_footage_min() {
		return square_footage_min;
	}
	public void setSquare_footage_min(String square_footage_min) {
		this.square_footage_min = square_footage_min;
	}
	public String getSquare_footage_max() {
		return square_footage_max;
	}
	public void setSquare_footage_max(String square_footage_max) {
		this.square_footage_max = square_footage_max;
	}
	public String getSold_within() {
		return sold_within;
	}
	public void setSold_within(String sold_within) {
		this.sold_within = sold_within;
	}
	public String getLot_size() {
		return lot_size;
	}
	public void setLot_size(String lot_size) {
		this.lot_size = lot_size;
	}
	public String getDays_on_RB() {
		return days_on_RB;
	}
	public void setDays_on_RB(String days_on_RB) {
		this.days_on_RB = days_on_RB;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
}

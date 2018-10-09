package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class PropertyEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pro_id;
	private String property_id;
	private String user_id;
	private String type;
	private String price_range;
	private String property_type;
	private String beds;
	private String baths;
	private String square_footage;
	private String build_year;
	private String address;
	private String latitude;
	private String longitude;
	private String description;
	private String lot_size;
	private String fee;
	private String resale;
	private String new_construction;
	private String foreclosure;
	private String open_house;
	private String reduced_prices;
	private String rb_block;
	private String property_rating;
	private String rating;
	private String review_count;
	private String isfavourite;
	private String property_name;
	private String ismymessageboard;
	private String property_datetime;
	private String MLS;
	private String description1;
	private String description2;
	private String description3;
	private String description4;
	private String description5;
	private ArrayList<PostPropertyUserDetails> property_posted_user_details;
	private ArrayList<PropertyPictures> property_pics;
	private ArrayList<PropertyPictures> property_video;
	private ArrayList<PropertyReview> property_review;
	private ArrayList<UserPropertyPics> userpropertypic;
	private ArrayList<BoardsDetails> my_message_board;



	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getDescription3() {
		return description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}

	public String getDescription4() {
		return description4;
	}

	public void setDescription4(String description4) {
		this.description4 = description4;
	}

	public String getDescription5() {
		return description5;
	}

	public void setDescription5(String description5) {
		this.description5 = description5;
	}

	public String getMLS() {
		return MLS;
	}

	public void setMLS(String mLS) {
		MLS = mLS;
	}



	public ArrayList<PropertyPictures> getProperty_video() {
		return property_video;
	}

	public void setProperty_video(ArrayList<PropertyPictures> property_video) {
		this.property_video = property_video;
	}

	public String getProperty_datetime() {
		return property_datetime;
	}

	public void setProperty_datetime(String property_datetime) {
		this.property_datetime = property_datetime;
	}

	public String getProperty_rating() {
		return property_rating;
	}

	public void setProperty_rating(String property_rating) {
		this.property_rating = property_rating;
	}

	public ArrayList<BoardsDetails> getMy_message_board() {
		return my_message_board;
	}

	public void setMy_message_board(ArrayList<BoardsDetails> my_message_board) {
		this.my_message_board = my_message_board;
	}

	public String getIsmymessageboard() {
		return ismymessageboard;
	}

	public void setIsmymessageboard(String ismymessageboard) {
		this.ismymessageboard = ismymessageboard;
	}

	public String getProperty_name() {
		return property_name;
	}

	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}

	public ArrayList<UserPropertyPics> getUserpropertypic() {
		return userpropertypic;
	}

	public void setUserpropertypic(ArrayList<UserPropertyPics> userpropertypic) {
		this.userpropertypic = userpropertypic;
	}

	public String getPro_id() {
		return pro_id;
	}

	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice_range() {
		return price_range;
	}

	public void setPrice_range(String price_range) {
		this.price_range = price_range;
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

	public String getSquare_footage() {
		return square_footage;
	}

	public void setSquare_footage(String square_footage) {
		this.square_footage = square_footage;
	}

	public String getBuild_year() {
		return build_year;
	}

	public void setBuild_year(String build_year) {
		this.build_year = build_year;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLot_size() {
		return lot_size;
	}

	public void setLot_size(String lot_size) {
		this.lot_size = lot_size;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
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

	public String getForeclosure() {
		return foreclosure;
	}

	public void setForeclosure(String foreclosure) {
		this.foreclosure = foreclosure;
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

	public String getRb_block() {
		return rb_block;
	}

	public void setRb_block(String rb_block) {
		this.rb_block = rb_block;
	}

	public ArrayList<PostPropertyUserDetails> getProperty_posted_user_details() {
		return property_posted_user_details;
	}

	public void setProperty_posted_user_details(
			ArrayList<PostPropertyUserDetails> property_posted_user_details) {
		this.property_posted_user_details = property_posted_user_details;
	}

	public ArrayList<PropertyPictures> getProperty_pics() {
		return property_pics;
	}

	public void setProperty_pics(ArrayList<PropertyPictures> property_pics) {
		this.property_pics = property_pics;
	}

	public ArrayList<PropertyReview> getProperty_review() {
		return property_review;
	}

	public void setProperty_review(ArrayList<PropertyReview> property_review) {
		this.property_review = property_review;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReview_count() {
		return review_count;
	}

	public void setReview_count(String review_count) {
		this.review_count = review_count;
	}

	public String getIsfavourite() {
		return isfavourite;
	}

	public void setIsfavourite(String isfavourite) {
		this.isfavourite = isfavourite;
	}

	public static Comparator<PropertyEntity> SQUARE_FEET_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			String max_price1 = arg0.getSquare_footage();
			String max_price2 = arg1.getSquare_footage();

			return max_price2.compareTo(max_price1);
		}

	};
	public static Comparator<PropertyEntity> RATING_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			String rating1 = arg0.getProperty_rating();
			String rating2 = arg1.getProperty_rating();

			return rating2.compareTo(rating1);
		}

	};
	public static Comparator<PropertyEntity> PRICE_MIN_TO_MAX_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			String max_price1 = arg0.getPrice_range();
			String max_price2 = arg1.getPrice_range();

			return max_price1.compareTo(max_price2);
		}

	};

	public static Comparator<PropertyEntity> PRICE_MAX_TO_MIN_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			String max_price1 = arg0.getPrice_range();
			String max_price2 = arg1.getPrice_range();

			return max_price2.compareTo(max_price1);
		}

	};

	public static Comparator<PropertyEntity> BED_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			String beds1 = arg0.getBeds();
			String beds2 = arg1.getBeds();

			return beds2.compareTo(beds1);
		}

	};

	public static Comparator<PropertyEntity> BATH_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			String baths1 = arg0.getBaths();
			String baths2 = arg1.getBaths();

			return baths2.compareTo(baths1);
		}

	};

	public static Comparator<PropertyEntity> DATE_SORT = new Comparator<PropertyEntity>() {

		@Override
		public int compare(PropertyEntity arg0, PropertyEntity arg1) {
			Date date1 = getDate(arg0.getProperty_datetime());
			Date date2 = getDate(arg1.getProperty_datetime());

			return date2.compareTo(date1);
		}

	};

	private static Date getDate(String dateString) {
		SimpleDateFormat df;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Date date = null;
		try {

			date = df.parse(dateString);
			return date;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new Date();
	}

}

package com.smaat.renterblock.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RentersBlockDatabase extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "rentersBlock.db";
	public static final String saved_search = "local_saved_search";
	public static final String sell_property_type = "local_sell_property";
	public static final String rent_property_type = "local_rent_property";
	public static final String sold_property_type = "local_sold_property";

	public RentersBlockDatabase(Context context) {
		super(context, DATABASE_NAME, null, 3);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ saved_search
				+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, location TEXT, property_type TEXT, latitude TEXT,"
				+ "Longitude TEXT)");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ sell_property_type
				+ "(user_id INTEGER,price_range_min TEXT, price_range_max TEXT,property_type TEXT,beds TEXT,baths TEXT,"
				+ "square_footage_min TEXT,square_footage_max TEXT,keywords TEXT,no_fee TEXT,year_build_min TEXT,year_build_max TEXT,"
				+ " lot_size TEXT,days_on_RB TEXT,resale TEXT,new_construction TEXT,fore_closure TEXT,open_house TEXT,"
				+ "TEXT,reduced_prices TEXT, MLS TEXT, sold_within TEXT, PRIMARY KEY (user_id))");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ rent_property_type
				+ "(user_id INTEGER,price_range_min TEXT, price_range_max TEXT,property_type TEXT,beds TEXT,baths TEXT,"
				+ "square_footage_min TEXT,square_footage_max TEXT,keywords TEXT,no_fee TEXT,year_build_min TEXT,year_build_max TEXT,"
				+ " lot_size TEXT,days_on_RB TEXT,resale TEXT,new_construction TEXT,fore_closure TEXT,open_house TEXT,"
				+ "TEXT,reduced_prices TEXT, MLS TEXT, sold_within TEXT, PRIMARY KEY (user_id))");

		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ sold_property_type
				+ "(user_id INTEGER,price_range_min TEXT, price_range_max TEXT,property_type TEXT,beds TEXT,baths TEXT,"
				+ "square_footage_min TEXT,square_footage_max TEXT,keywords TEXT,no_fee TEXT,year_build_min TEXT,year_build_max TEXT,"
				+ " lot_size TEXT,days_on_RB TEXT,resale TEXT,new_construction TEXT,fore_closure TEXT,open_house TEXT,"
				+ "TEXT,reduced_prices TEXT, MLS TEXT, sold_within TEXT, PRIMARY KEY (user_id))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

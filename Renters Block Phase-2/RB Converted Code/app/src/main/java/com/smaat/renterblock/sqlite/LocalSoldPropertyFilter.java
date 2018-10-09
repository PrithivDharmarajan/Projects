package com.smaat.renterblock.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smaat.renterblock.entity.SoldFilterObjectEntity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;

public class LocalSoldPropertyFilter {
	public static SoldFilterObjectEntity soldFilter(Context context) {

		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();

		SoldFilterObjectEntity category = new SoldFilterObjectEntity();

		String UserID = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_userId);

		String query = "SELECT * FROM "
				+ RentersBlockDatabase.sold_property_type + " WHERE user_id="
				+ UserID;

		try {

			Cursor cursor = db.rawQuery(query, null);
			if (cursor != null) {

				if (cursor.getCount() > 0) {
					cursor.moveToFirst();

					do {

						category.setPrice_range_min(cursor.getString(cursor
								.getColumnIndex("price_range_min")));
						category.setPrice_range_max(cursor.getString(cursor
								.getColumnIndex("price_range_max")));
						category.setProperty_type(cursor.getString(cursor
								.getColumnIndex("property_type")));
						category.setBeds(cursor.getString(cursor
								.getColumnIndex("beds")));
						category.setBaths(cursor.getString(cursor
								.getColumnIndex("baths")));
						category.setSquare_footage_min(cursor.getString(cursor
								.getColumnIndex("square_footage_min")));
						category.setSquare_footage_max(cursor.getString(cursor
								.getColumnIndex("square_footage_max")));
						category.setSold_within(cursor.getString(cursor
								.getColumnIndex("sold_within")));
						category.setLot_size(cursor.getString(cursor
								.getColumnIndex("lot_size")));
						category.setDays_on_RB(cursor.getString(cursor
								.getColumnIndex("days_on_RB")));
						category.setKeywords(cursor.getString(cursor
								.getColumnIndex("keywords")));
						category.setNo_fee(cursor.getString(cursor
								.getColumnIndex("no_fee")));
						category.setYear_build_min(cursor.getString(cursor
								.getColumnIndex("year_build_min")));
						category.setYear_build_max(cursor.getString(cursor
								.getColumnIndex("year_build_max")));
						category.setNew_construction(cursor.getString(cursor
								.getColumnIndex("new_construction")));
						category.setFore_closure(cursor.getString(cursor
								.getColumnIndex("fore_closure")));
						category.setResale(cursor.getString(cursor
								.getColumnIndex("resale")));
						category.setOpen_house(cursor.getString(cursor
								.getColumnIndex("open_house")));
						category.setReduced_prices(cursor.getString(cursor
								.getColumnIndex("reduced_prices")));
						category.setMLS(cursor.getString(cursor
								.getColumnIndex("MLS")));

					} while (cursor.moveToNext());

				}
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		DatabaseManager.getInstance().closeDatabase();

		return category;

	}

}

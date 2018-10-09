package com.smaat.renterblock.sqlite;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smaat.renterblock.savedsearch.LocalSavedSearchEntity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;

public class LocalSavedSearch {

	public static ArrayList<LocalSavedSearchEntity> saveSearch(Context context) {

		SQLiteDatabase db;
		db = DatabaseManager.getInstance().openDatabase();

		ArrayList<LocalSavedSearchEntity> category = new ArrayList<LocalSavedSearchEntity>();

		String UserID = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_userId);

		String query = "SELECT * FROM " + RentersBlockDatabase.saved_search
				+ " WHERE user_id=" + UserID;

		try {

			Cursor cursor = db.rawQuery(query, null);
			if (cursor != null) {

				if (cursor.getCount() > 0) {
					cursor.moveToFirst();

					do {

						LocalSavedSearchEntity saved_Se = new LocalSavedSearchEntity();

						if (!AppConstants.isAdded) {
							saved_Se.setLocation("");
							saved_Se.setProperty_type("");
							saved_Se.setUser_id("");
							saved_Se.setLatitude("");
							saved_Se.setLongitude("");
							category.add(saved_Se);
							AppConstants.isAdded = true;
						}

						saved_Se.setLocation(cursor.getString(cursor
								.getColumnIndex("location")));
						saved_Se.setProperty_type(cursor.getString(cursor
								.getColumnIndex("property_type")));
						saved_Se.setUser_id(cursor.getString(cursor
								.getColumnIndex("user_id")));
						saved_Se.setLatitude(cursor.getString(cursor
								.getColumnIndex("latitude")));
						saved_Se.setLongitude(cursor.getString(cursor
								.getColumnIndex("Longitude")));

						category.add(saved_Se);

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

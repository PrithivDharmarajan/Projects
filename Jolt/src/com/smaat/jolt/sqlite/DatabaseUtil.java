package com.smaat.jolt.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DatabaseUtil {

	private SQLiteDatabase db;

	public void open() throws SQLiteException {
		db = DatabaseManager.getInstance().openDatabase();
	}

	public void close() {
		DatabaseManager.getInstance().closeDatabase();
	}

	public void makeFavorite(String shopId) {
		try {
			open();
			ContentValues cv = new ContentValues();

			cv.put("shopId", shopId);

			try {
				db.insert(DataBaseHelper.FAVORITE_TABLE, null, cv);
			} catch (Exception e) {
				Log.e("Jolt", e.getLocalizedMessage());
			}

		} catch (Exception e) {
			Log.e("Jolt", e.getLocalizedMessage());
		} finally {
			close();
		}
	}

	public void makeUnfavorite(String shopId) {
		try {

			open();
			try {

				db.delete(DataBaseHelper.FAVORITE_TABLE, "shopId =?",
						new String[] { shopId });

			} catch (Exception e) {
				Log.e("Jolt", e.getLocalizedMessage());
			}

		} catch (Exception e) {
			Log.e("Jolt", e.getLocalizedMessage());
		} finally {
			close();
		}
	}

	public boolean isFavorite(String shopId) {

		boolean isfavorite = false;
		Cursor cursor = null;
		try {

			open();
			String query = "select * from " + DataBaseHelper.FAVORITE_TABLE
					+ " where shopId ='" + shopId + "'";

			cursor = db.rawQuery(query, null);

			if (cursor != null) {

				if (cursor.getCount() > 0) {
					isfavorite = true;
				} else {
					isfavorite = false;
				}
			}
		} catch (Exception e) {
			Log.e("Arete", e.getLocalizedMessage());
		} finally {
			if (cursor != null)
				cursor.close();

			close();
		}

		return isfavorite;
	}

}

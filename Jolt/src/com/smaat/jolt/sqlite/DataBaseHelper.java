package com.smaat.jolt.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "jolt_smaat.db";
	public SQLiteDatabase myDataBase;
	public static final String FAVORITE_TABLE = "favoriteTable";

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITE_TABLE
				+ "(shopId text,PRIMARY KEY(shopId))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

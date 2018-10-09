package com.smaat.ipharma.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ipharma.sqlite";

    public static final String TABLE_PILL_REMINDER = "PILL_REMINDER";
    public static final String PILL_REMINDER_ID = "PILL_REMINDER_ID";
    public static final String TABLET_NAME = "TABLET_NAME";
    public static final String DURATION_TYPE = "DURATION_TYPE";
    public static final String DURATION_TIME = "DURATION_TIME";
    public static final String PILL_TIMING = "PILL_TIMING";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PILL_REMINDER + "(" + PILL_REMINDER_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1,"
                + TABLET_NAME + " TEXT NOT NULL," + DURATION_TYPE + " TEXT NOT NULL,"
                + DURATION_TIME + " TEXT NOT NULL," + PILL_TIMING + " TEXT NOT NULL" + ")");
        Log.d("msg","table created successfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

package com.smaat.ipharma.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.smaat.ipharma.R;
import com.smaat.ipharma.db.model.PillTimerResponse;
import com.smaat.ipharma.util.DialogManager;

import java.util.ArrayList;

public class DatabaseUtil {

    private SQLiteDatabase db;
    DatabaseHelper dbhelper = null;


    public void open() throws SQLiteException {
        db = DatabaseManager.getInstance().openDatabase();
    }

    public void close() {
        DatabaseManager.getInstance().closeDatabase();
    }


//
//
//    public void open() throws SQLiteException {
//        // dbhelper = new DatabaseHelper(context);
//        // db = dbhelper.getWritableDatabase();
//        db = DatabaseManager.getInstance().openDatabase();
//    }
//
//    @SuppressWarnings("unused")
//    private Context context;
//
//    public DatabaseUtil(Context context) {
//        dbhelper = new DatabaseHelper(context);
//        this.context = context;
//    }
//
//    public void close() {
//        DatabaseManager.getInstance().closeDatabase();
//    }

//    public int myProfileInsert(Context context, String phone, String phar_name,
//                               String address, String ownernme, String email, String website,
//                               String opentime, String closetime, String deliverytime,
//                               String minipur, String isNew, double latitude, double longitude,
//                               byte[] photo1, byte[] photo2, byte[] photo3, byte[] photo4,
//                               String date, String isdelivery) {
//        open();
//        int count = 0;
//        try {
//
//            ContentValues values = new ContentValues();
//            values.put(DatabaseHelper.KEY_PHONE, phone);
//            values.put(DatabaseHelper.KEY_OWNER, ownernme);
//            values.put(DatabaseHelper.KEY_ADDRESS, address);
//            values.put(DatabaseHelper.KEY_EMAIL, email);
//            values.put(DatabaseHelper.KEY_PHARMA_NAME, phar_name);
//            values.put(DatabaseHelper.KEY_WEBSITE, website);
//            values.put(DatabaseHelper.KEY_OPEN_TIME, opentime);
//            values.put(DatabaseHelper.KEY_CLOSE_TIME, closetime);
//            values.put(DatabaseHelper.KEY_DELIVER_TIME, deliverytime);
//            values.put(DatabaseHelper.KEY_MIN_PURCHASE, minipur);
//            values.put(DatabaseHelper.KEY_IS_NEW, isNew);
//            values.put(DatabaseHelper.KEY_LAT, latitude);
//            values.put(DatabaseHelper.KEY_LONG, longitude);
//            values.put(DatabaseHelper.KEY_PHOTO1, photo1);
//            values.put(DatabaseHelper.KEY_PHOTO2, photo2);
//            values.put(DatabaseHelper.KEY_PHOTO3, photo3);
//            values.put(DatabaseHelper.KEY_PHOTO4, photo4);
//            values.put(DatabaseHelper.KEY_DATE, date);
//            values.put(DatabaseHelper.KEY_IS_DELIVERY, isdelivery);
//            try {
//                count += ((db.insertWithOnConflict(
//                        DatabaseHelper.TABLE_MYPROFILE, null, values,
//                        SQLiteDatabase.CONFLICT_IGNORE)) == -1) ? 0 : 1;
//
//                if (count > 0) {
//                    DialogManager.showMessageDialog(context,
//                            context.getString(R.string.saved_successfully), context.getString(R.string.ok));
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close();
//        }
//
//        return count;
//
//    }

    public int pillReminderInsert(Context context, PillTimerResponse pillTimeRes) {
        open();
        int count = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.TABLET_NAME, pillTimeRes.getTablet_name());
            values.put(DatabaseHelper.DURATION_TYPE, pillTimeRes.getDuration_type());
            values.put(DatabaseHelper.DURATION_TIME, pillTimeRes.getDuration_time());
            values.put(DatabaseHelper.PILL_TIMING, pillTimeRes.getPill_timing());

            try {
                count += ((db.insertWithOnConflict(DatabaseHelper.TABLE_PILL_REMINDER, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE)) == -1) ? 0 : 1;

                if (count > 0) {
                    DialogManager.showMessageDialog(context,
                            context.getString(R.string.saved_successfully), context.getString(R.string.ok));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return count;
    }

    public ArrayList<PillTimerResponse> getPillReminderRes() {
        System.out.println("Pill DB----------");
        ArrayList<PillTimerResponse> pillTimerRes = new ArrayList<>();
        open();
        String query;
        query = "SELECT * FROM " + DatabaseHelper.TABLE_PILL_REMINDER;
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        PillTimerResponse pillTimer = new PillTimerResponse();

                        pillTimer.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.PILL_REMINDER_ID)));
                        pillTimer.setTablet_name(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                                .TABLET_NAME)));
                        pillTimer.setDuration_type(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                                .DURATION_TYPE)));
                        pillTimer.setDuration_time(cursor.getString(cursor.getColumnIndex
                                (DatabaseHelper.DURATION_TIME)));
                        pillTimer.setPill_timing(cursor.getString(cursor.getColumnIndex(DatabaseHelper
                                .PILL_TIMING)));
//                        pillTimer.setPill_timing(cursor.getString(cursor.getColumnIndex(DatabaseHelper
//                                .DURATION_TIME)));

                        pillTimerRes.add(pillTimer);

                    } while (cursor.moveToNext());
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            close();
        }


        return pillTimerRes;
    }

    public void updatePillReminderData(Context context, PillTimerResponse data) {

        open();
        int count = 0;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.TABLET_NAME, data.getTablet_name());
            contentValues.put(DatabaseHelper.DURATION_TYPE, data.getDuration_type());
            contentValues.put(DatabaseHelper.DURATION_TIME, data.getDuration_time());
            contentValues.put(DatabaseHelper.PILL_TIMING, data.getPill_timing());

            try {

                count += ((db.updateWithOnConflict(DatabaseHelper.TABLE_PILL_REMINDER,
                        contentValues,
                        DatabaseHelper.PILL_REMINDER_ID + " = ?", new String[]{String.valueOf(data
                                .getId())}, SQLiteDatabase.CONFLICT_IGNORE) == -1) ? 0 : 1);


                if (count > 0) {
                    DialogManager.showMessageDialog(context,
                            context.getString(R.string.update_successfully), context.getString(R.string.ok));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }


    }

    public PillTimerResponse getPillReminderData(Context context, int reminderID) {

        open();

        Cursor cursor = db.query(DatabaseHelper.TABLE_PILL_REMINDER, new String[]{DatabaseHelper.PILL_REMINDER_ID,
                        DatabaseHelper.TABLET_NAME, DatabaseHelper.DURATION_TYPE, DatabaseHelper.DURATION_TIME, DatabaseHelper.PILL_TIMING},
                DatabaseHelper.PILL_REMINDER_ID + " = ?",
                new String[]{String.valueOf(reminderID)}, null, null, null);


        PillTimerResponse mRes = new PillTimerResponse();
        try {
            if (cursor != null)
                cursor.moveToFirst();
            mRes.setId(Integer.parseInt(cursor.getString(0)));
            mRes.setTablet_name(cursor.getString(1));
            mRes.setDuration_type(cursor.getString(2));
            mRes.setDuration_time(cursor.getString(3));
            mRes.setPill_timing(cursor.getString(4));

// return shop
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return mRes;
    }

    public void deletePillReminderData(Context context, int reminderID) {

        open();
        int count = 0;
        try {

            count += (db.delete(DatabaseHelper.TABLE_PILL_REMINDER, DatabaseHelper.PILL_REMINDER_ID +
                    " = ?", new String[]{String.valueOf(reminderID)}) == -1) ? 0 : 1;

            if (count > 0) {
                DialogManager.showMessageDialog(context,
                        context.getString(R.string.delete_successfully), context.getString(R.string.ok));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

    }

    public void deleteDB(Context context) {
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }

    public void deleteTable(Context context) {
        db.delete(DatabaseHelper.TABLE_PILL_REMINDER, null, null);
    }


//	public ArrayList<UserDetails> getProfile() {
//		ArrayList<UserDetails> userList = new ArrayList<UserDetails>();
//		open();
//		String query;
//
//		query = "select * from " + DatabaseHelper.TABLE_MYPROFILE + " where "
//				+ DatabaseHelper.KEY_IS_NEW + "='" + "0" + "'";
//		Cursor cursor = db.rawQuery(query, null);
//		try {
//			if (cursor != null) {
//				if (cursor.getCount() > 0) {
//					cursor.moveToFirst();
//					do {
//						UserDetails user = new UserDetails();
//
//						user.setPhone_number(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHONE)));
//
//						user.setPharmacy_name(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHARMA_NAME)));
//
//						user.setAddress(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_ADDRESS)));
//
//						user.setOwner_name(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_OWNER)));
//
//						user.setEmail_id(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_EMAIL)));
//						user.setWebsite(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_WEBSITE)));
//						user.setOpening_time(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_OPEN_TIME)));
//						user.setClosing_time(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_CLOSE_TIME)));
//						user.setDelivery_time(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_DELIVER_TIME)));
//						user.setMin_purchase(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_MIN_PURCHASE)));
//						user.setHome_delivery(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_IS_DELIVERY)));
//						user.setDatetime(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_DATE)));
//						user.setLatitude(cursor.getDouble(cursor
//								.getColumnIndex(DatabaseHelper.KEY_LAT)));
//						user.setLongitude(cursor.getDouble(cursor
//								.getColumnIndex(DatabaseHelper.KEY_LONG)));
//						user.setIsNew(cursor.getInt(cursor
//								.getColumnIndex(DatabaseHelper.KEY_IS_NEW)));
//						user.setRowid(cursor.getInt(cursor
//								.getColumnIndex(DatabaseHelper.KEY_ROW_ID)));
//
//						Image image = new Image();
//						image.setImage1(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO1)));
//
//						image.setImage2(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO2)));
//						image.setImage3(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO3)));
//
//						image.setImage4(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO4)));
//						user.setImage(image);
//
//
//						userList.add(user);
//					} while (cursor.moveToNext());
//				}
//			}
//		} catch (Exception e) {
//			Log.e("IPharma", e.getLocalizedMessage());
//			e.printStackTrace();
//		} finally {
//			if (cursor != null)
//				cursor.close();
//			close();
//		}
//		return userList;
//	}

//	public void UpdateTable(int i) {
//		try {
//
//			ContentValues values = new ContentValues();
//
//			values.put(DatabaseHelper.KEY_IS_NEW, "1");
//
//			open();
//
//			db.update(DatabaseHelper.TABLE_MYPROFILE, values,
//					DatabaseHelper.KEY_IS_NEW + " = ?" + "AND "
//							+ DatabaseHelper.KEY_ROW_ID + " = ?", new String[] {
//							"0", String.valueOf(i) });
//
//		} catch (Exception e) {
//			Log.e("IPharma", e.getLocalizedMessage());
//		}
//
//		finally {
//			close();
//		}
//	}
//
//	public void deleteUser() {
//		try {
//
//			open();
//
//			db.delete(DatabaseHelper.TABLE_MYPROFILE, DatabaseHelper.KEY_IS_NEW
//					+ " = ?", new String[] { "1" });
//		} catch (Exception e) {
//			Log.e("IPharma", e.getLocalizedMessage());
//		}
//
//		finally {
//			close();
//		}
//	}
//
//	public ArrayList<UserDetails> getPushProfile() {
//		ArrayList<UserDetails> userList = new ArrayList<UserDetails>();
//		open();
//		String query;
//
//		query = "select * from " + DatabaseHelper.TABLE_MYPROFILE + " where "
//				+ DatabaseHelper.KEY_IS_NEW + "='" + "1" + "'";
//		Cursor cursor = db.rawQuery(query, null);
//		try {
//			if (cursor != null) {
//				if (cursor.getCount() > 0) {
//					cursor.moveToFirst();
//					do {
//						UserDetails user = new UserDetails();
//
//						user.setPhone_number(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHONE)));
//
//						user.setPharmacy_name(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHARMA_NAME)));
//
//						user.setAddress(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_ADDRESS)));
//
//						user.setOwner_name(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_OWNER)));
//
//						user.setEmail_id(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_EMAIL)));
//						user.setWebsite(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_WEBSITE)));
//						user.setOpening_time(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_OPEN_TIME)));
//						user.setClosing_time(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_CLOSE_TIME)));
//						user.setDelivery_time(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_DELIVER_TIME)));
//						user.setMin_purchase(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_MIN_PURCHASE)));
//						user.setHome_delivery(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_IS_NEW)));
//						user.setDatetime(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_DATE)));
//						user.setLatitude(cursor.getDouble(cursor
//								.getColumnIndex(DatabaseHelper.KEY_LAT)));
//						user.setLongitude(cursor.getDouble(cursor
//								.getColumnIndex(DatabaseHelper.KEY_LONG)));
//						user.setIsDelivery(cursor.getString(cursor
//								.getColumnIndex(DatabaseHelper.KEY_IS_DELIVERY)));
//						user.setRowid(cursor.getInt(cursor
//								.getColumnIndex(DatabaseHelper.KEY_ROW_ID)));
//
//						Image image = new Image();
//						image.setImage1(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO1)));
//
//						image.setImage2(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO2)));
//						image.setImage3(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO3)));
//
//						image.setImage4(cursor.getBlob(cursor
//								.getColumnIndex(DatabaseHelper.KEY_PHOTO4)));
//						user.setImage(image);
//
//
//						userList.add(user);
//					} while (cursor.moveToNext());
//				}
//			}
//		} catch (Exception e) {
//			Log.e("IPharma", e.getLocalizedMessage());
//			e.printStackTrace();
//		} finally {
//			if (cursor != null)
//				cursor.close();
//			close();
//		}
//		return userList;
//	}
}

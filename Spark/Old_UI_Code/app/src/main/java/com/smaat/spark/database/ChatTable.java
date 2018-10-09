package com.smaat.spark.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smaat.spark.entity.outputEntity.ChatReceiveEntity;

import java.util.ArrayList;


public class ChatTable {

    private static final String TableChat = "TableChat";
    private static final String TableChatMaxID = "TableChatMaxID";
    private static final String TableChatDelete = "TableChatDelete";
    private static final String KeyChatID = "KeyChatID";
    private static final String KeyUserID = "UserID";
    private static final String KeyFriendID = "FriendID";
    private static final String KeySubject = "Subject";
    private static final String KeyMessage = "KeyMessage";
    private static final String KeyUserName = "UserName";
    private static final String KeyFriendName = "FriendName";
    private static final String KeyUserImg = "UserImg";
    private static final String KeyFriendImg = "FriendImg";
    private static final String KeySentMsgUser = "SentMsgUser";
    private static final String KeyDate = "Datetime";


    private static final String CREATE_ACTIVITY_TABLE = "CREATE TABLE IF NOT EXISTS " + TableChat + "" +
            "(" + KeyUserID + " TEXT," +
            KeyFriendID + " TEXT," +
            KeySubject + " TEXT," + KeyChatID + " TEXT," +
            KeyMessage + " TEXT," + KeyUserName + " TEXT," +
            KeyFriendName + " TEXT," + KeyUserImg + " TEXT," +
            KeyFriendImg + " TEXT," + KeySentMsgUser + " TEXT," + KeyDate + " TEXT " +
            ")";

    private static final String CHAT_MAX_ID_TABLE = "CREATE TABLE IF NOT EXISTS " + TableChatMaxID + "" +
            "(" + KeyUserID + " TEXT," +
            KeyFriendID + " TEXT," +
            KeySubject + " TEXT," + KeyChatID + " TEXT," +
            KeyMessage + " TEXT" +
            ")";

    private static final String CHAT_DELETE_TABLE = "CREATE TABLE IF NOT EXISTS " + TableChatDelete + "" +
            "(" + KeyUserID + " TEXT," +
            KeyFriendID + " TEXT," +
            KeySubject + " TEXT," + KeyChatID + " TEXT," +
            KeyMessage + " TEXT" +
            ")";

    static void createActivityTable(SQLiteDatabase db) {
        db.execSQL(CREATE_ACTIVITY_TABLE);
        db.execSQL(CHAT_MAX_ID_TABLE);
        db.execSQL(CHAT_DELETE_TABLE);
    }

    static void dropActivityTable() {
        DatabaseHelper.db.execSQL("DROP TABLE IF EXISTS" + TableChat);
        DatabaseHelper.db.execSQL("DROP TABLE IF EXISTS" + TableChatMaxID);
        DatabaseHelper.db.execSQL("DROP TABLE IF EXISTS" + TableChatDelete);
    }

    public static void deleteAll(String tableNameStr) {
        DatabaseHelper.db.delete(tableNameStr, null, null);
    }

    public static void updateChatTable(ChatReceiveEntity chatModel) {

        ContentValues contentValues;

        contentValues = new ContentValues();
        contentValues.put(KeyUserID, chatModel.getUser_id());
        contentValues.put(KeyFriendID, chatModel.getFriend_id());
        contentValues.put(KeySubject, chatModel.getSubject());
        contentValues.put(KeyChatID, chatModel.getChat_id());
        contentValues.put(KeyMessage, chatModel.getMessage());
        contentValues.put(KeyUserName, chatModel.getUsername());
        contentValues.put(KeyFriendName, chatModel.getFriendname());
        contentValues.put(KeyUserImg, chatModel.getUserimage());
        contentValues.put(KeyFriendImg, chatModel.getFriendimage());
        contentValues.put(KeySentMsgUser, chatModel.getMsg_sent_user());
        contentValues.put(KeyDate, chatModel.getDatetime());

        DatabaseHelper.db.insert(TableChat, null, contentValues);
    }

    public static ArrayList<ChatReceiveEntity> getChatMessageList(ChatReceiveEntity model) {
        ArrayList<ChatReceiveEntity> totalMsgList = new ArrayList<>();


        String[] args = new String[]{model.getUser_id(), model.getFriend_id(), model.getSubject()};
        String selectQuery = "SELECT * FROM " + TableChat + " WHERE " + KeyUserID + "=? AND " + KeyFriendID + "=? AND " + KeySubject + "=?";
        Cursor cursor = DatabaseHelper.db.rawQuery(selectQuery, args);
        if (cursor.moveToFirst()) {
            do {
                ChatReceiveEntity msgList = new ChatReceiveEntity();
                msgList.setChat_id(cursor.getString(cursor.getColumnIndex(KeyChatID)));
                msgList.setUser_id(cursor.getString(cursor.getColumnIndex(KeyUserID)));
                msgList.setFriend_id(cursor.getString(cursor.getColumnIndex(KeyFriendID)));
                msgList.setSubject(cursor.getString(cursor.getColumnIndex(KeySubject)));
                msgList.setChat_id(cursor.getString(cursor.getColumnIndex(KeyChatID)));
                msgList.setMessage(cursor.getString(cursor.getColumnIndex(KeyMessage)));
                msgList.setUsername(cursor.getString(cursor.getColumnIndex(KeyUserName)));
                msgList.setFriendname(cursor.getString(cursor.getColumnIndex(KeyFriendName)));
                msgList.setUserimage(cursor.getString(cursor.getColumnIndex(KeyUserImg)));
                msgList.setFriendimage(cursor.getString(cursor.getColumnIndex(KeyFriendImg)));
                msgList.setMsg_sent_user(cursor.getString(cursor.getColumnIndex(KeySentMsgUser)));
                msgList.setDatetime(cursor.getString(cursor.getColumnIndex(KeyDate)));
                totalMsgList.add(msgList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return totalMsgList;
    }


    public static void updateMaxChatIDTable(ChatReceiveEntity chatModel) {

        if (!chatModel.getChat_id().isEmpty() && !chatModel.getChat_id().equals("-1")) {
            ContentValues contentValues;
            contentValues = new ContentValues();

            contentValues.put(KeyUserID, chatModel.getUser_id());
            contentValues.put(KeyFriendID, chatModel.getFriend_id());
            contentValues.put(KeySubject, chatModel.getSubject());
            contentValues.put(KeyChatID, chatModel.getChat_id());
            contentValues.put(KeyMessage, chatModel.getMessage());

            int j = DatabaseHelper.db.update(TableChatMaxID, contentValues, KeyUserID + " = '" + chatModel.getUser_id() + "' AND " + KeyFriendID + " = '" + chatModel.getFriend_id() + "' AND " + KeySubject + " = '" + chatModel.getSubject() + "' AND " + KeyChatID + " = '" + chatModel.getChat_id() + "' AND " + KeyMessage + " = '" + chatModel.getMessage() + "'", null);
            if (j < 1) {
                DatabaseHelper.db.insert(TableChatMaxID, null, contentValues);
            }
        }

    }

    public static String getChatMaxID(ChatReceiveEntity model) {
        String[] args = new String[]{model.getUser_id(), model.getFriend_id(), model.getSubject()};
        String selectQuery = "SELECT * FROM " + TableChatMaxID + " WHERE " + KeyUserID + "=? AND " + KeyFriendID + "=? AND " + KeySubject + "=?";
        Cursor cursor = DatabaseHelper.db.rawQuery(selectQuery, args);

        String msgList = "";

        if (cursor.moveToFirst()) {
            do {
                msgList = cursor.getString(cursor.getColumnIndex(KeyChatID));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return msgList;
    }

    public static boolean deleteChatMessageList(ChatReceiveEntity model) {
        boolean isData = false;
        String[] args = new String[]{model.getUser_id(), model.getFriend_id(), model.getSubject()};
        String selectQuery = "SELECT * FROM " + TableChat + " WHERE " + KeyUserID + "=? AND " + KeyFriendID + "=? AND " + KeySubject + "=?";
        String deleteQuery =  KeyUserID + "=? AND " + KeyFriendID + "=? AND " + KeySubject + "=?";

        Cursor cursor = DatabaseHelper.db.rawQuery(selectQuery, args);
        if (cursor.getCount() > 0) {
            isData = true;
            DatabaseHelper.db.delete(TableChat, deleteQuery, args);
//            updateDeleteChatTable(model);
}
        cursor.close();
        return isData;
    }

//    private static void updateDeleteChatTable(ChatReceiveEntity chatModel) {
//
//        if (!chatModel.getChat_id().isEmpty() && !chatModel.getChat_id().equals("-1")) {
//            ContentValues contentValues;
//            contentValues = new ContentValues();
//
//            contentValues.put(KeyUserID, chatModel.getUser_id());
//            contentValues.put(KeyFriendID, chatModel.getFriend_id());
//            contentValues.put(KeySubject, chatModel.getSubject());
//            contentValues.put(KeyChatID, chatModel.getChat_id());
//            contentValues.put(KeyMessage, chatModel.getMessage());
//
//            int j = DatabaseHelper.db.update(TableChatDelete, contentValues, KeyUserID + " = '" + chatModel.getUser_id() + "' AND " + KeyFriendID + " = '" + chatModel.getFriend_id() + "' AND " + KeySubject + " = '" + chatModel.getSubject() + "' AND " + KeyChatID + " = '" + chatModel.getChat_id() + "' AND " + KeyMessage + " = '" + chatModel.getMessage() + "'", null);
//            if (j < 1) {
//                DatabaseHelper.db.insert(TableChatDelete, null, contentValues);
//            }
//        }
//
//    }

//    public static String getDeletedChat(ChatReceiveEntity model) {
//        String[] args = new String[]{model.getUser_id(), model.getFriend_id(), model.getSubject()};
//        String selectQuery = "SELECT * FROM " + TableChatDelete + " WHERE " + KeyUserID + "=? AND " + KeyFriendID + "=? AND " + KeySubject + "=? AND " + KeyFriendID + "=?";
//        Cursor cursor = DatabaseHelper.db.rawQuery(selectQuery, args);
//
//        String msgList = "";
//
//        if (cursor.moveToFirst()) {
//            do {
//                msgList = cursor.getString(cursor.getColumnIndex(KeyChatID));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return msgList;
//    }

}
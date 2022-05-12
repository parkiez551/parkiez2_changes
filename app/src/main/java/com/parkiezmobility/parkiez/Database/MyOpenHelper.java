package com.parkiezmobility.parkiez.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.parkiezmobility.parkiez.Entities.UserEntity;

public class MyOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ParkingDB";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "User";

    private SQLiteDatabase db;
    private Cursor cursor;

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (Id Integer PRIMARY KEY AUTOINCREMENT, " +
                "UserID TEXT, Name TEXT, Email TEXT, MobileNo TEXT, Password TEXT, " +
                "Address TEXT, ProfileImg TEXT, Vehicle TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        System.out.println("On Upgrade Call");
    }

    public UserEntity getUser() {
        UserEntity user = null;
        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME;

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    user = new UserEntity();
                    user.setUserID(cursor.getInt(1));
                    user.setName(cursor.getString(2));
                    user.setEmail(cursor.getString(3));
                    user.setMobileNo(cursor.getString(4));
                    user.setPassword(cursor.getString(5));
                    user.setAddress(cursor.getString(6));
                    user.setProfileImg(cursor.getString(7));
                    user.setVehicle(cursor.getString(8));
                } while (cursor.moveToNext());
            }
            return user;
        } catch (Exception e) {
            e.getMessage();
        }finally {
            cursor.close();
        }
        return user;
    }
}

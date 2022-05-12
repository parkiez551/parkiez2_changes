package com.parkiezmobility.parkiez.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.parkiezmobility.parkiez.Entities.UserEntity;

public class DataHelp {
    SQLiteDatabase db;
    Context context;
    private MyOpenHelper db1;

    public DataHelp(Context con) {
        this.context = con;
        SQLiteOpenHelper myHelper = new MyOpenHelper(this.context);
        this.db = myHelper.getWritableDatabase();
        this.db1 = new MyOpenHelper(this.context);
    }

    public boolean putUser(UserEntity user) {
        try {
            ContentValues conV = new ContentValues();
            conV.put("UserID", user.getUserID());
            conV.put("Name", user.getName());
            conV.put("Email", user.getEmail());
            conV.put("MobileNo", user.getMobileNo());
            conV.put("Password", user.getPassword());
            conV.put("Address", user.getAddress());
            conV.put("ProfileImg", user.getProfileImg());
            conV.put("Vehicle", user.getVehicle());

            UserEntity userDetails = db1.getUser();
            if (userDetails == null) {
                db.insert(MyOpenHelper.TABLE_NAME, null, conV);
                return true;
            } else {
                String where = "UserID = " + userDetails.getUserID();
                db.update(MyOpenHelper.TABLE_NAME, conV, where, null);
                return true;
            }
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    public boolean LogOutUser(int userID) {
        try {
            String where = "UserID = " + userID;
            db.delete(MyOpenHelper.TABLE_NAME, where, null);
            return true;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    public void dbClose(SQLiteDatabase db) {
        db.close();
    }

    public boolean RemoveUser() {
        try {
            db.delete(MyOpenHelper.TABLE_NAME, null, null);
            return true;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

}


package com.example.c_ronaldo.myapplication_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by C_Ronaldo on 4/10/17.
 */

public class DBOperations extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "dongTestDB1.db";
    private static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS tableDong ( nickname TEXT UNIQUE , country TEXT, state TEXT, city TEXT, year TEXT, longitude TEXT, latitude TEXT, id TEXT PRIMARY KEY);";

    public DBOperations(Context ctx){
        super(ctx, DB_NAME, null, DB_VERSION);
        Log.i("DB","database created!");

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.i("DB","table created!");

    }

    public void addInfo(User user){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put("nickname",user.userNickname);
        contentValue.put("country",user.userCountry);
        contentValue.put("state",user.userState);
        contentValue.put("city",user.userCity);
        contentValue.put("year",user.userYear);
        contentValue.put("longitude",user.userLongitude);
        contentValue.put("latitude",user.userLatitude);
        contentValue.put("id",user.userId);

        db.insertWithOnConflict("tableDong",null,contentValue,SQLiteDatabase.CONFLICT_IGNORE);
        Log.i("DB","one raw created!");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

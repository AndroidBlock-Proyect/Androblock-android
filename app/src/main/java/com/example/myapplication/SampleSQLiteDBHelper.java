package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SampleSQLiteDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "ANNDROBLOCKDB";
    public static final String CLIENT_TABLE_NAME = "person";
    public static final String CLIENT_COLUMN_ID = "_id";
    public static final String CLIENT_COLUMN_NAME = "name";
    public static final String CLIENT_COLUMN_PORT = "port";
    public static final String CLIENT_PAYDATE = "paydate" ;
    private static final String DB_NAME = DATABASE_NAME;
    private static final int DB_VERSION = DATABASE_VERSION;


    public SampleSQLiteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CLIENT_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CLIENT_TABLE_NAME + " (" +
                CLIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLIENT_COLUMN_NAME + " TEXT, " +
                CLIENT_COLUMN_PORT + " INT," +
                CLIENT_PAYDATE + " INT " + ")");
    }
}
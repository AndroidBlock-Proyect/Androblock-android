package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBmanager {

    private SampleSQLiteDBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBmanager(Context c) {
        context = c;
    }

    public DBmanager open() throws SQLException {
        dbHelper = new SampleSQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(int port, int paydate) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(SampleSQLiteDBHelper.CLIENT_COLUMN_NAME, "cliente");
        contentValue.put(SampleSQLiteDBHelper.CLIENT_COLUMN_PORT, port);
        contentValue.put(SampleSQLiteDBHelper.CLIENT_PAYDATE, paydate);
        database.insert(SampleSQLiteDBHelper.CLIENT_TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { SampleSQLiteDBHelper.CLIENT_COLUMN_ID, SampleSQLiteDBHelper.CLIENT_COLUMN_NAME, SampleSQLiteDBHelper.CLIENT_COLUMN_PORT, SampleSQLiteDBHelper.CLIENT_PAYDATE };
        Cursor cursor = database.query(SampleSQLiteDBHelper.CLIENT_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(long _id) {
        database.delete(SampleSQLiteDBHelper.CLIENT_TABLE_NAME, SampleSQLiteDBHelper.CLIENT_COLUMN_ID + "=" + _id, null);
    }
}

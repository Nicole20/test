package com.example.nicole.test.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

/**
 * Created by Nicole on 3/19/2018.
 */

public class dbHelper extends SQLiteOpenHelper {

    /* Version of the database*/
    public static final int DATABASE_VERSION = 1;

    /* Name of the database*/
    public static final String DATABASE_NAME = "Stuff.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " INTEGER";
    private static final String COMMA = ", ";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Contract.TABLE_NAME + "(" +
            Contract.ID_COLUMN + NUMBER_TYPE + " PRIMARY KEY AUTOINCREMENT, " +
            Contract.COLUMN_NAME + TEXT_TYPE + COMMA +
            Contract.COLUMN_AGE + NUMBER_TYPE + COMMA +
            Contract.COLUMN_GENDER + NUMBER_TYPE + COMMA +
            Contract.COLUMN_CITY + TEXT_TYPE + ");";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Contract.TABLE_NAME;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}

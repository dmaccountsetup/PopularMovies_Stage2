package com.example.android.movieapitest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movieapitest.data.Contract.*;
/**
 * Created by ka171 on 5/10/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite_movies.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = " CREATE TABLE " +
                TableEntry.TABLE_NAME + "(" +
                TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                TableEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                TableEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                TableEntry.COLUMN_RATING + " TEXT, " +
                TableEntry.COLUMN_SYNOPSIS + " TEXT, " +
                TableEntry.COLUMN_POSTER + " TEXT NOT NULL" +
                ");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableEntry.TABLE_NAME);
        /*
        if (oldVersion < 2) {
             db.execSQL(DATABASE_ALTER_1);
        }
        if (oldVersion < 3) {
             db.execSQL(DATABASE_ALTER_2);
        }/*/
        onCreate(db);
    }
}

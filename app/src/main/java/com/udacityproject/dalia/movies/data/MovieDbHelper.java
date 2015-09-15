package com.udacityproject.dalia.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.udacityproject.dalia.movies.data.MovieContract.*;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT UNIQUE NOT NULL," +
                MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT UNIQUE NOT NULL," +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT UNIQUE NOT NULL," +
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT UNIQUE NOT NULL " + " );";


        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
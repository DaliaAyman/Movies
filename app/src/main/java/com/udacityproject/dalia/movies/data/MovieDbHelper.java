package com.udacityproject.dalia.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.udacityproject.dalia.movies.data.MovieContract.MovieEntryByHighestRated;
import static com.udacityproject.dalia.movies.data.MovieContract.MovieEntryByMostPopular;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE_MOST_POPULAR = "CREATE TABLE " + MovieEntryByMostPopular.TABLE_NAME + " (" +
                MovieEntryByMostPopular._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntryByMostPopular.COLUMN_MOVIE_KEY + " UNIQUE INTEGER NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL " + " );";

        final String SQL_CREATE_MOVIE_TABLE_HIGHEST_RATED = "CREATE TABLE " + MovieEntryByHighestRated.TABLE_NAME + " (" +
                MovieEntryByHighestRated._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntryByHighestRated.COLUMN_MOVIE_KEY + " UNIQUE INTEGER NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL " + " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE_MOST_POPULAR);
        db.execSQL(SQL_CREATE_MOVIE_TABLE_HIGHEST_RATED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntryByMostPopular.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntryByHighestRated.TABLE_NAME);
        onCreate(db);
    }
}
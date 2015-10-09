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
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE_MOST_POPULAR = "CREATE TABLE " + MovieEntryByMostPopular.TABLE_NAME + " (" +
                MovieEntryByMostPopular._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntryByMostPopular.COLUMN_MOVIE_KEY + " INTEGER NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieEntryByMostPopular.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                " UNIQUE (" + MovieEntryByMostPopular.COLUMN_MOVIE_KEY + " ) ON CONFLICT REPLACE);";

        final String SQL_CREATE_MOVIE_TABLE_HIGHEST_RATED = "CREATE TABLE " + MovieEntryByHighestRated.TABLE_NAME + " (" +
                MovieEntryByHighestRated._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntryByHighestRated.COLUMN_MOVIE_KEY + " INTEGER NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieEntryByHighestRated.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                " UNIQUE (" + MovieEntryByHighestRated.COLUMN_MOVIE_KEY + " ) ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.ReviewEntry.TABLE_NAME + " (" +
                MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntryByMostPopular.TABLE_NAME + "(" + MovieEntryByMostPopular.COLUMN_MOVIE_KEY + "),"  +
                " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntryByHighestRated.TABLE_NAME + "(" + MovieEntryByHighestRated.COLUMN_MOVIE_KEY + ")," +
                " UNIQUE (" + MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " ) ON CONFLICT REPLACE);";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_VIDEO_KEY + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_NAME + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntryByMostPopular.TABLE_NAME + "(" + MovieEntryByMostPopular.COLUMN_MOVIE_KEY + ")," +
                " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntryByHighestRated.TABLE_NAME + "(" + MovieEntryByHighestRated.COLUMN_MOVIE_KEY + ")," +
                " UNIQUE (" + MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " ) ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE_MOST_POPULAR);
        db.execSQL(SQL_CREATE_MOVIE_TABLE_HIGHEST_RATED);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntryByMostPopular.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntryByHighestRated.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.TrailerEntry.TABLE_NAME);
        onCreate(db);
    }
}
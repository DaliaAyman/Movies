package com.udacityproject.dalia.movies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE_BY_MOST_POPULAR = 100;
    static final int MOVIE_BY_MOST_POPULAR_WITH_ID = 101;
    static final int MOVIE_BY_HIGHEST_RATED = 200;
    static final int MOVIE_BY_HIGHEST_RATED_WITH_ID = 201;

    //movie_most_popular._ID = ?
    private String getMovieMostPopularIDSelection(int id){
        String sMovieMostPopularIDSelection =
                MovieContract.MovieEntryByMostPopular.TABLE_NAME + "."
                + MovieContract.MovieEntryByMostPopular._ID + " = " + id;
        return sMovieMostPopularIDSelection;
    }
    //movie_highest_rated._ID = ?
    private String getMovieHighestRatedIDSelection(int id){
        String sMovieHighestRatedIDSelection =
                MovieContract.MovieEntryByHighestRated.TABLE_NAME + "."
                        + MovieContract.MovieEntryByHighestRated._ID + " = " + id;
        return sMovieHighestRatedIDSelection;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        mOpenHelper.getWritableDatabase();
        return true;
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE_MOST_POPULAR, MOVIE_BY_MOST_POPULAR);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_MOST_POPULAR + "/#", MOVIE_BY_MOST_POPULAR_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_HIGHEST_RATED, MOVIE_BY_HIGHEST_RATED);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_HIGHEST_RATED + "/#", MOVIE_BY_HIGHEST_RATED_WITH_ID);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE_BY_MOST_POPULAR:
                return MovieContract.MovieEntryByMostPopular.CONTENT_TYPE;
            case MOVIE_BY_MOST_POPULAR_WITH_ID:
                return MovieContract.MovieEntryByMostPopular.CONTENT_ITEM_TYPE;
            case MOVIE_BY_HIGHEST_RATED:
                return MovieContract.MovieEntryByHighestRated.CONTENT_TYPE;
            case MOVIE_BY_HIGHEST_RATED_WITH_ID:
                return MovieContract.MovieEntryByHighestRated.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE_BY_MOST_POPULAR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntryByMostPopular.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }
            case MOVIE_BY_MOST_POPULAR_WITH_ID: {
                long _id = MovieContract.MovieEntryByMostPopular.getMovieIDFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntryByMostPopular.TABLE_NAME, projection,
                        MovieContract.MovieEntryByMostPopular._ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null, null, sortOrder);
                break;
            }
            case MOVIE_BY_HIGHEST_RATED: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntryByHighestRated.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }
            case MOVIE_BY_HIGHEST_RATED_WITH_ID: {
                long _id = MovieContract.MovieEntryByHighestRated.getMovieIDFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntryByHighestRated.TABLE_NAME, projection,
                        MovieContract.MovieEntryByHighestRated._ID + " = ?",
                        new String[]{Long.toString(_id)}, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE_BY_MOST_POPULAR: {
                long _id = db.insert(MovieContract.MovieEntryByMostPopular.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntryByMostPopular.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_BY_HIGHEST_RATED: {
                long _id = db.insert(MovieContract.MovieEntryByHighestRated.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntryByHighestRated.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        //delete all rows and return the number of records deleted
        if (selection == null) {
            selection = "1";
        }

        switch (match){
            case MOVIE_BY_MOST_POPULAR:
                rowsDeleted = db.delete(MovieContract.MovieEntryByMostPopular.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_BY_HIGHEST_RATED:
                rowsDeleted = db.delete(MovieContract.MovieEntryByHighestRated.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case MOVIE_BY_MOST_POPULAR:
                rowsUpdated = db.update(MovieContract.MovieEntryByMostPopular.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_BY_HIGHEST_RATED:
                rowsUpdated = db.update(MovieContract.MovieEntryByHighestRated.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE_BY_MOST_POPULAR: {
                return bulkInsertHelper(MovieContract.MovieEntryByMostPopular.TABLE_NAME, db, values, uri);
            }
            case MOVIE_BY_HIGHEST_RATED: {
                return bulkInsertHelper(MovieContract.MovieEntryByHighestRated.TABLE_NAME, db, values, uri);
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
    private int bulkInsertHelper(String tableName, SQLiteDatabase db, ContentValues[] values, Uri uri){
        db.beginTransaction();
        int returnCount = 0;
        try{
            for(ContentValues value: values){
                long _id = db.insert(tableName, null, value);
                if(_id != -1){
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}

package com.udacityproject.dalia.movies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.udacityproject.dalia.movies.Utility;

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
    static final int REVIEW = 300;
    static final int REVIEW_WITH_MOVIE_ID = 301;
    static final int TRAILER = 400;
    static final int TRAILER_WITH_MOVIE_ID = 401;
    static final int FAVORITE = 500;
    static final int FAVORITE_WITH_MOVIE_ID = 501;

    public static long getMovieKeyFromUri(Context context, long id, int isFav){
        Cursor cursor;
        String sortOrder = Utility.getSortTypeFromPreferences(context);
        if (isFav == 0){
            switch (sortOrder){
                case MovieContract.POPULARITY:{
                    cursor = context.getContentResolver().query(MovieContract.MovieEntryByMostPopular.CONTENT_URI,
                            new String[]{MovieContract.MovieEntryByMostPopular._ID, MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_KEY},
                            MovieContract.MovieEntryByMostPopular._ID + " = ?",
                            new String[]{String.valueOf(id)}, null);
                    if(cursor.moveToFirst()){
                        int columnIndex = cursor.getColumnIndex(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_KEY);
                        String key = cursor.getString(columnIndex);
                        cursor.close();
                        return Long.parseLong(key);
                    }else{
                        return 0;
                    }

                }
                case MovieContract.VOTE_AVERAGE:{
                    cursor = context.getContentResolver().query(MovieContract.MovieEntryByHighestRated.CONTENT_URI,
                            new String[]{MovieContract.MovieEntryByHighestRated._ID, MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_KEY},
                            MovieContract.MovieEntryByHighestRated._ID + " = ?",
                            new String[]{String.valueOf(id)}, null);
                    if(cursor.moveToFirst()){
                        int columnIndex = cursor.getColumnIndex(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_KEY);
                        String key = cursor.getString(columnIndex);
                        cursor.close();
                        return Long.parseLong(key);
                    }else{
                        return 0;
                    }
                }
            }
        }else{
            //long _id = MovieContract.FavoriteMovie.getMovieIDFromUri(MovieContract.FavoriteMovie.CONTENT_URI);
            cursor = context.getContentResolver().query(MovieContract.FavoriteMovie.CONTENT_URI,
                    new String[]{MovieContract.FavoriteMovie._ID, MovieContract.FavoriteMovie.COLUMN_MOVIE_KEY},
                    MovieContract.FavoriteMovie._ID + " = ?",
                    new String[]{String.valueOf(id)}, null);
            if(cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(MovieContract.FavoriteMovie.COLUMN_MOVIE_KEY);
                String key = cursor.getString(columnIndex);
                cursor.close();
                return Long.parseLong(key);
            }else{
                return 0;
            }
        }
        return 0;
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

        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", REVIEW_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/#", TRAILER_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE + "/#", FAVORITE_WITH_MOVIE_ID);
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

            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_WITH_MOVIE_ID:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;

            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case TRAILER_WITH_MOVIE_ID:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;

            case FAVORITE:
                return MovieContract.FavoriteMovie.CONTENT_TYPE;
            case FAVORITE_WITH_MOVIE_ID:
                return MovieContract.FavoriteMovie.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case MOVIE_BY_MOST_POPULAR: {
                retCursor = db.query(
                        MovieContract.MovieEntryByMostPopular.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }
            case MOVIE_BY_MOST_POPULAR_WITH_ID: {
                long _id = MovieContract.MovieEntryByMostPopular.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MovieContract.MovieEntryByMostPopular.TABLE_NAME, projection,
                        MovieContract.MovieEntryByMostPopular._ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null, null, sortOrder);
                break;
            }
            case MOVIE_BY_HIGHEST_RATED: {
                retCursor = db.query(
                        MovieContract.MovieEntryByHighestRated.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }
            case MOVIE_BY_HIGHEST_RATED_WITH_ID: {
                long _id = MovieContract.MovieEntryByHighestRated.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MovieContract.MovieEntryByHighestRated.TABLE_NAME, projection,
                        MovieContract.MovieEntryByHighestRated._ID + " = ?",
                        new String[]{Long.toString(_id)}, null, null, sortOrder);
                break;
            }
            case REVIEW:{
                retCursor = db.query(
                        MovieContract.ReviewEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case REVIEW_WITH_MOVIE_ID:{
                long _id = MovieContract.ReviewEntry.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MovieContract.ReviewEntry.TABLE_NAME, projection,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = ?",
                        new String[]{Long.toString(_id)}, null, null, sortOrder);
                break;
            }
            case TRAILER:{
                retCursor = db.query(
                        MovieContract.TrailerEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case TRAILER_WITH_MOVIE_ID:{
                long _id = MovieContract.TrailerEntry.getMovieIDFromUri(uri);
                retCursor = db.query(MovieContract.TrailerEntry.TABLE_NAME, projection,
                        MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " = ?",
                        new String[]{Long.toString(_id)}, null, null, sortOrder);
                break;
            }
            case FAVORITE:{
                retCursor = db.query(
                        MovieContract.FavoriteMovie.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case FAVORITE_WITH_MOVIE_ID:{
                long _id = MovieContract.FavoriteMovie.getMovieIDFromUri(uri);
                retCursor = db.query(MovieContract.FavoriteMovie.TABLE_NAME, projection,
                        MovieContract.FavoriteMovie.COLUMN_MOVIE_KEY + " = ?",
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
            case REVIEW:{
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE:{
                long _id = db.insert(MovieContract.FavoriteMovie.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteMovie.buildMovieUri(_id);
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
            case REVIEW:
                rowsDeleted = db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILER:
                rowsDeleted = db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsDeleted = db.delete(MovieContract.FavoriteMovie.TABLE_NAME, selection, selectionArgs);
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
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRAILER:
                rowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsUpdated = db.update(MovieContract.FavoriteMovie.TABLE_NAME, values, selection, selectionArgs);
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
            case MOVIE_BY_MOST_POPULAR:
                return bulkInsertHelper(MovieContract.MovieEntryByMostPopular.TABLE_NAME, db, values, uri);
            case MOVIE_BY_HIGHEST_RATED:
                return bulkInsertHelper(MovieContract.MovieEntryByHighestRated.TABLE_NAME, db, values, uri);
            case REVIEW:
                return bulkInsertHelper(MovieContract.ReviewEntry.TABLE_NAME, db, values, uri);
            case TRAILER:
                return bulkInsertHelper(MovieContract.TrailerEntry.TABLE_NAME, db, values, uri);
            case FAVORITE:
                return bulkInsertHelper(MovieContract.FavoriteMovie.TABLE_NAME, db, values, uri);
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

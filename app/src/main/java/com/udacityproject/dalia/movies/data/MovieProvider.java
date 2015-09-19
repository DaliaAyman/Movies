package com.udacityproject.dalia.movies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.sql.SQLException;

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

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
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


    // TODO
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
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

    // TODO
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    // TODO
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

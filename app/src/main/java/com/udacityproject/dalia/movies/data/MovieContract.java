package com.udacityproject.dalia.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.udacityproject.dalia.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //path to the tables
    // content://com.udacityproject.dalia.movies.movie_most_popular
    public static final String PATH_MOVIE_MOST_POPULAR = "movie_most_popular";
    public static final String PATH_MOVIE_HIGHEST_RATED = "movie_highest_rated";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";

    /* Inner class that defines the table contents of the movie most popular table */
    public static class MovieEntryByMostPopular implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_MOST_POPULAR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_MOST_POPULAR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_MOST_POPULAR;

        public static final String TABLE_NAME = "movie_most_popular";

        //Columns
        public static final String COLUMN_MOVIE_KEY = "movie_id"; //id from the backend

        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getMovieIDFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }
    }


    /* Inner class that defines the table contents of the movie table */
    public static class MovieEntryByHighestRated implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_HIGHEST_RATED).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_HIGHEST_RATED;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_HIGHEST_RATED;

        public static final String TABLE_NAME = "movie_highest_rated";

        //Columns
        public static final String COLUMN_MOVIE_KEY = "movie_id"; //id from the backend

        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static int getMovieIDFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    /* Inner class that defines the table contents of the review table */
    public static class ReviewEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";

        //Columns
        public static final String COLUMN_MOVIE_KEY = "movie_id"; //id from the backend

        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";

        /*public static Uri buildReviewUriWithMovieID(long movie_key, String review_key){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movie_key))
                    .appendQueryParameter(COLUMN_REVIEW_ID, review_key).build();
        }*/
        public static Uri buildReviewWithId(long insertedId) {
            return ContentUris.withAppendedId(CONTENT_URI, insertedId);
        }

        /*public static String getReviewKeyFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }*/
        public static long getMovieIDFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }
    }

    /* Inner class that defines the table contents of the trailer table */
    public static class TrailerEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String TABLE_NAME = "trailer";

        //Columns
        public static final String COLUMN_MOVIE_KEY = "movie_id"; //id from the backend

        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_TRAILER_VIDEO_KEY = "video_key";
        public static final String COLUMN_TRAILER_NAME = "name";

        /*public static Uri buildTrailerUriWithMovieID(long movie_key, String trailer_key){
            return CONTENT_URI.buildUpon().appendPath(Long.toString(movie_key))
                    .appendQueryParameter(COLUMN_TRAILER_ID, trailer_key).build();
        }*/

        public static Uri buildTrailerWithId(long insertedId) {
            return ContentUris.withAppendedId(CONTENT_URI, insertedId);
        }

        /*public static String getTrailerKeyFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }*/

        public static long getMovieIDFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }
    }

}
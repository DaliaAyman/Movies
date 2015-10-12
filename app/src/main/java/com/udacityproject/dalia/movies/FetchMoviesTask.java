package com.udacityproject.dalia.movies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.udacityproject.dalia.movies.data.MovieContract;
import com.udacityproject.dalia.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Dalia on 9/2/2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Void> {
    GridView mGridView;
    Context mContext;

    //TODO remove before publishing to Github
    private final static String API_KEY = "7c8fd8219025b80ee4f7471ac5571281";
    private final static String MOVIES_BASE_URL
            = "http://api.themoviedb.org/3/discover/movie";
    private final static String SORT_BY_PARAM = "sort_by";
    private final static String API_PARAM = "api_key";

    String moviesJSONStr;

    private final static String MOVIE_DATA_BASE_URL
            = "http://api.themoviedb.org/3/movie";
    private final static String REVIEWS_PARAM = "reviews";
    private final static String TRAILERS_PARAM = "videos";

    public FetchMoviesTask(Context context, GridView gridView){
        //mAdapter = adapter;
        mContext = context;
        mGridView = gridView;
    }

    @Override
    protected Void doInBackground(String... options) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        moviesJSONStr = null; //JSON Response

        try {
            String sortType = options[0];
            // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]
            Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, sortType + ".desc")
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            //requesting and opening the connection
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //reading input stream to string
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            if(stringBuffer.length() == 0){ //stream was empty
                return null;
            }

            moviesJSONStr = stringBuffer.toString();

        }catch (IOException e){
            Log.e("NetworkConnection", "Error handling url", e);
            return null;
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("NetworkConnection", "Error closing stream", e);
                }
            }
        }

        //get data from JSON
        try{
            getMoviesDataFromJSON(moviesJSONStr);
        }catch (JSONException e){
            Log.e("grid", e.getMessage(), e);
            e.printStackTrace();
        }

        //only if error getting or parsing data
        return null;
    }

    public void getMoviesDataFromJSON(String moviesStr) throws JSONException{

        JSONObject moviesObject = new JSONObject(moviesStr);
        JSONArray resultsArray = moviesObject.getJSONArray("results");

        Vector<ContentValues> cVVector = new Vector<ContentValues>(resultsArray.length());
        String sortType = "";
        for(int i=0; i<resultsArray.length(); i++){
            JSONObject movieObj = resultsArray.getJSONObject(i);

            String key = movieObj.getString("id");

            String reviewsJSON = getReviewsOrTrailersJson(key, REVIEWS_PARAM);
            //Log.d("grid", "reviewsJSON: " + reviewsJSON);
            JSONObject reviewsObject = new JSONObject(reviewsJSON);
            JSONArray reviewsResultsArray = reviewsObject.getJSONArray("results");
            //Log.d("grid", "reviewResultsArray length: " + reviewsResultsArray.length());
            if(reviewsResultsArray.length() != 0){

            }
            for(int j=0; j<reviewsResultsArray.length(); j++){
                JSONObject reviewObj = reviewsResultsArray.getJSONObject(j);
                String review_id = reviewObj.getString("id");
                String author = reviewObj.getString("author");
                String content = reviewObj.getString("content");
                long reviewId = addReviews(key, review_id, author, content);
            }

            String trailersJSON = getReviewsOrTrailersJson(key, TRAILERS_PARAM);
            JSONObject trailersObject = new JSONObject(trailersJSON);
            JSONArray trailersResultsArray = trailersObject.getJSONArray("results");

            for(int k=0; k<trailersResultsArray.length(); k++){
                JSONObject trailerObj = trailersResultsArray.getJSONObject(k);
                String trailer_id = trailerObj.getString("id");
                String video_key = trailerObj.getString("key");
                String name = trailerObj.getString("name");
                addTrailers(key, trailer_id, video_key, name);
            }

            String title = movieObj.getString("title");
            String overview = movieObj.getString("overview");
            String posterPath = movieObj.getString("poster_path");
            double voteAverage = movieObj.getDouble("vote_average");
            String releaseDate = movieObj.getString("release_date");

            Movie m = new Movie(key, title, overview, posterPath, voteAverage, releaseDate);
            //Log.d("grid", "m: " + m.getTitle() + ", " + m.getOverview() + ", " + m.getPosterPath() + ", " + m.getVoteAverage());

            sortType = Utility.getSortTypeFromPreferences(mContext);
            ContentValues movieValues = new ContentValues();
            switch (sortType){
                case MovieContract.POPULARITY: {
                    movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_KEY, key);
                    movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_TITLE, title);
                    movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH, posterPath);
                    movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_OVERVIEW, overview);
                    movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_VOTE_AVERAGE, voteAverage);
                    movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
                    break;
                }
                case MovieContract.VOTE_AVERAGE: {
                    movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_KEY, key);
                    movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_TITLE, title);
                    movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH, posterPath);
                    movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_OVERVIEW, overview);
                    movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_VOTE_AVERAGE, voteAverage);
                    movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("Unknown sort type: " + sortType);
            }
            cVVector.add(movieValues);
        }
        int inserted = 0;
        if(cVVector.size() > 0){
            ContentValues[] cVArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cVArray);
            switch (sortType){
                case MovieContract.POPULARITY:{
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntryByMostPopular.CONTENT_URI, cVArray);
                    break;
                }case MovieContract.VOTE_AVERAGE: {
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntryByHighestRated.CONTENT_URI, cVArray);
                    break;
                }default:
                    throw new UnsupportedOperationException("Unknown sort type: " + sortType);
            }

            Log.d("grid", "FetchMoviesTask complete " + inserted + " inserted");
        }
    }
    String getReviewsOrTrailersJson(String movie_key, String PARAM){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String returnJson;
        try{
            Uri builtUri = Uri.parse(MOVIE_DATA_BASE_URL).buildUpon()
                    .appendPath(movie_key)
                    .appendPath(PARAM)
                    .appendQueryParameter(API_PARAM, API_KEY).build();
            URL url = new URL(builtUri.toString());

            //requesting and opening the connection
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //reading input stream to string
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\n");
            }

            if(stringBuffer.length() == 0){ //stream was empty
                return null;
            }

            returnJson = stringBuffer.toString();

        }catch (IOException e){
            Log.e("NetworkConnection", "Error handling url", e);
            return null;
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("NetworkConnection", "Error closing stream", e);
                }
            }
        }
        return returnJson;
    }
    long addReviews(String movie_key, String review_id, String author, String content){
        long reviewId;
        //check if already exists
        Cursor movieReviewCursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                new String[]{MovieContract.ReviewEntry._ID},
                MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movie_key}, null
        );
        if(movieReviewCursor.moveToFirst()){
            int reviewIdIndex = movieReviewCursor.getColumnIndex(MovieContract.ReviewEntry._ID);
            reviewId = movieReviewCursor.getLong(reviewIdIndex);
        }else{
            //inserting
            ContentValues reviewValues = new ContentValues();
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_KEY, movie_key);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, review_id);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, author);
            reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, content);

            Uri insertedUri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.CONTENT_URI, reviewValues);
            reviewId = ContentUris.parseId(insertedUri);
        }
        movieReviewCursor.close();
        return reviewId;
    }
    void addTrailers(String movie_key, String trailer_id, String video_key, String name){
        long trailerId;
        //check if already exists
        Cursor trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                new String[]{MovieContract.TrailerEntry._ID},
                MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movie_key}, null);

            //inserting
            ContentValues trailerValues = new ContentValues();
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_KEY, movie_key);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, trailer_id);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_VIDEO_KEY, video_key);
            trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, name);
            Log.d("grid", "trailerValues: " + movie_key + " " + trailer_id + " " + video_key + " " + name);
            Uri insertedUri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.CONTENT_URI, trailerValues);
            trailerId = ContentUris.parseId(insertedUri);

        trailerCursor.close();
    }
}
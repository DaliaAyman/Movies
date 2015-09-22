package com.udacityproject.dalia.movies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.udacityproject.dalia.movies.data.MovieContract;
import com.udacityproject.dalia.movies.model.Movie;
import com.udacityproject.dalia.movies.model.MoviesGridViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dalia on 9/2/2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {
    MoviesGridViewAdapter mAdapter;
    GridView mGridView;
    Context mContext;

    //TODO remove before publishing to Github
    private final static String API_KEY = "";
    private final static String MOVIES_BASE_URL
            = "http://api.themoviedb.org/3/discover/movie";

    private final static String SORT_BY_PARAM = "sort_by";
    private final static String API_PARAM = "api_key";

    String moviesJSONStr;

    public FetchMoviesTask(Context context, GridView gridView){
        //mAdapter = adapter;
        mContext = context;
        mGridView = gridView;
    }

    @Override
    protected Movie[] doInBackground(String... options) {
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
            return getMoviesDataFromJSON(moviesJSONStr);
        }catch (JSONException e){
            Log.e("grid", e.getMessage(), e);
            e.printStackTrace();
        }

        //only if error getting or parsing data
        return null;
    }

    void addMovie(ContentValues movieValues, String movieTableName, String title, String overview, String posterPath, double voteAverage, String releaseDate){

        switch (movieTableName){
            case MovieContract.MovieEntryByMostPopular.TABLE_NAME: {
                movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_TITLE, title);
                movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH, posterPath);
                movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_VOTE_AVERAGE, voteAverage);
                movieValues.put(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_RELEASE_DATE, releaseDate);

                Uri insertedUri = mContext.getContentResolver().insert(MovieContract.MovieEntryByMostPopular.CONTENT_URI, movieValues);

            }
            case MovieContract.MovieEntryByHighestRated.TABLE_NAME: {
                movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_TITLE, title);
                movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH, posterPath);
                movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_VOTE_AVERAGE, voteAverage);
                movieValues.put(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_RELEASE_DATE, releaseDate);

                Uri insertedUri = mContext.getContentResolver().insert(MovieContract.MovieEntryByHighestRated.CONTENT_URI, movieValues);

            }
            default:
                throw new UnsupportedOperationException("Wrong Table Name " + movieTableName);
        }
    }

    public Movie[] getMoviesDataFromJSON(String moviesStr) throws JSONException{

        JSONObject moviesObject = new JSONObject(moviesStr);
        JSONArray resultsArray = moviesObject.getJSONArray("results");

        Movie[] resultObjs = new Movie[resultsArray.length()];

        for(int i=0; i<resultsArray.length(); i++){
            JSONObject movieObj = resultsArray.getJSONObject(i);

            String title = movieObj.getString("title");
            String overview = movieObj.getString("overview");
            String posterPath = movieObj.getString("poster_path");
            double voteAverage = movieObj.getDouble("vote_average");
            String releaseDate = movieObj.getString("release_date");

            Movie m = new Movie(title, overview, posterPath, voteAverage, releaseDate);
            //Log.d("grid", "m: " + m.getTitle() + ", " + m.getOverview() + ", " + m.getPosterPath() + ", " + m.getVoteAverage());
            resultObjs[i] = m;

            String sortType = Utility.getSortTypeFromPreferences(mContext);
            ContentValues movieValues = new ContentValues();

            switch (sortType){
                case "popularity": {

                }
                case "vote_average": {

                }
                default:
                    throw new UnsupportedOperationException("Unknown sort type: " + sortType);
            }

        }

        return resultObjs;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        if(movies != null){
            ArrayList<Movie> arrayList = new ArrayList<Movie>(movies.length);
            for (Movie m1: movies){
                arrayList.add(m1);
            }
            mAdapter = new MoviesGridViewAdapter(mContext, R.layout.movie_grid_item, arrayList);
            mGridView.setAdapter(mAdapter);

        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

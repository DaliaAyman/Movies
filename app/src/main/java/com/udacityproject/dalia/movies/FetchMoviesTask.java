package com.udacityproject.dalia.movies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

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
public class FetchMoviesTask extends AsyncTask<Void, Void, Movie[]> {
    CustomGridViewAdapter mAdapter;
    GridView mGridView;
    Context mContext;

    ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();
    //ArrayList<String> moviesImages = new ArrayList<String>();

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
    protected Movie[] doInBackground(Void... params) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        moviesJSONStr = null; //JSON Response

        try {

            Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, "popularity.desc")
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

            Movie m = new Movie(title, overview, posterPath, voteAverage);
            Log.d("grid", "m: " + m.getTitle() + ", " + m.getOverview() + ", " + m.getPosterPath() + ", " + m.getVoteAverage());
            resultObjs[i] = m;
            moviesArrayList.add(m);
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
            mAdapter = new CustomGridViewAdapter(mContext, R.layout.movie_grid_item, arrayList);
            mGridView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

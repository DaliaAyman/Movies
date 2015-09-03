package com.udacityproject.dalia.movies;

import android.os.AsyncTask;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by Dalia on 9/3/2015.
 */
public class JSONParseMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    CustomGridViewAdapter mAdapter;
    GridView mGridView;
    ArrayList<String> moviesImages;

    public JSONParseMoviesTask(CustomGridViewAdapter adapter, GridView gridView){
        mAdapter = adapter;
        mGridView = gridView;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... movieJSONStr) {

        ArrayList<Movie> movies = new ArrayList<Movie>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        /*Type collectionType = new TypeToken<ArrayList<Movie>>(){}.getType();
        Log.d("gson", "Str: " + movieJSONStr[0]);
        movies = gson.fromJson(movieJSONStr[0], collectionType);*/

        return movies;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        if(movies != null){
            moviesImages = new ArrayList<String>();
            for(int i=0; i<movies.size(); i++){
                moviesImages.set(i, movies.get(i).getPosterPath());
            }
            mAdapter.setImagesUrlsArrayList(moviesImages);
            mGridView.setAdapter(mAdapter);
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

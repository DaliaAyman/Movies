package com.udacityproject.dalia.movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dalia on 9/2/2015.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {
    CustomGridViewAdapter mAdapter;
    GridView mGridView;

    //TODO remove before publishing to Github
    private final static String API_KEY = "";
    private final static String MOVIES_BASE_URL
            = "http://api.themoviedb.org/3/discover/movie";
    private final static String SORT_BY_PARAM = "sort_by";
    private final static String API_PARAM = "api_key";

    String moviesJSONStr;

    public FetchMoviesTask(CustomGridViewAdapter adapter, GridView gridView){
        mAdapter = adapter;
        mGridView = gridView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        moviesJSONStr = null; //JSON Response

        try {

            Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, "popularity")
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

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);

        JSONParseMoviesTask jsonParseMoviesTask = new JSONParseMoviesTask(mAdapter, mGridView);
        jsonParseMoviesTask.execute(moviesJSONStr);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

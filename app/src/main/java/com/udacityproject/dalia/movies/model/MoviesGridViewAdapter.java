package com.udacityproject.dalia.movies.model;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacityproject.dalia.movies.R;
import com.udacityproject.dalia.movies.Utility;
import com.udacityproject.dalia.movies.data.MovieContract;

/**
 * Created by Dalia on 8/25/2015.
 */
public class MoviesGridViewAdapter extends CursorAdapter {
    int width;
    int height;

    public MoviesGridViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById(R.id.movie_image);

        String sortOrder = Utility.getSortTypeFromPreferences(context);
        int moviePosterColumn=3;
        switch (sortOrder){
            case MovieContract.POPULARITY:
                moviePosterColumn = cursor.getColumnIndex(MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH);
                break;
            case MovieContract.VOTE_AVERAGE:
                moviePosterColumn = cursor.getColumnIndex(MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH);
                break;
            case MovieContract.FAVORITES:
                //TODO
                break;
        }

        String moviePoster = cursor.getString(moviePosterColumn);

        //Log.d("grid", "moviePoster = " + moviePoster);

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185//" + moviePoster)
                .placeholder(R.drawable.movie_loading)
                .resize(width/2, height/2)
                .centerCrop()

                .into(imageView);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
    }

    /*public MoviesGridViewAdapter(Context context, int resource, ArrayList<Movie> arrayList) {
        super(context, resource, arrayList);

        this.layoutResourceId = resource;
        this.context = context;
        movieArrayList = arrayList;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }*/
}

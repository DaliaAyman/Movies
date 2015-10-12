package com.udacityproject.dalia.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacityproject.dalia.movies.R;

/**
 * Created by Dalia on 10/11/2015.
 */
public class MovieDetailsAdapter_Header extends CursorAdapter{
    public MovieDetailsAdapter_Header(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_MOVIE_KEY = 1;
    private static final int COL_MOVIE_TITLE = 2;
    private static final int COL_MOVIE_POSTER_PATH = 3;
    private static final int COL_MOVIE_OVERVIEW = 4;
    private static final int COL_MOVIE_VOTE_AVERAGE = 5;
    private static final int COL_MOVIE_RELEASE_DATE = 6;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("grid", "newView in header");
        return LayoutInflater.from(context).inflate(R.layout.list_item_header, parent, false);
    }

    @Override
    public void bindView(View rootView, Context context, Cursor data) {
        Log.d("grid", "bindView in header");
        TextView title = (TextView)rootView.findViewById(R.id.movie_title);
        TextView releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        TextView rating = (TextView)rootView.findViewById(R.id.rating);
        ImageView poster = (ImageView)rootView.findViewById(R.id.poster);
        TextView overview = (TextView)rootView.findViewById(R.id.overview);


        if(data != null && data.moveToFirst()){
            title.setText(data.getString(COL_MOVIE_TITLE));
            double ratingDouble = data.getDouble(COL_MOVIE_VOTE_AVERAGE);
            rating.setText(ratingDouble + "/10");
            overview.setText(data.getString(COL_MOVIE_OVERVIEW));
            releaseDate.setText(data.getString(COL_MOVIE_RELEASE_DATE));
            Picasso.with(context.getApplicationContext()).load(
                    "http://image.tmdb.org/t/p/w342//" + data.getString(COL_MOVIE_POSTER_PATH))
                    .placeholder(R.drawable.movie_loading)
                    .resize(500,900)
                    .centerInside()
                    .into(poster);
        }
    }
}
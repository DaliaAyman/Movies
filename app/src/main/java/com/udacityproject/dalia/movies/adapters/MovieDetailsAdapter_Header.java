package com.udacityproject.dalia.movies.adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacityproject.dalia.movies.R;
import com.udacityproject.dalia.movies.data.MovieContract;

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
        return LayoutInflater.from(context).inflate(R.layout.list_item_header, parent, false);
    }

    @Override
    public void bindView(View rootView, Context context, Cursor data) {
        TextView title = (TextView)rootView.findViewById(R.id.movie_title);
        TextView releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        TextView rating = (TextView)rootView.findViewById(R.id.rating);
        ImageView poster = (ImageView)rootView.findViewById(R.id.poster);
        TextView overview = (TextView)rootView.findViewById(R.id.overview);
        final ImageButton favoriteImageView = (ImageButton)rootView.findViewById(R.id.favorite_icon);

        if(data != null && data.moveToFirst()){
            final String movieKey = data.getString(COL_MOVIE_KEY);
            final String titleVal = data.getString(COL_MOVIE_TITLE);
            title.setText(titleVal);
            final double voteAverageVal = data.getDouble(COL_MOVIE_VOTE_AVERAGE);
            rating.setText(voteAverageVal + "/10");
            final String overviewVal = data.getString(COL_MOVIE_OVERVIEW);
            overview.setText(overviewVal);
            final String releaseDateVal = data.getString(COL_MOVIE_RELEASE_DATE);
            releaseDate.setText(releaseDateVal);
            final String posterPathVal = data.getString(COL_MOVIE_POSTER_PATH);
            Picasso.with(context.getApplicationContext()).load(
                    "http://image.tmdb.org/t/p/w342//" + posterPathVal)
                    .placeholder(R.drawable.movie_loading)
                    .resize(500,900)
                    .centerInside()
                    .into(poster);
            if(checkIfFavorite(movieKey) == true){
                favoriteImageView.setImageResource(R.drawable.favorite_1);
            }else{
                favoriteImageView.setImageResource(R.drawable.favorite_0);
            }


            favoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkIfFavorite(movieKey) == true){
                        Log.d("grid", "already in favorites, so remove");
                        favoriteImageView.setImageResource(R.drawable.favorite_0);
                        removeFromFavorites(movieKey);
                    }
                    else{
                        Log.d("grid", "not in favorites, so add");
                        favoriteImageView.setImageResource(R.drawable.favorite_1);
                        addToFavoritesTable(movieKey, titleVal, overviewVal, posterPathVal, voteAverageVal, releaseDateVal);
                    }
                }
            });
        }
    }
    public void removeFromFavorites(String movieKey){
        int rowsDeleted = mContext.getContentResolver().delete(
                MovieContract.FavoriteMovie.CONTENT_URI,
                MovieContract.FavoriteMovie.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movieKey});


    }
    public boolean checkIfFavorite(String movieKey){
        boolean isFavorite = false;
        long favId;
        //check if already exists
        Cursor movieFavCursor = mContext.getContentResolver().query(
                MovieContract.FavoriteMovie.CONTENT_URI,
                new String[]{MovieContract.FavoriteMovie._ID},
                MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movieKey}, null
        );
        if(movieFavCursor.moveToFirst()) {
            int favIdIndex = movieFavCursor.getColumnIndex(MovieContract.ReviewEntry._ID);
            favId = movieFavCursor.getLong(favIdIndex);
            isFavorite = true;
        }
        return isFavorite;
    }

    public long addToFavoritesTable(String movieKey, String title, String overview, String posterPath, double voteAverage, String releaseDate){
        long favMovieId;
        //check if already exists
        Cursor favMovieCursor = mContext.getContentResolver().query(
                MovieContract.FavoriteMovie.CONTENT_URI,
                new String[]{MovieContract.FavoriteMovie._ID},
                MovieContract.FavoriteMovie.COLUMN_MOVIE_KEY + " = ?",
                new String[]{movieKey}, null
        );
        if(favMovieCursor.moveToFirst()){
            int favMovieIdIndex = favMovieCursor.getColumnIndex(MovieContract.FavoriteMovie._ID);
            favMovieId = favMovieCursor.getLong(favMovieIdIndex);
        }else{
            //inserting
            ContentValues favMovieValues = new ContentValues();
            favMovieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_KEY, movieKey);
            favMovieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE, title);
            favMovieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_OVERVIEW, overview);
            favMovieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_POSTER_PATH, posterPath);
            favMovieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_VOTE_AVERAGE, voteAverage);
            favMovieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_RELEASE_DATE, releaseDate);

            Uri insertedUri = mContext.getContentResolver().insert(MovieContract.FavoriteMovie.CONTENT_URI, favMovieValues);
            favMovieId = ContentUris.parseId(insertedUri);
        }
        favMovieCursor.close();
        return favMovieId;
    }
}
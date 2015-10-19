package com.udacityproject.dalia.movies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacityproject.dalia.movies.adapters.MoviesGridViewAdapter;
import com.udacityproject.dalia.movies.data.MovieContract;


public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private MoviesGridViewAdapter adapter;
    private GridView gridView;

    private static final int FAVORITES_LOADER = 1;

    public FavoritesFragment () {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        adapter = new MoviesGridViewAdapter(getActivity(), null, 0);
        gridView = (GridView)view.findViewById(R.id.fav_movies_grid);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor currentData = (Cursor) parent.getItemAtPosition(position);
                if (currentData != null) {
                    Intent detailsIntent = new Intent(getActivity(), MovieDetailActivity.class);

                    int MOVIE_ID_COL = currentData.getColumnIndex(MovieContract.FavoriteMovie._ID);
                    Uri movieUri = MovieContract.FavoriteMovie.buildMovieUri(currentData.getInt(MOVIE_ID_COL));
                    detailsIntent.putExtra("fav", "1");
                    detailsIntent.setData(movieUri);
                    startActivity(detailsIntent);
                }
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.FavoriteMovie.CONTENT_URI,
                new String[]{MovieContract.FavoriteMovie._ID, MovieContract.FavoriteMovie.COLUMN_MOVIE_POSTER_PATH},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("grid", "Favorites Cursor loaded, " + cursor.getCount() + " rows fetched");
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
}

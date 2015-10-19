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

import com.udacityproject.dalia.movies.data.MovieContract;
import com.udacityproject.dalia.movies.adapters.MoviesGridViewAdapter;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MoviesHolderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private MoviesGridViewAdapter adapter;
    //private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private GridView gridView;

    private FetchMoviesTask task;

    private static final int MOVIES_LOADER = 0;

    public MoviesHolderFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        updateMovies();
    }

    public void updateMovies(){
        String sortType = Utility.getSortTypeFromPreferences(getActivity());

        if(task != null){
            task.cancel(true);
        }
        task = new FetchMoviesTask(getActivity(), gridView);
        task.execute(sortType);
        gridView.setAdapter(adapter);
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new MoviesGridViewAdapter(getActivity(), null, 0);

        gridView = (GridView)rootView.findViewById(R.id.movies_grid);

        updateMovies();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor currentData = (Cursor) parent.getItemAtPosition(position);
                if (currentData != null) {
                    Intent detailsIntent = new Intent(getActivity(), MovieDetailActivity.class);
                    String sortType = Utility.getSortTypeFromPreferences(getActivity());
                    int MOVIE_ID_COL=0;
                    Uri movieUri;
                    switch (sortType){
                        case MovieContract.POPULARITY: {
                            MOVIE_ID_COL = currentData.getColumnIndex(MovieContract.MovieEntryByMostPopular._ID);
                            movieUri = MovieContract.MovieEntryByMostPopular.buildMovieUri(currentData.getInt(MOVIE_ID_COL));
                            detailsIntent.setData(movieUri);
                            detailsIntent.putExtra("fav", 0);
                            break;
                        }
                        case MovieContract.VOTE_AVERAGE: {
                            MOVIE_ID_COL = currentData.getColumnIndex(MovieContract.MovieEntryByHighestRated._ID);
                            movieUri = MovieContract.MovieEntryByHighestRated.buildMovieUri(currentData.getInt(MOVIE_ID_COL));
                            detailsIntent.setData(movieUri);
                            detailsIntent.putExtra("fav", 0);
                            break;
                        }
                    }
                    startActivity(detailsIntent);
                }
            }
        });

        return rootView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String sortOrderSetting = Utility.getSortTypeFromPreferences(getActivity());

        switch (sortOrderSetting){
            case MovieContract.POPULARITY:
                return new CursorLoader(getActivity(),
                        MovieContract.MovieEntryByMostPopular.CONTENT_URI,
                        new String[]{MovieContract.MovieEntryByMostPopular._ID, MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH},
                        null, null,
                        null);

            case MovieContract.VOTE_AVERAGE:
                return new CursorLoader(getActivity(),
                        MovieContract.MovieEntryByHighestRated.CONTENT_URI,
                        new String[]{MovieContract.MovieEntryByHighestRated._ID, MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH},
                        null, null,
                        null);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d("grid", "Cursor loaded, " + cursor.getCount() + " rows fetched");
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int movieIndex);
    }
}

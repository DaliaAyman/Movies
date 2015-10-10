package com.udacityproject.dalia.movies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacityproject.dalia.movies.data.MovieContract;

/**
 * Created by Dalia on 9/28/2015.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int DETAILS_LOADER = 0;
    static final String DETAIL_URI = "URI";
    public final static String POSITION = "position";

    private Uri mUri;

    TextView title, releaseDate, rating, overview; ImageView poster;

    private static final String[] MOVIE_MOST_POPULAR_COLUMNS = {
            MovieContract.MovieEntryByMostPopular.TABLE_NAME + "." + MovieContract.MovieEntryByMostPopular._ID,
            MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_KEY,
            MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_VOTE_AVERAGE,
            MovieContract.MovieEntryByMostPopular.COLUMN_MOVIE_RELEASE_DATE
    };
    private static final String[] MOVIE_HIGHEST_RATED_COLUMNS = {
            MovieContract.MovieEntryByHighestRated.TABLE_NAME + "." + MovieContract.MovieEntryByHighestRated._ID,
            MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_KEY,
            MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_VOTE_AVERAGE,
            MovieContract.MovieEntryByHighestRated.COLUMN_MOVIE_RELEASE_DATE
    };
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        title = (TextView)rootView.findViewById(R.id.movie_title);
        releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        rating = (TextView)rootView.findViewById(R.id.rating);
        poster = (ImageView)rootView.findViewById(R.id.poster);
        overview = (TextView)rootView.findViewById(R.id.overview);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAILS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri != null){
            String sortOrderSetting = Utility.getSortTypeFromPreferences(getActivity());
            switch (sortOrderSetting){
                case MovieContract.POPULARITY:
                    return new CursorLoader(getActivity(), mUri, MOVIE_MOST_POPULAR_COLUMNS, null, null, null);
                case MovieContract.VOTE_AVERAGE:
                    return new CursorLoader(getActivity(), mUri, MOVIE_HIGHEST_RATED_COLUMNS, null, null, null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            title.setText(data.getString(COL_MOVIE_TITLE));
            double ratingDouble = data.getDouble(COL_MOVIE_VOTE_AVERAGE);
            rating.setText(ratingDouble + "/10");
            overview.setText(data.getString(COL_MOVIE_OVERVIEW));
            releaseDate.setText(data.getString(COL_MOVIE_RELEASE_DATE));
            Picasso.with(getActivity().getApplicationContext()).load(
                    "http://image.tmdb.org/t/p/w342//" + data.getString(COL_MOVIE_POSTER_PATH))
                    .placeholder(R.drawable.movie_loading)
                    .resize(500,900)
                    .centerInside()
                    .into(poster);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

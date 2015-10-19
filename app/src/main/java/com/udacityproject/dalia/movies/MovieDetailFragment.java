package com.udacityproject.dalia.movies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.udacityproject.dalia.movies.adapters.MovieDetailsAdapter_Header;
import com.udacityproject.dalia.movies.adapters.MovieDetailsAdapter_Reviews;
import com.udacityproject.dalia.movies.adapters.MovieDetailsAdapter_Trailers;
import com.udacityproject.dalia.movies.data.MovieContract;
import com.udacityproject.dalia.movies.data.MovieProvider;

/**
 * Created by Dalia on 9/28/2015.
 */
public class MovieDetailFragment extends Fragment{

    static final String DETAIL_URI = "URI";
    static final String MOVIE_URI = "mURI";
    static final String IS_FAVORITE = "fav";
    public final static String POSITION = "position";

    private Uri mUri;
    MergeAdapter mergeAdapter;

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

    int isFavorite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
            isFavorite = arguments.getInt(MovieDetailFragment.MOVIE_URI);
            Log.d("grid", "isFavorite: " + isFavorite);
        }

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        mergeAdapter = new MergeAdapter();
        String movieId="";
        //String movieKey = MovieProvider.getMovieKeyFromUri(getActivity(), mUri, isFavorite);
        long movieKey=0;

        //get MovieHeader cursor
        Cursor cursor;
        String sortType = Utility.getSortTypeFromPreferences(getActivity());
        if (isFavorite == 1){
            movieId = String.valueOf(MovieContract.FavoriteMovie.getMovieIDFromUri(mUri));
            cursor = getActivity().getContentResolver().query(MovieContract.FavoriteMovie.CONTENT_URI,
                    null,
                    MovieContract.FavoriteMovie._ID + " = ?",
                    new String[]{movieId}, null);
            movieKey = MovieProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId), isFavorite);
            Log.d("grid", "fav movieKey: " + movieKey);
        }else {
            switch (sortType) {
                case MovieContract.POPULARITY: {
                    movieId = String.valueOf(MovieContract.MovieEntryByMostPopular.getMovieIDFromUri(mUri));
                    cursor = getActivity().getContentResolver().query(MovieContract.MovieEntryByMostPopular.CONTENT_URI,
                            null,
                            MovieContract.MovieEntryByMostPopular._ID + " = ?",
                            new String[]{movieId}, null);
                    // TODO
                    movieKey = MovieProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId), isFavorite);
                    break;
                }
                case MovieContract.VOTE_AVERAGE: {
                    movieId = String.valueOf(MovieContract.MovieEntryByHighestRated.getMovieIDFromUri(mUri));
                    cursor = getActivity().getContentResolver().query(MovieContract.MovieEntryByHighestRated.CONTENT_URI,
                            null,
                            MovieContract.MovieEntryByHighestRated._ID + " = ?",
                            new String[]{movieId}, null);
                    movieKey = MovieProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId), isFavorite);
                    break;
                }
                default:
                    cursor = null;
            }
        }
        ListView listView =(ListView)rootView.findViewById(R.id.list_view_fragment_detail);

        Log.d("grid", "movieKey = " + movieKey);
        Cursor trailerCursor = getActivity().getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                new String[]{MovieContract.TrailerEntry._ID, MovieContract.TrailerEntry.COLUMN_TRAILER_VIDEO_KEY, MovieContract.TrailerEntry.COLUMN_TRAILER_NAME},
                MovieContract.TrailerEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{Long.toString(movieKey)},null);

        Cursor reviewCursor = getActivity().getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                new String[]{MovieContract.ReviewEntry._ID, MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT},
                MovieContract.ReviewEntry.COLUMN_MOVIE_KEY + " = ?",
                new String[]{Long.toString(movieKey)}, null);

        mergeAdapter.addAdapter(new MovieDetailsAdapter_Header(getActivity(), cursor, 0));
        mergeAdapter.addView(LayoutInflater.from(getActivity()).inflate(R.layout.list_item_trailer_label, container, false));
        mergeAdapter.addAdapter(new MovieDetailsAdapter_Trailers(getActivity(), trailerCursor, 0));
        mergeAdapter.addView(LayoutInflater.from(getActivity()).inflate(R.layout.list_item_review_label, container, false));
        mergeAdapter.addAdapter(new MovieDetailsAdapter_Reviews(getActivity(), reviewCursor, 0));

        listView.setAdapter(mergeAdapter);
        Log.d("grid", "mergeAdapter set");
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

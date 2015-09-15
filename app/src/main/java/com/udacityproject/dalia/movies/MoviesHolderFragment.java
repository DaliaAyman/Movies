package com.udacityproject.dalia.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacityproject.dalia.movies.model.Movie;
import com.udacityproject.dalia.movies.model.MoviesGridViewAdapter;

import java.util.ArrayList;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MoviesHolderFragment extends Fragment {
    private MoviesGridViewAdapter adapter;
    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private GridView gridView;

    private FetchMoviesTask task;

    public MoviesHolderFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        updateMovies();
    }

    public void updateMovies(){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortType = sharedPrefs.getString(
                getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_key_most_popular_label));
        if(task != null){
            task.cancel(true);
        }
        task = new FetchMoviesTask(getActivity(), gridView);
        task.execute(sortType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        adapter = new MoviesGridViewAdapter(getActivity(), R.layout.movie_grid_item, moviesList);

        gridView = (GridView)rootView.findViewById(R.id.movies_grid);

        updateMovies();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("overview", movie.getOverview());
                intent.putExtra("poster_path", movie.getPosterPath());
                intent.putExtra("vote_average", movie.getVoteAverage());
                intent.putExtra("release_date", movie.getReleaseDate());
                startActivity(intent);
            }
        });

        return rootView;
    }
}

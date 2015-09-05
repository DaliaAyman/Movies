package com.udacityproject.dalia.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private CustomGridViewAdapter adapter;
        private ArrayList<Movie> moviesList = new ArrayList<Movie>();
        GridView gridView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);

            adapter = new CustomGridViewAdapter(getActivity(), R.layout.movie_grid_item, moviesList);

            gridView = (GridView)rootView.findViewById(R.id.movies_grid);
            //gridView.setAdapter(adapter);


            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType = sharedPrefs.getString(
                    getString(R.string.pref_sort_key_most_popular),
                    getString(R.string.pref_sort_key_high_rated));

            FetchMoviesTask task = new FetchMoviesTask(getActivity(), gridView);
            task.execute(sortType);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = (Movie)parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("title", movie.getTitle());
                    intent.putExtra("overview", movie.getOverview());
                    intent.putExtra("poster_path", movie.getPosterPath());
                    intent.putExtra("vote_average", movie.getVoteAverage());
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }
}

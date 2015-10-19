package com.udacityproject.dalia.movies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class FavoritesActivity extends ActionBarActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;
            if (savedInstanceState != null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new FavoritesFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else{
            mTwoPane = false;
        }
    }


}

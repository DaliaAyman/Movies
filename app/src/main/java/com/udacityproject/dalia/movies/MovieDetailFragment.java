package com.udacityproject.dalia.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacityproject.dalia.movies.model.MoviesGridViewAdapter;

/**
 * Created by Dalia on 9/28/2015.
 */
public class MovieDetailFragment extends Fragment{

    static MoviesGridViewAdapter mMovieGridDetails; // TODO : will be fixed when loaders are added
    public final static String POSITION = "position";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView title = (TextView)rootView.findViewById(R.id.movie_title);
        TextView releaseDate = (TextView)rootView.findViewById(R.id.release_date);
        TextView rating = (TextView)rootView.findViewById(R.id.rating);
        ImageView poster = (ImageView)rootView.findViewById(R.id.poster);
        TextView overview = (TextView)rootView.findViewById(R.id.overview);

        if(extras != null){
            title.setText(intent.getStringExtra("title"));
            double ratingDouble = intent.getDoubleExtra("vote_average", 0);
            rating.setText(ratingDouble + "/10");
            overview.setText(intent.getStringExtra("overview"));
            releaseDate.setText(intent.getStringExtra("release_date"));
            Picasso.with(getActivity().getApplicationContext()).load(
                    "http://image.tmdb.org/t/p/w342//" + intent.getStringExtra("poster_path"))
                    .placeholder(R.drawable.movie_loading)
                    .resize(500,900)
                    .centerInside()
                    .into(poster);
        }

        return rootView;
    }
}

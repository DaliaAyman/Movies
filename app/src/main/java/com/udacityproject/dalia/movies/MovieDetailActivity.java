package com.udacityproject.dalia.movies;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();

        TextView title = (TextView)findViewById(R.id.movie_title);
        TextView releaseDate = (TextView)findViewById(R.id.release_date);
        TextView rating = (TextView)findViewById(R.id.rating);
        ImageView poster = (ImageView)findViewById(R.id.poster);
        TextView overview = (TextView)findViewById(R.id.overview);

        title.setText(intent.getStringExtra("title"));
        double ratingDouble = intent.getDoubleExtra("vote_average", 0);
        rating.setText(ratingDouble + "/10");
        overview.setText(intent.getStringExtra("overview"));
        releaseDate.setText(intent.getStringExtra("release_date"));
        Picasso.with(getApplicationContext()).load(
                "http://image.tmdb.org/t/p/w342//" + intent.getStringExtra("poster_path"))
                .placeholder(R.drawable.movie_loading)
                .resize(500,900)
                .centerInside()
                .into(poster);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

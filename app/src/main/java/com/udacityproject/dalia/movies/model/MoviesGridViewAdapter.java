package com.udacityproject.dalia.movies.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacityproject.dalia.movies.R;

import java.util.ArrayList;

/**
 * Created by Dalia on 8/25/2015.
 */
public class MoviesGridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

    int width;
    int height;

    public MoviesGridViewAdapter(Context context, int resource, ArrayList<Movie> arrayList) {
        super(context, resource, arrayList);

        this.layoutResourceId = resource;
        this.context = context;
        movieArrayList = arrayList;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_image);
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185//" + movieArrayList.get(position).getPosterPath())
                    .placeholder(R.drawable.movie_loading)
                    .resize(width/2, height/2)
                    .centerCrop()

                    .into(imageView);

//        }else{
//
//        }


        return convertView;
    }
}

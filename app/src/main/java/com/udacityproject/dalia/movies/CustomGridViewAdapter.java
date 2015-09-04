package com.udacityproject.dalia.movies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dalia on 8/25/2015.
 */
public class CustomGridViewAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<Movie> movieArrayList = new ArrayList<Movie>();

    int width;
    int height;

    public CustomGridViewAdapter(Context context, int resource, ArrayList<Movie> arrayList) {
        super(context, resource, arrayList);

        this.layoutResourceId = resource;
        this.context = context;
        movieArrayList = arrayList;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("grid", "start of getView");
        //if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, null);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_image);
            Picasso.with(context).load("http://image.tmdb.org/t/p/w185//" + movieArrayList.get(position).getPosterPath())
                    //.placeholder()
                    .resize(width/2, height/2)
                    .centerCrop()

                    .into(imageView);

//        }else{
//
//        }


        return convertView;
    }
}

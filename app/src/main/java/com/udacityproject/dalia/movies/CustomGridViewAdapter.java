package com.udacityproject.dalia.movies;

import android.app.Activity;
import android.content.Context;
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
    ArrayList<String> imagesUrls = new ArrayList<String>();

    public CustomGridViewAdapter(Context context, int resource) {
        super(context, resource);

        this.layoutResourceId = resource;
        this.context = context;
    }

    public void setImagesUrlsArrayList(ArrayList<String> imagesUrls) {
        this.imagesUrls = imagesUrls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //convertView = inflater.inflate(R.)
            convertView = inflater.inflate(R.layout.movie_grid_item, null);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_image);
            //imageView.setImageResource(images.get(position));

            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + imagesUrls.get(position)).into(imageView);

        }else{

        }


        return convertView;
    }
}

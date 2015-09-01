package com.udacityproject.dalia.movies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Dalia on 8/25/2015.
 */
public class CustomGridViewAdapter extends ArrayAdapter {
    Context context;
    int layoutResourceId;
    ArrayList<Integer> images = new ArrayList<Integer>();

    public CustomGridViewAdapter(Context context, int resource, ArrayList objects) {
        super(context, resource, objects);

        this.layoutResourceId = resource;
        this.context = context;
        this.images = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //convertView = inflater.inflate(R.)
            convertView = inflater.inflate(R.layout.movie_grid_item, null);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_image);
            imageView.setImageResource(images.get(position));

        }else{

        }


        return convertView;
    }
}

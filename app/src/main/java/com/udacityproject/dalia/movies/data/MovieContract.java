package com.udacityproject.dalia.movies.data;

import android.net.Uri;

/**
 * Created by Dalia on 9/15/2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.udacityproject.dalia.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


}

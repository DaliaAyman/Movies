package com.udacityproject.dalia.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Dalia on 9/19/2015.
 */
public class Utility {
    public static String getSortTypeFromPreferences(Context context){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        String sortType = sharedPrefs.getString(
                context.getString(R.string.pref_sort_by_key),
                context.getString(R.string.pref_sort_key_most_popular));

        return sortType;
    }
}

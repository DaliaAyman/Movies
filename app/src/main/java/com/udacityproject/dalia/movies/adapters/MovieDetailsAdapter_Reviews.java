package com.udacityproject.dalia.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacityproject.dalia.movies.R;
import com.udacityproject.dalia.movies.data.MovieContract;

/**
 * Created by Dalia on 10/10/2015.
 */
public class MovieDetailsAdapter_Reviews extends CursorAdapter {

    public MovieDetailsAdapter_Reviews(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("grid", "newView Reviews");
        return LayoutInflater.from(context).inflate(R.layout.list_item_review, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("grid", "bindView Reviews");
        TextView author = (TextView)view.findViewById(R.id.review_author);
        TextView content = (TextView)view.findViewById(R.id.review_content);

        int authorColumn = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR);
        String authorString = cursor.getString(authorColumn);
        author.setText(authorString);

        int contentColumn = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT);
        String contentString = cursor.getString(contentColumn);
        content.setText(contentString);
    }
}

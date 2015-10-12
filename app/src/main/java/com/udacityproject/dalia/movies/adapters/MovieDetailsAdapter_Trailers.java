package com.udacityproject.dalia.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacityproject.dalia.movies.R;
import com.udacityproject.dalia.movies.data.MovieContract;

/**
 * Created by Dalia on 10/11/2015.
 */
public class MovieDetailsAdapter_Trailers extends CursorAdapter {
    public MovieDetailsAdapter_Trailers(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_trailer, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView name = (TextView)view.findViewById(R.id.trailer_name);

        int nameColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME);
        String nameString = cursor.getString(nameColumn);
        name.setText(nameString);

        //TODO intent to youtube link
        int linkColumn = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_VIDEO_KEY);
        final String keyString = cursor.getString(linkColumn);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + keyString));
                context.startActivity(intent);
            }
        });
    }
}

package com.rizqi.aryansa.favoritemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rizqi.aryansa.favoritemovie.BuildConfig;
import com.rizqi.aryansa.favoritemovie.R;
import com.rizqi.aryansa.favoritemovie.model.MovieItems;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rizqi.aryansa.favoritemovie.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.rizqi.aryansa.favoritemovie.database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.rizqi.aryansa.favoritemovie.database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.rizqi.aryansa.favoritemovie.database.DatabaseContract.MovieColumns.TITLE;
import static com.rizqi.aryansa.favoritemovie.database.DatabaseContract.getColumnString;

/**
 * Created by RizqiAryansa on 1/22/2018.
 */

public class FavoriteAdapter extends CursorAdapter {

    Context context;
    public FavoriteAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_movie, parent,
                false);
        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if(cursor != null) {
            TextView tvTitle = (TextView)view.findViewById(R.id.tv_item_title);
            TextView tvDate = (TextView)view.findViewById(R.id.tv_item_date);
            TextView tvDescription = (TextView)view.findViewById(R.id.tv_item_description);
            ImageView imagePoster = (ImageView)view.findViewById(R.id.img_poster);

            String url = BuildConfig.POSTER_URL + "w342/" + getColumnString(cursor,POSTER_PATH);
            Glide.with(context)
                    .load(url)
                    .override(342, 342)
                    .crossFade()
                    .into(imagePoster);

            tvTitle.setText(getColumnString(cursor,TITLE));
            tvDescription.setText(getColumnString(cursor,OVERVIEW));
            tvDate.setText(getColumnString(cursor,RELEASE_DATE));
        }
    }
}


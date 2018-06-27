package com.rizqi.aryansa.cataloguemovie.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.rizqi.aryansa.cataloguemovie.BuildConfig;
import com.rizqi.aryansa.cataloguemovie.Database.MovieHelper;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by RizqiAryansa on 1/23/2018.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<MovieItems> widgetItems = new ArrayList<>();
    private Context context;
    private MovieHelper movieHelper;
    private int mAppWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        movieHelper = new MovieHelper(context);
        movieHelper.open();
        widgetItems.addAll(movieHelper.getDataAll());
    }

    @Override
    public void onDestroy() {
        if (movieHelper != null) {
            movieHelper.close();
        }
    }

    @Override
    public int getCount() {
        return widgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget_movie);

        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .load(BuildConfig.POSTER_URL + "w342/" +
                            widgetItems.get(position).getPoster_path())
                    .asBitmap()
                    .error(new ColorDrawable(context.getResources().getColor(R.color.colorAccent)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }

        rv.setImageViewBitmap(R.id.imageView, bmp);

        Bundle extras = new Bundle();
        extras.putInt(StackWidgetMovie.EXTRA_ITEM, position);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

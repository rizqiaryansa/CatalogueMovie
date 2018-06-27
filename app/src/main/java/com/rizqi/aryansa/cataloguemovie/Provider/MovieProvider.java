package com.rizqi.aryansa.cataloguemovie.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.rizqi.aryansa.cataloguemovie.Database.MovieHelper;

import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.AUTHORITY;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.CONTENT_URI;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.TABLE_MOVIE;

/**
 * Created by RizqiAryansa on 1/21/2018.
 */

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_MOVIE+ "/#",
                MOVIE_ID);
    }

    private MovieHelper movieHelper;

    @Override
    public boolean onCreate() {
        movieHelper = new MovieHelper(getContext());
        movieHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            case MOVIE:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        long added ;

        switch (sUriMatcher.match(uri)){
            case MOVIE:
                added = movieHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated ;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated =  movieHelper.updateProvider(uri.getLastPathSegment(),values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted =  movieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    /**
     * Created by RizqiAryansa on 1/22/2018.
     */

    public static class CustomOnItemClickListener implements View.OnClickListener {

        private int position;
        private OnItemClickCallBack onItemClickCallBack;

        public CustomOnItemClickListener(int position,
                                         OnItemClickCallBack onItemClickCallBack) {
            this.position = position;
            this.onItemClickCallBack = onItemClickCallBack;
        }

        @Override
        public void onClick(View view) {
            onItemClickCallBack.onItemClicked(view, position);
        }

        public interface OnItemClickCallBack {
            void onItemClicked(View view, int position);
        }
    }
}

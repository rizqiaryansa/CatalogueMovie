package com.rizqi.aryansa.cataloguemovie.Database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by RizqiAryansa on 1/21/2018.
 */

public class DatabaseContract {
    public static String TABLE_MOVIE = "movies";

    public static final class MovieColumns implements BaseColumns {

        public static String TITLE = "title";
        public static String POSTER_PATH = "poster_path";
        public static String RELEASE_DATE = "release_date";
        public static String OVERVIEW = "overview";
        public static String VOTE_AVERAGE = "vote_average";
        public static String BACKDROP_PATH = "backdrop_path";

    }

    public static final String AUTHORITY = "com.rizqi.aryansa.cataloguemovie";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }
}

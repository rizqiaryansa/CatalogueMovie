package com.rizqi.aryansa.cataloguemovie.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.TITLE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.TABLE_MOVIE;

/**
 * Created by RizqiAryansa on 1/21/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovies";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            TABLE_MOVIE,
            _ID,
            TITLE,
            POSTER_PATH,
            RELEASE_DATE,
            OVERVIEW,
            VOTE_AVERAGE,
            BACKDROP_PATH
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MOVIE);
        onCreate(db);
    }
}

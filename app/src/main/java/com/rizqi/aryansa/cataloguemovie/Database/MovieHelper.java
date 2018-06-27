package com.rizqi.aryansa.cataloguemovie.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.CONTENT_URI;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.TITLE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.TABLE_MOVIE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.getColumnInt;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.getColumnString;

/**
 * Created by RizqiAryansa on 1/21/2018.
 */

public class MovieHelper {

    private static String DATABASE_TABLE = TABLE_MOVIE;
    private Context context;
    private DatabaseHelper dataBasehelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dataBasehelper = new DatabaseHelper(context);
        database = dataBasehelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBasehelper.close();
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }

    public ArrayList<MovieItems> getDataAll(){

        ArrayList<MovieItems> arrayList = new ArrayList<>();
        MovieItems movieItems;
        Cursor cursor = queryProvider();
        
        cursor.moveToFirst();

        if (cursor.getCount()>0){
            do {
                movieItems = new MovieItems();
                movieItems.setId(getColumnInt(cursor, _ID));
                movieItems.setTitle(getColumnString(cursor, TITLE));
                movieItems.setPoster_path(getColumnString(cursor, POSTER_PATH));
                movieItems.setRelease_date(getColumnString(cursor, RELEASE_DATE));
                movieItems.setOverview(getColumnString(cursor, OVERVIEW));
                movieItems.setVote_average(getColumnString(cursor, VOTE_AVERAGE));
                movieItems.setBackdrop_path(getColumnString(cursor, BACKDROP_PATH));

                arrayList.add(movieItems);

                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }

    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,_ID + " = ?", new String[]{id});
    }
}

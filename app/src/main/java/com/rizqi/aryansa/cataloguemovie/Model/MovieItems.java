package com.rizqi.aryansa.cataloguemovie.Model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.TITLE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.VOTE_AVERAGE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.getColumnInt;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.getColumnString;

/**
 * Created by RizqiAryansa on 1/11/2018.
 */

public class MovieItems implements Parcelable {
    private int id;
    private String title;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private String overview;
    private String vote_average;

    public MovieItems() {}

    public MovieItems(JSONObject object) {
        try {

            int id = object.getInt("id");

            String title = object.getString("original_title");
            String poster_path = object.getString("poster_path");
            String release_date = object.getString("release_date");
            String overview = object.getString("overview");
            String vote_average = object.getString("vote_average");
            String backdrop_path = object.getString("backdrop_path");

            this.id = id;
            this.title = title;
            this.poster_path = poster_path;
            this.release_date = release_date;
            this.overview = overview;
            this.vote_average = vote_average;
            this.backdrop_path = backdrop_path;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MovieItems(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.poster_path = getColumnString(cursor, POSTER_PATH);
        this.release_date = getColumnString(cursor, RELEASE_DATE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.vote_average = getColumnString(cursor, VOTE_AVERAGE);
        this.backdrop_path = getColumnString(cursor, BACKDROP_PATH);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.release_date);
        dest.writeString(this.overview);
        dest.writeString(this.vote_average);
    }

    protected MovieItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.poster_path = in.readString();
        this.backdrop_path = in.readString();
        this.release_date = in.readString();
        this.overview = in.readString();
        this.vote_average = in.readString();
    }

    public static final Parcelable.Creator<MovieItems> CREATOR = new Parcelable.Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel source) {
            return new MovieItems(source);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };
}

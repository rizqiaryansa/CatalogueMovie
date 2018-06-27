package com.rizqi.aryansa.cataloguemovie.Connection;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.rizqi.aryansa.cataloguemovie.BuildConfig;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by RizqiAryansa on 1/11/2018.
 */

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private ArrayList<MovieItems> mData;
    private boolean mHasResult = false;

    private String mKumpulanMovie;
    private String mUnikQuery;


    public MovieAsyncTaskLoader(final Context context, String kumpulanMovie,
                                String unikQuery) {
        super(context);

        onContentChanged();
        this.mKumpulanMovie = kumpulanMovie;
        this.mUnikQuery = unikQuery;
    }

    @Override
    protected void onStartLoading() {
        Log.d("Content Changed","1");
        if(takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mData);
            mData = null;
            mHasResult = false;
        }
    }


    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> movieItemsload = new ArrayList<>();

        String url = "";
        if(mUnikQuery == "search") {
            url = BuildConfig.BASE_URL+ "3/search/movie?api_key=" +
                    BuildConfig.API_KEY + "&language=en-US&query=" + mKumpulanMovie;
        } else if(mUnikQuery == "nowplaying") {
            url = BuildConfig.BASE_URL + "3/movie/now_playing?api_key=" +
                    BuildConfig.API_KEY + "&language=en-US";
        } else if(mUnikQuery == "upcoming") {
            url = BuildConfig.BASE_URL + "3/movie/upcoming?api_key=" +
                    BuildConfig.API_KEY + "&language=en-US";
        }

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject object = new JSONObject(result);
                    JSONArray list = object.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject mMovies = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(mMovies);
                        movieItemsload.add(movieItems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieItemsload;
    }

    protected void onReleaseResources(ArrayList<MovieItems> data) {

    }
}

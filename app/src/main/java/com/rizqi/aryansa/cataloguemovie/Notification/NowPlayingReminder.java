package com.rizqi.aryansa.cataloguemovie.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.rizqi.aryansa.cataloguemovie.Activity.DetailActivity;
import com.rizqi.aryansa.cataloguemovie.BuildConfig;
import com.rizqi.aryansa.cataloguemovie.Fragment.FragmentMovieDetail;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.CONTENT_URI;

/**
 * Created by RizqiAryansa on 1/23/2018.
 */

public class NowPlayingReminder extends GcmTaskService {

    public static final String TAG = "GetNowPlaying";
    public static String TAG_TASK_NOWPLAYING_LOG = "NowPlayingTask";
    public static int notifId = 100;

    final ArrayList<MovieItems> movieItemsload = new ArrayList<>();

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_NOWPLAYING_LOG)){
            getNowPlayingMovie();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getNowPlayingMovie() {

        SyncHttpClient client = new SyncHttpClient();
        String url = BuildConfig.BASE_URL + "3/movie/now_playing?api_key=" +
                BuildConfig.API_KEY + "&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {

                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    int listSize;
                    listSize = list.length();
                    Random random = new Random();
                    int randomJsonIndex = random.nextInt(listSize);

                    JSONObject movie = list.getJSONObject(randomJsonIndex);
                    MovieItems movieItems = new MovieItems(movie);
                    movieItemsload.add(movieItems);

                    int movie_id = movieItemsload.get(0).getId();
                    String title = movieItemsload.get(0).getTitle();
                    String message = getString(R.string.now_playing_day) + " " + title + " " +
                            getString(R.string.release);

                    showNotification(getApplicationContext(), movie_id, title, message, notifId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "Failed");
            }
        });
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        NowPlayingMovieTask mNowPlayingTask = new NowPlayingMovieTask(this);
        mNowPlayingTask.createPeriodicTask();
    }

    private void showNotification(Context context, int id, String title, String message,
                                  int notifId) {

        Intent action = new Intent(this, DetailActivity.class);

        MovieItems mItems = new MovieItems();
        mItems.setId(movieItemsload.get(0).getId());
        mItems.setTitle(movieItemsload.get(0).getTitle());
        mItems.setPoster_path(movieItemsload.get(0).getPoster_path());
        mItems.setRelease_date(movieItemsload.get(0).getRelease_date());
        mItems.setOverview(movieItemsload.get(0).getOverview());
        mItems.setVote_average(movieItemsload.get(0).getVote_average());
        mItems.setBackdrop_path(movieItemsload.get(0).getBackdrop_path());

        action.putExtra(FragmentMovieDetail.EXTRA_PARCEL, mItems);

        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification movie = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_alarm_white)
                .setContentIntent(operation)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true)
                .build();

        manager.notify(id, movie);
    }

    public static class NowPlayingMovieTask {

        private GcmNetworkManager mGcmNetworkManager;

        public NowPlayingMovieTask(Context context) {
            mGcmNetworkManager = GcmNetworkManager.getInstance(context);
        }

        public void createPeriodicTask() {
            Task periodicTask = new PeriodicTask.Builder()
                    .setService(NowPlayingReminder.class)
                    .setPeriod(60)
                    .setFlex(10)
                    .setTag(NowPlayingReminder.TAG_TASK_NOWPLAYING_LOG)
                    .setPersisted(true)
                    .build();

            mGcmNetworkManager.schedule(periodicTask);
            Log.d("TAG", "createPeriodicTask: 1");
        }

    }
}

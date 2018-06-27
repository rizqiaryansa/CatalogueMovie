package com.rizqi.aryansa.cataloguemovie.Notification;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.rizqi.aryansa.cataloguemovie.Activity.DetailActivity;
import com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract;
import com.rizqi.aryansa.cataloguemovie.Fragment.FragmentMovieDetail;
import com.rizqi.aryansa.cataloguemovie.R;

import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.TITLE;

/**
 * Created by RizqiAryansa on 1/23/2018.
 */

public class DailyReminderMovie extends IntentService {

    private static final String TAG = DailyReminderMovie.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;
    public static String EXTRA_ID = "extra_id";

    public static PendingIntent getReminerPendingIntent(Context context, Uri uri) {

        Intent action = new Intent(context, DailyReminderMovie.class);
        action.setData(uri);

        return PendingIntent.getService(context, 0, action,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public DailyReminderMovie() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Display a notification to view the task details
        Intent action = new Intent(this, DetailActivity.class);

        action.putExtra(FragmentMovieDetail.EXTRA_FAVO, uri);

        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task overview
        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);
        String title = "";
        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = DatabaseContract.getColumnString(cursor, OVERVIEW);
                title = DatabaseContract.getColumnString(cursor, TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification movie = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_alarm_white)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, movie);
    }

    public static void setDailyReminder(Context context, Uri uri, long time, int id) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, DailyReminderMovie.class);
        intent.setData(uri);
        intent.putExtra(EXTRA_ID, id);

        PendingIntent pendingIntent = getReminerPendingIntent(context, uri);

        alarmManager.setExact(AlarmManager.RTC, time, pendingIntent);


    }
}

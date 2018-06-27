package com.rizqi.aryansa.cataloguemovie.Fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rizqi.aryansa.cataloguemovie.Activity.DetailActivity;
import com.rizqi.aryansa.cataloguemovie.BuildConfig;
import com.rizqi.aryansa.cataloguemovie.Database.MovieHelper;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.Notification.DailyReminderMovie;
import com.rizqi.aryansa.cataloguemovie.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.CONTENT_URI;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.BACKDROP_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.POSTER_PATH;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.TITLE;
import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.MovieColumns.VOTE_AVERAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMovieDetail extends Fragment {

    @BindView(R.id.movie_title)
    TextView tvTitle;
    @BindView(R.id.movie_overview)
    TextView tvOverview;
    @BindView(R.id.movie_release_date)
    TextView tvReleasedate;
    @BindView(R.id.movie_user_rating)
    TextView tvRating;

    @BindView(R.id.movie_poster)
    ImageView imgPoster;

    @BindView(R.id.remove_from_favorite)
    ImageView mRemoveFavorite;

    @BindView(R.id.mark_as_favorite)
    ImageView mAddFavorite;

    ImageView imgBackdrop;

    public static final String EXTRA_PARCEL = "EXTRA_PARCEL";
    public static final String EXTRA_FAVO = "EXTRA_FAVO";

    MovieItems mMovie;

    @BindViews({R.id.rating_first_star, R.id.rating_second_star, R.id.rating_third_star,
            R.id.rating_fourth_star, R.id.rating_fifth_star})
    List<ImageView> ratingStar;

    private MovieItems movieItems;
    private MovieHelper movieHelper;

    boolean isTrueFavo, isTrueApi = false;

    Uri uri;

    public FragmentMovieDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(EXTRA_PARCEL)) {
            mMovie = getArguments().getParcelable(EXTRA_PARCEL);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                getActivity().findViewById(R.id.toolbar_layout);

        if (appBarLayout != null && getActivity() instanceof DetailActivity) {
            if (mMovie != null) {
                appBarLayout.setTitle(mMovie.getTitle());
            }

            if (movieItems != null) {
                appBarLayout.setTitle(movieItems.getTitle());
            }
        }

        imgBackdrop = ((ImageView) getActivity().findViewById(R.id.movie_backdrop));

        if (imgBackdrop != null && mMovie != null) {
            String url = BuildConfig.BACKDROP_URL
                    + mMovie.getBackdrop_path();
            Picasso.with(getActivity())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .into(imgBackdrop);

        }

        if (movieItems != null) {
            String url = BuildConfig.BACKDROP_URL
                    + movieItems.getBackdrop_path();
            Picasso.with(getActivity())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .into(imgBackdrop);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, v);

        //API MOVIE
        loadApiMovie();

        //Untuk Favorite Movie
        loadFavoMovie();

        updateFavorite();

        return v;
    }

    private void addFavorite() {
        if (!isFavoriteApi() && mMovie != null) {
            ContentValues values = new ContentValues();
            values.put(_ID, mMovie.getId());
            values.put(TITLE, mMovie.getTitle());
            values.put(POSTER_PATH, mMovie.getPoster_path());
            values.put(RELEASE_DATE, mMovie.getRelease_date());
            values.put(OVERVIEW, mMovie.getOverview());
            values.put(VOTE_AVERAGE, mMovie.getVote_average());
            values.put(BACKDROP_PATH, mMovie.getBackdrop_path());
            getContext().getContentResolver().insert(CONTENT_URI,
                    values);
        } else if (!isFavoriteProvider() && movieItems != null) {
            ContentValues values = new ContentValues();
            values.put(_ID, movieItems.getId());
            values.put(TITLE, movieItems.getTitle());
            values.put(POSTER_PATH, movieItems.getPoster_path());
            values.put(RELEASE_DATE, movieItems.getRelease_date());
            values.put(OVERVIEW, movieItems.getOverview());
            values.put(VOTE_AVERAGE, movieItems.getVote_average());
            values.put(BACKDROP_PATH, movieItems.getBackdrop_path());
            getContext().getContentResolver().insert(CONTENT_URI,
                    values);
        }
    }

    private void removeFavorite() {

        if (mMovie != null && movieItems == null) {
            getContext().getContentResolver().delete(CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(mMovie.getId())).build(),
                    null, null);
        } else if (mMovie == null && movieItems != null) {
            getContext().getContentResolver().delete(uri,
                    null, null);
        }
    }

    private void checkShowFavo() {
        if (isFavoriteProvider() || isFavoriteApi()) {
            mRemoveFavorite.setVisibility(View.VISIBLE);
            mAddFavorite.setVisibility(View.GONE);
        } else {
            mAddFavorite.setVisibility(View.VISIBLE);
            mRemoveFavorite.setVisibility(View.GONE);
        }
    }

    private void updateFavorite() {

        checkShowFavo();

        mAddFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFavorite();
                        checkShowFavo();
                    }
                }
        );

        mRemoveFavorite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFavorite();
                        checkShowFavo();
                    }
                });
    }

    private boolean isFavoriteApi() {

        if(mMovie != null) {

            Cursor movieCursor = getContext().getContentResolver().query(
                    CONTENT_URI.buildUpon().appendPath(String.valueOf(mMovie.getId())).build(),
                    null,
                    null,
                    null,
                    null);


            Log.e("cekcursor", String.valueOf(movieCursor.getCount()));

            if (movieCursor != null && movieCursor.getCount() > 0) {
                movieCursor.close();
                isTrueApi = true;
            } else {
                isTrueApi = false;
            }
        }

        return isTrueApi;
    }


    private boolean isFavoriteProvider() {

        if(uri != null) {

            Cursor movieCursor = getContext().getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    null);

            Log.e("cekuri", String.valueOf(movieCursor));

            if (movieCursor != null && movieCursor.moveToFirst()) {
                movieCursor.close();
                isTrueFavo = true;
            } else {
                isTrueFavo = false;
            }
        }
        return isTrueFavo;
    }

    private void loadFavoMovie() {
        movieHelper = new MovieHelper(getActivity());
        movieHelper.open();

        uri = Uri.parse(String.valueOf(getArguments().getParcelable(EXTRA_FAVO)));
        if (uri != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri,
                    null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movieItems = new MovieItems(cursor);
                cursor.close();
            }
        }

        if (movieItems != null) {
            String url = BuildConfig.POSTER_URL + "w342/" +
                    movieItems.getPoster_path();

            tvTitle.setText(movieItems.getTitle());
            tvOverview.setText(movieItems.getOverview());

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = dateformat.parse(movieItems.getRelease_date());
                DateFormat dayFormate = new SimpleDateFormat("EEEE, MMM d, yyyy");
                String dayFromDate = dayFormate.format(date);
                tvReleasedate.setText(getResources().getString(R.string.release_date) + " "
                        + dayFromDate);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Glide.with(getContext())
                    .load(url)
                    .crossFade()
                    .into(imgPoster);

            ratingBar();

        }
    }

    private void loadApiMovie() {
        if (mMovie != null) {
            String url = BuildConfig.POSTER_URL + "w342/" +
                    mMovie.getPoster_path();

            tvTitle.setText(mMovie.getTitle());
            tvOverview.setText(mMovie.getOverview());

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = dateformat.parse(mMovie.getRelease_date());
                DateFormat dayFormate = new SimpleDateFormat("EEEE, MMM d, yyyy");
                String dayFromDate = dayFormate.format(date);
                tvReleasedate.setText(getResources().getString(R.string.release_date) + " "
                        + dayFromDate);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Glide.with(getContext())
                    .load(url)
                    .crossFade()
                    .into(imgPoster);

            ratingBar();

        }
    }

    private void ratingBar() {

        if (mMovie != null) {
            if (mMovie.getVote_average() != null && !mMovie.getVote_average().isEmpty()) {
                String userRatingStr = getResources().getString(R.string.user_rating_movie,
                        mMovie.getVote_average());
                tvRating.setText(userRatingStr);

                float userRating = Float.valueOf(mMovie.getVote_average()) / 2;
                int integerPart = (int) userRating;

                for (int i = 0; i < integerPart; i++) {
                    ratingStar.get(i).setImageResource(R.drawable.ic_star_black_24dp);
                }

                if (Math.round(userRating) > integerPart) {
                    ratingStar.get(integerPart).setImageResource(
                            R.drawable.ic_star_half_black_24dp);
                }

            } else {
                tvRating.setVisibility(View.GONE);
            }

        } else if (movieItems != null) {
            if (movieItems.getVote_average() != null && !movieItems.getVote_average().isEmpty()) {
                String userRatingStr = getResources().getString(R.string.user_rating_movie,
                        movieItems.getVote_average());
                tvRating.setText(userRatingStr);

                float userRating = Float.valueOf(movieItems.getVote_average()) / 2;
                int integerPart = (int) userRating;

                for (int i = 0; i < integerPart; i++) {
                    ratingStar.get(i).setImageResource(R.drawable.ic_star_black_24dp);
                }

                if (Math.round(userRating) > integerPart) {
                    ratingStar.get(integerPart).setImageResource(
                            R.drawable.ic_star_half_black_24dp);
                }

            } else {
                tvRating.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_daily_reminder, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_daily_reminder) {

            String timeArray[] = "15:38".split(":");

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            c.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
            c.set(Calendar.SECOND, 0);

            String getTitle = "";
            int id = 0;
            if(mMovie != null) {
                getTitle = mMovie.getTitle();
                uri = CONTENT_URI.buildUpon().appendPath(String.valueOf(mMovie.getId())).build();
                id = mMovie.getId();
            } else if(movieItems != null) {
                getTitle = movieItems.getTitle();
                uri = Uri.parse(String.valueOf(getArguments().getParcelable(EXTRA_FAVO)));
                id = movieItems.getId();
            }

            showToast("Set Alarm "
                    + getTitle + " Success");

            DailyReminderMovie.setDailyReminder(getActivity(), uri, c.getTimeInMillis(), id);

        }

        return true;
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}

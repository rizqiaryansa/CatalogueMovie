package com.rizqi.aryansa.cataloguemovie.Fragment;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rizqi.aryansa.cataloguemovie.Adapter.FavoriteAdapter;
import com.rizqi.aryansa.cataloguemovie.Preference.AppPreference;
import com.rizqi.aryansa.cataloguemovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.ly_result)
    LinearLayout lyResult;
    FavoriteAdapter adapter;

    private Cursor list;

    static final String FAVO_MOVIES = "FAVO_MOVIES";
    static final String EXTRA_FAVO = "EXTRA_FAVO";

    AppPreference appPreference;
    boolean isRun;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new LoadMoviesFavoAsync().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, v);

        showRecyclerMovie();

        return v;
    }

    private void showRecyclerMovie() {
        rvMovie.setHasFixedSize(true);
        adapter = new FavoriteAdapter(getActivity());

        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(adapter);
    }

    private void showPreload() {
        progressBar.setVisibility(View.VISIBLE);
        rvMovie.setVisibility(View.GONE);
        lyResult.setVisibility(View.GONE);
    }

    private void showFinished() {
        progressBar.setVisibility(View.GONE);
        rvMovie.setVisibility(View.VISIBLE);
        lyResult.setVisibility(View.VISIBLE);
        tvResult.setText(getResources().getString(R.string.movies_favorite));
    }

    private class LoadMoviesFavoAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            appPreference = new AppPreference(getActivity());
            showPreload();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {

            isRun = appPreference.getFirstRun();

            if(isRun) {
                list = getActivity().getContentResolver()
                        .query(CONTENT_URI, null, null,
                                null, null);
            }

            return list;
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            showFinished();

            list = movies;
            adapter.setListFavoMovie(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }
    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvMovie, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.notifyDataSetChanged();
    }
}

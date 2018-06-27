package com.rizqi.aryansa.cataloguemovie.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rizqi.aryansa.cataloguemovie.Adapter.RecyclerMovieAdapter;
import com.rizqi.aryansa.cataloguemovie.Connection.MovieAsyncTaskLoader;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    @BindView(R.id.rv_movie)
    RecyclerView recyclerView;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ly_result)
    LinearLayout lyResult;
    RecyclerMovieAdapter adapter;
    String unikQuery = "upcoming";

    static final String UPCOMING_MOVIES = "UPCOMING_MOVIES";
    static final String EXTRA_UPCOMING = "EXTRA_UPCOMING";

    public UpComingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            loadSavedData(savedInstanceState);
        }
    }

    private void loadSavedData(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            unikQuery = savedInstanceState.getString(UPCOMING_MOVIES);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(UPCOMING_MOVIES, unikQuery);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_UPCOMING, unikQuery);

        getLoaderManager().initLoader(0, bundle, this);
        showPreload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_coming, container, false);
        ButterKnife.bind(this, view);
        showRecyclerMovie();
        return view;
    }

    private void showRecyclerMovie() {
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerMovieAdapter(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void showPreload() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lyResult.setVisibility(View.GONE);
    }

    private void showFinished() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(unikQuery != null && !TextUtils.isEmpty(unikQuery)) {
            lyResult.setVisibility(View.VISIBLE);
            tvResult.setText(getResources().getString(R.string.movies_upcoming));
        }
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            unikQuery = args.getString(EXTRA_UPCOMING);
        }

        return new MovieAsyncTaskLoader(getActivity(), "", unikQuery);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        adapter.setMovie(data);
        adapter.notifyDataSetChanged();
        showFinished();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setMovie(null);
    }
}

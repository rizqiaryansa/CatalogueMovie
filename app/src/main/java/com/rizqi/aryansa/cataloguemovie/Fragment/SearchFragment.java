package com.rizqi.aryansa.cataloguemovie.Fragment;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.rizqi.aryansa.cataloguemovie.Adapter.RecyclerMovieAdapter;
import com.rizqi.aryansa.cataloguemovie.Connection.MovieAsyncTaskLoader;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {

    @BindView(R.id.rv_movie)
    RecyclerView recyclerView;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ly_result)
    LinearLayout lyResult;
    @BindView(R.id.edt_search_title)
    EditText edtTitle;
    @BindView(R.id.btn_search)
    Button btn_search;
    RecyclerMovieAdapter adapter;
    String kumpulanMovie;

    static final String KUMPULAN_MOVIES = "KUMPULAN_MOVIES";
    static final String EXTRA_MOVIES = "EXTRA_MOVIES";

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            loadSavedData(savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String titleMovie = edtTitle.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MOVIES, titleMovie);

        getLoaderManager().initLoader(0, bundle, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        showRecyclerMovie();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();

                if(TextUtils.isEmpty(title)) return;

                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_MOVIES, title);
                getLoaderManager().restartLoader(0, bundle, SearchFragment.this);
                showPreload();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadSavedData(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            kumpulanMovie = savedInstanceState.getString(KUMPULAN_MOVIES);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KUMPULAN_MOVIES, kumpulanMovie);
    }

    private void showRecyclerMovie() {
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerMovieAdapter(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        if (args != null) {
            kumpulanMovie = args.getString(EXTRA_MOVIES);
        }

        return new MovieAsyncTaskLoader(getActivity(), kumpulanMovie, "search");
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        adapter.setMovie(data);
        adapter.notifyDataSetChanged();
        showFinished();
    }

    private void showPreload() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        lyResult.setVisibility(View.GONE);
    }

    private void showFinished() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if(kumpulanMovie != null && !TextUtils.isEmpty(kumpulanMovie)) {
            lyResult.setVisibility(View.VISIBLE);
            tvResult.setText(getResources().getString(R.string.movie_search_result)
                    + " : " + kumpulanMovie);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setMovie(null);
    }

}

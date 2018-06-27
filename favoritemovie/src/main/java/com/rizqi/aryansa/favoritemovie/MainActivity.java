package com.rizqi.aryansa.favoritemovie;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rizqi.aryansa.favoritemovie.adapter.FavoriteAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rizqi.aryansa.favoritemovie.database.DatabaseContract.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.
        LoaderCallbacks<Cursor> {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.ly_result)
    LinearLayout lyResult;
    FavoriteAdapter adapter;

    @BindView(R.id.lv_movies)
    ListView lvMovie;

    private final int LOAD_NOTES_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        showRecyclerMovie();

        getSupportLoaderManager().initLoader(LOAD_NOTES_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_NOTES_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        showPreload();
        return new CursorLoader(this, CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        showFinished();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    private void showRecyclerMovie() {
        adapter = new FavoriteAdapter(this, null, true);

        lvMovie.setAdapter(adapter);
    }

    private void showPreload() {
        progressBar.setVisibility(View.VISIBLE);
        lvMovie.setVisibility(View.GONE);
        lyResult.setVisibility(View.GONE);
    }

    private void showFinished() {
        progressBar.setVisibility(View.GONE);
        lvMovie.setVisibility(View.VISIBLE);
        lyResult.setVisibility(View.VISIBLE);
        tvResult.setText(getResources().getString(R.string.movies_favorite));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_NOTES_ID);
    }
}

package com.rizqi.aryansa.cataloguemovie.Activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rizqi.aryansa.cataloguemovie.Fragment.FragmentMovieDetail;
import com.rizqi.aryansa.cataloguemovie.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(FragmentMovieDetail.EXTRA_PARCEL,
                    getIntent().getParcelableExtra(FragmentMovieDetail.EXTRA_PARCEL));
            arguments.putParcelable(FragmentMovieDetail.EXTRA_FAVO,
                    getIntent().getParcelableExtra(FragmentMovieDetail.EXTRA_FAVO));
            FragmentMovieDetail fragmentMovieDetail = new FragmentMovieDetail();
            fragmentMovieDetail.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragmentMovieDetail)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

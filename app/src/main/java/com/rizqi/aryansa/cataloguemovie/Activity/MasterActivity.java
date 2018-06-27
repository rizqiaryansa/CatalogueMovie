package com.rizqi.aryansa.cataloguemovie.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.rizqi.aryansa.cataloguemovie.Fragment.FavoriteFragment;
import com.rizqi.aryansa.cataloguemovie.Fragment.SearchFragment;
import com.rizqi.aryansa.cataloguemovie.Fragment.ViewPagerFragment;
import com.rizqi.aryansa.cataloguemovie.Notification.NowPlayingReminder;
import com.rizqi.aryansa.cataloguemovie.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MasterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView profileCircleImageView;
    String profileImageUrl = "https://avatars3.githubusercontent.com/u/25208252?s=460&v=4";

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    ActionBarDrawerToggle toggle;

    NowPlayingReminder.NowPlayingMovieTask nowPlayingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileCircleImageView = (CircleImageView) navigationView.getHeaderView(0)
                .findViewById(R.id.img_master);

        Glide.with(this)
                .load(profileImageUrl)
                .into(profileCircleImageView);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            Fragment currentFragment = new ViewPagerFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, currentFragment)
                    .commit();
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }

        nowPlayingTask = new NowPlayingReminder.NowPlayingMovieTask(this);
        nowPlayingTask.createPeriodicTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.removeDrawerListener(toggle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        String title = "";

        if (id == R.id.nav_home) {
            fragment = new ViewPagerFragment();

        } else if (id == R.id.nav_search) {
            fragment = new SearchFragment();

        } else if (id == R.id.nav_favourite) {
            fragment = new FavoriteFragment();

        } else if (id == R.id.nav_setting) {
            Intent mItent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mItent);
        }

        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

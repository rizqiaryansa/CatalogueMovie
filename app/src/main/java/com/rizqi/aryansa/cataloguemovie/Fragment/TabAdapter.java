package com.rizqi.aryansa.cataloguemovie.Fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rizqi.aryansa.cataloguemovie.R;

/**
 * Created by RizqiAryansa on 1/15/2018.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    //private Activity getActivity;
    String title[];
    Context mContext;

    public TabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        title = new String[] {
                mContext.getResources().getString(R.string.now_playing),
                mContext.getResources().getString(R.string.upcoming)
        };
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new NowPlayingFragment();
                break;
            case 1:
                fragment = new UpComingFragment();
                break;
            default:
                fragment = null;
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }

}

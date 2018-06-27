package com.rizqi.aryansa.cataloguemovie.Deprecated;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rizqi.aryansa.cataloguemovie.BuildConfig;
import com.rizqi.aryansa.cataloguemovie.Activity.DetailActivity;
import com.rizqi.aryansa.cataloguemovie.Fragment.FragmentMovieDetail;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by RizqiAryansa on 1/11/2018.
 */

//DEPRECATED
public class MovieAdapter extends BaseAdapter {

    private ArrayList<MovieItems> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public static String EXTRA_PARCEL = "EXTRA_PARCEL";

    public MovieAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<MovieItems> items) {
        mData = items;
        notifyDataSetChanged();
    }

    public void addItem(final MovieItems item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public MovieItems getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.movie_items, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvOverview = (TextView) convertView.findViewById(R.id.tv_overview);
            holder.tvReleasedate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.ivMovie = (ImageView) convertView.findViewById(R.id.img_moview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final MovieItems mItems = mData.get(position);

        holder.tvTitle.setText(mItems.getTitle());
        holder.tvOverview.setText(mItems.getOverview());

        SimpleDateFormat dateformat  = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateformat.parse(mItems.getRelease_date());
            DateFormat dayFormate= new SimpleDateFormat("EEEE, MMM d, yyyy");
            String dayFromDate=dayFormate.format(date);
            holder.tvReleasedate.setText(dayFromDate);
            Log.d("asd", "----------:: "+dayFromDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        String url = BuildConfig.POSTER_URL + "w342/" + mItems.getPoster_path();
        Glide.with(context)
                .load(url)
                .override(342, 342)
                .crossFade()
                .into(holder.ivMovie);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(FragmentMovieDetail.EXTRA_PARCEL, mItems);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle, tvOverview, tvReleasedate;
        ImageView ivMovie;
    }
}

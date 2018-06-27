package com.rizqi.aryansa.cataloguemovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RizqiAryansa on 1/15/2018.
 */

public class RecyclerMovieAdapter extends RecyclerView.Adapter
        <RecyclerMovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<MovieItems> mData = new ArrayList<>();
    Cursor listFavo;

    public RecyclerMovieAdapter(Context context) {
        this.context = context;
    }

    public void setMovie(ArrayList<MovieItems> listMovie) {
        this.mData = listMovie;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieItems mItems = mData.get(position);

        holder.tvTitle.setText(mItems.getTitle());
        holder.tvOverview.setText(mItems.getOverview());

        SimpleDateFormat dateformat  = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateformat.parse(mItems.getRelease_date());
            DateFormat dayFormate= new SimpleDateFormat("EEEE, MMM d, yyyy");
            String dayFromDate=dayFormate.format(date);
            holder.tvDate.setText(dayFromDate);
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
                .into(holder.imgPoster);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(FragmentMovieDetail.EXTRA_PARCEL, mItems);
                context.startActivity(intent);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, mItems.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, mItems.getRelease_date()
                        + "\n" + mItems.getOverview());
                context.startActivity(shareIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent,
                false);
        return new MovieViewHolder(view);
    }


    class MovieViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_moview) ImageView imgPoster;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.btn_detail) Button btnDetail;
        @BindView(R.id.btn_share) Button btnShare;
        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

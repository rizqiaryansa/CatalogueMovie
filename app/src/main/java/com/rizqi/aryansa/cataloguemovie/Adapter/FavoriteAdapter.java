package com.rizqi.aryansa.cataloguemovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rizqi.aryansa.cataloguemovie.Activity.DetailActivity;
import com.rizqi.aryansa.cataloguemovie.BuildConfig;
import com.rizqi.aryansa.cataloguemovie.Fragment.FragmentMovieDetail;
import com.rizqi.aryansa.cataloguemovie.Model.MovieItems;
import com.rizqi.aryansa.cataloguemovie.Provider.MovieProvider;
import com.rizqi.aryansa.cataloguemovie.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rizqi.aryansa.cataloguemovie.Database.DatabaseContract.CONTENT_URI;

/**
 * Created by RizqiAryansa on 1/21/2018.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoMovieViewHolder> {

    Context mContext;
    private Cursor listFavo;

    public FavoriteAdapter(Context context) {
        this.mContext = context;
    }

    public void setListFavoMovie(Cursor listFavo) {
        this.listFavo = listFavo;
    }

    @Override
    public FavoMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items, parent,
                false);
        return new FavoMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoMovieViewHolder holder, int position) {
        final MovieItems movie = getItem(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //RELEASE_DATE MOVIE
        SimpleDateFormat dateformat  = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateformat.parse(movie.getRelease_date());
            DateFormat dayFormate= new SimpleDateFormat("EEEE, MMM d, yyyy");
            String dayFromDate=dayFormate.format(date);
            holder.tvDate.setText(dayFromDate);
            Log.d("asd", "----------:: "+dayFromDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url = BuildConfig.POSTER_URL + "w342/" + movie.getPoster_path();
        Glide.with(mContext)
                .load(url)
                .override(342, 342)
                .crossFade()
                .into(holder.imgPoster);

        holder.btnDetail.setOnClickListener(new MovieProvider.CustomOnItemClickListener(position,
                new MovieProvider.CustomOnItemClickListener.OnItemClickCallBack() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        Intent intent = new Intent(mContext, DetailActivity.class);

                        Uri uri = Uri.parse(CONTENT_URI+"/"+movie.getId());
                        intent.putExtra(FragmentMovieDetail.EXTRA_FAVO, uri);

                        mContext.startActivity(intent);
                    }
                })
        );

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                shareIntent.putExtra(Intent.EXTRA_TEXT, movie.getRelease_date()
                        + "\n" + movie.getOverview());
                mContext.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listFavo == null) return 0;
        return listFavo.getCount();
    }

    private MovieItems getItem(int position) {
        if (!listFavo.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MovieItems(listFavo);
    }

    class FavoMovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_moview)
        ImageView imgPoster;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.btn_detail)
        Button btnDetail;
        @BindView(R.id.btn_share) Button btnShare;

        FavoMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

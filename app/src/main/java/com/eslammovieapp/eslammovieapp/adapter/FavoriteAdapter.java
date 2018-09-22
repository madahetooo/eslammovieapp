package com.eslammovieapp.eslammovieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eslammovieapp.eslammovieapp.DetailsActivity;
import com.eslammovieapp.eslammovieapp.Model.Movie;
import com.eslammovieapp.eslammovieapp.R;
import com.eslammovieapp.eslammovieapp.api.ApiClient;

import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_RATING;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.GuestViewHolder> {

    private Cursor cursors;
    private Context context;


    public FavoriteAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursors = cursor;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.favorite_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        if (!cursors.moveToPosition(position))
            return;

        String title = cursors.getString(cursors.getColumnIndex(COLUMN_TITLE));
        String img = cursors.getString(cursors.getColumnIndex(COLUMN_POSTER_PATH));

        holder.tvMovieTitle.setText(title);
        Glide.with(context)
                .load(ApiClient.BaseImageURL + img)
                .into(holder.ivMovieImage);

    }

    @Override
    public int getItemCount() {
        return cursors.getCount();
    }

    class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle;
        ImageView ivMovieImage;

        GuestViewHolder(View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.favorite_title);
            ivMovieImage = itemView.findViewById(R.id.favorit_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        Movie movie=new Movie();

                        cursors.moveToPosition(position);
                        movie.setId(Integer.parseInt(cursors.getString(cursors.getColumnIndex(COLUMN_MOVIE_ID))));
                        movie.setPoster_path( cursors.getString(cursors.getColumnIndex(COLUMN_POSTER_PATH)));
                        movie.setTitle(      cursors.getString(cursors.getColumnIndex(COLUMN_TITLE)));
                        movie.setOverview(        cursors.getString(cursors.getColumnIndex(COLUMN_OVERVIEW)));
                        movie.setVote_average(   Double.parseDouble(cursors.getString(cursors.getColumnIndex(COLUMN_RATING))));

                        intent.putExtra(Intent.EXTRA_TEXT, movie);

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}

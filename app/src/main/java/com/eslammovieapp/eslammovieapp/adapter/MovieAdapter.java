package com.eslammovieapp.eslammovieapp.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {

        holder.cardTitle.setText(movies.get(position).getOriginal_title());
        holder.cardRating.setText(String.valueOf(movies.get(position).getVote_average()));
        if (movies.get(position).getPoster_path() != null)
            Glide.with(context)
                    .load(ApiClient.BaseImageURL + movies.get(position).getPoster_path())
                    .into(holder.cardImage);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle;
        TextView cardRating;
        ImageView cardImage;

        MovieViewHolder(View view) {
            super(view);
            cardTitle = view.findViewById(R.id.cardTitle);
            cardRating = view.findViewById(R.id.cardRating);
            cardImage = view.findViewById(R.id.cardImage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, movies.get(position));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}

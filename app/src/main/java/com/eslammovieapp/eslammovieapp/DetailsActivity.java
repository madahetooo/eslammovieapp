package com.eslammovieapp.eslammovieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eslammovieapp.eslammovieapp.Model.Movie;
import com.eslammovieapp.eslammovieapp.adapter.ReviewAdapter;
import com.eslammovieapp.eslammovieapp.adapter.TrailerAdapter;
import com.eslammovieapp.eslammovieapp.api.ApiClient;
import com.eslammovieapp.eslammovieapp.api.ApiInterface;
import com.eslammovieapp.eslammovieapp.response.ReviewResponse;
import com.eslammovieapp.eslammovieapp.response.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eslammovieapp.eslammovieapp.data.MoviesContract.CONTENT_URI;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_RATING;
import static com.eslammovieapp.eslammovieapp.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class DetailsActivity extends AppCompatActivity {

    private Movie movie;
    private TextView overView;
    private TextView tvDetailsTitle;
    private TextView tvReleaseDate;
    private ImageView ivDetailsImage;
    private ImageView ivFavouritImage;
    private RatingBar mRateView;
    private RecyclerView rvTrailer, rvReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivDetailsImage = findViewById(R.id.details_image);
        tvDetailsTitle = findViewById(R.id.details_title);
        overView = findViewById(R.id.details_overview);
        tvReleaseDate = findViewById(R.id.details_relase_date);
        mRateView = findViewById(R.id.ratingBar);
        ivFavouritImage = findViewById(R.id.favorit_image);
        rvReview = findViewById(R.id.rv_review);
        rvTrailer = findViewById(R.id.rv_trailer);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvTrailer.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvReview.setLayoutManager(mLayoutManager);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Movie Detail");
        ab.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent.getParcelableExtra(Intent.EXTRA_TEXT) != null) {
            movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            if (IsFavourite()) {
                ivFavouritImage.setImageResource(R.drawable.ic_like);
                ivFavouritImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivFavouritImage.setImageResource(R.drawable.ic_dis_like);
                        getContentResolver().delete(CONTENT_URI,
                                COLUMN_MOVIE_ID + "=?",
                                new String[]{String.valueOf(movie.getId())});

                    }
                });
            } else {
                //not favoutite
                ivFavouritImage.setImageResource(R.drawable.ic_dis_like);
                ivFavouritImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivFavouritImage.setImageResource(R.drawable.ic_like);

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_TITLE, movie.getOriginal_title());
                        values.put(COLUMN_OVERVIEW, movie.getOverview());
                        values.put(COLUMN_RATING, movie.getVote_average());
                        values.put(COLUMN_POSTER_PATH, movie.getPoster_path());
                        values.put(COLUMN_MOVIE_ID, movie.getId());
                        getContentResolver().insert(CONTENT_URI, values);

                    }
                });
            }


            if (movie != null) {

                tvDetailsTitle.setText(movie.getOriginal_title());
                overView.setText(movie.getOverview());
                tvReleaseDate.setText(movie.getRelease_date());
                mRateView.setRating((Float.parseFloat(String.valueOf(movie.getVote_average())) / 2));

                Glide.with(this)
                        .load(ApiClient.BaseImageURL + movie.getPoster_path())
                        .into(ivDetailsImage);

            } else {

                Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
            }
            long movie_id = movie.getId();
            loadMovieTrailer(movie_id);
            loadMovieReview(movie_id);
        }


        if (intent.getStringExtra("COLUMN_MOVIE_ID") != null) {
            String COLUMN_MOVIE_ID = intent.getStringExtra("COLUMN_MOVIE_ID");
            String COLUMN_POSTER_PATH = intent.getStringExtra("COLUMN_POSTER_PATH");
            String COLUMN_TITLE = intent.getStringExtra("COLUMN_TITLE");
            String COLUMN_OVERVIEW = intent.getStringExtra("COLUMN_OVERVIEW");
            String COLUMN_RATING = intent.getStringExtra("COLUMN_RATING");
            tvDetailsTitle.setText(COLUMN_TITLE);
            overView.setText(COLUMN_OVERVIEW);
            mRateView.setRating((Float.parseFloat(String.valueOf(COLUMN_RATING)) / 2));

            Glide.with(this)
                    .load(ApiClient.BaseImageURL + COLUMN_POSTER_PATH)
                    .into(ivDetailsImage);
            long movie_id = Integer.parseInt(COLUMN_MOVIE_ID);
            loadMovieTrailer(movie_id);
            loadMovieReview(movie_id);
        }


    }

    private boolean IsFavourite() {

        Cursor cursor = getContentResolver().query(CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie.getId())},
                null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            return true;
        } else {
            return false;
        }

    }

    private void loadMovieTrailer(long movieId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<TrailerResponse> responseCall = apiInterface.getMovieTrailer(movieId, BuildConfig.API_KEY);
        responseCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                List<TrailerResponse.ResultsBean> mrResponses = response.body().getResults();
                TrailerAdapter mAdapter = new TrailerAdapter(getApplicationContext(), mrResponses);
                rvTrailer.setAdapter(mAdapter);
                rvTrailer.smoothScrollToPosition(0);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

            }
        });
    }

    private void loadMovieReview(long movieId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ReviewResponse> responseCall = apiInterface.getMovieReview(movieId, BuildConfig.API_KEY);
        responseCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                List<ReviewResponse.ResultsBean> mrResponses = response.body().getResults();
                ReviewAdapter mAdapter = new ReviewAdapter(getApplicationContext(), mrResponses);
                rvReview.setAdapter(mAdapter);
                rvReview.smoothScrollToPosition(0);
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

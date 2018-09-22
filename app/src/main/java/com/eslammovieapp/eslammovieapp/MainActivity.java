package com.eslammovieapp.eslammovieapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eslammovieapp.eslammovieapp.Model.Movie;
import com.eslammovieapp.eslammovieapp.adapter.FavoriteAdapter;
import com.eslammovieapp.eslammovieapp.adapter.MovieAdapter;
import com.eslammovieapp.eslammovieapp.api.ApiClient;
import com.eslammovieapp.eslammovieapp.api.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.eslammovieapp.eslammovieapp.data.MoviesContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private String SORTING_KEY;
    private static final String RECYCLER_POSITION_KEY = "recycler_position";
    private List<Movie> movies;
    private RecyclerView mRecyclerView;
    GridLayoutManager manager;
    private String Default, popular, topRated, favSort;
    private Parcelable RecyclerView_state;
    private final static String API_KEY = BuildConfig.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SORTING_KEY = getResources().getString(R.string.SORTING_KEY);
        if (savedInstanceState != null) {
            RecyclerView_state = savedInstanceState.getParcelable(RECYCLER_POSITION_KEY);
        }

        init();
//        initRecycler();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        RecyclerView_state = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_POSITION_KEY, RecyclerView_state);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            RecyclerView_state = savedInstanceState.getParcelable(RECYCLER_POSITION_KEY);
        }
    }

    private void init() {

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No API KEY ", Toast.LENGTH_LONG).show();
            return;
        }
        initRecycler();
        checkPrefrence();
    }

    private void checkPrefrence() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Default = getResources().getString(R.string.SORTING_POPULAR);
        popular = getResources().getString(R.string.SORTING_POPULAR);
        topRated = getResources().getString(R.string.SORTING_TOP_RATED);
        favSort = getResources().getString(R.string.SORTING_FAVOURITES);
        if (movies == null) {
            if (prefs.getString(SORTING_KEY, Default).equals(popular)) {
                Popular();
            } else if (prefs.getString(SORTING_KEY, Default).equals(topRated)) {
                TopRated();
            } else {
                loadFavoriteMovies();
            }
        } else {
            populateRecycler(false, null);
        }
    }

    private void initRecycler() {

        mRecyclerView = findViewById(R.id.recycler_view);
        manager = new GridLayoutManager(this, calculateBestSpanCount(600));
        mRecyclerView.setLayoutManager(manager);

    }

    private void populateRecycler(Boolean fav, @Nullable Cursor cursor) {

        if (fav) {
            mRecyclerView.setAdapter(new FavoriteAdapter(getApplicationContext(), cursor));
        } else {
            MovieAdapter ma = new MovieAdapter(getApplicationContext(), movies);
            mRecyclerView.setAdapter(ma);
        }
        if (RecyclerView_state != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(RecyclerView_state);
        }
    }

    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    private void Popular() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiService.getMovie("popular", API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                movies = response.body().getResults();
                populateRecycler(false, null);

            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("Error", t.toString());
                Toast.makeText(MainActivity.this, "Error No Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void TopRated() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<Movie> call = apiService.getMovie("top_rated", API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movies = response.body().getResults();
                populateRecycler(false, null);
            }


            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error No Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void loadFavoriteMovies() {

        Cursor cursor = getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);
        populateRecycler(true, cursor);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        RecyclerView_state = null;

        if (item.getItemId() == R.id.action_popular) {
            editor.putString(SORTING_KEY, popular);
            editor.apply();
            Popular();
            Toast.makeText(this, "most popular", Toast.LENGTH_SHORT).show();

            return true;
        } else if (item.getItemId() == R.id.action_rate) {
            editor.putString(SORTING_KEY, topRated);
            editor.apply();
            TopRated();
            Toast.makeText(this, "highest rating", Toast.LENGTH_SHORT).show();

            return true;
        } else if (item.getItemId() == R.id.action_favoirate) {
            editor.putString(SORTING_KEY, favSort);
            editor.apply();
            loadFavoriteMovies();
            Toast.makeText(this, "Favourite ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

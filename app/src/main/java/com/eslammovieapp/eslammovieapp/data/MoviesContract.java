package com.eslammovieapp.eslammovieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.eslammovieapp.eslammovieapp.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITE_MOVIES = "favorite";




    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_FAVOURITE_MOVIES)
                    .build();
    public static class FavouriteMoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RATING = "rating";
    }
}

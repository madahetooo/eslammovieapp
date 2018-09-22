package com.eslammovieapp.eslammovieapp.api;

import com.eslammovieapp.eslammovieapp.Model.Movie;
import com.eslammovieapp.eslammovieapp.response.ReviewResponse;
import com.eslammovieapp.eslammovieapp.response.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("movie/top_rated")
    Call<Movie> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<Movie> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{sort}")
    Call<Movie> getMovie(@Path("sort") String order, @Query("api_key") String key);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReview(@Path("movie_id") long movieId, @Query("api_key") String key);


    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") long movieId, @Query("api_key") String key);
}

package com.eslammovieapp.eslammovieapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {



        public static final String BASE_URL = "https://api.themoviedb.org/3/";
        public static final String BaseImageURL="https://image.tmdb.org/t/p/w500";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

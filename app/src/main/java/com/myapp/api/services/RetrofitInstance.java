package com.myapp.api.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.myapp.api.Network.BASE_URL_V2;

public class RetrofitInstance {

    private static Retrofit instance;

    public static Retrofit getRetrofitInstance() {
        if (instance == null) {
            instance = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_V2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}

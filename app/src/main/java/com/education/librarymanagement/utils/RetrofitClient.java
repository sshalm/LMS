package com.education.librarymanagement.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private Apis apiObj;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Apis.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiObj = retrofit.create(Apis.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Apis getAPIObj() {
        return apiObj;
    }
}
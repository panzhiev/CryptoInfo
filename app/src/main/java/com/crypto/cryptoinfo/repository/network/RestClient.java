package com.crypto.cryptoinfo.repository.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.crypto.cryptoinfo.repository.network.ApiConstants.BASE_URL;

public class RestClient {

    public static ApiInterface getApiInterface() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        return retrofit.create(ApiInterface.class);
    }
}

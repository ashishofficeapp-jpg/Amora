package com.meetlive.app.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    //here 9/03/2021 use api for text..
    //public static final String BASE_URL = "https://admin.welivechat.me/api/";//test
   // public static final String BASE_URL = "https://zeeplive.com/api/";//test
    // public static final String BASE_URL = "http://65.0.190.236/api/";

    public static final String BASE_URL = "https://zeep.live/api/"; //live
    public static final String SOCKET_URL = "http://65.0.195.166:5050";//test
    //public static final String SOCKET_URL = "http://chat.zeep.live:5050"; //live

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient getRequestHeader() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)

                .build();

        return httpClient;
    }
}


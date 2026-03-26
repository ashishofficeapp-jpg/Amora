package com.meetlive.app.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientChat {
    public static final String BASE_URL = "http://65.0.195.166:3500/api/";
    //public static final String BASE_URL = "http://zeeplive.com:3500/api/";

    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

    /*        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
*/
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //                  .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

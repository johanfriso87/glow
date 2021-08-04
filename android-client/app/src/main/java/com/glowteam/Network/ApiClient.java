package com.glowteam.Network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

//    public static final String BASE_URL = "http://52.66.242.48:3333/";
//public static final String BASE_URL = "http://15.206.172.214:3333/";
    public static final String BASE_URL = "http://13.233.98.117:3333/";

    public static final String AMAZON_BASE_URL = "https://amazon-product-reviews-keywords.p.rapidapi.com/";
    public static final String BARCODE_BASE_URL = "https://amazon-price1.p.rapidapi.com/";
    public static final String BARCODE_LOOKUP_BASE_URL = "https://product-data1.p.rapidapi.com/";

    private static Retrofit retrofit = null;
    private static Retrofit retrofit1 = null;
    private final static long TIME_OUT = 2;

    public static Retrofit getClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(TIME_OUT, TimeUnit.MINUTES);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getAmzonClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(TIME_OUT, TimeUnit.MINUTES);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(AMAZON_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit1;
    }

    public static Retrofit getBarcodeClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(TIME_OUT, TimeUnit.MINUTES);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BARCODE_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit1;
    }

    public static Retrofit BarcodeLookupClient(){
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        okHttpClientBuilder
                .connectTimeout(TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(TIME_OUT, TimeUnit.MINUTES);

        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BARCODE_LOOKUP_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit1;
    }
}

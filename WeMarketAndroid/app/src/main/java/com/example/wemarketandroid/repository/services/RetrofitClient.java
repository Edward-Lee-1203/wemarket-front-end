package com.example.wemarketandroid.repository.services;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient INSTANCE;
    private final String BASE_URL = "http://192.168.1.7:8080/api/";
    private Retrofit mRetrofit;
    private WebService mService;
    private Gson mGson;
    private static String TOKEN;

    private RetrofitClient(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if(TOKEN!=null) {   // checks if exists TOKEN
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + TOKEN)
                            .build();
                    request = newRequest;
                }
                return chain.proceed(request);
            }
        }).build();
        mRetrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
        mService = mRetrofit.create(WebService.class);
        mGson = new Gson();
    }

    public static RetrofitClient getInstance(){
        if(INSTANCE == null) INSTANCE = new RetrofitClient();
        return INSTANCE;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public Retrofit getmRetrofit() {
        return mRetrofit;
    }

    public WebService getmService() {
        return mService;
    }

    public Gson getmGson() {
        return mGson;
    }

    public static String getTOKEN() {
        return TOKEN;
    }

    public static void setTOKEN(String TOKEN) {
        RetrofitClient.TOKEN = TOKEN;
    }
}

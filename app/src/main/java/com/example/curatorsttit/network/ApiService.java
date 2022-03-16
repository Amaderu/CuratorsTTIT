package com.example.curatorsttit.network;

import java.lang.reflect.Array;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static ApiService mInstance;
    //private static final String BASE_URL = "http://localhost:4119/api/";
    //String[] array = new String[]{"http://192.168.13.183:4119/api/", "http://192.168.13.183:4119/user19/api/"};
    //private static final String BASE_URL = "http://192.168.13.183:4119/api/";
    private static final String BASE_URL = "http://192.168.13.183:4119/";
    private Retrofit mRetrofit;

    public static ApiService getInstance() {
        if (mInstance == null) {
            mInstance = new ApiService();
        }
        return mInstance;
    }
    private ApiService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        //GsonConverterFactory factory = new Gson().;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }
    public Api getApi() {
        return mRetrofit.create(Api.class);
    }

}

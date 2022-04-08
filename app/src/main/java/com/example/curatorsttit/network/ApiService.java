package com.example.curatorsttit.network;

import com.example.curatorsttit.common.DateConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    //private static final String BASE_URL = "http://192.168.13.183:4119/user19/";
    //private static final String BASE_URL = "http://172.20.176.1:52818/";
    //private static final String BASE_URL = "http:127.0.0.1:52818/";
    private static final String BASE_URL = "http://192.168.43.65:52818/";
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
        Gson gson = new GsonBuilder().setDateFormat(DateConverter.DATE_TIME_MS_SQL).create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();
    }
    public Api getApi() {
        return mRetrofit.create(Api.class);
    }

}

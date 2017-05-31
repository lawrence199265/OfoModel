package com.zhuangbudong.ofo.net;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zhuangbudong.ofo.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangxu on 16/11/29.
 */
public class RetrofitNetApi {


    private static final int DEFAULT_TIME_OUT = 3000;
    private static final int DEFAULT_RETRY = 3;


    private static ApiService apiServiceInstance;

    public static ApiService getApiServiceInstance() {
        if (apiServiceInstance == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            apiServiceInstance = retrofit.create(ApiService.class);
        }
        return apiServiceInstance;
    }
}

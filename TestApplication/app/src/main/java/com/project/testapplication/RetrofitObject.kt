package com.project.testapplication

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager

object RetrofitObject {

       private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private val httpClient = OkHttpClient.Builder()
//        .addInterceptor(NoConnectionInterceptor())
            .connectTimeout(100, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(100, java.util.concurrent.TimeUnit.SECONDS)
//
            .addNetworkInterceptor(loggingInterceptor)


        private val okHttpClient = httpClient.build()


        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(HOST).client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .build()
        }

    }

package com.krishna.sharemarkettrainer.restclient

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SMTRetrofitHelper {

    val baseUrl = "http://144.24.132.18/"

    fun getInstance(): Retrofit {
        var httpClient = OkHttpClient.Builder()
            .callTimeout(1,TimeUnit.MINUTES)
            .connectTimeout(20,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.MINUTES)
            .writeTimeout(60,TimeUnit.SECONDS)

        var builder = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
        builder.client(httpClient.build())
        return builder
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

}
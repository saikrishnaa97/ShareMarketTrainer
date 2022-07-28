package com.krishna.sharemarkettrainer

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException


object NSERetrofitHelper {

    val baseUrl = "https://www.nseindia.com"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

}
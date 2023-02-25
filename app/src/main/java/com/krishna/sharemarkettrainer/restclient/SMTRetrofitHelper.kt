package com.krishna.sharemarkettrainer.restclient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SMTRetrofitHelper {

    val baseUrl = "http://144.24.132.18/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

}
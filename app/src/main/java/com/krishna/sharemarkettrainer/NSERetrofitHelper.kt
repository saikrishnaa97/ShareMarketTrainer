package com.krishna.sharemarkettrainer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NSERetrofitHelper {

    val baseUrl = "https://www.nseindia.com"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

}
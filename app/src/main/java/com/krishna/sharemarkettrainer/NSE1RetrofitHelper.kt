package com.krishna.sharemarkettrainer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NSE1RetrofitHelper {

        val baseUrl = "https://www1.nseindia.com"

        fun getInstance(): Retrofit {
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                // we need to add converter factory to
                // convert JSON object to Java object
                .build()
        }
}
package com.krishna.sharemarkettrainer

import retrofit2.http.GET
import retrofit2.http.Query

interface BSERestClient {

    @GET("RealTimeBseIndiaAPI/api/GetSensexData/w")
    fun getBSEStatus() : BSEStatus

    @GET("BseIndiaAPI/api/HoTurnover/w")
    fun getTopChangers(@Query("flag") flag : String) : TopChangersList
}
package com.krishna.sharemarkettrainer

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BSERestClient {

    @Headers(
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Content-type:application/json",
        "Host:api.bseindia.com",
        "Accept-Language:en-US,en;q=0.5",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0"
    )
    @GET("RealTimeBseIndiaAPI/api/GetSensexData/w")
    fun getBSEStatus() : Call<BSEStatus>

    @Headers(
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Content-type:application/json",
        "Host:api.bseindia.com",
        "Accept-Language:en-US,en;q=0.5",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0"
    )
    @GET("BseIndiaAPI/api/HoTurnover/w")
    fun getTopChangers(@Query("flag") flag : String) : Call<TopChangersList>


    @GET("BseIndiaAPI/api/StockReachGraph/w")
    fun getBSEStockData(@Query("flag") flag: Int, @Query("fromdate") fromDate : String, @Query("todate") toDate: String,
                        @Query("seriesid") seriesId: String, @Query("scripcode") scripCode: Int): Call<BSEStockData>

    @GET("Msource/1D/getQouteSearch.aspx")
    fun searchBSEStock(@Query("Type")type: String, @Query("text") text : String, @Query("flag")flag: String): Call<ResponseBody>
}
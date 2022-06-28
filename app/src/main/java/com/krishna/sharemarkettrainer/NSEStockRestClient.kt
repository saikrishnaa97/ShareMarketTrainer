package com.krishna.sharemarkettrainer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NSEStockRestClient {

    @Headers(
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Content-type:application/json",
        "Host:www.nseindia.com",
        "Accept-Language:en-US,en;q=0.5",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0"
    )
    @GET("api/search/autocomplete")
    fun searchStock(@Query("q") queryParam: String) : Call<StockSearchResult>

    @Headers(
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Content-type:application/json",
        "Host:www1.nseindia.com",
        "Accept-Language:en-US,en;q=0.5",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0"
    )
    @GET("homepage/Indices1.json")
    fun getNSEStatus() : Call<NSEStatus>

    @Headers(
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Content-type:application/json",
        "Host:www.nseindia.com",
        "Accept-Language:en-US,en;q=0.5",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0"
    )
    @GET("api/quote-equity")
    fun getStockStatus(@Query("symbol") symbol : String): Call<NSEStockStatus>

}
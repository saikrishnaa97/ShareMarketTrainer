package com.krishna.sharemarkettrainer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NSEStockRestClient {

    @Headers(
        "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Content-type:application/json",
        "Host:www.nseindia.com",
        "Accept-Language:en-US,en;q=0.5",
        "referer:https://www.nseindia.com/",
        "cookie:RT=\"z=1&dm=nseindia.com&si=e3796d2b-a0ee-4c80-8b53-c4aab7fa2ac1&ss=l52oj8xz&sl=4&tt=7pv&bcn=%2F%2F684d0d42.akstat.io%2F&ld=9isp&nu=326x3cg7&cl=9ril&ul=c9qp\"",
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
        "referer:https://www1.nseindia.com/",
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
        "referer:https://www.nseindia.com/",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0"
    )
    @GET("api/quote-equity")
    fun getStockStatus(@Query("symbol") symbol : String, @Header("cookie") cookie: String): Call<NSEStockStatus>


    @GET("/")
    @Headers(
        "Host:www.nseindia.com",
        "accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "Accept-Language:en-US,en;q=0.9",
        "referer:https://www.nseindia.com/",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36 Edg/95.0.1020.40",
        "X-Requested-With:XMLHttpRequest",
        "cache-control:max-age=0",
        "upgrade-insecure-requests: 1",
        "cookie: RT=\"z=1&dm=nseindia.com&si=1b3226bb-2a4f-45ef-8e98-9a4233728f4f&ss=l64md155&sl=2&tt=5tb&bcn=//684d0d49.akstat.io/&ld=aqs&ul=5kyh\""
    )
    fun getCookieRequest(): Call<Void>

}

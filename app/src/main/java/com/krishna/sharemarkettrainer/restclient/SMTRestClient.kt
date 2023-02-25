package com.krishna.sharemarkettrainer.restclient

import com.krishna.sharemarkettrainer.domain.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SMTRestClient {

    @GET("nseData/scanner")
    fun getScannerData(@Query("list") list: String): Call<ScannerData>

    @GET("nseData/search")
    fun getSearchData(@Query("symbol") symbol: String): Call<SearchResponseData>

    @GET("nseData/topGainers")
    fun getTopGainers(): Call<TopChangersData>

    @GET("nseData/topLosers")
    fun getTopLosers(): Call<TopChangersData>

    @GET("nseData/niftyData")
    fun getNiftyData(): Call<NiftyData>

    @GET("nseData/stockData")
    fun getStockData(@Query("symbol") symbol: String): Call<StockData>

    @GET("nseData/indexData")
    fun getIndexData(@Query("list") list: String): Call<IndexData>

    @GET("nseData/nWeekLow")
    fun getNWeekLow(@Query("symbol") symbol: String, @Query("weeks") weeks: Int): Call<NWeekChangeData>

    @GET("nseData/nWeekHigh")
    fun getNWeekHigh(@Query("symbol") symbol: String, @Query("weeks") weeks: Int): Call<NWeekChangeData>

    @GET("nseData/historicalData")
    fun getHistoricalData(@Query("symbol") symbol: String, @Query("from") from : String, @Query("to") to: String): Call<HistoricalData>

    @GET("nseData/portfolio")
    fun getPortfolio(@Query("user_id") userId: String): Call<PortfolioData>

}
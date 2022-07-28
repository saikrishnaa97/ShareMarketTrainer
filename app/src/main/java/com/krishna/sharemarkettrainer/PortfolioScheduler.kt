package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import okhttp3.Cookie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class PortfolioScheduler(context: Context, adapter: PortfolioAdapter, stockTradeDataList: List<StockTradeDataItem>,
                         cookie: String ) : TimerTask() {

    var context: Context
    var portfolioAdapter: PortfolioAdapter
    var stockTradeDataList: List<StockTradeDataItem>
    var cookie: String
    val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)

    init {
        this.context = context
        this.portfolioAdapter = adapter
        this.stockTradeDataList = stockTradeDataList
        this.cookie = cookie
    }

    override fun run() {
        var tradeDataList = ArrayList<StockTradeDataItem>()
        for (i in stockTradeDataList) {

            var stockDataResponse = bseApi.getBSEStockData(0, "", "", "", i.SCRIP)
            stockDataResponse.enqueue(object : Callback<BSEStockData> {
                override fun onResponse(
                    call: Call<BSEStockData>,
                    response: Response<BSEStockData>
                ) {
                    var responseBody = response.body()
                    var curPrice = responseBody?.CurrVal?.toDouble()
                    i.currentPrice = curPrice!!
                }

                override fun onFailure(call: Call<BSEStockData>, t: Throwable) {
                    Log.i(
                        "BSE StockData Request Details for " + i.stockSymbol,
                        call.request().toString()
                    )
                    Log.e("Request Failed", t.stackTraceToString())
                }
            })
            tradeDataList.add(i)
        }
        Log.i("Refreshed Data", tradeDataList.toString())
        portfolioAdapter = PortfolioAdapter(context!!, tradeDataList!!, cookie!!)
        portfolioAdapter.submitList(tradeDataList)
        portfolioAdapter.notifyDataSetChanged()
    }

}
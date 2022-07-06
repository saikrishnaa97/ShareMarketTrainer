package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.github.pnpninja.nsetools.NSETools
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class PortfolioScheduler(context: Context, adapter: PortfolioAdapter,stockTradeDataList: List<StockTradeDataItem>,
                         cookie: String ) : TimerTask() {

    var context: Context
    var portfolioAdapter : PortfolioAdapter
    var stockTradeDataList : List<StockTradeDataItem>
    var cookie: String
    val nseApi = NSERetrofitHelper.getInstance().create(NSEStockRestClient::class.java)

    init{
        this.context = context
        this.portfolioAdapter = adapter
        this.stockTradeDataList = stockTradeDataList
        this.cookie = cookie
    }

    override fun run() {
        var tradeDataList = ArrayList<StockTradeDataItem>()
        for(i in stockTradeDataList){

            var stockDataResponse = nseApi.getStockStatus(i.stockSymbol,cookie)

            stockDataResponse.enqueue(object: Callback<NSEStockStatus> {
                override fun onResponse(
                    call: Call<NSEStockStatus>,
                    response: Response<NSEStockStatus>
                ) {
                    if (response.code() != 200){
                        getCookieFromRequest()
                        stockDataResponse = nseApi.getStockStatus(i.stockSymbol,cookie)
                        stockDataResponse.enqueue(object: Callback<NSEStockStatus> {
                            override fun onResponse(
                                call: Call<NSEStockStatus>,
                                response: Response<NSEStockStatus>
                            ) {
                                var responseBody = response.body()
                                try {
                                    val df = DecimalFormat("#.##")
                                    df.roundingMode = RoundingMode.DOWN
                                    var percentChange = 100*(responseBody?.priceInfo?.lastPrice?.minus(
                                        i.purchasedAt
                                    ))?.div(i.purchasedAt)!!
                                    val roundoff = df.format(percentChange)
                                    Log.i(i.stockSymbol,responseBody?.priceInfo?.lastPrice.toString())
                                    i.currentPrice = responseBody?.priceInfo?.lastPrice!!
                                    tradeDataList.add(i)
                                }
                                catch (e: Exception){
                                    Log.e("An exception occurred inside portfolioScheduler",e.toString())
                                }
                            }
                            override fun onFailure(call: Call<NSEStockStatus>, t: Throwable) {
                                Log.d("TAG","Error Response = "+t.toString());
                            }
                        })
                    }
                    var responseBody = response.body()
                    try {
                        val df = DecimalFormat("#.##")
                        df.roundingMode = RoundingMode.DOWN
                        var percentChange = 100*(responseBody?.priceInfo?.lastPrice?.minus(
                            i.purchasedAt
                        ))?.div(i.purchasedAt)!!
                        val roundoff = df.format(percentChange)
                        Log.i(i.stockSymbol,responseBody?.priceInfo?.lastPrice.toString())
                        i.currentPrice = responseBody?.priceInfo?.lastPrice!!
                        tradeDataList.add(i)
                    }
                    catch (e: Exception){
                        Log.e("An exception occurred in portfolioScheduler",e.toString())
                    }
                }
                override fun onFailure(call: Call<NSEStockStatus>, t: Throwable) {
                    Log.d("TAG","Error Response = "+t.toString());
                }
            })

        }
        portfolioAdapter = PortfolioAdapter(context!!,tradeDataList!!,cookie!!)
        portfolioAdapter.notifyDataSetChanged()
    }

    fun getCookieFromRequest(): String{
        var cookie = ""

        var cookieResponse = nseApi.getCookieRequest()

        cookieResponse.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.i("Headers",response.headers().toString())
                if (!response.headers().get("Set-Cookie")?.isEmpty()!!) {
                    cookie = response.headers().get("Set-Cookie").toString()
                    Log.i("Got the cookie from the request",cookie!!)
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.i("request",call.request().toString())
                Log.e("An exception occured",t.toString())
            }
        })

        return cookie
    }

}
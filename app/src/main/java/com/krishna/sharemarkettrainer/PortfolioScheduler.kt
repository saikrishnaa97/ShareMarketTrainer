package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.github.pnpninja.nsetools.NSETools
import java.util.*
import kotlin.collections.ArrayList

class PortfolioScheduler(context: Context, adapter: PortfolioAdapter,stockTradeDataList: List<StockTradeDataItem> ) : TimerTask() {

    var context: Context
    var portfolioAdapter : PortfolioAdapter
    var stockTradeDataList : List<StockTradeDataItem>

    init{
        this.context = context
        this.portfolioAdapter = adapter
        this.stockTradeDataList = stockTradeDataList
    }

    override fun run() {
        var tradeDataList = ArrayList<StockTradeDataItem>()
        for(i in stockTradeDataList){
            try{
                var nseResult = NSETools().getQuote(i.stockSymbol)
                Log.i("stock data from nse",nseResult.lastPrice.toString())
            }
            catch (e: Exception){
                Log.e("Exception occurred",e.toString())
            }
        }
    }
}
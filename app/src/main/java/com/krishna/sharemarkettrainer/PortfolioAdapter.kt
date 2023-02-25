package com.krishna.sharemarkettrainer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.krishna.sharemarkettrainer.domain.Portfolio
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round


class PortfolioAdapter(context: Context, stockTradeDataList: List<Portfolio>) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder?>() {

    private val context : Context
    private val stockTradeDataList : List<Portfolio>
//    val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)

    init {
        this.context = context
        this.stockTradeDataList = stockTradeDataList
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var stock_name_portfolio: TextView
        var cost_portfolio: TextView
        var current_price_portfolio: TextView
        var percent_change_portfolio: TextView
        var target_price: TextView
        var stoploss_price: TextView
        var total_cost: TextView


        init {
            stock_name_portfolio = itemView.findViewById(R.id.stock_name_portfolio)
            cost_portfolio = itemView.findViewById(R.id.cost_portfolio)
            current_price_portfolio = itemView.findViewById(R.id.current_price_portfolio)
            percent_change_portfolio = itemView.findViewById(R.id.percent_change_portfolio)
            target_price = itemView.findViewById(R.id.target_price)
            stoploss_price = itemView.findViewById(R.id.stoploss_price)
            total_cost = itemView.findViewById(R.id.total_cost)
        }
    }

    fun submitList(newData: List<Portfolio>){
        Log.i("Adapter refreshed with data",newData.toString())
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.portfolio_item_layout,parent, false)
        return PortfolioAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.cost_portfolio.text = "Avg. Cost\n Rs."+stockTradeDataList?.get(position)?.avgCost.toString()
        holder.stock_name_portfolio.text = stockTradeDataList?.get(position)?.symbol.toString() + " x "+
                stockTradeDataList?.get(position)?.numOfShares
//        var stockDataResponse = bseApi.getBSEStockData(0,"","","",stockTradeDataList.get(position).SCRIP)
//        stockDataResponse.enqueue(object: Callback<BSEStockData>{
//            override fun onResponse(call: Call<BSEStockData>, response: Response<BSEStockData>) {
//                var responseBody = response.body()
//                Log.i(stockTradeDataList.get(position).symbol,responseBody?.CurrVal.toString())
                var curPrice = stockTradeDataList?.get(position)?.ltp?.toDouble()
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN
                var percentChange = 100*(curPrice?.minus(
                    stockTradeDataList?.get(position)?.avgCost!!
                ))?.div(stockTradeDataList?.get(position)?.avgCost!!)!!
                val roundoff = df.format(percentChange)
                holder.current_price_portfolio.text =
                    "Cur Price\n Rs. " + curPrice?.toString()
                holder.percent_change_portfolio.text = "% Change\n " + roundoff.toString()+"%"

                if(percentChange > 0){
                    holder.percent_change_portfolio.setTextColor(Color.GREEN)
                }
                else if (percentChange < 0){
                    holder.percent_change_portfolio.setTextColor(Color.RED)
                }

                holder.stoploss_price.text = "StopLoss Price\n Rs."+stockTradeDataList?.get(position)?.stopLoss!!
                holder.target_price.text = "Target Price\n Rs."+stockTradeDataList?.get(position)?.target!!
                holder.total_cost.text = "Rs. "+(stockTradeDataList?.get(position)?.numOfShares!! * stockTradeDataList?.get(position)?.avgCost!!).toString()
//            }
//
//            override fun onFailure(call: Call<BSEStockData>, t: Throwable) {
//                Log.i("BSE StockData Request Details for "+stockTradeDataList.get(position).symbol,call.request().toString())
//                Log.e("Request Failed",t.stackTraceToString())
//            }
//        })
    }

    override fun getItemCount(): Int {
        return stockTradeDataList?.size!!
    }

}

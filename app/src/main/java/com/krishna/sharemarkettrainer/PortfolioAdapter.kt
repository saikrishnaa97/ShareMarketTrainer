package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat


class PortfolioAdapter(context: Context, stockTradeDataList: List<StockTradeDataItem>, cookie: String) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder?>() {

    private val context : Context
    private val stockTradeDataList : List<StockTradeDataItem>
    var cookie : String
    val nseApi = NSERetrofitHelper.getInstance().create(NSEStockRestClient::class.java)
    val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)
    val myData = mutableListOf<StockTradeDataItem>()

    init {
        this.context = context
        this.stockTradeDataList = stockTradeDataList
        this.cookie = cookie
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var stock_name_portfolio: TextView
        var cost_portfolio: TextView
        var current_price_portfolio: TextView
        var percent_change_portfolio: TextView

        init {
            stock_name_portfolio = itemView.findViewById(R.id.stock_name_portfolio)
            cost_portfolio = itemView.findViewById(R.id.cost_portfolio)
            current_price_portfolio = itemView.findViewById(R.id.current_price_portfolio)
            percent_change_portfolio = itemView.findViewById(R.id.percent_change_portfolio)
        }
    }

    fun submitList(newData: List<StockTradeDataItem>){
        Log.i("Adapter refreshed with data",newData.toString())
        myData.clear()
        myData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.portfolio_item_layout,parent, false)
        return PortfolioAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cost_portfolio.text = "Avg. Cost\n Rs."+stockTradeDataList?.get(position)?.purchasedAt.toString()
        holder.stock_name_portfolio.text = stockTradeDataList?.get(position)?.stockSymbol.toString()

        var stockDataResponse = bseApi.getBSEStockData(0,"","","",stockTradeDataList.get(position).SCRIP)
        stockDataResponse.enqueue(object: Callback<BSEStockData>{
            override fun onResponse(call: Call<BSEStockData>, response: Response<BSEStockData>) {
                var responseBody = response.body()
                var curPrice = responseBody?.CurrVal?.toDouble()
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN
                var percentChange = 100*(curPrice?.minus(
                    stockTradeDataList?.get(position)?.purchasedAt
                ))?.div(stockTradeDataList?.get(position)?.purchasedAt)!!
                val roundoff = df.format(percentChange)
                holder.current_price_portfolio.text =
                    "Cur Price\n Rs. " + curPrice?.toString()
                holder.percent_change_portfolio.text = "% Change\n " + roundoff.toString()+"%"
            }

            override fun onFailure(call: Call<BSEStockData>, t: Throwable) {
                Log.i("BSE StockData Request Details for "+stockTradeDataList.get(position).stockSymbol,call.request().toString())
                Log.e("Request Failed",t.stackTraceToString())
            }
        })
    }

    override fun getItemCount(): Int {
        return stockTradeDataList?.size!!
    }

}

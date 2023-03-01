package com.krishna.sharemarkettrainer

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krishna.sharemarkettrainer.domain.Portfolio
import com.krishna.sharemarkettrainer.domain.PortfolioData
import com.krishna.sharemarkettrainer.restclient.SMTRestClient
import com.krishna.sharemarkettrainer.restclient.SMTRetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class PortfolioJobScheduler(context: Context, portfolioAdapter: PortfolioAdapter,portfolioRecyclerView: RecyclerView, uid: String, total_cost_view: TextView,
cur_value_view: TextView, pl_view: TextView) : TimerTask() {

    val context : Context
    var portfolioAdapter : PortfolioAdapter
    var uid : String
    var portfolioRecyclerView: RecyclerView
    var total_cost_view : TextView
    var cur_value_view : TextView
    var pl_view : TextView

    init{
        this.context = context
        this.portfolioAdapter = portfolioAdapter
        this.uid = uid
        this.portfolioRecyclerView = portfolioRecyclerView
        this.total_cost_view = total_cost_view
        this.cur_value_view = cur_value_view
        this.pl_view = pl_view
    }

    override fun run() {
        var total_cost = 0.0
        var cur_value = 0.0
        var smtApi = SMTRetrofitHelper.getInstance().create(SMTRestClient::class.java)
        var smtClient = smtApi.getPortfolio(uid)
        smtClient.enqueue(object: Callback<PortfolioData> {
            override fun onResponse(call: Call<PortfolioData>, response: Response<PortfolioData>) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                portfolioAdapter = PortfolioAdapter(context,responseBody?.portfolio!!)
                val linearLayoutManager = LinearLayoutManagerWrapper(context,
                    LinearLayoutManager.VERTICAL, false)
                portfolioRecyclerView.layoutManager = linearLayoutManager
                portfolioRecyclerView.adapter = portfolioAdapter
                portfolioAdapter.submitList(responseBody?.portfolio!!)
                portfolioAdapter.notifyDataSetChanged()

                responseBody?.portfolio!!.forEach{
                    total_cost += (it.avgCost * it.numOfShares)
                    cur_value += (it.ltp * it.numOfShares)
                }
                val df = DecimalFormat("#.##")
                df.roundingMode = RoundingMode.DOWN

                total_cost_view?.text = "Total Cost :- \nRS. "+df.format(total_cost).toString()
                cur_value_view?.text = "Current Value :- \nRS. "+df.format(cur_value).toString()
                pl_view?.text = "P&L :- \nRS. "+df.format(cur_value - total_cost).toString()

                if (cur_value > total_cost){
//                    cur_value_view?.setTextColor(Color.GREEN)
                    pl_view?.setTextColor(Color.GREEN)
                }
                else {
//                    cur_value_view?.setTextColor(Color.RED)
                    pl_view?.setTextColor(Color.RED)
                }

            }

            override fun onFailure(call: Call<PortfolioData>, t: Throwable) {
                Log.d("TAG","Portfolio Error Response = "+t.toString());
            }
        })
    }
}
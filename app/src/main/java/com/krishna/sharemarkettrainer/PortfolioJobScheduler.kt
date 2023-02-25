package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krishna.sharemarkettrainer.domain.Portfolio
import com.krishna.sharemarkettrainer.domain.PortfolioData
import com.krishna.sharemarkettrainer.restclient.SMTRestClient
import com.krishna.sharemarkettrainer.restclient.SMTRetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PortfolioJobScheduler(context: Context, portfolioAdapter: PortfolioAdapter,portfolioRecyclerView: RecyclerView, uid: String) : TimerTask() {

    val context : Context
    var portfolioAdapter : PortfolioAdapter
    var uid : String
    var portfolioRecyclerView: RecyclerView

    init{
        this.context = context
        this.portfolioAdapter = portfolioAdapter
        this.uid = uid
        this.portfolioRecyclerView = portfolioRecyclerView
    }

    override fun run() {
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
            }

            override fun onFailure(call: Call<PortfolioData>, t: Throwable) {
                Log.d("TAG","Portfolio Error Response = "+t.toString());
            }
        })
    }
}
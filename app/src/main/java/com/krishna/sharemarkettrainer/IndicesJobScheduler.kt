package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Worker
import androidx.work.WorkerParameters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class IndicesJobScheduler(context: Context, bse_live: TextView, nse_live: TextView) : TimerTask() {

    val context : Context
    var bse_live : TextView
    var nse_live : TextView

    init{
        this.context = context
        this.bse_live = bse_live
        this.nse_live = nse_live
    }

    override fun run() {
        Log.i("IndicesJobScheduler","reload liveIndices called")
        val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)
        var bseLiveResp = bseApi.getBSEStatus()

        bseLiveResp.enqueue(object: Callback<BSEStatus> {
            override fun onResponse(
                call: Call<BSEStatus>,
                response: Response<BSEStatus>
            ) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                for(i in responseBody!!){
                    if (i.indxnm.equals("SenSexValue")){
                        bse_live.text = "Sensex:- "+i.ltp+"\n% Change:- "+i.perchg+"%"
                    }
                }
            }
            override fun onFailure(call: Call<BSEStatus>, t: Throwable) {
                Log.d("TAG","Error Response = "+t.toString());
            }
        })

        val nseApi = NSE1RetrofitHelper.getInstance().create(NSEStockRestClient::class.java)
        var nseLiveResp = nseApi.getNSEStatus()

        nseLiveResp.enqueue(object: Callback<NSEStatus> {
            override fun onResponse(
                call: Call<NSEStatus>,
                response: Response<NSEStatus>
            ) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                for(i in responseBody?.data!!){
                    if (i.name.equals("NIFTY 50")){
                        Log.i("Response",i.toString())
                        nse_live.text = "NIFTY 50:- "+i.lastPrice+"\n% Change:- "+i.pChange+"%"
                    }
                }
            }
            override fun onFailure(call: Call<NSEStatus>, t: Throwable) {
                Log.d("TAG","Error Response = "+t.toString());
            }
        })
    }
}
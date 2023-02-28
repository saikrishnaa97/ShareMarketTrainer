package com.krishna.sharemarkettrainer

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.krishna.sharemarkettrainer.domain.NiftyData
import com.krishna.sharemarkettrainer.restclient.SMTRestClient
import com.krishna.sharemarkettrainer.restclient.SMTRetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class IndicesJobScheduler(context: Context, nse_live: TextView) : TimerTask() {

    val context : Context
//    var bse_live : TextView
    var nse_live : TextView

    init{
        this.context = context
//        this.bse_live = bse_live
        this.nse_live = nse_live
    }

    override fun run() {
        Log.i("IndicesJobScheduler","reload liveIndices called")
//        val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)
//        var bseLiveResp = bseApi.getBSEStatus()





//        bseLiveResp.enqueue(object: Callback<BSEStatus> {
//            override fun onResponse(
//                call: Call<BSEStatus>,
//                response: Response<BSEStatus>
//            ) {
//                var responseBody = response.body()
//                Log.d("Response Code",response.code().toString())
//                Log.d("Response Url",response.raw().request().url().toString())
//                Log.d("Response",responseBody.toString())
//                for(i in responseBody!!){
//                    if (i.indxnm.equals("SenSexValue")){
//                        bse_live.text = "Sensex:- "+i.ltp+"\n% Change:- "+i.perchg+"%"
//                        Log.i("Sensex:- ",i.ltp)
//                    }
//                }
//            }
//            override fun onFailure(call: Call<BSEStatus>, t: Throwable) {
//                Log.d("TAG","Error Response = "+t.toString());
//            }
//        })

        val smtDataApi = SMTRetrofitHelper.getInstance().create(SMTRestClient::class.java)
        var nseLiveResp = smtDataApi.getNiftyData()
        Log.i("Inside scheduler","True")
        nseLiveResp.enqueue(object: Callback<NiftyData> {
            override fun onResponse(
                call: Call<NiftyData>,
                response: Response<NiftyData>
            ) {
                var responseBody = response.body()
                Log.d("Response Code",response.code().toString())
                Log.d("Response Url",response.raw().request().url().toString())
                Log.d("Response",responseBody.toString())
                        var percChange = ""
                        if (responseBody?.pChange?.toDouble()!! > 0){
                            percChange = "+"+responseBody?.pChange
                        }
//                        else if(responseBody?.pChange?.toDouble()!! < 0){
//                            percChange = "-"+responseBody?.pChange
//                        }
                        else {
                            percChange = responseBody?.pChange!!
                        }
                        nse_live.text = "NIFTY 50:- "+responseBody?.lastPrice+"\n% Change:- "+percChange+"%"
                        Log.i("NIFTY 50:- ",responseBody?.lastPrice!!)
            }
            override fun onFailure(call: Call<NiftyData>, t: Throwable) {
                Log.d("TAG","Error Response = "+t.toString());
            }
        })
    }
}
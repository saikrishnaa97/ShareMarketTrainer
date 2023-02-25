package com.krishna.sharemarkettrainer

import android.app.ProgressDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.work.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.krishna.sharemarkettrainer.domain.TopChangersData
import com.krishna.sharemarkettrainer.restclient.SMTRestClient
import com.krishna.sharemarkettrainer.restclient.SMTRetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_home, container, false)
        var gainers_btn = view.findViewById<TextView>(R.id.gainers_btn)
        var losers_btn = view.findViewById<TextView>(R.id.losers_btn)
        var changeRecyclerView = view.findViewById<RecyclerView>(R.id.rec_layout_changers)
        var nse_live = view.findViewById<TextView>(R.id.nse_live)
//        var bse_live = view.findViewById<TextView>(R.id.bse_live)

        val progressDialog =  ProgressDialog(requireContext())
        progressDialog.setTitle("Share Market Trainer")
        // Update topChangers
        val linearLayoutManager = LinearLayoutManager(context)
        changeRecyclerView.layoutManager = linearLayoutManager
        getGainers(changeRecyclerView,progressDialog)

        gainers_btn.setOnClickListener({
            getGainers(changeRecyclerView,progressDialog)
        })

        losers_btn.setOnClickListener({
            getLosers(changeRecyclerView,progressDialog)
        })

        //Update live indices
        val timer = Timer()
        val updateLiveIndicesTask = IndicesJobScheduler(requireContext(),nse_live)
        timer.scheduleAtFixedRate(updateLiveIndicesTask, 500,10000)

        return view
    }

    fun getGainers(changeRecyclerView: RecyclerView,progressDialog: ProgressDialog){

        val smtDataApi = SMTRetrofitHelper.getInstance().create(SMTRestClient::class.java)
        val gainersResponse = smtDataApi.getTopGainers()
        progressDialog.setMessage("Top Gainers data is loading, please wait")
        progressDialog.show()
        gainersResponse.enqueue(object: Callback<TopChangersData> {
            override fun onResponse(
                call: Call<TopChangersData>,
                response: Response<TopChangersData>
            ) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                var changeAdapter = ChangersAdapter(requireContext(),responseBody?.data!!)
                changeRecyclerView?.adapter = changeAdapter
                changeRecyclerView?.adapter?.notifyDataSetChanged()
                progressDialog.cancel()
            }
            override fun onFailure(call: Call<TopChangersData>, t: Throwable) {
                Log.d("TAG","Gainers Error Response = "+t.toString());
            }
        })
    }

    fun getLosers(changeRecyclerView: RecyclerView,progressDialog: ProgressDialog){
        val smtDataApi = SMTRetrofitHelper.getInstance().create(SMTRestClient::class.java)
        val gainersResponse = smtDataApi.getTopLosers()
        progressDialog.setMessage("Top Losers data is loading, please wait")
        progressDialog.show()
        gainersResponse.enqueue(object: Callback<TopChangersData> {
            override fun onResponse(
                call: Call<TopChangersData>,
                response: Response<TopChangersData>
            ) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                var changeAdapter = ChangersAdapter(requireContext(),responseBody?.data!!)
                changeRecyclerView?.adapter = changeAdapter
                changeRecyclerView?.adapter?.notifyDataSetChanged()
                progressDialog.cancel()
            }

            override fun onFailure(call: Call<TopChangersData>, t: Throwable) {
                Log.d("TAG","Losers Error Response = "+t.toString());
            }
        })
    }

}
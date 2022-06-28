package com.krishna.sharemarkettrainer

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
        var bse_live = view.findViewById<TextView>(R.id.bse_live)

        // Update topChangers
        val linearLayoutManager = LinearLayoutManager(context)
        changeRecyclerView.layoutManager = linearLayoutManager
        getGainers(changeRecyclerView)

        gainers_btn.setOnClickListener({
            getGainers(changeRecyclerView)
        })

        losers_btn.setOnClickListener({
            getLosers(changeRecyclerView)
        })

        //Update live indices
        val timer = Timer()
        val updateLiveIndicesTask = IndicesJobScheduler(requireContext(),bse_live,nse_live)
        timer.scheduleAtFixedRate(updateLiveIndicesTask, 500,10000)

        return view
    }

    fun getGainers(changeRecyclerView: RecyclerView){
        val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)

        val gainersResponse = bseApi.getTopChangers("G")
        gainersResponse.enqueue(object: Callback<TopChangersList> {
            override fun onResponse(
                call: Call<TopChangersList>,
                response: Response<TopChangersList>
            ) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                var changeAdapter = ChangersAdapter(requireContext(),responseBody?.Table!!)
                changeRecyclerView?.adapter = changeAdapter
                changeRecyclerView?.adapter?.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<TopChangersList>, t: Throwable) {
                Log.d("TAG","Error Response = "+t.toString());
            }
        })
    }

    fun getLosers(changeRecyclerView: RecyclerView){
        val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)
        val gainersResponse = bseApi.getTopChangers("L")
        gainersResponse.enqueue(object: Callback<TopChangersList> {
            override fun onResponse(
                call: Call<TopChangersList>,
                response: Response<TopChangersList>
            ) {
                var responseBody = response.body()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                var changeAdapter = ChangersAdapter(requireContext(),responseBody?.Table!!)
                changeRecyclerView?.adapter = changeAdapter
                changeRecyclerView?.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<TopChangersList>, t: Throwable) {
                Log.d("TAG","Error Response = "+t.toString());
            }
        })
    }

}
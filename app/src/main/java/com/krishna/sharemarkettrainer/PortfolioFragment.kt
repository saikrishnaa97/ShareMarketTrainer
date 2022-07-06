package com.krishna.sharemarkettrainer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PortfolioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PortfolioFragment : Fragment() {

    var refUsers: DatabaseReference?= null
    var firebaseUser: FirebaseUser?= null

    var inflater: LayoutInflater? = null
    var container : ViewGroup? = null
    var cookie : String? = ""
    val nseApi = NSERetrofitHelper.getInstance().create(NSEStockRestClient::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_portfolio, container, false)
        this.inflater = inflater
        this.container = container
        var portfolioRecyclerView = view.findViewById<RecyclerView>(R.id.portfolio_recycler)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Trades").child(firebaseUser!!.uid)


        //get cookie
        if (cookie.equals("")){
            cookie = getCookieFromRequest()
        }

        refUsers?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var stockTradesList = ArrayList<StockTradeDataItem>()
                for(data in snapshot.children){
                    val tradeData = data.getValue(StockTradeDataItem::class.java)
                    val linearLayoutManager = LinearLayoutManager(context)
                    portfolioRecyclerView.layoutManager = linearLayoutManager
                    Log.i("tradesData",tradeData.toString())
                    stockTradesList.add(tradeData!!)
                }
                val portfolioAdapter = PortfolioAdapter(requireContext(),stockTradesList!!,cookie!!)
                portfolioRecyclerView.adapter = portfolioAdapter
                val timer = Timer()
                var portfolioScheduler = PortfolioScheduler(context!!,portfolioAdapter,stockTradesList, cookie!!)
                timer.scheduleAtFixedRate(portfolioScheduler, 100,3000)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        return view
    }

    override fun onResume() {
        var view = inflater?.inflate(R.layout.fragment_portfolio, container, false)
        var portfolioRecyclerView = view?.findViewById<RecyclerView>(R.id.portfolio_recycler)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Trades").child(firebaseUser!!.uid)


        refUsers?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var stockTradesList = ArrayList<StockTradeDataItem>()
                for(data in snapshot.children){
                    val tradeData = data.getValue(StockTradeDataItem::class.java)
                    val linearLayoutManager = LinearLayoutManager(context)
                    portfolioRecyclerView?.layoutManager = linearLayoutManager
                    Log.i("tradesData on Resume",tradeData?.purchasedAt.toString())
                    stockTradesList.add(tradeData!!)
                }
                val portfolioAdapter = PortfolioAdapter(context!!,stockTradesList!!,cookie!!)
                portfolioRecyclerView?.adapter = portfolioAdapter
                portfolioAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        super.onResume()
    }

    fun getCookieFromRequest(): String{
        var cookie = ""

        var cookieResponse = nseApi.getCookieRequest()

        cookieResponse.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.i("Headers",response.headers().toString())
                if (!response.headers().get("Set-Cookie")?.isEmpty()!!) {

                    var headers = response.headers()

                    for(i in headers.toMultimap()){
                        if(i.key.lowercase().equals("set-cookie")){
                            cookie = cookie + i.value
                        }
                    }

//                    cookie = response.headers().get("Set-Cookie").toString()
                    Log.i("Got the cookie from the request",cookie!!)
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.i("request initial",call.request().toString())
                Log.e("An exception occured initial",t.toString())
            }
        })

        return cookie
    }

}
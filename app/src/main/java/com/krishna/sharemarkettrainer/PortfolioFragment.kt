package com.krishna.sharemarkettrainer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
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


        //Added temp searchLogic for scripCodes from BSE
        val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)
        var tmp = bseApi.searchBSEStock("EQ","PAYTM","nw")
        tmp.enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val html = response.body()!!.string()
                    val document: Document = Jsoup.parse(html)
                    val elements: Elements = document.getElementsByClass("quotemenu")
                    var bseSearchResultList = ArrayList<BSESearchResult>()
                    for (element in elements) {
                        var bseSearchResult = BSESearchResult()
                        var elementText = ""
                        var i = 0
                        while(i < element.childNodeSize()){
                            var e = element.child(i)
                            elementText = e.text()
                            if(elementText.contains("matches found")){
                                elementText =
                                    e.text().split(" ").subList(0,e.text().split(" ").size - 3).toList().toString()
                            }
                            i++
                        }
                        var elementList = elementText.split(" ")
                        bseSearchResult.SCRIPCode = elementList.get(elementList.size -1).split("   ").get(elementList.get(elementList.size -1)
                            .split("   ").size -1).split(']').get(0).toInt()
                        bseSearchResult.StockSymbol= elementList.get(elementList.size -1).split("   ").get(elementList.get(elementList.size -1)
                            .split("   ").size -3)
                        bseSearchResultList.add(bseSearchResult)
                    }
                    Log.i("BseSearchResultList", bseSearchResultList.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

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
                timer.scheduleAtFixedRate(portfolioScheduler, 100,10000)
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
                    Log.d("tradesData on Resume",tradeData?.purchasedAt.toString())
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

}
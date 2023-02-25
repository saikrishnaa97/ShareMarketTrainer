package com.krishna.sharemarkettrainer

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.krishna.sharemarkettrainer.domain.Portfolio
import com.krishna.sharemarkettrainer.domain.PortfolioData
import com.krishna.sharemarkettrainer.restclient.SMTRestClient
import com.krishna.sharemarkettrainer.restclient.SMTRetrofitHelper
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
    var stockTradesList = ArrayList<Portfolio>()

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
        reloadPortfolio(firebaseUser?.uid!!,portfolioRecyclerView)
        val portfolioAdapter = PortfolioAdapter(requireContext(),stockTradesList)
        var reload_btn = view.findViewById<ImageButton>(R.id.portfolio_reload)
        reload_btn.setOnClickListener{
            reloadPortfolio(firebaseUser?.uid!!,portfolioRecyclerView)
        }

        //Added temp searchLogic for scripCodes from BSE
//        val bseApi = BSERetrofitHelper.getInstance().create(BSERestClient::class.java)
//        var tmp = bseApi.searchBSEStock("EQ","HDFCBANK","nw")
//        tmp.enqueue(object: Callback<ResponseBody>{
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                try {
//                    val html = response.body()!!.string()
//                    val document: Document = Jsoup.parse(html)
//                    val elements: Elements = document.getElementsByClass("quotemenu")
//                    var bseSearchResultList = ArrayList<BSESearchResult>()
//                    for (element in elements) {
//                        var bseSearchResult = BSESearchResult()
//                        var elementText = ""
//                        var i = 0
//                        while(i < element.childNodeSize()){
//                            var e = element.child(i)
//                            elementText = e.text()
//                            if(elementText.contains("matches found")){
//                                elementText =
//                                    e.text().split(" ").subList(0,e.text().split(" ").size - 3).toList().toString()
//                            }
//                            i++
//                        }
//                        var elementList = elementText.split(" ")
//                        bseSearchResult.SCRIPCode = elementList.get(elementList.size -1).split("   ").get(elementList.get(elementList.size -1)
//                            .split("   ").size -1).split(']').get(0).toInt()
//                        bseSearchResult.StockSymbol= elementList.get(elementList.size -1).split("   ").get(elementList.get(elementList.size -1)
//                            .split("   ").size -3)
//                        bseSearchResultList.add(bseSearchResult)
//                    }
//                    Log.i("BseSearchResultList", bseSearchResultList.toString())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })

//        refUsers?.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                stockTradesList = ArrayList<StockTradeDataItem>()
//                for(data in snapshot.children){
//                    val tradeData = data.getValue(StockTradeDataItem::class.java)
//                    val linearLayoutManager = LinearLayoutManagerWrapper(context,LinearLayoutManager.VERTICAL, false)
//                    portfolioRecyclerView.layoutManager = linearLayoutManager
//                    Log.i("tradesData",tradeData.toString())
//                    stockTradesList.add(tradeData!!)
//                    portfolioAdapter = PortfolioAdapter(requireContext(),stockTradesList!!,cookie!!)
//                    portfolioRecyclerView.adapter = portfolioAdapter
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
        Log.i("User ID",firebaseUser!!.uid)


//        var handler =  Handler()
//        handler.postDelayed(object: Runnable{
//            override fun run() {
//                portfolioAdapter.submitList(stockTradesList)
//                handler.postDelayed(this, 5000)
//            }
//        },10000)

        val timer = Timer()
        val updateLiveIndicesTask = PortfolioJobScheduler(requireContext(),portfolioAdapter,portfolioRecyclerView,firebaseUser?.uid!!)
        timer.scheduleAtFixedRate(updateLiveIndicesTask, 500,10000)

        return view
    }

    fun reloadPortfolio(uid: String, portfolioRecyclerView: RecyclerView){
        var smtApi = SMTRetrofitHelper.getInstance().create(SMTRestClient::class.java)
        var smtClient = smtApi.getPortfolio(firebaseUser!!.uid)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Share Market Trainer")
        progressDialog.setMessage("Portfolio is loading, please wait")
        progressDialog.show()

        smtClient.enqueue(object: Callback<PortfolioData>{
            override fun onResponse(call: Call<PortfolioData>, response: Response<PortfolioData>) {
                var responseBody = response.body()
                stockTradesList = ArrayList<Portfolio>()
                Log.i("Response Code",response.code().toString())
                Log.i("Response Url",response.raw().request().url().toString())
                Log.i("Response",responseBody.toString())
                stockTradesList = ArrayList(responseBody?.portfolio!!)
                var portfolioAdapter = PortfolioAdapter(requireContext(),responseBody?.portfolio!!)
                val linearLayoutManager = LinearLayoutManagerWrapper(context,LinearLayoutManager.VERTICAL, false)
                portfolioRecyclerView.layoutManager = linearLayoutManager
                portfolioRecyclerView.adapter = portfolioAdapter
                progressDialog.cancel()
            }

            override fun onFailure(call: Call<PortfolioData>, t: Throwable) {
                Log.d("TAG","Portfolio Error Response = "+t.toString());
            }
        })
    }

}

class LinearLayoutManagerWrapper : LinearLayoutManager {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}
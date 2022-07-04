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
                val portfolioAdapter = PortfolioAdapter(requireContext(),stockTradesList!!)
                portfolioRecyclerView.adapter = portfolioAdapter
                val timer = Timer()
                var portfolioScheduler = PortfolioScheduler(context!!,portfolioAdapter,stockTradesList)
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
                val portfolioAdapter = PortfolioAdapter(context!!,stockTradesList!!)
                portfolioRecyclerView?.adapter = portfolioAdapter
                portfolioAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        super.onResume()
    }

}
package com.krishna.sharemarkettrainer

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.view.MotionEvent

import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.krishna.sharemarkettrainer.domain.SearchResponseData
import com.krishna.sharemarkettrainer.restclient.SMTRestClient
import com.krishna.sharemarkettrainer.restclient.SMTRetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    var refUsers: DatabaseReference?= null
    var firebaseUser: FirebaseUser?= null
    private var mUsers: List<Users>? = null
    var searchString : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        var searchText = view?.findViewById<EditText>(R.id.search_text)
        var searchResultRecycler = view?.findViewById<RecyclerView>(R.id.recycler_view_search)
        var progressDialog =  ProgressDialog(requireContext())
        progressDialog.setTitle("Share Market Trainer")
        refUsers?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        var searchBtn = view?.findViewById<Button>(R.id.search_btn)
        searchBtn?.setOnClickListener({
            searchString = searchText?.text.toString()
            Log.i("Search Query",searchString)
//            val nseApi = NSERetrofitHelper.getInstance().create(NSEStockRestClient::class.java)
//
//            val stockSearchResult = nseApi.searchStock(searchString)

            val smtApi = SMTRetrofitHelper.getInstance().create(SMTRestClient::class.java)
            val stockSearchResult = smtApi.getSearchData(searchString)
            progressDialog.setMessage("Searching for "+searchString+", please wait")
            progressDialog.show()
            stockSearchResult.enqueue(object: Callback<SearchResponseData>{
                override fun onResponse(
                    call: Call<SearchResponseData>,
                    response: Response<SearchResponseData>
                ) {
                    var responseBody = response.body()
                    Log.i("Response Code",response.code().toString())
                    Log.i("Response Url",response.raw().request().url().toString())
                    Log.i("Response",responseBody.toString())
                    try{
                        var linearLayoutManager  = LinearLayoutManager(context)
                        searchResultRecycler?.layoutManager = linearLayoutManager
                        var recyclerViewAdapter = SearchResultAdapter(context!!,responseBody?.data!!)
                        searchResultRecycler?.adapter = recyclerViewAdapter
                    }
                    catch(e : Exception){
                        Toast.makeText(context,"An error occured while fetch stock details",Toast.LENGTH_SHORT).show()
                        Log.e("An Exception occured",e.toString())
                    }
                    progressDialog.cancel()
                }

                override fun onFailure(call: Call<SearchResponseData>, t: Throwable) {
                    Log.d("TAG","Error Response = "+t.toString());
                }
            })

        })
        return view
    }

}
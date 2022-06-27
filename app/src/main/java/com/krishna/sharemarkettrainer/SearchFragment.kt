package com.krishna.sharemarkettrainer

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


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

        refUsers?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("User Details",snapshot.toString())
                val user = snapshot.getValue(Users::class.java)
                Log.i("User's name",user?.getName()!!)
                Log.i("User's email",user?.getEmail()!!)
                Log.i("User's available balance",user?.getavailableBalance().toString()!!)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        Toast.makeText(view.context,refUsers?.child("name").toString(),Toast.LENGTH_SHORT).show()
        var searchBtn = view?.findViewById<Button>(R.id.search_btn)
        Log.i("User","username")
        searchBtn?.setOnClickListener({
            searchString = searchText?.text.toString()
            
        })
        return view
    }

}
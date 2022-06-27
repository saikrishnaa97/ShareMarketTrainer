package com.krishna.sharemarkettrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference
    private lateinit var tradeUser: DatabaseReference
    private var firebaseUserId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        val register_button: Button = findViewById(R.id.register_button)
        register_button.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val name: String = findViewById<EditText>(R.id.username_register).text.toString()
        val password: String = findViewById<EditText>(R.id.password_register).text.toString()
        val email: String = findViewById<EditText>(R.id.email_register).text.toString()

        if (name == ""){
            Toast.makeText(this, "Username cannot be  empty", Toast.LENGTH_SHORT).show()
        }else if (password == ""){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
        }else if (email == ""){
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    firebaseUserId = mAuth.currentUser!!.uid
                    refUser = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)

                    var userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserId
                    userHashMap["name"] = name
                    userHashMap["email"] = email
                    userHashMap["availableBalance"] = 300000
                    refUser.updateChildren(userHashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                Log.i("Registration status",task.isSuccessful.toString())
                                val intent = Intent(this,HomeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
//                    tradeUser = FirebaseDatabase.getInstance().reference.child("Trades").child(firebaseUserId)
//                    var tradeHashMap = HashMap<String, Any>()
//                    tradeHashMap[firebaseUserId] = ArrayList<Any>()
//                    tradeUser.updateChildren(tradeHashMap)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful){
//                                Log.i("TradesList creation status",task.isSuccessful.toString())
//                                val intent = Intent(this,HomeActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intent)
//                                finish()
//                            }
//                        }
                }
                else{
                    Log.i("Exception",task.exception!!.message.toString())
                    Toast.makeText(this,"An error occurred while trying to register, "+task.exception!!.message.toString(),
                    Toast.LENGTH_LONG).show()
                }
            }
        }

    }

}
package com.krishna.sharemarkettrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        val login_button : Button = findViewById(R.id.login_button)
        login_button.setOnClickListener{
            loginUser()
        }

    }

    private fun loginUser() {
        val email: String = findViewById<EditText>(R.id.email_login).text.toString()
        val password: String = findViewById<EditText>(R.id.password_login).text.toString()
        if (email == ""){
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
        }else if (password == ""){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val intent = Intent(this,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Could not login in. "+task.exception!!.message.toString(),
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
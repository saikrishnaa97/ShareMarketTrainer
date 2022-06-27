package com.krishna.sharemarkettrainer

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.auth.EmailAuthProvider

import com.google.firebase.auth.AuthCredential
import com.google.android.material.snackbar.Snackbar







// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    var refUsers: DatabaseReference?= null
    var firebaseUser: FirebaseUser?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        var name = view.findViewById<TextView>(R.id.name_edit)
        var email = view.findViewById<TextView>(R.id.email_edit)
        var passwd = view.findViewById<EditText>(R.id.passwd_edit)
        var availBal = view.findViewById<TextView>(R.id.avail_bal_edit)
        var transactionsBtn = view.findViewById<Button>(R.id.transactions_btn)
        var editBtn = view.findViewById<Button>(R.id.edit_btn)

        passwd.inputType = InputType.TYPE_NULL

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        refUsers?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                name.setText(user?.getName())
                email.setText(user?.getEmail())
                passwd.setText("******")
                availBal.setText(user?.getavailableBalance().toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        editBtn.setOnClickListener({
            showMessageBox()
        })

        return view
    }

    fun showMessageBox(){

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(activity).inflate(R.layout.edit_pwd_dialog, null)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(activity).setView(messageBoxView)

        //show dialog
        val  messageBoxInstance = messageBoxBuilder.show()

        var oldPwd = messageBoxView.findViewById<EditText>(R.id.old_pwd)
        var newPwd1 = messageBoxView.findViewById<EditText>(R.id.new_pwd_1)
        var newPwd2 = messageBoxView.findViewById<EditText>(R.id.new_pwd_2)
        var submitBtn = messageBoxView.findViewById<Button>(R.id.submit_btn)

        submitBtn.setOnClickListener {

            var oldPassword = oldPwd.text.toString()
            var newPassword = newPwd1.text.toString()
            var confirmNewPassword = newPwd2.text.toString()

            if (!oldPassword.isBlank() && !newPassword.isBlank() && !confirmNewPassword.isBlank()) {
                if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(
                        context,
                        "New password and confirm password do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                    messageBoxInstance.dismiss()
                } else {
                    val user: FirebaseUser?
                    user = FirebaseAuth.getInstance().currentUser
                    val email = user!!.email
                    val credential =
                        EmailAuthProvider.getCredential(email!!, oldPwd.text.toString())
                    user.reauthenticate(credential)
                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                            override fun onComplete(p0: Task<Void>) {
                                if (p0.isSuccessful()) {
                                    user.updatePassword(newPassword)
                                        .addOnCompleteListener(object : OnCompleteListener<Void> {
                                            override fun onComplete(task: Task<Void>) {
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Password Successfully Modified",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Something went wrong. Please try again.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                messageBoxInstance.dismiss()
                                            }
                                        })
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Something went wrong. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                messageBoxInstance.dismiss()
                            }
                        })
                }
            } else {
                Toast.makeText(
                    context,
                    "The fields cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
                messageBoxInstance.dismiss()
            }
        }

    }

}
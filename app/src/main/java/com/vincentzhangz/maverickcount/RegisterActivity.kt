package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vincentzhangz.maverickcount.models.Status
import com.vincentzhangz.maverickcount.models.User
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        register_button.setOnClickListener {
            firebaseRegister()
        }
        goto_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun firebaseRegister() {
        val userPass=password.text.toString()
        val userEmail=email.text.toString()
        if(userEmail==""){
            toast(this,"Email must be filled")
            return
        }
        else if(userPass==""){
            toast(this,"Password must be filled")
            return
        }
        else if(userPass.length<8){
            toast(this,"Password min 8 chars")
            return
        }

        if (password.text.toString() == confirm_password.text.toString()) {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        toast(this, user?.uid.toString())
                        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("userId", user?.uid).apply()
                        val name=userEmail.split("@")
                        database.getReference("users").child(user!!.uid)
                            .setValue(
                                user.email?.let { User(user.uid, name[0],0, Status(0,0,0), ArrayList()) }
                            )
                        toast(this, "Register success.")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        toast(this, "Register failed.")
                    }
                }
        } else {
            toast(this, "Password must be same!")
        }

    }
}

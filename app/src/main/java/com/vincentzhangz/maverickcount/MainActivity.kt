package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "").toString()

        user_id.text = userId

        if (userId == "") {
            val toast =
                Toast.makeText(applicationContext, "You are not authorized", Toast.LENGTH_LONG)
            toast.show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_chat.setOnClickListener {
            val intent = Intent(this, GlobalBorrowActivity::class.java)
            startActivity(intent)
        }

    }
}

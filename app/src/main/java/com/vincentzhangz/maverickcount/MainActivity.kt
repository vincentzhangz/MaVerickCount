package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
        toggle.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> toast("Profile clicked")
                R.id.chat -> {
                    val intent = Intent(this, ChatActivity::class.java)
                    startActivity(intent)
                }
                R.id.sign_out -> {
                    toast("Sign Out")
                }

            }

            // Close the drawer
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "").toString()
        if (userId == "") {
            toast("You are not authorized")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

//        user_id.text = userId
//        btn_chat.setOnClickListener {
//            val intent = Intent(this, GlobalBorrowActivity::class.java)
//            startActivity(intent)
//        }

    }


}

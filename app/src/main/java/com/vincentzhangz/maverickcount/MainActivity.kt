package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.vincentzhangz.maverickcount.models.NotificationModel
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*


class MainActivity : AppCompatActivity() {

    private lateinit var notif: NotificationModel
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FirebaseDatabase.getInstance()
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/topic")
        notif = NotificationModel(this.applicationContext)
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "").toString()

        val db = database.getReference("users").child(userId).child("name")

        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                current_user_name.text = ds.getValue(String()::class.java).toString()
            }

        })

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

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, Home())
            .addToBackStack(null).commit()

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Home())
                        .addToBackStack(null).commit()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Profile())
                        .addToBackStack(null).commit()
                }
                R.id.chat -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ChatActivity()).addToBackStack(null)
                        .commit()
                }
                R.id.browse -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, GlobalBorrowActivity())
                        .addToBackStack(null).commit()
                }
                R.id.borrow_request -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BorrowRequestActivity())
                        .addToBackStack(null).commit()
                }
                R.id.personal_borrow_request -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PersonalBorrowActivity())
                        .addToBackStack(null).commit()
                }
                R.id.lender_request -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LenderRequestActivity())
                        .addToBackStack(null).commit()
                }
                R.id.friend -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FriendFragment())
                        .addToBackStack(null).commit()
                }
                R.id.setting -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingFragment())
                        .addToBackStack(null).commit()
                }
                R.id.sign_out -> {
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(UserUtil.getUserId(this.applicationContext))
                        .child("deviceId").setValue("")
                    val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove("userId").apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    toast(this, "Sign out success.")
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val userId = UserUtil.getUserId(this)
        if (userId == "") {
            toast(this, "You are not logged in.")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }


}

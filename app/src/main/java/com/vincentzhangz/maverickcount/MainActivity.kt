package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import com.vincentzhangz.maverickcount.utilities.ThemeManager
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.header.view.*


class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val themeManager = ThemeManager(this)
        themeManager.loadTheme()
        database = FirebaseDatabase.getInstance()
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
                val themeManager = ThemeManager(drawerView.context)
                themeManager.loadTheme()
                val storage = FirebaseStorage.getInstance()
                val userId = UserUtil.getUserId(applicationContext)
                val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
                val gsReference =
                    storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
                        Glide.with(header_profile).load(it).circleCrop()
                            .into(header_profile.profile_image)
                    }.addOnFailureListener {
                        Glide.with(header_profile)
                            .load(R.drawable.person_icon_foreground)
                            .circleCrop()
                            .into(header_profile.profile_image)
                    }

                header_profile.setOnClickListener {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Profile())
                        .addToBackStack(null).commit()
                    drawer.closeDrawer(GravityCompat.START)
                }
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
                R.id.chat -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ChatActivity()).addToBackStack(null)
                        .commit()
                }
                R.id.borrow_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BorrowViewPagerActivity())
                        .addToBackStack(null).commit()
                }
                R.id.pending_request_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, RequestPendingActivity())
                        .addToBackStack(null).commit()
                }
                R.id.ongoing_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, OngoingActivity())
                        .addToBackStack(null).commit()
                }
                R.id.history_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HistoryActivity())
                        .addToBackStack(null).commit()
                }
                R.id.borrow_request_page -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BorrowRequestViewPagerActivity())
                        .addToBackStack(null).commit()
                }
                R.id.friend -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FriendActivity())
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
                    var sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove("userId").apply()
                    sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove("dark_mode").apply()
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

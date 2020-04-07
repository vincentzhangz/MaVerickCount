package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.vincentzhangz.maverickcount.models.NotificationModel
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import com.vincentzhangz.maverickcount.utilities.UserUtil
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAKWj3SCI:APA91bFtY6acERyEHgiI8Xx2-NSoeHMvn4mkpBhqaBsPaxdTkabLxS8kp-S4DH5NLNMeZebZfsw8dpBKQjFEKNSRkXdBn72XNX9dQ7oJtr1BtbaWygYirMvdNFa9QP9oWsPx56vPA4AU"
    private val contentType = "application/json"
    private lateinit var notif: NotificationModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/topic")
        notif = NotificationModel(this.applicationContext)

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
                R.id.profile -> toast(this, "Profile clicked")
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
                R.id.sign_out -> {
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(UserUtil.getUserId(this.applicationContext))
                        .child("deviceId").setValue("")
                    val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove("userId").apply()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    toast(this, "Sign Out")
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        val userId = UserUtil.getUserId(this)
        if (userId == "") {
            toast(this, "You are not authorized")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

//        notif.sendNotif("Judul","Pesan")
//        OtherUtil.printToken()
    }


}

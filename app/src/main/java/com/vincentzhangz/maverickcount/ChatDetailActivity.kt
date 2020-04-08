package com.vincentzhangz.maverickcount

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.vincentzhangz.maverickcount.models.ChatDetailLeft
import com.vincentzhangz.maverickcount.models.ChatDetailRight
import com.vincentzhangz.maverickcount.models.MessageDetail
import com.vincentzhangz.maverickcount.models.NotificationModel
import com.vincentzhangz.maverickcount.utilities.ThemeManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlin.properties.Delegates

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private lateinit var msgId: String
    private lateinit var friendToken: String
    private lateinit var notif: NotificationModel
    private var messageLength by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        val themeManager = ThemeManager(this)
        themeManager.loadTheme()
        messageLength = 0
        database = FirebaseDatabase.getInstance()
        msgId = intent.getStringExtra("msgId")
        supportActionBar?.title = msgId

        FirebaseMessaging.getInstance().subscribeToTopic("/topics/topic")
        notif = NotificationModel(this.applicationContext)

        recyclerview_chat_detail.layoutManager = LinearLayoutManager(this)
        et_msg.setOnClickListener {
            recyclerview_chat_detail.smoothScrollToPosition(messageLength)
        }
        getUserData()
        fetchMessage()
    }

    private fun getUserData() {
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "").toString()
        var friendId = intent.getStringExtra("user1")
        if (friendId == userId) {
            friendId = intent.getStringExtra("user2")
        }
        val db = database.getReference("users").child(friendId).child("deviceId")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                friendToken = ds.getValue(String()::class.java).toString()
            }

        })
    }

    private fun fetchMessage() {
        val db = msgId.let { database.getReference("chat-detail").child(it) }
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                ds.children.forEach {
//                    Log.d("msg",it.toString())
                    val messages = it.getValue(MessageDetail::class.java)
                    if (messages!!.user == userId) {
                        adapter.add(ChatDetailRight(messages.msg))
                    } else {
                        adapter.add(ChatDetailLeft(messages.msg))
                    }
                    messageLength++
                }
                recyclerview_chat_detail.adapter = adapter
                recyclerview_chat_detail.smoothScrollToPosition(messageLength)
                Log.d("messageLength", messageLength.toString())
            }

        })
        btn_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = et_msg.text.toString()
        if (message != "") {
            msgId.let {
                database.getReference("chat-detail").child(it).push()
                    .setValue(MessageDetail(userId, message, System.currentTimeMillis()))
            }
            et_msg.setText("")
            Log.d("token2", friendToken)
            notif.sendNotif("New Message", message, friendToken)
        }
    }
}

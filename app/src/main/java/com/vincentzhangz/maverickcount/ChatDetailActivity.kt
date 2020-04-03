package com.vincentzhangz.maverickcount

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.ChatDetailLeft
import com.vincentzhangz.maverickcount.models.ChatDetailRight
import com.vincentzhangz.maverickcount.models.MessageDetail
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_detail.*
import kotlinx.android.synthetic.main.message_item.*

class ChatDetailActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    var msgId=""
    var userId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)

        msgId=intent.getStringExtra("user")
        supportActionBar?.title=msgId

        recyclerview_chat_detail.layoutManager=LinearLayoutManager(this)

        getUserData()

        fetchMessage()
    }

    private fun getUserData() {
        val sharedPreferences= getSharedPreferences("user",Context.MODE_PRIVATE)
        userId=sharedPreferences.getString("userId","").toString()
    }

    private fun fetchMessage() {
        val db=database.getReference("chat-detail").child("\""+msgId+"\"")
        db.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
                ds.children.forEach{
//                    Log.d("msg",it.toString())
                    val messages=it.getValue(MessageDetail::class.java)
                    if(messages!!.user==userId){
                        adapter.add(ChatDetailRight(messages!!.msg))
                    }
                    else{
                        adapter.add(ChatDetailLeft(messages!!.msg))
                    }
                }
                recyclerview_chat_detail.adapter=adapter
            }

        })
        btn_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        var message=et_msg.text.toString()
        val db=database.getReference("chat-detail").child("\""+msgId+"\"").push().setValue(MessageDetail(userId,message,System.currentTimeMillis()))
        et_msg.setText("")
    }
}

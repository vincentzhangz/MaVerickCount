package com.vincentzhangz.maverickcount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.MessageHead
import com.vincentzhangz.maverickcount.models.MessageHeader
import com.vincentzhangz.maverickcount.services.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    var userId="1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title="Message"
        val adapter=GroupAdapter<ViewHolder>()

        recyclerview_message.adapter=adapter
        recyclerview_message.layoutManager=LinearLayoutManager(this)

        setSP()

//        TokenHelper.getCurrToken()

        fetchMessage()

    }

    private fun setSP() {
        val sharedPreferences = getSharedPreferences("user",Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("userId",userId).apply()
    }

    private fun fetchMessage() {
        val db=database.getReference("chats")
        db.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()
                ds.children.forEach{
                    val messages=it.getValue(MessageHead::class .java)
                    val data=messages as MessageHead
                    if(data.user1==userId||data.user2==userId){
//                        Log.d("msg",it.toString())
                        adapter.add(MessageHeader(messages!!))
                    }
                }
                
                adapter.setOnItemClickListener { item, view ->
                    val msgData=item as MessageHeader
                    val intent=Intent(view.context,ChatDetailActivity::class.java)
                    intent.putExtra("user",msgData.msg.msg.toString())
                    startActivity(intent)
                }
                
                recyclerview_message.adapter=adapter
            }

        })
    }

}

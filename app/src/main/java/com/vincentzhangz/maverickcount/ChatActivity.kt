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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()

//    val sharedPreferences = getSharedPreferences("user",Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title="Message"
        val adapter=GroupAdapter<ViewHolder>()

        recyclerview_message.adapter=adapter
        recyclerview_message.layoutManager=LinearLayoutManager(this)

        setSP()

        fetchMessage()
    }

    private fun setSP() {
//        sharedPreferences.edit().putString("userId","1").commit()
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
//                    Log.d("msg",it.toString())
                    val messages=it.getValue(MessageHead::class .java)
                    adapter.add(MessageHeader(messages!!))
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

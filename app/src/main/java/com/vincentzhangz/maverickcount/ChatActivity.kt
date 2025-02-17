package com.vincentzhangz.maverickcount

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.models.MessageHead
import com.vincentzhangz.maverickcount.models.MessageHeader
import com.vincentzhangz.maverickcount.utilities.UserUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.view.*


class ChatActivity : Fragment() {

    private val database = FirebaseDatabase.getInstance()
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.activity_chat, container, false)
        rootView.empty_error.visibility = View.VISIBLE
        rootView.recyclerview_message.visibility = View.GONE
        getUserId()
        rootView.recyclerview_message.layoutManager = LinearLayoutManager(activity)
        fetchMessage(rootView)
        return rootView
    }

    private fun getUserId() {
        userId = UserUtil.getUserId(activity!!.applicationContext)
    }

    private fun fetchMessage(v: View) {
        val db = database.getReference("chats")
        db.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(ds: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                var dataExists = false
                ds.children.forEach {
                    val messages = it.getValue(MessageHead::class.java)
                    val data = messages as MessageHead
                    if (data.user1 == userId || data.user2 == userId) {
                        adapter.add(MessageHeader(it.key.toString(), messages, userId))
                        dataExists = true
                    }
                }
                if (dataExists) {
                    v.empty_error.visibility = View.GONE
                    v.recyclerview_message.visibility = View.VISIBLE
                }

                adapter.setOnItemClickListener { item, view ->
                    val msgData = item as MessageHeader
                    val intent = Intent(view.context, ChatDetailActivity::class.java)
                    intent.putExtra("msgId", msgData.msgId)
                    intent.putExtra("user1", msgData.msg.user1.toString())
                    intent.putExtra("user2", msgData.msg.user2.toString())
                    startActivity(intent)
                }

                v.recyclerview_message.adapter = adapter
            }

        })
    }

}

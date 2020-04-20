package com.vincentzhangz.maverickcount.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_item.view.*

class MessageHead(val user1: String, val user2: String) {
    constructor() : this("", "")
}

class MessageHeader(val msgId: String, val msg: MessageHead, val userId: String) :
    Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        var friendId = msg.user1
        if (friendId == userId) {
            friendId = msg.user2
        }
        FirebaseDatabase.getInstance().getReference("users").child(friendId).child("name")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    val friendName = ds.getValue(String()::class.java)
                    viewHolder.itemView.txv_message.text = friendName
                }
            })
    }
}
package com.vincentzhangz.maverickcount.models

import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_item.view.*

class MessageHead(val user1: String, val user2: String) {
    constructor() : this("", "")
}

class MessageHeader(val msgId:String,val msg: MessageHead) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.message_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txv_message.text = msgId
    }
}
package com.vincentzhangz.maverickcount.models

import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_row_left.view.*
import kotlinx.android.synthetic.main.chat_row_right.view.*

class MessageDetail(val user: String, val msg: String, val timestamp: Long) {
    constructor() : this("", "", System.currentTimeMillis())
}

class ChatDetailLeft(private val txt: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_row_left
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txv_msg_detail.text = txt
    }

}

class ChatDetailRight(val txt: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_row_right
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txv_msg_detail_right.text = txt
    }

}
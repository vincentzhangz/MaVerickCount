package com.vincentzhangz.maverickcount.models

import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendModel(val uid: String, val name: String) {
    constructor() : this("", "")
}

class FriendItem(val friend: FriendModel) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.friend_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.friend_name.text = friend.name
    }
}
package com.vincentzhangz.maverickcount.models

import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendModel(val uid: String, val name: String) {
    constructor() : this("", "")
}

class FriendItem(val friend: FriendModel) : Item<ViewHolder>() {
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    override fun getLayout(): Int {
        return R.layout.friend_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.friend_name.text = friend.name
        val storage = FirebaseStorage.getInstance()
        val userId = friend.uid
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
            Glide.with(viewHolder.itemView).load(it).circleCrop()
                .into(viewHolder.itemView.profile_mini_icon)
        }.addOnFailureListener {
            Glide.with(viewHolder.itemView)
                .load(R.drawable.person_icon_foreground)
                .circleCrop()
                .into(viewHolder.itemView.profile_mini_icon)
        }
    }
}
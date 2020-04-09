package com.vincentzhangz.maverickcount.models

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friend_item.view.*

class User(
    val uid: String,
    val name: String,
    val balance: Int,
    val status: Status,
    val friends: ArrayList<Friend>
) {
    constructor() : this("", "", 0, Status(), ArrayList())
}

class Status(val paid: Int, val unpaid: Int, val late: Int) {
    constructor() : this(0, 0, 0)
}

class UserData(
    val uid: String,
    val name: String,
    val balance: Int,
    val status: Status,
    val friends: HashMap<String, Friend>
) {
    constructor() : this("", "", 0, Status(), HashMap())
}

class Friend(val uid: String, val name: String) {
    constructor() : this("", "")
}

class FriendRequest(val from: String, val to: String, val timestamp: Long) {
    constructor() : this("", "", System.currentTimeMillis())
}

class FriendSearch(val userData: UserData) : Item<ViewHolder>() {
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    override fun getLayout(): Int {
        return R.layout.friend_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.friend_name.text = userData.name
        val storage = FirebaseStorage.getInstance()
        val userId = userData.uid
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
            Glide.with(viewHolder.itemView).load(it).circleCrop()
                .into(viewHolder.itemView.profile_mini_icon)
        }.addOnFailureListener {
            Glide.with(viewHolder.itemView)
                .load(R.drawable.person_icon_foreground)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(viewHolder.itemView.profile_mini_icon)
        }
    }

}

class FriendRequestHandler(val uid: String, val data: FriendRequest) : Item<ViewHolder>() {
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    override fun getLayout(): Int {
        return R.layout.friend_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        FirebaseDatabase.getInstance().getReference("users").child(data.from).child("name")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(ds: DataSnapshot) {
                    viewHolder.itemView.friend_name.text = ds.getValue(String()::class.java)
                    val storage = FirebaseStorage.getInstance()
                    val userId = uid
                    val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
                    storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
                        Glide.with(viewHolder.itemView).load(it)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
                            .into(viewHolder.itemView.profile_mini_icon)
                    }.addOnFailureListener {
                        Glide.with(viewHolder.itemView)
                            .load(R.drawable.person_icon_foreground)
                            .circleCrop()
                            .into(viewHolder.itemView.profile_mini_icon)
                    }
                }

            })
    }

}
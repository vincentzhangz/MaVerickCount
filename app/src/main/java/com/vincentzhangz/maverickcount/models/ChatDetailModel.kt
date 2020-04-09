package com.vincentzhangz.maverickcount.models

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.vincentzhangz.maverickcount.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_row_left.view.*
import kotlinx.android.synthetic.main.chat_row_right.view.*

class MessageDetail(val user: String, val msg: String, val timestamp: Long) {
    constructor() : this("", "", System.currentTimeMillis())
}

class ChatDetailLeft(private val txt: String, private val userId: String) : Item<ViewHolder>() {
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    override fun getLayout(): Int {
        return R.layout.chat_row_left
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txv_msg_detail.text = txt
        val storage = FirebaseStorage.getInstance()
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            Glide.with(viewHolder.itemView).load(it)
                .thumbnail(Glide.with(viewHolder.itemView).load(R.drawable.person_icon_foreground))
                .apply(requestOptions).circleCrop()
                .into(viewHolder.itemView.receiver_image)
        }.addOnFailureListener {
            Glide.with(viewHolder.itemView)
                .load(R.drawable.person_icon_foreground)
                .circleCrop()
                .into(viewHolder.itemView.receiver_image)
        }
    }

}

class ChatDetailRight(private val txt: String, private val userId: String) : Item<ViewHolder>() {
    private val _databaseUrlPrefix = "gs://maverick-count.appspot.com/"
    override fun getLayout(): Int {
        return R.layout.chat_row_right
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txv_msg_detail_right.text = txt
        val storage = FirebaseStorage.getInstance()
        val parsedUrl = _databaseUrlPrefix + "profile-image/$userId"
        storage.getReferenceFromUrl(parsedUrl).downloadUrl.addOnSuccessListener {
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            Glide.with(viewHolder.itemView).load(it)
                .thumbnail(Glide.with(viewHolder.itemView).load(R.drawable.person_icon_foreground))
                .apply(requestOptions).circleCrop()
                .into(viewHolder.itemView.sender_image)
        }.addOnFailureListener {
            Glide.with(viewHolder.itemView)
                .load(R.drawable.person_icon_foreground)
                .circleCrop()
                .into(viewHolder.itemView.sender_image)
        }

    }
}
package com.vincentzhangz.maverickcount.utilities

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.vincentzhangz.maverickcount.models.MessageHead
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class UserUtil {
    companion object {
        fun getUserId(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            return sharedPreferences.getString("userId", "").toString()
        }
    }
}

class OtherUtil {
    companion object {
        fun getTimeFromMilis(time: Long): LocalDateTime {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(time),
                ZoneOffset.ofTotalSeconds(0)
            )
        }

        fun printToken() {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    val token = task.result!!.token
                    Log.d("token", token)
                })
        }

        fun setUserToken(userId: String) {
            Log.d("token", "test")
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    val token = task.result!!.token
                    Log.d("token", token)
                    FirebaseDatabase.getInstance().getReference("users").child(userId)
                        .child("deviceId").setValue(token)
                })
        }

        fun createNewChat(msg: MessageHead) {
            FirebaseDatabase.getInstance().getReference("chats")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(ds: DataSnapshot) {
                        var newChat = true
                        ds.children.forEach {
                            var data = it.getValue(MessageHead::class.java) as MessageHead
                            if ((data.user1 == msg.user1 || data.user1 == msg.user2) && (data.user2 == msg.user1 || data.user2 == msg.user2)) {
                                newChat = false
                            }
                        }
                        if (newChat) {
                            FirebaseDatabase.getInstance().getReference("chats").push()
                                .setValue(msg)
                        }
                    }

                })
        }

        fun updateUnpaid(userId: String) {
            FirebaseDatabase.getInstance().getReference("users").child(userId).child("status")
                .child("unpaid")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(ds: DataSnapshot) {
                        val unpaid = ds.getValue(Int::class.java) as Int
                        val newUnpaid = unpaid + 1
                        FirebaseDatabase.getInstance().getReference("users").child(userId)
                            .child("status").child("unpaid")
                            .setValue(newUnpaid)
                    }

                })
        }
    }
}

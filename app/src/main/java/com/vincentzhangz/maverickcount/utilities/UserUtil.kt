package com.vincentzhangz.maverickcount.utilities

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.vincentzhangz.maverickcount.utilities.SystemUtility.Companion.toast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class UserUtil{
    companion object{
        fun getUserId(context: Context):String{
            val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            return sharedPreferences.getString("userId", "").toString()
        }
    }
}

class OtherUtil{
    companion object{
        fun getTimeFromMilis(time:Long):LocalDateTime{
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(time),
                ZoneOffset.ofTotalSeconds(0))
        }
        fun printToken() {
            FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result!!.token
                Log.d("token",token)
            })
        }
    }
}

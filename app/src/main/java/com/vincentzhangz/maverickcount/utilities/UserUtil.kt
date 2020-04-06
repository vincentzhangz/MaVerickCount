package com.vincentzhangz.maverickcount.utilities

import android.content.Context
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
    }
}
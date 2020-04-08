package com.vincentzhangz.maverickcount.utilities

import android.content.Context
import android.widget.Toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SystemUtility {
    companion object {
        fun toast(context: Context, message: String) {
            Toast.makeText(context, message, message.length).show()
        }

        fun dateFormatter(date: LocalDateTime, pattern: String): String? {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            return date.format(formatter)
        }


    }
}
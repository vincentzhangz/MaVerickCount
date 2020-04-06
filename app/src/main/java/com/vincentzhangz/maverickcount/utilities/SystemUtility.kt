package com.vincentzhangz.maverickcount.utilities

import android.content.Context
import android.widget.Toast

class SystemUtility {
    companion object {
        fun Context.toast(message: String) {
            Toast.makeText(applicationContext, message, message.length).show()
        }
    }
}
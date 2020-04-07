package com.vincentzhangz.maverickcount.utilities

import android.content.Context
import android.widget.Toast

class SystemUtility {
    companion object {
        fun toast(context: Context, message: String) {
            Toast.makeText(context, message, message.length).show()
        }
    }
}
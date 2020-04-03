package com.vincentzhangz.maverickcount.services

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class ChatService:FirebaseMessagingService(){

    override fun onNewToken(token: String) {
//        super.onNewToken(token)
        Log.d("token",token)
    }

}

class Token(val userId:String, val token:String){
    constructor():this("","")
}

class TokenHelper{
    companion object{
        fun getCurrToken(){
            var tokenData=""
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("token", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    tokenData = token.toString()
                    Log.d("token", "Token Data: "+tokenData)
                })
//            Log.d("token", "Return this : "+tokenData)
//            return tokenData
        }
    }
}
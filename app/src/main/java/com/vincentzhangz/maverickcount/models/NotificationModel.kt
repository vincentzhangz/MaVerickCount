package com.vincentzhangz.maverickcount.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class NotificationModel(notifContext: Context){
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=AAAAKWj3SCI:APA91bFtY6acERyEHgiI8Xx2-NSoeHMvn4mkpBhqaBsPaxdTkabLxS8kp-S4DH5NLNMeZebZfsw8dpBKQjFEKNSRkXdBn72XNX9dQ7oJtr1BtbaWygYirMvdNFa9QP9oWsPx56vPA4AU"
    private val contentType = "application/json"

    fun sendNotif(title:String, message:String, target:String) {
        val notification = JSONObject()
        val notificationBody = JSONObject()

        try {
            notificationBody.put("title", title)
            notificationBody.put("message", message)
            notification.put("to", target)
            notification.put("data", notificationBody)
        } catch (e: JSONException) {
        }

        sendNotification(notification)
    }

    private fun sendNotification(notification: JSONObject) {
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject> { response ->
                Log.i("response", "onResponse: $response")
            },
            Response.ErrorListener {
                Log.i("response", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(notifContext)
    }
}
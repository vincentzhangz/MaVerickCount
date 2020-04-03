package com.vincentzhangz.maverickcount.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessaging : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val sented: String? =remoteMessage.data.get("sented")

        sendNotification(remoteMessage)

    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val user:String?=remoteMessage.data.get("user")
        val icon:String?=remoteMessage.data.get("icon")
        val title:String?=remoteMessage.data.get("title")
        val body:String?=remoteMessage.data.get("body")

        val notification:RemoteMessage.Notification?=remoteMessage.notification;

    }
}
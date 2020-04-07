package com.vincentzhangz.maverickcount.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vincentzhangz.maverickcount.MainActivity
import com.vincentzhangz.maverickcount.R
import java.util.*

class MyFirebaseMessagingService() : FirebaseMessagingService() {
    private val _notificationID = "notification_id"

    override fun onMessageReceived(r_m: RemoteMessage) {
        super.onMessageReceived(r_m)

        Log.d("notif", r_m.data.toString())

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)
//        intent.putExtra("msgId", "msg001")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, _notificationID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setLargeIcon(null)
            .setContentTitle(r_m.data["title"])
            .setContentText(r_m.data["message"])
            .setAutoCancel(true)
            .setSound(notificationSoundUri)
            .setContentIntent(pendingIntent)

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = R.color.colorPrimary
        }
        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val notificationTitle = "New notification"
        val notificationDescription = "Device to device notification"

        val notificationChannel: NotificationChannel
        notificationChannel = NotificationChannel(
            _notificationID,
            notificationTitle,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = notificationDescription
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(notificationChannel)
    }
}
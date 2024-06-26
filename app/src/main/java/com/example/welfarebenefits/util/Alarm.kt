package com.example.welfarebenefits.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.welfarebenefits.R
import com.example.welfarebenefits.activity.LogInActivity
import com.example.welfarebenefits.activity.MainActivity
import com.example.welfarebenefits.activity.SubListActivity
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Alarm(private val id:String, private val context: Context) {
    private var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private lateinit var auth: FirebaseAuth
    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        Log.d("Alerm", "createNotificationChannel")
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    enableVibration(true)
                    description = "description"
                }

        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun deliverNotification(welfareData: WelfareData) {
        Log.d("Alerm", "Delivering notification")
        auth = Firebase.auth
        var intent:Intent

        if(auth.currentUser!=null){
            intent = welfareData?.let {
                Intent(context, SubListActivity::class.java).apply {
                    putExtra("DATA", JsonConverter().dataToJson(it))
                }
            } ?: run {
                Intent(context, MainActivity::class.java)
            }
        }else{
            intent = Intent(context, LogInActivity::class.java)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notificationicons)
            .setShowWhen(true)
            .setContentTitle(welfareData.serviceName)
            .setContentText(welfareData.serviceSummary)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        Log.d("Alerm", "Notification built")
        AlarmUpdater().updateAlarm(id,context,welfareData)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        Log.d("Alerm", "Notification sent")
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
        const val NOTIFICATION_ID = 0
    }
}
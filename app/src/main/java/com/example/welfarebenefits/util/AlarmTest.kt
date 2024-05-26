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
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * NewUser에서 mutableList("")를 하니깐 db에 저장될떄 0:""이렇게 저장되어 시작함
 * guest User일때 id가 "guest"로 겹치는데 동적으로 게스트들의 id를 guest1,guest2,...로 하던가
 * huest는 알림을 지원하지말아야할듯 => id가 "guest", "userid"인지에 따라 알림 기능을 막아야할듯
 */
class AlarmTest(private val id:String,private val context: Context) {
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
        val intent:Intent

        if(auth.currentUser!=null){
            intent = Intent(context, MainActivity::class.java)
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
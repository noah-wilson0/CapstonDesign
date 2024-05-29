package com.example.welfarebenefits.util

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.welfarebenefits.entity.WelfareData
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmScheduler(private val context: Context) {

    fun scheduleDailyAlarm(hour: Int, minute: Int,id:String,welfareData: WelfareData) {
        val welfareDataJson=JsonConverter().DataToJson(welfareData)
        val data= Data.Builder()
            .putString("id",id)
            .putString("welfareData",welfareDataJson)
            .build()

        // 현재 시간 기준으로 다음 알람 시간을 계산
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        val now = Calendar.getInstance()

        // 다음 실행 시간 계산
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = calendar.timeInMillis - now.timeInMillis

        // WorkRequest 생성
        val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        // WorkManager에 작업 예약
        WorkManager.getInstance(context).enqueue(workRequest)

        // 매일 반복되도록 WorkRequest 설정
        val repeatingRequest = PeriodicWorkRequestBuilder<AlarmWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "dailyAlarm",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}

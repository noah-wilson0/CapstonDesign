package com.example.welfarebenefits.util

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class AlarmWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val id = inputData.getString("id") ?: return Result.failure()
        val welfareDataJson = inputData.getString("welfareData") ?: return Result.failure()

        val welfareData=JsonConverter().jsonToData(welfareDataJson)
        Alarm(id, applicationContext).deliverNotification(welfareData)

        return Result.success()
    }
}

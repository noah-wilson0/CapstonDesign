package com.example.welfarebenefits.util

import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.FirebaseDatabase

class AlarmUpdater {
    private val database = FirebaseDatabase.getInstance().reference

    fun updateAlarm(id:String, context: Context, welfareData: WelfareData) {
        database.child(id).child(context.getString(R.string.alarm)).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val alarmList = (dataSnapshot.value as? MutableList<String>) ?: mutableListOf()

                val newAlarm = "${welfareData.serviceName}: ${welfareData.serviceSummary}"

                if (!alarmList.contains(newAlarm)) {
                    alarmList.add(newAlarm)

                    if (alarmList.size > 10) {
                        alarmList.removeAt(0)
                    }
                    database.child(id).child(context.getString(R.string.alarm)).setValue(alarmList)
                }
            } else {
                Log.e("AlarmUpdater", "Error getting alarm data", task.exception)
            }
        }
    }
}



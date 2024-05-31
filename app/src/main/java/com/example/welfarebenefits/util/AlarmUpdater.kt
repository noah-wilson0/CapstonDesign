package com.example.welfarebenefits.util

import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.FirebaseDatabase

class AlarmUpdater {
    private val database = FirebaseDatabase.getInstance().reference

    fun updateAlarm(id: String, context: Context, welfareData: WelfareData) {
        val alarmList = mutableListOf<WelfareData>()

        // alarmList 가져오기
        database.child(id).child(context.getString(R.string.alarm)).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val alarmListFromDatabase = dataSnapshot.value as? List<WelfareData> ?: emptyList()
                if (alarmListFromDatabase != null) {
                    alarmList.addAll(alarmListFromDatabase)
                }
                alarmList.add(welfareData)

                // alarmList 크기가 10을 초과하는 경우, 오래된 항목 제거
                while (alarmList.size > 10) {
                    alarmList.removeAt(0)
                }

                // 업데이트된 alarmList를 데이터베이스에 저장
                database.child(id).child(context.getString(R.string.alarm)).setValue(alarmList)
            } else {
                Log.e("AlarmUpdater", "Error getting alarm data", task.exception)
            }
        }
    }
}

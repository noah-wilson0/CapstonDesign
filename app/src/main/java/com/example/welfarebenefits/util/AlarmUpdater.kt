//package com.example.welfarebenefits.util
//
//import android.content.Context
//import android.util.Log
//import com.example.welfarebenefits.R
//import com.example.welfarebenefits.entity.WelfareData
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.GenericTypeIndicator
//
//class AlarmUpdater {
//    private val database = FirebaseDatabase.getInstance().reference
//
//    fun updateAlarm(id:String, context: Context, welfareData: WelfareData) {
//        database.child(id).child(context.getString(R.string.alarm)).get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val dataSnapshot = task.result
//                val alarmMap = (dataSnapshot.getValue(object : GenericTypeIndicator<Map<String, WelfareData>>() {}) ?: mutableMapOf()).toMutableMap()
//
//                if (!alarmMap.containsKey(welfareData.serviceName)) {
//                    alarmMap[welfareData.serviceName] = welfareData
//
//                    // 가장 오래된 항목 제거 (키 목록의 첫 번째 항목을 제거)
//                    if (alarmMap.size > 10) {
//                        val oldestKey = alarmMap.keys.firstOrNull()
//                        if (oldestKey != null) {
//                            alarmMap.remove(oldestKey)
//                        }
//                    }
//
//                    // 업데이트된 맵을 데이터베이스에 저장
//                    database.child(id).child(context.getString(R.string.alarm)).setValue(alarmMap)
//                }
//            } else {
//                Log.e("AlarmUpdater", "Error getting alarm data", task.exception)
//            }
//        }
//    }
//}
//
//
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

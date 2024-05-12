package com.example.welfarebenefits.util

import android.util.Log
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class FirebaseWelfareData {
    private lateinit var database: DatabaseReference
    fun getWelfareData(callback:CallBackWelfareData){
        var welfareDataList: MutableList<WelfareData> = mutableListOf()
        database = Firebase.database.reference.child("data")
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    for(dataSnapshotChild  in dataSnapshot.children) {
                        var welfareDataSnapshot = dataSnapshotChild.value as HashMap<String, *>
                        val welfareData = WelfareData(
                            welfareDataSnapshot["사용자구분"] as String ?: "",
                            welfareDataSnapshot["상세조회URL"] as String ?: "",
                            welfareDataSnapshot["서비스ID"] as String ?: "",
                            welfareDataSnapshot["서비스명"] as String ?: "",
                            welfareDataSnapshot["서비스목적요약"] as String ?: "",
                            welfareDataSnapshot["서비스분야"] as String ?: "",
                            welfareDataSnapshot["선정기준"] as String ?: "",
                            welfareDataSnapshot["신청기한"] as String ?: "",
                            welfareDataSnapshot["신청방법"] as String ?: "",
                            welfareDataSnapshot["지원내용"] as String ?: ""
                        )
                        welfareDataList.add(welfareData)
                    }
                }
//                Log.e("FirebaseWelfareDataList", welfareDataList.toString())
                callback.getWelfareData(welfareDataList.toList())
            } else { // 실패햇을 경우 처리하기
                Log.e("TAG", "db에서 데이터 가져오기 실패", task.exception)

            }
        }

    }


}
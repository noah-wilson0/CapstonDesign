package com.example.welfarebenefits.util

import android.util.Log
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class WelfareDataFetcher {
    private lateinit var database: DatabaseReference
    fun getWelfareData(callback:CallBackWelfareData) {
        var welfareDataList: MutableList<WelfareData> = mutableListOf()
        database = Firebase.database.reference.child("data")
        var categoryMap: MutableMap<String, MutableList<WelfareData>> = mutableMapOf()
        database.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    for(category in dataSnapshot.children){
                        val categoryName = category.key ?: ""
                        val categoryList: MutableList<WelfareData> = mutableListOf()
                        for(name in category.children){
                            val welfareDataSnapshot=name.value as HashMap<String,*>
                            val welfareData = WelfareData(
                            welfareDataSnapshot["detailURL"] as String ?: "",
                            welfareDataSnapshot["serviceID"] as String ?: "",
                            welfareDataSnapshot["serviceName"] as String ?: "",
                            welfareDataSnapshot["serviceSummary"] as String ?: "",
                            welfareDataSnapshot["selectionCriteria"] as String ?: "",
                            welfareDataSnapshot["applicationDeadline"] as String ?: "",
                            welfareDataSnapshot["applicationMethod"] as String ?: "",
                            welfareDataSnapshot["supportContent"] as String ?: ""
                        )
                            welfareDataList.add(welfareData)
                            categoryList.add(welfareData)
                        }
                        categoryMap[categoryName] = categoryList
                    }
                }
                categoryMap["전체(맞춤)"]=welfareDataList
                WelfareCategoryMap.setCategoryMap(categoryMap)
                callback.getWelfareData(welfareDataList.toList())
                } else { // 실패햇을 경우 처리하기
                    Log.e("TAG", "db에서 데이터 가져오기 실패", task.exception)

                }
            }
    }

}
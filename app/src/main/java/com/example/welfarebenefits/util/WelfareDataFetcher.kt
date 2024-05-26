package com.example.welfarebenefits.util

import android.util.Log
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class WelfareDataFetcher {
    private lateinit var database: DatabaseReference

    fun getWelfareData(callback: CallBackWelfareData) {
        database = FirebaseDatabase.getInstance().reference

        // Step 1: Get user info including residence, gender, and significant
        database.child("id1").child("UserInfo").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    val userInfo = snapshot.value as HashMap<String, *>
                    val residence = userInfo["residence"] as String ?: ""
                    val gender = userInfo["gender"] as String ?: ""
                    val significant = userInfo["significant"]?: listOf<String>()
                    Log.e("getWelfareData", "Residence: $residence, Gender: $gender, Significant: $significant")

                    fetchWelfareData(residence, gender, significant as List<String>, callback)
                } else {
                    Log.e("Fetcher", "No user info available")
                }
            } else {
                Log.e("Fetcher", "Error getting user info", task.exception)
            }
        }
    }

    private fun fetchWelfareData(residence: String, gender:String, significant: List<String>, callback: CallBackWelfareData) {

        database.child("data").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    val welfareDataList: MutableList<WelfareData> = mutableListOf()
                    val categoryMap: MutableMap<String, MutableList<WelfareData>> = mutableMapOf()

                    for (category in dataSnapshot.children) {
                        val categoryName = category.key ?: ""
                        val categoryList: MutableList<WelfareData> = mutableListOf()

                        for (name in category.children) {
                            val welfareDataSnapshot = name.value as HashMap<String, *>
                            val serviceName=welfareDataSnapshot["serviceName"] as String? ?: ""
                            val serviceSummary=welfareDataSnapshot["serviceSummary"] as String? ?: ""
                            val supportContent = welfareDataSnapshot["supportContent"] as String? ?: ""
                            if (supportContent.contains(residence) || supportContent.contains(gender) || significant.any { supportContent.contains(it) }
                                || serviceName.contains(residence) || serviceName.contains(gender) || significant.any { serviceName.contains(it) }
                                || serviceSummary.contains(residence) || serviceSummary.contains(gender) || significant.any { serviceSummary.contains(it) }){
                                val welfareData = WelfareData(
                                    welfareDataSnapshot["detailURL"] as String? ?: "",
                                    welfareDataSnapshot["serviceID"] as String? ?: "",
                                    serviceName,
                                    serviceSummary,
                                    welfareDataSnapshot["selectionCriteria"] as String? ?: "",
                                    welfareDataSnapshot["applicationDeadline"] as String? ?: "",
                                    welfareDataSnapshot["applicationMethod"] as String? ?: "",
                                    supportContent
                                )
                                welfareDataList.add(welfareData)
                                categoryList.add(welfareData)
                            }
                        }
                        categoryMap[categoryName] = categoryList
                    }
                    categoryMap["전체(맞춤)"] = welfareDataList
                    WelfareCategoryMap.setCategoryMap(categoryMap)
                    callback.getWelfareData(welfareDataList.toList())
                } else {
                    Log.e("TAG", "No data available")
                }
            } else {
                Log.e("TAG", "Error getting data", task.exception)
            }
        }
    }
}

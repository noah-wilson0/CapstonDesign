package com.example.welfarebenefits.util

import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareCentralAgencyList
import com.example.welfarebenefits.entity.WelfareCentralAgencyMap
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class WelfareDataFetcher {
    private lateinit var database: DatabaseReference

    fun getWelfareData(context: Context,id:String, callback: CallBackWelfareData) {
        database = FirebaseDatabase.getInstance().reference
        Log.e("getWelfareData",  id)
        // Step 1: Get user info including residence, gender, and significant
        database.child(id).child(context.getString(R.string.UserInfo)).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    val userInfo = snapshot.value as HashMap<String, *>
                    val residence = userInfo["residence"] as String
                    val gender = userInfo["gender"] as String
                    val significant = userInfo["significant"]?: listOf<String>()
                    Log.e("getWelfareData", "Residence: $residence, Gender: $gender, Significant: $significant")

                    fetchWelfareData(residence, gender, significant as List<String>, callback)
                } else {
                    Log.e("getWelfareData", "No user info available")
                }
            } else {
                Log.e("getWelfareData", "Error getting user info", task.exception)
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
                            val agencyName=welfareDataSnapshot["agencyName"] as String? ?:""
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
                                    supportContent,
                                    agencyName
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
                    Log.e("fetchWelfareData", "No data available")
                }
            } else {
                Log.e("fetchWelfareData", "Error getting data", task.exception)
            }
        }
    }

    /**
     * 정부기관만 저장하는 리스트와 Map 클래스를 따로 만들어야할듯
     */
    fun fetchCentralAgencyWelfareData( callback: CallBackWelfareData) {
        database = FirebaseDatabase.getInstance().reference
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
                            val agencyName=welfareDataSnapshot["agencyName"] as String? ?:""
                            if (agencyName=="중앙행정기관"){
                                val welfareData = WelfareData(
                                    welfareDataSnapshot["detailURL"] as String? ?: "",
                                    welfareDataSnapshot["serviceID"] as String? ?: "",
                                    welfareDataSnapshot["serviceName"] as String? ?: "",
                                    welfareDataSnapshot["serviceSummary"] as String? ?: "",
                                    welfareDataSnapshot["selectionCriteria"] as String? ?: "",
                                    welfareDataSnapshot["applicationDeadline"] as String? ?: "",
                                    welfareDataSnapshot["applicationMethod"] as String? ?: "",
                                    welfareDataSnapshot["supportContent"] as String? ?: "",
                                    agencyName
                                )
                                welfareDataList.add(welfareData)
                                categoryList.add(welfareData)
                                WelfareCentralAgencyList.addWelfareCentralAgencyList(welfareData)
                            }
                        }
                        categoryMap[categoryName] = categoryList
                    }
                    categoryMap["전체(맞춤)"] = welfareDataList
                    WelfareCentralAgencyMap.setCategoryMap(categoryMap)
                    callback.getWelfareData(welfareDataList.toList())
                } else {
                    Log.e("fetchCentralAgencyWelfareData", "No data available")
                }
            } else {
                Log.e("fetchCentralAgencyWelfareData", "Error getting data", task.exception)
            }
        }
    }

    fun getWelfareData(bookmarkList:Array<*>,callback:CallBackWelfareData) {
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
                                welfareDataSnapshot["detailURL"] as String,
                                welfareDataSnapshot["serviceID"] as String,
                                welfareDataSnapshot["serviceName"] as String,
                                welfareDataSnapshot["serviceSummary"] as String,
                                welfareDataSnapshot["selectionCriteria"] as String,
                                welfareDataSnapshot["applicationDeadline"] as String,
                                welfareDataSnapshot["applicationMethod"] as String,
                                welfareDataSnapshot["supportContent"] as String,
                                welfareDataSnapshot["agencyName"] as String? ?:""
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
    fun getWelfareDataGUEST(callback:CallBackWelfareData){
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
                                welfareDataSnapshot["detailURL"] as String,
                                welfareDataSnapshot["serviceID"] as String,
                                welfareDataSnapshot["serviceName"] as String,
                                welfareDataSnapshot["serviceSummary"] as String,
                                welfareDataSnapshot["selectionCriteria"] as String,
                                welfareDataSnapshot["applicationDeadline"] as String,
                                welfareDataSnapshot["applicationMethod"] as String,
                                welfareDataSnapshot["supportContent"] as String,
                                welfareDataSnapshot["agencyName"] as String? ?:""
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
                Log.e("getWelfareDataGUEST", "db에서 데이터 가져오기 실패", task.exception)

            }
        }
    }

    fun getAlarmData(context: Context,id:String,callback:CallBackWelfareData){
        database=FirebaseDatabase.getInstance().reference
        var welfareDataList: MutableList<WelfareData> = mutableListOf()
        database.child(id).child(context.resources.getString(R.string.alarm)).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val dataSnapshot=task.result
                if (dataSnapshot.exists()){
//                    val alarmListFromDatabase = dataSnapshot.value as List<HashMap<String,*>>
                    val alarmListFromDatabase = dataSnapshot.getValue(object :
                        GenericTypeIndicator<List<HashMap<String, *>>>
                        () {})
                    if (alarmListFromDatabase != null) {
                        for(alarmMap in alarmListFromDatabase){
                            val welfareData = WelfareData(
                                alarmMap["detailURL"] as String,
                                alarmMap["serviceID"] as String,
                                alarmMap["serviceName"] as String,
                                alarmMap["serviceSummary"] as String,
                                alarmMap["selectionCriteria"] as String,
                                alarmMap["applicationDeadline"] as String,
                                alarmMap["applicationMethod"] as String,
                                alarmMap["supportContent"] as String,
                                alarmMap["agencyName"] as String? ?:""
                            )
                            welfareDataList.add(welfareData)
                        }
                    }

                    callback.getWelfareData(welfareDataList)

                } else{
                    Log.e("getAlarmData", "No alarm data available")
                }
            }else{
                Log.e("getAlarmData", "Error getting alarm data")
            }
        }

    }

}


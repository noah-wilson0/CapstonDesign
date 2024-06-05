package com.example.welfarebenefits.util

import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareBookmarkList
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareCentralAgencyList
import com.example.welfarebenefits.entity.WelfareCentralAgencyMap
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
                    val residence = (userInfo["residence"] as List<String>)
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

    private fun fetchWelfareData(residence: List<String>, gender:String, significant: List<String>, callback: CallBackWelfareData) {

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
                            if ((supportContent.contains(residence[0]) && supportContent.contains(residence[1]))|| supportContent.contains(gender) || significant.any { supportContent.contains(it) }
                                || (supportContent.contains(residence[0]) && supportContent.contains(residence[1])) || serviceName.contains(gender) || significant.any { serviceName.contains(it) }
                                || (supportContent.contains(residence[0]) && supportContent.contains(residence[1])) || serviceSummary.contains(gender) || significant.any { serviceSummary.contains(it) }){
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

    fun getBookmarkData(callback: CallBackWelfareDataByBookmark) {
        database = FirebaseDatabase.getInstance().reference
        database.child("data").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val datasnapshot = task.result
                if (datasnapshot.exists()) {
                    val welfareDataList: MutableList<WelfareData> = mutableListOf()
                    val bookmarkList = WelfareBookmarkList.getBookmarkList()
                    Log.e("BookmarkList", bookmarkList.toString())
                    for (category in datasnapshot.children) {
                        for (data in category.children) {
                            val welfareData = data.value as HashMap<String, *>
                            if (bookmarkList.isEmpty()){
                                break
                            }
                            if(bookmarkList.contains(welfareData["serviceName"])){
                                val welfareData = WelfareData(
                                    welfareData["detailURL"] as String,            //상세조회URL
                                    welfareData["serviceID"] as String,            //서비스ID
                                    welfareData["serviceName"] as String,          //서비스명
                                    welfareData["serviceSummary"] as String,       //서비스목적요약
                                    welfareData["selectionCriteria"] as String,    //선정기준
                                    welfareData["applicationDeadline"] as String,  //신청기한
                                    welfareData["applicationMethod"] as String,    //신청방법
                                    welfareData["supportContent"] as String,       //지원내용
                                    welfareData["agencyName"] as String
                                )
                                welfareDataList.add(welfareData)
                                bookmarkList.remove(welfareData.serviceName)
                            }
                        }
                    }
                    callback.getBookmarkData(welfareDataList.toList())
                    Log.e("123", "성공")
                } else {
                    Log.e("123", "ERROR")
                }
            } else {
                Log.e("123", "Error", task.exception)
            }
        }
    }
    fun getBookmarkDataList(id:String, context:Context){
        if(id=="guest"){
        }
        else{
            database = FirebaseDatabase.getInstance().reference
            database.child(id).child(context.getString(R.string.Bookmarks)).get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val snapshot = task.result
                    if(snapshot.exists()){
                        WelfareBookmarkList.setBookmarkList(snapshot.value as MutableList<String>)
                        Log.e("BookmarkList","북마크정보 불러오기 성공")
                    }
                    else{
                        Log.e("Bookmark","북마크정보 불러오기 실패")
                    }
                }else{
                    Log.e("Bookmark","북마크정보 불러오기 실패!!")
                }
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
fun getAlarmData(context: Context, id: String, callback: CallBackWelfareData) {
    database = FirebaseDatabase.getInstance().reference
    val welfareDataList: MutableList<WelfareData> = mutableListOf()

    database.child(id).child(context.resources.getString(R.string.alarm)).get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val dataSnapshot = task.result
            if (dataSnapshot.exists()) {
                val value = dataSnapshot.value
                if (value is String) {
                    // 문자열인 경우 처리하지 않음
                    return@addOnCompleteListener
                } else if (value is List<*>) {
                    // 리스트인 경우만 처리
                    val alarmListFromDatabase = value as List<*>
                    for (alarmItem in alarmListFromDatabase) {
                        if (alarmItem is HashMap<*, *>) {
                            val alarmMap = alarmItem as HashMap<String, *>
                            if (alarmMap.values.all { it is String }) {
                                val welfareData = WelfareData(
                                    alarmMap["detailURL"] as String,
                                    alarmMap["serviceID"] as String,
                                    alarmMap["serviceName"] as String,
                                    alarmMap["serviceSummary"] as String,
                                    alarmMap["selectionCriteria"] as String,
                                    alarmMap["applicationDeadline"] as String,
                                    alarmMap["applicationMethod"] as String,
                                    alarmMap["supportContent"] as String,
                                    alarmMap["agencyName"] as String? ?: ""
                                )
                                welfareDataList.add(welfareData)
                            }
                        }
                    }
                    callback.getWelfareData(welfareDataList)
                }
            } else {
                Log.e("getAlarmData", "No alarm data available")
            }
        } else {
            Log.e("getAlarmData", "Error getting alarm data")
        }
    }
}



}


package com.example.welfarebenefits.util

import android.content.Context
import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareBookmarkList
import com.example.welfarebenefits.entity.WelfareCategoryMap
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class WelfareDataFetcher {
    private lateinit var database: DatabaseReference

    fun getWelfareData(context: Context,id:String, callback: CallBackWelfareData) {
        database = FirebaseDatabase.getInstance().reference
        database.child(id).child(context.getString(R.string.Bookmarks)).get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val snapshot = task.result
                if(snapshot.exists()){
                    WelfareBookmarkList.setBookmarkList(snapshot.value as MutableList<String>)
                }
                else{
                    Log.e("Bookmark","북마크정보 불러오기 실패")
                }
            }else{
                Log.e("Bookmark","북마크정보 불러오기 실패!!")
            }
        }
        // Step 1: Get user info including residence, gender, and significant
        database.child(id).child(context.getString(R.string.UserInfo)).get().addOnCompleteListener { task ->
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

    fun getBookmarkData(callback: CallBackWelfareDataByBookmark) {
        database = FirebaseDatabase.getInstance().reference
        database.child("data").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val datasnapshot = task.result
                if (datasnapshot.exists()) {
                    val welfareDataList: MutableList<WelfareData> = mutableListOf()
                    val bookmarkList = WelfareBookmarkList.getBookmarkList()
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

}


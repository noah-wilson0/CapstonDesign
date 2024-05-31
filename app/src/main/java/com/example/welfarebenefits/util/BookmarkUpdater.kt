package com.example.welfarebenefits.util

import android.util.Log
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareData
import com.google.firebase.database.FirebaseDatabase

class BookmarkUpdater {
    private val database = FirebaseDatabase.getInstance().reference

    fun updateBookmark(id:String, welfareData:WelfareData){
        database.child(id).child("bookmarks").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                val bookmarkList = (dataSnapshot.value as? MutableList<String>) ?: mutableListOf()

                val bookmark = welfareData.serviceName

                if (bookmarkList.contains(bookmark)) {
                    bookmarkList.remove(bookmark)
                }else{
                    bookmarkList.add(bookmark)
                }
                database.child(id).child("bookmarks").setValue(bookmarkList)
            } else {
                Log.e("BookmarkUpdater", "Error getting bookmark data", task.exception)
            }
        }
    }
}
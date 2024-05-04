package com.example.welfarebenefits.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.welfarebenefits.activity.UserInfoActivity
import com.example.welfarebenefits.entity.User
import com.google.gson.Gson

//메서드 분리하기 (인텐트, json변환메서드)
class JsonConverter {
    companion object {
        fun startNextActivity(activity: Activity, user: User) {
            val gson = Gson()
            val userJson = gson.toJson(user)
            Log.e("togojon",userJson)

            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra("userJson", userJson)
            activity.startActivity(intent)
        }
    }
}
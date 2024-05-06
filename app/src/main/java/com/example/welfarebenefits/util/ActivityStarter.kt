package com.example.welfarebenefits.util

import android.app.Activity
import android.content.Intent
import com.example.welfarebenefits.activity.UserInfoActivity
import com.example.welfarebenefits.entity.User
import com.google.gson.Gson

//메서드 분리하기 (인텐트, json변환메서드)
class ActivityStarter {
    companion object {
        fun startNextActivity(activity: Activity, user: User) {
            val gson = Gson()
            val userJson = gson.toJson(user)
            val intent = Intent(activity, UserInfoActivity::class.java)
            intent.putExtra("user", userJson)
            activity.startActivity(intent)
        }
    }
}
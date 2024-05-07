package com.example.welfarebenefits.util

import android.app.Activity
import android.content.Intent
import com.example.welfarebenefits.entity.User


//메서드 분리하기 (인텐트, json변환메서드)
class ActivityStarter {
    companion object {
        fun startNextActivity(activity: Activity,nextActivity:Class<*>, user: User) {
            val userJson = JsonConverter().userToJson(user)
            val intent = Intent(activity, nextActivity)
            intent.putExtra("user", userJson)
            activity.startActivity(intent)
        }
        fun startNextActivity(activity: Activity,nextActivity:Class<*>){
            val intent = Intent(activity, nextActivity)
            activity.startActivity(intent)
        }

    }
}
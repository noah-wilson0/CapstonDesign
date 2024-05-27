package com.example.welfarebenefits.util

import com.example.welfarebenefits.entity.User
import com.example.welfarebenefits.entity.WelfareData
import com.google.gson.Gson


class JsonConverter {
    private val gson = Gson()

    fun userToJson(obj:Any):String {
        return gson.toJson(obj)
    }
    fun jsonToUser(json: String):User {
        return gson.fromJson(json,User::class.java )
    }
    fun jsonToData(json: String):WelfareData {
        return gson.fromJson(json,WelfareData::class.java )
    }

}

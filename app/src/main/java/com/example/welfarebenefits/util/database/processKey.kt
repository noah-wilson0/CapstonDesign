package com.example.welfarebenefits.util.database
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.File

fun main(){

// JSON 파일 경로
    val filePath = "C:\\Users\\Hanseo\\Desktop\\welfareDataFullEXx.json"

// Gson 객체 생성
    val gson = Gson()

// JSON 파일 읽기
    val file = File(filePath)
    val jsonString = file.readText()

// JSON 문자열을 객체로 변환
    val filedata = gson.fromJson(jsonString, JsonObject::class.java)
    val datas = filedata["data"].asJsonObject
    println(datas.keySet());
    println()
    val data = datas.keySet()
    var modifiedString = ""
    for(typeString in data){
        for(keyString in datas[typeString].asJsonObject.keySet()){
            if("." in keyString) {
                println(keyString)
                modifiedString = keyString.replace(".", "·")
                println(modifiedString);
            }else if("$" in keyString) {
                println(keyString)
                modifiedString = keyString.replace("$", " ")
                println(modifiedString);
            }
            else if("#" in keyString) {
                println(keyString)
                modifiedString = keyString.replace("#", " ")
                println(modifiedString);
            }
            else if("[" in keyString || "]" in keyString) {
                println(keyString)
                modifiedString = keyString.replace("[", "(")
                modifiedString = modifiedString.replace("]", ")")
                println(modifiedString);
            }
            else if("/" in keyString) {
                println(keyString)
                modifiedString = keyString.replace("/", "·")
                println(modifiedString);
            }
        }
    }


}
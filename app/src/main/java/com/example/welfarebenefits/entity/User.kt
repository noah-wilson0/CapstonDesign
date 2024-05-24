package com.example.welfarebenefits.entity

data class User(
    var id:String,
    var password:String,
    var name:String,
    var gender:String,
    var residence:String,
    var significant: MutableList<String?>
)
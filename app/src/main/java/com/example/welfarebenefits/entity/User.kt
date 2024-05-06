package com.example.welfarebenefits.entity

import java.io.Serializable

data class User(
    var id:String,
    var password:String,
    var name:String,
    var avgIncome:String,
    var familyStructure:String,
    var residence:String,
    var significant: MutableList<String?>
):Serializable
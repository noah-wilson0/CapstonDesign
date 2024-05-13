package com.example.welfarebenefits.entity

data class WelfareData (
    //val userType:String,             //사용자구분
    val detailURL:String,            //상세조회URL
    val serviceID:String,            //서비스ID
    val serviceName:String,          //서비스명
    val serviceSummary:String,       //서비스목적요약
    //val serviceCategory:String,      //서비스분야
    val selectionCriteria:String,    //선정기준
    val applicationDeadline:String,  //신청기한
    val applicationMethod:String,    //신청 방법
    val supportContent:String        //지원 내용
)
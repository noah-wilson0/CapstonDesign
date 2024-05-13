package com.example.welfarebenefits.entity

data class WelfareData (
    val detailURL:String,            //상세조회URL
    val serviceID:String,            //서비스ID
    val serviceName:String,          //서비스명
    val serviceSummary:String,       //서비스목적요약
    val selectionCriteria:String,    //선정기준
    val applicationDeadline:String,  //신청기한
    val applicationMethod:String,    //신청방법
    val supportContent:String        //지원내용
)
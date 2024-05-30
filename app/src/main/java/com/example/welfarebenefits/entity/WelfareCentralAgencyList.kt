package com.example.welfarebenefits.entity

object WelfareCentralAgencyList {
    private  var welfareCentralAgencyList:MutableList<WelfareData> = mutableListOf()

    fun setWelfareCentralAgencyList(welfareCentralAgencyList:MutableList<WelfareData>){
        this.welfareCentralAgencyList=welfareCentralAgencyList
    }
    fun addWelfareCentralAgencyList(welfareData: WelfareData){
        welfareCentralAgencyList.add(welfareData)
    }
    fun getWelfareCentralAgencyList():List<WelfareData>{
        return welfareCentralAgencyList
    }

}
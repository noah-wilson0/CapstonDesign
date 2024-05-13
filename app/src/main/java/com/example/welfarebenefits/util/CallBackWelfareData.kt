package com.example.welfarebenefits.util

import com.example.welfarebenefits.entity.WelfareData

interface CallBackWelfareData {
    fun getWelfareData(welfareDataList: List<WelfareData>)
}
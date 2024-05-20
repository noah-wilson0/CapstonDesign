package com.example.welfarebenefits.entity

object WelfareCategoryMap {
    private var categoryMap: MutableMap<String, MutableList<WelfareData>> = mutableMapOf()

    // categoryMap을 설정하는 메서드
    fun setCategoryMap(categoryMap: MutableMap<String, MutableList<WelfareData>>) {
        this.categoryMap = categoryMap
    }

    // categoryMap을 가져오는 메서드
    fun getCategoryMap(): Map<String, MutableList<WelfareData>> {
        return categoryMap
    }

    fun getCategoryMap(category: String): List<WelfareData>? {
        return categoryMap[category]
    }
}


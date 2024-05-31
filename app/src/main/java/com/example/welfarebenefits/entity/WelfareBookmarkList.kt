package com.example.welfarebenefits.entity

object WelfareBookmarkList {
    private var bookmarkList: MutableList<String> = mutableListOf()

    fun setBookmarkList(bookmarkList: MutableList<String>){
        this.bookmarkList = bookmarkList
    }

    fun getBookmarkList(): MutableList<String> {
        return ArrayList(bookmarkList)
    }

    fun getBookmarkList(real:Int): MutableList<String> {
        return bookmarkList
    }



    fun checkBookmarkList(welfareData: WelfareData):Boolean{
        if(bookmarkList.contains(welfareData.serviceName)){
            bookmarkList.remove(welfareData.serviceName)
            return false
        }else{
            bookmarkList.add(welfareData.serviceName)
            return true
        }
    }
}
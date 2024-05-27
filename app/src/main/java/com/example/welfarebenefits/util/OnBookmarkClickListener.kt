package com.example.welfarebenefits.util

import com.example.welfarebenefits.entity.User

interface OnBookmarkClickListener {
    fun onBookmarkClick(bookmark: Array<*>)
    fun onBookmarkClick(id: String)
}
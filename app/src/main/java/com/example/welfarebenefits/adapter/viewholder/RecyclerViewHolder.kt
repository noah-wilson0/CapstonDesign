package com.example.welfarebenefits.adapter.viewholder

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.welfarebenefits.databinding.RecyclerviewItemBinding
import com.example.welfarebenefits.entity.WelfareBookmarkList
import com.example.welfarebenefits.entity.WelfareData

class RecyclerViewHolder(private  val binding: RecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root) {
    val bookmarkBtn:Button = binding.bookmark
    private val bookmarkList = WelfareBookmarkList.getBookmarkList(1)
    fun bind(welfareData: WelfareData) {
        binding.titleTV.text = welfareData.serviceName
        binding.descriptionTV.text = welfareData.serviceSummary
        if (bookmarkList.contains(welfareData.serviceName)){
            bookmarkBtn.setBackgroundResource(R.drawable.bookmark_remove)
        }else{
            bookmarkBtn.setBackgroundResource(R.drawable.bookmark_add)
        }
    }


}

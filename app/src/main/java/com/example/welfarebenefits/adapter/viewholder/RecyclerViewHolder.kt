package com.example.welfarebenefits.adapter.viewholder

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.RecyclerviewItemBinding
import com.example.welfarebenefits.entity.WelfareData

class RecyclerViewHolder(private  val binding: RecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root) {
    val bookmarkBtn:Button = binding.bookmark
    fun bind(welfareData: WelfareData) {
        binding.titleTV.text = welfareData.serviceName
        binding.descriptionTV.text = welfareData.serviceSummary
//        if(marked) bookmarkBtn.setBackgroundResource(R.drawable.bookmark_remove)
//        else bookmarkBtn.setBackgroundResource(R.drawable.bookmark_add)
    }


}

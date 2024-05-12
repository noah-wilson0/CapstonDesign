package com.example.welfarebenefits.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.welfarebenefits.databinding.RecyclerviewItemBinding
import com.example.welfarebenefits.entity.WelfareData

class RecyclerViewHolder(private  val binding: RecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(welfareData: WelfareData) {
        binding.titleTV.text = welfareData.serviceName
        binding.descriptionTV.text = welfareData.serviceSummary
        binding.category.text = welfareData.userType
    }


}

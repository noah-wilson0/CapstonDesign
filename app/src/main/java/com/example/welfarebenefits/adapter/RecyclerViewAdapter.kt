package com.example.welfarebenefits.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.welfarebenefits.R
import com.example.welfarebenefits.adapter.viewholder.RecyclerViewHolder
import com.example.welfarebenefits.databinding.RecyclerviewItemBinding
import com.example.welfarebenefits.entity.WelfareBookmarkList
import com.example.welfarebenefits.entity.WelfareData

class RecyclerViewAdapter(private val welfareDataList:List<WelfareData>, private val listener:OnItemClickListener): RecyclerView.Adapter<RecyclerViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  RecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  welfareDataList.size
    }

    fun getItem(position: Int): WelfareData {
        return welfareDataList[position]
    }



    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = welfareDataList[position]

//        Log.e("RecyclerViewAdapter","${welfareDataList[0].serviceSummary}와${welfareDataList[1].serviceSummary}")
//        Log.e("RecyclerViewAdapter", "Binding item at position: $position, serviceName: ${currentItem.serviceName}")
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
        holder.bookmarkBtn.setOnClickListener{
            if(WelfareBookmarkList.checkBookmarkList(currentItem)){
                holder.bookmarkBtn.setBackgroundResource(R.drawable.bookmark_remove)
            }else{
                holder.bookmarkBtn.setBackgroundResource(R.drawable.bookmark_add)
            }
            listener.onButtonClick(position)
        }
    }

}
interface OnItemClickListener {
    fun onItemClick(position: Int)
    fun onButtonClick(position: Int)
}

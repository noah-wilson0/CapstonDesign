package com.example.welfarebenefits.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.R
import com.example.welfarebenefits.adapter.OnItemClickListener
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.ActivityBookmarkListBinding
import com.example.welfarebenefits.databinding.ActivitySubListBinding
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.CallBackWelfareData
import com.example.welfarebenefits.util.WelfareDataFetcher

class Bookmark_listActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding : ActivityBookmarkListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bookmarkList = intent.getStringArrayExtra("bookmarkList")
        if(intent.getStringArrayExtra("bookmarkList").isNullOrEmpty()){
            Log.e("BookmarkListActivity","게스트회원 사용자 북마크 조회")
        }
        else{
            if (bookmarkList != null) {
                binding.mosaicBackgroundBookmark.visibility = View.GONE
                WelfareDataFetcher().getWelfareData(bookmarkList ,object : CallBackWelfareData {
                    override fun getWelfareData(welfareDataList: List<WelfareData>) {
                        Log.e("MainActivity", "Received welfare data: ${welfareDataList.size} items")
                        val recyclerViewAdapter = RecyclerViewAdapter(welfareDataList, this@Bookmark_listActivity)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@Bookmark_listActivity)
                        binding.recyclerView.adapter = recyclerViewAdapter
                    }
                })
            }
        }
    }

    override fun onItemClick(position: Int) {
        val clickedItem = binding.recyclerView.adapter?.let { adapter ->
            if (adapter is RecyclerViewAdapter) {
                adapter.getItem(position)
            } else null
        }
        clickedItem?.let {
            val intent = Intent(this, SubListActivity::class.java).apply {
                putExtra("SERVICE_URL", it.detailURL)
                putExtra("SERVICE_ID", it.serviceID)
                putExtra("SERVICE_NAME", it.serviceName)
                putExtra("SERVICE_SUMMARY", it.serviceSummary)
                putExtra("SERVICE_CRITERIA", it.selectionCriteria)
                putExtra("SERVICE_DEADLINE", it.applicationDeadline)
                putExtra("SERVICE_METHOD", it.applicationMethod)
                putExtra("SERVICE_CONTENT", it.supportContent)
            }
            startActivity(intent)
        }
    }

    override fun onButtonClick(position: Int) {

    }
}
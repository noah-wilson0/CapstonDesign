package com.example.welfarebenefits.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.adapter.OnItemClickListener
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.ActivityBookmarkListBinding
import com.example.welfarebenefits.entity.WelfareBookmarkList
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.BookmarkUpdater
import com.example.welfarebenefits.util.CallBackWelfareDataByBookmark
import com.example.welfarebenefits.util.JsonConverter
import com.example.welfarebenefits.util.WelfareDataFetcher

class Bookmark_listActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding : ActivityBookmarkListBinding
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private lateinit var id:String
    private lateinit var bookmarkDataList: MutableList<WelfareData>
    private lateinit var bookmarkList: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bookmarkList = WelfareBookmarkList.getBookmarkList()
        id = intent.getStringExtra("id").toString()
        if(id == "guest"){
            Log.e("BookmarkListActivity","게스트회원 사용자 북마크 조회")
            binding.recyclerView.visibility = View.GONE
        }
        else{
            binding.mosaicBackgroundBookmark.visibility = View.GONE
            if(bookmarkList.isNullOrEmpty()){
                binding.recyclerView.visibility = View.GONE
                binding.mosaicBackgroundBookmark.visibility=View.VISIBLE
                binding.mosaicBackgroundBookmark.text="북마크한 정보가 없습니다"
            }else{
                WelfareDataFetcher().getBookmarkData(object : CallBackWelfareDataByBookmark {
                    override fun getBookmarkData(welfareDataList: List<WelfareData>) {
                        bookmarkDataList = welfareDataList as MutableList<WelfareData>
                        recyclerViewAdapter = RecyclerViewAdapter(welfareDataList, this@Bookmark_listActivity)
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@Bookmark_listActivity)
                        binding.recyclerView.adapter = recyclerViewAdapter
                    }
                })
            }
        }
        binding.backArrowImgInfo.setOnClickListener{
            finish()
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
                putExtra("DATA", JsonConverter().dataToJson(it))
            }
            startActivity(intent)
        }
    }

    override fun onButtonClick(position: Int) {
        val clickedBtn = binding.recyclerView.adapter?.let { adapter ->
            if (adapter is RecyclerViewAdapter) {
                adapter.getItem(position)
            } else null
        }
        clickedBtn?.let {
            BookmarkUpdater().updateBookmark(id,it)
            bookmarkDataList.remove(it)
            if(!bookmarkDataList.isEmpty()){
                recyclerViewAdapter = RecyclerViewAdapter(bookmarkDataList, this@Bookmark_listActivity)
                binding.recyclerView.layoutManager = LinearLayoutManager(this@Bookmark_listActivity)
                binding.recyclerView.adapter = recyclerViewAdapter
            }else{
                binding.recyclerView.visibility = View.GONE
                binding.mosaicBackgroundBookmark.visibility=View.VISIBLE
                binding.mosaicBackgroundBookmark.text="북마크한 정보가 없습니다"
            }

        }
    }
}
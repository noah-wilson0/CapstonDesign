package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.adapter.OnItemClickListener
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.ActivityAlertListBinding
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.CallBackWelfareData
import com.example.welfarebenefits.util.JsonConverter
import com.example.welfarebenefits.util.WelfareDataFetcher

class AlertListActivity : AppCompatActivity(), OnItemClickListener {
    private var mBinding:ActivityAlertListBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityAlertListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent:Intent= intent
        val id:String= intent.getStringExtra("id")!!

        WelfareDataFetcher().getAlarmData(this,id,object :CallBackWelfareData{
            override fun getWelfareData(welfareDataList: List<WelfareData>) {
                Log.e("AlertListActivity",welfareDataList.size.toString())
                val recyclerViewAdapter = RecyclerViewAdapter(welfareDataList,this@AlertListActivity)
                if (recyclerViewAdapter.itemCount==0){
                    binding.alarmRecyclerView.visibility= View.GONE
                }
                else {
                    binding.mosaicText.visibility=View.GONE
                    binding.alarmRecyclerView.adapter = recyclerViewAdapter
                    binding.alarmRecyclerView.layoutManager =
                        LinearLayoutManager(this@AlertListActivity)
                    binding.alarmRecyclerView.adapter = recyclerViewAdapter
                }
            }

        })
        binding.backArrowImgAlertList.setOnClickListener {
            ActivityStarter.startNextActivityNotFinish(this,MainActivity::class.java,id)
        }



    }

    override fun onItemClick(position: Int) {
        val clickedItem = binding.alarmRecyclerView.adapter?.let { adapter ->
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
        TODO("Not yet implemented")
    }

}
package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfarebenefits.adapter.OnItemClickListener
import com.example.welfarebenefits.adapter.RecyclerViewAdapter
import com.example.welfarebenefits.databinding.ActivityAlertListBinding
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.ActivityStarter
import com.example.welfarebenefits.util.CallBackWelfareData
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
                binding.alarmRecyclerView.adapter=recyclerViewAdapter
                binding.alarmRecyclerView.layoutManager = LinearLayoutManager(this@AlertListActivity)
                binding.alarmRecyclerView.adapter = recyclerViewAdapter
            }

        })
        binding.backArrowImgAlertList.setOnClickListener {
            ActivityStarter.startNextActivityNotFinish(this,MainActivity::class.java,id)
        }



    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onButtonClick(position: Int) {
        TODO("Not yet implemented")
    }

}
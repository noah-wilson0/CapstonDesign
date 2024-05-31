package com.example.welfarebenefits.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivitySubListBinding
import com.example.welfarebenefits.util.JsonConverter

class SubListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dataString = intent.getStringExtra("DATA")
        val dataJson = JsonConverter().jsonToData(dataString!!)
        binding.servicenameTV.text = dataJson.serviceName
        binding.summaryTV.text = dataJson.serviceSummary
        if(dataJson.selectionCriteria.equals("")){
            binding.criterionTV.text = "지원 내용 참조"
        }else{
            binding.criterionTV.text = dataJson.selectionCriteria
        }
        binding.contentTV.text = dataJson.supportContent
        binding.methodTV.text = dataJson.applicationMethod
        binding.urlTV.text = dataJson.detailURL
        binding.termTV.text = dataJson.applicationDeadline

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        binding.infoLL.visibility = View.VISIBLE
                        binding.appmethodLL.visibility = View.GONE
                    }

                    1 -> {
                        binding.infoLL.visibility = View.GONE
                        binding.appmethodLL.visibility = View.VISIBLE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.urlTV.setOnClickListener{
            val urlPath = dataJson.detailURL
            val intent2 = Intent(Intent.ACTION_VIEW)
            intent2.setData(Uri.parse(urlPath))
            startActivity(intent2)

        }

        binding.backArrowImgInfo.setOnClickListener{
            finish()
        }


    }
}
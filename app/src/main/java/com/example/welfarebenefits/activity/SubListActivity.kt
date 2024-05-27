package com.example.welfarebenefits.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivitySubListBinding

class SubListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.servicenameTV.text = intent.getStringExtra("SERVICE_NAME")
        binding.summaryTV.text = intent.getStringExtra("SERVICE_SUMMARY")
        if(intent.getStringExtra("SERVICE_CRITERIA").equals("")){
            binding.criterionTV.text = "지원 내용 참조"
        }else{
            binding.criterionTV.text = intent.getStringExtra("SERVICE_CRITERIA")
        }
        binding.contentTV.text = intent.getStringExtra("SERVICE_CONTENT")
        binding.methodTV.text = intent.getStringExtra("SERVICE_METHOD")
        binding.urlTV.text = intent.getStringExtra("SERVICE_URL")
        binding.termTV.text = intent.getStringExtra("SERVICE_DEADLINE")

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
            val intent2 = Intent(Intent.ACTION_VIEW);
            intent2.setData(Uri.parse(intent.getStringExtra("SERVICE_URL")));
            startActivity(intent2);

        }

        binding.backArrowImgInfo.setOnClickListener{
            finish()
        }


    }
}
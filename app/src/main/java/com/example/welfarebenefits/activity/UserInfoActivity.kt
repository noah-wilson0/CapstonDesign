package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    private var mBinding:ActivityUserInfoBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent= Intent()
        intent=getIntent()
        Log.e("UserInfoActivity", intent.toString())

        binding.backArrowImgInfo.setOnClickListener {
            var  data=Intent(this,MainActivity::class.java)
            startActivity(data)
        }


    }
}
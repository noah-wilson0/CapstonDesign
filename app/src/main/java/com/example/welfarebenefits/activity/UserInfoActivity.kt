package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivityUserInfoBinding
import com.example.welfarebenefits.util.JsonConverter


/*
    회원탈퇴하기 버튼 추가
 */
class UserInfoActivity : AppCompatActivity() {
    private var mBinding:ActivityUserInfoBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent=Intent()
        intent=getIntent()

        val userJson=intent.getStringExtra("user")
        val user = JsonConverter().jsonToUser(userJson!!)
        Log.e("UserInfoActivity", user.toString())

        binding.backArrowImgInfo.setOnClickListener {
            var  data=Intent(this,MainActivity::class.java)
            startActivity(data)
        }


    }
}

package com.example.welfarebenefits.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.welfarebenefits.databinding.ActivityUserInfoBinding
import com.example.welfarebenefits.util.JsonConverter


/*
    회원탈퇴하기 버튼 추가
    ui수정해야될듯(게스트,회원간 화면을 어떻게 할건지?=> 1.layout을 따로 연결시킬까?2.회원일 경우 가림막과"회원가입하시겠습니까?"텍스트 devisible하기)
 */
class UserInfoActivity : AppCompatActivity() {
    private var mBinding:ActivityUserInfoBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("UserInfoActivity","UserInfoActivity도착")
        super.onCreate(savedInstanceState)
        mBinding= ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent=Intent()
        intent=getIntent()
        if(intent.getStringExtra("user").isNullOrEmpty()){
            Log.e("UserInfoActivity","게스트회원 사용자 정보 조회")
        }
        else {
            Log.e("UserInfoActivity","회원 사용자 정보 조회")
            binding.mosaicBackgroundInfo.visibility= View.GONE
            binding.mosaicTextviewInfo.visibility=View.GONE
            val userJson = intent.getStringExtra("user")
            val user = JsonConverter().jsonToUser(userJson!!)
            Log.e("UserInfoActivity", user.toString())

            binding.idInfo.text = user.id
            binding.passwordInfo.text = user.password
            binding.nameInfo.text = user.name
            binding.avgIncomeInfo.text = user.avgIncome
            binding.familyStructureInfo.text = user.familyStructure
            binding.residencInfo.text = user.residence
            binding.significantInfo.text = user.significant.joinToString(", ")
        }

        binding.backArrowImgInfo.setOnClickListener {
            var  data=Intent(this,MainActivity::class.java)
            startActivity(data)
        }


    }
}

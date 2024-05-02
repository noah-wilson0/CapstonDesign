package com.example.welfarebenefits.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.welfarebenefits.R
import com.example.welfarebenefits.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {
    private var mBinding:ActivityLogInBinding?=null
    private val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
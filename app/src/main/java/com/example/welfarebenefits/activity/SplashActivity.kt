package com.example.welfarebenefits.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.welfarebenefits.R
import com.example.welfarebenefits.entity.WelfareData
import com.example.welfarebenefits.util.ActivityStarter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var welfareDataList: List<WelfareData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000)

    }
    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("userId", null)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (id!=null&&currentUser != null) {
            Log.e("Splash","이미로그인한유저:자동로그인진행")
            ActivityStarter.startNextActivity(this@SplashActivity, MainActivity::class.java,id)
        } else {
            Log.e("Splash","로그인화면으로분기")
            // 로그인되지 않은 상태면 로그인 화면으로 이동
            ActivityStarter.startNextActivity(this@SplashActivity, LogInActivity::class.java)
        }
    }

}
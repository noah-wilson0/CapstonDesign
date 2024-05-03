package com.example.welfarebenefits.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.welfarebenefits.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Intent
import com.example.welfarebenefits.util.ShowAlertDialog

class LogInActivity : AppCompatActivity() {
    private var mBinding:ActivityLogInBinding?=null
    private val binding get() = mBinding!!
    private lateinit var auth: FirebaseAuth
    var userId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.loginbutton.setOnClickListener {
            when {
                //회원이 아닌경우 처리문 추가하기

                binding.idET.text.toString().isBlank()&&binding.idET.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this,"로그인","아이디를 입력해 주세요!","확인").showAlertDialog()
                }
                binding.passwdET.text.toString().isBlank()&&binding.passwdET.text.toString().isNullOrEmpty() -> {
                    ShowAlertDialog(this,"로그인","비밀번호를 입력해 주세요!","확인").showAlertDialog()
                }
                binding.idET.text.toString().isNotBlank()&&binding.passwdET.text.toString().isNotBlank() ->{
                    //로그인 처리 로직 구현

                    finish()
                }
                else -> {
                    finish()
                    Log.e("LOGIN","로그인 오류 발생")
                }
            }
        }

        binding.singUpTV.setOnClickListener {
            Log.e("LOGIN","문제")
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.gestLogIn.setOnClickListener{ guestLogin() }

    }

    //다음에 앱을 실행하면 메인화면으로 넘어가게해야됨
    private fun guestLogin() {
        val user=auth.currentUser
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish()
        } else {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java);
                        startActivity(intent)
                        finish()

                    } else {
                        Log.e("GUESTLOGIN","익명 로그인 실패")
                    }
                }

        }

    }




}